package com.flycode.timespace.ui.main.organization.organizationView

import com.flycode.timespace.ui.base.BaseContract

interface OrganizationViewContract {
    interface OrganizationViewFragment : BaseContract.View{

    }
    interface OrganizationViewPresenter<V : OrganizationViewFragment> : BaseContract.Presenter<V>{

    }
}