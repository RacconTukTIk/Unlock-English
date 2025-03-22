package com.example.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.google.firebase.auth.FirebaseAuth
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
        val button: Button =findViewById(R.id.button_reg)
        val buttonToLogin:TextView =findViewById(R.id.button_to_login)

        val interBoldTypeface = Typeface.create("sans-serif", Typeface.BOLD)
        // Настройка SpannableString для текста "Уже есть аккаунт? Войти"
        val text = "Уже есть аккаунт? Войти"
        val spannableString = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Обработка нажатия на "Войти"
                val intent = Intent(this@MainActivity, LogInActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLACK // Цвет текста "Войти"
                ds.isUnderlineText = true
            }
        }

        // Применение CustomTypefaceSpan для текста "Войти"
        val typefaceSpan = CustomTypefaceSpan(interBoldTypeface)

        // Установка диапазона для ClickableSpan и CustomTypefaceSpan
        val startIndex = text.indexOf("Войти")
        val endIndex = startIndex + "Войти".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(typefaceSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        buttonToLogin.text = spannableString
        buttonToLogin.movementMethod = LinkMovementMethod.getInstance()

        binding.buttonReg.setOnClickListener {
            val login=userLogin.text.toString().trim()
            val email=userEmail.text.toString().trim()
            val password=userPassword.text.toString().trim()
            fun isValidEmail(email: String): Boolean {
                val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                return emailRegex.matches(email)
            }


            if (binding.userLogin.text.toString().isEmpty()||binding.userEmail.text.toString().isEmpty()||binding.userPassword.text.toString().isEmpty()||(!isValidEmail(binding.userEmail.text.toString())))
                Toast.makeText(applicationContext,"Не все поля корректно заполнены",Toast.LENGTH_SHORT).show()
            else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.userEmail.text.toString(),binding.userPassword.text.toString())
                    .addOnCompleteListener(this){task->
                        if(task.isSuccessful)
                        {
                            /*val sharedPreferences=getSharedPreferences("user_data",Context.MODE_PRIVATE)
                            val editor=sharedPreferences.edit()
                            editor.putString("username", login)
                            editor.apply()*/
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

    }
    override fun onBackPressed()
    {
        finish()
    }
}