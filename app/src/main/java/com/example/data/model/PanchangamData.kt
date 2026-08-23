package com.example.data.model

import java.time.LocalDate

data class PanchangamDetail(
    val gregorianDate: LocalDate,
    val tamilDate: Int,
    val tamilMonth: TamilMonth,
    val tamilYear: TamilSamvatsara,
    // 1. Samvatsara (Tamil Year)
    val samvatsaraName: String = "",
    val samvatsaraNameTa: String = samvatsaraName,
    val samvatsaraNameEn: String = samvatsaraName,
    val samvatsaraNameHi: String = samvatsaraName,
    // 2. Ayanam (Dakshinayanam / Uttarayanam)
    val ayanam: String,
    // 3. Rithu (Season)
    val ritu: String,
    // 4. Sanskrit Solar Month (e.g. Mesha Masa / சிம்ம மாதம்)
    val sanskritMonth: String = "",
    val sanskritMonthTa: String = sanskritMonth,
    val sanskritMonthEn: String = sanskritMonth,
    val sanskritMonthHi: String = sanskritMonth,
    // 5. Paksham (Shukla / Krishna)
    val paksha: String,
    // 6. Tithi
    val tithi: String,
    val tithiEndTime: String,
    val tithiPercent: Int = 100,
    val nextTithi: String? = null,
    // 7. Vasaram (Vara / Day of Week in Vedic tradition)
    val vasaram: String = "",
    val dayOfWeek: String,
    // 8. Nakshatram
    val nakshatram: String,
    val nakshatramEndTime: String,
    val pada: Int = 1,
    val nextNakshatram: String? = null,
    // 9. Yogam (Nithya Yoga + Dina Yoga)
    val yogam: String,
    val yogamEndTime: String = "10:15 PM",
    val dinaYogam: String = "சித்த யோகம்",
    // 10. Karanam
    val karanam: String,
    val karanamEndTime: String = "02:15 PM",
    val nextKaranam: String? = null,
    
    // Additional Vedic & Astronomy timings
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val chandraRasi: String = "மேஷம்",
    val suryaRasi: String = "சிம்மம்",
    val chandrashtamam: String = "",
    val nallaNeramMorning: String = "07:30 AM - 08:30 AM",
    val nallaNeramEvening: String = "04:30 PM - 05:30 PM",
    val gowriNallaNeramMorning: String = "10:30 AM - 11:30 AM",
    val gowriNallaNeramEvening: String = "07:30 PM - 08:30 PM",
    val rahuKalam: String,
    val yamagandam: String,
    val kuligai: String,
    val abhijitMuhurtham: String,
    val durMuhurtham: String,
    val varjyam: String,
    val dishaSoola: String = "மேற்கு",
    val soolaPariharam: String = "வெல்லம்",
    val specialObservances: List<ObservanceType> = emptyList(),
    val festivalNameTa: String? = null,
    val festivalNameEn: String? = null,
    val festivalNameHi: String? = null,
    val isDemoData: Boolean = false,
    
    // Multi-language pure representations
    val tithiTa: String = tithi,
    val tithiEn: String = tithi,
    val tithiHi: String = tithi,
    val nakshatramTa: String = nakshatram,
    val nakshatramEn: String = nakshatram,
    val nakshatramHi: String = nakshatram,
    val yogamTa: String = yogam,
    val yogamEn: String = yogam,
    val yogamHi: String = yogam,
    val karanamTa: String = karanam,
    val karanamEn: String = karanam,
    val karanamHi: String = karanam,
    val pakshaTa: String = paksha,
    val pakshaEn: String = paksha,
    val pakshaHi: String = paksha,
    val ayanamTa: String = ayanam,
    val ayanamEn: String = ayanam,
    val ayanamHi: String = ayanam,
    val rituTa: String = ritu,
    val rituEn: String = ritu,
    val rituHi: String = ritu,
    val dayOfWeekTa: String = dayOfWeek,
    val dayOfWeekEn: String = dayOfWeek,
    val dayOfWeekHi: String = dayOfWeek,
    val vasaramTa: String = vasaram,
    val vasaramEn: String = vasaram,
    val vasaramHi: String = vasaram,
    val chandraRasiTa: String = chandraRasi,
    val chandraRasiEn: String = chandraRasi,
    val chandraRasiHi: String = chandraRasi,
    val suryaRasiTa: String = suryaRasi,
    val suryaRasiEn: String = suryaRasi,
    val suryaRasiHi: String = suryaRasi,
    val chandrashtamamTa: String = chandrashtamam,
    val chandrashtamamEn: String = chandrashtamam,
    val chandrashtamamHi: String = chandrashtamam,
    val dishaSoolaTa: String = dishaSoola,
    val dishaSoolaEn: String = dishaSoola,
    val dishaSoolaHi: String = dishaSoola,
    val soolaPariharamTa: String = soolaPariharam,
    val soolaPariharamEn: String = soolaPariharam,
    val soolaPariharamHi: String = soolaPariharam,
    val dinaYogamTa: String = dinaYogam,
    val dinaYogamEn: String = dinaYogam,
    val dinaYogamHi: String = dinaYogam
) {
    fun getFestivalName(lang: AppLanguage): String? = when (lang) {
        AppLanguage.TAMIL -> festivalNameTa
        AppLanguage.HINDI -> festivalNameHi ?: festivalNameEn
        AppLanguage.ENGLISH -> festivalNameEn
    }

    fun getTithi(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> tithiTa
        AppLanguage.HINDI -> tithiHi
        AppLanguage.ENGLISH -> tithiEn
    }

    fun getNakshatram(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nakshatramTa
        AppLanguage.HINDI -> nakshatramHi
        AppLanguage.ENGLISH -> nakshatramEn
    }

    fun getYogam(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> yogamTa
        AppLanguage.HINDI -> yogamHi
        AppLanguage.ENGLISH -> yogamEn
    }

    fun getKaranam(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> karanamTa
        AppLanguage.HINDI -> karanamHi
        AppLanguage.ENGLISH -> karanamEn
    }

    fun getPaksha(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> pakshaTa
        AppLanguage.HINDI -> pakshaHi
        AppLanguage.ENGLISH -> pakshaEn
    }

    fun getAyanam(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> ayanamTa
        AppLanguage.HINDI -> ayanamHi
        AppLanguage.ENGLISH -> ayanamEn
    }

    fun getRitu(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> rituTa
        AppLanguage.HINDI -> rituHi
        AppLanguage.ENGLISH -> rituEn
    }

    fun getDayOfWeek(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> dayOfWeekTa
        AppLanguage.HINDI -> dayOfWeekHi
        AppLanguage.ENGLISH -> dayOfWeekEn
    }

    fun getVasaram(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> vasaramTa
        AppLanguage.HINDI -> vasaramHi
        AppLanguage.ENGLISH -> vasaramEn
    }

    fun getChandraRasi(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> chandraRasiTa
        AppLanguage.HINDI -> chandraRasiHi
        AppLanguage.ENGLISH -> chandraRasiEn
    }

    fun getSuryaRasi(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> suryaRasiTa
        AppLanguage.HINDI -> suryaRasiHi
        AppLanguage.ENGLISH -> suryaRasiEn
    }

    fun getChandrashtamam(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> chandrashtamamTa
        AppLanguage.HINDI -> chandrashtamamHi
        AppLanguage.ENGLISH -> chandrashtamamEn
    }

    fun getDishaSoola(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> dishaSoolaTa
        AppLanguage.HINDI -> dishaSoolaHi
        AppLanguage.ENGLISH -> dishaSoolaEn
    }

    fun getSoolaPariharam(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> soolaPariharamTa
        AppLanguage.HINDI -> soolaPariharamHi
        AppLanguage.ENGLISH -> soolaPariharamEn
    }

    fun getDinaYogam(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> dinaYogamTa
        AppLanguage.HINDI -> dinaYogamHi
        AppLanguage.ENGLISH -> dinaYogamEn
    }

    fun getSamvatsara(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> samvatsaraNameTa.ifBlank { tamilYear.tamilName }
        AppLanguage.HINDI -> samvatsaraNameHi.ifBlank { tamilYear.hindiName }
        AppLanguage.ENGLISH -> samvatsaraNameEn.ifBlank { tamilYear.englishName }
    }

    fun getSanskritMonth(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> sanskritMonthTa.ifBlank { tamilMonth.sanskritMasa }
        AppLanguage.HINDI -> sanskritMonthHi.ifBlank { tamilMonth.sanskritMasaHi }
        AppLanguage.ENGLISH -> sanskritMonthEn.ifBlank { tamilMonth.sanskritMasaEn }
    }

    fun getTillText(lang: AppLanguage, endTime: String): String = when (lang) {
        AppLanguage.TAMIL -> "$endTime வரை"
        AppLanguage.HINDI -> "$endTime तक"
        AppLanguage.ENGLISH -> "till $endTime"
    }
}

enum class ObservanceType(
    val tamilName: String,
    val englishName: String,
    val hindiName: String,
    val iconEmoji: String,
    val isMajor: Boolean = false
) {
    AMAVASAI("அமாவாசை", "New Moon (Amavasya)", "अमावस्या", "🌑", true),
    POURNAMI("பௌர்ணமி", "Full Moon (Pournami)", "पूर्णिमा", "🌕", true),
    PRADOSHAM("பிரதோஷம்", "Pradosham", "प्रदोष व्रत", "🔱", true),
    EKADASHI("ஏகாதசி", "Ekadashi", "एकादशी व्रत", "✨", true),
    SANKATAHARA_CHATURTHI("சங்கடஹர சதுர்த்தி", "Sankatahara Chaturthi", "संकष्टी चतुर्थी", "🐘", true),
    SHUKLA_SHASHTI("சஷ்டி விரதம்", "Shukla Shashti", "स्कन्द षष्ठी व्रत", "🦚", true),
    KRITTIKAI("கிருத்திகை", "Krittikai", "कृत्तिका नक्षत्रम्", "🔥", true),
    MONTHLY_SHIVARATRI("மாத சிவராத்திரி", "Monthly Shivaratri", "मासिक शिवरात्रि", "🕉️", true),
    CHATURTHI("சதுர்த்தி", "Chaturthi", "विनायक चतुर्थी", "🌸"),
    ASHTAMI("அஷ்டமி", "Ashtami", "दुर्गाष्टमी", "⚡"),
    NAVAMI("நவமி", "Navami", "श्री राम नवमी", "🏹"),
    FESTIVAL("சிறப்பு திருவிழா", "Temple Festival", "मंदिर उत्सव", "🪔", true),
    TIRUVONAM("திருவோணம்", "Tiruvonam (Shravana)", "श्रवणम्", "🐚"),
    BHARANI_DEEPAM("பரணி தீபம்", "Bharani Deepam", "भरणी दीपम्", "🪔");

    fun getDisplayName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> tamilName
        AppLanguage.HINDI -> hindiName
        AppLanguage.ENGLISH -> englishName
    }
}
