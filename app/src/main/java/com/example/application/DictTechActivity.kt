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
import com.example.application.DataDict
import com.example.application.DictAdapterTech
import android.speech.tts.TextToSpeech
import java.util.Locale
import android.util.Log

class DictTechActivity : AppCompatActivity(), DictAdapterTech.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DictAdapterTech
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dict_tech)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewTech)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dictionaryEntries = listOf(
            DataDict("Algorithm", "[ˈælɡəˌrɪðəm]", "Алгоритм"),
            DataDict("Application", "[ˌæplɪˈkeɪʃən]", "Приложение"),
            DataDict("Artificial Intelligence", "[ˌɑːrtɪˈfɪʃl ɪnˈtɛlɪdʒəns]", "Искусственный интеллект"),
            DataDict("Bandwidth", "[ˈbændwɪdθ]", "Полоса пропускания"),
            DataDict("Cloud Computing", "[klaʊd kəmˈpjuːtɪŋ]", "Облачные вычисления"),
            DataDict("Cybersecurity", "[ˌsaɪbərsɪˈkjʊrɪti]", "Кибербезопасность"),
            DataDict("Data Mining", "[ˈdeɪtə ˈmaɪnɪŋ]", "Анализ данных"),
            DataDict("Database", "[ˈdeɪtəbeɪs]", "База данных"),
            DataDict("Encryption", "[ɪnˈkrɪpʃən]", "Шифрование"),
            DataDict("Firmware", "[ˈfɜrmwɛr]", "Прошивка"),
            DataDict("Hardware", "[ˈhɑrdwɛr]", "Аппаратное обеспечение"),
            DataDict("Internet of Things", "[ˈɪntərnɛt əv θɪŋz]", "Интернет вещей"),
            DataDict("Machine Learning", "[məˈʃiːn ˈlɜrnɪŋ]", "Обучение с помощью машин"),
            DataDict("Network", "[ˈnɛtwɜrk]", "Сеть"),
            DataDict("Open Source", "[ˈoʊpən sɔrs]", "Открытый исходный код"),
            DataDict("Programming", "[ˈproʊɡræmɪŋ]", "Программирование"),
            DataDict("Robot", "[ˈroʊbɑt]", "Робот"),
            DataDict("Software", "[ˈsɔftwɛr]", "Программное обеспечение"),
            DataDict("User Interface", "[ˈjuːzər ˈɪntərfeɪs]", "Пользовательский интерфейс"),
            DataDict("Virtual Reality", "[ˈvɜrʧuəl riˈælɪti]", "Виртуальная реальность"),
            DataDict("Web Development", "[wɛb dɪˈvɛləpmənt]", "Веб-разработка"),
            DataDict("Wireless", "[ˈwaɪərləs]", "Беспроводной"),
            DataDict("Blockchain", "[ˈblɔkʧeɪn]", "Блокчейн"),
            DataDict("Cryptocurrency", "[ˌkrɪptoʊˈkʌrənsi]", "Криптовалюта"),
            DataDict("Augmented Reality", "[ɔːɡˈmɛntəd riˈælɪti]", "Дополненная реальность"),
            DataDict("Artificial Neural Network", "[ˌɑːrtɪˈfɪʃl ˈnjʊrəl ˈnɛtˌwɜrk]", "Искусственная нейронная сеть"),
            DataDict("Big Data", "[bɪɡ ˈdeɪtə]", "Большие данные")
        )
        adapter = DictAdapterTech(dictionaryEntries, this)
        recyclerView.adapter = adapter

        val button = findViewById<ImageView>(R.id.exitDictTech)
        button.setOnClickListener {
            finish()
        }


        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                    Log.e("TTS", "Язык не поддерживается")
                }
            } else {
                Log.e("TTS", "Инициализация не прошла")
            }
        }
    }

    override fun onSoundButtonClick(word: String) {
        speakOut(word)
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {

        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

    override fun onBackPressed() {

    }
}