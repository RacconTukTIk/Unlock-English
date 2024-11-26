package com.example.application

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val ButtonExam: Button = findViewById(R.id.button_exam)
        val ButtonThemes: Button = findViewById(R.id.button_themes)
        val ButtonMistakes: Button = findViewById(R.id.button_mistakes)
        val ButtonFavorite: Button = findViewById(R.id.button_favorite)
        val ButtonMarathon: Button = findViewById(R.id.button_marathon)
    }
}