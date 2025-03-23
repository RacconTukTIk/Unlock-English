package com.example.application.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learned_words")
data class LearnedWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String, // Слово на английском
    val translation: String, // Перевод на русский
    val dateAdded: Long // Дата добавления
)