package com.flycode.timespace.ui.main.entries.meetingEntry

import com.flycode.timespace.ui.base.BaseContract

interface MeetingEntryContract {
    interface MeetingEntryFragment : BaseContract.View{

    }

    interface MeetingEntryPresenter<V : MeetingEntryFragment> : BaseContract.Presenter<V>{

    }
}