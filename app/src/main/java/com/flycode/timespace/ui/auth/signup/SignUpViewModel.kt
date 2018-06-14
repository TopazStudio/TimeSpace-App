package com.flycode.timespace.ui.auth.signup

import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel

class SignUpViewModel
    : BaseViewModel<SignUpFragment, SignUpPresenter>(){
    val user : User = User()
}
