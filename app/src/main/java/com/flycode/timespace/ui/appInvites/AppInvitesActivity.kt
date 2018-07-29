package com.flycode.timespace.ui.appInvites

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.ui.appInvites.contactInvites.ContactInvitesFragment
import com.flycode.timespace.ui.appInvites.facebookInvites.FacebookInvitesFragment
import com.flycode.timespace.ui.appInvites.googleInvites.GoogleInvitesFragment
import com.flycode.timespace.ui.appInvites.selfInvites.SelfInvitesFragment
import com.flycode.timespace.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_app_invites.*
import javax.inject.Inject

class AppInvitesActivity
    : BaseFragment<AppInvitesActivity, AppInvitesPresenter, AppInvitesViewModel>(),
        AppInvitesContract.AppInvitesFragment {

    @Inject
    override lateinit var viewModel: AppInvitesViewModel
    @Inject
    lateinit var appInvitesViewPager: AppInvitesViewPager

    var listeners: ArrayList<AppInvitesFragmentInterface> = ArrayList<AppInvitesFragmentInterface>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_invites,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init(){
        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Find friends and invite new ones"

        setupViewPager()
        fab_done.setOnClickListener {
            listeners.forEach {
                it.onFinished()
            }
            presenter.onFinished()
        }
    }

    private fun setupViewPager() {
        appInvitesViewPager.addFragment(ContactInvitesFragment().apply {
            this@AppInvitesActivity.listeners.add(this)
        })
        appInvitesViewPager.addFragment(FacebookInvitesFragment())
        appInvitesViewPager.addFragment(GoogleInvitesFragment())
        appInvitesViewPager.addFragment(SelfInvitesFragment())

        view_pager.offscreenPageLimit = 3
        view_pager.adapter = appInvitesViewPager

        tab_layout.setupWithViewPager(view_pager)
        createTabIcons()
    }

    private fun createTabIcons() {
        val tabOne = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_phone_white_24dp)
        tab_layout.getTabAt(0)?.text = "Contacts"
        tab_layout.getTabAt(0)?.customView = tabOne

        val tabTwo = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_facebook)
        tab_layout.getTabAt(1)?.text = "Facebook"
        tab_layout.getTabAt(1)?.customView = tabTwo

        val tabThree = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        tab_layout.getTabAt(2)?.setIcon(R.drawable.ic_google)
        tab_layout.getTabAt(2)?.text = "Google"
        tab_layout.getTabAt(2)?.customView = tabThree

        val tabFour = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        tab_layout.getTabAt(3)?.setIcon(R.drawable.ic_edit_24dp)
        tab_layout.getTabAt(3)?.text = "Personal"
        tab_layout.getTabAt(3)?.customView = tabFour
    }

    interface AppInvitesFragmentInterface{
        fun onFinished()
    }

}
