package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.flycode.timespace.R
import com.flycode.timespace.databinding.MeetingAttendeesBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.flexible_items.PlainUserListItem
import com.flycode.timespace.ui.flexible_items.SearchResultsHeaderItem
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.utils.Log
import kotlinx.android.synthetic.main.fragment_meeting_attendees.*
import javax.inject.Inject
import javax.inject.Named

class MeetingAttendeesFragment  
    : BaseFragment<MeetingAttendeesFragment, MeetingAttendeesPresenter, MeetingAttendeesViewModel>(),
        MeetingAttendeesContract.MeetingAttendeesFragment,
        MeetingEntryFragment.MeetingEntryFragmentInterface,
        PlainUserListItem.PlainUserListItemListener {
    @Inject
    override lateinit var viewModel: MeetingAttendeesViewModel
    lateinit var meetingAttendeesBinding: MeetingAttendeesBinding

    @field: [Inject Named("search_list_adapter")]
    lateinit var searchListAdapter: FlexibleAdapter<SearchResultsHeaderItem>

    @field: [Inject Named("main_list_adapter")]
    lateinit var mainListAdapter: FlexibleAdapter<PlainHeaderItem>

    private var input_finish_delay: Long = 1000 // 1 seconds after user stops typing
    private var input_finish_handler = Handler()
    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > viewModel.lastTextEdit + input_finish_delay - 500) {
            presenter.searchUsers(meetingAttendeesBinding.edSearch.text.toString())
            onOpenSearch()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        meetingAttendeesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_meeting_attendees, container, false)
        meetingAttendeesBinding.viewModel = viewModel
        meetingAttendeesBinding.setLifecycleOwner(this)
        return meetingAttendeesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        meetingAttendeesBinding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                //You need to remove this to run only once
                input_finish_handler.removeCallbacks(input_finish_checker)

            }

            override fun afterTextChanged(s: Editable) {
                //avoid triggering event when text is empty
                if (s.isNotEmpty()) {
                    viewModel.lastTextEdit = System.currentTimeMillis()
                    input_finish_handler.postDelayed(input_finish_checker, input_finish_delay)
                }
            }
        })

        meetingAttendeesBinding.mainReloadBtn.setOnClickListener {
           mainListAdapter.notifyDataSetChanged()
        }

        meetingAttendeesBinding.searchReloadBtn.setOnClickListener {
            presenter.searchUsers(meetingAttendeesBinding.edSearch.text.toString())
        }

        meetingAttendeesBinding.btnSearchToggle.setOnClickListener {
            if (!viewModel.uiState.isSearchOpen){ //if not open
                onOpenSearch()
            }else{
                onCloseSearch()
            }
        }
        meetingAttendeesBinding.btnCloseSearchHint.setOnClickListener {
            viewModel.uiState.isInvitationHintOpen = false
        }
        setupMainRecyclerViews()
        setupSearchRecyclerView()
    }

    private fun setupMainRecyclerViews(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("MainAdapter")

        mainListAdapter.addItem(0, viewModel.mainListHeaderItem)


        val adapter = mainListAdapter
        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .setStickyHeaders(true)
                .expandItemsAtStartUp() //Items must be pre-initialized with expanded=true
                .setAutoCollapseOnExpand(false) //Force closes all others expandable item before expanding a new one
                .setDisplayHeadersAtStartUp(true)
                .setAutoScrollOnExpand(true) //Needs a SmoothScrollXXXLayoutManager
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true) //Enable animation for reverse scrolling

        //LAYOUT MANAGER
        val layoutManager = SmoothScrollLinearLayoutManager(context)

        meetingAttendeesBinding.mainRecyclerView.layoutManager = layoutManager
        meetingAttendeesBinding.mainRecyclerView.setHasFixedSize(true)
        meetingAttendeesBinding.mainRecyclerView.adapter = adapter

        mainListAdapter.expandAll()
    }

    /**
     * Setup the recycler view by adding the adapter
     * */
    private fun setupSearchRecyclerView(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("SearchAdapter")

        searchListAdapter.addItem(0, viewModel.searchResultHeaderItem)

        val adapter = searchListAdapter

        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true) //Enable animation for reverse scrolling

        //LAYOUT MANAGER
        val layoutManager = SmoothScrollLinearLayoutManager(context)

        meetingAttendeesBinding.searchRecyclerView.layoutManager = layoutManager
        meetingAttendeesBinding.searchRecyclerView.setHasFixedSize(true)
        meetingAttendeesBinding.searchRecyclerView.adapter = adapter
    }

    private fun onOpenSearch(){
        if (!viewModel.uiState.isSearchOpen) {
            YoYo.with(Techniques.SlideInDown)
                    .duration(500)
                    .repeat(0)
//                    .onStart {
//                        search_results.visibility = View.VISIBLE
//                    }
                    .playOn(search_results)

            YoYo.with(Techniques.FadeOut)
                    .duration(500)
                    .delay(100)
                    .repeat(0)
//                    .onEnd {
//                        details_frame.visibility = View.GONE
//                    }
                    .playOn(main_recycler_view)

            viewModel.uiState.isSearchOpen = true
        }

    }

    private fun onCloseSearch(){
        if (viewModel.uiState.isSearchOpen) {
            YoYo.with(Techniques.SlideOutUp)
                    .duration(500)
                    .delay(100)
                    .repeat(0)
//                    .onEnd {
//                        search_results.visibility = View.GONE
//                    }
                    .playOn(search_results)


            YoYo.with(Techniques.FadeIn)
                    .duration(500)
                    .repeat(0)
//                    .onStart{
//                        details_frame.visibility = View.VISIBLE
//                    }
                    .playOn(main_recycler_view)

            viewModel.uiState.isSearchOpen = false
        }
    }

//    override fun onExpand(position: Int) {
//        mainListAdapter.expand(position)
//    }
//
//    override fun onCollapse(position: Int){
//        mainListAdapter.collapse(position)
//    }

    override fun onUserClicked(plainUserListItem: PlainUserListItem) {
        presenter.addItemToMainList(plainUserListItem)
    }
    
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MeetingAttendeesFragment()
    }
}
