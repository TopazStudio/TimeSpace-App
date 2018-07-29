package com.flycode.timespace.ui.main.groups.groupsView

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.ui.base.BaseViewModel

class GroupViewViewModel
    : BaseViewModel<GroupViewFragment, GroupViewPresenter>() {

    val uiState = UiState()

    class UiState : BaseObservable(){
        var group: Group = Group()

        @get: Bindable
        var createdOn: String = "3rd July 2018"
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var membersCount: String = "0"
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isJoined: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isPending: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        fun getJoinedState(): String{
            return when {
                isJoined -> "JOINED"
                isPending -> "PENDING"
                else -> "JOIN"
            }
        }
    }

}