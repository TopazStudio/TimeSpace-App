package com.flycode.timespace.ui.appInvites.contactInvites

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem

class ContactInvitesViewModel: BaseViewModel<ContactInvitesFragment, ContactInvitesPresenter>() {
    val user : User = User()
    var lastTextEdit: Long = 0
    var headingsList = java.util.ArrayList<ExpandableHeaderItem>()
    var contactList = java.util.ArrayList<User>()

    var contactsFetched: Boolean= false

    val uiState = UiState()
    class UiState : BaseObservable(){
        @get: Bindable
        var contactPermission: Boolean = false
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

        @get: Bindable
        var isSearchActive: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var resultCount: Int = 0
            set(value) {
                field = value
                notifyChange()
            }
    }
}