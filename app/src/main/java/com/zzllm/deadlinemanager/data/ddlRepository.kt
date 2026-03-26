package com.zzllm.deadlinemanager.data

import com.zzllm.deadlinemanager.data.AppDatabase
import com.zzllm.deadlinemanager.data.DDL
import com.zzllm.deadlinemanager.data.SubTask
import kotlinx.coroutines.flow.Flow

class DDLRepository(private val db: AppDatabase) {
    fun getAllDDLs(): Flow<List<DDL>> = db.ddlDao().getAllDDLs()

    suspend fun insertDdl(ddl: DDL): Long = db.ddlDao().insert(ddl)

    suspend fun updateDdl(ddl: DDL) = db.ddlDao().update(ddl)

    suspend fun deleteDdl(ddl: DDL) = db.ddlDao().delete(ddl)

    fun getSubTasksForDdl(ddlId: Int): Flow<List<SubTask>> = db.subTaskDao().getSubTasksForDdl(ddlId)

    suspend fun insertSubTask(subTask: SubTask) = db.subTaskDao().insert(subTask)

    suspend fun updateSubTask(subTask: SubTask) = db.subTaskDao().update(subTask)

    suspend fun deleteSubTask(subTask: SubTask) = db.subTaskDao().delete(subTask)
}