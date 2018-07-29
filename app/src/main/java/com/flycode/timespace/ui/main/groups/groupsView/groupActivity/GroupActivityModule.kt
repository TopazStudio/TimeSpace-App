package com.flycode.timespace.ui.main.groups.groupsView.groupActivity

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class GroupActivityModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            
    ): GroupActivityPresenter = GroupActivityPresenter(
            
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            GroupActivityFragment: GroupActivityFragment,
            GroupActivityPresenter: GroupActivityPresenter
    ): GroupActivityViewModel {
        val viewModel = ViewModelProviders.of(GroupActivityFragment).get(GroupActivityViewModel::class.java)
        GroupActivityPresenter.viewModel = viewModel
        viewModel.presenter = GroupActivityPresenter
        return viewModel
    }
}