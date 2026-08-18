package com.example.data.model

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    TAMIL("ta", "Tamil", "தமிழ்"),
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी")
}

data class MultilingualText(
    val ta: String,
    val en: String,
    val hi: String = en
) {
    fun get(language: AppLanguage): String = when (language) {
        AppLanguage.TAMIL -> ta
        AppLanguage.ENGLISH -> en
        AppLanguage.HINDI -> hi
    }
}

typealias BilingualText = MultilingualText

