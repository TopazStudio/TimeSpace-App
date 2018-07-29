package com.flycode.timespace.ui.main.groups.groupsEntry

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class GroupEntryModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            
    ): GroupEntryPresenter = GroupEntryPresenter(
            
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            GroupEntryFragment: GroupEntryFragment,
            GroupEntryPresenter: GroupEntryPresenter
    ): GroupEntryViewModel {
        val viewModel = ViewModelProviders.of(GroupEntryFragment).get(GroupEntryViewModel::class.java)
        GroupEntryPresenter.viewModel = viewModel
        viewModel.presenter = GroupEntryPresenter
        return viewModel
    }
}