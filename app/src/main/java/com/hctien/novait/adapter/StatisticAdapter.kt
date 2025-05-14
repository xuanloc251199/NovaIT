package com.hctien.novait.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.model.Statistic

class StatisticAdapter(private val statistics: List<Statistic>)
    : RecyclerView.Adapter<StatisticAdapter.StatisticViewHolder>() {

    class StatisticViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.statName)
        val value: TextView = itemView.findViewById(R.id.statValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_statistic, parent, false)
        return StatisticViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        val stat = statistics[position]
        holder.name.text = stat.name
        holder.value.text = stat.value
    }

    override fun getItemCount(): Int = statistics.size
}
