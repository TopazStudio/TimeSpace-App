package com.flycode.timespace.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.Nullable
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerFragment

abstract class BaseFragment<V : BaseContract.View, P : BaseContract.Presenter<V>, C : BaseContract.ViewModel<V,P>>
    : DaggerFragment(),
        BaseContract.View {

    private var currentRequestCode: Int = 0
    protected lateinit var presenter: P
    protected open lateinit var viewModel: C

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
    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachLifecycle(lifecycle)
        presenter.detachView()
    }

}