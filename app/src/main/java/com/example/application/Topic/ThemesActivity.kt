package com.example.application.Topic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.DataBase.EnglishDatabase
import com.example.application.DataBase.TopicDao
import com.example.application.Description.DescriptionActivity
import com.example.application.R
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

                // Обновляем адаптер
                adapter = TopicsAdapter(
                    topics,
                    onTopicClick = { topic ->
                        Toast.makeText(this@ThemesActivity, "Выбрана тема: ${topic.title}", Toast.LENGTH_SHORT).show()
                    },
                    onReadClick = { topic ->
                        // Переход на DescriptionActivity
                        val intent = Intent(this@ThemesActivity, DescriptionActivity::class.java).apply {
                            putExtra("TOPIC_ID", topic.id)
                            putExtra("TOPIC_TITLE", topic.title)
                            putExtra("TOPIC_DESCRIPTION", topic.description)
                        }
                        startActivity(intent)
                    }
                )
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}