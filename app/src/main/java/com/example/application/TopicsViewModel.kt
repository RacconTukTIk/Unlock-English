package com.example.application

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData // Добавьте этот импорт
import androidx.lifecycle.viewModelScope // Добавьте этот импорт
import kotlinx.coroutines.launch

class TopicsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TopicsRepository(EnglishDatabase.getDatabase(application))

    val allTopics: LiveData<List<Topic>> = repository.getAllTopics()
    // LiveData для счетчика пройденных тем
    val completedTopicsCount: LiveData<Int> = repository.getCompletedCount().asLiveData()

    // LiveData для получения темы по ID
    fun getTopic(topicId: Int): LiveData<Topic> = repository.getTopic(topicId).asLiveData()

    // Метод для отметки темы как пройденной
    fun markTopicCompleted(topicId: Int) = viewModelScope.launch {
        repository.markTopicCompleted(topicId)
    }
}

class TopicsRepository(private val db: EnglishDatabase) {
    // Получение счетчика пройденных тем (возвращает Flow)
    fun getCompletedCount() = db.topicDao().getCompletedCount()

    // Получение темы по ID (возвращает Flow)
    fun getTopic(topicId: Int) = db.topicDao().getTopicById(topicId)
    fun getAllTopics(): LiveData<List<Topic>> = db.topicDao().getAllTopics()
    // Обновление статуса темы
    suspend fun markTopicCompleted(topicId: Int) {
        db.topicDao().markTopicCompleted(topicId)
    }
}