package com.flycode.timespace.ui.main.groups.groupsOverview

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.data.models.Organization
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseViewModel
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem
import com.flycode.timespace.ui.flexible_items.SearchResultsHeaderItem

open class GroupsOverviewViewModel
    : BaseViewModel<GroupsOverviewFragment, GroupsOverviewPresenter>(){
    //TODO: get currently logged in user
    val user : User = User().apply {
        this.id = 23
    }
    var lastTextEdit: Long = 0
    var organizationSearchResultsList: MutableList<Organization> = ArrayList()
    var groupSearchResultsList: MutableList<Group> = ArrayList()
    var headingsList = java.util.ArrayList<ExpandableHeaderItem>()
    val uiState = UiState()
    var searchResultHeaderItem: SearchResultsHeaderItem = SearchResultsHeaderItem(resultCount = uiState.resultCount)

    class UiState : BaseObservable(){
        @get: Bindable
        var isSearchHintOpen: Boolean = true
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
        var isSearchOpen: Boolean = false
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