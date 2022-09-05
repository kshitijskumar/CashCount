package com.example.cashcount.features.auth.domain

import com.example.cashcount.datastore.user.models.UserDataStoreModel
import com.example.cashcount.features.auth.data.repository.IAuthRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val authRepo: IAuthRepository
) {

    suspend fun invoke(user: UserDataStoreModel) {
        authRepo.createUser(user)
    }

}