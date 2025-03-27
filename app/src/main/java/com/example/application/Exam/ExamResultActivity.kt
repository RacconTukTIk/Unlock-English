package com.example.application.Exam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.application.R

class ExamResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_result)

        val correctAnswers = intent.getIntExtra("CORRECT_ANSWERS", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 20)

        val resultImage = findViewById<ImageView>(R.id.imageResult)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvMessage = findViewById<TextView>(R.id.tvMessage)
        val btnRetry = findViewById<Button>(R.id.btnRetry)
        val btnFinish = findViewById<Button>(R.id.btnFinish)

        if (correctAnswers >= 15) {
            resultImage.setImageResource(R.drawable.funny_cat)
            tvMessage.text = "Экзамен сдан!"
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.success_green))
        } else {
            resultImage.setImageResource(R.drawable.sad_cat)
            tvMessage.text = "Экзамен не сдан"
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.error_red))
        }

        tvResult.text = "Правильных ответов: $correctAnswers из $totalQuestions"

        btnRetry.setOnClickListener {
            startActivity(Intent(this, ExamActivity::class.java))
            finish()
        }

        btnFinish.setOnClickListener {
            finish()
        }
    }
}