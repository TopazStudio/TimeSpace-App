package com.flycode.timespace.ui.main.groups.groupsView.groupTimeTables

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class GroupTimetablesModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            
    ): GroupTimetablesPresenter = GroupTimetablesPresenter(
            
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            GroupTimetablesFragment: GroupTimetablesFragment,
            GroupTimetablesPresenter: GroupTimetablesPresenter
    ): GroupTimetablesViewModel {
        val viewModel = ViewModelProviders.of(GroupTimetablesFragment).get(GroupTimetablesViewModel::class.java)
        GroupTimetablesPresenter.viewModel = viewModel
        viewModel.presenter = GroupTimetablesPresenter
        return viewModel
    }
}