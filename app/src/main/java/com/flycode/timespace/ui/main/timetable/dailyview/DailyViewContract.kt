package com.flycode.timespace.ui.main.timetable.dailyview

import com.flycode.timespace.ui.base.MvpView
import com.test.tudou.library.model.CalendarDay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

interface DailyViewContract {
    interface DailyViewList : MvpView{
        fun setupRecyclerView(adapter: FlexibleAdapter<IFlexible<*>>)
    }
    interface DailyViewPresenter<V : DailyViewList>{
        fun fetchItems(mCalendarDay: CalendarDay)
    }
}