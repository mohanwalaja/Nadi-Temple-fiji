package com.example.data.model

import java.time.LocalDate

data class HinduFestival(
    val id: String,
    val nameTa: String,
    val nameEn: String,
    val tamilMonth: TamilMonth,
    val deityTa: String,
    val deityEn: String,
    val significanceTa: String,
    val significanceEn: String,
    val ritualsTa: String,
    val ritualsEn: String,
    val traditionalRuleTa: String,
    val traditionalRuleEn: String,
    val iconEmoji: String,
    val isTempleMajor: Boolean = false,
    val nameHi: String = nameEn,
    val deityHi: String = deityEn,
    val significanceHi: String = significanceEn,
    val ritualsHi: String = ritualsEn,
    val traditionalRuleHi: String = traditionalRuleEn
) {
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
    fun getDeity(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> deityTa
        AppLanguage.HINDI -> deityHi
        AppLanguage.ENGLISH -> deityEn
    }
    fun getSignificance(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> significanceTa
        AppLanguage.HINDI -> significanceHi
        AppLanguage.ENGLISH -> significanceEn
    }
    fun getRituals(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> ritualsTa
        AppLanguage.HINDI -> ritualsHi
        AppLanguage.ENGLISH -> ritualsEn
    }
    fun getTraditionalRule(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> traditionalRuleTa
        AppLanguage.HINDI -> traditionalRuleHi
        AppLanguage.ENGLISH -> traditionalRuleEn
    }

    /**
     * Computes the calculated approximate Gregorian date for this festival for a given Gregorian year.
     */
    fun calculateDateForYear(year: Int): LocalDate {
        return when (id) {
            "tamil_new_year" -> LocalDate.of(year, 4, 14)
            "chithirai_pournami" -> LocalDate.of(year, 5, 2)
            "vaikasi_visakam" -> LocalDate.of(year, 5, 28)
            "aadi_perukku" -> LocalDate.of(year, 8, 3)
            "aadi_amavasai" -> LocalDate.of(year, 8, 12)
            "krishna_jayanthi" -> LocalDate.of(year, 8, 26)
            "vinayaka_chaturthi" -> LocalDate.of(year, 9, 7)
            "navaratri" -> LocalDate.of(year, 10, 3)
            "saraswati_pooja" -> LocalDate.of(year, 10, 11)
            "ayudha_pooja" -> LocalDate.of(year, 10, 11)
            "vijayadashami" -> LocalDate.of(year, 10, 12)
            "deepavali" -> LocalDate.of(year, 10, 31)
            "skanda_sashti" -> LocalDate.of(year, 11, 2)
            "soorasamharam" -> LocalDate.of(year, 11, 7)
            "karthigai_deepam" -> LocalDate.of(year, 12, 13)
            "thiruvathirai" -> LocalDate.of(year, 1, 3)
            "vaikunta_ekadashi" -> LocalDate.of(year, 1, 10)
            "thai_pongal" -> LocalDate.of(year, 1, 14)
            "thaipusam" -> LocalDate.of(year, 1, 23)
            "maha_shivaratri" -> LocalDate.of(year, 2, 26)
            "panguni_uthiram" -> LocalDate.of(year, 3, 24)
            else -> LocalDate.of(year, 1, 1)
        }
    }
}
