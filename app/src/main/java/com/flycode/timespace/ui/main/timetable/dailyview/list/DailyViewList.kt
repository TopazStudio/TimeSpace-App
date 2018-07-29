package com.flycode.timespace.ui.main.timetable.dailyview.list


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.databinding.DailyViewBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.test.tudou.library.model.CalendarDay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.utils.Log
import kotlinx.android.synthetic.main.daily_view_list.*
import java.util.*
import javax.inject.Inject


class DailyViewList
    : BaseFragment<DailyViewList, DailyViewPresenter, DailyViewViewModel>(),
        DailyViewContract.DailyViewList, ExpandableHeaderItem.ExpandableHeaderItemListener, PlainHeaderItem.PlainHeaderItemListener {


    @Inject override lateinit var viewModel: DailyViewViewModel
    lateinit var mCalendarDay: CalendarDay
    @Inject lateinit var mainListAdapter: FlexibleAdapter<PlainHeaderItem>
    lateinit var dailyViewBinding: DailyViewBinding

    companion object {
        const val CLASSES_LIST_POSITION = 0
        const val MEETINGS_LIST_POSITION = 1
        const val EXAM_LIST_POSITION = 2
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
        dailyViewBinding = DataBindingUtil.inflate(inflater,R.layout.daily_view_list,container,false)
        dailyViewBinding.viewModel = viewModel
        dailyViewBinding.setLifecycleOwner(this)

        return dailyViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_reload_btn.setOnClickListener {
            viewModel.uiState.onError = false
            presenter.fetchItems()
        }

        init()
    }

    fun init(){
        setupMainRecyclerViews()
    }

    private fun setupMainRecyclerViews(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("MainAdapter")

        //LAYOUT MANAGER
        val layoutManager = SmoothScrollLinearLayoutManager(context)
        val adapter = mainListAdapter

        if (viewModel.headingsList.isEmpty()){
            //HEADERS
            viewModel.headingsList =  ArrayList<PlainHeaderItem>().apply {
                this.add(CLASSES_LIST_POSITION,PlainHeaderItem(1, "Classes", 0).apply {
                    listener = this@DailyViewList
                })
                this.add(MEETINGS_LIST_POSITION,PlainHeaderItem(2, "Meetings", 0).apply {
                    listener = this@DailyViewList
                })
                this.add(EXAM_LIST_POSITION,PlainHeaderItem(3, "Exams", 0).apply {
                    listener = this@DailyViewList
                })
            }
            mainListAdapter.addItems(0, viewModel.headingsList)
            // Non-exhaustive configuration that don't need RV instance
            adapter.addListener(this) //Only if you didn't use the Constructor
                    .setStickyHeaders(true)
                    .expandItemsAtStartUp() //Items must be pre-initialized with expanded=true
                    .setAutoCollapseOnExpand(false) //Force closes all others expandable item before expanding a new one
                    .setDisplayHeadersAtStartUp(true)
                    .setAutoScrollOnExpand(true) //Needs a SmoothScrollXXXLayoutManager
                    .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                    .setAnimationOnReverseScrolling(true) //Enable animation for reverse scrolling

        }

        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter

        mainListAdapter.expandAll()
    }

    override fun onExpand(position: Int) {
        mainListAdapter.expand(position)
    }

    override fun onCollapse(position: Int){
        mainListAdapter.collapse(position)
    }
}
