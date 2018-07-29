package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees

import com.flycode.timespace.ui.base.BaseContract

interface MeetingAttendeesContract {
    interface MeetingAttendeesFragment : BaseContract.View{

    }

    interface MeetingAttendeesPresenter<V : MeetingAttendeesFragment> : BaseContract.Presenter<V>{

    }
}