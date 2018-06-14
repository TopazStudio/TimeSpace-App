package com.flycode.timespace.ui.auth.signup

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.flycode.timespace.R
import com.flycode.timespace.databinding.SignUpBinding
import com.flycode.timespace.ui.base.BaseFragment
import kotlinx.android.synthetic.main.sign_up_fragment.*
import javax.inject.Inject

class SignUpFragment
    : BaseFragment<SignUpFragment, SignUpPresenter, SignUpViewModel>(),
        SignUpContract.SignUpFragment{

    @Inject
    override lateinit var viewModel: SignUpViewModel

    private lateinit var signUpBinding: SignUpBinding
    private val SELECT_PHOTO = 1
    private val CAPTURE_PHOTO = 2
    private val PERMISSION_REQUEST_CODE = 3

    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        signUpBinding = DataBindingUtil.inflate(inflater,R.layout.sign_up_fragment, container, false)
        signUpBinding.viewModel = viewModel
        signUpBinding.setLifecycleOwner(this)
        return signUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sign_up_btn.setOnClickListener{
            if(checkPasswordRetype())
                presenter.signUp()
        }

        sign_in_text_btn.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.signUpFragment)
            //TODO: clean up
        }
    }

    private fun init(){
//        checkImagePermissions()
        setupProfilePic()
        profile_pic_progress_bar.progress = 0
        profile_pic_progress_bar.max = 100
    }

    private fun checkPasswordRetype(): Boolean{
        return if (et_password_retype.text.toString() != et_password.text.toString()){
            showError("The passwords do not match")
            false
        }else true
    }

    /**
     * Show custom Chooser dialog allowing to choose method of adding a photo
     * or removing the current one.
     *
     */
    fun setupProfilePic() {
        presenter.imageBitmap?.let {
            profile_pic.setImageBitmap(it)
        }
        profile_pic.setOnClickListener{
            context?.let {
                MaterialDialog.Builder(it)
                        .title("Add a progress photo")
                        .items(R.array.uploadImage)
                        .itemsIds(R.array.uploadImageItemIds)
                        .itemsCallback({ _, _, position, _ ->
                            when (position) {
                                0 -> {
                                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                                    photoPickerIntent.type = "image/*"
                                    startActivityForResult(photoPickerIntent, SELECT_PHOTO)
                                }
                                1 -> {
                                    val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    startActivityForResult(photoCaptureIntent, CAPTURE_PHOTO)
                                }
                                2 -> {
                                    profile_pic.setImageResource(R.drawable.image_placeholder)
                                    //Don't save image. The placeholder will be set by default
                                    presenter.doImageSave = false
                                    presenter.onClearImageBitmap()
                                }
                            }
                        })
                        .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == SELECT_PHOTO) {
                if (resultCode == RESULT_OK) {
                    presenter.onPickerImageResult(data)
                }
            } else if (requestCode == CAPTURE_PHOTO) {
                if (resultCode == RESULT_OK) {
                    presenter.onCaptureImageResult(data)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                profile_pic_section.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Show the progress bar on the progress photo
     * to indicate the image is being loaded.
     *
     */
    override fun showProgressBar() {
        profile_pic_progress_bar.visibility = View.VISIBLE
    }

    /**
     * Hide the progress bar on the progress photo
     * to indicate the image is done being loaded.
     *
     */
    override fun hideProgressBar() {
        profile_pic_progress_bar.visibility = View.GONE
    }

    override fun setPhotoProgress(progress: Int) {
        profile_pic_progress_bar.progress = progress
    }

    override fun setImageBitmap(imageBitmap: Bitmap) {
        profile_pic.maxWidth = 200
        profile_pic.setImageBitmap(imageBitmap)
    }

    override fun getContentResolver(): ContentResolver {
        return context?.contentResolver!!
    }

    /**
     * Check if the application has been granted access to the camera.
     * If not hide the progress image card view and try requesting for it.
     *
     */
    private fun checkImagePermissions() {
        if (context?.let {
                    ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA)
                } != PackageManager.PERMISSION_GRANTED) {

            profile_pic_section.visibility = View.GONE

            activity?.let {
                ActivityCompat.requestPermissions(
                        it,
                        arrayOf(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        PERMISSION_REQUEST_CODE
                )
            }

        } else {
            profile_pic_section.visibility = View.VISIBLE
        }
    }

}
