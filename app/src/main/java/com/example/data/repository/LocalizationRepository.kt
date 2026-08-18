package com.example.data.repository

import com.example.data.model.AppLanguage

object AppStrings {
    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில்"
        AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी मंदिर, नादी"
        AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Kovil"
    }

    fun appSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "திருக்கோயில் • பஞ்சாங்கம் • ஜாதகம்"
        AppLanguage.HINDI -> "मंदिर • पंचांग • कुंडली"
        AppLanguage.ENGLISH -> "Temple • Panchangam • Horoscope"
    }

    fun templeTag(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "முருகன் திருத்தலம்"
        AppLanguage.HINDI -> "भगवान मुरुगन मंदिर"
        AppLanguage.ENGLISH -> "LORD MURUGAN TEMPLE"
    }

    fun deityName(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "வள்ளி தெய்வானை சமேத ஸ்ரீ சுப்பிரமணியர்"
        AppLanguage.HINDI -> "माता वल्ली एवं देवसेना सहित श्री सुब्रमण्यम"
        AppLanguage.ENGLISH -> "Lord Murugan with Valli & Deivayanai"
    }

    fun home(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "முகப்பு"
        AppLanguage.HINDI -> "मुख्य पृष्ठ"
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
        AppLanguage.ENGLISH -> "Horoscope"
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
        AppLanguage.HINDI -> "मंदिर खुला है"
        AppLanguage.ENGLISH -> "Temple is OPEN"
    }

    fun templeStatusClosed(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "திருக்கோயில் நடை சாத்தப்பட்டுள்ளது"
        AppLanguage.HINDI -> "मंदिर बंद है"
        AppLanguage.ENGLISH -> "Temple is CLOSED"
    }

    fun morningArti(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "காலை ஆரத்தி"
        AppLanguage.HINDI -> "प्रातः आरती"
        AppLanguage.ENGLISH -> "Morning Aarti"
    }

    fun eveningArti(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "மாலை ஆரத்தி"
        AppLanguage.HINDI -> "संध्या आरती"
        AppLanguage.ENGLISH -> "Evening Aarti"
    }

    fun todayFestival(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "இன்றைய விசேஷம் மற்றும் விரதம்"
        AppLanguage.HINDI -> "आज का पर्व एवं व्रत"
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

    fun nallaNeram(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "நல்ல நேரம்"
        AppLanguage.HINDI -> "शुभ समय"
        AppLanguage.ENGLISH -> "Auspicious Time"
    }

    fun gowriNallaNeram(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "கௌரி நல்ல நேரம்"
        AppLanguage.HINDI -> "गौरी शुभ समय"
        AppLanguage.ENGLISH -> "Gowri Nalla Neram"
    }

    fun viewDetails(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "முழு விவரம்"
        AppLanguage.HINDI -> "विस्तृत विवरण"
        AppLanguage.ENGLISH -> "View Details"
    }

    fun call(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "தொடர்பு கொள்ள"
        AppLanguage.HINDI -> "संपर्क करें"
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

    fun quickServices(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "விரைவு சேவைகள்"
        AppLanguage.HINDI -> "त्वरित सेवाएं"
        AppLanguage.ENGLISH -> "Quick Services"
    }

    fun upcomingFestivals(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "வரவிருக்கும் திருவிழாக்கள் & விசேஷங்கள்"
        AppLanguage.HINDI -> "आगामी पर्व एवं विशेष उत्सव"
        AppLanguage.ENGLISH -> "Upcoming Festivals & Events"
    }

    fun todayPanchangamSummary(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "இன்றைய பஞ்சாங்க சுருக்கம்"
        AppLanguage.HINDI -> "आज का संक्षिप्त पंचांग"
        AppLanguage.ENGLISH -> "Today's Panchangam Summary"
    }

    fun auspiciousTimings(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "சுப நேரங்கள்"
        AppLanguage.HINDI -> "शुभ मुहूर्त समय"
        AppLanguage.ENGLISH -> "Auspicious Timings"
    }

    fun inauspiciousTimings(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "அசுப நேரங்கள்"
        AppLanguage.HINDI -> "अशुभ समय"
        AppLanguage.ENGLISH -> "Inauspicious Timings"
    }

    fun solarLunarDetails(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "சூரிய & சந்திர நிலைகள்"
        AppLanguage.HINDI -> "सूर्य एवं चन्द्र स्थितियां"
        AppLanguage.ENGLISH -> "Sun & Moon Timings"
    }

    fun demoLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "மாதிரி கணிப்பு"
        AppLanguage.HINDI -> "डेमो गणना"
        AppLanguage.ENGLISH -> "Demo Engine"
    }

    fun viewAll(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "அனைத்தும்"
        AppLanguage.HINDI -> "सभी देखें"
        AppLanguage.ENGLISH -> "View All"
    }

    fun readMore(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "படிக்க"
        AppLanguage.HINDI -> "पढ़ें"
        AppLanguage.ENGLISH -> "Read More"
    }

    fun transitAlertTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "2026 முக்கிய கிரக எச்சரிக்கை"
        AppLanguage.HINDI -> "2026 महत्वपूर्ण ग्रह गोचर अलर्ट"
        AppLanguage.ENGLISH -> "2026 Planetary Transit Alert"
    }

    fun transitAlertSummary(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "🔴 மிக முக்கிய கிரக எச்சரிக்கை: மேஷம் (சனி), கடகம் (ராகு/கேது), சிம்மம் (ராகு/கேது), மகரம் (ராகு/சனி), கும்பம் (சனி/கேது), மீனம் (சனி/கேது). 12 ராசிகளுக்கான விரிவான பலன்கள் மற்றும் பரிகாரங்கள்."
        AppLanguage.HINDI -> "🔴 मुख्य ग्रह अलर्ट: मेष (शनि), कर्क (राहु/केतु), सिंह (राहु/केतु), मकर (राहु/शनि), कुंभ (शनि/केतु), मीन (शनि/केतु)। सभी 12 राशियों के लिए विस्तृत फल एवं उपाय।"
        AppLanguage.ENGLISH -> "🔴 Critical Planetary Alert: Aries (Saturn), Cancer (Rahu/Ketu), Leo (Rahu/Ketu), Capricorn (Rahu/Saturn), Aquarius (Saturn/Ketu), Pisces (Saturn/Ketu). Complete 12 Rasi predictions & remedies."
    }

    fun transitCategories(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "வேலை • நிதி • திருமணம் • உடல்நலம்"
        AppLanguage.HINDI -> "करियर • वित्त • विवाह • स्वास्थ्य"
        AppLanguage.ENGLISH -> "Career • Finance • Marriage • Health"
    }

    fun templeClosingTime(lang: AppLanguage, time: String) = when (lang) {
        AppLanguage.TAMIL -> "இன்று நடை சாத்துதல்: $time"
        AppLanguage.HINDI -> "आज पट बंद होने का समय: $time"
        AppLanguage.ENGLISH -> "Closes today: $time"
    }

    fun monthPrefix(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "மாதம்"
        AppLanguage.HINDI -> "माह"
        AppLanguage.ENGLISH -> "Month"
    }

    fun dayTimingHeader(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "இன்றைய சுப மற்றும் கால நேரங்கள்"
        AppLanguage.HINDI -> "आज के शुभ एवं अशुभ समय"
        AppLanguage.ENGLISH -> "Today's Auspicious & Inauspicious Timings"
    }

    fun nallaNeramLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.TAMIL -> "நல்ல நேரம்"
        AppLanguage.HINDI -> "शुभ समय"
        AppLanguage.ENGLISH -> "Auspicious Time"
    }
}

