package com.flycode.timespace.ui.auth.signup.ProfilePic

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.auth.signup.SignUpViewModel
import dagger.Module
import dagger.Provides

@Module
open class ProfilePicModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            superViewModel: SignUpViewModel
    ): ProfilePicPresenter
            = ProfilePicPresenter(
            superViewModel = superViewModel
    )

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            profilePicFragment: ProfilePicFragment,
            profilePicPresenter: ProfilePicPresenter
    ) : ProfilePicViewModel{
        val viewModel = ViewModelProviders.of(profilePicFragment).get(ProfilePicViewModel::class.java)
        profilePicPresenter.viewModel = viewModel
        viewModel.presenter = profilePicPresenter
        return viewModel
    }
}