package com.example.dicti

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Intents.Insert
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.DataDictTech
import com.example.application.DictAdapterTech

class DictTechActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DictAdapterTech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dict_tech)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Пример данных словаря
        val dictionaryEntries = listOf(
            DataDictTech("Word 1", "[wɜːrd 1]", "Перевод 1"),
            DataDictTech("Word 2", "[wɜːrd 2]", "Перевод 2"),
            DataDictTech("Word 3", "[wɜːrd 3]", "Перевод 3")
            // Добавьте больше слов и определений по мере необходимости
        )

        adapter = DictAdapterTech(dictionaryEntries)
        recyclerView.adapter = adapter

        var button = findViewById<ImageView>(R.id.exitDictTech)
        button.setOnClickListener {
            startActivity(Intent(this,DictMainActivity::class.java))
        }

    }
}