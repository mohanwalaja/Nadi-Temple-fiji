package com.example.data.model

enum class AlertSeverity {
    HIGH_ALERT,     // Red / Major planetary watch (e.g. Sani 12th/1st/2nd, Rahu/Ketu in 7/8/2)
    PERIOD_ALERT,   // Orange / Periodic transit change (e.g. Guru from June 2 in 8th house)
    STANDARD_WATCH  // Subtle / General transit guidance
}

data class PlanetTransitAlert(
    val planetTa: String,
    val planetEn: String,
    val planetHi: String,
    val headlineTa: String,
    val headlineEn: String,
    val headlineHi: String,
    val detailsTa: String,
    val detailsEn: String,
    val detailsHi: String
) {
    fun getPlanet(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> planetTa
        AppLanguage.HINDI -> planetHi
        AppLanguage.ENGLISH -> planetEn
    }
    fun getHeadline(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> headlineTa
        AppLanguage.HINDI -> headlineHi
        AppLanguage.ENGLISH -> headlineEn
    }
    fun getDetails(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> detailsTa
        AppLanguage.HINDI -> detailsHi
        AppLanguage.ENGLISH -> detailsEn
    }
}

data class RasiTransitPalan2026(
    val rasi: Rasi,
    val severity: AlertSeverity,
    val alertTagTa: String,
    val alertTagEn: String,
    val alertTagHi: String,
    val keyPlanets: List<PlanetTransitAlert>,
    val careerTa: String,
    val careerEn: String,
    val careerHi: String,
    val financeTa: String,
    val financeEn: String,
    val financeHi: String,
    val marriageTa: String,
    val marriageEn: String,
    val marriageHi: String,
    val educationTa: String,
    val educationEn: String,
    val educationHi: String,
    val healthTa: String,
    val healthEn: String,
    val healthHi: String,
    val familyTa: String,
    val familyEn: String,
    val familyHi: String,
    val primaryAdviceTa: String,
    val primaryAdviceEn: String,
    val primaryAdviceHi: String,
    val periodGuidanceTa: String = "",
    val periodGuidanceEn: String = "",
    val periodGuidanceHi: String = ""
) {
    fun getAlertTag(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> alertTagTa
        AppLanguage.HINDI -> alertTagHi
        AppLanguage.ENGLISH -> alertTagEn
    }
    fun getCareer(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> careerTa
        AppLanguage.HINDI -> careerHi
        AppLanguage.ENGLISH -> careerEn
    }
    fun getFinance(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> financeTa
        AppLanguage.HINDI -> financeHi
        AppLanguage.ENGLISH -> financeEn
    }
    fun getMarriage(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> marriageTa
        AppLanguage.HINDI -> marriageHi
        AppLanguage.ENGLISH -> marriageEn
    }
    fun getEducation(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> educationTa
        AppLanguage.HINDI -> educationHi
        AppLanguage.ENGLISH -> educationEn
    }
    fun getHealth(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> healthTa
        AppLanguage.HINDI -> healthHi
        AppLanguage.ENGLISH -> healthEn
    }
    fun getFamily(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> familyTa
        AppLanguage.HINDI -> familyHi
        AppLanguage.ENGLISH -> familyEn
    }
    fun getPrimaryAdvice(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> primaryAdviceTa
        AppLanguage.HINDI -> primaryAdviceHi
        AppLanguage.ENGLISH -> primaryAdviceEn
    }
    fun getPeriodGuidance(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> periodGuidanceTa
        AppLanguage.HINDI -> periodGuidanceHi
        AppLanguage.ENGLISH -> periodGuidanceEn
    }
}
