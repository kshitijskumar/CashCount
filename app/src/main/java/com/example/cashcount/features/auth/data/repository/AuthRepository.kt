package com.example.cashcount.features.auth.data.repository

import com.example.cashcount.datastore.user.models.UserDataStoreModel
import com.example.cashcount.features.auth.data.local.IAuthLocalDataSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val localDataSource: IAuthLocalDataSource
) : IAuthRepository {

    override suspend fun createUser(user: UserDataStoreModel) {
        localDataSource.createUser(user)
    }

}