package com.example.application.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Word
import com.example.application.data.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LearningViewModel(
    private val repository: WordRepository
) : ViewModel() {
    private val _currentWord = MutableStateFlow<Word?>(null)
    val currentWord: StateFlow<Word?> = _currentWord

    init {
        loadNextWord()
    }

    fun loadNextWord() {
        viewModelScope.launch {
            _currentWord.value = repository.getRandomWord()
        }
    }

    fun checkAnswer(userAnswer: String): Boolean {
        val word = _currentWord.value ?: return false
        val isCorrect = userAnswer.equals(word.english, ignoreCase = true)

        viewModelScope.launch {
            if (isCorrect) {
                repository.markAsLearned(word)
            } else {
                repository.markAsNeedRepeat(word)
            }
        }

        return isCorrect
    }
}