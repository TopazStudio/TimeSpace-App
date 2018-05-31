package com.flycode.timespace.ui.auth.signin

import com.flycode.timespace.ui.base.MvpView

interface SignInContract {
    interface SignInFragment : MvpView
    interface SignInPresenter<SignInFragment>
}