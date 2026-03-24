package com.zzllm.deadlinemanager.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.zzllm.deadlinemanager.DdlListAdapter
import com.zzllm.deadlinemanager.R
import com.zzllm.deadlinemanager.data.AppDatabase
import com.zzllm.deadlinemanager.data.DDLRepository
import com.zzllm.deadlinemanager.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.util.*

class DateGroupedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var selectDateButton: MaterialButton
    private lateinit var adapter: DdlListAdapter
    private lateinit var viewModel: MainViewModel
    private var selectedDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_date_grouped, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewGrouped)
        selectDateButton = view.findViewById(R.id.selectDateButton)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = DdlListAdapter()
        recyclerView.adapter = adapter

        // 初始化 ViewModel
        val database = AppDatabase.getInstance(requireContext())
        val repository = DDLRepository(database)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
        })[MainViewModel::class.java]

        // 设置按钮点击监听器
        selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        // 观察数据变化并过滤
        lifecycleScope.launch {
            viewModel.ddlList.collect { ddls ->
                val filteredDdls = if (selectedDate != null) {
                    ddls.filter { it.dueDate == selectedDate }
                } else {
                    ddls
                }
                adapter.submitList(filteredDdls)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // 注意：selectedMonth是从0开始的
                selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                selectDateButton.text = "选择的日期: $selectedDate"
                // 触发数据更新
                lifecycleScope.launch {
                    viewModel.ddlList.collect { ddls ->
                        val filteredDdls = ddls.filter { it.dueDate == selectedDate }
                        adapter.submitList(filteredDdls)
                    }
                }
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
