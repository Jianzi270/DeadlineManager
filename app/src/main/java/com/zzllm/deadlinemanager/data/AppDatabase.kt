package com.zzllm.deadlinemanager.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.zzllm.deadlinemanager.data.DDL
import com.zzllm.deadlinemanager.data.DDLDAO

@Database(
    entities = [DDL::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ddlDao(): DDLDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ddl_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}