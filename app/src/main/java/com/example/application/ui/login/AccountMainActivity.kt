package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application.LogInActivity
import com.example.application.R
import com.example.dicti.DictTechActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import android.util.Log



class AccountMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account)

        val textViewEmail:TextView=findViewById(R.id.textViewEmail)
        val textViewReg:TextView=findViewById(R.id.textViewReg)
        val currentUser:FirebaseUser?=FirebaseAuth.getInstance().currentUser


        if (currentUser != null) {
            Log.d("AccountDebug", "User UID: ${currentUser.uid}")
            Log.d("AccountDebug", "Email: ${currentUser.email}")
            Log.d("AccountDebug", "Creation timestamp: ${currentUser.metadata?.creationTimestamp}")
            print("ДА")
            // Email
            textViewEmail.text = "Email: ${currentUser.email ?: "не указан"}"

            // Дата регистрации
            currentUser.metadata?.let { metadata ->
                val creationDate = Date(metadata.creationTimestamp)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                textViewReg.text = "Дата регистрации: ${sdf.format(creationDate)}"
            } ?: run {
                textViewReg.text = "Дата регистрации: недоступна"
            }

        } else {
            Log.e("AccountDebug", "User is NULL")
            // Перенаправление на экран входа
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }
}