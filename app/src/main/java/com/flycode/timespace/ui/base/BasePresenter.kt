package com.flycode.timespace.ui.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import android.support.annotation.CallSuper
import com.flycode.timespace.data.models.User
import com.flycode.timespace.di.component.DaggerPresenterComponent
import com.raizlabs.android.dbflow.config.DatabaseDefinition
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

abstract class BasePresenter<V : BaseContract.View, P : BaseContract.Presenter<V>, C : BaseContract.ViewModel<V,P>>
    : LifecycleObserver,
        BaseContract.Presenter<V> {

    val compositeDisposable = CompositeDisposable()
    protected val utilityWrapper = UtilityWrapper()
    protected var stateBundle: Bundle = Bundle()
    lateinit var viewModel : C
    override var view: V? = null

    init {
        DaggerPresenterComponent.create().inject(utilityWrapper)
    }

    override val isViewAttached: Boolean
        get() = view != null

    override fun attachLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun detachLifecycle(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    @CallSuper
    override fun onPresenterDestroy() {
        if (!stateBundle.isEmpty) {
            stateBundle.clear()
        }
        compositeDisposable.dispose()
    }
}


open class UtilityWrapper{
    @field: Inject
    lateinit var database: DatabaseDefinition

    @field: [Inject Named("default_user")]
    lateinit var defaultUser: User
}
