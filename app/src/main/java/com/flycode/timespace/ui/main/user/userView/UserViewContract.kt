package com.flycode.timespace.ui.main.user.userView

import com.flycode.timespace.ui.base.BaseContract

interface UserViewContract {
    interface UserViewFragment : BaseContract.View
    interface UserViewPresenter<V : UserViewFragment> : BaseContract.Presenter<V>
}