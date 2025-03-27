package com.example.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class DescriptionAdapter(private val descriptions: List<String>) :
    RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return DescriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        val description = descriptions[position]
        holder.bind(description)

        // Устанавливаем черный цвет текста
        holder.textView.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.black))
    }

    override fun getItemCount(): Int {
        return descriptions.size
    }

    inner class DescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Убираем модификатор private, чтобы textView был доступен извне
        val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(description: String) {
            textView.text = description
        }
    }
}