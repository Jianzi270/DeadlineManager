// MainActivity.kt
package com.zzllm.deadlinemanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var fabAddDdl: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 设置 Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 初始化视图
        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)
        fabAddDdl = findViewById(R.id.fabAddDdl)

        // 设置 RecyclerView 布局管理器
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 设置适配器（稍后替换为真实适配器）
        val adapter = DdlListAdapter()
        recyclerView.adapter = adapter

        // 点击 FAB 跳转到添加页面
        fabAddDdl.setOnClickListener {
            val intent = Intent(this, AddEditDdlActivity::class.java)
            startActivity(intent)
        }
    }
}