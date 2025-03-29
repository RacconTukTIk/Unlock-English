import android.content.Context
import android.util.Log
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

object FirebaseService {
    private const val USERS_PATH = "Users"
    private const val COMPLETED_TOPICS = "completedTopics"
    private const val COMPLETED_TESTS = "completedTests"
    private const val USER_ERRORS = "userErrors"

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

    // region Error Handling
    suspend fun saveUserErrors(topicId: Int, errors: Int) {
        val userId = getCurrentUserId() ?: return

        try {
            val updates = hashMapOf<String, Any>(
                "errorCount" to errors,
                "lastAttemptErrors" to errors
            )

            dbRef.child(USERS_PATH)
                .child(userId)
                .child(USER_ERRORS)
                .child(topicId.toString())
                .updateChildren(updates)
                .await()
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error saving errors: ${e.message}")
        }
    }

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

    suspend fun saveUserProgress(context: Context) {
        val userId = getCurrentUserId() ?: return
        val db = EnglishDatabase.getDatabase(context)

        try {
            val topics = db.topicDao().getAllTopics().first() // Добавляем .first() для Flow<List<Topic>>

            val completedTopics = topics
                .filter { topic -> topic.isCompleted }
                .map { topic -> topic.id }

            val completedTests = topics
                .filter { topic -> topic.isTestCompleted }
                .map { topic -> topic.id }

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
                .getValue(typeIndicator) ?: emptyList()

            val completedTests = dbRef.child(USERS_PATH)
                .child(userId)
                .child(COMPLETED_TESTS)
                .get()
                .await()
                .getValue(typeIndicator) ?: emptyList()

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
            val snapshot = dbRef.child(USERS_PATH)
                .child(userId)
                .child(USER_ERRORS)
                .get()
                .await()

            snapshot.children.forEach { topicSnapshot ->
                val topicId = topicSnapshot.key?.toIntOrNull() ?: return@forEach
                val errorCount = topicSnapshot.child("errorCount").getValue(Int::class.java) ?: 0
                val lastAttempt = topicSnapshot.child("lastAttemptErrors").getValue(Int::class.java) ?: 0

                db.topicDao().apply {
                    updateErrorCount(topicId, errorCount)
                    updateLastAttemptErrors(topicId, lastAttempt)
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error loading errors: ${e.message}")
        }
    }
    // endregion
}