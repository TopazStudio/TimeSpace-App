package com.flycode.timespace.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
    protected val PERMISSION_REQUEST_CODE = 2 //Fragment Request code

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

    open fun onPermissionsDenied(requestCode: Int, permissions: Array<String>, grantResults: IntArray){
        showError(resources.getString(R.string.no_permissions))
    }

    fun requestAppPermissions(requestedPermissions: Array<String>, stringId: Int, requestCode: Int) {
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
                                requestPermissions(requestedPermissions, requestCode)
                            }.show()
                } else {
                    requestPermissions(requestedPermissions, requestCode)
                }
            } else {
                onPermissionsGranted(requestCode)
            }
        }

    }

    @CallSuper
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            var permissionCheck = PackageManager.PERMISSION_GRANTED
            for (permission in grantResults) {
                permissionCheck += permission
            }

            if (grantResults.isNotEmpty() && PackageManager.PERMISSION_GRANTED == permissionCheck) {
                onPermissionsGranted(requestCode)
            } else {
                onPermissionsDenied(requestCode,permissions,grantResults)
            }
        }else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachLifecycle(lifecycle)
        presenter.detachView()
    }

}