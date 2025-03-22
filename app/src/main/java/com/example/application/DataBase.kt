package com.example.application

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context


import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

                ).addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Заполнение базы данных начальными данными
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                val topicDao = database.topicDao()

                // Добавление начальных тем
                val topics = listOf(
                    Topic(title = "Present Simple", description = "Basic present tense"),
                    Topic(title = "Past Simple", description = "Basic past tense"),
                    Topic(title = "Future Simple", description = "Basic future tense"),
                    Topic(title = "Present Continuous", description = "Ongoing actions in the present"),
                    Topic(title = "Past Continuous", description = "Ongoing actions in the past"),
                    Topic(title = "Future Continuous", description = "Ongoing actions in the future")
                )

                topics.forEach { topic ->
                    topicDao.insert(topic)
                }
            }
        }
    }

}