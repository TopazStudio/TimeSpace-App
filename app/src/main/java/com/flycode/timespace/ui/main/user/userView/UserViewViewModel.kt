package com.flycode.timespace.ui.main.user.userView

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel

class UserViewViewModel
    : BaseViewModel<UserViewFragment, UserViewPresenter>(){
    var lastTextEdit: Long = 0

    val uiState = UiState()

    class UiState : BaseObservable(){
        var user: User = User()

        @get: Bindable
        var memberSince: String = "3 years ago"
            set(value) {
                field = value
                notifyChange()
            }

        fun getDisplayName(): String{
            return "${user.first_name} ${user.second_name} ${user.surname}"
        }
    }
}