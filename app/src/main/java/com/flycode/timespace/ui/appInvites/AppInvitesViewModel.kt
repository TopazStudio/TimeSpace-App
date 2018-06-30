package com.flycode.timespace.ui.appInvites

import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel

class AppInvitesViewModel: BaseViewModel<AppInvitesActivity, AppInvitesPresenter>(){
    var inviteBy: String = "email"
    val contacts: ArrayList<User> = ArrayList<User>()

}
