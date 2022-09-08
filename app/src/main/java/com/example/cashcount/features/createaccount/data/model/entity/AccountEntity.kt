package com.example.cashcount.features.createaccount.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cashcount.features.createaccount.data.model.AccountType

@Entity(tableName = "account_table")
data class AccountEntity(
    @PrimaryKey(autoGenerate = false) val accountId: String,
    val userId: String,
    val accountType: AccountType,
    val accountName: String,
    val balance: Long,
)
