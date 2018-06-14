package com.flycode.timespace.ui.base

import android.arch.lifecycle.ViewModel


open class BaseViewModel<V : BaseContract.View, P : BaseContract.Presenter<V>>
    : ViewModel(), BaseContract.ViewModel<V,P>{

    override var presenter: P? = null
        set(value){
            if (field == null) {
                field = value
            }
        }


    override fun onCleared() {
        super.onCleared()
        presenter!!.onPresenterDestroy()
        presenter = null
    }
}