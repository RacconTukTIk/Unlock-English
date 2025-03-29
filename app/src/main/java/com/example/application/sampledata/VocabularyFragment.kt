package com.example.application

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.application.databinding.FragmentVocabularyBinding

class VocabularyFragment : Fragment() {
    private var _binding: FragmentVocabularyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVocabularyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLearnWords.setOnClickListener {
            startActivity(Intent(requireActivity(), LearningActivity::class.java))
        }

        binding.buttonLearned.setOnClickListener {
            startActivity(Intent(requireActivity(), LearnedWordsActivity::class.java))
        }

        binding.buttonNeedToRepeat.setOnClickListener {
            startActivity(Intent(requireActivity(), RepeatWordsActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}