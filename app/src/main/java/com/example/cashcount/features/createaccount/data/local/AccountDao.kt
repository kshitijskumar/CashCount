package com.example.cashcount.features.createaccount.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cashcount.features.createaccount.data.model.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAccount(account: AccountEntity)

    @Query("SELECT * FROM account_table WHERE userId =:userId")
    fun getAllAccountsForUserId(userId: String): Flow<List<AccountEntity>>

    @Query("SELECT * FROM account_table")
    fun getAllAccountsInApp(): Flow<List<AccountEntity>>

}