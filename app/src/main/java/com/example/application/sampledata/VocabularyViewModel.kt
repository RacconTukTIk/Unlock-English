package com.example.application.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.LearnedWord
import com.example.application.data.VocabularyRepository
import com.example.application.data.WordToLearn
import kotlinx.coroutines.launch

class VocabularyViewModel(private val repository: VocabularyRepository) : ViewModel() {

    // Слова для изучения
    val allWordsToLearn = repository.allWordsToLearn

    fun insertWordToLearn(word: WordToLearn) = viewModelScope.launch {
        repository.insertWordToLearn(word)
    }

    fun deleteWordToLearn(wordId: Int) = viewModelScope.launch {
        repository.deleteWordToLearn(wordId)
    }

    // Выученные слова
    val allLearnedWords = repository.allLearnedWords

    fun insertLearnedWord(word: LearnedWord) = viewModelScope.launch {
        repository.insertLearnedWord(word)
    }

    fun deleteLearnedWord(wordId: Int) = viewModelScope.launch {
        repository.deleteLearnedWord(wordId)
    }
}