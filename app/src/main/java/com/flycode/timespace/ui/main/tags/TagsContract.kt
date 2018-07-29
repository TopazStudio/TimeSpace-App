package com.flycode.timespace.ui.main.tags

import com.flycode.timespace.ui.base.BaseContract

interface TagsContract {
    interface TagsFragment : BaseContract.View{

    }
    interface TagsPresenter<V : TagsFragment> : BaseContract.Presenter<V>{

    }
}