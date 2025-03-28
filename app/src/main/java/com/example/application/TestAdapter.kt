package com.example.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TestAdapter(
    private val tests: List<Topic>,
    private val onTestClicked: (Topic) -> Unit
) : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textTestTitle)
        val number: TextView = itemView.findViewById(R.id.textTestNumber)
        val button: Button = itemView.findViewById(R.id.btnPass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test, parent, false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val test = tests[position]
        holder.number.text = (position + 1).toString()
        holder.title.text = test.title

        holder.button.setOnClickListener {
            onTestClicked(test)
        }
    }

    override fun getItemCount() = tests.size
}