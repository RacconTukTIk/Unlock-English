package com.example.application

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.application.data.VocabularyDatabase
import com.example.application.data.VocabularyRepository
import com.example.application.databinding.FragmentVocabularyBinding
import com.example.application.ui.VocabularyViewModel
import com.example.application.ui.VocabularyViewModelFactory

class VocabularyFragment : Fragment() {

    private var _binding: FragmentVocabularyBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VocabularyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVocabularyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация ViewModel
        val database = VocabularyDatabase.getDatabase(requireContext())
        val repository = VocabularyRepository(database.wordToLearnDao(), database.learnedWordDao())
        viewModel = ViewModelProvider(this, VocabularyViewModelFactory(repository)).get(
            VocabularyViewModel::class.java)


        binding.buttonLearnWords.setOnClickListener {
            // Переход к фрагменту LearnWordFragment
            val learnWordFragment = LearnWordFragment()
            val fragmentManager: FragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Заменяем текущий фрагмент на LearnWordFragment
            fragmentTransaction.replace(R.id.button_learnWords, learnWordFragment)
            fragmentTransaction.addToBackStack(null) // Добавляем транзакцию в стек возврата
            fragmentTransaction.commit()
        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = VocabularyFragment()
    }

    }