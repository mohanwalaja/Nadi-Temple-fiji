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
     * Computes the astronomical Gregorian date for this festival for any given year.
     */
    fun calculateDateForYear(year: Int): LocalDate {
        return FestivalAstronomicalEngine.findFestivalDate(id, year)
    }
}

object FestivalAstronomicalEngine {
    private fun getJulianDay(date: LocalDate, hourFraction: Double = 0.25): Double {
        val y = date.year
        val m = date.monthValue
        val d = date.dayOfMonth + hourFraction
        val a = if (m <= 2) y - 1 else y
        val b = if (m <= 2) m + 12 else m
        val capA = a / 100
        val capB = 2 - capA + (capA / 4)
        return kotlin.math.floor(365.25 * (a + 4716)) + kotlin.math.floor(30.6001 * (b + 1)) + d + capB - 1524.5
    }

    private fun getEphemeris(jd: Double): Pair<Double, Double> {
        val t = (jd - 2451545.0) / 36525.0
        // Sun
        val l0 = (280.46646 + 36000.76983 * t + 0.0003032 * t * t) % 360.0
        val mSun = (357.52911 + 35999.05029 * t - 0.0001537 * t * t) % 360.0
        val cSun = (1.914602 - 0.004817 * t - 0.000014 * t * t) * kotlin.math.sin(Math.toRadians(mSun)) +
                (0.019993 - 0.000101 * t) * kotlin.math.sin(Math.toRadians(2 * mSun)) +
                0.000289 * kotlin.math.sin(Math.toRadians(3 * mSun))
        var sunTrue = (l0 + cSun) % 360.0
        if (sunTrue < 0) sunTrue += 360.0
        val ayanamsha = 23.85 + (jd - 2451545.0) * (50.29 / 3600.0 / 365.25)
        var sunSid = (sunTrue - ayanamsha) % 360.0
        if (sunSid < 0) sunSid += 360.0

        // Moon
        val lp = (218.3164477 + 481267.88123421 * t - 0.0015786 * t * t) % 360.0
        val d = (297.8501921 + 445267.1114034 * t - 0.0018819 * t * t) % 360.0
        val m = (134.9633964 + 477198.8675055 * t + 0.0087414 * t * t) % 360.0
        val f = (93.2720950 + 483202.0175233 * t - 0.0036539 * t * t) % 360.0
        val moonCorr = 6.288774 * kotlin.math.sin(Math.toRadians(m)) +
                1.274027 * kotlin.math.sin(Math.toRadians(2 * d - m)) +
                0.658314 * kotlin.math.sin(Math.toRadians(2 * d)) +
                0.213618 * kotlin.math.sin(Math.toRadians(2 * m)) -
                0.185116 * kotlin.math.sin(Math.toRadians(mSun)) -
                0.114332 * kotlin.math.sin(Math.toRadians(2 * f))
        var moonTrue = (lp + moonCorr) % 360.0
        if (moonTrue < 0) moonTrue += 360.0
        var moonSid = (moonTrue - ayanamsha) % 360.0
        if (moonSid < 0) moonSid += 360.0

        return sunSid to moonSid
    }

    fun findFestivalDate(festivalId: String, year: Int): LocalDate {
        val (startMonth, endMonth) = when (festivalId) {
            "thiruvathirai", "vaikunta_ekadashi", "thai_pongal", "thaipusam" -> 1 to 2
            "maha_shivaratri", "panguni_uthiram" -> 2 to 4
            "tamil_new_year", "chithirai_pournami" -> 4 to 5
            "vaikasi_visakam" -> 5 to 6
            "aadi_perukku", "aadi_amavasai" -> 7 to 8
            "krishna_jayanthi", "vinayaka_chaturthi" -> 8 to 9
            "navaratri", "saraswati_pooja", "ayudha_pooja", "vijayadashami" -> 9 to 10
            "deepavali", "skanda_sashti", "soorasamharam" -> 10 to 11
            "karthigai_deepam" -> 11 to 12
            else -> 1 to 12
        }

        val startDate = LocalDate.of(year, startMonth, 1)
        val maxDays = if (endMonth == 2) (if (LocalDate.of(year, 1, 1).isLeapYear) 29 else 28) else if (endMonth in listOf(4, 6, 9, 11)) 30 else 31
        val endDate = LocalDate.of(year, endMonth, maxDays)

        var curr = startDate
        var candidateDate: LocalDate? = null

        while (!curr.isAfter(endDate)) {
            val jd = getJulianDay(curr, 0.25)
            val (sunSid, moonSid) = getEphemeris(jd)
            val sunRasi = (sunSid / 30.0).toInt() % 12
            var elongation = (moonSid - sunSid)
            if (elongation < 0) elongation += 360.0
            val tithiIndex = (elongation / 12.0).toInt() + 1 // 1 to 30 (15 = Pournami, 30 = Amavasai)
            val nakshatraIndex = (moonSid / (360.0 / 27.0)).toInt() + 1 // 1=Ashwini, 3=Krittika, 4=Rohini, 6=Arudra, 8=Pushya, 12=Uttiram, 14=Chitra, 16=Visakam

            val match = when (festivalId) {
                "tamil_new_year" -> sunRasi == 0 && (curr.monthValue == 4 && curr.dayOfMonth in 13..15)
                "chithirai_pournami" -> sunRasi == 0 && (tithiIndex == 15 || nakshatraIndex == 14)
                "vaikasi_visakam" -> sunRasi == 1 && (nakshatraIndex == 16 || tithiIndex == 15)
                "aadi_perukku" -> sunRasi == 3 && (curr.monthValue == 8 && curr.dayOfMonth in 2..4)
                "aadi_amavasai" -> sunRasi == 3 && (tithiIndex == 30 || tithiIndex == 29)
                "krishna_jayanthi" -> sunRasi == 4 && (tithiIndex in 22..24 || nakshatraIndex == 4)
                "vinayaka_chaturthi" -> sunRasi == 4 && (tithiIndex in 4..5)
                "navaratri" -> sunRasi == 5 && (tithiIndex in 1..2)
                "saraswati_pooja", "ayudha_pooja" -> sunRasi == 5 && (tithiIndex in 9..10)
                "vijayadashami" -> sunRasi == 5 && (tithiIndex in 10..11)
                "deepavali" -> sunRasi == 6 && (tithiIndex in 28..29)
                "skanda_sashti", "soorasamharam" -> sunRasi == 6 && (tithiIndex in 6..7)
                "karthigai_deepam" -> sunRasi == 7 && (nakshatraIndex == 3 || tithiIndex == 15)
                "thiruvathirai" -> (sunRasi == 8 || curr.monthValue == 1) && (nakshatraIndex == 6 || tithiIndex == 15)
                "vaikunta_ekadashi" -> (sunRasi == 8 || (curr.monthValue == 1 && curr.dayOfMonth <= 15)) && (tithiIndex == 11)
                "thai_pongal" -> sunRasi == 9 && (curr.monthValue == 1 && curr.dayOfMonth in 14..16)
                "thaipusam" -> sunRasi == 9 && (nakshatraIndex == 8 || tithiIndex == 15)
                "maha_shivaratri" -> sunRasi == 10 && (tithiIndex in 28..29)
                "panguni_uthiram" -> sunRasi == 11 && (nakshatraIndex == 12 || tithiIndex == 15)
                else -> false
            }

            if (match) {
                return curr
            }
            if (candidateDate == null && sunRasi == when (festivalId) {
                "tamil_new_year", "chithirai_pournami" -> 0
                "vaikasi_visakam" -> 1
                "aadi_perukku", "aadi_amavasai" -> 3
                "krishna_jayanthi", "vinayaka_chaturthi" -> 4
                "navaratri", "saraswati_pooja", "ayudha_pooja", "vijayadashami" -> 5
                "deepavali", "skanda_sashti", "soorasamharam" -> 6
                "karthigai_deepam" -> 7
                "thiruvathirai", "vaikunta_ekadashi" -> 8
                "thai_pongal", "thaipusam" -> 9
                "maha_shivaratri" -> 10
                "panguni_uthiram" -> 11
                else -> 0
            }) {
                candidateDate = curr
            }
            curr = curr.plusDays(1)
        }
        return candidateDate ?: startDate
    }
}
