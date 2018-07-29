package com.flycode.timespace.ui.main.timetable.weeklyview

import android.graphics.RectF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_weekly_view.*
import javax.inject.Inject

class WeeklyViewFragment
    : BaseFragment<WeeklyViewFragment, WeeklyViewPresenter, WeeklyViewViewModel>(),
        WeeklyViewContract.WeeklyViewFragment,
        WeekView.EventClickListener,
        MonthLoader.MonthChangeListener,
        WeekView.EventLongPressListener {

    @Inject
    override lateinit var viewModel: WeeklyViewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "My Timetable"

        init()
    }

    fun init(){
        setupAndroidWeekView()
    }


    fun setupAndroidWeekView(){

        weekView.setOnEventClickListener(this)

        weekView.setMonthChangeListener(this);

        weekView.setEventLongPressListener(this);
    }

    override fun onEventLongPress(event: WeekViewEvent?, eventRect: RectF?) {

    }

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent> {
        return presenter.onMonthChange(newYear,newMonth)
    }

    override fun onEventClick(event: WeekViewEvent?, eventRect: RectF?) {

    }
}
