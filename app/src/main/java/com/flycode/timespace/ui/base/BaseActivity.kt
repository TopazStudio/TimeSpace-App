package com.flycode.timespace.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerAppCompatActivity

open class BaseActivity : DaggerAppCompatActivity(), MvpView {

    private var currentRequestCode: Int = 0

    override fun navigateToActivity(from: Context?, to: Class<*>) {
        startActivity(Intent(this,to))
        finish()
    }

    override fun navigateToFragment(from: Fragment?, to: Int){
        NavHostFragment.findNavController(from!!).navigate(to)
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
        //TODO: implement a better way of showing errors
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
