package com.example.application

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.application.MenuFragment
import com.example.application.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class AccountMainFragment : Fragment() {
    private lateinit var textViewEmail:TextView
    private  lateinit var textViewReg:TextView
    private lateinit var buttonLogout: Button
    private lateinit var textViewUsername:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating layout for this fragment
        val view = inflater.inflate(R.layout.activity_account, container, false)
        textViewEmail = view.findViewById(R.id.textViewEmail)
        textViewReg = view.findViewById(R.id.textViewReg)
        textViewUsername = view.findViewById(R.id.textViewUsername)
        buttonLogout = view.findViewById(R.id.buttonLogout)

        // Получение текущего пользователя
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // Обновление email
            textViewEmail.text = "Email: ${currentUser.email ?: "не указан"}"

            // Обновление даты регистрации
            currentUser.metadata?.let { metadata ->
                val creationDate = Date(metadata.creationTimestamp)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                textViewReg.text = "Дата регистрации: ${sdf.format(creationDate)}"
            } ?: run {
                textViewReg.text = "Дата регистрации: недоступна"
            }

            /*val sharedPreferences=requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val username=sharedPreferences.getString("username","не указан")
            textViewUsername.text="Логин: $username"*/
            val database=FirebaseDatabase.getInstance().reference
            currentUser.uid?.let{uid->
                database.child("Users").child(uid).get().addOnSuccessListener { snapshot->
                    val username=snapshot.child("username").getValue(String::class.java)
                    textViewUsername.text="Логин: $username"
                }.addOnFailureListener{ e->
                    Log.e("AccountDebug", "Ошибка получения логина: ${e.message}")
                    textViewUsername.text = "Логин: не указан"
                }
            }

        } else {
            // Перенаправление на экран входа
            startActivity(Intent(requireContext(), LogInActivity::class.java))
            requireActivity().finish()
        }

        buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Выход из аккаунта
            Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LogInActivity::class.java))
            requireActivity().finish()
        }

        return view
    }
    companion object {
        @JvmStatic
        fun newInstance() = AccountMainFragment()
    }
}