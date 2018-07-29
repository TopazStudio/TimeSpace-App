package com.flycode.timespace.services.groupService

import com.flycode.timespace.data.models.Group
import com.flycode.timespace.ui.base.BaseServiceContract

interface GroupServiceContract {
    interface GroupService : BaseServiceContract.Service {
        val group: Group
    }

    interface GroupServicePresenter<S : GroupService> : BaseServiceContract.Presenter<S>
}