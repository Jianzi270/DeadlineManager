package com.zzllm.deadlinemanager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzllm.deadlinemanager.data.DDL
import com.zzllm.deadlinemanager.data.DDLRepository
import com.zzllm.deadlinemanager.data.SubTask
import kotlinx.coroutines.launch

class AddEditDdlViewModel(private val repository: DDLRepository) : ViewModel() {

    fun insertDdl(title: String, dueDate: String, description: String, onComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val ddl = DDL(title = title, dueDate = dueDate, description = description)
            val id = repository.insertDdl(ddl)
            onComplete(id)
        }
    }
}