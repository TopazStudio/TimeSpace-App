package com.flycode.timespace.ui.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import com.flycode.timespace.di.component.DaggerPresenterComponent
import io.reactivex.disposables.CompositeDisposable


open class BaseServicePresenter<S : BaseServiceContract.Service>
    : LifecycleObserver, BaseServiceContract.Presenter<S>{
    val compositeDisposable = CompositeDisposable()
    protected val utilityWrapper = UtilityWrapper()
    override var service: S? = null
    override val isServiceAttached: Boolean
        get() = service != null

    init {
        DaggerPresenterComponent.create().inject(utilityWrapper)
    }

    override fun attachLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun detachLifecycle(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    override fun attachService(service: S) {
        this.service = service
    }

    override fun detachService() {
        service = null
    }

    override fun onPresenterDestroy() {
        compositeDisposable.dispose()
    }
}
