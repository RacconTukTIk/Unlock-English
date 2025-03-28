package com.example.application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

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
}