package com.flycode.timespace.ui.main.groups.groupsView

import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.main.groups.groupsView.groupActivity.GroupActivityFragment
import com.flycode.timespace.ui.main.groups.groupsView.groupActivity.GroupActivityModule
import com.flycode.timespace.ui.main.groups.groupsView.groupMembers.GroupMembersFragment
import com.flycode.timespace.ui.main.groups.groupsView.groupMembers.GroupMembersModule
import com.flycode.timespace.ui.main.groups.groupsView.groupTimeTables.GroupTimetablesFragment
import com.flycode.timespace.ui.main.groups.groupsView.groupTimeTables.GroupTimetablesModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {
    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(GroupActivityModule::class)])
    internal abstract fun groupActivityFragment(): GroupActivityFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(GroupMembersModule::class)])
    internal abstract fun groupMembersFragment(): GroupMembersFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(GroupTimetablesModule::class)])
    internal abstract fun groupTimetablesFragment(): GroupTimetablesFragment
}