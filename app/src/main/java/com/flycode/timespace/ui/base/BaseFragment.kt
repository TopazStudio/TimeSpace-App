package com.flycode.timespace.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.CallSuper
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.SparseIntArray
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.flycode.timespace.R
import dagger.android.support.DaggerFragment

abstract class BaseFragment<V : BaseContract.View, P : BaseContract.Presenter<V>, C : BaseContract.ViewModel<V,P>>
    : DaggerFragment(),
        BaseContract.View {

    private var currentRequestCode: Int = 0
    protected lateinit var presenter: P
    protected open lateinit var viewModel: C
    private val mErrorString: SparseIntArray? = null

    @CallSuper
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = viewModel.presenter!!
        presenter.attachLifecycle(lifecycle)
        presenter.attachView(this as V)
    }

    override fun navigateToActivity(from: Context?, to: Class<*>) {
        startActivity(Intent(from,to))
        activity?.finish()
    }

    fun navigateToFragment(to: Int){
        NavHostFragment.findNavController(this).navigate(to)
    }

    override fun openForResult(next: Class<*>, requestCode: Int, data: Bundle?) {
        this.currentRequestCode = requestCode
        val i = Intent(context, next)
        if (data != null) {
            i.putExtras(data)
        }
        startActivityForResult(i, requestCode)
    }

    override fun showLoading() {
        hideLoading()
        //TODO: implement activity loading
    }

    override fun hideLoading() {
        //TODO: implement activity loading
    }

    override fun showMessage(message: String) {
        //TODO: implement a better way of showing messages
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        //TODO: implement a better way of showing messages
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun hideSoftKeyboard(){
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun showSoftKeyboard(){
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    open fun onPermissionsGranted(requestCode: Int){
        showMessage(resources.getString(R.string.permissions_granted))
    }

    fun requestAppPermissions(requestedPermissions: Array<String>, stringId: Int, requestCode: Int) {
        mErrorString?.put(requestCode, stringId)
        activity?.let { activity->
            var permissionCheck = PackageManager.PERMISSION_GRANTED
            var showRequestPermissions = false
            for (permission in requestedPermissions) {
                permissionCheck += ContextCompat.checkSelfPermission(activity, permission)
                showRequestPermissions = showRequestPermissions
                        || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            }

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (showRequestPermissions) {
                    Snackbar.make(activity.findViewById<View>(android.R.id.content)!!, stringId, Snackbar.LENGTH_INDEFINITE)
                            .setAction("GRANT") {
                                ActivityCompat.requestPermissions(activity, requestedPermissions, requestCode)
                            }.show()
                } else {
                    ActivityCompat.requestPermissions(activity, requestedPermissions, requestCode)
                }
            } else {
                onPermissionsGranted(requestCode)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        for (permisson in grantResults) {
            permissionCheck += permisson
        }
        activity?.let { activity ->
            if (grantResults.isNotEmpty() && PackageManager.PERMISSION_GRANTED == permissionCheck) {
                onPermissionsGranted(requestCode)
            } else {
                //Display message when contain some Dangerous permisson not accept
                Snackbar.make(activity.findViewById<View>(android.R.id.content), mErrorString?.get(requestCode)!!,
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE") {
                    val i = Intent()
                    i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    i.data = Uri.parse("package:${activity.packageName}")
                    i.addCategory(Intent.CATEGORY_DEFAULT)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    startActivity(i)
                }.show()
            }
        }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachLifecycle(lifecycle)
        presenter.detachView()
    }

}