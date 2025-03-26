package com.example.application

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val correct = intent.getIntExtra("CORRECT_ANSWERS", 0)
        val total = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val currentTopicId = intent.getIntExtra("TOPIC_ID", -1)

        val tvResult = findViewById<TextView>(R.id.tvResult)
        val imageResult = findViewById<ImageView>(R.id.imageResult)
        val tvMessage = findViewById<TextView>(R.id.tvMessage)
        val btnRetry = findViewById<Button>(R.id.btnRetry)
        val btnFinish = findViewById<Button>(R.id.btnFinish)

        tvResult.text = "Правильных ответов: $correct из $total"

        if (correct == total) {
            imageResult.setImageResource(R.drawable.funny_cat)
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

        btnRetry.setOnClickListener {
            val intent = Intent(this, TestQuestionsActivity::class.java).apply {
                putExtra("TOPIC_ID", currentTopicId)
                putExtra("TOPIC_TITLE", getTopicTitle(currentTopicId))
            }
            startActivity(intent)
            finish()
        }

        btnFinish.setOnClickListener {
            finish()
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
            }
        }
    }
}