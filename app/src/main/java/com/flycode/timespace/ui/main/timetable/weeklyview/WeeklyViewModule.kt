package com.flycode.timespace.ui.main.timetable.weeklyview

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel2
import dagger.Module
import dagger.Provides

@Module
class WeeklyViewModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(

    ): WeeklyViewPresenter
            = WeeklyViewPresenter()

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            weeklyViewFragment: WeeklyViewFragment,
            weeklyViewPresenter: WeeklyViewPresenter
    ) : WeeklyViewViewModel{
        val viewModel = ViewModelProviders.of(weeklyViewFragment).get(WeeklyViewViewModel::class.java)
        weeklyViewPresenter.viewModel = viewModel
        viewModel.presenter = weeklyViewPresenter
        return viewModel
    }

}
