package com.flycode.timespace.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.flycode.timespace.R
import com.flycode.timespace.databinding.BaseActivityBinding
import com.flycode.timespace.databinding.BaseNavDrawerHeadingBindings
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity(){

    //Todo: find a way to make non null completely.
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var baseActivityBinding : BaseActivityBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivityBinding =  DataBindingUtil.setContentView(this,R.layout.activity_base_nav)

        //DRAWER HEADING
        val baseNavDrawerHeadingBindings: BaseNavDrawerHeadingBindings = DataBindingUtil
                .inflate(
                        layoutInflater,
                        R.layout.base_navigation_drawer_header,
                        null,
                        false
                )
//        baseNavDrawerHeadingBindings.setUser(user)

        //DRAWER LISTENER
        actionBarDrawerToggle = ActionBarDrawerToggle(
                this,
                baseActivityBinding.baseDrawerLayout,
                R.string.navigation_open,
                R.string.navigation_close
        )
        baseActivityBinding.baseDrawerLayout.addDrawerListener(actionBarDrawerToggle)

        //NAV VIEW
        baseActivityBinding.baseNavView.addHeaderView(baseNavDrawerHeadingBindings.root)
        baseActivityBinding.baseNavView.setupWithNavController(findNavController(R.id.base_nav_fragment))
    }

    override fun onSupportNavigateUp() = findNavController(R.id.base_nav_fragment).navigateUp()

    public override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    private fun toggleDrawer() {
        if (baseActivityBinding.baseDrawerLayout.isDrawerOpen(baseActivityBinding.baseNavView)) {
            baseActivityBinding.baseDrawerLayout.closeDrawer(baseActivityBinding.baseNavView)
        } else
            baseActivityBinding.baseDrawerLayout.openDrawer(baseActivityBinding.baseNavView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                toggleDrawer()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
