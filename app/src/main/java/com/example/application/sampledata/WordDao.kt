package com.example.application.sampledata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert
    suspend fun insertAll(words: List<Word>)

    @Query("SELECT * FROM words WHERE status = 'LEARNED'")
    fun getLearnedWords(): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE status = 'NEED_REPEAT'")
    fun getWordsToRepeat(): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE status = 'NOT_LEARNED' ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): Word?

    @Update
    suspend fun updateWord(word: Word)
}