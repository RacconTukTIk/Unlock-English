package com.example.application

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.databinding.ActivityLearnedWordsBinding

class LearnedWordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnedWordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnedWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = WordAdapter(LearningActivity.learnedWords)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@LearnedWordsActivity)
            this.adapter = adapter
        }

        binding.emptyState.visibility = if (LearningActivity.learnedWords.isEmpty()) View.VISIBLE else View.GONE
    }
}