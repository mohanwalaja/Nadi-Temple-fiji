package com.example.data.model

enum class PoruthamStatus(
    val nameTa: String,
    val nameEn: String,
    val nameHi: String,
    val score: Double
) {
    UTTHAMAM("உத்தமம்", "Auspicious (Good)", "उत्तम (श्रेष्ठ)", 1.0),
    MADHYAMAM("மத்திமம்", "Average (Moderate)", "मध्यम", 0.5),
    PORUNDHADHU("பொருந்தாது", "Not Compatible", "अयोग्य", 0.0);

    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
}

enum class RajjuType(
    val nameTa: String,
    val nameEn: String,
    val nameHi: String,
    val significanceTa: String,
    val significanceEn: String,
    val significanceHi: String
) {
    SIRO("சிரோ ரஜ்ஜு (தலை)", "Siro Rajju (Head)", "शिरो रज्जु (सिर)", "கணவருக்கு ஆயுள் பலம்", "Longevity of husband", "पति की दीर्घायु"),
    KANDA("கண்ட ரஜ்ஜு (கழுத்து)", "Kanda Rajju (Neck)", "कंठ रज्जु (गला)", "மனைவிக்கு மாங்கல்ய பலம்", "Mangalya protection for wife", "पत्नी का अखंड सौभाग्य"),
    UDHARA("உதர ரஜ்ஜு (வயிறு)", "Udhara / Kati Rajju (Stomach)", "उदर रज्जु (नाभि/पेट)", "புத்திர பாக்கியம்", "Progeny and children", "संतान वृद्धि"),
    URU("ஊரு ரஜ்ஜு (தொடை)", "Uru Rajju (Thighs)", "ऊरु रज्जु (जांघ)", "தன தான்ய விருத்தி மற்றும் செல்வம்", "Wealth and prosperity", "धन-धान्य समृद्धि"),
    PADA("பாத ரஜ்ஜு (பாதம்)", "Pada Rajju (Feet)", "पाद रज्जु (पैर)", "பயண சௌக்கியம் மற்றும் அமைதி", "Domestic peace & travel harmony", "गृह शांति एवं सुख");

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

data class SinglePoruthamResult(
    val id: String,
    val nameTa: String,
    val nameEn: String,
    val nameHi: String,
    val status: PoruthamStatus,
    val pointsEarned: Double,
    val maxPoints: Double,
    val explanationTa: String,
    val explanationEn: String,
    val explanationHi: String = explanationEn,
    val isCrucial: Boolean = false // e.g. Rajju, Dina, Rasi
) {
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
    fun getExplanation(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> explanationTa
        AppLanguage.HINDI -> explanationHi
        AppLanguage.ENGLISH -> explanationEn
    }
}

data class SevvayDoshamAnalysis(
    val isBrideHasDosham: Boolean,
    val isGroomHasDosham: Boolean,
    val brideDoshamSeverity: String, // "தோஷம் இல்லை" / "மிதமான தோஷம்" / "தீவிர தோஷம்"
    val groomDoshamSeverity: String,
    val brideDoshamSeverityEn: String = brideDoshamSeverity,
    val groomDoshamSeverityEn: String = groomDoshamSeverity,
    val brideDoshamSeverityHi: String = brideDoshamSeverityEn,
    val groomDoshamSeverityHi: String = groomDoshamSeverityEn,
    val brideCancellationReasonTa: String?,
    val groomCancellationReasonTa: String?,
    val brideCancellationReasonEn: String?,
    val groomCancellationReasonEn: String?,
    val brideCancellationReasonHi: String? = brideCancellationReasonEn,
    val groomCancellationReasonHi: String? = groomCancellationReasonEn,
    val doshaSamyamStatusTa: String, // தோஷ சாம்யம் உண்டு / இருவருக்கும் பொருந்தும்
    val doshaSamyamStatusEn: String,
    val doshaSamyamStatusHi: String,
    val recommendationTa: String,
    val recommendationEn: String,
    val recommendationHi: String
) {
    fun getBrideDoshamSeverity(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> brideDoshamSeverity
        AppLanguage.HINDI -> brideDoshamSeverityHi
        AppLanguage.ENGLISH -> brideDoshamSeverityEn
    }

    fun getGroomDoshamSeverity(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> groomDoshamSeverity
        AppLanguage.HINDI -> groomDoshamSeverityHi
        AppLanguage.ENGLISH -> groomDoshamSeverityEn
    }

    fun getBrideCancellationReason(lang: AppLanguage): String? = when (lang) {
        AppLanguage.TAMIL -> brideCancellationReasonTa
        AppLanguage.HINDI -> brideCancellationReasonHi
        AppLanguage.ENGLISH -> brideCancellationReasonEn
    }

    fun getGroomCancellationReason(lang: AppLanguage): String? = when (lang) {
        AppLanguage.TAMIL -> groomCancellationReasonTa
        AppLanguage.HINDI -> groomCancellationReasonHi
        AppLanguage.ENGLISH -> groomCancellationReasonEn
    }
}

data class WeddingMatchResult(
    val brideRasi: Rasi,
    val brideNakshatram: String,
    val bridePada: Int,
    val groomRasi: Rasi,
    val groomNakshatram: String,
    val groomPada: Int,
    val poruthams: List<SinglePoruthamResult>,
    val totalPoruthamsMatched: Int, // e.g. 8 out of 10
    val totalScore: Double,
    val maxScore: Double,
    val overallVerdictTa: String,
    val overallVerdictEn: String,
    val overallVerdictHi: String,
    val verdictStatus: PoruthamStatus,
    val rajjuMatch: Boolean,
    val sevvayDosham: SevvayDoshamAnalysis,
    val brideNakshatramEn: String = brideNakshatram,
    val brideNakshatramHi: String = brideNakshatram,
    val groomNakshatramEn: String = groomNakshatram,
    val groomNakshatramHi: String = groomNakshatram
) {
    fun getBrideNakshatram(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> brideNakshatram
        AppLanguage.HINDI -> brideNakshatramHi
        AppLanguage.ENGLISH -> brideNakshatramEn
    }

    fun getGroomNakshatram(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> groomNakshatram
        AppLanguage.HINDI -> groomNakshatramHi
        AppLanguage.ENGLISH -> groomNakshatramEn
    }

    fun getOverallVerdict(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> overallVerdictTa
        AppLanguage.HINDI -> overallVerdictHi
        AppLanguage.ENGLISH -> overallVerdictEn
    }
}

