package com.example.application.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.LearnedWord
import com.example.application.data.VocabularyRepository
import com.example.application.data.WordToRepeat
import kotlinx.coroutines.launch

class VocabularyViewModel(private val repository: VocabularyRepository) : ViewModel() {

    // Выученные слова
    val allLearnedWords = repository.allLearnedWords

    fun insertLearnedWord(word: LearnedWord) = viewModelScope.launch {
        repository.insertLearnedWord(word)
    }

    fun deleteLearnedWord(wordId: Int) = viewModelScope.launch {
        repository.deleteLearnedWord(wordId)
    }

    // Слова для повторения
    val allWordsToRepeat = repository.allWordsToRepeat

    fun insertWordToRepeat(word: WordToRepeat) = viewModelScope.launch {
        repository.insertWordToRepeat(word)
    }

    fun deleteWordToRepeat(wordId: Int) = viewModelScope.launch {
        repository.deleteWordToRepeat(wordId)
    }
}