package com.flycode.timespace.ui.main.organization.organizationView

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.data.models.Organization
import com.flycode.timespace.ui.base.BaseViewModel

class OrganizationViewViewModel
    : BaseViewModel<OrganizationViewFragment, OrganizationViewPresenter>(){
    var organization: Organization? = Organization()
    var lastTextEdit: Long = 0

    var groupSearchResultsList: MutableList<Group> = ArrayList()

    val uiState = UiState()

    class UiState : BaseObservable(){
        @get: Bindable
        var memberSince: String = "3 years ago"
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
        var groupsCount: String = "0"
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var inSearchMode: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isSearchLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        /*@get: Bindable
        var isSearchOpen: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }*/

        @get: Bindable
        var resultCount: Int = 0
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