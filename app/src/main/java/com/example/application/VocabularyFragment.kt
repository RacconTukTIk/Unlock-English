package com.example.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.application.databinding.ActivityVocabularyBinding
class VocabularyFragment : Fragment() {

    private var _binding: ActivityVocabularyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализация ViewBinding
        _binding = ActivityVocabularyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработчики кликов для кнопок
        binding.buttonLearned.setOnClickListener {
            // Действие при нажатии на кнопку "Выученные"
            Toast.makeText(requireContext(), "Выученные", Toast.LENGTH_SHORT).show()
        }

        binding.buttonNeedRepeat.setOnClickListener {
            // Действие при нажатии на кнопку "Нужно повторить"
            Toast.makeText(requireContext(), "Нужно повторить", Toast.LENGTH_SHORT).show()
        }

        binding.buttonNeedRepeat.setOnClickListener {
            // Действие при нажатии на кнопку "Учить слова"
            Toast.makeText(requireContext(), "Учить слова!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Очистка binding для предотвращения утечек памяти
        _binding = null
    }
}