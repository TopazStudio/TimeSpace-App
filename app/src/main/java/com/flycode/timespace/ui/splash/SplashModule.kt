package com.flycode.timespace.ui.splash

import dagger.Module
import dagger.Provides

@Module
class SplashModule{

    @Provides
    fun providePresenter(splashFragment : SplashActivity)
            : SplashContract.SplashPresenter<SplashContract.SplashActivity>{
        return SplashPresenter(splashFragment)
    }
}
