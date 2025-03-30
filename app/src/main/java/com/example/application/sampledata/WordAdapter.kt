package com.example.application.sampledata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.application.DataDict
import com.example.application.R

class WordAdapter(private val words: List<DataDict>) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordText: TextView = itemView.findViewById(R.id.wordText)

        fun bind(word: String) {
            wordText.text = word
            // Остальные TextView (translationText, transcriptionText) остаются в макете,
            // но не используются и будут пустыми
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position].word) // Передаем только слово
    }

    override fun getItemCount() = words.size
}