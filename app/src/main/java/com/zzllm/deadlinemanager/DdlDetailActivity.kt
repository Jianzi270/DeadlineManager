package com.zzllm.deadlinemanager

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.zzllm.deadlinemanager.data.AppDatabase
import com.zzllm.deadlinemanager.data.DDL
import com.zzllm.deadlinemanager.data.DDLRepository
import com.zzllm.deadlinemanager.data.SubTask
import com.zzllm.deadlinemanager.viewModel.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DdlDetailActivity : AppCompatActivity() {

    private lateinit var etTitle: TextInputEditText
    private lateinit var etDueDate: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var rvSubTasks: RecyclerView
    private lateinit var btnAddSubTask: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var btnDelete: MaterialButton
    private lateinit var btnBack: MaterialButton
    private lateinit var tvSubTaskStatus: TextView

    private lateinit var subTaskAdapter: SubTaskAdapter
    private lateinit var viewModel: MainViewModel
    private var ddlId: Int = -1
    private lateinit var currentDdl: DDL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ddl_detail)

        ddlId = intent.getIntExtra("ddl_id", -1)
        if (ddlId == -1) {
            finish()
            return
        }

        initializeViews()
        setupRecyclerView()
        setupViewModel()
        loadDdlData()
        setupListeners()
    }

    private fun initializeViews() {
        etTitle = findViewById(R.id.etTitle)
        etDueDate = findViewById(R.id.etDueDate)
        etDescription = findViewById(R.id.etDescription)
        rvSubTasks = findViewById(R.id.rvSubTasks)
        btnAddSubTask = findViewById(R.id.btnAddSubTask)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)
        btnBack = findViewById(R.id.btnBack)
        tvSubTaskStatus = findViewById(R.id.tvSubTaskStatus)
    }

    private fun setupRecyclerView() {
        subTaskAdapter = SubTaskAdapter()
        rvSubTasks.layoutManager = LinearLayoutManager(this)
        rvSubTasks.adapter = subTaskAdapter

        subTaskAdapter.onSubTaskUpdated = { subTask ->
            lifecycleScope.launch {
                viewModel.updateSubTask(subTask)
                val subTasks = viewModel.getSubTasksForDdl(ddlId).first()
                updateSubTaskStatus(subTasks)
            }
        }
    }

    private fun setupViewModel() {
        val database = AppDatabase.getInstance(this)
        val repository = DDLRepository(database)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
        })[MainViewModel::class.java]
    }

    private fun loadDdlData() {
        lifecycleScope.launch {
            viewModel.ddlList.collect { ddls ->
                val ddl = ddls.find { it.id == ddlId }
                if (ddl != null) {
                    currentDdl = ddl
                    etTitle.setText(ddl.title)
                    etDueDate.setText(ddl.dueDate)
                    etDescription.setText(ddl.description)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getSubTasksForDdl(ddlId).collect { subTasks ->
                subTaskAdapter.submitList(subTasks)
                updateSubTaskStatus(subTasks)
            }
        }
    }

    private fun setupListeners() {
        btnAddSubTask.setOnClickListener {
            showAddSubTaskDialog()
        }

        btnSave.setOnClickListener {
            saveDdl()
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showAddSubTaskDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_sub_task, null)
        val etTitle = dialogView.findViewById<TextInputEditText>(R.id.etSubTaskTitle)
        val etDueDate = dialogView.findViewById<TextInputEditText>(R.id.etSubTaskDueDate)

        AlertDialog.Builder(this)
            .setTitle("添加子任务")
            .setView(dialogView)
            .setPositiveButton("添加") { _, _ ->
                val title = etTitle.text.toString().trim()
                val dueDate = etDueDate.text.toString().trim()
                if (title.isNotEmpty() && dueDate.isNotEmpty()) {
                    val subTask = SubTask(ddlId = ddlId, title = title, dueDate = dueDate)
                    lifecycleScope.launch {
                        viewModel.insertSubTask(subTask)
                    }
                } else {
                    Toast.makeText(this, "标题和截止日期不能为空", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun saveDdl() {
        val title = etTitle.text.toString().trim()
        val dueDate = etDueDate.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (title.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "标题和截止日期不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedDdl = currentDdl.copy(
            title = title,
            dueDate = dueDate,
            description = description
        )

        lifecycleScope.launch {
            viewModel.updateDdl(updatedDdl)
            Toast.makeText(this@DdlDetailActivity, "保存成功", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除这个DDL吗？")
            .setPositiveButton("删除") { _, _ ->
                lifecycleScope.launch {
                    viewModel.deleteDdl(currentDdl)
                    Toast.makeText(this@DdlDetailActivity, "删除成功", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateSubTaskStatus(subTasks: List<SubTask>) {
        val completedCount = subTasks.count { it.isCompleted }
        val totalCount = subTasks.size
        tvSubTaskStatus.text = "子任务 $completedCount / $totalCount"

        if (totalCount > 0 && completedCount == totalCount && !currentDdl.isCompleted) {
            val updatedDdl = currentDdl.copy(isCompleted = true)
            lifecycleScope.launch {
                viewModel.updateDdl(updatedDdl)
            }
        }
    }
}
