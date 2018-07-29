package com.flycode.timespace.ui.main.entries.meetingEntry

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.Location
import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.data.models.Time
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel

class MeetingEntryViewModel
    : BaseViewModel<MeetingEntryFragment, MeetingEntryPresenter>() {
    val uiState = UiState()
    var attendees: MutableList<User> = java.util.ArrayList()

    class UiState : BaseObservable(){
        var meeting: Meeting = Meeting()
        var location: Location = Location()
        var times: MutableList<Time> = ArrayList()

        @get: Bindable
        var isEmptyTagsHidden: Boolean = true
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

        @get: Bindable
        var isLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }
    }
}