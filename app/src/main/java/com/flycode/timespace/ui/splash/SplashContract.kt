package com.flycode.timespace.ui.splash

import com.flycode.timespace.ui.base.BaseContract

interface SplashContract : BaseContract{
    interface SplashActivity : BaseContract.View
    interface SplashPresenter<V : SplashActivity> : BaseContract.Presenter<V>{
        fun startCounting()
    }
}