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
import com.example.application.BottomNavigationActivity
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
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dictionaryEntries = listOf(
            DataDictTech("Algorithm", "[ˈælɡəˌrɪðəm]", "Алгоритм"),
            DataDictTech("Application", "[ˌæplɪˈkeɪʃən]", "Приложение"),
            DataDictTech("Artificial Intelligence", "[ˌɑːrtɪˈfɪʃl ɪnˈtɛlɪdʒəns]", "Искусственный интеллект"),
            DataDictTech("Bandwidth", "[ˈbændwɪdθ]", "Полоса пропускания"),
            DataDictTech("Cloud Computing", "[klaʊd kəmˈpjuːtɪŋ]", "Облачные вычисления"),
            DataDictTech("Cybersecurity", "[ˌsaɪbərsɪˈkjʊrɪti]", "Кибербезопасность"),
            DataDictTech("Data Mining", "[ˈdeɪtə ˈmaɪnɪŋ]", "Анализ данных"),
            DataDictTech("Database", "[ˈdeɪtəbeɪs]", "База данных"),
            DataDictTech("Encryption", "[ɪnˈkrɪpʃən]", "Шифрование"),
            DataDictTech("Firmware", "[ˈfɜrmwɛr]", "Прошивка"),
            DataDictTech("Hardware", "[ˈhɑrdwɛr]", "Аппаратное обеспечение"),
            DataDictTech("Internet of Things", "[ˈɪntərnɛt əv θɪŋz]", "Интернет вещей"),
            DataDictTech("Machine Learning", "[məˈʃiːn ˈlɜrnɪŋ]", "Обучение с помощью машин"),
            DataDictTech("Network", "[ˈnɛtwɜrk]", "Сеть"),
            DataDictTech("Open Source", "[ˈoʊpən sɔrs]", "Открытый исходный код"),
            DataDictTech("Programming", "[ˈproʊɡræmɪŋ]", "Программирование"),
            DataDictTech("Robot", "[ˈroʊbɑt]", "Робот"),
            DataDictTech("Software", "[ˈsɔftwɛr]", "Программное обеспечение"),
            DataDictTech("User Interface", "[ˈjuːzər ˈɪntərfeɪs]", "Пользовательский интерфейс"),
            DataDictTech("Virtual Reality", "[ˈvɜrʧuəl riˈælɪti]", "Виртуальная реальность"),
            DataDictTech("Web Development", "[wɛb dɪˈvɛləpmənt]", "Веб-разработка"),
            DataDictTech("Wireless", "[ˈwaɪərləs]", "Беспроводной"),
            DataDictTech("Blockchain", "[ˈblɔkʧeɪn]", "Блокчейн"),
            DataDictTech("Cryptocurrency", "[ˌkrɪptoʊˈkʌrənsi]", "Криптовалюта"),
            DataDictTech("Augmented Reality", "[ɔːɡˈmɛntəd riˈælɪti]", "Дополненная реальность")
        )
        adapter = DictAdapterTech(dictionaryEntries)
        recyclerView.adapter = adapter

        var button = findViewById<ImageView>(R.id.exitDictTech)
        button.setOnClickListener {
            finish()
        }

    }
}