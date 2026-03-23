package com.zzllm.deadlinemanager

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zzllm.deadlinemanager.data.AppDatabase
import com.zzllm.deadlinemanager.data.DDLRepository
import com.zzllm.deadlinemanager.databinding.ActivityAddEditDdlBinding
import com.zzllm.deadlinemanager.viewModel.AddEditDdlViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddEditDdlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditDdlBinding
    private lateinit var viewModel: AddEditDdlViewModel
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditDdlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化 ViewModel
        val database = AppDatabase.getInstance(this)
        val repository = DDLRepository(database)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AddEditDdlViewModel(repository) as T
            }
        })[AddEditDdlViewModel::class.java]

        // 设置日期选择器
        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        // 保存按钮
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val dueDate = binding.etDate.text.toString().trim()

            if (title.isEmpty()) {
                binding.tilTitle.error = getString(R.string.title_required)
                return@setOnClickListener
            }
            if (dueDate.isEmpty()) {
                binding.tilDate.error = getString(R.string.date_required)
                return@setOnClickListener
            }

            viewModel.insertDdl(title, dueDate) { id ->
                if (id > 0) {
                    Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, R.string.save_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 取消按钮
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.etDate.setText(format.format(calendar.time))
                binding.tilDate.error = null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}