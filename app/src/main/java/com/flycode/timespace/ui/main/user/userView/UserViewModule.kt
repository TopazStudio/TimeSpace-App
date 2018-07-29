package com.flycode.timespace.ui.main.user.userView

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
class UserViewModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
    ): UserViewPresenter {
        return UserViewPresenter(
        )
    }

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            UserViewFragment : UserViewFragment,
            userViewPresenter: UserViewPresenter
    ): UserViewViewModel {
        val viewModel = ViewModelProviders.of(UserViewFragment).get(UserViewViewModel::class.java)
        userViewPresenter.viewModel = viewModel
        viewModel.presenter = userViewPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideUserViewViewPager(
            UserViewActivity: UserViewFragment
    ): UserViewViewPager = UserViewViewPager(UserViewActivity.childFragmentManager)
}