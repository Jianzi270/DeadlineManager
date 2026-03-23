package com.zzllm.deadlinemanager.data

import com.zzllm.deadlinemanager.data.AppDatabase
import com.zzllm.deadlinemanager.data.DDL
import kotlinx.coroutines.flow.Flow

class ddlRepository(private val db: AppDatabase) {
    fun getAllDDLs(): Flow<List<DDL>> = db.ddlDao().getAllDDLs()

    suspend fun insertDdl(ddl: DDL): Long = db.ddlDao().insert(ddl)
}