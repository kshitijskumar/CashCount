package com.example.cashcount.features.auth.data.repository

import com.example.cashcount.datastore.user.models.UserDataStoreModel

interface IAuthRepository {

    suspend fun createUser(user: UserDataStoreModel)

}