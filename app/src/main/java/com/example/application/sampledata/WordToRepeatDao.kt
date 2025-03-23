package com.example.application.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordToLearnDao {
    @Insert
    suspend fun insert(word: WordToLearn)

    @Query("SELECT * FROM words_to_learn ORDER BY dateAdded DESC")
    fun getAllWords(): Flow<List<WordToLearn>>

    @Query("DELETE FROM words_to_learn WHERE id = :wordId")
    suspend fun deleteWord(wordId: Int)
}