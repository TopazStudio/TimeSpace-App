package com.flycode.timespace.ui.main

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel

class MainViewModel
    : BaseViewModel<MainActivity, MainPresenter>(){
    var defaultUser = User()
    val uiState = UiState()
    class UiState : BaseObservable(){
        @get: Bindable
        var isLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

    }
}