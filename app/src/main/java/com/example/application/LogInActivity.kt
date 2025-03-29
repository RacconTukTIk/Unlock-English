package com.example.application

import SessionManager
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.MainActivity
import com.example.application.databinding.LogInActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: LogInActivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        sessionManager=SessionManager(applicationContext)

        setupRegistrationLink()
        setupLoginButton()
    }

    private fun setupRegistrationLink() {
        val text = "Еще нет аккаунта? Зарегистрироваться"
        val spannableString = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LogInActivity, MainActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLACK
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        val startIndex = text.indexOf("Зарегистрироваться")
        val endIndex = startIndex + "Зарегистрироваться".length
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.buttonToReg.apply {
            this.text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    private fun setupLoginButton() {
        binding.enterToAccount.setOnClickListener {
            val email = binding.userExistLogin.text.toString().trim()
            val password = binding.userExistPassword.text.toString().trim()

            when {
                email.isEmpty() -> showError("Введите email")
                password.isEmpty() -> showError("Введите пароль")
                !isValidEmail(email) -> showError("Некорректный формат email")
                else -> performLogin(email, password)
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().currentUser?.let { user ->
                        sessionManager.startNewSession(user.uid)
                    }
                    lifecycleScope.launch {
                        // Сброс локального прогресса перед загрузкой новых данных
                        EnglishDatabase.getDatabase(this@LogInActivity).topicDao().resetAllProgress()

                        FirebaseService.loadUserErrors(this@LogInActivity)
                        FirebaseService.loadUserProgress(this@LogInActivity)
                        navigateToMainApp()
                    }
                } else {
                    handleLoginError(task.exception?.message)
                }
            }
    }

    private fun handleLoginError(errorMessage: String?) {
        val error = errorMessage ?: "Неизвестная ошибка"
        val message = when {
            error.contains("invalid-email", true) -> "Некорректный email"
            error.contains("user-not-found", true) -> "Пользователь не найден"
            error.contains("wrong-password", true) -> "Неверный пароль"
            error.contains("network-error", true) -> "Ошибка сети"
            else -> "Ошибка входа: ${error.take(30)}..."
        }
        showError(message)
    }

    private fun navigateToMainApp() {
        val intent = Intent(this, BottomNavigationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        return emailPattern.matches(email)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onBackPressed() {

    }
}