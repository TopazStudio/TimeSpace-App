package com.flycode.timespace.ui.main.entries.meetingEntry

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.databinding.MeetingsEntryBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments.MeetingAttachmentsFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees.MeetingAttendeesFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails.MeetingDetailsFragment
import javax.inject.Inject

class MeetingEntryFragment  
    : BaseFragment<MeetingEntryFragment, MeetingEntryPresenter, MeetingEntryViewModel>(),
        MeetingEntryContract.MeetingEntryFragment {

    @Inject override lateinit var viewModel: MeetingEntryViewModel
    @Inject lateinit var meetingEntryViewPager: MeetingEntryViewPager
    lateinit var meetingEntryBinding: MeetingsEntryBinding
    var listeners: ArrayList<MeetingEntryFragmentInterface> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        meetingEntryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_meeting_entry,container,false)
        meetingEntryBinding.viewModel = viewModel
        meetingEntryBinding.setLifecycleOwner(this)

        return meetingEntryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(meetingEntryBinding.toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewPager()
    }

    private fun setupViewPager() {
        meetingEntryViewPager.addFragment(MeetingDetailsFragment().apply {
            this@MeetingEntryFragment.listeners.add(this)
        })

        meetingEntryViewPager.addFragment(MeetingAttendeesFragment().apply {
            this@MeetingEntryFragment.listeners.add(this)
        })

        meetingEntryViewPager.addFragment(MeetingAttachmentsFragment().apply {
            this@MeetingEntryFragment.listeners.add(this)
        })

        meetingEntryBinding.viewPager.offscreenPageLimit = 3
        meetingEntryBinding.viewPager.adapter = meetingEntryViewPager

        meetingEntryBinding.tabLayout.setupWithViewPager(meetingEntryBinding.viewPager)
        createTabIcons()
    }

    private fun createTabIcons() {
        val tabOne = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        meetingEntryBinding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_edit_24dp)
        meetingEntryBinding.tabLayout.getTabAt(0)?.text = "Details"
        meetingEntryBinding.tabLayout.getTabAt(0)?.customView = tabOne

        val tabTwo = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        meetingEntryBinding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_group_add_white_24dp)
        meetingEntryBinding.tabLayout.getTabAt(0)?.text = "Attendees"
        meetingEntryBinding.tabLayout.getTabAt(0)?.customView = tabTwo

        val tabThree = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        meetingEntryBinding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_attach_file_black_24dp)
        meetingEntryBinding.tabLayout.getTabAt(1)?.text = "Attachments"
        meetingEntryBinding.tabLayout.getTabAt(1)?.customView = tabThree

    }

    interface MeetingEntryFragmentInterface{

    }
    
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MeetingEntryFragment()
    }
}
