package com.flycode.timespace.ui.main.timetable.weeklyview

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.main.timetable.TimeTableViewModel
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter

@Module
class WeeklyViewModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            apolloClient: ApolloClient,
            superViewModel: TimeTableViewModel
    ): WeeklyViewPresenter
            = WeeklyViewPresenter(
            apolloClient = apolloClient,
            superViewModel = superViewModel
    )

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
