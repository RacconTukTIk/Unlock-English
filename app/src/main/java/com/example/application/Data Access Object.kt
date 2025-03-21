package com.example.application

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Insert
    suspend fun insert(topic: Topic)

    @Query("SELECT * FROM topics")
    fun getAllTopics(): LiveData<List<Topic>>

    @Query("UPDATE topics SET isCompleted = 1 WHERE id = :topicId")
    suspend fun markTopicCompleted(topicId: Int)

    @Query("SELECT COUNT(*) FROM topics WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    fun getTopicById(topicId: Int): Flow<Topic>
}

@Dao
interface TestDao {
    @Insert
    suspend fun insert(test: Test)

    @Query("SELECT * FROM tests WHERE topicId = :topicId")
    fun getTestsByTopicId(topicId: Int): Flow<List<Test>> // Используем Flow для наблюдения за изменениями
}