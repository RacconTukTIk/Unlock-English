package com.example.application.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WordToLearn::class, LearnedWord::class],
    version = 1,
    exportSchema = false
)
abstract class VocabularyDatabase : RoomDatabase() {

    abstract fun wordToLearnDao(): WordToLearnDao
    abstract fun learnedWordDao(): LearnedWordDao

    companion object {
        @Volatile
        private var INSTANCE: VocabularyDatabase? = null

        fun getDatabase(context: Context): VocabularyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VocabularyDatabase::class.java,
                    "vocabulary_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}