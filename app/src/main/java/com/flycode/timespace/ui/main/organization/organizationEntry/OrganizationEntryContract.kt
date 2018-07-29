package com.flycode.timespace.ui.main.organization.organizationEntry

import com.flycode.timespace.ui.base.BaseContract

interface OrganizationEntryContract {
    interface OrganizationEntryFragment : BaseContract.View{

    }

    interface OrganizationEntryPresenter<V : OrganizationEntryFragment> : BaseContract.Presenter<V>{

    }
}