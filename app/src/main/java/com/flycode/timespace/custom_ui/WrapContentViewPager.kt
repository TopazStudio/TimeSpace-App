package com.flycode.timespace.custom_ui

import android.content.Context
import android.graphics.Point
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import com.flycode.timespace.R
import java.util.*

class WrapContentViewPager : ViewPager {

    private var defaultToWindow: Boolean = false

    private var windowSize: Point? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.WrapContentViewPager,
                0, 0)
        try {
            defaultToWindow = a.getBoolean(R.styleable.WrapContentViewPager_defaultToWindow, true)

        } finally {
            a.recycle()
        }
        init()
    }

    private fun init() {
        //GET ATTRIBUTES
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = Objects.requireNonNull(windowManager).defaultDisplay
        windowSize = Point()
        display.getSize(windowSize)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val child = getChildAt(currentItem)
        if (child != null) {
            //Tell child it can be as tall as it wants
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            //Get the height that the child measured
            val h = child.measuredHeight

            //TODO: If the child is height than the available screen height add the screen height as the measure spec
            //Tell super that it needs to be exactly what the child wants
            //            int windowHeight = getContext()
            if (h < windowSize!!.y && defaultToWindow) {
                //Smaller than window
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(windowSize!!.y, View.MeasureSpec.EXACTLY)
            } else
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


}