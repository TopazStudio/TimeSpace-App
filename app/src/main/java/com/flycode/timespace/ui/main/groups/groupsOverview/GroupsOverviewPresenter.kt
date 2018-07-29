package com.flycode.timespace.ui.main.groups.groupsOverview

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.R
import com.flycode.timespace.SearchGroupQuery
import com.flycode.timespace.SearchOrganizationQuery
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.data.models.GroupMembership
import com.flycode.timespace.data.models.Organization
import com.flycode.timespace.data.network.GroupService
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem
import com.flycode.timespace.ui.flexible_items.GroupListItem
import com.flycode.timespace.ui.flexible_items.OrganizationListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raizlabs.android.dbflow.kotlinextensions.delete
import com.raizlabs.android.dbflow.kotlinextensions.insert
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.ISectionable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

//TODO: deal with groupmembership
class GroupsOverviewPresenter(
        val apolloClient: ApolloClient,
        val searchResultsListAdapter: FlexibleAdapter<ISectionable<*,*>>,
        val mainListAdapter: FlexibleAdapter<ExpandableHeaderItem>,
        val groupService: GroupService
)
    : BasePresenter<GroupsOverviewFragment, GroupsOverviewPresenter, GroupsOverviewViewModel>(),
        GroupsOverviewContract.OrientationPresenter<GroupsOverviewFragment> {

    private var numberOfSearches = 2
    private var numberOfSearchesFinished = 0

    fun searchOrganizationsAndGroups(term: String){
        viewModel.uiState.isSearchLoading = true
        viewModel.uiState.resultCount = 0
        searchResultsListAdapter.clear()
        searchOrganizations(term)
        searchGroups(term)
    }

    private fun searchOrganizations(term: String) {
        view?.let {view ->
            Rx2Apollo.from(searchOrganization(term))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        it.data()?.searchOrganization()!!.let {
                            viewModel.organizationSearchResultsList = Gson().fromJson(
                                    Gson().toJson(it.hits()),
                                    object : TypeToken<MutableList<Organization>>() {}.type
                            )
                            viewModel.uiState.resultCount = viewModel.uiState.resultCount + it.totalHits()?.toInt()!!

                            val organizationSearchListItems =
                                    ArrayList<OrganizationListItem>().apply {
                                this.addAll(viewModel.organizationSearchResultsList.map {
                                    OrganizationListItem(viewModel.searchResultHeaderItem,it).apply {
                                        this.listener = view
                                    }
                                })
                            }
                            if(organizationSearchListItems.isNotEmpty())
                                searchResultsListAdapter.addItems(searchResultsListAdapter.itemCount,
                                        organizationSearchListItems as List<ISectionable<*, *>>)
                            stopLoading()
                        }

                    },{
                        stopLoading()
                        if (it.message != null){
                            view.showError(message = it.message.toString())
                        }else{
                            view.showError("Something went wrong. Please try again.")
                        }
                    })
        }
    }

    private fun searchGroups(term: String) {
        view?.let {view ->
            Rx2Apollo.from(searchGroup(term))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        it.data()?.searchGroup()!!.let {
                            viewModel.uiState.resultCount = viewModel.uiState.resultCount + it.totalHits()?.toInt()!!
                            viewModel.groupSearchResultsList = Gson().fromJson(
                                    Gson().toJson(it.hits()),
                                    object : TypeToken<MutableList<Group>>() {}.type
                            )

                            val groupSearchListItems =
                                    ArrayList<GroupListItem>().apply {
                                        this.addAll(viewModel.groupSearchResultsList.map {
                                            GroupListItem(viewModel.searchResultHeaderItem,it,context = view.context).apply {
                                                this.listener = view
                                            }
                                        })
                                    }

                            if(groupSearchListItems.isNotEmpty())
                                searchResultsListAdapter.addItems(searchResultsListAdapter.itemCount,
                                        groupSearchListItems as List<ISectionable<*, *>>)

                            stopLoading()
                        }

                    },{
                        stopLoading()
                        if (it.message != null){
                            view.showError(message = it.message.toString())
                        }else{
                            view.showError("Something went wrong. Please try again.")
                        }
                    })
        }
    }

    private fun stopLoading(){
        numberOfSearchesFinished++
        if (numberOfSearchesFinished == numberOfSearches)
        {
            viewModel.uiState.isSearchLoading = false
            numberOfSearchesFinished = 0
        }
    }

    private fun searchOrganization(term :String): ApolloQueryCall<SearchOrganizationQuery.Data>{
        return apolloClient.query(
                SearchOrganizationQuery
                        .builder()
                        .type("match")
                        .property("name")
                        .term(term)
                        .build()
        )
    }

    private fun searchGroup(term :String): ApolloQueryCall<SearchGroupQuery.Data>{
        return apolloClient.query(
                SearchGroupQuery
                        .builder()
                        .type("match")
                        .property("name")
                        .term(term)
                        .build()
        )
    }

    fun onOrganizationClicked(organization: Organization) {
//        view?.openForResult(OrganizationViewFragment::class.java,
//                GroupsOverviewFragment.ORGANIZATION_VIEW_RESULT_CODE,
//                Bundle().apply {
//                    this.putString("organization",Gson().toJson(organization))
//                })
    }

    fun onGroupClicked(group: Group) {
        NavHostFragment.findNavController(view!!).navigate(R.id.GroupViewFragment, Bundle().apply {
            this.putString("group",Gson().toJson(group))
        })
    }

    fun onJoinedGroupRemoved(groupListItem: GroupListItem,holder: GroupListItem.MyViewHolder?,position: Int) {
        view?.let {view ->
            holder?.join_request_progress_bar?.visibility = View.VISIBLE
            holder?.btn_join?.visibility = View.GONE

            compositeDisposable.add(groupService.leaveGroup(GroupMembership().apply {
                    this.user_id_2 = viewModel.user.id
                    this.group_id_2 = groupListItem.model.id
                })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    when(it.data.join_status){
                        "LEFT" -> {
                            onGroupLeftReply(groupListItem,position, GroupsOverviewFragment.JOINED_LIST_POSITION)

                            Observable.just(it.data.delete())
                                    .subscribeOn(Schedulers.io())
                        }
                        else -> {
                            Observable.error<Boolean>(Throwable(it.errors.asJsonObject.get("onGroupLeftReply").asString))
                                    .subscribeOn(Schedulers.io())
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    holder?.join_request_progress_bar?.visibility = View.GONE
                    holder?.btn_join?.visibility = View.VISIBLE
                    if (it){
                        view.showMessage(view.resources.getString(R.string.un_registered_successfully))
                    }else{
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                },{
                    holder?.join_request_progress_bar?.visibility = View.GONE
                    holder?.btn_join?.visibility = View.VISIBLE
                    if (it.message != null){
                        view.showError(message = it.message.toString())
                    }else{
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                })
            )
        }
    }

    fun onJoinGroup(groupListItem: GroupListItem,holder: GroupListItem.MyViewHolder?,position: Int) {
        view?.let {view ->
            holder?.join_request_progress_bar?.visibility = View.VISIBLE
            holder?.btn_join?.visibility = View.GONE

            compositeDisposable.add(
                groupService.joinGroup(GroupMembership().apply {
                this.user_id_2 = viewModel.user.id
                this.group_id_2= groupListItem.model.id
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .flatMap {

                    when(it.data.join_status){
                        "PENDING" -> {
                            //Update the one in the search list
                            onPendingGroupReply(groupListItem,position)

                            Observable.just(it.data.insert())
                                    .subscribeOn(Schedulers.io())
                        }
                        "JOINED" -> {
                            //Update the one in the search list
                            onJoinedGroupReply(groupListItem,position)

                            Observable.just(it.data.insert())
                                    .subscribeOn(Schedulers.io())
                        }
                        "REJECTED" -> {
                            onRejectedGroupReply(groupListItem,position)

                            Observable.just(it.data.insert())
                                    .subscribeOn(Schedulers.io())
                        }
                        else -> {
                            Observable.error<Boolean>(Throwable(it.errors.asJsonObject.get("onGroupLeftReply").asString))
                                    .subscribeOn(Schedulers.io())
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                holder?.join_request_progress_bar?.visibility = View.GONE
                holder?.btn_join?.visibility = View.VISIBLE

                if (it != null){
                    view.showMessage(view.resources.getString(R.string.requested_successfully))
                }else{
                    view.showError(view.resources.getString(R.string.something_went_wrong))
                }
            },{
                holder?.join_request_progress_bar?.visibility = View.GONE
                holder?.btn_join?.visibility = View.VISIBLE

                it.printStackTrace()
                if (it.message != null){
                    view.showError(message = it.message.toString())
                }else{
                    view.showError(view.resources.getString(R.string.something_went_wrong))
                }
            })
            )
        }
    }

    fun onPendingGroupRemoved(groupListItem: GroupListItem, holder: GroupListItem.MyViewHolder?, position: Int) {
        view?.let {view ->
            holder?.join_request_progress_bar?.visibility = View.VISIBLE
            holder?.btn_join?.visibility = View.GONE

            compositeDisposable.add(
                groupService.leaveGroup(GroupMembership().apply {
                    this.user_id_2 = viewModel.user.id
                    this.group_id_2 = groupListItem.model.id
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    when(it.data.join_status){
                        "LEFT" -> {
                            onGroupLeftReply(groupListItem, position, GroupsOverviewFragment.PENDING_LIST_POSITION)

                            Observable.just(it.data.delete())
                                    .subscribeOn(Schedulers.io())
                        }
                        else -> {
                            Observable.error<Boolean>(Throwable(it.errors.asJsonObject.get("onGroupLeftReply").asString))
                                    .subscribeOn(Schedulers.io())
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    holder?.join_request_progress_bar?.visibility = View.GONE
                    holder?.btn_join?.visibility = View.VISIBLE

                    if (it){
                        view.showMessage(view.resources.getString(R.string.un_registered_successfully))
                    }else{
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                },{
                    holder?.join_request_progress_bar?.visibility = View.GONE
                    holder?.btn_join?.visibility = View.VISIBLE

                    it.printStackTrace()
                    if (it.message != null){
                        view.showError(message = it.message.toString())
                    }else{
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                })
            )
        }
    }

    private fun onGroupLeftReply(groupListItem: GroupListItem, position: Int, headerPosition: Int){
        groupListItem.joinState = "NOT_JOINED"

        //Search in the search results
        if (searchResultsListAdapter.itemCount > 0){ //has something
            searchResultsListAdapter.currentItems.forEach {
                if (it == groupListItem){ //if it is the group item
                    searchResultsListAdapter.updateItem(groupListItem)
                }
            }
        }
        //Search in the main list and remove
        val headerItem = mainListAdapter.getItem(headerPosition)!!

        if (headerItem.subItems != null){
            headerItem.subItems.find {
                it == groupListItem
            }?.let {
                headerItem.removeSubItem(it)

                //remove from main list. When expanded there are two instances.
                if (mainListAdapter.getItem(position) !is ExpandableHeaderItem){
                    mainListAdapter.removeItem(position)
                }
                headerItem.entries = headerItem.entries - 1
            }

            mainListAdapter.notifyDataSetChanged()
        }
    }

    private fun onJoinedGroupReply(groupListItem: GroupListItem, position: Int){
        groupListItem.joinState = "JOINED" //Change the state

        //Search in the search results
        if (searchResultsListAdapter.itemCount > 0){ //has something
            searchResultsListAdapter.currentItems.forEach {
                if (it == groupListItem){ //if it is the group item
                    searchResultsListAdapter.updateItem(groupListItem)
                }
            }
        }
        //Search in the main joined list
        val headerItem = mainListAdapter.getItem(GroupsOverviewFragment.JOINED_LIST_POSITION)!!
        if (headerItem.subItems != null) { //Something is there
            var found = false
            headerItem.subItems.forEach {
                if (it == groupListItem){ //if it is in the list
                    found = true
                    mainListAdapter.notifyDataSetChanged()
                }
            }
            if (!found){ //If not found add the item
                addSubItem(groupListItem, headerItem, "JOINED")
                mainListAdapter.notifyDataSetChanged()
            }
        } else { //Nothing in the subItems. Add the item.
            addSubItem(groupListItem,headerItem,"JOINED")
            mainListAdapter.notifyDataSetChanged()
        }
    }

    private fun onRejectedGroupReply(groupListItem: GroupListItem, position: Int){
        groupListItem.joinState = "REJECTED"

        //Search in the search results
        if (searchResultsListAdapter.itemCount > 0){ //has something
            searchResultsListAdapter.currentItems.forEach {
                if (it == groupListItem){ //if it is the group item
                    searchResultsListAdapter.updateItem(groupListItem)
                }
            }
        }

        //Search in the main list and notify
        val headerItem = mainListAdapter.getItem(GroupsOverviewFragment.JOINED_LIST_POSITION)!!
        val headerItem2 = mainListAdapter.getItem(GroupsOverviewFragment.PENDING_LIST_POSITION)!!

        if (headerItem.subItems != null)
            headerItem.subItems.forEach {
                if (it == groupListItem){ //if it is in the list
                    mainListAdapter.notifyDataSetChanged()
                }
            }
        if (headerItem2.subItems != null)
            headerItem.subItems.forEach {
                if (it == groupListItem){ //if it is in the list
                    mainListAdapter.notifyDataSetChanged()
                }
            }

    }

    private fun onPendingGroupReply(groupListItem: GroupListItem,position: Int){
        groupListItem.joinState = "PENDING" //Change the state

        //Search in the search results
        if (searchResultsListAdapter.itemCount > 0){ //has something
            searchResultsListAdapter.currentItems.forEach {
                if (it == groupListItem){ //if it is the group item
                    searchResultsListAdapter.updateItem(groupListItem)
                }
            }
        }
        //Search in the main pending list
        val headerItem = mainListAdapter.getItem(GroupsOverviewFragment.PENDING_LIST_POSITION)!!
        if (headerItem.subItems != null) { //Something is there
            var found = false
            headerItem.subItems.forEach {
                if (it == groupListItem){ //if it is in the list
                    found = true
                    mainListAdapter.notifyDataSetChanged()
                }
            }
            if (!found){ //If not found add the item
                addSubItem(groupListItem, headerItem, "PENDING")
                mainListAdapter.notifyDataSetChanged()
            }
        } else { //Nothing in the subItems. Add the item.
            addSubItem(groupListItem,headerItem,"PENDING")
            mainListAdapter.notifyDataSetChanged()
        }

    }

    private fun addSubItem(groupListItem: GroupListItem, headerItem: ExpandableHeaderItem,joinState: String){
        headerItem.addSubItem(headerItem.subItemsCount,
                GroupListItem(headerItem,
                        groupListItem.model,
                        joinState,
                        view?.context
                ).apply {
                    listener = view
                }
        )
        headerItem.entries = 1 + headerItem.entries
    }

}