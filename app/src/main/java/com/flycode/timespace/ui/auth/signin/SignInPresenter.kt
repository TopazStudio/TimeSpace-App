package com.flycode.timespace.ui.auth.signin


import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.flycode.timespace.LoginMutation
import com.flycode.timespace.R
import com.flycode.timespace.data.models.LoginPayload
import com.flycode.timespace.data.models.Response
import com.flycode.timespace.data.models.User
import com.flycode.timespace.data.network.AuthService
import com.flycode.timespace.ui.auth.signup.SignUpFragment
import com.flycode.timespace.ui.auth.signup.UserDetails.UserDetailsFragment
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignInPresenter(
        val apolloClient: ApolloClient,
        var authService: AuthService,
        val sharedPreferences: SharedPreferences,
        var googleSignInClient: GoogleSignInClient
) : BasePresenter<SignInFragment,SignInPresenter,SignInViewModel>(),
        SignInContract.SignInPresenter<SignInFragment>{

    override fun login(){
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
                                                    ,LoginPayload::class.java
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

    private fun loginUser(): ApolloMutationCall<LoginMutation.Data> {
        return apolloClient.mutate(
                LoginMutation.builder()
                        .email(viewModel.credentials.email)
                        .password(viewModel.credentials.password)
                        .build()
        )
    }

    override fun signInWithGoogle() {
        view?.showLoading()
        val signInIntent = googleSignInClient.signInIntent
        view?.startActivityForResult(signInIntent, UserDetailsFragment.SIGN_IN_BY_GOOGLE_RESPONSE_CODE)
    }

    override fun signInWithFacebookCallback(): FacebookCallback<LoginResult> {
        return object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                sendFacebookAuthToken(loginResult.accessToken.token)
            }

            override fun onCancel() {

            }

            override fun onError(exception: FacebookException) {
                view?.hideLoading()
                if (exception.message != null){
                    view?.showError(message = exception.message.toString())
                }else{
                    view?.showError("Something went wrong. Please try again.")
                }
            }
        }
    }

    override fun handleSignInWithGoogleResult(task: Task<GoogleSignInAccount>?) {
        try {
            task?.getResult(ApiException::class.java)!!.let {
                sendGoogleIdToken(it.idToken!!)
            }
        } catch (e: ApiException) {
            if (e.message != null){
                view?.hideLoading()
                view?.showError(message = e.message.toString())
            }else{
                view?.showError("We were unable to authenticate you. Please try again.")
            }
        }

    }

    private fun sendFacebookAuthToken(authToken: String){
        sendAuthToServer(authService.signInWithFacebook(LoginPayload(authToken, User())),"facebook")
    }

    private fun sendGoogleIdToken(idToken: String){
        sendAuthToServer(authService.signInWithGoogle(LoginPayload(idToken, User())),"google")
    }

    private fun sendAuthToServer(observable : Observable<Response<LoginPayload>>, type : String = "google"){
        view?.let {view ->
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        if (it.message == "registration_required") {
                            val args = Bundle().apply {
                                this.putSerializable(SignUpFragment.UN_REGISTERED_USER,it.data.user)
                                this.putString(SignUpFragment.EXTERNAL_AUTH_TYPE,type)
                            }
                            NavHostFragment.findNavController(view).navigate(R.id.signUpFragment,args)


                        } else {
                            autoRegister(it.data)
                        }
                    },{
                        view.hideLoading()
                        if (it.message != null){
                            view.showError(message = it.message.toString())
                        }else{
                            view.showError("Something went wrong. Please try again.")
                        }
                    })
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
            view?.showMessage("Successfully Authenticated")
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