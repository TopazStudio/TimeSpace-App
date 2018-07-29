package com.flycode.timespace.ui.main.timetable.dailyview.list

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.Clazz
import com.flycode.timespace.data.models.Examination
import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem

class DailyViewViewModel
    : BaseViewModel<DailyViewList, DailyViewPresenter>(){

    var headingsList = java.util.ArrayList<PlainHeaderItem>()
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