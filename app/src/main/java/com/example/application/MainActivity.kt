package com.example.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
//import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.application.BottomNavigationActivity
import com.example.application.LogInActivity
import com.example.application.MenuActivity
import com.example.application.R
import com.example.application.databinding.ActivityMainBinding
import com.example.application.databinding.LogInActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)//mb delete

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //intent = Intent.getIntentOld()
        val userLogin:EditText=findViewById(R.id.user_login)
        val userEmail:EditText=findViewById(R.id.user_email)
        val userPassword:EditText=findViewById(R.id.user_password)
        val button:Button=findViewById(R.id.button_reg)
        val buttonToLogin:Button=findViewById(R.id.button_to_login)

        binding.buttonReg.setOnClickListener {
            val login=userLogin.text.toString().trim()
            val email=userEmail.text.toString().trim()
            val password=userPassword.text.toString().trim()


            if (binding.userLogin.text.toString().isEmpty()||binding.userEmail.text.toString().isEmpty()||binding.userPassword.text.toString().isEmpty())
                Toast.makeText(applicationContext,"Не все поля заполнены",Toast.LENGTH_SHORT).show()
            else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.userEmail.text.toString(),binding.userPassword.text.toString())
                    .addOnCompleteListener(this){task->
                        if(task.isSuccessful)
                        {
                            val userInfo=mutableMapOf<String,String>()
                            userInfo["email"]=binding.userEmail.text.toString()
                            userInfo["username"]=binding.userLogin.text.toString()

                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(userInfo)
                            startActivity(Intent(this,BottomNavigationActivity::class.java))
                        }

                    }



            }
        }
        buttonToLogin.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
        

    }


}