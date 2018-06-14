package com.flycode.timespace.ui.auth.signin

import com.flycode.timespace.data.models.Credentials
import com.flycode.timespace.ui.base.BaseViewModel

class SignInViewModel: BaseViewModel<SignInFragment, SignInPresenter>()
        , SignInContract.SignInViewModel<SignInFragment,SignInPresenter>{

    override val credentials: Credentials = Credentials()
}
