package com.flycode.timespace.ui.main.groups.groupsView.groupTimeTables

import com.flycode.timespace.ui.base.BaseContract

interface GroupTimetablesContract {
    interface GroupTimetablesFragment : BaseContract.View{

    }

    interface GroupTimetablesPresenter<V : GroupTimetablesFragment> : BaseContract.Presenter<V>{

    }
}