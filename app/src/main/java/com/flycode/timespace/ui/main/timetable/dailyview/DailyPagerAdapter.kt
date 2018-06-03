package com.flycode.timespace.ui.main.timetable.dailyview

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.test.tudou.library.WeekPager.adapter.WeekPagerAdapter


class DailyPagerAdapter(fm: FragmentManager) : WeekPagerAdapter(fm) {

    override fun createFragmentPager(position: Int): Fragment {
        return DailyViewList.newInstance(mDays[position])
    }
}