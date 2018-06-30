package com.flycode.timespace.ui.main

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerActivity
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    @PerActivity
    fun providePresenter(
    ): MainPresenter{
        return MainPresenter(
        )
    }

    @Provides
    @PerActivity
    fun provideViewModel(
            mainActivity : MainActivity,
            mainPresenter: MainPresenter
    ): MainViewModel{
        val viewModel = ViewModelProviders.of(mainActivity).get(MainViewModel::class.java)
        mainPresenter.viewModel = viewModel
        viewModel.presenter = mainPresenter
        return viewModel
    }
}