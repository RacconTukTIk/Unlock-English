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
import com.example.application.databinding.LogInActivityBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.annotation.Nonnull


class LogInActivity : AppCompatActivity() {
    private lateinit var binding:LogInActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.log_in_activity)//mb delete
        binding=LogInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intent.javaClass
        val buttonToReg: Button = findViewById(R.id.button_to_reg)

        buttonToReg.setOnClickListener {
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val userExistLogin: EditText = findViewById(R.id.user_exist_login)
        val userExistPassword: EditText = findViewById(R.id.user_exist_password)
        val enterToAccButton: Button = findViewById(R.id.enter_to_account)

        /*enterToAccButton.setOnClickListener {
            val login = userExistLogin.text.toString().trim()
            val password = userExistPassword.text.toString().trim()*/

        binding.enterToAccount.setOnClickListener{
            //val login = userExistLogin.text.toString().trim()
            //val password = userExistPassword.text.toString().trim()


            if (binding.userExistLogin.text.toString().isEmpty()|| binding.userExistPassword.text.toString().isEmpty())
                Toast.makeText(applicationContext, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
            else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.userExistLogin.text.toString(),binding.userExistPassword.text.toString())
                    .addOnCompleteListener(this){ task->
                        if(task.isSuccessful)
                        {
                            startActivity(Intent(this,MenuActivity::class.java))
                        }

                    }
                //val enter = Intent(this,MenuActivity::class.java)
                //startActivity(enter)
            }
        }
    }

}