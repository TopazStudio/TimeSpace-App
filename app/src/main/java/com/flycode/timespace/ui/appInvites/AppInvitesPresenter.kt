package com.flycode.timespace.ui.appInvites

import com.flycode.timespace.data.network.AppInvitesService
import com.flycode.timespace.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppInvitesPresenter(
        val appInvitesService: AppInvitesService
) : BasePresenter<AppInvitesActivity, AppInvitesPresenter, AppInvitesViewModel>(),
        AppInvitesContract.AppInvitesPresenter<AppInvitesActivity> {

    fun onFinished(){
        view?.let { view ->
            view.showLoading()
            compositeDisposable.add(
                appInvitesService
                    .commitAppInvites(
                            viewModel.inviteBy,
                            AppInvitesService.Users(viewModel.contacts)
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        view.hideLoading()
                        view.showMessage("Successful")
                    },{
                        view.hideLoading()
                        if (it.message != null){
                            view.showError(message = it.message.toString())
                        }else{
                            view.showError("Something went wrong. Please try again.")
                        }
                    })

            )

        }
    }
}