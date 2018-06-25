package com.flycode.timespace.ui.appInvites

import com.flycode.timespace.ui.base.BaseContract

interface AppInvitesContract {
    interface AppInvitesFragment : BaseContract.View
    interface AppInvitesPresenter<V : AppInvitesFragment> : BaseContract.Presenter<V>
}