package com.flycode.timespace.di.module

import com.flycode.timespace.di.scope.PerActivity
import com.flycode.timespace.ui.auth.AuthActivity
import com.flycode.timespace.ui.main.MainActivity
import com.flycode.timespace.ui.splash.SplashActivity
import com.flycode.timespace.ui.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingsModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [(SplashModule::class)])
    internal abstract fun splashActivity(): SplashActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(com.flycode.timespace.ui.auth.FragmentProvider::class)])
    internal abstract fun authActivity(): AuthActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(com.flycode.timespace.ui.main.FragmentProvider::class)])
    internal abstract fun mainActivity(): MainActivity
}
