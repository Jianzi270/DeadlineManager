package com.zzllm.deadlinemanager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzllm.deadlinemanager.data.DDL
import com.zzllm.deadlinemanager.data.DDLRepository
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
}
