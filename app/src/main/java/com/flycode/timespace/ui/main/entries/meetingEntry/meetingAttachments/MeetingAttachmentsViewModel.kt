package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees.MeetingAttendeesViewModel

class MeetingAttachmentsViewModel
    : BaseViewModel<MeetingAttachmentsFragment, MeetingAttachmentsPresenter>() {
    val uiState = MeetingAttendeesViewModel.UiState()
    class UiState : BaseObservable(){
        @get: Bindable
        var isMainLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onMainError: Boolean = true
            set(value) {
                field = value
                notifyChange()
            }
    }
}