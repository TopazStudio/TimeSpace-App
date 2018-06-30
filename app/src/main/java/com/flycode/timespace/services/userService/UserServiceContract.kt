package com.flycode.timespace.services.userService

import com.flycode.timespace.ui.base.BaseServiceContract

interface UserServiceContract {
    interface UserService : BaseServiceContract.Service {
        fun showNewFollowerNotification(data: String)
    }

    interface UserServicePresenter<S : UserService> : BaseServiceContract.Presenter<S>
}