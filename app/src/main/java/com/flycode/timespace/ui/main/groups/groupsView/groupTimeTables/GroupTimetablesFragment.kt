package com.flycode.timespace.ui.main.groups.groupsView.groupTimeTables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.main.groups.groupsView.GroupViewFragment

class GroupTimetablesFragment  
    : BaseFragment<GroupTimetablesFragment, GroupTimetablesPresenter, GroupTimetablesViewModel>(),
        GroupTimetablesContract.GroupTimetablesFragment, GroupViewFragment.GroupViewFragmentInterface {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_entry, container, false)
    }

    
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = GroupTimetablesFragment()
    }
}
