package com.flycode.timespace.ui.main.organization.organizationView

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.data.models.Organization
import com.flycode.timespace.databinding.OrganizationViewBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.flexible_items.GroupListItem
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.utils.Log
import kotlinx.android.synthetic.main.activity_organization_view.*
import javax.inject.Inject
import javax.inject.Named

class OrganizationViewFragment
    : BaseFragment<OrganizationViewFragment, OrganizationViewPresenter, OrganizationViewViewModel>(),
        OrganizationViewContract.OrganizationViewFragment,
        GroupListItem.GroupListItemListener {

    @Inject
    override lateinit var viewModel: OrganizationViewViewModel
    @field: [Inject Named("groups_recycler_view")]
    lateinit var groupsListAdapter: FlexibleAdapter<IFlexible<*>>
    lateinit var organizationViewBinding: OrganizationViewBinding
    private var input_finish_delay: Long = 1000 // 1 seconds after user stops typing
    private var input_finish_handler = Handler()
    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > viewModel.lastTextEdit + input_finish_delay - 500) {
            presenter.searchGroups(et_search.text.toString())
        }
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        organizationViewBinding = DataBindingUtil.inflate(inflater,R.layout.activity_organization_view,container,false)
        organizationViewBinding.viewModel = viewModel
        organizationViewBinding.setLifecycleOwner(this)

        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
        return organizationViewBinding.root
    }

    private fun init(){
        arguments?.let {
            if (it.getString("organization") != null){
                viewModel.organization = Gson().fromJson(it.getString("organization"),Organization::class.java)
                presenter.init()
            }/*else finish()*/ //TODO: action if no organization
        }
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
        btn_search.setOnClickListener {
            viewModel.uiState.inSearchMode = !viewModel.uiState.inSearchMode
        }
        setupDetails()
        setupSearchRecyclerView()
    }

    private fun setupDetails(){
        viewModel.organization?.let {
            if (!it.pictures.isEmpty())
                Picasso.get()
                        .load(it.pictures[0].remote_location)
                        .into(im_picture)
            else im_picture.setImageDrawable(
                    TextDrawable.builder().buildRound(
                            it.name.toCharArray()[0].toString(),
                            ColorGenerator.MATERIAL.getColor(it.name)
                    )
            )
        }
    }

    /**
     * Setup the recycler view by adding the adapter
     * */
    private fun setupSearchRecyclerView(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("SearchAdapter")
        val adapter = groupsListAdapter

        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true) //Enable animation for reverse scrolling

        //LAYOUT MANAGER
        val layoutManager = SmoothScrollLinearLayoutManager(context)

        groups_recycler_view.layoutManager = layoutManager
        groups_recycler_view.setHasFixedSize(true)
        groups_recycler_view.adapter = adapter
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

}
