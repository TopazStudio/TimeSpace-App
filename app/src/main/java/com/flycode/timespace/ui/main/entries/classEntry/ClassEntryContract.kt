package com.flycode.timespace.ui.main.entries.classEntry

import com.flycode.timespace.ui.base.BaseContract

interface ClassEntryContract {
    interface ClassEntryFragment : BaseContract.View{

    }

    interface ClassEntryPresenter<V : ClassEntryFragment> : BaseContract.Presenter<V>{

    }
}