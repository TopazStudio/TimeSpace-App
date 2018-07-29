package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.SearchUserQuery
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.flexible_items.PlainUserListItem
import com.flycode.timespace.ui.flexible_items.SearchResultsHeaderItem
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.ISectionable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class MeetingAttendeesPresenter(
        val apolloClient: ApolloClient,
        val searchResultsListAdapter: FlexibleAdapter<SearchResultsHeaderItem>,
        val mainListAdapter: FlexibleAdapter<PlainHeaderItem>,
        val superViewModel: MeetingEntryViewModel
) : BasePresenter<MeetingAttendeesFragment, MeetingAttendeesPresenter, MeetingAttendeesViewModel>(),
        MeetingAttendeesContract.MeetingAttendeesPresenter<MeetingAttendeesFragment> {

    private fun searchUser(term :String): ApolloQueryCall<SearchUserQuery.Data> {
        return apolloClient.query(
                SearchUserQuery
                        .builder()
                        .type("match")
                        .property("first_name")
                        .term(term)
                        .build()
        )
    }

    fun searchUsers(term: String) {
        view?.let {view ->
            viewModel.uiState.isSearchLoading = true
            Rx2Apollo.from(searchUser(term))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        it.data()?.searchUser()!!.let {
                            viewModel.usersSearchResultsList = Gson().fromJson(
                                    Gson().toJson(it.hits()),
                                    object : TypeToken<MutableList<User>>() {}.type
                            )

                            val userSearchListItems =
                                    ArrayList<PlainUserListItem>().apply {
                                        this.addAll(viewModel.usersSearchResultsList.map {
                                            PlainUserListItem(viewModel.searchResultHeaderItem,it).apply {
                                                this.listener = view
                                            }
                                        })
                                    }

                            if (userSearchListItems.isNotEmpty()){
                                viewModel.searchResultHeaderItem.subItems?.clear()
                                viewModel.searchResultHeaderItem.addSubItems(0,userSearchListItems as List<ISectionable<*, *>>)
                                viewModel.searchResultHeaderItem.resultCount = it.totalHits()?.toInt()!!
                                searchResultsListAdapter.notifyDataSetChanged()
                                searchResultsListAdapter.expandAll()

                                if (searchResultsListAdapter.hasNewFilter(term)){
                                    searchResultsListAdapter.setFilter(term)
                                    searchResultsListAdapter.filterItems()
                                }
                            }
//
                            viewModel.uiState.isSearchLoading = false

                        }

                    },{
                        viewModel.uiState.isSearchLoading = false
                        viewModel.uiState.onSearchError = true
                        if (it.message != null){
                            view.showError(message = it.message.toString())
                        }else{
                            view.showError("Something went wrong. Please try again.")
                        }
                    })
        }
    }

    fun addItemToMainList(plainUserListItem: PlainUserListItem){
        //Search in the main joined list
        if (viewModel.mainListHeaderItem.subItems != null) { //Something is there
            var found = false
            viewModel.mainListHeaderItem.subItems.forEach {
                if (it == plainUserListItem){ //if it is in the list
                    found = true
                }
            }
            if (!found){ //If not found add the item
                addSubItem(plainUserListItem)
                mainListAdapter.notifyDataSetChanged()
            }
        } else { //Nothing in the subItems. Add the item.
            addSubItem(plainUserListItem)
            mainListAdapter.notifyDataSetChanged()
        }
    }

    private fun addSubItem(plainUserListItem: PlainUserListItem){
        viewModel.mainListHeaderItem.addSubItem(viewModel.mainListHeaderItem.subItemsCount,
                PlainUserListItem(viewModel.mainListHeaderItem,
                        plainUserListItem.model,
                        view?.context
                ).apply {
                    listener = view
                }
        )
        viewModel.mainListHeaderItem.entries = 1 + viewModel.mainListHeaderItem.entries
    }
}