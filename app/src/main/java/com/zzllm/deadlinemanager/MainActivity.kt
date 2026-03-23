// MainActivity.kt
package com.zzllm.deadlinemanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import com.zzllm.deadlinemanager.data.AppDatabase
import com.zzllm.deadlinemanager.data.DDLRepository
import com.zzllm.deadlinemanager.viewModel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var fabAddDdl: FloatingActionButton
    private lateinit var adapter: DdlListAdapter
    private lateinit var viewModel: MainViewModel

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

        // 设置适配器
        adapter = DdlListAdapter()
        recyclerView.adapter = adapter

        // 初始化 ViewModel
        val database = AppDatabase.getInstance(this)
        val repository = DDLRepository(database)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
        })[MainViewModel::class.java]

        // 观察数据变化
        lifecycleScope.launch {
            viewModel.ddlList.collect { ddls ->
                adapter.submitList(ddls)
                if (ddls.isEmpty()) {
                    recyclerView.visibility = android.view.View.GONE
                    emptyView.visibility = android.view.View.VISIBLE
                } else {
                    recyclerView.visibility = android.view.View.VISIBLE
                    emptyView.visibility = android.view.View.GONE
                }
            }
        }

        // 点击 FAB 跳转到添加页面
        fabAddDdl.setOnClickListener {
            val intent = Intent(this, AddEditDdlActivity::class.java)
            startActivity(intent)
        }
    }
}