package com.flycode.timespace.ui.base

import com.flycode.timespace.di.component.DaggerPresenterComponent


open class BaseServicePresenter<V : MvpService>(val service: V){
    private val utilityWrapper = UtilityWrapper()

    init {
        DaggerPresenterComponent.create().inject(utilityWrapper)
    }
}
