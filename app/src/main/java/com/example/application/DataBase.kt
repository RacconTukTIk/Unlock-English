package com.example.application

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Topic::class, Test::class], version = 2, exportSchema = false) // Версия изменена на 2
abstract class EnglishDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao
    abstract fun testDao(): TestDao

    companion object {
        @Volatile // Добавлено для потокобезопасности
        private var INSTANCE: EnglishDatabase? = null // Добавлено объявление INSTANCE

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE topics ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): EnglishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EnglishDatabase::class.java,
                    "english_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(DatabaseCallback(context.applicationContext)) // Добавлено подключение колбэка
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                // Используем новый экземпляр базы данных вместо getDatabase()
                val database = Room.databaseBuilder(
                    context,
                    EnglishDatabase::class.java,
                    "english_database"
                ).build()

                val topics = listOf(
                    Topic(title = "Present Simple", description = "Basic present tense"),
                    Topic(title = "Past Simple", description = "Basic past tense"),
                    Topic(title = "Future Simple", description = "Basic future tense"),
                    Topic(
                        title = "Present Continuous",
                        description = "Ongoing actions in the present"
                    ),
                    Topic(title = "Past Continuous", description = "Ongoing actions in the past"),
                    Topic(
                        title = "Future Continuous",
                        description = "Ongoing actions in the future"
                    )
                )

                try {
                    topics.forEach { topic ->
                        database.topicDao().insert(topic)
                        Log.d("DB", "Inserted topic: ${topic.title}")
                    }
                } catch (e: Exception) {
                    Log.e("DB", "Error inserting topics", e)
                }
            }
        }
    }
}