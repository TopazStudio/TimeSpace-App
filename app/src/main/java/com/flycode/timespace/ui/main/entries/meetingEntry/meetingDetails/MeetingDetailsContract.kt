package com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails

import com.flycode.timespace.ui.base.BaseContract

interface MeetingDetailsContract {
    interface MeetingDetailsFragment : BaseContract.View{

    }

    interface MeetingDetailsPresenter<V : MeetingDetailsFragment> : BaseContract.Presenter<V>{

    }
}