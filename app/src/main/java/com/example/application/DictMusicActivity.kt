package com.example.application

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
import android.speech.tts.TextToSpeech
import java.util.Locale
import android.util.Log
import com.example.application.DictAdapterMusic

class DictMusicActivity : AppCompatActivity(), DictAdapterMusic.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DictAdapterMusic
    private lateinit var textToSpeech: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dict_music)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerViewMusic)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val dictionaryEntries = listOf(
            DataDict("Melody", "[ˈmelədi]", "Мелодия"),
            DataDict("Harmony", "[ˈhɑːrməni]", "Гармония"),
            DataDict("Rhythm", "[ˈrɪðəm]", "Ритм"),
            DataDict("Tempo", "[ˈtɛmpoʊ]", "Темп"),
            DataDict("Chord", "[kɔrd]", "Аккорд"),
            DataDict("Scale", "[skeɪl]", "Масштаб"),
            DataDict("Note", "[noʊt]", "Нота"),
            DataDict("Composer", "[kəmˈpoʊzər]", "Композитор"),
            DataDict("Concert", "[ˈkɒn.sɜrt]", "Концерт"),
            DataDict("Instrument", "[ˈɪnstrəmənt]", "Инструмент"),
            DataDict("Soprano", "[səˈprænoʊ]", "Сопрано"),
            DataDict("Bass", "[beɪs]", "Бас"),
            DataDict("Symphony", "[ˈsɪmfəni]", "Симфония"),
            DataDict("Orchestra", "[ˈɔrkɪstrə]", "Оркестр"),
            DataDict("Solo", "[ˈsoʊloʊ]", "Соло"),
            DataDict("Duet", "[djuˈɛt]", "Дуэт"),
            DataDict("Genre", "[ˈʒɑːnrə]", "Жанр"),
            DataDict("Lyrics", "[ˈlɪrɪks]", "Текст песни"),
            DataDict("Performance", "[pərˈfɔrməns]", "Выступление"),
            DataDict("Rehearsal", "[rɪˈhɜrsəl]", "Репетиция"),
            DataDict("Composition", "[ˌkɒmpəˈzɪʃən]", "Композиция"),
            DataDict("Chorus", "[ˈkɔːrəs]", "Хор"),
            DataDict("Accompaniment", "[əˈkʌmpənɪmənt]", "Акомпанемент"),
            DataDict("Melodrama", "[mɛləˈdrɑːmə]", "Мелодрама"),
            DataDict("Crescendo", "[krəˈʃɛndoʊ]", "Кресчендо"),
            DataDict("Forte", "[fɔrˈteɪ]", "Форте"),
            DataDict("Piano", "[piˈɑːnoʊ]", "Пиано")
        )
        adapter = DictAdapterMusic(dictionaryEntries,this)
        recyclerView.adapter = adapter

        val button = findViewById<ImageView>(R.id.exitDictMusic)
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
                Log.e("TTS", "Initialization failed")
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
