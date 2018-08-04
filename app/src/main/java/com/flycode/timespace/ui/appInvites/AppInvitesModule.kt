package com.flycode.timespace.ui.appInvites

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.data.network.retrofit.AppInvitesService
import com.flycode.timespace.di.scope.PerActivity
import dagger.Module
import dagger.Provides

@Module
class AppInvitesModule {

    @Provides
    @PerActivity
    fun providePresenter(
            appInvitesService: AppInvitesService
    ): AppInvitesPresenter
            = AppInvitesPresenter(
            appInvitesService = appInvitesService
    )

    @Provides
    @PerActivity
    fun provideViewModel(
            appInvitesActivity: AppInvitesActivity,
            appInvitesPresenter: AppInvitesPresenter
    ) : AppInvitesViewModel {
        val viewModel = ViewModelProviders.of(appInvitesActivity).get(AppInvitesViewModel::class.java)
        appInvitesPresenter.viewModel = viewModel
        viewModel.presenter = appInvitesPresenter
        return viewModel
    }


    @Provides
    @PerActivity
    fun provideAppInvitesViewPager(
            appInvitesActivity: AppInvitesActivity
    ): AppInvitesViewPager = AppInvitesViewPager(appInvitesActivity.childFragmentManager)

}