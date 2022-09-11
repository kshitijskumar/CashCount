package com.example.cashcount.features.addtransaction.di

import com.example.cashcount.db.AppDb
import com.example.cashcount.features.addtransaction.data.ITransactionRepository
import com.example.cashcount.features.addtransaction.data.TransactionRepository
import com.example.cashcount.features.addtransaction.data.local.ITransactionLocalDataSource
import com.example.cashcount.features.addtransaction.data.local.TransactionDao
import com.example.cashcount.features.addtransaction.data.local.TransactionLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object TransactionModule {

    @Provides
    fun provideTransactionDao(
        appDb: AppDb
    ): TransactionDao = appDb.transactionDao

    @Provides
    fun provideTransactionLocalDataSource(
        impl: TransactionLocalDataSource
    ): ITransactionLocalDataSource = impl

    @Provides
    fun provideTransactionRepository(
        impl: TransactionRepository
    ): ITransactionRepository = impl

}