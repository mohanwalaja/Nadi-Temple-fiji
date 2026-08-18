package com.example.data.model

import java.time.LocalDate
import java.time.LocalTime

enum class Rasi(
    val index: Int,
    val nameTa: String,
    val nameEn: String,
    val lordTa: String,
    val lordEn: String,
    val symbolEmoji: String,
    val nameHi: String = nameEn,
    val lordHi: String = lordEn
) {
    MESHAM(1, "மேஷம்", "Mesham (Aries)", "செவ்வாய்", "Mars", "♈", "मेष (Mesha)", "मंगल (Mars)"),
    RISHABAM(2, "ரிஷபம்", "Rishabam (Taurus)", "சுக்கிரன்", "Venus", "♉", "वृषभ (Vrishabha)", "शुक्र (Venus)"),
    MITHUNAM(3, "மிதுனம்", "Mithunam (Gemini)", "புதன்", "Mercury", "♊", "मिथुन (Mithuna)", "बुध (Mercury)"),
    KADAGAM(4, "கடகம்", "Kadagam (Cancer)", "சந்திரன்", "Moon", "♋", "कर्क (Karka)", "चन्द्र (Moon)"),
    SIMHAM(5, "சிம்மம்", "Simham (Leo)", "சூரியன்", "Sun", "♌", "सिंह (Simha)", "सूर्य (Sun)"),
    KANNI(6, "கன்னி", "Kanni (Virgo)", "புதன்", "Mercury", "♍", "कन्या (Kanya)", "बुध (Mercury)"),
    THULAM(7, "துலாம்", "Thulam (Libra)", "சுக்கிரன்", "Venus", "♎", "तुला (Tula)", "शुक्र (Venus)"),
    VIRUCHIGAM(8, "விருச்சிகம்", "Viruchigam (Scorpio)", "செவ்வாய்", "Mars", "♏", "वृश्चिक (Vrishchika)", "मंगल (Mars)"),
    DHANUSU(9, "தனுசு", "Dhanusu (Sagittarius)", "குரு", "Jupiter", "♐", "धनु (Dhanu)", "बृहस्पति / गुरु (Jupiter)"),
    MAGARAM(10, "மகரம்", "Magaram (Capricorn)", "சனி", "Saturn", "♑", "मकर (Makara)", "शनि (Saturn)"),
    KUMBAM(11, "கும்பம்", "Kumbham (Aquarius)", "சனி", "Saturn", "♒", "कुम्भ (Kumbha)", "शनि (Saturn)"),
    MEENAM(12, "மீனம்", "Meenam (Pisces)", "குரு", "Jupiter", "♓", "मीन (Meena)", "बृहस्पति / गुरु (Jupiter)");

    val symbol: String get() = symbolEmoji
    val lord: String get() = lordTa
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
    fun getLord(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> lordTa
        AppLanguage.HINDI -> lordHi
        AppLanguage.ENGLISH -> lordEn
    }
}

enum class Graha(
    val id: String,
    val nameTa: String,
    val nameEn: String,
    val shortTa: String,
    val shortEn: String,
    val nameHi: String = nameEn,
    val shortHi: String = shortEn
) {
    SURYA("sun", "சூரியன்", "Sun (Surya)", "சூரி", "Su", "सूर्य (Surya)", "सू"),
    CHANDRA("moon", "சந்திரன்", "Moon (Chandra)", "சந்", "Mo", "चन्द्र (Chandra)", "च"),
    CHEVVAI("mars", "செவ்வாய்", "Mars (Mangal)", "செவ்", "Ma", "मंगल (Mangal)", "मं"),
    BUDHA("mercury", "புதன்", "Mercury (Budha)", "புத", "Me", "बुध (Budha)", "बु"),
    GURU("jupiter", "குரு", "Jupiter (Guru)", "குரு", "Ju", "बृहस्पति (Guru)", "गु"),
    SUKRA("venus", "சுக்கிரன்", "Venus (Sukra)", "சுக்", "Ve", "शुक्र (Shukra)", "शु"),
    SANI("saturn", "சனி", "Saturn (Sani)", "சனி", "Sa", "शनि (Shani)", "श"),
    RAHU("rahu", "ராகு", "Rahu", "ராகு", "Ra", "राहु (Rahu)", "रा"),
    KETU("ketu", "கேது", "Ketu", "கேது", "Ke", "केतु (Ketu)", "के");

    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
}

data class PlanetPosition(
    val graha: Graha,
    val rasi: Rasi,
    val degrees: Double, // 0.0 - 30.0 inside rasi
    val nakshatram: String,
    val pada: Int,
    val isRetrograde: Boolean = false,
    val isCombust: Boolean = false,
    val bhavaNumber: Int = 1
)

data class BhavaDetail(
    val number: Int, // 1 to 12
    val nameTa: String,
    val nameEn: String,
    val rasi: Rasi,
    val significanceTa: String,
    val significanceEn: String,
    val occupantGrahas: List<Graha> = emptyList(),
    val nameHi: String = nameEn,
    val significanceHi: String = significanceEn
) {
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
    fun getSignificance(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> significanceTa
        AppLanguage.HINDI -> significanceHi
        AppLanguage.ENGLISH -> significanceEn
    }
}

data class DashaPeriod(
    val mahadashaLord: Graha,
    val antardashaLord: Graha,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val descriptionTa: String,
    val descriptionEn: String,
    val descriptionHi: String = descriptionEn
) {
    fun getDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> descriptionTa
        AppLanguage.HINDI -> descriptionHi
        AppLanguage.ENGLISH -> descriptionEn
    }
}

data class SaniTransitStatus(
    val isEzharaiSani: Boolean,
    val ezharaiTypeTa: String, // விரய சனி, ஜென்ம சனி, பாத சனி
    val ezharaiTypeEn: String, // Viraya Sani (12th), Jenma Sani (1st), Patha Sani (2nd)
    val isAshtamaSani: Boolean, // 8th house Sani
    val isKandakaSani: Boolean, // 4th, 7th, 10th Sani
    val remedyTa: String,
    val remedyEn: String,
    val ezharaiTypeHi: String = ezharaiTypeEn,
    val remedyHi: String = remedyEn
) {
    fun getEzharaiType(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> ezharaiTypeTa
        AppLanguage.HINDI -> ezharaiTypeHi
        AppLanguage.ENGLISH -> ezharaiTypeEn
    }
    fun getRemedy(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> remedyTa
        AppLanguage.HINDI -> remedyHi
        AppLanguage.ENGLISH -> remedyEn
    }
}

data class DoshaCheckResult(
    val nameTa: String,
    val nameEn: String,
    val isPresent: Boolean,
    val severityTa: String, // இல்லை / மிதமானது / தீவிரமானது
    val severityEn: String, // None / Moderate / Prominent
    val descriptionTa: String,
    val descriptionEn: String,
    val traditionalRemedyTa: String,
    val traditionalRemedyEn: String,
    val nameHi: String = nameEn,
    val severityHi: String = severityEn,
    val descriptionHi: String = descriptionEn,
    val traditionalRemedyHi: String = traditionalRemedyEn
) {
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
    fun getSeverity(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> severityTa
        AppLanguage.HINDI -> severityHi
        AppLanguage.ENGLISH -> severityEn
    }
    fun getDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> descriptionTa
        AppLanguage.HINDI -> descriptionHi
        AppLanguage.ENGLISH -> descriptionEn
    }
    fun getRemedy(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> traditionalRemedyTa
        AppLanguage.HINDI -> traditionalRemedyHi
        AppLanguage.ENGLISH -> traditionalRemedyEn
    }
}

data class TempleJathagaSummary(
    val healthTa: String,
    val healthEn: String,
    val wealthTa: String,
    val wealthEn: String,
    val educationTa: String,
    val educationEn: String,
    val careerTa: String,
    val careerEn: String,
    val marriageTa: String,
    val marriageEn: String,
    val familyTa: String,
    val familyEn: String,
    val foreignTravelTa: String,
    val foreignTravelEn: String,
    val currentPeriodGuidanceTa: String,
    val currentPeriodGuidanceEn: String,
    val healthHi: String = healthEn,
    val wealthHi: String = wealthEn,
    val educationHi: String = educationEn,
    val careerHi: String = careerEn,
    val marriageHi: String = marriageEn,
    val familyHi: String = familyEn,
    val foreignTravelHi: String = foreignTravelEn,
    val currentPeriodGuidanceHi: String = currentPeriodGuidanceEn,
    val disclaimerTa: String = "பாரம்பரிய ஜோதிட குறிப்பு மட்டுமே. அறிவார்ந்த ஜோதிடர் வழிகாட்டுதலுடன் அறிந்து கொள்ளவும்.",
    val disclaimerEn: String = "Traditional Jyotisha indication only. Requires verified astrologer interpretation.",
    val disclaimerHi: String = "यह केवल पारंपरिक ज्योतिषीय मार्गदर्शन है। पूर्ण फलकथन के लिए योग्य ज्योतिषाचार्य से परामर्श लें।"
) {
    fun getHealth(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> healthTa; AppLanguage.HINDI -> healthHi; AppLanguage.ENGLISH -> healthEn }
    fun getWealth(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> wealthTa; AppLanguage.HINDI -> wealthHi; AppLanguage.ENGLISH -> wealthEn }
    fun getEducation(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> educationTa; AppLanguage.HINDI -> educationHi; AppLanguage.ENGLISH -> educationEn }
    fun getCareer(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> careerTa; AppLanguage.HINDI -> careerHi; AppLanguage.ENGLISH -> careerEn }
    fun getMarriage(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> marriageTa; AppLanguage.HINDI -> marriageHi; AppLanguage.ENGLISH -> marriageEn }
    fun getFamily(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> familyTa; AppLanguage.HINDI -> familyHi; AppLanguage.ENGLISH -> familyEn }
    fun getForeignTravel(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> foreignTravelTa; AppLanguage.HINDI -> foreignTravelHi; AppLanguage.ENGLISH -> foreignTravelEn }
    fun getCurrentPeriodGuidance(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> currentPeriodGuidanceTa; AppLanguage.HINDI -> currentPeriodGuidanceHi; AppLanguage.ENGLISH -> currentPeriodGuidanceEn }
    fun getDisclaimer(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> disclaimerTa; AppLanguage.HINDI -> disclaimerHi; AppLanguage.ENGLISH -> disclaimerEn }
}

data class HoroscopeResult(
    val devoteeName: String,
    val dob: LocalDate,
    val tob: LocalTime,
    val birthPlace: String,
    val lagnaRasi: Rasi,
    val lagnaDegrees: Double,
    val chandraRasi: Rasi,
    val janmaNakshatram: String,
    val janmaPada: Int,
    val planetPositions: List<PlanetPosition>,
    val bhavas: List<BhavaDetail>,
    val navamsaPositions: Map<Graha, Rasi>,
    val dashaPeriods: List<DashaPeriod>,
    val saniStatus: SaniTransitStatus,
    val doshas: List<DoshaCheckResult>,
    val summary: TempleJathagaSummary,
    val isDemoEngine: Boolean = true
) {
    val janmaRasi: Rasi get() = chandraRasi
}

typealias JathagamResult = HoroscopeResult

