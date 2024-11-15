package com.example.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertTest(item: Test)
    @Query("SELECT * FROM tests")
    fun getAllTest(): Flow<List<Test>>
}