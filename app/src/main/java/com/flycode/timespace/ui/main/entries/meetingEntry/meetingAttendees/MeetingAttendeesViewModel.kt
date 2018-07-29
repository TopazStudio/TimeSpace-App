package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.flexible_items.SearchResultsHeaderItem

class MeetingAttendeesViewModel
    : BaseViewModel<MeetingAttendeesFragment, MeetingAttendeesPresenter>() {
    val uiState = UiState()
    var lastTextEdit: Long = 0

    var usersSearchResultsList: MutableList<User> = ArrayList()
    var searchResultHeaderItem: SearchResultsHeaderItem = SearchResultsHeaderItem(resultCount = 0)
    var mainListHeaderItem: PlainHeaderItem = PlainHeaderItem()

    class UiState : BaseObservable(){
        @get: Bindable
        var isInvitationHintOpen: Boolean = true
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
        var isMainLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onMainError: Boolean = true
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isSearchOpen: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onSearchError: Boolean = true
            set(value) {
                field = value
                notifyChange()
            }
    }
}