import android.content.Context
import android.util.Log
import com.example.application.DataDict
import com.example.application.EnglishDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList as emptyList1

object FirebaseService {
    private const val USERS_PATH = "Users"
    private const val COMPLETED_TOPICS = "completedTopics"
    private const val COMPLETED_TESTS = "completedTests"
    private const val USER_ERRORS = "userErrors"
    private const val LEARNED_WORDS = "learnedWords"
    private const val WORDS_TO_REPEAT = "wordsToRepeat"

    private val database = Firebase.database("https://unlock-english-22c67-default-rtdb.europe-west1.firebasedatabase.app/")
    private val dbRef: DatabaseReference = database.reference

    // region Core Functions
    fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun syncAllData(context: Context) {
        try {
            loadUserProgress(context)
            loadUserErrors(context)
        } catch (e: Exception) {
            Log.e("FirebaseService", "Full sync failed: ${e.message}")
        }
    }
    // endregion

    suspend fun resetErrorsForTopic(topicId: Int) {
        val userId = getCurrentUserId() ?: return

        try {
            dbRef.child(USERS_PATH)
                .child(userId)
                .child(USER_ERRORS)
                .child(topicId.toString())
                .removeValue()
                .await()
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error resetting errors: ${e.message}")
        }
    }
    // endregion

    // region Progress Management
    suspend fun saveCompletedTopics(topicIds: List<Int>) {
        val userId = getCurrentUserId() ?: return
        dbRef.child(USERS_PATH)
            .child(userId)
            .child(COMPLETED_TOPICS)
            .setValue(topicIds)
            .await()
    }

    suspend fun saveCompletedTests(testIds: List<Int>) {
        val userId = getCurrentUserId() ?: return
        dbRef.child(USERS_PATH)
            .child(userId)
            .child(COMPLETED_TESTS)
            .setValue(testIds)
            .await()
    }

    suspend fun saveUserErrors(topicId: Int, errors: Int) {
        val userId = getCurrentUserId() ?: return

        try {
            // Получаем текущие значения из Firebase
            val currentData = dbRef.child(USERS_PATH)
                .child(userId)
                .child(USER_ERRORS)
                .child(topicId.toString())
                .get()
                .await()

            val currentErrors = currentData.child("errorCount").getValue(Int::class.java) ?: 0

            // Обновляем значения с накоплением
            val updates = hashMapOf<String, Any>(
                "errorCount" to (currentErrors + errors),
                "lastAttemptErrors" to errors
            )

            dbRef.child(USERS_PATH)
                .child(userId)
                .child(USER_ERRORS)
                .child(topicId.toString())
                .setValue(updates)
                .await()
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error saving errors: ${e.message}")
        }
    }

    suspend fun saveUserProgress(context: Context) {
        val userId = getCurrentUserId() ?: return
        val db = EnglishDatabase.getDatabase(context)

        try {
            val topics = db.topicDao().getAllTopics().first()

            val completedTopics = topics
                .filter { it.isCompleted }
                .map { it.id }

            val completedTests = topics
                .filter { it.isTestCompleted }
                .map { it.id }

            saveCompletedTopics(completedTopics)
            saveCompletedTests(completedTests)
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error saving progress: ${e.message}")
        }
    }

    suspend fun loadUserProgress(context: Context) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext
        val db = EnglishDatabase.getDatabase(context)

        try {
            val typeIndicator = object : GenericTypeIndicator<List<Int>>() {}

            val completedTopics = dbRef.child(USERS_PATH)
                .child(userId)
                .child(COMPLETED_TOPICS)
                .get()
                .await()
                .getValue(typeIndicator) ?: emptyList1()

            val completedTests = dbRef.child(USERS_PATH)
                .child(userId)
                .child(COMPLETED_TESTS)
                .get()
                .await()
                .getValue(typeIndicator) ?: emptyList1()

            db.topicDao().apply {
                completedTopics.forEach { topicId ->
                    getTopicById(topicId)?.let { topic -> // Явно указываем параметр topic
                        if (!topic.isCompleted) {
                            topic.isCompleted = true
                            update(topic)
                        }
                    }
                }

                completedTests.forEach { testId ->
                    getTopicById(testId)?.let { topic -> // Явно указываем параметр topic
                        if (!topic.isTestCompleted) {
                            topic.isTestCompleted = true
                            update(topic)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error loading progress: ${e.message}")
        }
    }

    // region Error Loading
    suspend fun loadUserErrors(context: Context) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext
        val db = EnglishDatabase.getDatabase(context)

        try {
            // Получаем все темы с ошибками из локальной базы
            val topicsWithErrors = db.topicDao().getTopicsWithErrors().first()

            // Синхронизируем каждую тему с Firebase
            topicsWithErrors.forEach { topic ->
                val updates = hashMapOf<String, Any>(
                    "errorCount" to topic.errorCount,
                    "lastAttemptErrors" to topic.lastAttemptErrors
                )

                dbRef.child(USERS_PATH)
                    .child(userId)
                    .child(USER_ERRORS)
                    .child(topic.id.toString())
                    .setValue(updates)
                    .await()
            }

            // Помечаем ошибки как синхронизированные
            db.topicDao().resetErrorsSyncFlag()

        } catch (e: Exception) {
            Log.e("FirebaseService", "Sync errors failed: ${e.message}")
        }
    }
    // endregion

    suspend fun saveLearnedWord(word: DataDict) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext
        try {
            database.getReference("$USERS_PATH/$userId/learnedWords")
                .child(word.word.hashCode().toString())
                .setValue(word)
                .await()
        } catch (e: Exception) {
            Log.e("FirebaseService", "Save learned word error: ${e.message}")
        }
    }

    // Сохранение слова для повторения
    suspend fun saveWordToRepeat(word: DataDict) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext
        try {
            database.getReference("$USERS_PATH/$userId/wordsToRepeat")
                .child(word.word.hashCode().toString())
                .setValue(word)
                .await()
        } catch (e: Exception) {
            Log.e("FirebaseService", "Save word to repeat error: ${e.message}")
        }
    }

    // Загрузка выученных слов
    suspend fun loadLearnedWords(): List<DataDict> = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext emptyList1()
        try {
            val snapshot = dbRef.child(USERS_PATH)
                .child(userId)
                .child(LEARNED_WORDS)
                .get()
                .await()

            return@withContext snapshot.children.mapNotNull {
                it.getValue(DataDict::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error loading learned words: ${e.message}")
            return@withContext emptyList1()
        }
    }

    // Загрузка слов для повторения
    suspend fun loadWordsToRepeat(): List<DataDict> = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext emptyList1()
        try {
            database.getReference("$USERS_PATH/$userId/wordsToRepeat")
                .get()
                .await()
                .children
                .mapNotNull { it.getValue(DataDict::class.java) }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Load words to repeat error: ${e.message}")
            emptyList1()
        }
    }
}


