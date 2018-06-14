package com.flycode.timespace.ui.main.timetable.weeklyview

import com.alamkanak.weekview.WeekViewEvent
import com.flycode.timespace.ui.base.BaseContract

interface WeeklyViewContract {
    interface WeeklyViewFragment : BaseContract.View {

    }
    interface WeeklyViewPresenter<V : WeeklyViewContract.WeeklyViewFragment> : BaseContract.Presenter<V>{
        fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent>
    }
}