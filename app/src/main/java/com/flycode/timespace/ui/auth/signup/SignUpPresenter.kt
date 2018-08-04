package com.flycode.timespace.ui.auth.signup

import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.musclemax_app.util.FileUtils
import com.flycode.timespace.LoginMutation
import com.flycode.timespace.RegisterUserMutation
import com.flycode.timespace.RegisterUserWithPicturesMutation
import com.flycode.timespace.data.models.LoginPayload
import com.flycode.timespace.data.models.Picture
import com.flycode.timespace.data.models.Response
import com.flycode.timespace.data.models.User
import com.flycode.timespace.data.models.apolloMappers.UserMapper
import com.flycode.timespace.data.network.retrofit.TempService
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.main.MainActivity
import com.google.gson.Gson
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignUpPresenter(
        val tempService: TempService,
        val apolloClient: ApolloClient,
        val sharedPreferences: SharedPreferences
) : BasePresenter<SignUpFragment, SignUpPresenter, SignUpViewModel>(),
        SignUpContract.SignUpPresenter<SignUpFragment>{

    override fun onFinish() {
        view?.let{view ->
            view.showLoading()
            if (viewModel.doImageSave && !viewModel.signedUp) {
                compositeDisposable.add(
                        saveImageLocally() //SAVE PROFILE PIC LOCALLY
                                .flatMap {
                                    //SAVE PROFILE PIC REMOTELY TEMPORALLY
                                    prepareImageUpload()
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                }
                                .flatMap {
                                    it.data.apply {
                                        this.name = "profile_pic"
                                        this.description = "Profile picture of user."
                                        viewModel.user.pictures.clear()
                                        viewModel.user.pictures.add(this)
                                    }

                                    //SAVE THE USER TOGETHER WITH PICTURES REMOTELY
                                    Rx2Apollo.from(registerUserWithPictures())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                }
                                .flatMap {
                                    if (it.data()?.user() == null){
                                        Observable.error(Throwable(it.errors()[0].message()))
                                    }else{
                                        val user : User = Gson().fromJson(Gson().toJson(it.data()?.user()),User::class.java)
                                        user.pictures[0].local_location = viewModel.imagePath!!
                                        user.password = viewModel.user.password
                                        saveUserLocally(user) //SAVE THE USER TOGETHER WITH PICTURES LOCALLY
                                    }
                                }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe ({
                                    if (it == true) {
                                        viewModel.signedUp = true
                                        login() //After signing up. Login user
                                    } else {
                                        view.showError("Sorry, something went wrong. Please try again.")
                                    }
                                },{
                                    view.hideLoading()
                                    viewModel.signedUp = false
                                    if (it.message != null){
                                        view.showError(message = it.message.toString())
                                    }else{
                                        view.showError("Something went wrong. Please try again.")
                                    }
                                })
                )
            } else if(viewModel.signedUp){
                login() //IF ALL READY SIGNED UP. JUST SIGN IN
            }  else {
                compositeDisposable.add(
                        Rx2Apollo.from(registerUser())
                                .flatMap {
                                    if (it.data()?.user() == null){
                                        Observable.error(Throwable(it.errors()[0].message()))
                                    }else{
                                        val user : User = Gson().fromJson(Gson().toJson(it.data()?.user()),User::class.java)
                                        user.password = viewModel.user.password
                                        saveUserLocally(user) //SAVE THE USER TOGETHER WITH PICTURES LOCALLY
                                    }
                                }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe ({
                                    if (it == true) {
                                        viewModel.signedUp = true
                                        login()
                                    }
                                    else {
                                        view.showError("Sorry, something went wrong. Please try again.")
                                    }
                                },{
                                    view.hideLoading()
                                    viewModel.signedUp = false
                                    if (it.message != null){
                                        view.showError(message = it.message.toString())
                                    }else{
                                        view.showError("Something went wrong. Please try again.")
                                    }
                                })
                )
            }
        }
    }

    private fun registerUser(): ApolloMutationCall<RegisterUserMutation.Data> {

        return apolloClient.mutate(
                    RegisterUserMutation
                            .builder()
                            .user(UserMapper.mapUserToUserInput(viewModel.user))
                            .password(viewModel.user.password)
                            .build()
                )
    }

    private fun registerUserWithPictures(): ApolloMutationCall<RegisterUserWithPicturesMutation.Data>{
        return apolloClient.mutate(
                RegisterUserWithPicturesMutation
                        .builder()
                        .user(UserMapper.mapUserToUserInput(viewModel.user))
                        .password(viewModel.user.password)
                        .pictures(viewModel.user.pictures.map {
                            UserMapper.mapPictureToPictureInput(picture = it)
                        })
                        .build()
        )
    }

    private fun prepareImageUpload(): Observable<Response<Picture>>{
        val file = File(viewModel.imagePath!!)
        val reqFile = RequestBody.create(MediaType.parse("image/jpeg"), FileUtils.readFileToBytes(viewModel.imagePath!!))
        val image = MultipartBody.Part.createFormData("tempImage", file.name, reqFile)

        return tempService.tempSaveImage(image)
    }

    private fun saveImageLocally() : Observable<String>{
        return Observable.create { emitter ->
            //SAVE NEW IMAGE
            viewModel.imageBitmap?.let {
                FileUtils.saveImage(view?.context!!,it, "/user_photos","profile_pic.jpg")
            }.apply {
                if (this != null){
                    viewModel.imagePath = this
                    emitter.onNext(this)
                    emitter.onComplete()
                }
                else emitter.onError(Throwable("Saving Image failed. Please try again."))
            }
            emitter.onComplete()
        }
    }

    private fun saveUserLocally(user: User) : Observable<Boolean>{
        return Observable.just(user.save())
    }

    private fun loginUser(): ApolloMutationCall<LoginMutation.Data> {
        return apolloClient.mutate(
                LoginMutation.builder()
                        .email(viewModel.user.email)
                        .password(viewModel.user.password)
                        .build()
        )
    }

    private fun login(){
        view?.let { view ->
            compositeDisposable.add(
                    Rx2Apollo.from(loginUser())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it?.data()?.let {it1 ->
                                    val loginPayload : LoginPayload = Gson()
                                            .fromJson(
                                                    Gson().toJson(it1.login()!!)
                                                    , LoginPayload::class.java
                                            )
                                    autoRegister(loginPayload)
                                }
                            },{
                                view.hideLoading()
                                if (it.message != null){
                                    view.showError(message = it.message.toString())
                                }else{
                                    view.showError("Something went wrong. Please try again.")
                                }
                            })
            )
        }
    }

    private fun autoRegister(loginPayload: LoginPayload){
        Observable.create<Boolean> {
            sharedPreferences.edit().putString("token",loginPayload.token).apply()
            loginPayload.user._tag = "default_user"
            if (loginPayload.user.save()){
                it.onNext(true)
            }else it.onError(Throwable("Something Went Wrong"))
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            view?.hideLoading()
            view?.showMessage("All Done!")

            //Deep link to orient user
            view?.navigateToActivity(view?.context, MainActivity::class.java)

        },{
            view?.hideLoading()
            if (it.message != null){
                view?.showError(message = it.message.toString())
            }else{
                view?.showError("Something went wrong. Please try again.")
            }
        })
    }
}