// AddEditDdlActivity.kt
package com.zzllm.deadlinemanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddEditDdlActivity : AppCompatActivity() {

    private lateinit var tilTitle: TextInputLayout
    private lateinit var etTitle: TextInputEditText
    private lateinit var tilDate: TextInputLayout
    private lateinit var etDate: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_edit_ddl)

        // 绑定视图
        //Todo
//        tilTitle = findViewById(R.id.tilTitle)
//        etTitle = findViewById(R.id.etTitle)
//        tilDate = findViewById(R.id.tilDate)
//        etDate = findViewById(R.id.etDate)
//        btnSave = findViewById(R.id.btnSave)
//        btnCancel = findViewById(R.id.btnCancel)

        // 保存按钮点击事件
        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val dueDate = etDate.text.toString()
            // 后续调用 Repository 保存 DDL
            // 保存成功后关闭页面
            finish()
        }

        // 取消按钮
        btnCancel.setOnClickListener {
            finish()
        }
    }
}