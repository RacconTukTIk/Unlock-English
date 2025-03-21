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
import com.example.application.R
import com.example.application.MistakesActivity
import com.example.dicti.DictTechActivity
import androidx.fragment.app.viewModels


class MenuFragment : Fragment() {
    private lateinit var tvProgress: TextView
    private val viewModel: TopicsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_menu, container, false)
        tvProgress = view.findViewById(R.id.tvTopicsProgress)

        viewModel.completedTopicsCount.observe(viewLifecycleOwner) { count ->
            tvProgress.text = "$count/12"
        }
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
    
    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}