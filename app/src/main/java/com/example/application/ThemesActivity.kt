package com.example.application

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ThemesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopicsAdapter
    private lateinit var englishDatabase: EnglishDatabase
    private lateinit var topicDao: TopicDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)

        recyclerView = findViewById(R.id.recyclerViewThemes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализация базы данных и DAO
        englishDatabase = EnglishDatabase.getDatabase(this)
        topicDao = englishDatabase.topicDao()

        // Загрузка данных из базы данных
        loadTopics()

        val buttonExit: ImageView = findViewById(R.id.exitTheme)
        buttonExit.setOnClickListener {
            finish()
        }
    }

    private fun loadTopics() {
        lifecycleScope.launch {
            topicDao.getAllTopics().collect { topics ->
                // Логируем данные
                Log.d("ThemesActivity", "Loaded topics: ${topics.size}")
                topics.forEach { topic ->
                    Log.d("ThemesActivity", "Topic: ${topic.id}, ${topic.title}")
                }

                // Обновляем адаптер
                adapter = TopicsAdapter(topics) { topic ->
                    Toast.makeText(this@ThemesActivity, "Выбрана тема: ${topic.title}", Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onBackPressed() {

    }
}