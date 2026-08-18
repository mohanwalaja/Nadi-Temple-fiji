package com.example.data.model

import java.time.LocalDate

data class PanchangamDetail(
    val gregorianDate: LocalDate,
    val tamilDate: Int,
    val tamilMonth: TamilMonth,
    val tamilYear: TamilSamvatsara,
    // 1. Samvatsara (Tamil Year)
    val samvatsaraName: String = "",
    // 2. Ayanam (Dakshinayanam / Uttarayanam)
    val ayanam: String,
    // 3. Rithu (Season)
    val ritu: String,
    // 4. Sanskrit Solar Month (e.g. Mesha Masa / சிம்ம மாதம்)
    val sanskritMonth: String = "",
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
    val dinaYogam: String = "சித்த யோகம் (Siddha Yoga)",
    // 10. Karanam
    val karanam: String,
    val karanamEndTime: String = "02:15 PM",
    val nextKaranam: String? = null,
    
    // Additional Vedic & Astronomy timings
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val chandraRasi: String = "மேஷம் (Aries)",
    val suryaRasi: String = "சிம்மம் (Leo)",
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
    val dishaSoola: String = "மேற்கு (West)",
    val soolaPariharam: String = "வெல்லம் (Jaggery)",
    val specialObservances: List<ObservanceType> = emptyList(),
    val festivalNameTa: String? = null,
    val festivalNameEn: String? = null,
    val festivalNameHi: String? = null,
    val isDemoData: Boolean = false
) {
    fun getFestivalName(lang: AppLanguage): String? = when (lang) {
        AppLanguage.TAMIL -> festivalNameTa
        AppLanguage.HINDI -> festivalNameHi ?: festivalNameEn
        AppLanguage.ENGLISH -> festivalNameEn
    }
}

enum class ObservanceType(
    val tamilName: String,
    val englishName: String,
    val hindiName: String,
    val iconEmoji: String,
    val isMajor: Boolean = false
) {
    AMAVASAI("அமாவாசை", "Amavasai (New Moon)", "अमावस्या (Amavasya)", "🌑", true),
    POURNAMI("பௌர்ணமி", "Pournami (Full Moon)", "पूर्णिमा (Purnima)", "🌕", true),
    PRADOSHAM("பிரதோஷம்", "Pradosham", "प्रदोष व्रत (Pradosham)", "🔱", true),
    EKADASHI("ஏகாதசி", "Ekadashi", "एकादशी व्रत (Ekadashi)", "✨", true),
    SANKATAHARA_CHATURTHI("சங்கடஹர சதுர்த்தி", "Sankatahara Chaturthi", "संकष्टी चतुर्थी (Sankashti)", "🐘", true),
    SHUKLA_SHASHTI("சஷ்டி விரதம்", "Shukla Shashti", "स्कन्द षष्ठी व्रत (Sashti)", "🦚", true),
    KRITTIKAI("கிருத்திகை", "Krittikai", "कृत्तिका नक्षत्रम् (Krittikai)", "🔥", true),
    MONTHLY_SHIVARATRI("மாத சிவராத்திரி", "Monthly Shivaratri", "मासिक शिवरात्रि (Shivaratri)", "🕉️", true),
    CHATURTHI("சதுர்த்தி", "Chaturthi", "विनायक चतुर्थी (Chaturthi)", "🌸"),
    ASHTAMI("அஷ்டமி", "Ashtami", "दुर्गाष्टमी (Ashtami)", "⚡"),
    NAVAMI("நவமி", "Navami", "श्री राम नवमी (Navami)", "🏹"),
    FESTIVAL("சிறப்பு திருவிழா", "Temple Festival", "मंदिर उत्सव (Temple Festival)", "🪔", true),
    TIRUVONAM("திருவோணம்", "Tiruvonam", "श्रवणम् / तिरुवोणम्", "🐚"),
    BHARANI_DEEPAM("பரணி தீபம்", "Bharani Deepam", "भरणी दीपम् (Bharani Deepam)", "🪔");

    fun getDisplayName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> tamilName
        AppLanguage.HINDI -> hindiName
        AppLanguage.ENGLISH -> englishName
    }
}
