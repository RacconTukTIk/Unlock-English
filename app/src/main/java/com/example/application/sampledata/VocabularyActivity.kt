package com.example.application.sampledata

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.FragmentVocabularyBinding
import com.example.application.sampledata.LearnedWordsActivity


class VocabularyActivity : AppCompatActivity() {
    private lateinit var binding: FragmentVocabularyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentVocabularyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Проверка биндинга
        if (!::binding.isInitialized) {
            throw IllegalStateException("Binding not initialized")
        }

        binding.buttonLearnWords.setOnClickListener {
            startActivity(Intent(this, LearningActivity::class.java))
        }

        binding.buttonLearned.setOnClickListener {
            startActivity(Intent(this, LearnedWordsActivity::class.java))
        }

        binding.buttonNeedToRepeat.setOnClickListener {
            startActivity(Intent(this, RepeatWordsActivity::class.java))
        }
    }
}