package com.flycode.timespace.ui.main.groups.groupsEntry

import com.flycode.timespace.ui.base.BaseContract

interface GroupEntryContract {
    interface GroupEntryFragment : BaseContract.View{

    }

    interface GroupEntryPresenter<V : GroupEntryFragment> : BaseContract.Presenter<V>{

    }
}