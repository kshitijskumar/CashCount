package com.example.cashcount.datastore.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cashcount.datastore.user.models.UserDataStoreModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

interface UserDataStore {

    fun getCurrentLoggedInUser(): Flow<UserDataStoreModel?>

    fun isUserLoggedIn(): Flow<Boolean>

    suspend fun setCurrentUser(user: UserDataStoreModel)

    suspend fun getCurrentUserId(): String?

}

class UserDataStoreImpl @Inject constructor(
    @Named(DATASTORE_KEY) private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) : UserDataStore {

    override fun getCurrentLoggedInUser(): Flow<UserDataStoreModel?> {
        return dataStore.data.map {
            it.get(USER_DETAILS_PREF)
        }.map {
            try {
                gson.fromJson(it, UserDataStoreModel::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun isUserLoggedIn(): Flow<Boolean> {
        return getCurrentLoggedInUser()
            .map {
                it != null
            }
    }

    override suspend fun setCurrentUser(user: UserDataStoreModel) {
        val userDetailsJson = gson.toJson(user)
        dataStore.edit {
            it.set(USER_DETAILS_PREF, userDetailsJson)
        }
    }

    override suspend fun getCurrentUserId(): String? {
        return getCurrentLoggedInUser().firstOrNull()?.userPhoneNumber
    }

    companion object {
        const val DATASTORE_KEY = "userDataStore"

        private val USER_DETAILS_PREF = stringPreferencesKey("user_details_pref")
    }
}