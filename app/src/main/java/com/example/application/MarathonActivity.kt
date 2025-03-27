package com.example.application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.application.EnglishDatabase
import com.example.application.Test
import com.example.application.TestDao
import com.example.application.R
import kotlinx.coroutines.launch

class MarathonActivity : AppCompatActivity() {
    private lateinit var tests: List<Test>
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private lateinit var marathonQuestion: TextView
    private lateinit var btnOption1: Button
    private lateinit var btnOption2: Button
    private lateinit var btnOption3: Button
    private lateinit var btnOption4: Button
    private lateinit var progressText: TextView
    private lateinit var exitMarathon: ImageView
    private lateinit var answerButtons: List<Button>
    private var correctAnswerIndex = -1
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marathon)

        marathonQuestion = findViewById(R.id.marathonQuestion)
        exitMarathon = findViewById(R.id.exitMarathon)
        btnOption1 = findViewById(R.id.btnOption1)
        btnOption2 = findViewById(R.id.btnOption2)
        btnOption3 = findViewById(R.id.btnOption3)
        btnOption4 = findViewById(R.id.btnOption4)
        progressText = findViewById(R.id.progressText)

        loadAllTests()
        setupExitButton()

        answerButtons = listOf(btnOption1, btnOption2, btnOption3, btnOption4)
    }

    private fun loadAllTests() {
        lifecycleScope.launch {
            tests = EnglishDatabase.getDatabase(this@MarathonActivity)
                .testDao()
                .getAllTests()
                .shuffled()

            if (tests.isNotEmpty()) {
                showNextQuestion()
            }
        }
    }

    private fun showNextQuestion() {
        val currentTest = tests[currentQuestionIndex]
        val options = listOf(
            currentTest.option1,
            currentTest.option2,
            currentTest.option3,
            currentTest.option4
        ).shuffled()

        correctAnswerIndex = options.indexOfFirst { it == currentTest.correctAnswer }

        answerButtons.forEachIndexed { index, button ->
            button.text = options[index]
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.button_default))
            button.isEnabled = true
        }

        marathonQuestion.text = currentTest.question
        progressText.text = "${currentQuestionIndex + 1}/${tests.size}"
        setupClickListeners(currentTest)
    }

    private fun checkAnswer(selectedButton: Button, selectedAnswer: String, correctAnswer: String) {
        // Блокируем все кнопки
        answerButtons.forEach { it.isEnabled = false }

        // Подсветка ответов
        if (selectedAnswer == correctAnswer) {
            correctAnswers++
            selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        } else {
            selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            answerButtons[correctAnswerIndex].setBackgroundColor(
                ContextCompat.getColor(this, R.color.green)
            )
        }

        // Задержка перед переходом
        handler.postDelayed({
            if (currentQuestionIndex < tests.size - 1) {
                currentQuestionIndex++
                showNextQuestion()
            } else {
                showResults()
            }
        }, 1000) // 1 секунды задержки
    }

    private fun setupClickListeners(currentTest: Test) {
        answerButtons.forEach { button ->
            button.setOnClickListener {
                checkAnswer(button, button.text.toString(), currentTest.correctAnswer)
            }
        }
    }


    private fun showResults() {
        val intent = Intent(this, MarathonResultActivity::class.java).apply {
            putExtra("CORRECT_ANSWERS", correctAnswers)
            putExtra("TOTAL_QUESTIONS", tests.size)
        }
        startActivity(intent)
        finish()
    }

    private fun setupExitButton() {
        exitMarathon.setOnClickListener {
            finish()
        }
    }

    private fun shuffleAnswers(currentTest: Test): List<String> {
        return listOf(
            currentTest.option1,
            currentTest.option2,
            currentTest.option3,
            currentTest.option4
        ).shuffled()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}