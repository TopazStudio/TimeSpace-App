package com.flycode.timespace.ui.splash

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.flycode.timespace.di.scope.PerActivity
import dagger.Module
import dagger.Provides

@Module
class SplashModule{

    @Provides
    @PerActivity
    fun providePresenter(
            sharedPreferences: SharedPreferences
    ): SplashPresenter{
        return SplashPresenter(
                sharedPreferences = sharedPreferences
        )
    }

    @Provides
    @PerActivity
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
