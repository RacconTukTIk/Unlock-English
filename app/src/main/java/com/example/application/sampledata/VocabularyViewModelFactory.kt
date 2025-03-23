package com.example.application.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.application.data.VocabularyRepository

class VocabularyViewModelFactory(
    private val repository: VocabularyRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VocabularyViewModel::class.java)) {
            return VocabularyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}