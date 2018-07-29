package com.flycode.timespace.ui.main.groups.groupsView

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class GroupViewModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            
    ): GroupViewPresenter = GroupViewPresenter(
            
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            GroupViewFragment: GroupViewFragment,
            GroupViewPresenter: GroupViewPresenter
    ): GroupViewViewModel {
        val viewModel = ViewModelProviders.of(GroupViewFragment).get(GroupViewViewModel::class.java)
        GroupViewPresenter.viewModel = viewModel
        viewModel.presenter = GroupViewPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideGroupViewViewPager(
            groupViewFragment: GroupViewFragment
    ): GroupViewViewPager = GroupViewViewPager(groupViewFragment.childFragmentManager)
}