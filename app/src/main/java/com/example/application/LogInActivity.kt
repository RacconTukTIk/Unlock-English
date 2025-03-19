package com.example.application

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


        val buttonToReg: TextView = findViewById(R.id.button_to_reg)

        val interBoldTypeface = Typeface.create("sans-serif", Typeface.BOLD)
        // Настройка SpannableString для текста "Еще нет аккаунта?Зарегестрироваться"
        val text = "Еще нет аккаунта? Зарегестрироваться"
        val spannableString = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Обработка нажатия на "Войти"
                val intent = Intent(this@LogInActivity, MainActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLACK // Цвет текста "Зарегестрироваться"
                ds.isUnderlineText = true
            }
        }

        // Применение CustomTypefaceSpan для текста "Зарегестрироваться"
        val typefaceSpan = CustomTypefaceSpan(interBoldTypeface)

        // Установка диапазона для ClickableSpan и CustomTypefaceSpan
        val startIndex = text.indexOf("Зарегестрироваться")
        val endIndex = startIndex + "Зарегестрироваться".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(typefaceSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        buttonToReg.text = spannableString
        buttonToReg.movementMethod = LinkMovementMethod.getInstance()

        buttonToReg.setOnClickListener {
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val userExistLogin: EditText = findViewById(R.id.user_exist_login)
        val userExistPassword: EditText = findViewById<EditText?>(R.id.user_exist_password)
        val enterToAccButton: Button = findViewById(R.id.enter_to_account)

        binding.enterToAccount.setOnClickListener{


            if (binding.userExistLogin.text.toString().isEmpty()|| binding.userExistPassword.text.toString().isEmpty())
                Toast.makeText(applicationContext, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
            else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.userExistLogin.text.toString(),binding.userExistPassword.text.toString())
                    .addOnCompleteListener(this){ task->
                        if(task.isSuccessful)
                        {
                            startActivity(Intent(this,BottomNavigationActivity::class.java))

                        }
                        else
                        {
                            Toast.makeText(applicationContext,"Аккаунта с такими логином или паролем не существует!",Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }
    }

    override fun onBackPressed()
    {
        //Блокирую системную кнопку "Назад"
    }
}