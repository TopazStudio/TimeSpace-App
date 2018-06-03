package com.flycode.timespace.ui.main.timetable.dailyview

import dagger.Module
import dagger.Provides

@Module
class DailyViewModule {
    @Provides
    fun providePresenter(dailyViewFragment : DailyViewList)
            : DailyViewContract.DailyViewPresenter<DailyViewContract.DailyViewList>{
        return DailyViewPresenter(dailyViewFragment)
    }
}