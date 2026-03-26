package com.zzllm.deadlinemanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao {
    @Query("SELECT * FROM sub_tasks WHERE ddlId = :ddlId")
    fun getSubTasksForDdl(ddlId: Int): Flow<List<SubTask>>

    @Insert
    suspend fun insert(subTask: SubTask)

    @Update
    suspend fun update(subTask: SubTask)

    @Delete
    suspend fun delete(subTask: SubTask)
}
