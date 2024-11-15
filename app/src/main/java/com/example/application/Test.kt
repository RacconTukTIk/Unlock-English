package com.example.application

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "tests")
data class Test(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "task")
    var task: String,
    @ColumnInfo(name = "answer")
    var answer: String,
)
