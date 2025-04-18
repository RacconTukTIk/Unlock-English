package com.example.application

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {

    private lateinit var tvTestProgress: TextView
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
        tvTestProgress = view.findViewById(R.id.tvTestProgress)

        // Загружаем количество пройденных тем и тестов
        loadCompletedTopicsCount()
        //loadCompletedTestCount()

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

        val buttonTest: Button = view.findViewById(R.id.button_test)
        buttonTest.setOnClickListener {
            val intent = Intent(requireActivity(),TestActivity::class.java)
            startActivity(intent)
        }

        val buttonMarathon: Button = view.findViewById(R.id.button_marathon)
        buttonMarathon.setOnClickListener {
            val intent = Intent(requireActivity(),MarathonActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun loadCompletedTopicsCount() {
        lifecycleScope.launch {
            topicDao.getCompletedTopicsCount().collect { count ->
                tvTopicsProgress.text = "$count/18"
            }
        }
    }

    private fun loadCompletedTestCount() {
        lifecycleScope.launch {
            topicDao.getCompletedTestsCount().collect { count ->
                tvTestProgress.text = "$count/14"
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FirebaseService.getCurrentUserId() == null) {
            startActivity(Intent(requireActivity(), LogInActivity::class.java))
            requireActivity().finish()
        } else {
            lifecycleScope.launch {
                try {
                    FirebaseService.loadUserProgress(requireContext())
                    FirebaseService.loadUserErrors(requireContext())
                    updateProgressCounters()
                } catch (e: Exception) {
                    Log.e("MenuFragment", "Ошибка загрузки данных: ${e.message}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(requireActivity(), LogInActivity::class.java))
            requireActivity().finish()
        } else {
            lifecycleScope.launch {
                try {
                    FirebaseService.loadUserProgress(requireContext())
                    FirebaseService.loadUserErrors(requireContext())
                    updateProgressCounters()
                } catch (e: Exception) {
                    Log.e("MenuFragment", "Ошибка обновления данных: ${e.message}")
                }
            }
        }
    }

    private fun updateProgressCounters() {
        loadCompletedTopicsCount()
        loadCompletedTestCount()
    }


    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}