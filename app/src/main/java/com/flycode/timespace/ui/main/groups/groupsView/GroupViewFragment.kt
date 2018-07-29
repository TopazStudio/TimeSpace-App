package com.flycode.timespace.ui.main.groups.groupsView

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.databinding.GroupViewBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.base.BaseServiceContract
import com.flycode.timespace.ui.main.groups.groupsView.groupActivity.GroupActivityFragment
import com.flycode.timespace.ui.main.groups.groupsView.groupMembers.GroupMembersFragment
import com.flycode.timespace.ui.main.groups.groupsView.groupTimeTables.GroupTimetablesFragment
import com.google.gson.Gson
import javax.inject.Inject

class GroupViewFragment  
    : BaseFragment<GroupViewFragment, GroupViewPresenter, GroupViewViewModel>(),
        GroupViewContract.GroupViewFragment, BaseServiceContract.ServiceListener {

    @Inject
    override lateinit var viewModel: GroupViewViewModel
    @Inject
    lateinit var groupViewViewPager: GroupViewViewPager
    lateinit var groupViewBinding: GroupViewBinding

    var listeners: ArrayList<GroupViewFragmentInterface> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        groupViewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_group_view,container,false)
        groupViewBinding.viewModel = viewModel
        groupViewBinding.setLifecycleOwner(this)

        (activity as AppCompatActivity).setSupportActionBar(groupViewBinding.toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()

        return groupViewBinding.root
    }

    private fun init(){
        arguments?.let {
            if (it.getString("group") != null){
                viewModel.uiState.group = Gson().fromJson(it.getString("group"), Group::class.java)
                presenter.init()
            }/*else finish()*/
        }
        setupViewPager()
    }

    private fun setupViewPager() {
        groupViewViewPager.addFragment(GroupActivityFragment().apply {
            this@GroupViewFragment.listeners.add(this)
        })

        groupViewViewPager.addFragment(GroupTimetablesFragment().apply {
            this@GroupViewFragment.listeners.add(this)
        })

        groupViewViewPager.addFragment(GroupMembersFragment().apply {
            this@GroupViewFragment.listeners.add(this)
        })


        groupViewBinding.viewPager.offscreenPageLimit = 3
        groupViewBinding.viewPager.adapter = groupViewViewPager

        groupViewBinding.tabLayout.setupWithViewPager(groupViewBinding.viewPager)
        createTabIcons()
    }

    private fun createTabIcons() {
        val tabOne = LayoutInflater.from(context).inflate(R.layout.custom_tab_pic_only, null)
        groupViewBinding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_history)
        groupViewBinding.tabLayout.getTabAt(0)?.customView = tabOne

        val tabTwo = LayoutInflater.from(context).inflate(R.layout.custom_tab_pic_only, null)
        groupViewBinding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_calendar_white)
        groupViewBinding.tabLayout.getTabAt(1)?.customView = tabTwo

        val tabThree = LayoutInflater.from(context).inflate(R.layout.custom_tab_pic_only, null)
        groupViewBinding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_group_white_24dp)
        groupViewBinding.tabLayout.getTabAt(2)?.customView = tabThree

    }

//    override fun onPermissionsGranted(requestCode: Int) {
//        super.onPermissionsGranted(requestCode)
//        when(requestCode){
//
//        }
//    }

    interface GroupViewFragmentInterface{

    }

    override fun onError(error: String) {
        showError(error)
    }

    override fun onSuccess(message: String) {
        showMessage(message)
    }

    override fun onFinish(success: Boolean, data: Bundle) {
        if (success) showMessage("Successful")
    }

    companion object {
        @JvmStatic
        fun newInstance() = GroupViewFragment()
    }
}
