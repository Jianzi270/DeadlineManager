package com.zzllm.deadlinemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sub_tasks")
data class SubTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // Non-nullable for primary key
    val ddlId: Int,
    val title: String,
    val dueDate: String,  // 新增截止日期
    val isCompleted: Boolean = false
)
