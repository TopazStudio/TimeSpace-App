package com.flycode.timespace.ui.main.entries.meetingEntry

import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments.MeetingAttachmentsFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments.MeetingAttachmentsModule
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees.MeetingAttendeesFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees.MeetingAttendeesModule
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails.MeetingDetailsFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails.MeetingDetailsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(MeetingDetailsModule::class)])
    internal abstract fun meetingDetailsFragment(): MeetingDetailsFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(MeetingAttendeesModule::class)])
    internal abstract fun meetingAttendeesFragment(): MeetingAttendeesFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(MeetingAttachmentsModule::class)])
    internal abstract fun meetingAttachmentsFragment(): MeetingAttachmentsFragment
}