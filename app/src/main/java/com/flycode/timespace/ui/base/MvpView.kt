package com.flycode.timespace.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Base interface that every view must implement.
 * Carries common functionality that every view must have
 */
interface MvpView {

    /**
     * Finish activity and go to next activity
     * */
    fun navigateToActivity(from: Context?, to: Class<*>)

    /**
     * Navigate current fragment destination to the next fragment destination
     * */
    fun navigateToFragment(from : Fragment?, to: Int)

    /**
     * Navigate to another activity for results.
     */
    fun openForResult(next: Class<*>, requestCode: Int, data: Bundle?)

    /**
     * Show loading animation. Loading animation can be customized
     * here.
     */
    fun showLoading()

    /**
     * Hide loading animation
     */
    fun hideLoading()

    /**
     * Display message to user. Info Message can be customized here.
     */
    fun showMessage(message: String)

    /**
     * Display error message to user. Error message can be customized here
     */
    fun showError(message: String)

}
