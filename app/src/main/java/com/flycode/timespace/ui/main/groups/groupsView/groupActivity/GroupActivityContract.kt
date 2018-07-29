package com.flycode.timespace.ui.main.groups.groupsView.groupActivity

import com.flycode.timespace.ui.base.BaseContract

interface GroupActivityContract {
    interface GroupActivityFragment : BaseContract.View{

    }

    interface GroupActivityPresenter<V : GroupActivityFragment> : BaseContract.Presenter<V>{

    }
}