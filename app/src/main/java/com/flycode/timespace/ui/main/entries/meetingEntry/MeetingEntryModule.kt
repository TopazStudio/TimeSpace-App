package com.flycode.timespace.ui.main.entries.meetingEntry

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class MeetingEntryModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            
    ): MeetingEntryPresenter = MeetingEntryPresenter(
            
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            MeetingEntryFragment: MeetingEntryFragment,
            MeetingEntryPresenter: MeetingEntryPresenter
    ): MeetingEntryViewModel {
        val viewModel = ViewModelProviders.of(MeetingEntryFragment).get(MeetingEntryViewModel::class.java)
        MeetingEntryPresenter.viewModel = viewModel
        viewModel.presenter = MeetingEntryPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideMeetingEntryViewPager(
            meetingEntryFragment: MeetingEntryFragment
    ): MeetingEntryViewPager = MeetingEntryViewPager(meetingEntryFragment.childFragmentManager)
}