package com.flycode.timespace.ui.main.groups.groupsOverview

import android.content.Intent
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
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.data.models.Organization
import com.flycode.timespace.databinding.OrientationBindings
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem
import com.flycode.timespace.ui.flexible_items.GroupListItem
import com.flycode.timespace.ui.flexible_items.OrganizationListItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.ISectionable
import eu.davidea.flexibleadapter.utils.Log
import kotlinx.android.synthetic.main.fragment_group_overview.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class GroupsOverviewFragment
    : BaseFragment<GroupsOverviewFragment, GroupsOverviewPresenter, GroupsOverviewViewModel>(),
        GroupsOverviewContract.OrientationFragment,
        GroupListItem.GroupListItemListener,
        OrganizationListItem.OrganizationListItemListener,
        ExpandableHeaderItem.ExpandableHeaderItemListener {

    @Inject
    override lateinit var viewModel: GroupsOverviewViewModel
    lateinit var orientationsBindings: OrientationBindings

    @field: [Inject Named("search_list_adapter")]
    lateinit var searchListAdapter: FlexibleAdapter<ISectionable<*,*>>

    @field: [Inject Named("main_list_adapter")]
    lateinit var mainListAdapter: FlexibleAdapter<ExpandableHeaderItem>

    private var input_finish_delay: Long = 1000 // 1 seconds after user stops typing
    private var input_finish_handler = Handler()
    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > viewModel.lastTextEdit + input_finish_delay - 500) {
            presenter.searchOrganizationsAndGroups(et_search.text.toString())
            onOpenSearch()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        orientationsBindings = DataBindingUtil.inflate(inflater,R.layout.fragment_group_overview, container, false)
        orientationsBindings.viewModel = viewModel
        orientationsBindings.setLifecycleOwner(this)
        return orientationsBindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_search.addTextChangedListener(object : TextWatcher {
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
        btn_close_search.setOnClickListener {
            if (!viewModel.uiState.isSearchOpen){ //if not open
                onOpenSearch()
            }else{
                onCloseSearch()
            }
        }
        btn_close_search_hint.setOnClickListener {
            viewModel.uiState.isSearchHintOpen = false
        }
        setupMainRecyclerViews()
        setupSearchRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != 0){
            if (resultCode == GROUP_VIEW_RESULT_CODE){
                if (data?.hasExtra("joined")!!
                        && data.hasExtra("pending")
                        && data.hasExtra("group")){
//                    if (data.getBooleanExtra("joined"))
                }
            }
        }
    }

    private fun setupMainRecyclerViews(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("MainAdapter")

        //HEADERS
        viewModel.headingsList =  ArrayList<ExpandableHeaderItem>().apply {
            this.add(JOINED_LIST_POSITION,ExpandableHeaderItem(1, "Joined", 0).apply {
                listener = this@GroupsOverviewFragment
            })
            this.add(PENDING_LIST_POSITION,ExpandableHeaderItem(2, "Pending", 0).apply {
                listener = this@GroupsOverviewFragment
            })
            this.add(SUGGESTED_LIST_POSITION,ExpandableHeaderItem(3, "Suggested", 0).apply {
                listener = this@GroupsOverviewFragment
            })
        }
        mainListAdapter.addItems(0, viewModel.headingsList)

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

        main_recycler_view.layoutManager = layoutManager
        main_recycler_view.setHasFixedSize(true)
        main_recycler_view.adapter = adapter

        mainListAdapter.expandAll()
    }

    /**
     * Setup the recycler view by adding the adapter
     * */
    private fun setupSearchRecyclerView(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("SearchAdapter")
        val adapter = searchListAdapter

        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true) //Enable animation for reverse scrolling

        //LAYOUT MANAGER
        val layoutManager = SmoothScrollLinearLayoutManager(context)

        search_recycler_view.layoutManager = layoutManager
        search_recycler_view.setHasFixedSize(true)
        search_recycler_view.adapter = adapter
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

    override fun onOrganizationClicked(organization: Organization) {
        presenter.onOrganizationClicked(organization)
    }

    override fun onGroupClicked(group: Group) {
        presenter.onGroupClicked(group)
    }

    override fun onJoinGroup(groupListItem: GroupListItem,holder: GroupListItem.MyViewHolder?,position: Int){
        presenter.onJoinGroup(groupListItem,holder,position)
    }

    override fun onJoinedGroupRemoved(groupListItem: GroupListItem,holder: GroupListItem.MyViewHolder?,position: Int) {
        presenter.onJoinedGroupRemoved(groupListItem,holder,position)
    }

    override fun onPendingGroupRemoved(groupListItem: GroupListItem, holder: GroupListItem.MyViewHolder?, position: Int) {
        presenter.onPendingGroupRemoved(groupListItem,holder,position)
    }

    override fun onExpand(position: Int) {
        mainListAdapter.expand(position)
    }

    override fun onCollapse(position: Int){
        mainListAdapter.collapse(position)
    }
    companion object {
        const val ORGANIZATION_VIEW_RESULT_CODE = 1
        const val GROUP_VIEW_RESULT_CODE = 2
        const val JOINED_LIST_POSITION = 0
        const val PENDING_LIST_POSITION = 1
        const val SUGGESTED_LIST_POSITION = 2
    }
}
