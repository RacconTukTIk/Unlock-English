package com.example.application
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String
    val description: String,
    var isCompleted: Boolean = false
)

@Entity(tableName = "tests")
data class Test(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: Int, // Связь с темой
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val correctAnswer: String
)