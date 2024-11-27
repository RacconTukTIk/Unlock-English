package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app.MainActivity
import com.example.application.MenuActivity

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.log_in_activity)

        //intent.javaClass
        val buttonToReg: Button = findViewById(R.id.button_to_reg)

        buttonToReg.setOnClickListener {
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val userExistLogin: EditText = findViewById(R.id.user_exist_login)
        val userExistPassword: EditText = findViewById(R.id.user_exist_password)
        val enterToAccButton: Button = findViewById(R.id.enter_to_account)

        enterToAccButton.setOnClickListener {
            val login = userExistLogin.text.toString().trim()
            val password = userExistPassword.text.toString().trim()


            if (login == "" || password == "")
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            else {
                val enter = Intent(this,MenuActivity::class.java)
                startActivity(enter)
            }
        }
    }
}