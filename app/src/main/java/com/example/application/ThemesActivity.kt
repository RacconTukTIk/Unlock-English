package com.example.application

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ThemesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopicsAdapter
    private lateinit var viewModel: TopicsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[TopicsViewModel::class.java]

        // Настройка RecyclerView
        recyclerView = findViewById(R.id.recyclerViewThemes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Наблюдение за данными
        viewModel.allTopics.observe(this) { topics ->
            Log.d("ThemesActivity", "Получено тем: ${topics.size}")

            if (topics.isEmpty()) {
                Log.e("ThemesActivity", "Список тем пуст!")
                Toast.makeText(this, "Нет доступных тем", Toast.LENGTH_SHORT).show()
                return@observe
            }

            // Инициализация адаптера
            adapter = TopicsAdapter(topics) { topic ->
                Log.d("ThemesActivity", "Нажата тема: ${topic.title}")
                openTopicDetails(topic.id)
            }
            recyclerView.adapter = adapter
        }

        // Кнопка выхода
        val buttonExit: ImageView = findViewById(R.id.exitTheme)
        buttonExit.setOnClickListener { finish() }
    }

    private fun openTopicDetails(topicId: Int) {
        val intent = Intent(this, TopicDiscriptionActivity::class.java).apply {
            putExtra("TOPIC_ID", topicId)
        }
        startActivity(intent)
    }

    override fun onBackPressed() {

    }
}