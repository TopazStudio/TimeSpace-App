package com.flycode.timespace.ui.main

import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.flycode.timespace.ui.main.timetable.FragmentProvider
import com.flycode.timespace.ui.main.timetable.TimeTableFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(FragmentProvider::class)])
    internal abstract fun timeTableFragment(): TimeTableFragment
}