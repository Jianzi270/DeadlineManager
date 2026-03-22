// DdlListAdapter.kt
package com.zzllm.deadlinemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DdlListAdapter : RecyclerView.Adapter<DdlListAdapter.DdlViewHolder>() {

    // 数据列表，初始为空
    private var ddlList = listOf<Any>()

    // 设置数据并刷新
    fun submitList(list: List<Any>) {
        ddlList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DdlViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ddl, parent, false)
        return DdlViewHolder(view)
    }

    override fun onBindViewHolder(holder: DdlViewHolder, position: Int) {
        val ddl = ddlList[position]
        holder.bind(ddl)
    }

    override fun getItemCount(): Int = ddlList.size

    class DdlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvDdlTitle)
        private val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        private val tvProgress: TextView = itemView.findViewById(R.id.tvProgress)

        fun bind(ddl: Any) {
            //Todo
//            tvTitle.text = ddl.title
//            tvDueDate.text = "截止日期：${ddl.dueDate}"
//            // 进度显示（暂时用占位，后续可计算子任务完成情况）
//            tvProgress.text = "任务进度：0/0"
        }
    }
}