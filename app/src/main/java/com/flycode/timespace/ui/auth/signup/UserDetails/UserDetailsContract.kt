package com.flycode.timespace.ui.auth.signup.UserDetails

import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.flycode.timespace.ui.base.BaseContract
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

interface UserDetailsContract{
    interface UserDetailsFragment : BaseContract.View
    interface UserDetailsPresenter<V : UserDetailsContract.UserDetailsFragment> : BaseContract.Presenter<V>{
        fun signInWithGoogle()
        fun signInWithFacebookCallback(): FacebookCallback<LoginResult>
        fun handleSignInWithGoogleResult(task: Task<GoogleSignInAccount>?)
    }
}