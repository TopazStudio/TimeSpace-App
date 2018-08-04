package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem

class MeetingAttachmentsViewModel
    : BaseViewModel<MeetingAttachmentsFragment, MeetingAttachmentsPresenter>() {
    val uiState = UiState()
    var headingsList: MutableList<PlainHeaderItem> = ArrayList()

    class UiState : BaseObservable(){
        @get: Bindable
        var showEmptyAttachmentsHint: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isMainLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onMainError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }
    }
}