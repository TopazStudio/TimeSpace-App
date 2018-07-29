package com.flycode.timespace.util.BindingAdapters

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.util.Log
import android.widget.EditText
import android.widget.TextView

/**
 * Convert an Double value to string value and set it on an
 * EditText view through the "android:text" attribute of the view.
 */
@BindingAdapter("android:text")
fun edSetDoubleText(view: EditText, value: Double) {
    //Make sure its not the same value already in the EditText
    if (view.text != null && !view.text.toString().isEmpty()) {
        //if there is a value already in the view check if its the same
        var a = 0.0

        try {
            a = java.lang.Double.parseDouble(view.text.toString())
        } catch (e: Exception) {
            Log.e("DoubleParseError", e.message)
        }

        if (a != value)
            view.setText(java.lang.Double.toString(value))
        else
            return
    } else
    //if no value exists then just add the new value
        view.setText(java.lang.Double.toString(value))
}

/**
 * Get and convert the value of an EditText view to an Double and return it
 * from a "android:text" attribute of the view.
 */
@InverseBindingAdapter(attribute = "android:text")
fun edsetDoubleText(view: EditText): Double {
    //Make sure your not converting an empty or null value
    if (view.text != null && !view.text.toString().isEmpty()) {
        try {
            return java.lang.Double.parseDouble(view.text.toString())
        } catch (e: Exception) {
            Log.e("DoubleParseError", e.message)
        }

    }
    return 0.0
}

/**
 * Convert an Double value to string value and set it on an
 * TextView view through the "android:text" attribute of the view.
 */
@BindingAdapter("android:text")
fun tvSetDoubleText(view: TextView, value: Double) {
    //Make sure its not the same value already in the TextView
    if (view.text != null && !view.text.toString().isEmpty()) {
        //if there is a value already in the view check if its the same
        var a = 0.0

        try {
            a = java.lang.Double.parseDouble(view.text.toString())
        } catch (e: Exception) {
            Log.e("DoubleParseError", e.message)
        }

        if (a != value)
            view.text = java.lang.Double.toString(value)
        else
            return
    } else
    //if no value exists then just add the new value
        view.text = java.lang.Double.toString(value)
}

/**
 * Get and convert the value of an TextView view to an Double and return it
 * from a "android:text" attribute of the view.
 */
@InverseBindingAdapter(attribute = "android:text")
fun tvGetDoubleText(view: TextView): Double {
    //Make sure your not converting an empty or null value
    if (view.text != null && !view.text.toString().isEmpty()) {
        try {
            return java.lang.Double.parseDouble(view.text.toString())
        } catch (e: Exception) {
            Log.e("DoubleParseError", e.message)
        }

    }
    return 0.0
}
