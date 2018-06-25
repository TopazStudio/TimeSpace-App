package com.flycode.timespace.ui.base

import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.os.Bundle


interface BaseContract {

    interface View{
        /**
         * Finish activity and go to next activity
         * */
        fun navigateToActivity(from: Context?, to: Class<*>)

        /**
         * Navigate to another activity for results.
         */
        fun openForResult(next: Class<*>, requestCode: Int, data: Bundle? = null)

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

    interface Presenter<V : View> {

        val view: V?

        val isViewAttached: Boolean

        fun attachLifecycle(lifecycle: Lifecycle)

        fun detachLifecycle(lifecycle: Lifecycle)

        fun attachView(view: V)

        fun detachView()

        fun onPresenterDestroy()
    }

    interface ViewModel<V : View, P : Presenter<V>>{

        var presenter: P?

    }
}