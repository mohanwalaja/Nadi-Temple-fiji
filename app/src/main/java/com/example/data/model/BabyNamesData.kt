package com.example.data.model

data class PadaLetterInfo(
    val padaNumber: Int, // 1, 2, 3, 4
    val letterTa: String,
    val letterEn: String,
    val letterHi: String = letterEn,
    val rasi: Rasi,
    val rasiTa: String = rasi.nameTa,
    val rasiEn: String = rasi.nameEn,
    val rasiHi: String = rasi.nameHi
)

data class NakshatraBabyLetters(
    val nakshatraIndex: Int, // 1 to 27
    val nakshatraNameTa: String,
    val nakshatraNameEn: String,
    val nakshatraNameHi: String,
    val deityTa: String,
    val deityEn: String,
    val deityHi: String = deityEn,
    val lordTa: String,
    val lordEn: String,
    val lordHi: String = lordEn,
    val ganaTa: String,
    val ganaEn: String = ganaTa,
    val ganaHi: String = ganaEn,
    val yoniTa: String,
    val yoniEn: String = yoniTa,
    val yoniHi: String = yoniEn,
    val rajjuTa: String,
    val rajjuEn: String = rajjuTa,
    val rajjuHi: String = rajjuEn,
    val padas: List<PadaLetterInfo>,
    val allLettersSummaryTa: String,
    val allLettersSummaryEn: String,
    val allLettersSummaryHi: String = allLettersSummaryEn
) {
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nakshatraNameTa
        AppLanguage.HINDI -> nakshatraNameHi
        AppLanguage.ENGLISH -> nakshatraNameEn
    }

    fun getLord(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> lordTa
        AppLanguage.HINDI -> lordHi
        AppLanguage.ENGLISH -> lordEn
    }

    fun getDeity(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> deityTa
        AppLanguage.HINDI -> deityHi
        AppLanguage.ENGLISH -> deityEn
    }

    fun getGana(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> ganaTa
        AppLanguage.HINDI -> ganaHi
        AppLanguage.ENGLISH -> ganaEn
    }

    fun getYoni(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> yoniTa
        AppLanguage.HINDI -> yoniHi
        AppLanguage.ENGLISH -> yoniEn
    }

    fun getRajju(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> rajjuTa
        AppLanguage.HINDI -> rajjuHi
        AppLanguage.ENGLISH -> rajjuEn
    }

    fun getLettersSummary(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> allLettersSummaryTa
        AppLanguage.HINDI -> allLettersSummaryHi
        AppLanguage.ENGLISH -> allLettersSummaryEn
    }
}
