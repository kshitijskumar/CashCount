package com.example.cashcount.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.example.cashcount.db.AppDb.Companion.APP_DB_VERSION
import com.example.cashcount.features.addtransaction.data.local.TransactionDao
import com.example.cashcount.features.addtransaction.data.model.entity.TransactionEntity
import com.example.cashcount.features.createaccount.data.local.AccountDao
import com.example.cashcount.features.createaccount.data.model.entity.AccountEntity

@Database(entities = [AccountEntity::class, TransactionEntity::class], version = APP_DB_VERSION, exportSchema = true)
abstract class AppDb : RoomDatabase() {

    abstract val accountDao: AccountDao
    abstract val transactionDao: TransactionDao

    companion object{

        const val APP_DB_VERSION = 2
        private const val APP_DB_NAME = "app_db"

        private val MIGRATION_1_2 = Migration(1, 2) {
            it.execSQL("CREATE TABLE IF NOT EXISTS `transaction_table` (`transactionId` TEXT NOT NULL, `transactionCategory` TEXT NOT NULL, `transactionType` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `transactionDescription` TEXT NOT NULL, `userId` TEXT NOT NULL, `accountId` TEXT NOT NULL, PRIMARY KEY(`transactionId`))")
        }

        fun getInstance(context: Context): AppDb {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDb::class.java,
                APP_DB_NAME
            )
                .addMigrations(
                    MIGRATION_1_2
                )
                .build()
        }

    }


}