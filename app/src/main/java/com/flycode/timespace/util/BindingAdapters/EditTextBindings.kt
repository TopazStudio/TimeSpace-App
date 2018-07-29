package com.flycode.timespace.util.BindingAdapters

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.EditText


class EditTextBindings {

    @BindingAdapter("android:drawableEnd")
    fun setDrawableEnd(view: EditText, resourceId: Int) {
        val drawable = view.context.getDrawable(resourceId)
        setIntrinsicBounds(drawable)
        val drawables = view.compoundDrawables
        view.setCompoundDrawables(drawables[4], drawables[1], drawable, drawables[3])
    }

    private fun setIntrinsicBounds(drawable: Drawable?) {
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    }
}