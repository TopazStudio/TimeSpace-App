package com.flycode.timespace.ui.main.timetable.weeklyview

import com.alamkanak.weekview.WeekViewEvent
import com.flycode.timespace.ui.base.BasePresenter

class WeeklyViewPresenter
    : BasePresenter<WeeklyViewFragment,WeeklyViewPresenter,WeeklyViewViewModel>() ,
        WeeklyViewContract.WeeklyViewPresenter<WeeklyViewFragment> {

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent>{
        return MutableList(3){WeekViewEvent()}
    }
}