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
    val significanceTa: String,
    val significanceEn: String
) {
    SIRO("சிரோ ரஜ்ஜு (தலை)", "Siro Rajju (Head)", "கணவருக்கு ஆயுள் பலம்", "Longevity of husband"),
    KANDA("கண்ட ரஜ்ஜு (கழுத்து)", "Kanda Rajju (Neck)", "மனைவிக்கு மாங்கல்ய பலம்", "Mangalya protection for wife"),
    UDHARA("உதர ரஜ்ஜு (வயிறு)", "Udhara / Kati Rajju (Stomach)", "புத்திர பாக்கியம்", "Progeny and children"),
    URU("ஊரு ரஜ்ஜு (தொடை)", "Uru Rajju (Thighs)", "தன தான்ய விருத்தி மற்றும் செல்வம்", "Wealth and prosperity"),
    PADA("பாத ரஜ்ஜு (பாதம்)", "Pada Rajju (Feet)", "பயண சௌக்கியம் மற்றும் அமைதி", "Domestic peace & travel harmony")
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
    val brideCancellationReasonTa: String?,
    val groomCancellationReasonTa: String?,
    val brideCancellationReasonEn: String?,
    val groomCancellationReasonEn: String?,
    val doshaSamyamStatusTa: String, // தோஷ சாம்யம் உண்டு / இருவருக்கும் பொருந்தும்
    val doshaSamyamStatusEn: String,
    val doshaSamyamStatusHi: String,
    val recommendationTa: String,
    val recommendationEn: String,
    val recommendationHi: String
)

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
    val sevvayDosham: SevvayDoshamAnalysis
)
