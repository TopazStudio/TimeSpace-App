package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.flexible_items.SearchResultsHeaderItem

class MeetingAttendeesViewModel
    : BaseViewModel<MeetingAttendeesFragment, MeetingAttendeesPresenter>() {
    val uiState = UiState()
    var lastTextEdit: Long = 0

    var searchResultHeaderItem: SearchResultsHeaderItem = SearchResultsHeaderItem(resultCount = 0)
    var mainListHeaderItem: PlainHeaderItem = PlainHeaderItem(title = "Attending")

    class UiState : BaseObservable(){

        @get: Bindable
        var showEmptyAttendeesHint: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isInvitationHintOpen: Boolean = false
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
        var onMainError: Boolean = false
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
        var onSearchError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }
    }
}