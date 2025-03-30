
package com.example.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.application.databinding.ActivityLearningBinding
import com.example.dicti.DictTechActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LearningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearningBinding
    private lateinit var allWords: MutableList<DataDict>
    private lateinit var currentWord: DataDict

    companion object {
        val learnedWords = mutableListOf<DataDict>()
        val wordsToRepeat = mutableListOf<DataDict>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация списка слов
        allWords = mutableListOf<DataDict>().apply {
            addAll(DictChildActivity().getDictionaryEntries())
            addAll(DictMusicActivity().getDictionaryEntries())
            addAll(DictTechActivity().getDictionaryEntries())
            shuffle()
        }

        showNextWord()

        binding.buttonCheck.setOnClickListener {
            checkAnswer()
        }

        binding.buttonNext.setOnClickListener {
            showNextWord()
        }
        lifecycleScope.launch {
            lifecycleScope.launch {
                learnedWords.addAll(FirebaseService.loadLearnedWords())
                wordsToRepeat.addAll(FirebaseService.loadWordsToRepeat())

                // Инициализация списка слов
                allWords = mutableListOf<DataDict>().apply {
                    addAll(DictChildActivity().getDictionaryEntries())
                    addAll(DictMusicActivity().getDictionaryEntries())
                    addAll(DictTechActivity().getDictionaryEntries())
                    // Исключаем уже изученные слова
                    removeAll { word ->
                        learnedWords.any { it.word == word.word }
                    }
                    shuffle()
                }

                showNextWord()
            }
        }

    }


    private fun showNextWord() {
        if (allWords.isEmpty()) {
            binding.textViewRussianWord.text = "Слова закончились!"
            binding.editTextAnswer.isEnabled = false
            binding.buttonCheck.isEnabled = false
            return
        }

        currentWord = allWords.removeFirst()
        binding.textViewRussianWord.text = currentWord.translation
        binding.editTextAnswer.text?.clear()
        binding.textViewResult.text = ""
    }

    private fun checkAnswer() {
        val userAnswer = binding.editTextAnswer.text.toString().trim()
        val isCorrect = userAnswer.equals(currentWord.word, ignoreCase = true)

        binding.textViewResult.text = if (isCorrect) {
            addToLearned(currentWord)
            "✅ Верно!"
        } else {
            addToRepeat(currentWord)
            "❌ Неверно! Правильно: ${currentWord.word}"
        }
    }

    private fun addToLearned(word: DataDict) {
        if (!learnedWords.any { it.word == word.word }) {
            learnedWords.add(word)
            lifecycleScope.launch {
                FirebaseService.saveLearnedWord(word)
            }
        }
    }

    private fun addToRepeat(word: DataDict) {
        if (!wordsToRepeat.any { it.word == word.word }) {
            wordsToRepeat.add(word)
            lifecycleScope.launch {
                FirebaseService.saveWordToRepeat(word)
            }
        }
    }
}

    // Добавляем методы для получения слов в каждом Activity
    fun DictChildActivity.getDictionaryEntries() = listOf(
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

    fun DictMusicActivity.getDictionaryEntries() = listOf(
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

    fun DictTechActivity.getDictionaryEntries() = listOf(
        DataDict("Algorithm", "[ˈælɡəˌrɪðəm]", "Алгоритм"),
        DataDict("Application", "[ˌæplɪˈkeɪʃən]", "Приложение"),
        DataDict(
            "Artificial Intelligence",
            "[ˌɑːrtɪˈfɪʃl ɪnˈtɛlɪdʒəns]",
            "Искусственный интеллект"
        ),
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
        DataDict(
            "Artificial Neural Network",
            "[ˌɑːrtɪˈfɪʃl ˈnjʊrəl ˈnɛtˌwɜrk]",
            "Искусственная нейронная сеть"
        ),
        DataDict("Big Data", "[bɪɡ ˈdeɪtə]", "Большие данные")
    )