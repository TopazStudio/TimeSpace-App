package com.flycode.timespace.ui.main.timetable.dailyview.list

import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem
import com.flycode.timespace.ui.flexible_items.MeetingListItem
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.test.tudou.library.model.CalendarDay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.ISectionable


class DailyViewPresenter
    : BasePresenter<DailyViewList, DailyViewPresenter, DailyViewViewModel>()
        , DailyViewContract.DailyViewPresenter<DailyViewList> {


    private fun fetchMeetings(){
        SQLite.select()
                .from(Meeting::class.java)
                .where()
                .async()
                .queryListResultCallback{ transaction, tResult ->

                }
                .error{
                    transaction, error ->
                    view?.showError(error.message.toString())
                }
                .execute()
    }

    fun getDatabaseList(): List<IFlexible<*>> {
        val headingsList =  ArrayList<ExpandableHeaderItem>()

        //MEETINGS
        val meetingList = ArrayList<MeetingListItem>()
        //Create Header
        val header = ExpandableHeaderItem(1, "Meetings", 4)
        //Get the list
        meetingList.add(MeetingListItem(header, Meeting(
                1,
                "HCI",
                "Some Note",
                111111,
                "Bla Bal"
        )))
        header.subItems = meetingList as List<ISectionable<*, *>>?
        headingsList.add(header)

        return headingsList
    }

    override fun fetchItems(mCalendarDay: CalendarDay){
        // Optional but strongly recommended: Compose the initial list
        val myItems = getDatabaseList()

        //ADAPTER
        FlexibleAdapter.useTag("Meetings Adapter")
        val adapter = FlexibleAdapter<IFlexible<*>>(myItems)

        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .expandItemsAtStartUp() //Items must be pre-initialized with expanded=true
                .setAutoCollapseOnExpand(false) //Force closes all others expandable item before expanding a new one
                .setDisplayHeadersAtStartUp(true)
                .setAutoScrollOnExpand(true) //Needs a SmoothScrollXXXLayoutManager
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true)
                //Enable animation for reverse scrolling

        //LAYOUT MANAGER
        val layoutManager : SmoothScrollLinearLayoutManager = SmoothScrollLinearLayoutManager(view?.getFragmentContext())


        // Initialize the RecyclerView and attach the Adapter to it as usual
        view?.setupRecyclerView(adapter,layoutManager)
    }
}