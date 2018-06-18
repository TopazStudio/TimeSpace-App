package com.flycode.timespace.ui.auth.signup.ProfilePic

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.flycode.timespace.R
import com.flycode.timespace.ui.auth.signup.SignUpViewModel
import com.flycode.timespace.ui.base.BasePresenter
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.lang.Exception

class ProfilePicPresenter(
        var superViewModel: SignUpViewModel
) : BasePresenter<ProfilePicFragment, ProfilePicPresenter, ProfilePicViewModel>(),
        ProfilePicContract.ProfilePicPresenter<ProfilePicFragment>{

    val imageViewTarget : Target = object : com.squareup.picasso.Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            bitmap?.let {
                superViewModel.doImageSave = true
                superViewModel.imageBitmap = it
                view?.setImageBitmap(it)
            }
        }
    }

    override fun clearImageBitmap(){
        superViewModel.doImageSave = false
        superViewModel.imageBitmap = null
    }

    override fun setupProfilePic(){
        superViewModel.imageBitmap?.let {
            view?.setImageBitmap(it)
        }
        if(!superViewModel.user.pictures.isEmpty() && !superViewModel.user.pictures[0].remote_location.isEmpty()){
           loadImageFromRemote()
        }
    }

    private fun loadImageFromRemote() {
        Picasso.get()
                .load(superViewModel.user.pictures[0].remote_location)
                .placeholder(R.drawable.image_placeholder)
                .into(imageViewTarget)
    }

    override fun onCaptureImageResult(data: Intent) {
        view?.let { view ->
            view.showProgressBar()
            compositeDisposable.add(
                    Observable.create(ObservableOnSubscribe<String> { emitter ->
                        superViewModel.imageBitmap = data.extras.get("data") as Bitmap
                        view.setPhotoProgress(100)
                        emitter.onComplete()
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({

                            },{ throwable ->
                                view.showError(throwable.message!!)
                            },{
                                view.setImageBitmap(superViewModel.imageBitmap!!)
                                superViewModel.doImageSave = true
                                view.hideProgressBar()
                            })
            )
        }
    }

    override fun onPickerImageResult(data: Intent) {
        view?.let { view ->
            view.showProgressBar()
            compositeDisposable.add(
                    Observable.create(ObservableOnSubscribe<String> { emitter ->
                        val imageUri = data.data
                        view.setPhotoProgress(30)

                        val imageStream: InputStream = view.getContentResolver().openInputStream(imageUri)!!

                        view.setPhotoProgress(50)

                        superViewModel.imageBitmap = BitmapFactory.decodeStream(imageStream)

                        view.setPhotoProgress(100)
                        emitter.onComplete()
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({

                            },{
                                throwable -> view.showError(throwable.message!!)
                            },{
                                view.setImageBitmap(superViewModel.imageBitmap!!)
                                superViewModel.doImageSave = true
                                view.hideProgressBar()
                            })
            )
        }
    }

}