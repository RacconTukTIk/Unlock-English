package com.example.application.data

import com.example.application.data.com.example.application.sampledata.WordToRepeatDao
import com.example.application.sampledata.LearnedWordDao
import kotlinx.coroutines.flow.Flow

class VocabularyRepository(
    private val learnedWordDao: LearnedWordDao,
    private val wordToRepeatDao: WordToRepeatDao
) {

    // Для выученных слов
    val allLearnedWords: Flow<List<LearnedWord>> = learnedWordDao.getAllWords()

    suspend fun insertLearnedWord(word: LearnedWord) {
        learnedWordDao.insert(word)
    }

    suspend fun deleteLearnedWord(wordId: Int) {
        learnedWordDao.deleteWord(wordId)
    }

    // Для слов, которые нужно повторить
    val allWordsToRepeat: Flow<List<WordToRepeat>> = wordToRepeatDao.getAllWords()

    suspend fun insertWordToRepeat(word: WordToRepeat) {
        wordToRepeatDao.insert(word)
    }

    suspend fun deleteWordToRepeat(wordId: Int) {
        wordToRepeatDao.deleteWord(wordId)
    }
}