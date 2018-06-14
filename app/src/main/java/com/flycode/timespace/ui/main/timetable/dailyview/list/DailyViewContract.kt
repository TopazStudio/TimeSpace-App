package com.flycode.timespace.ui.main.timetable.dailyview.list

import android.content.Context
import com.flycode.timespace.ui.base.BaseContract
import com.test.tudou.library.model.CalendarDay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible

interface DailyViewContract {
    interface DailyViewList : BaseContract.View{
        fun setupRecyclerView(adapter: FlexibleAdapter<IFlexible<*>>,layoutManager: SmoothScrollLinearLayoutManager)
        fun getFragmentContext(): Context?
    }
    interface DailyViewPresenter<V : DailyViewList> : BaseContract.Presenter<V>{
        fun fetchItems(mCalendarDay: CalendarDay)
    }
}