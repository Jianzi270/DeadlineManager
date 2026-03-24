package com.zzllm.deadlinemanager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zzllm.deadlinemanager.ui.DateGroupedFragment
import com.zzllm.deadlinemanager.ui.DdlListFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DdlListFragment()
            1 -> DateGroupedFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
