package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var testAdapter: TestAdapter
    private lateinit var db: EnglishDatabase
    private lateinit var topicDao: TopicDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        db = EnglishDatabase.getDatabase(this)
        topicDao = db.topicDao()

        recyclerView = findViewById(R.id.recyclerViewTests)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализация адаптера
        testAdapter = TestAdapter(emptyList()) { topic ->
            // Обработка нажатия на тест
            val intent = Intent(this, TestQuestionsActivity::class.java).apply {
                putExtra("TOPIC_ID", topic.id)
                putExtra("TOPIC_TITLE", topic.title)
            }
            startActivity(intent)
        }

        recyclerView.adapter = testAdapter

        // Загрузка данных
        loadTopicsWithTests()

        // Обработка кнопки назад
        findViewById<ImageView>(R.id.exitTests).setOnClickListener {
            finish()
        }
    }

    private fun loadTopicsWithTests() {
        lifecycleScope.launch(Dispatchers.IO) {
            topicDao.getTopicsWithTests().collect { topics ->
                val filteredTopics = mutableListOf<Topic>()

                topics.forEach { topic ->
                    val testsCount = db.testDao().getTestCount(topic.id)
                    if (testsCount > 0) {
                        filteredTopics.add(topic)
                    }
                }

                withContext(Dispatchers.Main) {
                    testAdapter = TestAdapter(filteredTopics) { topic ->
                        val intent = Intent(
                            this@TestActivity,
                            TestQuestionsActivity::class.java
                        ).apply {
                            putExtra("TOPIC_ID", topic.id)
                            putExtra("TOPIC_TITLE", topic.title) // Добавлено передача названия
                        }
                        startActivity(intent)
                    }
                    recyclerView.adapter = testAdapter
                }
            }
        }
    }
}