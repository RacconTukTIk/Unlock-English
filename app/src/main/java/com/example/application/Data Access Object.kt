package com.example.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Insert
    suspend fun insert(topic: Topic)

    @Query("SELECT * FROM topics")
    fun getAllTopics(): Flow<List<Topic>> // Используем Flow для наблюдения за изменениями
}

@Dao
interface TestDao {
    @Insert
    suspend fun insert(test: Test)

    @Query("SELECT * FROM tests WHERE topicId = :topicId")
    fun getTestsByTopicId(topicId: Int): Flow<List<Test>> // Используем Flow для наблюдения за изменениями
}