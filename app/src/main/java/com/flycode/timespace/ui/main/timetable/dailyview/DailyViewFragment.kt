package com.flycode.timespace.ui.main.timetable.dailyview


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment
import com.test.tudou.library.WeekPager.adapter.WeekViewAdapter
import com.test.tudou.library.WeekPager.view.WeekDayViewPager
import com.test.tudou.library.model.CalendarDay
import com.test.tudou.library.util.DayUtils
import kotlinx.android.synthetic.main.fragment_daily_view.*


class DailyViewFragment
    : BaseFragment(),
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
        weekViewAdapter.setTextNormalColor(resources.getColor(R.color.colorBlack))
        weekViewAdapter.setTextSelectColor(resources.getColor(R.color.colorWhite))
        weekViewAdapter.setIndicatorColor(resources.getColor(R.color.colorBlack))

        //The recycler view
        week_recycler_view.adapter = weekViewAdapter
    }

    private fun setUpData() {
        val reachAbleDays = ArrayList<CalendarDay>()
        //TODO: yearly range
        reachAbleDays.add(CalendarDay(2018, 1, 1))
        reachAbleDays.add(CalendarDay(2018, 12, 31))
        weekViewAdapter.setData(reachAbleDays[0], reachAbleDays[reachAbleDays.size - 1], null)
        dailyPagerAdapter.setData(reachAbleDays[0], reachAbleDays[reachAbleDays.size - 1])
    }

    override fun onDayPageScrolled(position: Int, postionOffset: Float, positionOffesetPixels: Int) {
        text_day_label.text = DayUtils.formatEnglishTime(dailyPagerAdapter.datas[position].time)
    }

    override fun onDayPageSelected(position: Int) {

    }

    override fun onDayPageScrollStateChanged(state: Int) {

    }
}
