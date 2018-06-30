package com.flycode.timespace.ui.appInvites.contactInvites

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.flexible_items.ContactListItem
import com.flycode.timespace.ui.flexible_items.ContactsListHeaderItem

class ContactInvitesViewModel: BaseViewModel<ContactInvitesFragment, ContactInvitesPresenter>() {
    val user : User = User()
    var lastTextEdit: Long = 0

    var headingsList = java.util.ArrayList<ContactsListHeaderItem>()
    lateinit var members_header: ContactsListHeaderItem
    lateinit var non_members_header: ContactsListHeaderItem

    var contactList = java.util.ArrayList<User>()
    var contactsFoundList  = java.util.ArrayList<User>()
    var contactsNotFoundList  = java.util.ArrayList<User>()

    var contactsFetched: Boolean= false
    var contactsFetching: Boolean= false
    var contactsSentToServer: Boolean= false

    var contactsFoundListItems = java.util.ArrayList<ContactListItem>()
    var contactsNotFoundListItems = java.util.ArrayList<ContactListItem>()
    val uiState = UiState()

    class UiState : BaseObservable(){
        @get: Bindable
        var errorOccured: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var contactPermission: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isContactsLoading: Boolean = false
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