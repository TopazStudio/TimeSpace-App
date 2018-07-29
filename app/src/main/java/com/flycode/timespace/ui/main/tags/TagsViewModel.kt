package com.flycode.timespace.ui.main.tags

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.ui.base.BaseViewModel

class TagsViewModel: BaseViewModel<TagsFragment, TagsPresenter>() {
    val uiState = UiState()

    class UiState : BaseObservable(){
        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }
    }
}