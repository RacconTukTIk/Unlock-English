package com.example.application

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RememberMe : Application() {
    override fun onCreate() {
        super.onCreate()

        // Инициализация Firebase
        FirebaseApp.initializeApp(this)
        Firebase.database.setPersistenceEnabled(true)
        FirebaseDatabase.getInstance("https://unlock-english-22c67-default-rtdb.europe-west1.firebasedatabase.app/").setPersistenceEnabled(true)

    }
}