package com.example.dicti

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application.DictChildActivity
import com.example.application.DictMusicActivity
import com.example.application.R

class DictMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dict_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        var buttonChild = findViewById<Button>(R.id.ChildButton)
        buttonChild.setOnClickListener {
            startActivity(Intent(this,DictChildActivity::class.java))
        }
        var buttonTech = findViewById<Button>(R.id.TechButton)
        buttonTech.setOnClickListener {
            startActivity(Intent(this,  DictTechActivity::class.java))
        }
        var buttonMusic = findViewById<Button>(R.id.MusicButton)
        buttonTech.setOnClickListener {
            startActivity(Intent(this,  DictMusicActivity::class.java))
        }
    }
}