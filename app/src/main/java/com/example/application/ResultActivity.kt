package com.example.application

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.reflect.Array.getInt

class ResultActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private var currentTopicId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        sharedPref = getSharedPreferences("app_stats", Context.MODE_PRIVATE)
        currentTopicId = intent.getIntExtra("TOPIC_ID", -1)

        val correct = intent.getIntExtra("CORRECT_ANSWERS", 0)
        val total = intent.getIntExtra("TOTAL_QUESTIONS", 0)


        val tvResult = findViewById<TextView>(R.id.tvResult)
        val imageResult = findViewById<ImageView>(R.id.imageResult)
        val tvMessage = findViewById<TextView>(R.id.tvMessage)
        val btnRetry = findViewById<Button>(R.id.btnRetry)
        val btnFinish = findViewById<Button>(R.id.btnFinish)

        tvResult.text = "Правильных ответов: $correct из $total"

        if (correct == total) {
            imageResult.setImageResource(R.drawable.kitty)
            tvMessage.text = "Молодец, так держать!"
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.green))
            btnRetry.visibility = View.GONE

            // Обновляем констрейнты для кнопки "Завершить"
            val params = btnFinish.layoutParams as ConstraintLayout.LayoutParams
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            params.horizontalBias = 0.5f
            params.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            params.matchConstraintPercentWidth = 0.9f // Занимает 90% ширины

            btnFinish.layoutParams = params
            markTestAsCompleted(currentTopicId)

        } else {
            imageResult.setImageResource(R.drawable.sad_cat)
            tvMessage.text = "Попробуй еще раз!\n Ты почти у цели!"
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.red))
            btnRetry.visibility = View.VISIBLE
            val params = btnFinish.layoutParams as ConstraintLayout.LayoutParams
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
            params.endToEnd = ConstraintLayout.LayoutParams.UNSET
            params.startToEnd = R.id.btnRetry
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            params.matchConstraintPercentWidth = 0.45f
            params.horizontalBias = 0f

            btnFinish.layoutParams = params
        }

        saveStatistics(correct, total)

        btnRetry.setOnClickListener {
            val intent = Intent(this, TestQuestionsActivity::class.java).apply {
                putExtra("TOPIC_ID", currentTopicId)
                putExtra("TOPIC_TITLE", getTopicTitle(currentTopicId))
            }
            startActivity(intent)
            finish()
        }

        btnFinish.setOnClickListener {
            lifecycleScope.launch {
                // Сохраняем данные в Firebase
                FirebaseService.saveUserProgress(this@ResultActivity)

                setResult(Activity.RESULT_OK)
                finish()
            }
        }


    }

    private fun saveStatistics(correct: Int, total: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val errors = total - correct
            val db = EnglishDatabase.getDatabase(this@ResultActivity)

            try {
                // Обновляем локальную базу
                db.topicDao().apply {
                    addErrors(currentTopicId, errors)
                    updateLastAttemptErrors(currentTopicId, errors)
                }

                // Синхронизируем с Firebase
                FirebaseService.saveUserErrors(currentTopicId, errors)

                // Обновляем общую статистику
                withContext(Dispatchers.Main) {
                    val currentErrors = sharedPref.getInt("total_errors", 0)
                    val currentCorrect = sharedPref.getInt("total_correct", 0)

                    sharedPref.edit()
                        .putInt("total_errors", currentErrors + errors)
                        .putInt("total_correct", currentCorrect + correct)
                        .apply()
                }
            } catch (e: Exception) {
                Log.e("ResultActivity", "Error saving stats: ${e.message}")
            }
        }
    }

    private fun getTopicTitle(topicId: Int): String {
        return runBlocking {
            withContext(Dispatchers.IO) {
                EnglishDatabase.getDatabase(this@ResultActivity)
                    .topicDao()
                    .getTopicTitle(topicId)
            }
        }
    }


    private fun markTopicAsCompleted(topicId: Int) {
        if (topicId == -1) return

        lifecycleScope.launch(Dispatchers.IO) {
            val db = EnglishDatabase.getDatabase(this@ResultActivity)
            val topic = db.topicDao().getTopicById(topicId)
            topic?.let { currentTopic ->
                currentTopic.isCompleted = true
                db.topicDao().update(currentTopic)
            }
        }
    }

    private fun markTestAsCompleted(topicId: Int) {
        if (topicId == -1) return

        lifecycleScope.launch(Dispatchers.IO) {
            val db = EnglishDatabase.getDatabase(this@ResultActivity)
            val topic = db.topicDao().getTopicById(topicId)
            topic?.let {
                it.isTestCompleted = true
                db.topicDao().update(it)

                // Получаем все завершенные тесты
                val currentCompleted = db.topicDao().getAllTopics().first()
                    .filter { it.isTestCompleted }
                    .map { it.id }

                FirebaseService.saveCompletedTests(currentCompleted)
            }
        }
    }
}