package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter

@Module
open class MeetingAttachmentsModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            mainListAdapter: FlexibleAdapter<PlainHeaderItem>
    ): MeetingAttachmentsPresenter = MeetingAttachmentsPresenter(
            mainListAdapter = mainListAdapter
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

    @Provides
    @PerFragmentLevel2
    fun provideMainListAdapter(): FlexibleAdapter<PlainHeaderItem> = FlexibleAdapter(null)

}