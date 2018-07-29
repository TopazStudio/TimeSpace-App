package com.flycode.timespace.ui.main.timetable.weeklyview

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.alamkanak.weekview.WeekViewEvent
import com.flycode.timespace.data.models.Clazz
import com.flycode.timespace.data.models.Examination
import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.ui.base.BaseViewModel

class WeeklyViewViewModel: BaseViewModel<WeeklyViewFragment, WeeklyViewPresenter>(){
    var weekViewEvents: MutableList<WeekViewEvent> = ArrayList()
    var meetings: MutableList<Meeting> = ArrayList()
    var examinations: MutableList<Examination> = ArrayList()
    var clazzes: MutableList<Clazz> = ArrayList()
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
