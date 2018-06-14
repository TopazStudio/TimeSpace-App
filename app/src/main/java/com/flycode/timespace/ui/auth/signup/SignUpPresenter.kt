package com.flycode.timespace.ui.auth.signup

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.musclemax_app.data.models.Response
import com.flycode.musclemax_app.util.FileUtils
import com.flycode.timespace.RegisterUser
import com.flycode.timespace.RegisterUserWithPictures
import com.flycode.timespace.data.models.Picture
import com.flycode.timespace.data.models.User
import com.flycode.timespace.data.models.apolloMappers.UserMapper
import com.flycode.timespace.data.network.TempService
import com.flycode.timespace.ui.base.BasePresenter
import com.google.gson.Gson
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream

class SignUpPresenter(
        val tempService: TempService,
        val apolloClient: ApolloClient
) : BasePresenter<SignUpFragment, SignUpPresenter, SignUpViewModel>(),
        SignUpContract.SignUpPresenter<SignUpFragment>{

    override var imageBitmap: Bitmap? = null
    override var doImageSave: Boolean = false
    private var imagePath: String? = null

    override fun signUp() {
        view?.let{view ->
            view.showLoading()
            if (doImageSave)
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
                                        user.pictures[0].local_location = imagePath!!
                                        user.password = viewModel.user.password
                                        saveUserLocally(user) //SAVE THE USER TOGETHER WITH PICTURES LOCALLY
                                    }
                                }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe ({
                                    view.hideLoading()
                                    if (it == true) {
                                        view.showMessage("Successfully Registered")
                                    } else {
                                        view.showError("Sorry, something went wrong. Please try again.")
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

            else
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
                                view.hideLoading()
                                if (it == true) view.showMessage("Successfully Registered")
                                else view.showError("Sorry, something went wrong. Please try again.")
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

    private fun registerUser(): ApolloMutationCall<RegisterUser.Data> {

        return apolloClient.mutate(
                    RegisterUser
                            .builder()
                            .user(UserMapper.mapUserToUserInput(viewModel.user))
                            .password(viewModel.user.password)
                            .build()
                )
    }

    private fun registerUserWithPictures(): ApolloMutationCall<RegisterUserWithPictures.Data>{
        return apolloClient.mutate(
                RegisterUserWithPictures
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
        val file = File(imagePath!!)
        val reqFile = RequestBody.create(MediaType.parse("image/jpeg"), FileUtils.readFileToBytes(imagePath!!))
        val image = MultipartBody.Part.createFormData("tempImage", file.name, reqFile)

        return tempService.tempSaveImage(image)
    }

    private fun saveImageLocally() : Observable<String>{
        return Observable.create { emitter ->
            //SAVE NEW IMAGE
            imageBitmap?.let {
                FileUtils.saveImage(view?.context!!,it, "/user_photos","profile_pic.jpg")
            }.apply {
                if (this != null){
                    imagePath = this
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

    fun onClearImageBitmap(){
        imageBitmap = null
    }

    override fun onCaptureImageResult(data: Intent) {
        view?.let { view ->
            view.showProgressBar()
            compositeDisposable.add(
                    Observable.create(ObservableOnSubscribe<String> { emitter ->
                        imageBitmap = data.extras.get("data") as Bitmap
                        view.setPhotoProgress(100)
                        emitter.onComplete()
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({

                            },{ throwable ->
                                view.showError(throwable.message!!)
                            },{
                                view.setImageBitmap(imageBitmap!!)
                                doImageSave = true
                                view.hideProgressBar()
                            })
            )
        }
    }

    override fun onPickerImageResult(data: Intent) {
        view?.let { view ->
            view.showProgressBar()
            compositeDisposable.add(
                    Observable.create(ObservableOnSubscribe<String> { emitter ->
                        val imageUri = data.data
                        view.setPhotoProgress(30)

                        val imageStream: InputStream = view.getContentResolver().openInputStream(imageUri)!!

                        view.setPhotoProgress(50)

                        imageBitmap = BitmapFactory.decodeStream(imageStream)

                        view.setPhotoProgress(100)
                        emitter.onComplete()
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({

                            },{
                                throwable -> view.showError(throwable.message!!)
                            },{
                                view.setImageBitmap(imageBitmap!!)
                                doImageSave = true
                                view.hideProgressBar()
                            })
            )
        }
    }
}