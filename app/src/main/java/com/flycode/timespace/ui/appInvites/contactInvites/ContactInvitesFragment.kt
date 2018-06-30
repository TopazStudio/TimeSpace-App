package com.flycode.timespace.ui.appInvites.contactInvites


import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.data.models.User
import com.flycode.timespace.databinding.ContactInvitesBinding
import com.flycode.timespace.ui.appInvites.AppInvitesActivity
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.flexible_items.ContactListItem
import com.flycode.timespace.ui.flexible_items.ContactsListHeaderItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.utils.Log
import kotlinx.android.synthetic.main.fragment_contact_invites.*
import java.util.*
import javax.inject.Inject

class ContactInvitesFragment
    : BaseFragment<ContactInvitesFragment, ContactInvitesPresenter, ContactInvitesViewModel>(),
        ContactInvitesContract.ContactInvitesFragment,
        ContactsListHeaderItem.ContactsHeaderItemListener,
        ContactListItem.ContactListItemListener,
        AppInvitesActivity.AppInvitesFragmentInterface{

    @Inject
    override lateinit var viewModel: ContactInvitesViewModel
    lateinit var contactInvitesBinding: ContactInvitesBinding
    @Inject
    lateinit var mainListAdapter: FlexibleAdapter<ContactsListHeaderItem>

    private var input_finish_delay: Long = 1000 // 1 seconds after user stops typing
    private var input_finish_handler = Handler()
    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > viewModel.lastTextEdit + input_finish_delay - 500) {
//            presenter.searchOrganizationsAndGroups(et_search.text.toString())
//            onOpenSearch()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        contactInvitesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_contact_invites,
                container, false)
        contactInvitesBinding.viewModel = viewModel
        contactInvitesBinding.setLifecycleOwner(this)
        return contactInvitesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkForPermission()
        setupMainRecyclerViews()

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
            if (!viewModel.uiState.isSearchActive){ //if not active
//                onOpenSearch()
                //TODO: do filtering of contacts
                viewModel.uiState.isSearchActive = true
            }else{
//                onCloseSearch()
                et_search.text.clear()
                viewModel.uiState.isSearchActive = false
                //TODO: undo filtering of contacts
            }
        }

        button_ask_permission.setOnClickListener {
            if(!viewModel.uiState.contactPermission)
                checkForPermission()
        }

        btn_retry.setOnClickListener{
            viewModel.uiState.errorOccured = false
            if (!viewModel.contactsFetching)
                presenter.fetchContacts()
        }

        btn_skip.setOnClickListener{
            viewModel.uiState.errorOccured = false
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.uiState.contactPermission = true
                if (!viewModel.contactsFetching)
                    presenter.fetchContacts()
            }
        }
    }


    private fun setupMainRecyclerViews(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("MainAdapter")

        //HEADERS
        viewModel.headingsList =  ArrayList<ContactsListHeaderItem>().apply {
            this.add(TIMESPACE_USERS_LIST_POSITION,ContactsListHeaderItem(1, "Already using TimeSpace", 0).apply {
                listener = this@ContactInvitesFragment
                viewModel.members_header = this
            })
            this.add(NON_USERS_LIST_POSITION,ContactsListHeaderItem(2, "Other Contacts", 0).apply {
                listener = this@ContactInvitesFragment
                viewModel.non_members_header = this
            })
        }
        mainListAdapter.addItems(0, viewModel.headingsList)

        // Non-exhaustive configuration that don't need RV instance
        mainListAdapter.addListener(this) //Only if you didn't use the Constructor
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
        main_recycler_view.adapter = mainListAdapter
    }

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            viewModel.uiState.contactPermission = false
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)

        } else {
            viewModel.uiState.contactPermission = true
            if (!viewModel.contactsFetching)
                presenter.fetchContacts()
        }
    }

    override fun getContentResolver(): ContentResolver {
        return activity?.contentResolver!!
    }

    override fun onExpand(position: Int) {
        mainListAdapter.expand(position)
    }

    override fun onCollapse(position: Int){
        mainListAdapter.collapse(position)
    }

    override fun onContactClicked(user: User) {
        presenter.onContactClicked(user)
    }

    override fun onUnInvite(contactListItem: ContactListItem, holder: ContactListItem.MyViewHolder?, position: Int) {
        presenter.onUnInvite(contactListItem,holder,position)
    }

    override fun onUnFollow(contactListItem: ContactListItem, holder: ContactListItem.MyViewHolder, position: Int) {
        presenter.onUnFollow(contactListItem,holder,position)
    }

    override fun onFollow(contactListItem: ContactListItem, holder: ContactListItem.MyViewHolder, position: Int) {
        presenter.onFollow(contactListItem,holder,position)
    }

    override fun onInvite(contactListItem: ContactListItem, holder: ContactListItem.MyViewHolder, position: Int) {
        presenter.onInvite(contactListItem,holder,position)
    }

    override fun onSelectAll(header: ContactsListHeaderItem) {
        presenter.onSelectAll(header)
    }

    override fun onUnSelectAll(header: ContactsListHeaderItem) {
        presenter.onUnSelectAll(header)
    }

    override fun onFinished() {
        presenter.onFinished()
    }

    companion object {
        const val TIMESPACE_USERS_LIST_POSITION = 0
        const val NON_USERS_LIST_POSITION = 1
        const val PERMISSION_REQUEST_CODE = 1
    }
}
