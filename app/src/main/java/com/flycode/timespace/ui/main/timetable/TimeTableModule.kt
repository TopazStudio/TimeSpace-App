package com.flycode.timespace.ui.main.timetable

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class TimeTableModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            apolloClient: ApolloClient
    ): TimeTablePresenter = TimeTablePresenter(
            apolloClient = apolloClient
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            TimeTableFragment: TimeTableFragment,
            TimeTablePresenter: TimeTablePresenter
    ): TimeTableViewModel {
        val viewModel = ViewModelProviders.of(TimeTableFragment).get(TimeTableViewModel::class.java)
        TimeTablePresenter.viewModel = viewModel
        viewModel.presenter = TimeTablePresenter
        return viewModel
    }
}