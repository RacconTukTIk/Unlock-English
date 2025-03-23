package com.example.application

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.application.MenuFragment
import com.example.application.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class AccountMainFragment : Fragment() {
    private lateinit var textViewEmail: TextView
    private lateinit var textViewReg: TextView
    private lateinit var buttonLogout: Button
    private lateinit var textViewUsername: TextView
    private lateinit var editNotes: EditText
    private lateinit var currentUser: FirebaseUser
    private val database = FirebaseDatabase.getInstance("https://unlock-english-22c67-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_account, container, false)
        initViews(view)
        setupFirebase()
        setupUI()
        setupListeners()
        return view
    }

    private fun initViews(view: View) {
        textViewEmail = view.findViewById(R.id.textViewEmail)
        textViewReg = view.findViewById(R.id.textViewReg)
        textViewUsername = view.findViewById(R.id.textViewUsername)
        buttonLogout = view.findViewById(R.id.buttonLogout)
        editNotes = view.findViewById(R.id.editTextText2)
    }

    private fun setupFirebase() {
        currentUser = FirebaseAuth.getInstance().currentUser ?: run {
            redirectToLogin()
            return
        }
    }

    private fun setupUI() {
        textViewEmail.text = "Email: ${currentUser.email ?: "не указан"}"
        textViewReg.text = currentUser.metadata?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            "Дата регистрации: ${sdf.format(Date(it.creationTimestamp))}"
        } ?: "Дата регистрации: недоступна"

        database.child("Users").child(currentUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    textViewUsername.text = "Логин: ${snapshot.child("username").getValue(String::class.java) ?: "не указан"}"
                    val notes = snapshot.child("notes").getValue(String::class.java) ?: ""
                    editNotes.setText(notes)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupListeners() {
        editNotes.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveNotes(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            database.keepSynced(false)
            startActivity(Intent(requireContext(), SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            requireActivity().finish()
            Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveNotes(text: String) {
        val notesRef = database.child("Users").child(currentUser.uid).child("notes")

        notesRef.setValue(text)
            .addOnFailureListener { e ->
                Log.e("SAVE_DATA", "Ошибка сохранения: ${e.message}")
                Toast.makeText(requireContext(), "Ошибка сохранения", Toast.LENGTH_SHORT).show()
            }
    }

    private fun redirectToLogin() {
        startActivity(Intent(requireContext(), LogInActivity::class.java))
        requireActivity().finish()
    }

    override fun onPause()
    {
        Log.d("ACCOUNT_DEBUG", "Вызов onPause()")
        super.onPause()
        saveNotes(editNotes.text.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountMainFragment()
    }
}