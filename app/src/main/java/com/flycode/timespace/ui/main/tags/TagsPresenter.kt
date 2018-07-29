package com.flycode.timespace.ui.main.tags

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.*
import com.flycode.timespace.data.models.Tag
import com.flycode.timespace.type.Tag_Input
import com.flycode.timespace.ui.base.BasePresenter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TagsPresenter(
        val tagsAdapter: TagsAdapter,
        val apolloClient: ApolloClient
): BasePresenter<TagsFragment, TagsPresenter, TagsViewModel>(),
        TagsContract.TagsPresenter<TagsFragment>  {
    
    fun addTag(tag: Tag) {
        view?.let { view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                Rx2Apollo.from(
                    apolloClient.mutate(
                        AddTagMutation.builder()
                        .tag(
                            Tag_Input.builder()
                                .id(tag.id.toString())
                                .name(tag.name)
                                .color(tag.color.toLong())
                                .description(tag.description)
                                .owner_id(1.toString())
                                .build()
                        )
                        .build()
                    )
                )
                .subscribeOn(Schedulers.io())
//                .flatMap {
//                    if (it.data() != null)
//                        Observable.just(Gson().fromJson(Gson().toJson(it.data()?.tag()),Tag::class.java).insert())
//                    else Observable.error(Throwable(view.resources.getString(R.string.something_went_wrong)))
//                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    if (it.data() != null) {
                        view.showMessage(view.resources.getString(R.string.successfully_updated))
                        tagsAdapter.addTag(tag)
                    } else {
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                    viewModel.uiState.isLoading = false
                },{
                    viewModel.uiState.isLoading = false
                    viewModel.uiState.onError = true
                    it.printStackTrace()
                    if (it.message != null){
                        view.showError(it.message.toString())
                    }else{
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                })
            )
        }
    }

    fun deleteTag(tag: Tag) {
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                Rx2Apollo.from(
                    apolloClient.mutate(
                        DeleteTagMutation.builder()
                        .tag(
                                Tag_Input.builder()
                                        .id(tag.id.toString())
                                        .name(tag.name)
                                        .color(tag.color.toLong())
                                        .description(tag.description)
                                        .owner_id(1.toString())
                                        .build()
                        )
                        .build()
                    )
                )
                .subscribeOn(Schedulers.io())
//                .flatMap {
//                    if (it.data() != null){
//                        val t = Gson().fromJson(Gson().toJson(it.data()?.tag()),Tag::class.java)
//                        Observable.just(t.delete())
//                    }
//                    else Observable.error(Throwable(view.resources.getString(R.string.something_went_wrong)))
//
//                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    if (it.data() != null) {
                        view.showMessage(view.resources.getString(R.string.successfully_updated))
                        tagsAdapter.deleteTag(tag)
                    } else {
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                    viewModel.uiState.isLoading = false
                },{
                    viewModel.uiState.isLoading = false
                    viewModel.uiState.onError = true
                    it.printStackTrace()
                    if (it.message != null){
                        view.showError(it.message.toString())
                    }else{
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                })
            )
        }
    }

    /**
     * Fetches all users tags.
     */
    fun fetchTags() {
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(apolloClient.query(
                            GetUserTagsQuery.builder().build()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.user()!![0]?.owned_tags()?.let {
                                    val tags = Gson().fromJson<List<Tag>>(Gson().toJson(it),
                                            object : TypeToken<List<Tag>>(){}.type
                                    )
                                    tagsAdapter.addMultipleTags(tags)
                                }
                                viewModel.uiState.isLoading = false
                            },{
                                viewModel.uiState.isLoading = false
                                viewModel.uiState.onError = true
                                it.printStackTrace()
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
//        SQLite.select()
//                .from<Tag>(Tag::class.java)
//                .async()
//                .queryListResultCallback { transaction, tResult ->
//                    tagsAdapter.addMultipleTags(tResult)
//                }
//                .error {  transaction,throwable ->
//                    view?.showError(throwable.message!!)
//                }
//                .execute()
    }

    fun updateTag(tag: Tag, index: Int) {
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                Rx2Apollo.from(
                    apolloClient.mutate(
                        UpdateTagMutation.builder()
                            .tag(
                                    Tag_Input.builder()
                                            .id(tag.id.toString())
                                            .name(tag.name)
                                            .color(tag.color.toLong())
                                            .description(tag.description)
                                            .owner_id(1.toString())
                                            .build()
                            )
                            .build()
                    )
                )
                .subscribeOn(Schedulers.io())
//                .flatMap {
//                    if (it.data() != null)
//                        Observable.just(Gson().fromJson(Gson().toJson(it.data()?.tag()),Tag::class.java).save())
//                    else Observable.error(Throwable(view.resources.getString(R.string.something_went_wrong)))
//                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    if (it.data() != null) {
                        view.showMessage(view.resources.getString(R.string.successfully_updated))
                        tagsAdapter.updateTag(tag, index)
                    } else {
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                    viewModel.uiState.isLoading = false
                },{
                    viewModel.uiState.isLoading = false
                    viewModel.uiState.onError = true
                    it.printStackTrace()
                    if (it.message != null){
                        view.showError(it.message.toString())
                    }else{
                        view.showError(view.resources.getString(R.string.something_went_wrong))
                    }
                })
            )
        }

    }
}