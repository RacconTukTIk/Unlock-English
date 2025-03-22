package com.example.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.application.databinding.ActivityDescriptionBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionBinding
    private lateinit var englishDatabase: EnglishDatabase
    private lateinit var topicDao: TopicDao
    private var topicId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация базы данных и DAO
        englishDatabase = EnglishDatabase.getDatabase(this)
        topicDao = englishDatabase.topicDao()

        // Получаем данные из Intent
        topicId = intent.getIntExtra("TOPIC_ID", -1)
        val topicTitle = intent.getStringExtra("TOPIC_TITLE") ?: "Название темы"
        val topicDescription = intent.getStringExtra("TOPIC_DESCRIPTION") ?: "Описание темы"

        // Устанавливаем данные в UI
        binding.sectionTitle.text = topicTitle
        binding.recyclerView.adapter = DescriptionAdapter(listOf(topicDescription))

        // Обработка кнопки "Тема пройдена"
        binding.actionButton.setOnClickListener {
            lifecycleScope.launch {
                // Получаем тему по ID
                val topic = topicDao.getAllTopics().first().find { it.id == topicId }
                topic?.let {
                    // Обновляем статус темы
                    it.isCompleted = true
                    topicDao.update(it)

                    // Делаем кнопку некликабельной
                    binding.actionButton.isEnabled = false

                    // Обновляем счетчик на главном экране
                    updateCompletedTopicsCount()
                }
            }
        }

        // Обработка кнопки выхода
        binding.exitTopics.setOnClickListener {
            finish()
        }
    }

    private fun updateCompletedTopicsCount() {
        // Отправляем результат в MenuFragment
        setResult(RESULT_OK)
    }

    override fun onBackPressed() {

    }
}