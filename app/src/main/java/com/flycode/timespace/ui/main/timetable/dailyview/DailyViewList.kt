package com.flycode.timespace.ui.main.timetable.dailyview


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment
import com.test.tudou.library.model.CalendarDay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.daily_view_list.*
import javax.inject.Inject


class DailyViewList
    : BaseFragment(),
        DailyViewContract.DailyViewList {


    @Inject
    lateinit var presenter : DailyViewContract.DailyViewPresenter<DailyViewContract.DailyViewList>
    /**
     * The day information is required from this list
     * */
    lateinit var mCalendarDay: CalendarDay

    companion object {
        fun newInstance(calendarDay: CalendarDay): DailyViewList {
            val dailyViewList = DailyViewList()
            dailyViewList.mCalendarDay = calendarDay
            return dailyViewList
        }
    }

    /**
     * Initialize view
     * */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.daily_view_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        presenter.fetchItems(mCalendarDay)
    }

    /**
     * Setup the recycler view by adding the adapter
     * */
    override fun setupRecyclerView(adapter: FlexibleAdapter<IFlexible<*>>){
        recycler_view.adapter = adapter
    }

}
