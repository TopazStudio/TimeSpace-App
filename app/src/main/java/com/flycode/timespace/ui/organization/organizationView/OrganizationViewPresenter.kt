package com.flycode.timespace.ui.organization.organizationView

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.flycode.timespace.GetOrganizationGroupsQuery
import com.flycode.timespace.SearchGroupQuery
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.flexible_items.GroupListItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class OrganizationViewPresenter(
        val apolloClient: ApolloClient,
        val groupsListAdapter: FlexibleAdapter<IFlexible<*>>
) : BasePresenter<OrganizationViewActivity, OrganizationViewPresenter, OrganizationViewViewModel>()
        , OrganizationViewContract.OrganizationViewPresenter<OrganizationViewActivity> {

    fun init(){
        fetchOrganizationGroups(viewModel.organization?.id!!)
    }

    private fun fetchOrganizationGroups(id: Int) {
        /*view?.let {view ->
            viewModel.uiState.isSearchLoading = true
            Rx2Apollo.from(fetchGroups(id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        it.data()?.organization()!![0]!!.let {
                            viewModel.organization?.groups?.clear()
                            viewModel.organization?.groups = Gson().fromJson(
                                    Gson().toJson(it.groups()),
                                    object : TypeToken<MutableList<Group>>() {}.type
                            )

                            val groupSearchListItems =
                                    ArrayList<GroupListItem>().apply {
                                        this.addAll(viewModel.organization?.groups!!.map {
                                            GroupListItem(it).apply {
                                                this.listener = view
                                            }
                                        })
                                    }

                            if(groupSearchListItems.isNotEmpty())
                                groupsListAdapter.addItems(groupsListAdapter.itemCount,
                                        groupSearchListItems as List<IFlexible<*>>)

                            viewModel.uiState.isSearchLoading = false
                        }

                    },{
                        viewModel.uiState.isSearchLoading = false
                        if (it.message != null){
                            view.showError(message = it.message.toString())
                        }else{
                            view.showError("Something went wrong. Please try again.")
                        }
                    })
        }*/
    }

    fun searchGroups(term: String) {
        /*view?.let {view ->
            viewModel.uiState.isSearchLoading = true
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
                                            GroupListItem(it).apply {
                                                this.listener = view
                                            }
                                        })
                                    }

                            if(groupSearchListItems.isNotEmpty())
                                groupsListAdapter.addItems(groupsListAdapter.itemCount,
                                        groupSearchListItems as List<IFlexible<*>>)

                            viewModel.uiState.isSearchLoading = false
                        }

                    },{
                        viewModel.uiState.isSearchLoading = false
                        if (it.message != null){
                            view.showError(message = it.message.toString())
                        }else{
                            view.showError("Something went wrong. Please try again.")
                        }
                    })
        }*/
    }

    private fun searchGroup(term :String): ApolloQueryCall<SearchGroupQuery.Data> {
        return apolloClient.query(
                SearchGroupQuery
                        .builder()
                        .type("match")
                        .property("name")
                        .term(term)
                        .build()
        )
    }


    private fun fetchGroups(id :Int): ApolloQueryCall<GetOrganizationGroupsQuery.Data> {
        return apolloClient.query(
                GetOrganizationGroupsQuery
                        .builder()
                        .id(id.toString())
                        .build()
        )
    }

    fun onGroupClicked(group: Group) {
        //TODO: Create group view activity
    }

    fun onJoinGroup(groupListItem: GroupListItem,holder: GroupListItem.MyViewHolder?,position: Int){
    }

    fun onJoinedGroupRemoved(groupListItem: GroupListItem,holder: GroupListItem.MyViewHolder?,position: Int) {
    }

    fun onPendingGroupRemoved(groupListItem: GroupListItem, holder: GroupListItem.MyViewHolder?, position: Int) {
    }


}