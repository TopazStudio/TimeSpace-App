package com.flycode.timespace.ui.main

import com.flycode.timespace.ui.base.BaseContract

interface MainContract {
    interface MainActivity : BaseContract.View
    interface MainPresenter<V : MainActivity> : BaseContract.Presenter<V>{
        fun init()
        fun registerUserServiceStartedBroadcast()
        fun unRegisterUserServiceStartedBroadcast()
        fun syncWithUserService()
        fun unSyncWithUserService()
        fun startUserService()
    }
}
