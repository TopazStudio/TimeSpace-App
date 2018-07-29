package com.flycode.timespace.ui.main.user.userView

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.data.models.User
import com.flycode.timespace.databinding.UserViewBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.google.gson.Gson
import javax.inject.Inject

class UserViewFragment
    : BaseFragment<UserViewFragment, UserViewPresenter, UserViewViewModel>()
        , UserViewContract.UserViewFragment {

    @Inject
    override lateinit var viewModel: UserViewViewModel
    @Inject
    lateinit var userViewViewPager: UserViewViewPager
    lateinit var userViewBinding: UserViewBinding

    var listeners: ArrayList<UserViewFragmentInterface> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userViewBinding = DataBindingUtil.inflate(inflater,R.layout.activity_user_view,container,false)
        userViewBinding.viewModel = viewModel
        userViewBinding.setLifecycleOwner(this)

        (activity as AppCompatActivity).setSupportActionBar(userViewBinding.toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()

        return userViewBinding.root
    }

    private fun init(){
        arguments?.let {
            if (it.getString("user") != null){
                viewModel.uiState.user = Gson().fromJson(it.getString("user"), User::class.java)
                presenter.init()
            }/*else finish()*/
        }
        userViewBinding.btnCall.setOnClickListener { presenter.callUser() }
        userViewBinding.btnEmail.setOnClickListener { presenter.sendEmail() }
    }

    private fun setupViewPager() {
//        groupViewViewPager.addFragment(UserGroupsFragment().apply {
//            this@UserViewFragment.listeners.add(this)
//        })
//
//        groupViewViewPager.addFragment(UserTimetablesFragment().apply {
//            this@UserViewFragment.listeners.add(this)
//        })

        userViewBinding.viewPager.offscreenPageLimit = 3
        userViewBinding.viewPager.adapter = userViewViewPager

        userViewBinding.tabLayout.setupWithViewPager(userViewBinding.viewPager)
        createTabIcons()
    }

    private fun createTabIcons() {
        val tabOne = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        userViewBinding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_group_white_24dp)
        userViewBinding.tabLayout.getTabAt(0)?.text = "Groups"
        userViewBinding.tabLayout.getTabAt(0)?.customView = tabOne

        val tabTwo = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        userViewBinding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_calendar_white)
        userViewBinding.tabLayout.getTabAt(1)?.text = "TimeTables"
        userViewBinding.tabLayout.getTabAt(1)?.customView = tabTwo
        
    }

    override fun onPermissionsGranted(requestCode: Int) {
        super.onPermissionsGranted(requestCode)
        when(requestCode){
            CALL_USER_REQUEST_CODE -> presenter.onCallPermissionsGranted()
        }
    }

    interface UserViewFragmentInterface{

    }

    companion object {
        const val SEND_EMAIL_REQUEST_CODE: Int = 1
        const val CALL_USER_REQUEST_CODE: Int = 2
    }
}
