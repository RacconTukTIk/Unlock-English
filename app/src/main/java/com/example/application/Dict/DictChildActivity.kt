package com.example.application.Dict

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.speech.tts.TextToSpeech
import java.util.Locale
import android.util.Log
import com.example.application.R

class DictChildActivity : AppCompatActivity(), DictAdapterChild.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DictAdapterChild
    private lateinit var textToSpeech: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dict_child)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerViewChild)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val dictionaryEntries = listOf(
            DataDict("Playground", "[ˈpleɪɡraʊnd]", "Игровая площадка"),
            DataDict("Toy", "[tɔɪ]", "Игрушка"),
            DataDict("Friendship", "[ˈfrɛndʃɪp]", "Дружба"),
            DataDict("Imagination", "[ɪˌmædʒɪˈneɪʃən]", "Воображение"),
            DataDict("School", "[skuːl]", "Школа"),
            DataDict("Game", "[ɡeɪm]", "Игра"),
            DataDict("Storybook", "[ˈstɔːribʊk]", "Сказка"),
            DataDict("Crayon", "[ˈkreɪən]", "Мелок"),
            DataDict("Adventure", "[ədˈvɛnʧər]", "Приключение"),
            DataDict("Family", "[ˈfæmɪli]", "Семья"),
            DataDict("Birthday", "[ˈbɜrθdeɪ]", "День рождения"),
            DataDict("Vacation", "[veɪˈkeɪʃən]", "Каникулы"),
            DataDict("Cartoon", "[kɑrˈtun]", "Мультфильм"),
            DataDict("Laughter", "[ˈlæftər]", "Смех"),
            DataDict("Puppet", "[ˈpʌpɪt]", "Кукла"),
            DataDict("Sandbox", "[ˈsændbɑks]", "Песочница"),
            DataDict("Hide and Seek", "[haɪd ənd siːk]", "Прятки"),
            DataDict("Bicycle", "[ˈbaɪsɪkl]", "Велосипед"),
            DataDict("Dress-up", "[drɛs ʌp]", "Костюмированная игра"),
            DataDict("Nursery", "[ˈnɜrseri]", "Детский сад"),
            DataDict("Cradle", "[ˈkreɪdl]", "Колыбель"),
            DataDict("Memory", "[ˈmɛməri]", "Память"),
            DataDict("Cuddle", "[ˈkʌdl]", "Обниматься"),
            DataDict("Sledding", "[ˈslɛdɪŋ]", "Катание на санках"),
            DataDict("Swing", "[swɪŋ]", "Качели"),
            DataDict("Fairy Tale", "[ˈfɛri teɪl]", "Сказка"),
            DataDict("Treasure Hunt", "[ˈtrɛʒər hʌnt]", "Охота за сокровищами")
        )
        adapter = DictAdapterChild(dictionaryEntries,this)
        recyclerView.adapter = adapter

        val button = findViewById<ImageView>(R.id.exitDictChild)
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
        finish()
    }
}
