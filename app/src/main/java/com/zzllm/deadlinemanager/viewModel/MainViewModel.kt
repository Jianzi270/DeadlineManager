package com.zzllm.deadlinemanager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzllm.deadlinemanager.data.DDL
import com.zzllm.deadlinemanager.data.DDLRepository
import com.zzllm.deadlinemanager.data.SubTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DDLRepository) : ViewModel() {

    private val _ddlList = MutableStateFlow<List<DDL>>(emptyList())
    val ddlList: StateFlow<List<DDL>> = _ddlList

    init {
        loadDDLs()
    }

    private fun loadDDLs() {
        viewModelScope.launch {
            repository.getAllDDLs().collect { ddls ->
                _ddlList.value = ddls
            }
        }
    }

    fun updateDdl(ddl: DDL) {
        viewModelScope.launch {
            repository.updateDdl(ddl)
        }
    }

    fun deleteDdl(ddl: DDL) {
        viewModelScope.launch {
            repository.deleteDdl(ddl)
        }
    }

    fun getSubTasksForDdl(ddlId: Int): Flow<List<SubTask>> {
        return repository.getSubTasksForDdl(ddlId)
    }

    fun insertSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.insertSubTask(subTask)
        }
    }

    fun updateSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.updateSubTask(subTask)
        }
    }
}
