package com.flycode.timespace.ui.appInvites.contactInvites

import android.provider.ContactsContract
import com.flycode.timespace.data.models.Picture
import com.flycode.timespace.data.models.Response
import com.flycode.timespace.data.models.User
import com.flycode.timespace.data.network.retrofit.AppInvitesService
import com.flycode.timespace.ui.appInvites.AppInvitesViewModel
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.flexible_items.ContactListItem
import com.flycode.timespace.ui.flexible_items.ContactsListHeaderItem
import com.google.i18n.phonenumbers.PhoneNumberUtil
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.ISectionable
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ContactInvitesPresenter(
        val appInvitesService: AppInvitesService,
        val mainListAdapter: FlexibleAdapter<ContactsListHeaderItem>,
        val superViewModel: AppInvitesViewModel
) : BasePresenter<ContactInvitesFragment,ContactInvitesPresenter, ContactInvitesViewModel>(),
        ContactInvitesContract.ContactInvitesPresenter<ContactInvitesFragment> {

    override fun fetchContacts() {
        view?.let { view ->
            viewModel.uiState.isContactsLoading = true
            viewModel.contactsFetching = true
            compositeDisposable.add(
                fetchContactsFromPhone() //FETCH CONTACTS FROM PHONE
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap{
                    viewModel.contactList = it
                    viewModel.contactsFetched = true
                    if (!viewModel.contactsSentToServer) //IF NOT SENT
                        appInvitesService.searchForFriends(AppInvitesService.Users(viewModel.contactList))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    else Observable.create<Response<ArrayList<User>>> { it.onComplete() }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) //SEND TO SERVER
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable {
                    viewModel.contactsSentToServer = true
                    viewModel.contactsFoundList = it.data
                    viewModel.contactsNotFoundList = viewModel.contactList.filter { user1 -> //FIND CONTACTS NOT RETURNED BY SERVER
                        var found = false
                        viewModel.contactsFoundList.forEach {
                            found = it.id == user1.id || it.email == user1.email
                        }
                        !found
                    } as java.util.ArrayList<User>

                    addFoundContactsFromServer()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    viewModel.contactsFetching = false
                    viewModel.uiState.isContactsLoading = false
                    mainListAdapter.notifyDataSetChanged()
                    mainListAdapter.expandAll()
                    view.showError("Contacts fetched successfully")
                },{
                    viewModel.contactsFetching = false
                    viewModel.uiState.isContactsLoading = false
                    viewModel.uiState.errorOccured = true
                    if (it.message != null){
                        view.showError(message = it.message.toString())
                    }else{
                        view.showError("Something went wrong. Please try again.")
                    }
                })

            )

        }

    }

    private fun fetchContactsFromPhone(): Observable<ArrayList<User>> {
        return Observable.create<ArrayList<User>> { emitter ->
            val array = ArrayList<User>()
            if (!viewModel.contactsFetched){ //IF NOT FETCHED
                val contactsCursor = view!!.getContentResolver()
                        .query(ContactsContract.Contacts.CONTENT_URI,
                                null,
                                ContactsContract.Data.DISPLAY_NAME + " = ?",
                                Array(1){"Erick Kirabui"},
                                " _id LIMIT 1"
                        )
                if (contactsCursor.count > 0){
                    while (contactsCursor.moveToNext()){
                        array.add(
                                User().apply {
                                    this.id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)).toInt()
                                    this.first_name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) ?: "No name"
                                    contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))?.let {
                                        this.pictures.add(Picture().apply {
                                            this.local_location = it
                                        })
                                    }

                                    //get email
                                    val emailCursor = view!!.getContentResolver()
                                            .query(
                                                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                                    null,
                                                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                                    Array(1){this.id.toString()},
                                                    null
                                            )
                                    while (emailCursor.moveToNext()){
                                        //PARSE TO A SEARCHABLE TEXT
                                        this.email = this.email + emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)) + ";"
                                    }
                                    emailCursor.close()

                                    //get phone number
                                    if (contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt() > 0){
                                        val phoneCursor = view!!.getContentResolver()
                                                .query(
                                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                        null,
                                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                                        Array(1){this.id.toString()},
                                                        null
                                                )

                                        while (phoneCursor.moveToNext()){
                                            //PARSE TO A SEARCHABLE TEXT
                                            val parsedNumber = PhoneNumberUtil.getInstance().parse(
                                                    phoneCursor.getString(
                                                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                                    ),
                                                    "KE")
                                            this.tel = this.tel + parsedNumber.countryCode + parsedNumber.nationalNumber + ";" //PARSE TO A SEARCHABLE TEXT
                                        }

                                        phoneCursor.close()
                                    }
                                }
                        )

                    }
                    contactsCursor.close()
                }
                emitter.onNext(array)
            }
            emitter.onComplete()
        }
    }
    private fun addFoundContactsFromServer(): Completable {
        return Completable.create {emitter ->

            viewModel.members_header.apply {
                val header = this
                this.addSubItems( //ADD SUBITEMS TO HEADER FROM VIEWMODEL
                    this.subItemsCount,
                    viewModel.contactsFoundListItems.apply {
                        this.clear()
                        this.addAll( //ADD CONTACT LIST ITEMS TO VIEWMODEL
                            viewModel.contactsFoundList.map { user -> //MAP DATA INTO CONTACT LIST ITEMS
                                ContactListItem(header,user.apply { this._tag="NOT_FOLLOWING" },view!!.context!!).apply {
                                    listener = view
                                }
                            }.apply {
                                header.entries = this.size
                            }
                        )
                    } as List<ISectionable<*, *>>?
                )
            }

            viewModel.non_members_header.apply {
                val header = this
                this.addSubItems( //ADD SUBITEMS TO HEADER FROM VIEWMODEL
                    this.subItemsCount,
                    viewModel.contactsNotFoundListItems.apply {
                        this.clear()
                        this.addAll( //ADD CONTACT LIST ITEMS TO VIEWMODEL
                            viewModel.contactsNotFoundList.map { user2 -> //MAP DATA INTO CONTACT LIST ITEMS
                                ContactListItem(header,user2.apply { this._tag="NOT_INVITED" },view!!.context!!).apply {
                                    listener = view
                                }
                            }.apply {
                                header.entries = this.size
                            }
                        )
                    } as List<ISectionable<*,*>>?
                )
            }

            emitter.onComplete()
        }
    }

    fun onContactClicked(user: User) {

    }

    fun onUnInvite(contactListItem: ContactListItem, holder: ContactListItem.MyViewHolder?, position: Int) {
        contactListItem.user._tag = "NOT_INVITED"
        mainListAdapter.notifyDataSetChanged()
    }

    fun onUnFollow(contactListItem: ContactListItem, holder: ContactListItem.MyViewHolder, position: Int) {
        contactListItem.user._tag = "NOT_FOLLOWING"
        mainListAdapter.notifyDataSetChanged()
    }

    fun onFollow(contactListItem: ContactListItem, holder: ContactListItem.MyViewHolder, position: Int) {
        contactListItem.user._tag = "FOLLOWING"
        mainListAdapter.notifyDataSetChanged()
    }

    fun onInvite(contactListItem: ContactListItem, holder: ContactListItem.MyViewHolder, position: Int) {
        contactListItem.user._tag = "INVITED"
        mainListAdapter.notifyDataSetChanged()
    }

    fun onUnSelectAll(header: ContactsListHeaderItem) {
        header.subItems.forEach {
            val item = it as ContactListItem
            if(item.user._tag == "INVITED") item.user._tag = "NOT_INVITED"
            if(item.user._tag == "FOLLOWING") item.user._tag = "NOT_FOLLOWING"
        }

        mainListAdapter.notifyDataSetChanged()
    }

    fun onSelectAll(header: ContactsListHeaderItem) {
        header.subItems.forEach {
            val item = it as ContactListItem
            if(item.user._tag == "NOT_INVITED") item.user._tag = "INVITED"
            if(item.user._tag == "NOT_FOLLOWING") item.user._tag = "FOLLOWING"
        }

        mainListAdapter.notifyDataSetChanged()
    }

    fun onFinished() {
        superViewModel.contacts.addAll(viewModel.contactsFoundList + viewModel.contactsNotFoundList)
    }


}