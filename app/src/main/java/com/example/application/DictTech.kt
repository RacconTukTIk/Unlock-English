package com.example.application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DictAdapterTech(private val entries: List<DataDictTech>) :
    RecyclerView.Adapter<DictAdapterTech.DictionaryViewHolder>() {

    class DictionaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordTextView: TextView = view.findViewById(R.id.wordTextView)
        val transcriptionTextView: TextView = view.findViewById(R.id.transcriptionTextView)
        val translationTextView: TextView = view.findViewById(R.id.translationTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dictionary, parent, false)
        return DictionaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        val entry = entries[position]
        holder.wordTextView.text = entry.word
        holder.transcriptionTextView.text = entry.transcription
        holder.translationTextView.text = entry.translation
    }

    override fun getItemCount() = entries.size
}