package com.flycode.timespace.ui.main.timetable

import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.main.timetable.dailyview.DailyViewFragment
import com.flycode.timespace.ui.main.timetable.dailyview.DailyViewList
import com.flycode.timespace.ui.main.timetable.dailyview.DailyViewModule
import com.flycode.timespace.ui.main.timetable.monthlyview.MonthlyViewFragment
import com.flycode.timespace.ui.main.timetable.weeklyview.WeeklyViewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {

    @PerFragmentLevel2
    @ContributesAndroidInjector()
    internal abstract fun dailyViewFragment(): DailyViewFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(DailyViewModule::class)])
    internal abstract fun dailyViewList(): DailyViewList

    @PerFragmentLevel2
    @ContributesAndroidInjector()
    internal abstract fun monthlyViewFragment(): MonthlyViewFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector()
    internal abstract fun weeklyViewFragment(): WeeklyViewFragment
}