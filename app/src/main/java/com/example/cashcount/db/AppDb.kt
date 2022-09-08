package com.example.cashcount.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cashcount.db.AppDb.Companion.APP_DB_VERSION
import com.example.cashcount.features.createaccount.data.local.AccountDao
import com.example.cashcount.features.createaccount.data.model.entity.AccountEntity

@Database(entities = [AccountEntity::class], version = APP_DB_VERSION, exportSchema = true)
abstract class AppDb : RoomDatabase() {

    abstract val accountDao: AccountDao

    companion object{

        const val APP_DB_VERSION = 1
        private const val APP_DB_NAME = "app_db"

        fun getInstance(context: Context): AppDb {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDb::class.java,
                APP_DB_NAME
            )
                .build()
        }

    }


}