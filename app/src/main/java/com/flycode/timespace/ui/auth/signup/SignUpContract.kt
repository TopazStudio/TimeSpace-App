package com.flycode.timespace.ui.auth.signup

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.flycode.timespace.ui.base.BaseContract

interface SignUpContract {
    interface SignUpFragment : BaseContract.View{
        fun hideProgressBar()
        fun setPhotoProgress(progress: Int)
        fun setImageBitmap(imageBitmap: Bitmap)
        fun showProgressBar()
        fun getContentResolver(): ContentResolver
        fun getContext(): Context?
    }
    interface SignUpPresenter<V : SignUpFragment> : BaseContract.Presenter<V>{
        var imageBitmap: Bitmap?
        var doImageSave: Boolean
        fun signUp()
        fun onCaptureImageResult(data: Intent)
        fun onPickerImageResult(data: Intent)
    }
}