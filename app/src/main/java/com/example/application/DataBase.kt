package com.example.application

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Topic::class, Test::class], version = 1, exportSchema = false)
abstract class EnglishDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao
    abstract fun testDao(): TestDao

    companion object {
        @Volatile
        private var INSTANCE: EnglishDatabase? = null

        fun getDatabase(context: Context): EnglishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EnglishDatabase::class.java,
                    "english_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}