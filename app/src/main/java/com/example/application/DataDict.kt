package com.example.application

data class DataDict(
    val word: String = "",
    val transcription: String = "",
    val translation: String = ""
) {
    // Пустой конструктор необходим для Firebase
    constructor() : this("", "", "")
}