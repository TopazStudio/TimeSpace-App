package com.flycode.timespace.ui.appInvites

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import com.flycode.timespace.R
import com.flycode.timespace.ui.appInvites.contactInvites.ContactInvitesFragment
import com.flycode.timespace.ui.appInvites.facebookInvites.FacebookInvitesFragment
import com.flycode.timespace.ui.appInvites.googleInvites.GoogleInvitesFragment
import com.flycode.timespace.ui.appInvites.selfInvites.SelfInvitesFragment
import com.flycode.timespace.ui.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_app_invites.*
import javax.inject.Inject

class AppInvitesActivity
    : BaseActivity<AppInvitesActivity, AppInvitesPresenter, AppInvitesViewModel>(),
        AppInvitesContract.AppInvitesFragment {

    @Inject
    override lateinit var viewModel: AppInvitesViewModel
    @Inject
    lateinit var appInvitesViewPager: AppInvitesViewPager

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_app_invites)

        setSupportActionBar(toolbar as Toolbar)

        init()
    }

    private fun init(){
        setupViewPager()
    }

    private fun setupViewPager() {
        appInvitesViewPager.addFragment(ContactInvitesFragment())
        appInvitesViewPager.addFragment(FacebookInvitesFragment())
        appInvitesViewPager.addFragment(GoogleInvitesFragment())
        appInvitesViewPager.addFragment(SelfInvitesFragment())

        view_pager.offscreenPageLimit = 3
        view_pager.adapter = appInvitesViewPager

        tab_layout.setupWithViewPager(view_pager)
        createTabIcons()
    }

    private fun createTabIcons() {
        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_phone_white_24dp)
        tab_layout.getTabAt(0)?.text = "Contacts"
        tab_layout.getTabAt(0)?.customView = tabOne

        val tabTwo = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_facebook)
        tab_layout.getTabAt(1)?.text = "Facebook"
        tab_layout.getTabAt(1)?.customView = tabTwo

        val tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
        tab_layout.getTabAt(2)?.setIcon(R.drawable.ic_google)
        tab_layout.getTabAt(2)?.text = "Google"
        tab_layout.getTabAt(2)?.customView = tabThree

        val tabFour = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
        tab_layout.getTabAt(3)?.setIcon(R.drawable.ic_edit_24dp)
        tab_layout.getTabAt(3)?.text = "Personal"
        tab_layout.getTabAt(3)?.customView = tabFour
    }

}
