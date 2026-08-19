package com.example.data.model

import java.time.LocalDate

data class TamilSamvatsara(
    val number: Int,
    val tamilName: String,
    val englishName: String,
    val hindiName: String = englishName
) {
    fun displayName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> "$tamilName வருடம் ($number/60)"
        AppLanguage.HINDI -> "$hindiName संवत्सर ($number/60)"
        AppLanguage.ENGLISH -> "$englishName Samvatsara ($number/60)"
    }
}

enum class TamilMonth(
    val index: Int,
    val tamilName: String,
    val englishName: String,
    val sanskritMasa: String,
    val sanskritMasaEn: String,
    val solarRasi: String,
    val typicalStartDay: Int, // Gregorian start day in approx month
    val hindiName: String = englishName,
    val sanskritMasaHi: String = hindiName
) {
    CHITHIRAI(1, "சித்திரை", "Chithirai", "மேஷ மாதம்", "Mesha Masa", "மேஷம்", 14, "चैत्र मास", "मेष मास"),
    VAIKASI(2, "வைகாசி", "Vaikasi", "ரிஷப மாதம்", "Vrishabha Masa", "ரிஷபம்", 15, "वैशाख मास", "वृषभ मास"),
    AANI(3, "ஆனி", "Aani", "மிதுன மாதம்", "Mithuna Masa", "மிதுனம்", 15, "ज्येष्ठ मास", "मिथुन मास"),
    AADI(4, "ஆடி", "Aadi", "கடக மாதம்", "Kataka Masa", "கடகம்", 16, "ஆஷாட் மாஸ", "कर्क मास"),
    AVANI(5, "ஆவணி", "Avani", "சிம்ம மாதம்", "Simha Masa", "சிம்மம்", 17, "श्रावण मास", "सिंह मास"),
    PURATTASI(6, "புரட்டாசி", "Purattasi", "கன்யா மாதம்", "Kanya Masa", "கன்னி", 17, "भाद्रपद मास", "कन्या मास"),
    AIPASI(7, "ஐப்பசி", "Aipasi", "துலா மாதம்", "Tula Masa", "துலாம்", 18, "ஆஸ்வின மாஸ", "तुला मास"),
    KARTHIGAI(8, "கார்த்திகை", "Karthigai", "விருச்சிக மாதம்", "Vrischika Masa", "விருச்சிகம்", 17, "कार्तिक मास", "वृश्चिक मास"),
    MARGHAZHI(9, "மார்கழி", "Marghazhi", "தனுர் மாதம்", "Dhanus Masa", "தனுசு", 16, "मार्गशीर्ष मास", "धनु मास"),
    THAI(10, "தை", "Thai", "மகர மாதம்", "Makara Masa", "மகரம்", 14, "पौष मास", "मकर मास"),
    MASI(11, "மாசி", "Masi", "கும்ப மாதம்", "Kumbha Masa", "கும்பம்", 13, "माघ मास", "कुम्भ मास"),
    PANGUNI(12, "பங்குனி", "Panguni", "மீன மாதம்", "Meena Masa", "மீனம்", 14, "फाल्गुन मास", "मीन मास");

    fun name(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> tamilName
        AppLanguage.HINDI -> hindiName
        AppLanguage.ENGLISH -> englishName
    }

    fun getDisplayNameWithSanskrit(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> "$tamilName ($sanskritMasa)"
        AppLanguage.HINDI -> "$hindiName ($sanskritMasaHi)"
        AppLanguage.ENGLISH -> "$englishName ($sanskritMasaEn)"
    }
}

object TamilSamvatsaraEngine {
    val YEARS_60 = listOf(
        TamilSamvatsara(1, "பிரபவ", "Prabhava", "प्रभव"),
        TamilSamvatsara(2, "விபவ", "Vibhava", "विभव"),
        TamilSamvatsara(3, "சுக்ல", "Shukla", "शुक्ल"),
        TamilSamvatsara(4, "பிரமோதூத", "Pramodhuta", "प्रमोदूत"),
        TamilSamvatsara(5, "பிரசோற்பத்தி", "Prajotpatti", "प्रजोत्पत्ति"),
        TamilSamvatsara(6, "ஆங்கீரச", "Angirasa", "आङ्गिरस"),
        TamilSamvatsara(7, "ஸ்ரீமுக", "Srimukha", "श्रीमुख"),
        TamilSamvatsara(8, "பவ", "Bhava", "भव"),
        TamilSamvatsara(9, "யுவ", "Yuva", "युव"),
        TamilSamvatsara(10, "தாது", "Dhatru", "धातृ"),
        TamilSamvatsara(11, "ஈஸ்வர", "Ishvara", "ईश्वर"),
        TamilSamvatsara(12, "வெகுதானிய", "Bahudhanya", "बहुधान्य"),
        TamilSamvatsara(13, "பிரமாதி", "Pramathi", "प्रमाथी"),
        TamilSamvatsara(14, "விக்கிரம", "Vikrama", "विक्रम"),
        TamilSamvatsara(15, "விஷு", "Vishu", "विषु"),
        TamilSamvatsara(16, "சித்திரபானு", "Chitrabanu", "चित्रभानु"),
        TamilSamvatsara(17, "சுபானு", "Subhanu", "सुभानु"),
        TamilSamvatsara(18, "தாரண", "Tharana", "तारण"),
        TamilSamvatsara(19, "பார்த்திப", "Parthiba", "पार्थिव"),
        TamilSamvatsara(20, "விய", "Viya", "व्यय"),
        TamilSamvatsara(21, "சர்வஜித்", "Sarvajith", "सर्वजित्"),
        TamilSamvatsara(22, "சர்வதாரி", "Sarvadhari", "सर्वधारी"),
        TamilSamvatsara(23, "விரோதி", "Virodhi", "विरोधी"),
        TamilSamvatsara(24, "விக்ருதி", "Vikruthi", "विकृति"),
        TamilSamvatsara(25, "கர", "Kara", "खर"),
        TamilSamvatsara(26, "நந்தன", "Nandhana", "नन्दन"),
        TamilSamvatsara(27, "விஜய", "Vijaya", "विजय"),
        TamilSamvatsara(28, "ஜய", "Jaya", "जय"),
        TamilSamvatsara(29, "மன்மத", "Manmatha", "मन्मथ"),
        TamilSamvatsara(30, "துன்முகி", "Dhunmukhi", "दुर्मुख"),
        TamilSamvatsara(31, "ஹேவிளம்பி", "Hevilambi", "हेविलम्बी"),
        TamilSamvatsara(32, "விளம்பி", "Vilambi", "विलम्बी"),
        TamilSamvatsara(33, "விகாரி", "Vikari", "विकारी"),
        TamilSamvatsara(34, "சார்வரி", "Sarvari", "शार्वरी"),
        TamilSamvatsara(35, "பிலவ", "Plava", "प्लव"),
        TamilSamvatsara(36, "சுபகிருது", "Subhakruthu", "शुभकृत्"),
        TamilSamvatsara(37, "சோபகிருது", "Sobhakruthu", "शोभकृत्"),
        TamilSamvatsara(38, "குரோதி", "Krodhi", "क्रोधी"),
        TamilSamvatsara(39, "விசுவாசுவ", "Visvavasu", "विश्वावसु"),
        TamilSamvatsara(40, "பராபவ", "Parabhava", "पराभव"),
        TamilSamvatsara(41, "பிலவங்க", "Plavanga", "प्लवङ्ग"),
        TamilSamvatsara(42, "கீலக", "Keelaka", "कीलक"),
        TamilSamvatsara(43, "சௌமிய", "Saumya", "सौम्य"),
        TamilSamvatsara(44, "சாதாரண", "Sadharana", "साधारण"),
        TamilSamvatsara(45, "விரோதகிருது", "Virodhikruthu", "विरोधकृत्"),
        TamilSamvatsara(46, "பரிதாபி", "Paridhabi", "परिधावी"),
        TamilSamvatsara(47, "பிரமாதீச", "Pramadheesa", "प्रमादी"),
        TamilSamvatsara(48, "ஆனந்த", "Anandha", "आनन्द"),
        TamilSamvatsara(49, "ராட்சச", "Rakshasa", "राक्षस"),
        TamilSamvatsara(50, "நள", "Nala", "नल"),
        TamilSamvatsara(51, "பிங்கள", "Pingala", "पिङ्गल"),
        TamilSamvatsara(52, "காளயுக்தி", "Kalayukthi", "कालयुक्त"),
        TamilSamvatsara(53, "சித்தார்த்தி", "Siddharthi", "सिद्धार्थी"),
        TamilSamvatsara(54, "ரௌத்திரி", "Raudhri", "रौद्र"),
        TamilSamvatsara(55, "துன்மதி", "Dunmathi", "दुर्मति"),
        TamilSamvatsara(56, "துந்துபி", "Dhundhubhi", "दुन्दुभी"),
        TamilSamvatsara(57, "ருத்ரோத்காரி", "Rudhrodhkari", "रुधिरोद्गारी"),
        TamilSamvatsara(58, "ரக்தாட்சி", "Raktakshi", "रक्ताक्ष"),
        TamilSamvatsara(59, "குரோதன", "Krodhana", "क्रोधन"),
        TamilSamvatsara(60, "அட்சய", "Akshaya", "क्षय")
    )

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

    private fun getSunSiderealLongitude(jd: Double): Double {
        val t = (jd - 2451545.0) / 36525.0
        val l0 = (280.46646 + 36000.76983 * t + 0.0003032 * t * t) % 360.0
        val m = (357.52911 + 35999.05029 * t - 0.0001537 * t * t) % 360.0
        val c = (1.914602 - 0.004817 * t - 0.000014 * t * t) * kotlin.math.sin(Math.toRadians(m)) +
                (0.019993 - 0.000101 * t) * kotlin.math.sin(Math.toRadians(2 * m)) +
                0.000289 * kotlin.math.sin(Math.toRadians(3 * m))
        var sunTrue = (l0 + c) % 360.0
        if (sunTrue < 0) sunTrue += 360.0
        // Lahiri Ayanamsha
        val ayanamsha = 23.85 + (jd - 2451545.0) * (50.29 / 3600.0 / 365.25)
        var sunSid = (sunTrue - ayanamsha) % 360.0
        if (sunSid < 0) sunSid += 360.0
        return sunSid
    }

    /**
     * Calculates the true astronomical Tamil Samvatsara from Sun's Sidereal Longitude.
     * Tamil New Year begins when the Sun enters Mesha Rasi (Mesha Sankranti in April).
     */
    fun getSamvatsaraForDate(date: LocalDate): TamilSamvatsara {
        val jd = getJulianDay(date, 0.25)
        val sunSid = getSunSiderealLongitude(jd)
        val year = date.year
        // If in Jan-April before Mesha Sankranti (when sun is in Makara, Kumbha, or Meena > 270 deg),
        // effective year is year - 1
        val effectiveYear = if (date.monthValue < 4 || (date.monthValue == 4 && sunSid >= 330.0)) {
            year - 1
        } else {
            year
        }
        var index = ((effectiveYear - 1987) % 60)
        if (index < 0) index += 60
        val yearNumber = index + 1
        return YEARS_60.firstOrNull { it.number == yearNumber } ?: YEARS_60[0]
    }

    /**
     * Calculates the true astronomical Tamil Month and Date for a Gregorian date
     * based on the exact solar transit (Sankranti) and solar sidereal longitude.
     */
    fun getTamilDate(date: LocalDate): Pair<TamilMonth, Int> {
        val jd = getJulianDay(date, 0.25)
        val sunSid = getSunSiderealLongitude(jd)
        val rasiIndex = (sunSid / 30.0).toInt() % 12
        val currentMonth = TamilMonth.entries[rasiIndex]

        // Find the exact day of Sankranti (when Sun entered this rasi)
        var testDate = date
        var daysBack = 0
        while (daysBack < 32) {
            val prevDate = testDate.minusDays(1)
            val prevJd = getJulianDay(prevDate, 0.25)
            val prevSunSid = getSunSiderealLongitude(prevJd)
            val prevRasi = (prevSunSid / 30.0).toInt() % 12
            if (prevRasi != rasiIndex) {
                // prevDate was in the previous rasi, so testDate is day 1!
                break
            }
            testDate = prevDate
            daysBack++
        }
        val tamilDay = daysBack + 1
        return currentMonth to tamilDay
    }
}
