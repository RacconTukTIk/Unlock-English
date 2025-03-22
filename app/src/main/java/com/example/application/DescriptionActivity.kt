package com.example.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.ActivityDescriptionBinding

class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем данные из Intent
        val topicId = intent.getIntExtra("TOPIC_ID", -1)
        val topicTitle = intent.getStringExtra("TOPIC_TITLE") ?: "Название темы"
        val topicDescription = intent.getStringExtra("TOPIC_DESCRIPTION") ?: "Описание темы"

        // Устанавливаем данные в UI
        binding.sectionTitle.text = topicTitle
        binding.recyclerView.adapter = DescriptionAdapter(listOf(topicDescription)) // Пример адаптера для описания

        // Обработка кнопки "Тема пройдена"
        binding.actionButton.setOnClickListener {

        }

        // Обработка кнопки выхода
        binding.exitTopics.setOnClickListener {
            finish()
        }
    }
    override fun onBackPressed() {

    }
}