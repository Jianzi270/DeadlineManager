package com.zzllm.deadlinemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "ddls")
data class DDL(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val dueDate: String,          // 存储格式 "yyyy-MM-dd"
    val createdAt: Long = System.currentTimeMillis(),
    val description: String
)