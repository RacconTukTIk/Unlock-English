package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.application.Error.MistakesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val currentUser: FirebaseUser?= FirebaseAuth.getInstance().currentUser
        if(currentUser==null)
        {
            startActivity(Intent(this,LogInActivity::class.java))
        }

        val ButtonExam: Button = findViewById(R.id.button_exam)
        val ButtonThemes: Button = findViewById(R.id.button_themes)
        val ButtonMistakes: Button = findViewById(R.id.button_mistakes)
        val ButtonFavorite: Button = findViewById(R.id.button_test)
        val ButtonMarathon: Button = findViewById(R.id.button_marathon)

        ButtonMistakes.setOnClickListener {
            val intent = Intent(this, MistakesActivity::class.java)
            startActivity(intent)
        }
    }
}