package com.flycode.timespace.ui.main.timetable.dailyview.list

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel2
import dagger.Module
import dagger.Provides

@Module
class DailyViewModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
    ): DailyViewPresenter
            = DailyViewPresenter()

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            dailyViewList: DailyViewList,
            dailyViewPresenter: DailyViewPresenter
    ) : DailyViewViewModel {
        val viewModel = ViewModelProviders.of(dailyViewList).get(DailyViewViewModel::class.java)
        dailyViewPresenter.viewModel = viewModel
        viewModel.presenter = dailyViewPresenter
        return viewModel
    }
}