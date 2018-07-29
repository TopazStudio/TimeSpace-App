package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments

import com.flycode.timespace.ui.base.BaseContract

interface MeetingAttachmentsContract {
    interface MeetingAttachmentsFragment : BaseContract.View{

    }

    interface MeetingAttachmentsPresenter<V : MeetingAttachmentsFragment> : BaseContract.Presenter<V>{

    }
}