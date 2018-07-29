package com.flycode.timespace.ui.main.tags

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
class TagsModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            tagsAdapter: TagsAdapter,
            apolloClient: ApolloClient
    ): TagsPresenter = TagsPresenter(
            tagsAdapter = tagsAdapter,
            apolloClient = apolloClient
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            tagsFragment: TagsFragment,
            tagsPresenter: TagsPresenter
    ): TagsViewModel {
        val viewModel = ViewModelProviders.of(tagsFragment).get(TagsViewModel::class.java)
        tagsPresenter.viewModel = viewModel
        viewModel.presenter = tagsPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideTagsAdapter(
            tagsFragment: TagsFragment
    ): TagsAdapter = TagsAdapter(
            context = tagsFragment.context!!
    )
}