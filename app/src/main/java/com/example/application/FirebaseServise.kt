import android.content.Context
import android.widget.Toast
import com.example.application.EnglishDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.firebase.database.FirebaseDatabase

object FirebaseService {
    private const val USERS_PATH = "Users"
    private const val COMPLETED_TOPICS = "completedTopics"
    private const val COMPLETED_TESTS = "completedTests"

    private val database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://unlock-english-22c67-default-rtdb.europe-west1.firebasedatabase.app/")
    private val dbRef: DatabaseReference = database.reference

    fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun saveCompletedTopics(topicIds: List<Int>) {
        val userId = getCurrentUserId() ?: return
        dbRef.child(USERS_PATH).child(userId).child(COMPLETED_TOPICS)
            .setValue(topicIds)
            .await()
    }

    suspend fun saveCompletedTests(testIds: List<Int>) {
        val userId = getCurrentUserId() ?: return
        dbRef.child(USERS_PATH).child(userId).child(COMPLETED_TESTS)
            .setValue(testIds)
            .await()
    }


    suspend fun loadUserProgress(context: Context) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext
        val db = EnglishDatabase.getDatabase(context)

        try {
            val snapshot = dbRef.child(USERS_PATH).child(userId).get().await()

            // Загрузка списка завершённых тем
            val completedTopics = snapshot.child(COMPLETED_TOPICS)
                .getValue(object : GenericTypeIndicator<List<Int>>() {}) ?: emptyList()

            // Загрузка списка завершённых тестов
            val completedTests = snapshot.child(COMPLETED_TESTS)
                .getValue(object : GenericTypeIndicator<List<Int>>() {}) ?: emptyList()

            db.topicDao().apply {
                // Обновляем только полученные данные
                setTopicsCompleted(completedTopics)
                setTestsCompleted(completedTests)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Ошибка загрузки прогресса", Toast.LENGTH_SHORT).show()
            }
        }
    }
}