package com.flycode.timespace.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.Nullable
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>, C : BaseContract.ViewModel<V, P>>
    : DaggerAppCompatActivity(),
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
        startActivity(Intent(this,to))
        finish()
    }

    override fun openForResult(next: Class<*>, requestCode: Int, data: Bundle?) {
        this.currentRequestCode = requestCode
        val i = Intent(this, next)
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        //TODO: implement a better way of showing messages
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachLifecycle(lifecycle)
        presenter.detachView()
    }

}
