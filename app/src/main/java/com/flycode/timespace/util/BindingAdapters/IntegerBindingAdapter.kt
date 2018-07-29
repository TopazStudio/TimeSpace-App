package com.flycode.timespace.util.BindingAdapters

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.widget.EditText
import android.widget.TextView

    /**
     * Convert an integer value to string value and set it on an
     * EditText view through the "android:text" attribute of the view.
     */
    @BindingAdapter("android:text")
    fun setText(view: EditText, value: Int) {
        //Make sure its not the same value already in the EditText
        if (view.text != null
                && !view.text.toString().isEmpty()
                && Integer.parseInt(view.text.toString()) != value) {
            view.setText(Integer.toString(value))
        }else if(view.text == null && view.text.toString().isEmpty()){
            view.setText(Integer.toString(value))
        }
    }

    /**
     * Get and convert the value of an EditText view to an integer and return it
     * from a "android:text" attribute of the view.
     */
    @InverseBindingAdapter(attribute = "android:text")
    fun getText(view: EditText): Int {
        //Make sure your not converting an empty or null value
        return if (view.text != null && !view.text.toString().isEmpty()) {
            Integer.parseInt(view.text.toString())
        } else 0
    }

    /**
     * Convert an integer value to string value and set it on an
     * TextView view through the "android:text" attribute of the view.
     */
    @BindingAdapter("android:text")
    fun setText(view: TextView, value: Int) {
        //Make sure its not the same value already in the EditText
        if (view.text != null
                && !view.text.toString().isEmpty()
                && Integer.parseInt(view.text.toString()) != value) {
            view.text = Integer.toString(value)
        }else if(view.text == null && view.text.toString().isEmpty()){
            view.setText(Integer.toString(value))
        }
    }

    /**
     * Get and convert the value of an TextView view to an integer and return it
     * from a "android:text" attribute of the view.
     */
    @InverseBindingAdapter(attribute = "android:text")
    fun getText(view: TextView): Int {
        //Make sure your not converting an empty or null value
        return if (view.text != null && !view.text.toString().isEmpty()) {
            Integer.parseInt(view.text.toString())
        } else 0
    }
