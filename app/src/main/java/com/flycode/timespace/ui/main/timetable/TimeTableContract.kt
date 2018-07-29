package com.flycode.timespace.ui.main.timetable

import com.flycode.timespace.ui.base.BaseContract

interface TimeTableContract {
    interface TimeTableFragment : BaseContract.View{

    }

    interface TimeTablePresenter<V : TimeTableFragment> : BaseContract.Presenter<V>{

    }
}