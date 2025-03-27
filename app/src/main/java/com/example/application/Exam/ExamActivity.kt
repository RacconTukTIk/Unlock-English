package com.example.application.Exam

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.application.DataBase.EnglishDatabase
import com.example.application.DataBase.Test
import com.example.application.DataBase.TestDao
import com.example.application.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections.shuffle

class ExamActivity : AppCompatActivity() {

    private lateinit var btnAnswer1: Button
    private lateinit var btnAnswer2: Button
    private lateinit var btnAnswer3: Button
    private lateinit var btnAnswer4: Button
    private lateinit var examQuestionTextView: TextView
    private lateinit var btnExit: ImageView
    private lateinit var timerTextView: TextView

    private lateinit var testDao: TestDao
    private lateinit var allExamQuestions: List<Test>
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftMillis: Long = 20 * 60 * 1000 // 20 минут

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        // Инициализация элементов
        initViews()
        setupDatabase()
        loadExamQuestions()
        startTimer()
    }

    private fun initViews() {
        examQuestionTextView = findViewById(R.id.ExamQuestion)
        btnAnswer1 = findViewById(R.id.btnAnswer1)
        btnAnswer2 = findViewById(R.id.btnAnswer2)
        btnAnswer3 = findViewById(R.id.btnAnswer3)
        btnAnswer4 = findViewById(R.id.btnAnswer4)
        btnExit = findViewById(R.id.exitExam)
        timerTextView = findViewById(R.id.timerText)

        btnExit.setOnClickListener { finishExam() }
    }

    private fun setupDatabase() {
        val db = EnglishDatabase.getDatabase(this)
        testDao = db.testDao()
    }

    private fun loadExamQuestions() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Получаем все вопросы кроме категорий (ID 1,5,9,13)
            val questions = testDao.getAllTests().filter { it.topicId !in listOf(1,5,9,13) }

            // Перемешиваем и выбираем 20 уникальных вопросов
            shuffle(questions)
            allExamQuestions = questions.take(20)

            withContext(Dispatchers.Main) {
                showNextQuestion()
            }
        }
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex < allExamQuestions.size) {
            val currentQuestion = allExamQuestions[currentQuestionIndex]
            examQuestionTextView.text = currentQuestion.question

            // Создаем список ответов и перемешиваем
            val answers = listOf(
                currentQuestion.option1,
                currentQuestion.option2,
                currentQuestion.option3,
                currentQuestion.correctAnswer
            ).shuffled()

            // Назначаем ответы на кнопки
            btnAnswer1.text = answers[0]
            btnAnswer2.text = answers[1]
            btnAnswer3.text = answers[2]
            btnAnswer4.text = answers[3]

            // Обработчики кликов
            setAnswerListeners(currentQuestion.correctAnswer)
        } else {
            finishExam()
        }
    }

    private fun setAnswerListeners(correctAnswer: String) {
        val answerButtons = listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4)

        answerButtons.forEach { button ->
            button.setOnClickListener {
                if (button.text.toString() == correctAnswer) {
                    correctAnswers++
                    button.setBackgroundColor(getColor(R.color.green))
                } else {
                    button.setBackgroundColor(getColor(R.color.red))
                }

                // Блокируем кнопки после ответа
                answerButtons.forEach { it.isEnabled = false }

                // Переход к следующему вопросу через 1 секунду
                Handler(Looper.getMainLooper()).postDelayed({
                    currentQuestionIndex++
                    resetButtons()
                    showNextQuestion()
                }, 1000)
            }
        }
    }

    private fun resetButtons() {
        listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4).forEach {
            it.setBackgroundColor(getColor(R.color.button_default))
            it.isEnabled = true
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMillis = millisUntilFinished
                updateTimerDisplay()
            }

            override fun onFinish() {
                finishExam()
            }
        }.start()
    }

    private fun updateTimerDisplay() {
        val minutes = (timeLeftMillis / 1000) / 60
        val seconds = (timeLeftMillis / 1000) % 60
        timerTextView.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun finishExam() {
        countDownTimer.cancel()

        val intent = Intent(this, ExamResultActivity::class.java).apply {
            putExtra("CORRECT_ANSWERS", correctAnswers)
            putExtra("TOTAL_QUESTIONS", allExamQuestions.size)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        finishExam()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}