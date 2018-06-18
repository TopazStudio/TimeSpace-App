package com.flycode.timespace.ui.auth.signup

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.Bitmap
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel

open class SignUpViewModel
    : BaseViewModel<SignUpFragment, SignUpPresenter>(){
    val user : User = User()
    var imagePath: String? = null
    var imageBitmap: Bitmap? = null
    var doImageSave: Boolean = false
//    var googleLoginSuccess : Boolean = UiState.googleLoginSuccess
//    var facebookLoginSuccess : Boolean = UiState.facebookLoginSuccess
    val uiState = UiState()

    class UiState : BaseObservable(){
        @get: Bindable
        var googleLoginSuccess: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var facebookLoginSuccess: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }
    }
}
