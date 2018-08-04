package com.flycode.timespace.ui.auth.signup.UserDetails

import android.content.SharedPreferences
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.flycode.timespace.data.models.LoginPayload
import com.flycode.timespace.data.models.Response
import com.flycode.timespace.data.models.User
import com.flycode.timespace.data.network.retrofit.AuthService
import com.flycode.timespace.ui.auth.signup.SignUpViewModel
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserDetailsPresenter(
        var authService: AuthService,
        var superViewModel: SignUpViewModel,
        val sharedPreferences: SharedPreferences,
        var googleSignInClient: GoogleSignInClient
) : BasePresenter<UserDetailsFragment, UserDetailsPresenter, UserDetailsViewModel>(),
        UserDetailsContract.UserDetailsPresenter<UserDetailsFragment>{

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
                superViewModel.uiState.facebookLoginSuccess = false
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

    private fun sendAuthToServer(observable : Observable<Response<LoginPayload>>,type : String = "google"){
        view?.let {view ->
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        if (it.message == "registration_required") {
                            //GET DATA FROM GOOGLE
                            superViewModel.user.apply {
                                this.first_name = it.data.user.first_name
                                this.second_name = it.data.user.second_name
                                this.surname = it.data.user.surname
                                this.email = it.data.user.email
                                this.tel = it.data.user.tel
                                this.password = "000000"
                                this.pictures.apply {
                                    this.add(com.flycode.timespace.data.models.Picture().apply {
                                        this.remote_location = it.data.user.pictures[0].remote_location
                                    })
                                }
                            }
                            view.hideLoading()
                            view.showMessage("Successfully Authenticated")
                            when (type){
                                "google" -> superViewModel.uiState.googleLoginSuccess = true
                                "facebook" -> superViewModel.uiState.facebookLoginSuccess = true
                            }

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
            if (loginPayload.user.save()){
                it.onNext(true)
            }else it.onError(Throwable("Something Went Wrong"))
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            view?.hideLoading()
            view?.showMessage("Successfully Authenticated")
            view?.navigateToActivity(view?.context,MainActivity::class.java)

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