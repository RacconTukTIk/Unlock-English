package com.example.application.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.application.data.com.example.application.sampledata.WordToRepeatDao
import com.example.application.sampledata.LearnedWordDao

@Database(
    entities = [LearnedWord::class, WordToRepeat::class],
    version = 1,
    exportSchema = false
)
abstract class VocabularyDatabase : RoomDatabase() {

    abstract fun learnedWordDao(): LearnedWordDao
    abstract fun wordToRepeatDao(): WordToRepeatDao

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