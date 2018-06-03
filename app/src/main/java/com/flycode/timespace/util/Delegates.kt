package com.flycode.timespace.util

import android.app.Activity
import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import kotlin.reflect.KProperty


/**
 * Delegate for inflating the content view of an Activity and
 * returning a reference to its ViewDataBinding class.
 *
 * */
class SetContentView<in R: Activity, out T: ViewDataBinding>(
        @LayoutRes private val layoutRes: Int
){
    private var value : T? = null

    operator fun getValue(thisRef: R, property: KProperty<*>): T {
        value = value ?: DataBindingUtil.setContentView<T>(thisRef,layoutRes)
        return value!!
    }
}
fun <R: Activity, T: ViewDataBinding> setContentViewLazy(@LayoutRes layoutRes: Int): SetContentView<R,T>{
    return SetContentView(layoutRes)
}

/**
 * Delegate for observable fields inside an Observable Model used
 * for data binding
 *
 * */
class BindableDelegate<in R: BaseObservable, T: Any>(
        private var value: T,
        private val bindingEntry: Int
){
    operator fun getValue(thisRef: R, property: KProperty<*>): T = value
    operator fun setValue(thisRef: R, property: KProperty<*>, value: T){
        this.value = value
        thisRef.notifyPropertyChanged(bindingEntry)
    }
}
fun <R : BaseObservable, T: Any> bindable(value: T, bindingRes: Int): BindableDelegate<R,T>{
    return BindableDelegate(value,bindingRes)
}