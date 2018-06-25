package com.flycode.timespace.ui.organization.organizationView

import com.flycode.timespace.ui.base.BaseContract

interface OrganizationViewContract {
    interface OrganizationViewActivity : BaseContract.View{

    }
    interface OrganizationViewPresenter<V : OrganizationViewActivity> : BaseContract.Presenter<V>{

    }
}