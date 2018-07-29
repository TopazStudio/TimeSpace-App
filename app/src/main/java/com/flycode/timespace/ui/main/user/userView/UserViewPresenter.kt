package com.flycode.timespace.ui.main.user.userView

import android.Manifest
import android.content.Intent
import android.net.Uri
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BasePresenter



class UserViewPresenter
    : BasePresenter<UserViewFragment, UserViewPresenter, UserViewViewModel>()
        , UserViewContract.UserViewPresenter<UserViewFragment> {

    fun init(){
        getCurrentUserSettings()
    }

    private fun getCurrentUserSettings(){

    }

    fun onCallPermissionsGranted(){
        view?.let {view ->

            //Creating intents for making a call
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:${viewModel.uiState.user.tel}")
            view.startActivity(callIntent)

        }
    }

    fun callUser() {
        view?.let {view ->
            view.requestAppPermissions(
                    arrayOf(
                            Manifest.permission.CALL_PHONE
                    ),
                    R.string.we_need_permission_to_function, UserViewFragment.CALL_USER_REQUEST_CODE)
        }
    }

    fun sendEmail(){
        val intent = Intent(Intent.ACTION_SEND)//common intent
        intent.data = Uri.parse("mailto:${viewModel.uiState.user.email}") // only email apps should handle this
        view?.startActivityForResult(intent, UserViewFragment.SEND_EMAIL_REQUEST_CODE)
    }
}