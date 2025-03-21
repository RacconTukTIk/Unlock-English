package com.example.application.sampledata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.application.data.LearnedWord
import kotlinx.coroutines.flow.Flow

// DAO для выученных слов
@Dao
interface LearnedWordDao {
    @Insert
    suspend fun insert(word: LearnedWord)

    @Query("SELECT * FROM learned_words ORDER BY dateAdded DESC")
    fun getAllWords(): Flow<List<LearnedWord>>

    @Query("DELETE FROM learned_words WHERE id = :wordId")
    suspend fun deleteWord(wordId: Int)
}