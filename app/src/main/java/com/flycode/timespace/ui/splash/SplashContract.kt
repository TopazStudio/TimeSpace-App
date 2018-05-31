package com.flycode.timespace.ui.splash

import com.flycode.timespace.ui.base.MvpView

interface SplashContract {
    interface SplashActivity : MvpView
    interface SplashPresenter<V : SplashActivity>{
        fun startCounting()
    }
}