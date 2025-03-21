package com.example.application.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Сущность для выученных слов
@Entity(tableName = "learned_words")
data class LearnedWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val translation: String,
    val dateAdded: Long // Дата добавления слова
)

// Сущность для слов, которые нужно повторить
@Entity(tableName = "words_to_repeat")
data class WordToRepeat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val translation: String,
    val dateAdded: Long, // Дата добавления слова
    val nextRepeatDate: Long // Дата следующего повторения
)