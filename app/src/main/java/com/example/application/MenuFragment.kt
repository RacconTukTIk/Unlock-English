package com.example.application

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.application.R
import com.example.application.MistakesActivity
import com.example.dicti.DictTechActivity
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {

    private lateinit var tvTopicsProgress: TextView
    private lateinit var englishDatabase: EnglishDatabase
    private lateinit var topicDao: TopicDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_menu, container, false)

        // Инициализация базы данных и DAO
        englishDatabase = EnglishDatabase.getDatabase(requireContext())
        topicDao = englishDatabase.topicDao()

        // Находим TextView для отображения прогресса
        tvTopicsProgress = view.findViewById(R.id.tvTopicsProgress)

        // Загружаем количество пройденных тем
        loadCompletedTopicsCount()

        val buttonThemes: Button = view.findViewById(R.id.button_themes)
        buttonThemes.setOnClickListener {
            val intent = Intent(requireActivity(), ThemesActivity::class.java)
            startActivity(intent)
        }

        val buttonError: Button = view.findViewById(R.id.button_mistakes)
        buttonError.setOnClickListener {
            val intent = Intent(requireActivity(), MistakesActivity::class.java)
            startActivity(intent)
        }

        val buttonExam: Button = view.findViewById(R.id.button_exam)
        buttonExam.setOnClickListener {
            val intent = Intent(requireActivity(), ExamActivity::class.java)
            startActivity(intent)
        }
        return view
    }
    private fun loadCompletedTopicsCount() {
        lifecycleScope.launch {
            topicDao.getCompletedTopicsCount().collect { count ->
                tvTopicsProgress.text = "$count/6"
            }
        }
    }
    
    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}