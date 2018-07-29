package com.flycode.timespace.util.BindingAdapters

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.widget.RadioGroup


/**
     * Method to set gender value from the Model to the view.
     */
    @BindingAdapter("gender")
    fun setGender(view: RadioGroup, gender: String?) {
        //Check the gender given
        if (gender != null) {
//            if (gender == "Male") {
//                view.check(R.id.male_radio_btn)
//            } else if (gender == "Female") {
//                view.check(R.id.female_radio_btn)
//            }
        }
    }

    /**
     * Method to get gender value from the view to the Model.
     */
    @InverseBindingAdapter(attribute = "gender", event = "genderAttrChanged")
    fun getGender(view: RadioGroup): String {
//        return if (view.checkedRadioButtonId == R.id.male_radio_btn) {
//            "Male"
//        } else if (view.checkedRadioButtonId == R.id.female_radio_btn) {
//            "Female"
//        } else {
//            "Male"
//        }
        return ""
    }

    /**
     * Event to notify DataBinding framework when to get gender value from the view.
     */
    @BindingAdapter("genderAttrChanged")
    fun onGenderChange(view: RadioGroup, genderAttrChanged: InverseBindingListener?) {
        view.setOnCheckedChangeListener { group, checkedId ->
            genderAttrChanged?.onChange()
        }
    }

