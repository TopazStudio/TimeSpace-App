package com.flycode.timespace.ui.main.timetable.dailyview


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.test.tudou.library.WeekPager.adapter.WeekViewAdapter
import com.test.tudou.library.WeekPager.view.WeekDayViewPager
import com.test.tudou.library.model.CalendarDay
import com.test.tudou.library.util.DayUtils
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_daily_view.*


class DailyViewFragment
    : DaggerFragment(),
        WeekDayViewPager.DayScrollListener{

    private val OFFSCREEN_PAGE_LIMIT = 3
    lateinit var dailyPagerAdapter: DailyPagerAdapter
    lateinit var weekViewAdapter : WeekViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "My Timetable"

        init()
    }

    private fun init(){
        setUpPager()
        setUpData()
    }

    private fun setUpPager() {
        //The view pager
        dailyPagerAdapter = DailyPagerAdapter(fragmentManager!!)
        week_day_view_pager.offscreenPageLimit = OFFSCREEN_PAGE_LIMIT
        week_day_view_pager.adapter = dailyPagerAdapter
        week_day_view_pager.setWeekRecyclerView(week_recycler_view)
        week_day_view_pager.setDayScrollListener(this)
        weekViewAdapter = WeekViewAdapter(context, week_day_view_pager)
        weekViewAdapter.setTextNormalColor(resources.getColor(R.color.colorWhite))
        weekViewAdapter.setTextSelectColor(resources.getColor(R.color.colorWhite))

        //The recycler view
        week_recycler_view.adapter = weekViewAdapter
    }

    private fun setUpData() {
        weekViewAdapter.setData(CalendarDay(2018, 7, 1),
                CalendarDay(2018, 6, 1), null)
        dailyPagerAdapter.setData(CalendarDay(2018, 7, 1),
                CalendarDay(2018, 6, 1))
    }

    override fun onDayPageScrolled(position: Int, postionOffset: Float, positionOffesetPixels: Int) {
        text_day_label.text = DayUtils.formatEnglishTime(dailyPagerAdapter.datas[position].time)
    }

    override fun onDayPageSelected(position: Int) {

    }

    override fun onDayPageScrollStateChanged(state: Int) {

    }
}
