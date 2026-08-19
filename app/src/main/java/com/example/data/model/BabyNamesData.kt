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
    val lordTa: String,
    val lordEn: String,
    val ganaTa: String,
    val yoniTa: String,
    val rajjuTa: String,
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
        AppLanguage.HINDI -> lordEn
        AppLanguage.ENGLISH -> lordEn
    }

    fun getDeity(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> deityTa
        AppLanguage.HINDI -> deityEn
        AppLanguage.ENGLISH -> deityEn
    }

    fun getLettersSummary(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> allLettersSummaryTa
        AppLanguage.HINDI -> allLettersSummaryHi
        AppLanguage.ENGLISH -> allLettersSummaryEn
    }
}
