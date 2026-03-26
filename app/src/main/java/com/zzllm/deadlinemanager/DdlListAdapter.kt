// DdlListAdapter.kt
package com.zzllm.deadlinemanager

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzllm.deadlinemanager.data.DDL
import com.zzllm.deadlinemanager.data.SubTask

class DdlListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<Any>()

    // 点击监听器
    var onItemClick: ((Any) -> Unit)? = null

    fun submitList(ddls: List<DDL>, subTasks: List<SubTask> = emptyList()) { // 把子任务也写进ddl列表中
        val combined = mutableListOf<Any>()
        ddls.forEach { ddl ->
            combined.add(ddl)
            combined.addAll(subTasks.filter { it.ddlId == ddl.id})
        }
        items = combined
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DDL -> 0
            is SubTask -> 1
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ddl, parent, false)
                DdlViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_sub_task, parent, false)
                SubTaskViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is DdlViewHolder -> holder.bind(item as DDL)
            is SubTaskViewHolder -> holder.bind(item as SubTask)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class DdlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvDdlTitle)
        private val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        private val tvProgress: TextView = itemView.findViewById(R.id.tvProgress)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    onItemClick?.invoke(item)
                }
            }
        }

        fun bind(ddl: DDL) {
            tvTitle.text = ddl.title
            tvDueDate.text = "截止日期：${ddl.dueDate}"
            tvProgress.text = "任务进度：0/0"
        }
    }

    inner class SubTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cbCompleted: CheckBox = itemView.findViewById(R.id.cbCompleted)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvSubTaskTitle)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    onItemClick?.invoke(item)
                }
            }
        }

        fun bind(subTask: SubTask) {
            tvTitle.text = subTask.title
            cbCompleted.isChecked = subTask.isCompleted
            // 可以设置颜色
            itemView.setBackgroundColor(Color.LTGRAY)  // 不同颜色
        }
    }
}