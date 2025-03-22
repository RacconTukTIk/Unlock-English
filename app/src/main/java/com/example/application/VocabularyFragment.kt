package com.example.application

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.application.R

class VocabularyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Надуваем макет для фрагмента
        val view = inflater.inflate(R.layout.fragment_vocabulary, container, false)

        // Находим кнопки в макете
        val learnedButton: Button = view.findViewById(R.id.button_learned)
        val needToRepeatButton: Button = view.findViewById(R.id.button_need_to_repeat)
        val reminderButton: Button = view.findViewById(R.id.button_learnWords)

        // Обработчик клика для кнопки "Выученные"
        learnedButton.setOnClickListener {
            // Действие при нажатии на кнопку "Выученные"
            Toast.makeText(requireContext(), "Выученные", Toast.LENGTH_SHORT).show()
            // Пример перехода на другую активность или фрагмент
            // val intent = Intent(requireActivity(), LearnedActivity::class.java)
            // startActivity(intent)
        }

        // Обработчик клика для кнопки "Нужно повторить"
        needToRepeatButton.setOnClickListener {
            // Действие при нажатии на кнопку "Нужно повторить"
            Toast.makeText(requireContext(), "Нужно повторить", Toast.LENGTH_SHORT).show()
            // Пример перехода на другую активность или фрагмент
            // val intent = Intent(requireActivity(), RepeatActivity::class.java)
            // startActivity(intent)
        }

        // Обработчик клика для кнопки "Учить слова"
        reminderButton.setOnClickListener {
            // Действие при нажатии на кнопку "Учить слова"
            Toast.makeText(requireContext(), "Учить слова!", Toast.LENGTH_SHORT).show()
            // Пример перехода на другую активность или фрагмент
            // val intent = Intent(requireActivity(), ReminderActivity::class.java)
            // startActivity(intent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = VocabularyFragment()
    }
}