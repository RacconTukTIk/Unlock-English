package com.example.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import  com.example.application.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val userLogin:EditText=findViewById(R.id.user_login)
        val userEmail:EditText=findViewById(R.id.user_email)
        val userPassword:EditText=findViewById(R.id.user_password)
        val button:Button=findViewById(R.id.button_reg)

        button.setOnClickListener {
            val login=userLogin.text.toString().trim()
            val email=userEmail.text.toString().trim()
            val password=userPassword.text.toString().trim()


            if (login==""||email==""||password=="")
                Toast.makeText(this,"Не все поля заполнены",Toast.LENGTH_LONG).show()
            else{
            }
        }
    }
}