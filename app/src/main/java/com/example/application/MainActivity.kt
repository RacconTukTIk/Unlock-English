package com.example.app

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.application.BottomNavigationActivity
import com.example.application.CustomTypefaceSpan
import com.example.application.LogInActivity
import com.example.application.R
import com.example.application.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        setupLoginLink()
        setupRegistrationButton()
    }
    private fun setupLoginLink() {
        val text = "Уже есть аккаунт? Войти"
        val spannableString = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@MainActivity, LogInActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLACK
                ds.isUnderlineText = true
            }
        }

        val startIndex = text.indexOf("Войти")
        val endIndex = startIndex + "Войти".length
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.buttonToLogin.apply {
            this.text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT // Убираем подсветку при нажатии
        }
    }

    private fun setupRegistrationButton() {
        val email = binding.userEmail.text.toString().trim()
        val password = binding.userPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val username = binding.userLogin.text.toString().trim()

        when {
            username.isEmpty() -> showError("Введите имя пользователя")
            email.isEmpty() || !isValidEmail(email) -> showError("Некорректный email")
            password.isEmpty() -> showError("Введите пароль")
            password != confirmPassword -> showError("Пароли не совпадают")
            else -> registerUser(username, email, password)
        }
    }


    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveUserData(username, email)
                    navigateToMainScreen()
                } else {
                    showError("Ошибка регистрации: ${task.exception?.message}")
                }
            }
    }
    private fun saveUserData(username: String, email: String) {
        val userInfo = hashMapOf(
            "username" to username,
            "email" to email
        )

        FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(auth.currentUser?.uid ?: "")
            .setValue(userInfo)
    }
    private fun navigateToMainScreen() {
        val intent = Intent(this, BottomNavigationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        return emailRegex.matches(email)
    }
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        finish()
    }
}
