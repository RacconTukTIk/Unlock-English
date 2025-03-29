package com.example.application.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val russian: String,
    val english: String,
    val status: WordStatus = WordStatus.NOT_LEARNED
) {
    enum class WordStatus {
        NOT_LEARNED,
        LEARNED,
        NEED_REPEAT
    }
}