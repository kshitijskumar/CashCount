package com.example.cashcount.features.addtransaction.data.local

import androidx.room.*
import com.example.cashcount.features.addtransaction.data.model.TransactionType
import com.example.cashcount.features.addtransaction.data.model.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transaction_table WHERE userId =:userId ORDER BY timestamp DESC")
    fun getAllTransactionsOfUser(userId: String): Flow<List<TransactionEntity>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transactionEntity: TransactionEntity)

    @Transaction
    @Query("DELETE FROM transaction_table WHERE transactionId =:transactionId")
    suspend fun deleteTransaction(transactionId: String)

    @Transaction
    @Query("SELECT * FROM transaction_table WHERE transactionId =:transactionId")
    suspend fun getTransactionForId(transactionId: String): TransactionEntity?

    @Transaction
    @Query("UPDATE account_table SET balance =:balanceUpdated WHERE accountId =:accountId")
    suspend fun updateAccountBalanceForAccountId(accountId: String, balanceUpdated: Long)

    @Transaction
    @Query("SELECT balance FROM account_table WHERE accountId =:accountId")
    suspend fun getCurrentBalanceOfAccount(accountId: String): Long?

    @Transaction
    suspend fun addOrUpdateTransactionAndUpdateAccountBalance(
        newEntry: TransactionEntity
    ) {
        val oldEntry = getTransactionForId(newEntry.transactionId)
        addTransaction(newEntry)
        if (oldEntry == null) {
            val currBalance = getCurrentBalanceOfAccount(newEntry.accountId) ?: 0
            val updatedBalance = when(newEntry.transactionType) {
                TransactionType.EXPENSE -> currBalance - newEntry.amount
                TransactionType.INCOME -> currBalance + newEntry.amount
            }
            updateAccountBalanceForAccountId(newEntry.accountId, updatedBalance)
        } else {
            val currBalanceFromOldEntry = getCurrentBalanceOfAccount(oldEntry.accountId) ?: 0 // curr balance from account corresponding to old entry
            val currBalanceFromNewEntry = getCurrentBalanceOfAccount(newEntry.accountId) ?: 0 // curr balance from account corresponding to new entry
            // undoing this previous transaction update
            val balanceExcludingPrevEntry = when(oldEntry.transactionType) {
                TransactionType.EXPENSE -> currBalanceFromOldEntry + oldEntry.amount
                TransactionType.INCOME -> currBalanceFromOldEntry - oldEntry.amount
            }
            // curr balance change from new entry
            val updatedBalance = when(newEntry.transactionType) {
                TransactionType.EXPENSE -> currBalanceFromNewEntry - newEntry.amount
                TransactionType.INCOME -> currBalanceFromNewEntry + newEntry.amount
            }
            updateAccountBalanceForAccountId(oldEntry.accountId, balanceExcludingPrevEntry) // update old entry account balance
            updateAccountBalanceForAccountId(newEntry.accountId, updatedBalance) // update new entry account balance
        }
    }
}