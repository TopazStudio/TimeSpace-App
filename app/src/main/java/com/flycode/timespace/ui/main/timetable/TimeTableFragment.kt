package com.flycode.timespace.ui.main.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment

class TimeTableFragment : BaseFragment() {

    companion object {
        fun newInstance() = TimeTableFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.time_table_fragment, container, false)
    }


}
