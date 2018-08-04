package com.flycode.timespace.ui.main.entries.meetingEntry

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.*
import com.flycode.timespace.ui.base.BaseViewModel
import java.util.*
import kotlin.collections.ArrayList

class MeetingEntryViewModel
    : BaseViewModel<MeetingEntryFragment, MeetingEntryPresenter>() {
    val uiState = UiState()
    var attendees: MutableList<User> = ArrayList()
    val tagList: MutableList<Tag> = ArrayList()
    val documents: MutableList<Document> = ArrayList()
    var startTime : Calendar = Calendar.getInstance()
    var endTime : Calendar = Calendar.getInstance()

    class UiState : BaseObservable(){
        var meeting: Meeting = Meeting()
        var location: Location = Location()
        var time: Time = Time()

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