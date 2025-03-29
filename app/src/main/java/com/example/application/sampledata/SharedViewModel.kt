package com.example.application.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    fun navigateTo(event: NavigationEvent) {
        _navigationEvent.value = event
    }

    sealed class NavigationEvent {
        object LearnWords : NavigationEvent()
        object LearnedWords : NavigationEvent()
        object RepeatWords : NavigationEvent()
    }
}