// SessionManager.kt
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
    private val database = FirebaseDatabase.getInstance("https://unlock-english-22c67-default-rtdb.europe-west1.firebasedatabase.app/")

    // Запуск новой сессии (вызывается при входе пользователя)
    fun startNewSession(userId: String) {
        val sessionRef = database.getReference("Users/$userId/logins").push()
        val sessionKey = sessionRef.key ?: return

        // Сохраняем ключ сессии
        prefs.edit().putString("current_session_key", sessionKey).apply()

        // Записываем время начала
        sessionRef.child("start").setValue(System.currentTimeMillis())
    }

    // Завершение сессии (вызывается при выходе или закрытии приложения)
    fun endSession(userId: String) {
        val sessionKey = prefs.getString("current_session_key", null) ?: return

        // Обновляем время окончания
        database.getReference("Users/$userId/logins/$sessionKey/end")
            .setValue(System.currentTimeMillis())
            .addOnSuccessListener {
                prefs.edit().remove("current_session_key").apply()
            }
    }
}