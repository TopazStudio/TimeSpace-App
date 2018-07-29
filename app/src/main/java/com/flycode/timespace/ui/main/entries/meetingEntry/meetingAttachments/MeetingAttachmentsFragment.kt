package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.databinding.MeetingAttachmentsBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryFragment
import javax.inject.Inject

class MeetingAttachmentsFragment  
    : BaseFragment<MeetingAttachmentsFragment, MeetingAttachmentsPresenter, MeetingAttachmentsViewModel>(),
        MeetingAttachmentsContract.MeetingAttachmentsFragment,
        MeetingEntryFragment.MeetingEntryFragmentInterface {

    @Inject
    override lateinit var viewModel: MeetingAttachmentsViewModel
    lateinit var meetingAttachmentsBinding: MeetingAttachmentsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        meetingAttachmentsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_meeting_attachments, container, false)
        meetingAttachmentsBinding.viewModel = viewModel
        meetingAttachmentsBinding.setLifecycleOwner(this)
        return meetingAttachmentsBinding.root
    }

    
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MeetingAttachmentsFragment()
    }
}
