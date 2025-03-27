package com.example.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Insert
    suspend fun insert(topic: Topic)

    @Query("SELECT * FROM topics")
    fun getAllTopics(): Flow<List<Topic>> // Используем Flow для наблюдения за изменениями

    @Update
    suspend fun update(topic: Topic) // Метод для обновления темы

    @Query("SELECT COUNT(*) FROM topics WHERE isCompleted = 1")
    fun getCompletedTopicsCount(): Flow<Int>

    @Query("SELECT * FROM topics WHERE id NOT IN (1,5,9,13)") // Исключаем категории
    fun getTopicsWithTests(): Flow<List<Topic>>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    suspend fun getTopicById(topicId: Int): Topic?

    @Query("SELECT title FROM topics WHERE id = :topicId")
    suspend fun getTopicTitle(topicId: Int): String

    @Query("SELECT COUNT(*) FROM topics WHERE isTestCompleted = 1")
    fun getCompletedTestsCount(): Flow<Int>

}

@Dao
interface TestDao {
    @Insert
    suspend fun insert(test: Test)

    @Insert
    suspend fun insertAll(tests: List<Test>)

    @Query("SELECT * FROM tests WHERE topicId = :topicId")
    fun getTestsByTopicId(topicId: Int): Flow<List<Test>> // Используем Flow для наблюдения за изменениями

    @Query("SELECT COUNT(*) FROM tests WHERE topicId = :topicId")
    suspend fun getTestCount(topicId: Int): Int

    @Query("SELECT * FROM tests")
    suspend fun getAllTests(): List<Test>

}