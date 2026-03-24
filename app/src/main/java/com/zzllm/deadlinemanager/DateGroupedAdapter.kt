package com.zzllm.deadlinemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzllm.deadlinemanager.data.DDL

class DateGroupedAdapter : RecyclerView.Adapter<DateGroupedAdapter.DateGroupViewHolder>() {

    private var groupedDdls: Map<String, List<DDL>> = emptyMap()

    fun submitGroupedList(grouped: Map<String, List<DDL>>) {
        groupedDdls = grouped
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_group, parent, false)
        return DateGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateGroupViewHolder, position: Int) {
        val date = groupedDdls.keys.elementAt(position)
        val ddls = groupedDdls[date] ?: emptyList()
        holder.bind(date, ddls)
    }

    override fun getItemCount(): Int = groupedDdls.size

    class DateGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewDdls)

        fun bind(date: String, ddls: List<DDL>) {
            dateTextView.text = date
            val adapter = DdlListAdapter()
            recyclerView.layoutManager = LinearLayoutManager(itemView.context)
            recyclerView.adapter = adapter
            adapter.submitList(ddls)
        }
    }
}
