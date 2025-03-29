package com.example.application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var currentSessionKey:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // Проверяем пользователя без задержки
        checkAuthState()
    }

    private fun checkAuthState() {
        val currentUser = auth.currentUser
        Log.d("AUTH_DEBUG", "Current user: ${currentUser?.uid ?: "null"}")

        if (currentUser != null) {
            // Переход сразу, без проверки токена
            navigateToMainApp()
            startNewSession()
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToMainApp() {
        Log.d("AUTH_DEBUG", "User authenticated: ${auth.currentUser?.uid}")
        startActivity(Intent(this, BottomNavigationActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        Log.d("AUTH_DEBUG", "No user session")
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }

    private fun startNewSession() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val userId = it.uid
            val loginRef = FirebaseDatabase.getInstance("https://unlock-english-22c67-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users/$userId/logins")
                .push()

            currentSessionKey = loginRef.key // Сохраняем ключ сессии
            val sessionStart = HashMap<String, Any>()
            sessionStart["start"] = System.currentTimeMillis()
            sessionStart["end"] = 0 // Пока не завершена
            loginRef.setValue(sessionStart)
        }
    }

}