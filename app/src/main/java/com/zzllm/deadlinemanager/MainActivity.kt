// MainActivity.kt
package com.zzllm.deadlinemanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zzllm.deadlinemanager.ui.DdlListFragment
import com.zzllm.deadlinemanager.ui.DateGroupedFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var fabAddDdl: FloatingActionButton
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 设置 Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 初始化视图
        viewPager = findViewById(R.id.viewPager)
        fabAddDdl = findViewById(R.id.fabAddDdl)
        tabLayout = findViewById(R.id.tabLayout)

        // 设置 ViewPager 适配器
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // 设置 TabLayout 与 ViewPager 关联
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "按截止日期"
                1 -> "按日期分组"
                else -> null
            }
        }.attach()

        // 点击 FAB 跳转到添加页面
        fabAddDdl.setOnClickListener {
            val intent = Intent(this, AddEditDdlActivity::class.java)
            startActivity(intent)
        }
    }
}