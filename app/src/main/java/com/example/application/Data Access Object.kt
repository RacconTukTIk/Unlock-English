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

    @Query("UPDATE topics SET errorCount = errorCount + :count WHERE id = :topicId")
    suspend fun addErrors(topicId: Int, count: Int)

    @Query("UPDATE topics SET errorCount = 0 WHERE id = :topicId")
    suspend fun resetErrors(topicId: Int)

    @Query("UPDATE topics SET lastAttemptErrors = :errors WHERE id = :topicId")
    suspend fun updateLastAttemptErrors(topicId: Int, errors: Int)

    @Query("SELECT * FROM topics WHERE lastAttemptErrors > 0")
    fun getTopicsWithErrors(): Flow<List<Topic>>

    @Query("UPDATE topics SET isCompleted = 0, isTestCompleted = 0, errorCount = 0, lastAttemptErrors = 0")
    suspend fun resetAllProgress()

    @Query("UPDATE topics SET isCompleted = 1 WHERE id IN (:ids)")
    suspend fun setTopicsCompleted(ids: List<Int>)

    @Query("UPDATE topics SET isTestCompleted = 1 WHERE id IN (:ids)")
    suspend fun setTestsCompleted(ids: List<Int>)

    @Query("UPDATE topics SET isTestCompleted = 0")
    suspend fun resetTestProgress()

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