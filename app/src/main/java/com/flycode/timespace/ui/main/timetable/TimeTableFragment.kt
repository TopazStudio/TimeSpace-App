package com.flycode.timespace.ui.main.timetable

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment

class TimeTableFragment : BaseFragment() {

    companion object {
        fun newInstance() = TimeTableFragment()
    }

    private lateinit var viewModel: TimeTableViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.time_table_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this as Fragment).get(TimeTableViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
