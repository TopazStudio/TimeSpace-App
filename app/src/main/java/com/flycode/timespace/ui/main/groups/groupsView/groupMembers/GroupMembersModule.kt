package com.flycode.timespace.ui.main.groups.groupsView.groupMembers

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class GroupMembersModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            
    ): GroupMembersPresenter = GroupMembersPresenter(
            
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            GroupMembersFragment: GroupMembersFragment,
            GroupMembersPresenter: GroupMembersPresenter
    ): GroupMembersViewModel {
        val viewModel = ViewModelProviders.of(GroupMembersFragment).get(GroupMembersViewModel::class.java)
        GroupMembersPresenter.viewModel = viewModel
        viewModel.presenter = GroupMembersPresenter
        return viewModel
    }
}