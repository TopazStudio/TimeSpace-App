package com.flycode.timespace.ui.auth.signup.ProfilePic

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.flycode.timespace.ui.base.BaseContract

interface ProfilePicContract {
    interface ProfilePicFragment : BaseContract.View{
        fun hideProgressBar()
        fun setPhotoProgress(progress: Int)
        fun setImageBitmap(imageBitmap: Bitmap)
        fun showProgressBar()
        fun getContentResolver(): ContentResolver
        fun getContext(): Context?
    }

    interface ProfilePicPresenter<V : ProfilePicContract.ProfilePicFragment> : BaseContract.Presenter<V>{
        fun setupProfilePic()
        fun clearImageBitmap()
        fun onCaptureImageResult(data: Intent)
        fun onPickerImageResult(data: Intent)
    }
}