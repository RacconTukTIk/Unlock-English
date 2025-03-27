package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.application.R

class MarathonResultActivity : AppCompatActivity() {
    // Объявляем элементы интерфейса
    private lateinit var tvResult: TextView
    private lateinit var tvMessage: TextView
    private lateinit var imageResult: ImageView
    private lateinit var btnRetry: Button
    private lateinit var btnFinish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marathon_result)

        // Инициализируем элементы
        tvResult = findViewById(R.id.tvResult)
        tvMessage = findViewById(R.id.tvMessage)
        imageResult = findViewById(R.id.imageResult)
        btnRetry = findViewById(R.id.btnRetry)
        btnFinish = findViewById(R.id.btnFinish)

        val correct = intent.getIntExtra("CORRECT_ANSWERS", 0)
        val total = intent.getIntExtra("TOTAL_QUESTIONS", 0)

        setupResultUI(correct, total)
        setupButtons()
    }

    private fun setupResultUI(correct: Int, total: Int) {
        tvResult.text = "Правильных ответов: $correct из $total"
        tvMessage.text = when {
            correct == total -> "Идеальный результат! 🎉"
            correct > total / 2 -> "Хороший результат! 👍"
            else -> "Попробуйте еще раз! 💪"
        }

        imageResult.setImageResource(
            if (correct == total) R.drawable.funny_cat
            else if (correct > total / 2) R.drawable.cat
            else R.drawable.sad_cat
        )
    }

    private fun setupButtons() {
        btnRetry.setOnClickListener {
            val intent = Intent(this, MarathonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnFinish.setOnClickListener {
            finish()
        }
    }
}