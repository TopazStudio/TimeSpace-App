package com.flycode.timespace.ui.main.groups.groupsView

import com.flycode.timespace.ui.base.BaseContract

interface GroupViewContract {
    interface GroupViewFragment : BaseContract.View{

    }

    interface GroupViewPresenter<V : GroupViewFragment> : BaseContract.Presenter<V>{
        fun init()
        fun registerGroupServiceStartedBroadcast()
        fun unRegisterGroupServiceStartedBroadcast()
        fun syncWithGroupService()
        fun unSyncWithGroupService()
        fun startGroupService()
    }
}