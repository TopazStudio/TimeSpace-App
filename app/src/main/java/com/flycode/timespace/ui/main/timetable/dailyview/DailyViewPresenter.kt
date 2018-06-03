package com.flycode.timespace.ui.main.timetable.dailyview

import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.main.timetable.dailyview.items.ExpandableHeaderItem
import com.flycode.timespace.ui.main.timetable.dailyview.items.MeetingListItem
import com.test.tudou.library.model.CalendarDay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible


class DailyViewPresenter<V : DailyViewContract.DailyViewList>(mvpView: V)
    : BasePresenter<V>(mvpView) , DailyViewContract.DailyViewPresenter<V>{

    fun getDatabaseList(): List<IFlexible<*>> {
        val list = ArrayList<MeetingListItem>()
        val header = ExpandableHeaderItem(1,"Meetings",4)
        list.add(MeetingListItem(header,Meeting(
                1,
                1,
                1,
                "Human Computer Interaction",
                "Bla Bal",
                0,
            "Some more shit"
        )))
        return list
    }

    override fun fetchItems(mCalendarDay: CalendarDay){
        // Optional but strongly recommended: Compose the initial list
        val myItems = getDatabaseList()

        // Initialize the Adapter
        FlexibleAdapter.useTag("Meetings Adapter")
        val adapter = FlexibleAdapter<IFlexible<*>>(myItems)
        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .expandItemsAtStartUp() //Items must be pre-initialized with expanded=true
                .setAutoCollapseOnExpand(false) //Force closes all others expandable item before expanding a new one
                .setMinCollapsibleLevel(1) //Auto collapse only items with level >= 1 (Don't auto-collapse level 0)
                .setAutoScrollOnExpand(true) //Needs a SmoothScrollXXXLayoutManager
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true)
                //Enable animation for reverse scrolling


        // Initialize the RecyclerView and attach the Adapter to it as usual
        mvpView.setupRecyclerView(adapter)
    }
}