package com.example.cashcount.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.cashcount.datastore.user.UserDataStore
import com.example.cashcount.datastore.user.UserDataStoreImpl
import com.example.cashcount.db.AppDb
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    @Named(UserDataStoreImpl.DATASTORE_KEY)
    fun provideDataStoreForUser(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            migrations = listOf(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ) {
            context.preferencesDataStoreFile(UserDataStoreImpl.DATASTORE_KEY)
        }
    }

    @Provides
    fun provideUserDataStore(
        @Named(UserDataStoreImpl.DATASTORE_KEY) dataStore: DataStore<Preferences>,
        gson: Gson
    ): UserDataStore {
        return UserDataStoreImpl(
            dataStore = dataStore,
            gson = gson
        )
    }

    @Provides
    @Singleton
    fun provideAppDb(
        @ApplicationContext context: Context
    ): AppDb {
        return AppDb.getInstance(context)
    }


}