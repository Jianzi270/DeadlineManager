package com.zzllm.deadlinemanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zzllm.deadlinemanager.data.DDL
import kotlinx.coroutines.flow.Flow

@Dao
interface ddlDAO {
    @Insert
    suspend fun insert(ddl: DDL): Long

    @Query("SELECT * FROM ddls ORDER BY dueDate ASC")
    fun getAllDDLs(): Flow<List<DDL>>
}