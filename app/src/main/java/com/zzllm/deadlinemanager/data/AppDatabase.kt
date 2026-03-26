package com.zzllm.deadlinemanager.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [DDL::class, SubTask::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ddlDao(): DDLDAO
    abstract fun subTaskDao(): SubTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        // Migration from version 1 to 2 - initial creation of sub_tasks table
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create sub_tasks table with all columns
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `sub_tasks` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`ddlId` INTEGER NOT NULL, " +
                    "`title` TEXT NOT NULL, " +
                    "`dueDate` TEXT NOT NULL, " +
                    "`isCompleted` INTEGER NOT NULL)"
                )
            }
        }
        
        // Migration from version 2 to 3 - ensure table exists with correct schema
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Drop and recreate to ensure correct schema
                db.execSQL("DROP TABLE IF EXISTS `sub_tasks`")
                db.execSQL(
                    "CREATE TABLE `sub_tasks` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`ddlId` INTEGER NOT NULL, " +
                    "`title` TEXT NOT NULL, " +
                    "`dueDate` TEXT NOT NULL, " +
                    "`isCompleted` INTEGER NOT NULL)"
                )
            }
        }
        
        // Migration from version 3 to 4 - add dueDate column if missing
        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Simple approach: recreate table with correct schema
                db.execSQL("CREATE TABLE IF NOT EXISTS `sub_tasks_temp` (" +
                          "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                          "`ddlId` INTEGER NOT NULL, " +
                          "`title` TEXT NOT NULL, " +
                          "`dueDate` TEXT NOT NULL, " +
                          "`isCompleted` INTEGER NOT NULL)")
                
                // Try to copy data if old table exists
                try {
                    db.execSQL("INSERT INTO `sub_tasks_temp` SELECT id, ddlId, title, '', isCompleted FROM `sub_tasks`")
                } catch (e: Exception) {
                    // Table might not exist or have different schema, that's OK
                }
                
                db.execSQL("DROP TABLE IF EXISTS `sub_tasks`")
                db.execSQL("ALTER TABLE `sub_tasks_temp` RENAME TO `sub_tasks`")
            }
        }
        
        // Migration from version 4 to 5 - final cleanup migration
        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Ensure table has correct schema
                db.execSQL("CREATE TABLE IF NOT EXISTS `sub_tasks_new` (" +
                          "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                          "`ddlId` INTEGER NOT NULL, " +
                          "`title` TEXT NOT NULL, " +
                          "`dueDate` TEXT NOT NULL, " +
                          "`isCompleted` INTEGER NOT NULL)")
                
                // Copy existing data
                try {
                    db.execSQL("INSERT INTO `sub_tasks_new` SELECT * FROM `sub_tasks`")
                } catch (e: Exception) {
                    // If copy fails, table might have wrong schema, start fresh
                }
                
                db.execSQL("DROP TABLE IF EXISTS `sub_tasks`")
                db.execSQL("ALTER TABLE `sub_tasks_new` RENAME TO `sub_tasks`")
            }
        }
        
        // Migration from version 5 to 6 - add isCompleted column to ddls table
        private val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `ddls` ADD COLUMN `isCompleted` INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ddl_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                .fallbackToDestructiveMigration()  // Use destructive if migration fails
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}