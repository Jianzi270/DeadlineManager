package com.zzllm.deadlinemanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zzllm.deadlinemanager.AddEditDdlActivity
import com.zzllm.deadlinemanager.DdlListAdapter
import com.zzllm.deadlinemanager.R
import com.zzllm.deadlinemanager.data.AppDatabase
import com.zzllm.deadlinemanager.data.DDLRepository
import com.zzllm.deadlinemanager.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DdlListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var fabAddDdl: FloatingActionButton
    private lateinit var tvCurrentDate: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var adapter: DdlListAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ddl_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化视图
        recyclerView = view.findViewById(R.id.recyclerView)
        emptyView = view.findViewById(R.id.emptyView)
        fabAddDdl = view.findViewById(R.id.fabAddDdl)
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate)
        tvGreeting = view.findViewById(R.id.tvGreeting)

        // 设置 RecyclerView 布局管理器
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 设置适配器
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

        // 观察数据变化
        lifecycleScope.launch {
            viewModel.ddlList.collect { ddls ->
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val todaysDdls = ddls.filter { it.dueDate == today }
                adapter.submitList(todaysDdls)
                if (todaysDdls.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
            }
        }

        // 设置当前日期和问候语
        setCurrentDate()
        setGreeting()

        // 点击 FAB 跳转到添加页面
        fabAddDdl.setOnClickListener {
            val intent = Intent(requireContext(), AddEditDdlActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setCurrentDate() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        tvCurrentDate.text = currentDate
    }

    private fun setGreeting() {
        val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hourOfDay < 12 -> "早上好"
            hourOfDay < 18 -> "下午好"
            else -> "晚上好"
        }
        tvGreeting.text = greeting
    }
}
