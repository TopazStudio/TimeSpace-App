package com.flycode.timespace.ui.main.groups.groupsView.groupMembers

import com.flycode.timespace.ui.base.BaseContract

interface GroupMembersContract {
    interface GroupMembersFragment : BaseContract.View{

    }

    interface GroupMembersPresenter<V : GroupMembersFragment> : BaseContract.Presenter<V>{

    }
}