package com.example.data.repository

import com.example.data.model.AppLanguage

object AppStrings {
    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில்"
        AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी मंदिर, नादी"
        AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Kovil"
    }

    fun home(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "முகப்பு"
        AppLanguage.HINDI -> "होम"
        AppLanguage.ENGLISH -> "Home"
    }

    fun calendar(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "நாட்காட்டி"
        AppLanguage.HINDI -> "कैलेंडर"
        AppLanguage.ENGLISH -> "Calendar"
    }

    fun panchangam(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "பஞ்சாங்கம்"
        AppLanguage.HINDI -> "पंचांग"
        AppLanguage.ENGLISH -> "Panchangam"
    }

    fun jathagam(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "ஜாதகம்"
        AppLanguage.HINDI -> "कुंडली"
        AppLanguage.ENGLISH -> "Jathagam"
    }

    fun temple(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "திருக்கோயில்"
        AppLanguage.HINDI -> "मंदिर"
        AppLanguage.ENGLISH -> "Temple"
    }

    fun rasiPalan(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "ராசி பலன்"
        AppLanguage.HINDI -> "राशिफल"
        AppLanguage.ENGLISH -> "Rasi Palan"
    }

    fun dharmaSastra(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "தர்ம சாஸ்திரம்"
        AppLanguage.HINDI -> "धर्म शास्त्र"
        AppLanguage.ENGLISH -> "Dharma Sastra"
    }

    fun settings(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "அமைப்புகள்"
        AppLanguage.HINDI -> "सेटिंग्स"
        AppLanguage.ENGLISH -> "Settings"
    }

    fun templeStatusOpen(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "திருக்கோயில் நடை திறந்துள்ளது"
        AppLanguage.HINDI -> "मंदिर खुला है (OPEN)"
        AppLanguage.ENGLISH -> "Temple is OPEN"
    }

    fun templeStatusClosed(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "திருக்கோயில் நடை சாத்தப்பட்டுள்ளது"
        AppLanguage.HINDI -> "मंदिर बंद है (CLOSED)"
        AppLanguage.ENGLISH -> "Temple is CLOSED"
    }

    fun morningArti(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "காலை ஆரத்தி"
        AppLanguage.HINDI -> "प्रातः आरती"
        AppLanguage.ENGLISH -> "Morning Arti"
    }

    fun eveningArti(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "மாலை ஆரத்தி"
        AppLanguage.HINDI -> "संध्या आरती"
        AppLanguage.ENGLISH -> "Evening Arti"
    }

    fun todayFestival(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "இன்றைய விசேஷம் / விரதம்"
        AppLanguage.HINDI -> "आज का पर्व / व्रत"
        AppLanguage.ENGLISH -> "Today's Festival & Observance"
    }

    fun sunrise(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "சூரியோதயம்"
        AppLanguage.HINDI -> "सूर्योदय"
        AppLanguage.ENGLISH -> "Sunrise"
    }

    fun sunset(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "சூரியாஸ்தமனம்"
        AppLanguage.HINDI -> "सूर्यास्त"
        AppLanguage.ENGLISH -> "Sunset"
    }

    fun moonrise(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "சந்திரோதயம்"
        AppLanguage.HINDI -> "चन्द्रोदय"
        AppLanguage.ENGLISH -> "Moonrise"
    }

    fun moonset(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "சந்திராஸ்தமனம்"
        AppLanguage.HINDI -> "चन्द्रास्त"
        AppLanguage.ENGLISH -> "Moonset"
    }

    fun tithi(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "திதி"
        AppLanguage.HINDI -> "तिथि"
        AppLanguage.ENGLISH -> "Tithi"
    }

    fun nakshatram(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "நட்சத்திரம்"
        AppLanguage.HINDI -> "नक्षत्र"
        AppLanguage.ENGLISH -> "Nakshatram"
    }

    fun yogam(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "யோகம்"
        AppLanguage.HINDI -> "योग"
        AppLanguage.ENGLISH -> "Yogam"
    }

    fun karanam(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "கரணம்"
        AppLanguage.HINDI -> "करण"
        AppLanguage.ENGLISH -> "Karanam"
    }

    fun rahuKalam(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "இராகு காலம்"
        AppLanguage.HINDI -> "राहु काल"
        AppLanguage.ENGLISH -> "Rahu Kalam"
    }

    fun yamagandam(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "எமகண்டம்"
        AppLanguage.HINDI -> "यमगंड"
        AppLanguage.ENGLISH -> "Yamagandam"
    }

    fun kuligai(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "குளிகை"
        AppLanguage.HINDI -> "गुलिक काल"
        AppLanguage.ENGLISH -> "Kuligai"
    }

    fun abhijit(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "அபிஜித் முகூர்த்தம்"
        AppLanguage.HINDI -> "अभिजित मुहूर्त"
        AppLanguage.ENGLISH -> "Abhijit Muhurtham"
    }

    fun durMuhurtham(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "துர்முகூர்த்தம்"
        AppLanguage.HINDI -> "दुर्मुहूर्त"
        AppLanguage.ENGLISH -> "Dur Muhurtham"
    }

    fun varjyam(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "வர்ஜ்யம்"
        AppLanguage.HINDI -> "वर्ज्यम्"
        AppLanguage.ENGLISH -> "Varjyam"
    }

    fun viewDetails(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "முழு விவரம்"
        AppLanguage.HINDI -> "विस्तृत विवरण देखें"
        AppLanguage.ENGLISH -> "View Details"
    }

    fun call(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "தொடர்பு கொள்ள"
        AppLanguage.HINDI -> "संपर्क करें (Call)"
        AppLanguage.ENGLISH -> "Call"
    }

    fun saveProfile(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "ஜாதகம் சேமிக்க"
        AppLanguage.HINDI -> "कुंडली सुरक्षित करें"
        AppLanguage.ENGLISH -> "Save Horoscope"
    }

    fun generateJathagam(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "ஜாதகம் கணிக்க"
        AppLanguage.HINDI -> "कुंडली गणना करें"
        AppLanguage.ENGLISH -> "Calculate Horoscope"
    }

    fun savedProfiles(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "சேமிக்கப்பட்ட ஜாதகங்கள்"
        AppLanguage.HINDI -> "सुरक्षित कुंडलियां"
        AppLanguage.ENGLISH -> "Saved Horoscopes"
    }

    fun fijiTimeNotice(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "நாடி, பிஜி தீவுகள் நேரம் (UTC+12:00)"
        AppLanguage.HINDI -> "नादी, फिजी द्वीप समूह समय (UTC+12:00)"
        AppLanguage.ENGLISH -> "Nadi, Fiji Islands Time (UTC+12:00)"
    }

    fun demoLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "மாதிரி கணிப்பு (DEMO)"
        AppLanguage.HINDI -> "डेमो गणना (DEMO)"
        AppLanguage.ENGLISH -> "DEMO ENGINE"
    }
}

