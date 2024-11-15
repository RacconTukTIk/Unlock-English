package com.example.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Test::class], version = 1)
abstract class EnglishDatabase : RoomDatabase()
{
    abstract fun getDao(): Dao
    
    companion object{
        fun getDb(context: Context): EnglishDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                EnglishDatabase::class.java,
                name = "testEnglish.db"
            ).build()
        }
    }
}