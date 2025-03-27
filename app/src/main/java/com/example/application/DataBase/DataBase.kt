package com.example.application.DataBase

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context


import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Database(entities = [Topic::class, Test::class], version = 2, exportSchema = false)
abstract class EnglishDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao
    abstract fun testDao(): TestDao

    companion object {
        @Volatile
        private var INSTANCE: EnglishDatabase? = null

        fun getDatabase(context: Context): EnglishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EnglishDatabase::class.java,
                    "english_database"
                )
                    .addCallback(DatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            fillInitialData()
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            checkAndFillData()
        }

        private fun fillInitialData() {
            CoroutineScope(Dispatchers.IO).launch {
                val db = getDatabase(context)
                if (db.topicDao().getAllTopics().first().isEmpty()) {
                    insertInitialData(db)
                }
            }
        }

        private fun checkAndFillData() {
            CoroutineScope(Dispatchers.IO).launch {
                val db = getDatabase(context)
                if (db.topicDao().getAllTopics().first().isEmpty()) {
                    insertInitialData(db)
                }
            }
        }

        private suspend fun insertInitialData(database: EnglishDatabase) {
            val topicDao = database.topicDao()
            val testDao = database.testDao()
            val topics = listOf(
                Topic(id = 1, title = "Категория Simple", description = "Это группа простых времен. К ней относятся действия, которые совершаются / совершались / будут совершаться часто и / или регулярно, а также однократные поступки и события."),
                Topic(id = 2, title = "Present Simple", description = "Настоящее время в этой категории образуется от инфинитива глагола без частицы to. При этом если подлежащее стоит в 3 лице (he/she/it), то к сказуемому прибавляется окончание –s. Вопросительная и отрицательная формы строятся при помощи вспомогательного глагола do (в 3 лице — does).\n" +
                        "\n" +
                        "Формула : I/We/You/They + глагол\n" +
                        "\n" +
                        "Примеры: I think. — Я думаю. We smile. — Мы улыбаемся. You know. — Вы (ты) знаете (-ешь). Boys jump. — Мальчики прыгают.\n" +
                        "\n" +
                        "Формула : He/She/It + глагол + -s (-es)\n" +
                        "\n" +
                        "Примеры: He goes. — Он ходит. She speaks. — Она разговаривает. A boy jumps. — Мальчик прыгает."),
                Topic(id = 3, title = "Past Simple", description = "В прошедшем используется вторая форма глагола с окончанием -ed (если глагол правильный), для вопросов и отрицаний добавляют did, а глагол оставляют в первой форме.\n" +
                        "\n" +
                        "Формула : I/He/She/It/We/You/They + 2-я форма глагола\n" +
                        "\n" +
                        "Примеры: I played. — Я играл. He told. — Он говорил. She stopped. — Она остановилась. It worked. — Оно работало. We discussed. — Мы обсудили. You did. — Вы сделали. They forgot. — Они забыли."),
                Topic(id = 4, title = "Future Simple", description = "Для будущего времени также характерен глагол в инфинитиве, только перед ним добавляют вспомогательное слово will. С его же помощью образуют вопросы и отрицания.\n" +
                        "\n" +
                        "Формула : I/He/She/It/We/You/They + will + глагол\n" +
                        "\n" +
                        "Примеры: I will come. — Я приду. He will win. — Он выиграет. She will understand. — Она поймет. It will break. — Оно сломается. We will find. — Мы найдем. You will see. — Вы увидите. They will agree. — Они согласятся."),
                Topic(id = 5,
                    title = "Категория Continuous",
                    description = "Времена глагола в английском языке, представляющие группу Continuous, относятся к продолженным. Времена этой группы отмечают длительность происходящего процесса, совершение действий в конкретный момент времени. Акцент выставлен не на факт выполнения чего-то, а на время, в которое эти действия выполняются/выполнялись/будут выполняться."
                ),
                Topic(id = 6,
                    title = "Present Continuous",
                    description = "Для продолженного настоящего времени в английском языке характерно использование конструкции to be + глагол с окончанием -ing. Создание вопросительных и отрицательных предложений не требует вспомогательного do, поскольку для этого используется сказуемое to be.\n" +
                            "\n" +
                            "Формула : I am + глагол-ing\n" +
                            "\n" +
                            "Пример: I am singing. — Я пою.\n" +
                            "\n" +
                            "Формула : He/She/It is + глагол-ing\n" +
                            "\n" +
                            "Примеры: He is smiling. — Он улыбается. She is lying. — Она лежит. It is shining. — Оно светит.\n" +
                            "\n" +
                            "Формула : We/You/They are + глагол-ing\n" +
                            "\n" +
                            "Примеры: We are listening. — Мы слушаем. You are dancing. — Вы танцуете. They are swimming. — Они плавают."
                ),
                Topic(id = 7, title = "Past Continuous", description = "По той же схеме строится и прошедшее время, но to be используется в прошедшей форме — was/were.\n" +
                        "\n" +
                        "Формула : I/He/She/It + was + глагол-ing\n" +
                        "\n" +
                        "Примеры: I was singing. — Я пел. He was walking. — Он гулял. She was writing. — Она писала. It was falling. — Оно падало.\n" +
                        "\n" +
                        "Формула : We/You/They + were + глагол-ing\n" +
                        "\n" +
                        "Примеры: We were reading. — Мы читали. You were talking. — Вы разговаривали. They were running. — Они бежали."),
                Topic(id = 8,
                    title = "Future Continuous",
                    description = "Для будущего времени потребуется глагол will с частицей be.\n" +
                            "\n" +
                            "Формула : I/He/She/It/We/You/They + will be + глагол-ing\n" +
                            "\n" +
                            "Примеры: I will be speaking. — Я буду говорить. He will be building. — Он будет строить. She will be eating. — Она будет есть. It will be operating. — Оно будет работать. We will be drawing. — Мы будем рисовать. You will be thinking. — Вы будете думать. They will be walking. — Они будут гулять."
                ),
                Topic(id = 9,
                    title = "Категория Perfect",
                    description = "Времена глагола в английском языке, относящиеся к группе Perfect, выражают завершенные действия, результат которых непосредственно связан с данным моментом времени. В таких выражениях не может быть использовано простое прошедшее время, поскольку контекст предполагает подчеркивание временного промежутка. Данное действие либо завершили только что, либо к наступлению определенного момента."
                ),
                Topic(id = 10,
                    title = "Present Perfect",
                    description = "Конструкция настоящего времени содержит глагол have/has и 3-ю форму глагола.\n" +
                            "\n" +
                            "Формула : I/We/You/They + have + 3-я форма глагола\n" +
                            "\n" +
                            "Примеры: I have started. — Я начал. We have gone. — Мы ушли. You have finished. — Вы закончили. They have come. — Они пришли.\n" +
                            "\n" +
                            "Формула : He/She/It + has + 3-я форма глагола\n" +
                            "\n" +
                            "Примеры: He has decided. — Он решил. She has done. — Она сделала. It has turned off. — Оно выключилось."
                ),
                Topic(id = 11,
                    title = "Past Perfect",
                    description = "Та же схема характерна и для прошедшего времени, но have используется в прошедшей форме - had.\n" +
                            "\n" +
                            "Формула : I/He/She/It/We/You/They + had + 3-я форма глагола\n" +
                            "\n" +
                            "Примеры: I had done. — Я сделал. He had seen. — Он увидел. She had found. — Она нашла. It had fallen. — Оно упало. We had learned. — Мы выучили. You had decided. — Вы решили. They had gone. — Они ушли."
                ),
                Topic(id = 12,
                    title = "Future Perfect",
                    description = "Конструкция будущего времени дополняется глаголом will.\n" +
                            "\n" +
                            "Формула : I/He/She/It/We/You/They + will have + 3-я форма глагола\n" +
                            "\n" +
                            "Примеры: I will have finished. — Я закончу. He will have decided. — Он решит. She will have painted. — Она нарисует. It will have produced. — Оно произведет. We will have arrived. — Мы прибудем. You will have confirmed. — Вы подтвердите. They will have received. — Они получат."
                ),
                Topic(id = 13,
                    title = "Категория Perfect Continuous",
                    description = "Все времена глаголов в английском языке, входящие в группу Perfect Continuous, относятся к совершенно-продолженным. Основное их назначение - подчеркнуть, что действие/событие все еще будет продолжаться, когда наступит указанный момент времени, или уже начнется/совершится еще одно действие. Иными словами, действие находится в процессе уже какое-то время, и все еще будет продолжаться."
                ),
                Topic(id = 14,
                    title = "Present Perfect Continuous",
                    description = "Конструкция настоящего времени включает глагол have been и глагол с окончанием -ing.\n" +
                            "\n" +
                            "Формула : I/We/You/They + have been + глагол-ing\n" +
                            "\n" +
                            "Примеры: I have been reading. — Я читаю. We have been waiting. — Мы ожидаем. You have been playing. — Вы играете. They have been working. — Они работают.\n" +
                            "\n" +
                            "Формула : He/She/It + has been + глагол-ing\n" +
                            "\n" +
                            "Примеры: He has been running. — Он бегает. She has been laughing. — Она смеется. It has been working. — Оно работает."
                ),
                Topic(id = 15,
                    title = "Past Perfect Continuous",
                    description = "При формировании предложения в прошедшем времени have been превращается в had been.\n" +
                            "\n" +
                            "Формула : I/He/She/It/We/You/They + had been + глагол-ing\n" +
                            "\n" +
                            "Примеры: I had been waiting. — Я ждал. He had been singing. — Он пел. She had been walking. — Она гуляла. It had been ringing. — Оно звенело. We had been learning. — Мы учили. You had been building. — Вы строили. They had been swimming. — Они плавали."
                ),
                Topic(id = 16,
                    title = "Future Perfect Continuous",
                    description = "Для формулы будущего времени используется will.\n" +
                            "\n" +
                            "Формула : I/He/She/It/We/You/They + will have been + глагол-ing\n" +
                            "\n" +
                            "Примеры: I will have been playing. — Я буду играть. He will have been reading. — Он будет читать. She will have been solving. — Она будет решать. It will have been showing. — Оно будет показывать. We will have been waiting. — Мы будем ждать. You will have been translating. — Вы будете переводить. They will have been calculating. — Они будут считать."
                ),
                Topic(id = 17,
                    title = "Future in the Past",
                    description = "Существует еще одна категория времен английского глагола - будущее в прошедшем. Предложения с данными конструкциями отсылают к будущему с точки зрения некоего момента в прошлом.\n" +
                            "\n" +
                            "Пример: We wondered if the train would arrive on time. — Мы подумали, прибудет ли поезд вовремя.\n" +
                            "\n" +
                            "Времена Future in the Past употребляются в придаточных предложениях после слов said (that), told (that), thought (that) и т. п.\n" +
                            "\n" +
                            "Конструкция Future in the Past также может относиться к настоящему, продолженному, совершенному или продолженному совершенному времени.\n" +
                            "\n" +
                            "\n" +
                            "Simple Future in the Past обозначает действия в будущем, воспринимаемом из прошлого.\n" +
                            "\n" +
                            "Пример: He said he would go to the dentist. — Он сказал, что пойдет к зубному.\n" +
                            "\n" +
                            "\n" +
                            "Continuous Future in the Past обозначает процесс, который будет длиться в определенный момент будущего, воспринимаемого из прошлого.\n" +
                            "\n" +
                            "Пример: He was imagining how he would be sipping a cocktail on his vacation. — Он представлял себе, как будет потягивать коктейль в отпуске.\n" +
                            "\n" +
                            "\n" +
                            "Perfect Future in the Past выражает действие, которое будет закончено к определенному моменту будущего, воспринимаемого из прошлого\n" +
                            "\n" +
                            "Пример: We hoped we would have finished our homework by midday. — Мы надеялись, что сделаем домашнее задание к полудню."
                ),
                Topic(id = 18,
                    title = "Perfect Continuous Future in the Past",
                    description = "Perfect Continuous Future in the Past — чрезвычайно редкое даже в письменной речи время, которое обозначает процесс, который начнется и будет продолжаться до определенного момента в будущем, воспринимаемом из прошлого.\n" +
                            "\n" +
                            "Пример: He told us he would have been working at the plant for 30 years next December. — Он рассказал нам, что в декабре исполнится 30 лет, как он работает на этом заводе."
                )
            )
            topics.forEach { topic ->
                topicDao.insert(topic)
            }

            // Тесты для Present Simple (topicId = 2)
            val presentSimpleTests = listOf(
                Test(
                    topicId = 2,
                    question = "Выберите правильный вариант:",
                    option1 = "She writes a letter every day",
                    option2 = "She write a letter every day",
                    option3 = "She is writing a letter every day",
                    option4 = "She has written a letter every day",
                    correctAnswer = "She writes a letter every day"
                ),
                Test(
                    topicId = 2,
                    question = "Как образуется отрицание для he?",
                    option1 = "He doesn't plays",
                    option2 = "He don't play",
                    option3 = "He doesn't play",
                    option4 = "He not play",
                    correctAnswer = "He doesn't play"
                ),
                Test(
                    topicId = 2,
                    question = "Выберите правильный вопрос:",
                    option1 = "Does she likes coffee?",
                    option2 = "Do she like coffee?",
                    option3 = "Does she like coffee?",
                    option4 = "Is she like coffee?",
                    correctAnswer = "Does she like coffee?"
                ),
                Test(
                    topicId = 2,
                    question = "Какое время описывает регулярные действия?",
                    option1 = "Present Continuous",
                    option2 = "Present Simple",
                    option3 = "Past Simple",
                    option4 = "Future Perfect",
                    correctAnswer = "Present Simple"
                ),
                Test(
                    topicId = 2,
                    question = "Выберите правильный перевод: 'Они работают здесь'",
                    option1 = "They are working here",
                    option2 = "They work here",
                    option3 = "They worked here",
                    option4 = "They has worked here",
                    correctAnswer = "They work here"
                )
            )
            testDao.insertAll(presentSimpleTests)

            // Тесты для Past Simple (topicId = 3)
            val pastSimpleTests = listOf(
                Test(
                    topicId = 3,
                    question = "Форма глагола 'go' в Past Simple:",
                    option1 = "goed",
                    option2 = "went",
                    option3 = "gone",
                    option4 = "goes",
                    correctAnswer = "went"
                ),
                Test(
                    topicId = 3,
                    question = "Выберите правильное предложение:",
                    option1 = "I did went to school",
                    option2 = "I went to school",
                    option3 = "I was go to school",
                    option4 = "I were go to school",
                    correctAnswer = "I went to school"
                ),
                Test(
                    topicId = 3,
                    question = "Отрицание для 'They saw':",
                    option1 = "They didn't saw",
                    option2 = "They didn't see",
                    option3 = "They don't see",
                    option4 = "They not saw",
                    correctAnswer = "They didn't see"
                ),
                Test(
                    topicId = 3,
                    question = "Какое время описывает завершенные действия в прошлом?",
                    option1 = "Present Perfect",
                    option2 = "Past Continuous",
                    option3 = "Past Simple",
                    option4 = "Future in the Past",
                    correctAnswer = "Past Simple"
                ),
                Test(
                    topicId = 3,
                    question = "Выберите правильный перевод: 'Я купил машину'",
                    option1 = "I buy a car",
                    option2 = "I bought a car",
                    option3 = "I have bought a car",
                    option4 = "I buys a car",
                    correctAnswer = "I bought a car"
                )
            )
            testDao.insertAll(pastSimpleTests)

            // Тесты для Future Simple (topicId = 4)
            val futureSimpleTests = listOf(
                Test(
                    topicId = 4,
                    question = "Выберите правильный вариант:",
                    option1 = "I will going",
                    option2 = "I will go",
                    option3 = "I will went",
                    option4 = "I going to go",
                    correctAnswer = "I will go"
                ),
                Test(
                    topicId = 4,
                    question = "Отрицательная форма для 'You will see':",
                    option1 = "You won't see",
                    option2 = "You don't see",
                    option3 = "You didn't see",
                    option4 = "You not will see",
                    correctAnswer = "You won't see"
                ),
                Test(
                    topicId = 4,
                    question = "Выберите правильный вопрос:",
                    option1 = "Will she comes?",
                    option2 = "Will she come?",
                    option3 = "Does she will come?",
                    option4 = "Is she will come?",
                    correctAnswer = "Will she come?"
                ),
                Test(
                    topicId = 4,
                    question = "Какое время используется для спонтанных решений?",
                    option1 = "Present Simple",
                    option2 = "Future Simple",
                    option3 = "Past Simple",
                    option4 = "Present Continuous",
                    correctAnswer = "Future Simple"
                ),
                Test(
                    topicId = 4,
                    question = "Переведите: 'Они не согласятся'",
                    option1 = "They won't agree",
                    option2 = "They don't agree",
                    option3 = "They didn't agree",
                    option4 = "They not agree",
                    correctAnswer = "They won't agree"
                )
            )
            testDao.insertAll(futureSimpleTests)

            // Тесты для Present Continuous (topicId = 6)
            val presentContinuousTests = listOf(
                Test(
                    topicId = 6,
                    question = "Выберите правильный вариант:",
                    option1 = "I am read a book",
                    option2 = "I am reading a book",
                    option3 = "I reading a book",
                    option4 = "I reads a book",
                    correctAnswer = "I am reading a book"
                ),
                Test(
                    topicId = 6,
                    question = "Отрицание для 'He is sleeping':",
                    option1 = "He isn't sleeping",
                    option2 = "He don't sleeping",
                    option3 = "He doesn't sleeping",
                    option4 = "He not sleeping",
                    correctAnswer = "He isn't sleeping"
                ),
                Test(
                    topicId = 6,
                    question = "Выберите правильный перевод: 'Мы сейчас работаем'",
                    option1 = "We work now",
                    option2 = "We are working now",
                    option3 = "We worked now",
                    option4 = "We works now",
                    correctAnswer = "We are working now"
                ),
                Test(
                    topicId = 6,
                    question = "Какое время описывает действия в момент речи?",
                    option1 = "Present Simple",
                    option2 = "Present Continuous",
                    option3 = "Past Continuous",
                    option4 = "Future Continuous",
                    correctAnswer = "Present Continuous"
                ),
                Test(
                    topicId = 6,
                    question = "Выберите неправильный случай использования:",
                    option1 = "Действие сейчас",
                    option2 = "Постоянные привычки",
                    option3 = "Временные ситуации",
                    option4 = "Планы на будущее",
                    correctAnswer = "Постоянные привычки"
                )
            )
            testDao.insertAll(presentContinuousTests)

            // Тесты для Past Continuous (topicId = 7)
            val pastContinuousTests = listOf(
                Test(
                    topicId = 7,
                    question = "Выберите правильный вариант:",
                    option1 = "I was watching TV",
                    option2 = "I were watching TV",
                    option3 = "I watched TV",
                    option4 = "I watching TV",
                    correctAnswer = "I was watching TV"
                ),
                Test(
                    topicId = 7,
                    question = "Отрицание для 'They were playing':",
                    option1 = "They weren't playing",
                    option2 = "They didn't playing",
                    option3 = "They don't playing",
                    option4 = "They wasn't playing",
                    correctAnswer = "They weren't playing"
                ),
                Test(
                    topicId = 7,
                    question = "Выберите правильный перевод: 'Она готовила ужин в 6 часов'",
                    option1 = "She was cooking dinner at 6 o'clock",
                    option2 = "She cooked dinner at 6 o'clock",
                    option3 = "She is cooking dinner at 6 o'clock",
                    option4 = "She had cooked dinner at 6 o'clock",
                    correctAnswer = "She was cooking dinner at 6 o'clock"
                ),
                Test(
                    topicId = 7,
                    question = "С каким временем часто используется Past Continuous?",
                    option1 = "Past Simple",
                    option2 = "Present Perfect",
                    option3 = "Future Simple",
                    option4 = "Present Continuous",
                    correctAnswer = "Past Simple"
                ),
                Test(
                    topicId = 7,
                    question = "Выберите правильную форму для 'We'",
                    option1 = "was",
                    option2 = "were",
                    option3 = "are",
                    option4 = "is",
                    correctAnswer = "were"
                )
            )
            testDao.insertAll(pastContinuousTests)

            // Тесты для Future Continuous (topicId = 8)
            val futureContinuousTests = listOf(
                Test(
                    topicId = 8,
                    question = "Выберите правильный вариант:",
                    option1 = "I will be working tomorrow",
                    option2 = "I will working tomorrow",
                    option3 = "I will work tomorrow",
                    option4 = "I be working tomorrow",
                    correctAnswer = "I will be working tomorrow"
                ),
                Test(
                    topicId = 8,
                    question = "Отрицание для 'They will be sleeping'",
                    option1 = "They won't be sleeping",
                    option2 = "They don't be sleeping",
                    option3 = "They aren't sleeping",
                    option4 = "They willn't be sleeping",
                    correctAnswer = "They won't be sleeping"
                ),
                Test(
                    topicId = 8,
                    question = "Выберите правильный перевод: 'Мы будем обедать в это время'",
                    option1 = "We will have lunch at this time",
                    option2 = "We will be having lunch at this time",
                    option3 = "We are having lunch at this time",
                    option4 = "We have lunch at this time",
                    correctAnswer = "We will be having lunch at this time"
                ),
                Test(
                    topicId = 8,
                    question = "С каким временем чаще используется Future Continuous?",
                    option1 = "Конкретный момент в будущем",
                    option2 = "Прошлый опыт",
                    option3 = "Завершенные действия",
                    option4 = "Неопределенное будущее",
                    correctAnswer = "Конкретный момент в будущем"
                ),
                Test(
                    topicId = 8,
                    question = "Какая структура верна?",
                    option1 = "will + be + глагол-ing",
                    option2 = "will + have + глагол-ing",
                    option3 = "would + be + глагол",
                    option4 = "will + глагол-ing",
                    correctAnswer = "will + be + глагол-ing"
                )
            )
            testDao.insertAll(futureContinuousTests)

            // Тесты для Present Perfect (topicId = 10)
            val presentPerfectTests = listOf(
                Test(
                    topicId = 10,
                    question = "Выберите правильный вариант:",
                    option1 = "I have finished work",
                    option2 = "I has finished work",
                    option3 = "I finished work",
                    option4 = "I finishing work",
                    correctAnswer = "I have finished work"
                ),
                Test(
                    topicId = 10,
                    question = "Отрицание для 'She has seen the movie'",
                    option1 = "She hasn't seen the movie",
                    option2 = "She haven't seen the movie",
                    option3 = "She didn't see the movie",
                    option4 = "She hadn't seen the movie",
                    correctAnswer = "She hasn't seen the movie"
                ),
                Test(
                    topicId = 10,
                    question = "Выберите правильный вопрос:",
                    option1 = "Have you ever been to Paris?",
                    option2 = "Has you ever been to Paris?",
                    option3 = "Did you ever be to Paris?",
                    option4 = "Do you ever been to Paris?",
                    correctAnswer = "Have you ever been to Paris?"
                ),
                Test(
                    topicId = 10,
                    question = "Present Perfect используется для:",
                    option1 = "Действий с результатом в настоящем",
                    option2 = "Действий в определенное время в прошлом",
                    option3 = "Будущих планов",
                    option4 = "Регулярных действий",
                    correctAnswer = "Действий с результатом в настоящем"
                ),
                Test(
                    topicId = 10,
                    question = "Переведите: 'Я потерял ключи'",
                    option1 = "I have lost my keys",
                    option2 = "I lost my keys",
                    option3 = "I lose my keys",
                    option4 = "I losing my keys",
                    correctAnswer = "I have lost my keys"
                )
            )
            testDao.insertAll(presentPerfectTests)

            // Тесты для Past Perfect (topicId = 11)
            val pastPerfectTests = listOf(
                Test(
                    topicId = 11,
                    question = "Выберите правильный пример:",
                    option1 = "I had finished work",
                    option2 = "I have finished work",
                    option3 = "I finished work",
                    option4 = "I has finished work",
                    correctAnswer = "I had finished work"
                ),
                Test(
                    topicId = 11,
                    question = "Отрицание для 'She had left'",
                    option1 = "She hadn't left",
                    option2 = "She didn't left",
                    option3 = "She hasn't left",
                    option4 = "She don't left",
                    correctAnswer = "She hadn't left"
                ),
                Test(
                    topicId = 11,
                    question = "Выберите правильный перевод: 'Когда я пришел, они уже ушли'",
                    option1 = "When I came, they had already left",
                    option2 = "When I come, they left",
                    option3 = "When I had come, they left",
                    option4 = "When I come, they had left",
                    correctAnswer = "When I came, they had already left"
                ),
                Test(
                    topicId = 11,
                    question = "Для чего используется Past Perfect?",
                    option1 = "Действие до другого в прошлом",
                    option2 = "Текущие действия",
                    option3 = "Будущие планы",
                    option4 = "Одновременные действия",
                    correctAnswer = "Действие до другого в прошлом"
                ),
                Test(
                    topicId = 11,
                    question = "Какая структура верна?",
                    option1 = "had + причастие",
                    option2 = "have + причастие",
                    option3 = "did + глагол",
                    option4 = "has + причастие",
                    correctAnswer = "had + причастие"
                )
            )
            testDao.insertAll(pastPerfectTests)

            // Тесты для Future Perfect (topicId = 12)
            val futurePerfectTests = listOf(
                Test(
                    topicId = 12,
                    question = "Выберите правильный вариант:",
                    option1 = "By 5 PM I will have finished",
                    option2 = "By 5 PM I will finish",
                    option3 = "By 5 PM I finished",
                    option4 = "By 5 PM I have finish",
                    correctAnswer = "By 5 PM I will have finished"
                ),
                Test(
                    topicId = 12,
                    question = "Отрицание для 'They will have arrived'",
                    option1 = "They won't have arrived",
                    option2 = "They don't have arrived",
                    option3 = "They didn't have arrived",
                    option4 = "They will not have arrived",
                    correctAnswer = "They won't have arrived"
                ),
                Test(
                    topicId = 12,
                    question = "Выберите правильный перевод: 'К завтрашнему дню я закончу проект'",
                    option1 = "By tomorrow I will have finished the project",
                    option2 = "Tomorrow I will finish the project",
                    option3 = "I have finished the project tomorrow",
                    option4 = "Tomorrow I have finish the project",
                    correctAnswer = "By tomorrow I will have finished the project"
                ),
                Test(
                    topicId = 12,
                    question = "Future Perfect описывает:",
                    option1 = "Действие к определенному моменту",
                    option2 = "Длительный процесс",
                    option3 = "Регулярные действия",
                    option4 = "Будующие действия",
                    correctAnswer = "Действие к определенному моменту"
                ),
                Test(
                    topicId = 12,
                    question = "Какая структура верна?",
                    option1 = "will have + причастие",
                    option2 = "will be + причастие",
                    option3 = "would have + причастие",
                    option4 = "would + причастие",
                    correctAnswer = "will have + причастие"
                )
            )
            testDao.insertAll(futurePerfectTests)

            // Тесты для Present Perfect Continuous (topicId = 14)
            val presentPerfectContinuousTests = listOf(
                Test(
                    topicId = 14,
                    question = "Выберите правильный вариант:",
                    option1 = "I have been working",
                    option2 = "I has been working",
                    option3 = "I had been working",
                    option4 = "I have being working",
                    correctAnswer = "I have been working"
                ),
                Test(
                    topicId = 14,
                    question = "Отрицание для 'He has been waiting'",
                    option1 = "He hasn't been waiting",
                    option2 = "He haven't been waiting",
                    option3 = "He hadn't been waiting",
                    option4 = "He don't been waiting",
                    correctAnswer = "He hasn't been waiting"
                ),
                Test(
                    topicId = 14,
                    question = "Выберите правильный перевод: 'Я учу английский 3 года'",
                    option1 = "I have been learning English for 3 years",
                    option2 = "I learn English for 3 years",
                    option3 = "I learned English for 3 years",
                    option4 = "I learning English for 3 years",
                    correctAnswer = "I have been learning English for 3 years"
                ),
                Test(
                    topicId = 14,
                    question = "Какой временной указатель используется?",
                    option1 = "For 2 hours",
                    option2 = "Yesterday",
                    option3 = "Last week",
                    option4 = "Tomorrow",
                    correctAnswer = "For 2 hours"
                ),
                Test(
                    topicId = 14,
                    question = "Какая структура верна?",
                    option1 = "have/has been + глагол-ing",
                    option2 = "had been + глагол-ing",
                    option3 = "will have been + глагол-ing",
                    option4 = "have/has + глагол-ing",
                    correctAnswer = "have/has been + глагол-ing"
                )
            )
            testDao.insertAll(presentPerfectContinuousTests)

            // Тесты для Past Perfect Continuous (topicId = 15)
            val pastPerfectContinuousTests = listOf(
                Test(
                    topicId = 15,
                    question = "Выберите правильный пример:",
                    option1 = "I had been studying",
                    option2 = "I have been studying",
                    option3 = "I was studying",
                    option4 = "I did been studying",
                    correctAnswer = "I had been studying"
                ),
                Test(
                    topicId = 15,
                    question = "Отрицание для 'They had been working'",
                    option1 = "They hadn't been working",
                    option2 = "They haven't been working",
                    option3 = "They didn't been working",
                    option4 = "They wasn't working",
                    correctAnswer = "They hadn't been working"
                ),
                Test(
                    topicId = 15,
                    question = "Выберите правильный перевод: 'Она готовила ужин 2 часа, когда я пришел'",
                    option1 = "She had been cooking dinner for 2 hours when I came",
                    option2 = "She was cooking dinner for 2 hours when I came",
                    option3 = "She cooked dinner for 2 hours when I came",
                    option4 = "She has been cooking dinner for 2 hours when I came",
                    correctAnswer = "She had been cooking dinner for 2 hours when I came"
                ),
                Test(
                    topicId = 15,
                    question = "Past Perfect Continuous подчеркивает:",
                    option1 = "Длительность до другого действия",
                    option2 = "Результат к моменту",
                    option3 = "Будущие планы",
                    option4 = "Прошлое",
                    correctAnswer = "Длительность до другого действия"
                ),
                Test(
                    topicId = 15,
                    question = "Какая структура верна?",
                    option1 = "had been + глагол-ing",
                    option2 = "have been + глагол-ing",
                    option3 = "has been + глагол-ing",
                    option4 = "had been + глагол",
                    correctAnswer = "had been + глагол-ing"
                )
            )
            testDao.insertAll(pastPerfectContinuousTests)

            // Тесты для Future Perfect Continuous (topicId = 16)
            val futurePerfectContinuousTests = listOf(
                Test(
                    topicId = 16,
                    question = "Выберите правильный вариант:",
                    option1 = "By 2025 I will have been working here 10 years",
                    option2 = "By 2025 I will work here 10 years",
                    option3 = "By 2025 I have worked here 10 years",
                    option4 = "By 2025 I would have been working here 10 years",
                    correctAnswer = "By 2025 I will have been working here 10 years"
                ),
                Test(
                    topicId = 16,
                    question = "Отрицание для 'He will have been studying'",
                    option1 = "He won't have been studying",
                    option2 = "He don't have been studying",
                    option3 = "He didn't have been studying",
                    option4 = "He hasn't been studying",
                    correctAnswer = "He won't have been studying"
                ),
                Test(
                    topicId = 16,
                    question = "Выберите правильный перевод: 'К июню мы будем жить здесь 5 лет'",
                    option1 = "By June we will have been living here for 5 years",
                    option2 = "In June we will live here for 5 years",
                    option3 = "We have lived here for 5 years in June",
                    option4 = "We will live here 5 years by June",
                    correctAnswer = "By June we will have been living here for 5 years"
                ),
                Test(
                    topicId = 16,
                    question = "Future Perfect Continuous описывает:",
                    option1 = "Длительность к моменту в будущем",
                    option2 = "Результат в прошлом",
                    option3 = "Текущие действия",
                    option4 = "Длительность к моменту в настоящем",
                    correctAnswer = "Длительность к моменту в будущем"
                ),
                Test(
                    topicId = 16,
                    question = "Какая структура верна?",
                    option1 = "will have been + глагол-ing",
                    option2 = "would have been + глагол-ing",
                    option3 = "will be + глагол-ing",
                    option4 = "will be + глагол",
                    correctAnswer = "will have been + глагол-ing"
                )
            )
            testDao.insertAll(futurePerfectContinuousTests)

            // Тесты для Future in the Past (topicId = 17)
            val futureInThePastTests = listOf(
                Test(
                    topicId = 17,
                    question = "Выберите правильный пример:",
                    option1 = "He said he would come",
                    option2 = "He said he will come",
                    option3 = "He says he would come",
                    option4 = "He says he will came",
                    correctAnswer = "He said he would come"
                ),
                Test(
                    topicId = 17,
                    question = "Отрицание для 'They would be working'",
                    option1 = "They wouldn't be working",
                    option2 = "They didn't be working",
                    option3 = "They won't be working",
                    option4 = "They not would be working",
                    correctAnswer = "They wouldn't be working"
                ),
                Test(
                    topicId = 17,
                    question = "Выберите правильный перевод: 'Он думал, что будет спать'",
                    option1 = "He thought he would be sleeping",
                    option2 = "He thinks he will sleep",
                    option3 = "He thought he will be sleeping",
                    option4 = "He think he would sleep",
                    correctAnswer = "He thought he would be sleeping"
                ),
                Test(
                    topicId = 17,
                    question = "Future in the Past используется:",
                    option1 = "В косвенной речи о будущем",
                    option2 = "Для текущих действий",
                    option3 = "В вопросах",
                    option4 = "В утверждениях о прошлом",
                    correctAnswer = "В косвенной речи о будущем"
                ),
                Test(
                    topicId = 17,
                    question = "Какая структура верна?",
                    option1 = "would + глагол",
                    option2 = "will + глагол",
                    option3 = "had + глагол",
                    option4 = "was going to + глагол",
                    correctAnswer = "would + глагол"
                )
            )
            testDao.insertAll(futureInThePastTests)

            // Тесты для Perfect Continuous Future in the Past (topicId = 18)
            val perfectContinuousFutureInPastTests = listOf(
                Test(
                    topicId = 18,
                    question = "Выберите правильный пример:",
                    option1 = "He said he would have been working",
                    option2 = "He says he will have been working",
                    option3 = "He said he will work",
                    option4 = "He says he would work",
                    correctAnswer = "He said he would have been working"
                ),
                Test(
                    topicId = 18,
                    question = "Отрицание для 'She would have been studying'",
                    option1 = "She wouldn't have been studying",
                    option2 = "She didn't have been studying",
                    option3 = "She won't have been studying",
                    option4 = "She would't have studying",
                    correctAnswer = "She wouldn't have been studying"
                ),
                Test(
                    topicId = 18,
                    question = "Выберите правильный перевод: 'Они думали, что будут ждать 3 часа'",
                    option1 = "They thought they would have been waiting for 3 hours",
                    option2 = "They think they will wait 3 hours",
                    option3 = "They thought they will have waited 3 hours",
                    option4 = "They think they won't wait 3 hours",
                    correctAnswer = "They thought they would have been waiting for 3 hours"
                ),
                Test(
                    topicId = 18,
                    question = "Это время используется для:",
                    option1 = "Длительных действий в прошлом",
                    option2 = "Будущих действий с точки зрения прошлого",
                    option3 = "Текущих планов",
                    option4 = "Будующих действий",
                    correctAnswer = "Будущих действий с точки зрения прошлого"
                ),
                Test(
                    topicId = 18,
                    question = "Какая структура верна?",
                    option1 = "would have been + глагол-ing",
                    option2 = "will have been + глагол-ing",
                    option3 = "had been + глагол-ing",
                    option4 = "have been + глагол-ing",
                    correctAnswer = "would have been + глагол-ing"
                )
            )
            testDao.insertAll(perfectContinuousFutureInPastTests)
        }

    }
}


