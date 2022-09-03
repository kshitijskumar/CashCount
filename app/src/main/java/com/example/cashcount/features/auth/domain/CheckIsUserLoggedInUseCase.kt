package com.example.cashcount.features.auth.domain

import com.example.cashcount.datastore.user.UserDataStore
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@ViewModelScoped
class CheckIsUserLoggedInUseCase @Inject constructor(
    private val userDataStore: UserDataStore
) {

    suspend operator fun invoke(): Boolean {
        return userDataStore.isUserLoggedIn().first()
    }

}