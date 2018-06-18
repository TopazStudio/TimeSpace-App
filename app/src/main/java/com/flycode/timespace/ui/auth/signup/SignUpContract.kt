package com.flycode.timespace.ui.auth.signup

import com.flycode.timespace.ui.base.BaseContract

interface SignUpContract {
    interface SignUpFragment : BaseContract.View{
        fun onFinish()
        fun onSignIn()
    }
    interface SignUpPresenter<V : SignUpFragment> : BaseContract.Presenter<V>{
        fun onFinish()
    }
}