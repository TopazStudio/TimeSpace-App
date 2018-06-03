package com.flycode.timespace.di.component

import com.flycode.timespace.di.module.DataModule
import com.flycode.timespace.ui.base.UtilityWrapper
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(DataModule::class)])
interface PresenterComponent {
    fun inject(utilityWrapper: UtilityWrapper)
}
