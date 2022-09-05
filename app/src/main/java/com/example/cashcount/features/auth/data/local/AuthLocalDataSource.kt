package com.example.cashcount.features.auth.data.local

import com.example.cashcount.datastore.user.UserDataStore
import com.example.cashcount.datastore.user.models.UserDataStoreModel
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
    private val userDataStore: UserDataStore
) : IAuthLocalDataSource {

    override suspend fun createUser(user: UserDataStoreModel) {
        userDataStore.setCurrentUser(user)
    }
}