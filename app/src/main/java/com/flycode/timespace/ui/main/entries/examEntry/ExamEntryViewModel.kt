package com.flycode.timespace.ui.main.entries.examEntry

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.Examination
import com.flycode.timespace.data.models.Location
import com.flycode.timespace.data.models.Time
import com.flycode.timespace.ui.base.BaseViewModel
import java.util.*

class ExamEntryViewModel
    : BaseViewModel<ExamEntryFragment, ExamEntryPresenter>() {
    var startTime : Calendar = Calendar.getInstance()
    var endTime : Calendar = Calendar.getInstance()

    val uiState = UiState()

    class UiState : BaseObservable(){
        var examination: Examination = Examination()
        var location: Location = Location()
        var time: Time = Time()

        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isEmptyTimeHidden: Boolean = false
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

        @get: Bindable
        var isTagsLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        var onTagsError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

    }
}