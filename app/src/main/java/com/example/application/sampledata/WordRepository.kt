package com.example.application.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val wordDao: WordDao
) {
    fun getLearnedWords(): Flow<List<Word>> = wordDao.getLearnedWords()

    fun getWordsToRepeat(): Flow<List<Word>> = wordDao.getWordsToRepeat()

    suspend fun getRandomWord(): Word? = wordDao.getRandomWord()

    suspend fun markAsLearned(word: Word) {
        wordDao.updateWord(word.copy(status = Word.WordStatus.LEARNED))
    }

    suspend fun markAsNeedRepeat(word: Word) {
        wordDao.updateWord(word.copy(status = Word.WordStatus.NEED_REPEAT))
    }

    suspend fun initializeDatabase(words: List<Word>) {
        wordDao.insertAll(words)
    }
}