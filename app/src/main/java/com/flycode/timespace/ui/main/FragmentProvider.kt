package com.flycode.timespace.ui.main

import com.flycode.timespace.di.scope.PerFragment
import com.flycode.timespace.ui.main.timetable.TimeTableFragment
import com.flycode.timespace.ui.main.timetable.TimeTableModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragment
    @ContributesAndroidInjector(modules = [(TimeTableModule::class)])
    internal abstract fun timeTableFragment(): TimeTableFragment
}