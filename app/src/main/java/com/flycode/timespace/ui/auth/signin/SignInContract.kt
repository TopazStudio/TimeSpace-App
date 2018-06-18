package com.flycode.timespace.ui.auth.signin

import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.flycode.timespace.data.models.Credentials
import com.flycode.timespace.ui.base.BaseContract
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

interface SignInContract {
    interface SignInFragment : BaseContract.View
    interface SignInPresenter<V : SignInFragment> : BaseContract.Presenter<V>{
        fun login()
        fun signInWithGoogle()
        fun signInWithFacebookCallback(): FacebookCallback<LoginResult>
        fun handleSignInWithGoogleResult(task: Task<GoogleSignInAccount>?)
    }
    interface SignInViewModel<V : SignInFragment, P : SignInPresenter<V>> : BaseContract.ViewModel<V,P>{
        val credentials : Credentials
    }
}