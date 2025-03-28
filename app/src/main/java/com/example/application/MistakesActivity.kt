package com.example.application

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.LogInActivity
import com.example.application.R
import com.example.application.databinding.LogInActivityBinding
import com.example.dicti.DictTechActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch


class MistakesActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var resultImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mistakes)

        sharedPref = getSharedPreferences("app_stats", Context.MODE_PRIVATE)
        resultImageView = findViewById(R.id.resultImage)

        setupRecyclerView()
        loadStatistics()

        var buttonExit: ImageView = findViewById(R.id.exitMistakes)
        buttonExit.setOnClickListener {
            finish()
        }
    }

    private fun loadStatistics() {
        val totalErrors = sharedPref.getInt("total_errors", 0)
        val totalCorrect = sharedPref.getInt("total_correct", 0)
        val totalQuestions = totalErrors + totalCorrect
        val successRate = if (totalQuestions > 0) (totalCorrect * 100 / totalQuestions) else 0

        findViewById<TextView>(R.id.correctAnswersCount).text = totalCorrect.toString()
        findViewById<TextView>(R.id.successRateText).text =
            "Процент успешности: $successRate%"
    }

    private fun setupRecyclerView() {
        val errorsRecycler = findViewById<RecyclerView>(R.id.errorsRecycler)
        errorsRecycler.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            EnglishDatabase.getDatabase(this@MistakesActivity)
                .topicDao()
                .getTopicsWithErrors()
                .collect { topics ->
                    errorsRecycler.adapter = ErrorsAdapter(topics).apply {
                        onRetryClick = { topic ->
                            startActivity(
                                Intent(this@MistakesActivity, TestQuestionsActivity::class.java).apply {
                                    putExtra("TOPIC_ID", topic.id)
                                    putExtra("TOPIC_TITLE", topic.title)
                                }
                            )
                        }
                    }

                    // Перенесем проверку пустого списка сюда
                    if (topics.isEmpty()) {
                        resultImageView.setImageResource(R.drawable.funny_cat)
                        findViewById<TextView>(R.id.resultMessage).text = "Все темы пройдены успешно!"
                    } else {
                        resultImageView.setImageResource(R.drawable.sad_cat)
                        findViewById<TextView>(R.id.resultMessage).text = "Есть ошибки для повторения"
                    }
                }
        }
    }
}

class ErrorsAdapter(private val items: List<Topic>) : RecyclerView.Adapter<ErrorsAdapter.ViewHolder>() {
    var onRetryClick: (Topic) -> Unit = {}

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val totalErrors: TextView = view.findViewById(R.id.tvTotalErrors)
        val lastErrors: TextView = view.findViewById(R.id.tvLastErrors)
        val retryButton: Button = view.findViewById(R.id.btnRetry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_error, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = items[position]
        holder.title.text = topic.title
        holder.totalErrors.text = "Всего ошибок: ${topic.errorCount}"
        holder.lastErrors.text = "Последняя попытка: ${topic.lastAttemptErrors} ошибок"
        holder.retryButton.setOnClickListener { onRetryClick(topic) }
    }

    override fun getItemCount() = items.size
}