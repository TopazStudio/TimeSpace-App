package com.flycode.timespace.ui.main.timetable.dailyview.list

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.main.timetable.TimeTableViewModel
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter

@Module
class DailyViewModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            apolloClient: ApolloClient,
            mainListAdapter: FlexibleAdapter<PlainHeaderItem>,
            superViewModel: TimeTableViewModel
    ): DailyViewPresenter
            = DailyViewPresenter(
            apolloClient = apolloClient,
            mainListAdapter = mainListAdapter,
            superViewModel = superViewModel
    )

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

    @Provides
    @PerFragmentLevel2
    fun provideSearchListAdapter(): FlexibleAdapter<PlainHeaderItem> = FlexibleAdapter(null)
}