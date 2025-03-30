package com.example.application.sampledata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {
    val learnedWords = repository.getLearnedWords()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val wordsToRepeat = repository.getWordsToRepeat()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}