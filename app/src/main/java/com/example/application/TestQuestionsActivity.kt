package com.example.application

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestQuestionsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var db: EnglishDatabase
    private lateinit var testDao: TestDao
    private var currentTopicId = -1
    private var correctAnswersCount = 0
    private var totalQuestions = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_questions)

        db = EnglishDatabase.getDatabase(this)
        testDao = db.testDao()
        currentTopicId = intent.getIntExtra("TOPIC_ID", -1)


        setupUI()
        loadQuestions()
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.recyclerQuestions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Установка заголовка темы
        val topicTitle = intent.getStringExtra("TOPIC_TITLE") ?: "Тест"
        findViewById<TextView>(R.id.tvTopicTitle).text = topicTitle

        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            showResults()
        }

        val buttonExitTest: ImageView  = findViewById(R.id.exitTests)
        buttonExitTest.setOnClickListener {
            finish()
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                questionAdapter.currentPosition = firstVisiblePosition
                updateProgressBar()
            }
        })
    }

    private fun loadQuestions() {
        lifecycleScope.launch(Dispatchers.IO) {
            val tests = testDao.getTestsByTopicId(currentTopicId).first()
            totalQuestions = tests.size

            withContext(Dispatchers.Main) {
                questionAdapter = QuestionAdapter(tests) { isCorrect ->
                    if (isCorrect) correctAnswersCount++
                }
                recyclerView.adapter = questionAdapter
                updateProgressBar()
            }
        }
    }

    private fun updateProgressBar() {
        val progress = (questionAdapter.currentPosition + 1).toFloat() / totalQuestions
        findViewById<ProgressBar>(R.id.progressBar).progress = (progress * 100).toInt()
    }

    private fun showResults() {
        val resultIntent = Intent(this, ResultActivity::class.java).apply {
            putExtra("CORRECT_ANSWERS", correctAnswersCount)
            putExtra("TOTAL_QUESTIONS", totalQuestions)
            putExtra("TOPIC_ID", currentTopicId)
        }
        startActivity(resultIntent)
        finish()
    }



    inner class QuestionAdapter(
        private var questions: List<Test>,
        private val onAnswerSelected: (Boolean) -> Unit
    ) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

        var currentPosition = 0

        inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
            val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)
            val radioButtons = listOf<RadioButton>(
                itemView.findViewById(R.id.rbOption1),
                itemView.findViewById(R.id.rbOption2),
                itemView.findViewById(R.id.rbOption3)
            )

            fun bind(question: Test) {
                tvQuestion.text = question.question
                radioButtons.forEachIndexed { index, radioButton ->
                    radioButton.text = question.getOptionByIndex(index)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_question, parent, false)
            return QuestionViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            currentPosition = position
            val question = questions[position]
            holder.bind(question)

            holder.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedIndex = when (checkedId) {
                    R.id.rbOption1 -> 0
                    R.id.rbOption2 -> 1
                    R.id.rbOption3 -> 2
                    else -> -1
                }

                if (selectedIndex != -1) {
                    onAnswerSelected(question.correctAnswer == question.getOptionByIndex(selectedIndex))
                }
            }
        }

        override fun getItemCount() = questions.size

        private fun Test.getOptionByIndex(index: Int): String {
            return when (index) {
                0 -> option1
                1 -> option2
                2 -> option3
                else -> ""
            }
        }
    }
}