package com.example.application.data.com.example.application.sampledata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.application.data.WordToRepeat
import kotlinx.coroutines.flow.Flow

// DAO для слов, которые нужно повторить
@Dao
interface WordToRepeatDao {
    @Insert
    suspend fun insert(word: WordToRepeat)

    @Query("SELECT * FROM words_to_repeat ORDER BY nextRepeatDate ASC")
    fun getAllWords(): Flow<List<WordToRepeat>>

    @Query("DELETE FROM words_to_repeat WHERE id = :wordId")
    suspend fun deleteWord(wordId: Int)
}