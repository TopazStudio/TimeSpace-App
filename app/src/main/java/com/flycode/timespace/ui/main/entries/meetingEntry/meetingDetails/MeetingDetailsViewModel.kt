package com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.main.tags.TagsEntryUiState

class MeetingDetailsViewModel
    : BaseViewModel<MeetingDetailsFragment, MeetingDetailsPresenter>() {

    val uiState = UiState()
    val tagsEntryUiState = TagsEntryUiStateImpl()

    class UiState : BaseObservable(){
        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isEmptyTagsHidden: Boolean = false
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

    class TagsEntryUiStateImpl :  BaseObservable(), TagsEntryUiState{
        @get: Bindable
        override var isEmptyTagsHidden: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        override var isTagsLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        override var onTagsError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }
    }
}