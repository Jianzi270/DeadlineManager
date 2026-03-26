package com.zzllm.deadlinemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzllm.deadlinemanager.data.SubTask

class SubTaskAdapter : RecyclerView.Adapter<SubTaskAdapter.SubTaskViewHolder>() {

    private var subTasks = listOf<SubTask>()
    var onSubTaskUpdated: ((SubTask) -> Unit)? = null

    fun submitList(list: List<SubTask>) {
        subTasks = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sub_task, parent, false)
        return SubTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubTaskViewHolder, position: Int) {
        val subTask = subTasks[position]
        holder.bind(subTask)
    }

    override fun getItemCount(): Int = subTasks.size

    inner class SubTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cbCompleted: CheckBox = itemView.findViewById(R.id.cbCompleted)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvSubTaskTitle)

        fun bind(subTask: SubTask) {
            tvTitle.text = subTask.title
            cbCompleted.isChecked = subTask.isCompleted

            cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                val updatedSubTask = subTask.copy(isCompleted = isChecked)
                onSubTaskUpdated?.invoke(updatedSubTask)
            }
        }
    }
}
