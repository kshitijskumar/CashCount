package com.example.cashcount.features.createaccount.di

import com.example.cashcount.db.AppDb
import com.example.cashcount.features.createaccount.data.AccountRepository
import com.example.cashcount.features.createaccount.data.IAccountRepository
import com.example.cashcount.features.createaccount.data.local.AccountDao
import com.example.cashcount.features.createaccount.data.local.AccountLocalDataSource
import com.example.cashcount.features.createaccount.data.local.IAccountLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AccountModule {

    @Provides
    fun provideAccountDao(
        appDb: AppDb
    ): AccountDao = appDb.accountDao

    @Provides
    fun provideAccountLocalDataSource(
        impl: AccountLocalDataSource
    ): IAccountLocalDataSource = impl

    @Provides
    fun provideAccountRepository(
        impl: AccountRepository
    ): IAccountRepository = impl

}