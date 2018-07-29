package com.flycode.timespace.ui.main.organization.organizationEntry

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class OrganizationEntryModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            
    ): OrganizationEntryPresenter = OrganizationEntryPresenter(
            
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            OrganizationEntryFragment: OrganizationEntryFragment,
            OrganizationEntryPresenter: OrganizationEntryPresenter
    ): OrganizationEntryViewModel {
        val viewModel = ViewModelProviders.of(OrganizationEntryFragment).get(OrganizationEntryViewModel::class.java)
        OrganizationEntryPresenter.viewModel = viewModel
        viewModel.presenter = OrganizationEntryPresenter
        return viewModel
    }
}