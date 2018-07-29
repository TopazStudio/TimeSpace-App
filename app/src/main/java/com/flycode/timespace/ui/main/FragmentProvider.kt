package com.flycode.timespace.ui.main

import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.flycode.timespace.ui.main.entries.classEntry.ClassEntryFragment
import com.flycode.timespace.ui.main.entries.classEntry.ClassEntryModule
import com.flycode.timespace.ui.main.entries.examEntry.ExamEntryFragment
import com.flycode.timespace.ui.main.entries.examEntry.ExamEntryModule
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryModule
import com.flycode.timespace.ui.main.groups.groupsEntry.GroupEntryFragment
import com.flycode.timespace.ui.main.groups.groupsEntry.GroupEntryModule
import com.flycode.timespace.ui.main.groups.groupsOverview.GroupsOverviewFragment
import com.flycode.timespace.ui.main.groups.groupsOverview.GroupsOverviewModule
import com.flycode.timespace.ui.main.groups.groupsView.GroupViewFragment
import com.flycode.timespace.ui.main.groups.groupsView.GroupViewModule
import com.flycode.timespace.ui.main.organization.organizationEntry.OrganizationEntryFragment
import com.flycode.timespace.ui.main.organization.organizationEntry.OrganizationEntryModule
import com.flycode.timespace.ui.main.organization.organizationView.OrganizationViewFragment
import com.flycode.timespace.ui.main.organization.organizationView.OrganizationViewModule
import com.flycode.timespace.ui.main.timetable.FragmentProvider
import com.flycode.timespace.ui.main.timetable.TimeTableFragment
import com.flycode.timespace.ui.main.timetable.TimeTableModule
import com.flycode.timespace.ui.main.user.userView.UserViewFragment
import com.flycode.timespace.ui.main.user.userView.UserViewModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(TimeTableModule::class),(FragmentProvider::class)])
    internal abstract fun timeTableFragment(): TimeTableFragment



    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(GroupViewModule::class)])
    internal abstract fun groupViewOverview(): GroupViewFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(GroupEntryModule::class)])
    internal abstract fun groupEntryOverview(): GroupEntryFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(GroupsOverviewModule::class)])
    internal abstract fun groupsOverview(): GroupsOverviewFragment



    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(OrganizationViewModule::class)])
    internal abstract fun OrganizationViewOverview(): OrganizationViewFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(OrganizationEntryModule::class)])
    internal abstract fun OrganizationEntryOverview(): OrganizationEntryFragment

//    @PerFragmentLevel1
//    @ContributesAndroidInjector(modules = [(OrganizationsOverviewModule::class)])
//    internal abstract fun OrganizationsOverview(): OrganizationsOverviewFragment


    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(ClassEntryModule::class)])
    internal abstract fun classEntryFagment(): ClassEntryFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(ExamEntryModule::class)])
    internal abstract fun examEntryOverview(): ExamEntryFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(MeetingEntryModule::class),(com.flycode.timespace.ui.main.entries.meetingEntry.FragmentProvider::class)])
    internal abstract fun meetingEntryOverview(): MeetingEntryFragment


    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(UserViewModule::class)])
    internal abstract fun userViewOverview(): UserViewFragment


}