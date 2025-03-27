package com.example.application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DictAdapterTech(
    private val entries: List<DataDict>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DictAdapterTech.ViewHolder>() {

    interface OnItemClickListener {
        fun onSoundButtonClick(word: String)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordTextView: TextView = view.findViewById(R.id.wordTextView)
        val transcriptionTextView: TextView = view.findViewById(R.id.transcriptionTextView)
        val translationTextView: TextView = view.findViewById(R.id.translationTextView)
        val soundButton: ImageView = view.findViewById(R.id.buttonSound)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dictionary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.wordTextView.text = entry.word
        holder.transcriptionTextView.text = entry.transcription
        holder.translationTextView.text = entry.translation

        holder.soundButton.setOnClickListener {
            listener.onSoundButtonClick(entry.word)
        }
    }

    override fun getItemCount() = entries.size
}