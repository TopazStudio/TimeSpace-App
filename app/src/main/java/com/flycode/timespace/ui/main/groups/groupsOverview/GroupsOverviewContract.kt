package com.flycode.timespace.ui.main.groups.groupsOverview

import com.flycode.timespace.ui.base.BaseContract

class GroupsOverviewContract {
    interface OrientationFragment : BaseContract.View{

    }

    interface OrientationPresenter<V : OrientationFragment> : BaseContract.Presenter<V>{

    }
}