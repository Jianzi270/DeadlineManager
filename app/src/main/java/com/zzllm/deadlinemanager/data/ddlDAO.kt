package com.zzllm.deadlinemanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zzllm.deadlinemanager.data.DDL
import kotlinx.coroutines.flow.Flow

@Dao
interface DDLDAO {
    @Insert
    suspend fun insert(ddl: DDL): Long

    @Update
    suspend fun update(ddl: DDL)

    @Delete
    suspend fun delete(ddl: DDL)

    @Query("SELECT * FROM ddls ORDER BY dueDate ASC")
    fun getAllDDLs(): Flow<List<DDL>>
}