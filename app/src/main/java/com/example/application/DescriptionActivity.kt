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
                val userId = FirebaseService.getCurrentUserId()
                if (userId != null) {
                    val topic = topicDao.getTopicById(topicId)
                    topic?.let {
                        it.isCompleted = true
                        topicDao.update(it)

                        // Сохраняем только ID текущей темы
                        val currentCompleted = topicDao.getAllTopics().first()
                            .filter { it.isCompleted }
                            .map { it.id }

                        FirebaseService.saveCompletedTopics(currentCompleted)
                    }
                }
                updateCompletedTopicsCount()
                finish()
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

    private suspend fun updateTopicCompletion() {
        val db = EnglishDatabase.getDatabase(this)
        val topic = db.topicDao().getTopicById(topicId)
        topic?.let {
            it.isCompleted = true
            db.topicDao().update(it)

            // Получаем актуальный список завершённых тем
            val completedTopics = db.topicDao().getAllTopics().first()
                .filter { it.isCompleted }
                .map { it.id }

            FirebaseService.saveCompletedTopics(completedTopics)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}