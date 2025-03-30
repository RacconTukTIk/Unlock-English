package com.example.application.sampledata

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.sampledata.LearnedWordsActivity
import com.example.application.databinding.ActivityLearnedWordsBinding

class LearnedWordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnedWordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnedWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        var buttonExit: ImageView = findViewById(R.id.exitWord)
        buttonExit.setOnClickListener {
            finish()
        }
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