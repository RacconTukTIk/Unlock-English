
package com.example.application

import android.graphics.Color

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.application.Calendar.DayModel
import com.example.application.R



class CalendarAdapter(
    private var days: List<DayModel>,
    private val onDayClick: (String) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    // Добавляем метод обновления данных
    fun updateDays(newDays: List<DayModel>) {
        days = newDays
        notifyDataSetChanged() // Важно: уведомляем об изменениях
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view, onDayClick)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount() = days.size

    class DayViewHolder(
        itemView: View,
        private val onDayClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)

        fun bind(dayModel: DayModel) {
            dayTextView.text = dayModel.day
            itemView.setBackgroundColor(
                if (dayModel.isChecked) Color.GREEN else Color.TRANSPARENT
            )

            itemView.setOnClickListener {
                if (dayModel.day.isNotEmpty()) {
                    onDayClick(dayModel.day)
                }
            }
        }
    }
}