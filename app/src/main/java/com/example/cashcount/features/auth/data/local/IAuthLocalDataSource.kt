package com.example.cashcount.features.auth.data.local

import com.example.cashcount.datastore.user.models.UserDataStoreModel

interface IAuthLocalDataSource {

    suspend fun createUser(user: UserDataStoreModel)

}