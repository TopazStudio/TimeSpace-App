package com.flycode.timespace.util.BindingAdapters

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.util.Log
import android.widget.EditText
import android.widget.TextView

    /**
     * Convert an float value to string value and set it on an
     * EditText view through the "android:text" attribute of the view.
     */
    @BindingAdapter("android:text")
    fun edSetText(view: EditText, value: Float) {
        //Make sure its not the same value already in the EditText
        if (view.text != null && !view.text.toString().isEmpty()) {
            //if there is a value already in the view check if its the same
            var a = 0f

            try {
                a = java.lang.Float.parseFloat(view.text.toString())
            } catch (e: Exception) {
                Log.e("FloatParseError", e.message)
            }

            if (a != value)
                view.setText(java.lang.Float.toString(value))
            else
                return
        } else
        //if no value exists then just add the new value
            view.setText(java.lang.Float.toString(value))
    }

    /**
     * Get and convert the value of an EditText view to an float and return it
     * from a "android:text" attribute of the view.
     */
    @InverseBindingAdapter(attribute = "android:text")
    fun edGetText(view: EditText): Float {
        //Make sure your not converting an empty or null value
        if (view.text != null && !view.text.toString().isEmpty()) {
            try {
                return java.lang.Float.parseFloat(view.text.toString())
            } catch (e: Exception) {
                Log.e("FloatParseError", e.message)
            }

        }
        return 0f
    }

    /**
     * Convert an float value to string value and set it on an
     * TextView view through the "android:text" attribute of the view.
     */
    @BindingAdapter("android:text")
    fun tvSetText(view: TextView, value: Float) {
        //Make sure its not the same value already in the TextView
        if (view.text != null && !view.text.toString().isEmpty()) {
            //if there is a value already in the view check if its the same
            var a = 0f

            try {
                a = java.lang.Float.parseFloat(view.text.toString())
            } catch (e: Exception) {
                Log.e("FloatParseError", e.message)
            }

            if (a != value)
                view.text = java.lang.Float.toString(value)
            else
                return
        } else
        //if no value exists then just add the new value
            view.text = java.lang.Float.toString(value)
    }

    /**
     * Get and convert the value of an TextView view to an float and return it
     * from a "android:text" attribute of the view.
     */
    @InverseBindingAdapter(attribute = "android:text")
    fun tvGetText(view: TextView): Float {
        //Make sure your not converting an empty or null value
        if (view.text != null && !view.text.toString().isEmpty()) {
            try {
                return java.lang.Float.parseFloat(view.text.toString())
            } catch (e: Exception) {
                Log.e("FloatParseError", e.message)
            }

        }
        return 0f
    }
