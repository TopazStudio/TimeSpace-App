package com.flycode.timespace.ui.main.timetable.dailyview.list

import com.flycode.timespace.ui.base.BaseContract
import com.test.tudou.library.model.CalendarDay

interface DailyViewContract {
    interface DailyViewList : BaseContract.View{
    }
    interface DailyViewPresenter<V : DailyViewList> : BaseContract.Presenter<V>{
    }
}