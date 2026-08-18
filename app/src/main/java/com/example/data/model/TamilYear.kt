package com.example.data.model

import java.time.LocalDate

data class TamilSamvatsara(
    val number: Int,
    val tamilName: String,
    val englishName: String,
    val hindiName: String = englishName
) {
    fun displayName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> "$tamilName வருடம் ($englishName Samvatsara - $number)"
        AppLanguage.HINDI -> "$hindiName संवत्सर ($number/60)"
        AppLanguage.ENGLISH -> "$englishName Samvatsara ($tamilName - Year $number)"
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
    val hindiName: String = englishName
) {
    CHITHIRAI(1, "சித்திரை", "Chithirai", "மேஷ மாதம்", "Mesha Masa", "மேஷம் (Mesham)", 14, "चैत्र / मेष मास (Chaitra / Mesha)"),
    VAIKASI(2, "வைகாசி", "Vaikasi", "ரிஷப மாதம்", "Vrishabha Masa", "ரிஷபம் (Rishabam)", 15, "वैशाख / वृषभ मास (Vaisakha / Vrishabha)"),
    AANI(3, "ஆனி", "Aani", "மிதுன மாதம்", "Mithuna Masa", "மிதுனம் (Mithunam)", 15, "ज्येष्ठ / मिथुन मास (Jyeshtha / Mithuna)"),
    AADI(4, "ஆடி", "Aadi", "கடக மாதம்", "Kataka Masa", "கடகம் (Kadagam)", 16, "आषाढ़ / कर्क मास (Ashadha / Kataka)"),
    AVANI(5, "ஆவணி", "Avani", "சிம்ம மாதம்", "Simha Masa", "சிம்மம் (Simham)", 17, "श्रावण / सिंह मास (Shravana / Simha)"),
    PURATTASI(6, "புரட்டாசி", "Purattasi", "கன்யா மாதம்", "Kanya Masa", "கன்னி (Kanni)", 17, "भाद्रपद / कन्या मास (Bhadrapada / Kanya)"),
    AIPASI(7, "ஐப்பசி", "Aipasi", "துலா மாதம்", "Tula Masa", "துலாம் (Thulam)", 18, "आश्विन / तुला मास (Ashwin / Tula)"),
    KARTHIGAI(8, "கார்த்திகை", "Karthigai", "விருச்சிக மாதம்", "Vrischika Masa", "விருச்சிகம் (Viruchigam)", 17, "कार्तिक / वृश्चिक मास (Karthigai / Vrischika)"),
    MARGHAZHI(9, "மார்கழி", "Marghazhi", "தனுர் மாதம்", "Dhanus Masa", "தனுசு (Dhanusu)", 16, "मार्गशीर्ष / धनुष मास (Margashirsha / Dhanus)"),
    THAI(10, "தை", "Thai", "மகர மாதம்", "Makara Masa", "மகரம் (Magaram)", 14, "पौष / मकर मास (Makara / Thai)"),
    MASI(11, "மாசி", "Masi", "கும்ப மாதம்", "Kumbha Masa", "கும்பம் (Kumbham)", 13, "माघ / कुम्भ मास (Magha / Kumbha)"),
    PANGUNI(12, "பங்குனி", "Panguni", "மீன மாதம்", "Meena Masa", "மீனம் (Meenam)", 14, "फाल्गुन / मीन मास (Phalguna / Meena)");

    fun name(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> tamilName
        AppLanguage.HINDI -> hindiName
        AppLanguage.ENGLISH -> englishName
    }

    fun getDisplayNameWithSanskrit(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> "$tamilName ($sanskritMasa / $sanskritMasaEn)"
        AppLanguage.HINDI -> "$hindiName ($sanskritMasaEn)"
        AppLanguage.ENGLISH -> "$englishName ($sanskritMasaEn / $sanskritMasa)"
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

    /**
     * Calculates the Tamil Samvatsara from Gregorian date.
     * Tamil New Year begins around April 14.
     * Year 2024 (after April 14) is Krodhi (38).
     * Year 2025 is Visvavasu (39).
     * Year 2026 is Parabhava (40).
     */
    fun getSamvatsaraForDate(date: LocalDate): TamilSamvatsara {
        val year = date.year
        // Tamil new year begins on April 14
        val effectiveYear = if (date.monthValue > 4 || (date.monthValue == 4 && date.dayOfMonth >= 14)) {
            year
        } else {
            year - 1
        }
        var index = ((effectiveYear - 1987) % 60)
        if (index < 0) index += 60
        val yearNumber = index + 1
        return YEARS_60.firstOrNull { it.number == yearNumber } ?: YEARS_60[0]
    }

    /**
     * Calculates the Tamil Month and Date for a Gregorian date.
     */
    fun getTamilDate(date: LocalDate): Pair<TamilMonth, Int> {
        val month = date.monthValue
        val day = date.dayOfMonth
        
        return when {
            month == 1 -> if (day >= 14) TamilMonth.THAI to (day - 13) else TamilMonth.MARGHAZHI to (day + 16)
            month == 2 -> if (day >= 13) TamilMonth.MASI to (day - 12) else TamilMonth.THAI to (day + 18)
            month == 3 -> if (day >= 14) TamilMonth.PANGUNI to (day - 13) else TamilMonth.MASI to (day + 16)
            month == 4 -> if (day >= 14) TamilMonth.CHITHIRAI to (day - 13) else TamilMonth.PANGUNI to (day + 17)
            month == 5 -> if (day >= 15) TamilMonth.VAIKASI to (day - 14) else TamilMonth.CHITHIRAI to (day + 17)
            month == 6 -> if (day >= 15) TamilMonth.AANI to (day - 14) else TamilMonth.VAIKASI to (day + 17)
            month == 7 -> if (day >= 16) TamilMonth.AADI to (day - 15) else TamilMonth.AANI to (day + 16)
            month == 8 -> if (day >= 17) TamilMonth.AVANI to (day - 16) else TamilMonth.AADI to (day + 16)
            month == 9 -> if (day >= 17) TamilMonth.PURATTASI to (day - 16) else TamilMonth.AVANI to (day + 15)
            month == 10 -> if (day >= 18) TamilMonth.AIPASI to (day - 17) else TamilMonth.PURATTASI to (day + 14)
            month == 11 -> if (day >= 17) TamilMonth.KARTHIGAI to (day - 16) else TamilMonth.AIPASI to (day + 14)
            else -> if (day >= 16) TamilMonth.MARGHAZHI to (day - 15) else TamilMonth.KARTHIGAI to (day + 14) // month 12
        }
    }
}
