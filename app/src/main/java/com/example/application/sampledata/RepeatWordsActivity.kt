package com.example.application.sampledata

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.databinding.ActivityRepeatWordsBinding
import com.example.application.R

class RepeatWordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepeatWordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepeatWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        var buttonExit: ImageView = findViewById(R.id.exitWordRepeat)
        buttonExit.setOnClickListener {
            finish()
        }

    }

    private fun setupRecyclerView() {
        val adapter = WordAdapter(LearningActivity.wordsToRepeat)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RepeatWordsActivity)
            this.adapter = adapter
        }

        binding.emptyState.visibility = if (LearningActivity.wordsToRepeat.isEmpty()) View.VISIBLE else View.GONE
    }
}