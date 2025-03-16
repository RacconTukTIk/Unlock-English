package com.example.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val days: List<String>,
    private val onDayClick: (String) -> Unit // Лямбда-функция для обработки кликов
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view, onDayClick)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    class DayViewHolder(
        itemView: View,
        private val onDayClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)

        fun bind(day: String) {
            dayTextView.text = day
            itemView.setOnClickListener {
                if (day.isNotEmpty()) {
                    onDayClick(day)
                }
            }
        }
    }
}