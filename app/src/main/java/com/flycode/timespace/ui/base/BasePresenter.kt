package com.flycode.timespace.ui.base

import com.flycode.timespace.data.models.User
import com.flycode.timespace.di.component.DaggerPresenterComponent
import com.raizlabs.android.dbflow.config.DatabaseDefinition
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

/**
 * Base class of presenters. Provides methods managing the view presenter
 * relationship.
 */
open class BasePresenter<V : MvpView>(val mvpView: V){
    val compositeDisposable = CompositeDisposable()
    protected val utilityWrapper = UtilityWrapper()

    init {
        DaggerPresenterComponent.create().inject(utilityWrapper)
    }
}

open class UtilityWrapper{
    @field: Inject
    lateinit var database: DatabaseDefinition

    @field: [Inject Named("default_user")]
    lateinit var defaultUser: User
}
