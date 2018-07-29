package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel2
import dagger.Module
import dagger.Provides

@Module
open class MeetingAttachmentsModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            
    ): MeetingAttachmentsPresenter = MeetingAttachmentsPresenter(
            
    )

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            MeetingAttachmentsFragment: MeetingAttachmentsFragment,
            MeetingAttachmentsPresenter: MeetingAttachmentsPresenter
    ): MeetingAttachmentsViewModel {
        val viewModel = ViewModelProviders.of(MeetingAttachmentsFragment).get(MeetingAttachmentsViewModel::class.java)
        MeetingAttachmentsPresenter.viewModel = viewModel
        viewModel.presenter = MeetingAttachmentsPresenter
        return viewModel
    }
}