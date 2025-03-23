package com.example.application.data

import kotlinx.coroutines.flow.Flow

class VocabularyRepository(
    private val wordToLearnDao: WordToLearnDao,
    private val learnedWordDao: LearnedWordDao
) {

    // Для слов, которые нужно учить
    val allWordsToLearn: Flow<List<WordToLearn>> = wordToLearnDao.getAllWords()

    suspend fun insertWordToLearn(word: WordToLearn) {
        wordToLearnDao.insert(word)
    }

    suspend fun deleteWordToLearn(wordId: Int) {
        wordToLearnDao.deleteWord(wordId)
    }

    // Для выученных слов
    val allLearnedWords: Flow<List<LearnedWord>> = learnedWordDao.getAllWords()

    suspend fun insertLearnedWord(word: LearnedWord) {
        learnedWordDao.insert(word)
    }

    suspend fun deleteLearnedWord(wordId: Int) {
        learnedWordDao.deleteWord(wordId)
    }
}