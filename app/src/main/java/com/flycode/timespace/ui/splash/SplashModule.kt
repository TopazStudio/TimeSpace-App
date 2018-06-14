package com.flycode.timespace.ui.splash

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class SplashModule{

    @Provides
    fun providePresenter(): SplashPresenter{
        return SplashPresenter()
    }

    @Provides
    fun provideViewModel(
            splashActivity : SplashActivity,
            splashPresenter: SplashPresenter
    ): SplashViewModel{
        val viewModel = ViewModelProviders.of(splashActivity).get(SplashViewModel::class.java)
        splashPresenter.viewModel = viewModel
        viewModel.presenter = splashPresenter
        return viewModel
    }
}
