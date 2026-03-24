package com.zzllm.deadlinemanager.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

class DdlListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var fabAddDdl: FloatingActionButton
    private lateinit var tvCurrentDate: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var adapter: DdlListAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var handler: Handler
    private lateinit var updateTimeRunnable: Runnable
    private val timeZone = TimeZone.getTimeZone("Asia/Shanghai")

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
        fabAddDdl = view.findViewById(R.id.fabAddDdl01)
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate)
        tvCurrentTime = view.findViewById(R.id.tvCurrentTime)
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
        setCurrentTime()
        setGreeting()

        // 点击 FAB 跳转到添加页面
        fabAddDdl.setOnClickListener {
            val intent = Intent(requireContext(), AddEditDdlActivity::class.java)
            startActivity(intent)
        }

        // 初始化 Handler 和 Runnable
        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                setCurrentTime()
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 开始更新时间的循环
        handler.post(updateTimeRunnable)
    }

    override fun onPause() {
        super.onPause()
        // 停止更新时间的循环
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun setCurrentDate() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = timeZone
        val currentDate = sdf.format(Date())
        tvCurrentDate.text = currentDate
    }

    private fun setCurrentTime() {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        sdf.timeZone = timeZone
        val currentTime = sdf.format(Date())
        tvCurrentTime.text = currentTime
    }

    private fun setGreeting() {
        val calendar = Calendar.getInstance(timeZone)
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hourOfDay in 6..11 -> "早上好"
            hourOfDay in 12..13 -> "中午好"
            hourOfDay in 14..17 -> "下午好"
            else -> "晚上好"
        }
        tvGreeting.text = greeting
    }
}
