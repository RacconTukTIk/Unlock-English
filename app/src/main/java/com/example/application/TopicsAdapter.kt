package com.example.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TopicsAdapter(
    private val topics: List<Topic>,
    private val onTopicClick: (Topic) -> Unit // Обработчик нажатия
) : RecyclerView.Adapter<TopicsAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topics[position]
        holder.bind(topic)
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTopicNumber: TextView = itemView.findViewById(R.id.textTopicNumber)
        private val textTopicTitle: TextView = itemView.findViewById(R.id.textTopicTitle)

        fun bind(topic: Topic) {
            // Устанавливаем номер темы из поля id
            textTopicNumber.text = topic.id.toString()

            // Устанавливаем заголовок темы
            textTopicTitle.text = topic.title

            // Обработка нажатия на элемент
            itemView.setOnClickListener {
                onTopicClick(topic)
            }
        }
    }
}