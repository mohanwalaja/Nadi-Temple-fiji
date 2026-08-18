package com.example.data.model

enum class PalanTimeframe(
    val id: String,
    val nameTa: String,
    val nameEn: String,
    val nameHi: String = nameEn
) {
    DAILY("daily", "இன்றைய பலன்", "Daily Palan", "दैनिक राशिफल"),
    WEEKLY("weekly", "வார பலன்", "Weekly Palan", "साप्ताहिक राशिफल"),
    MONTHLY("monthly", "மாத பலன்", "Monthly Palan", "मासिक राशिफल"),
    YEARLY("yearly", "வருட பலன் (குரோதி)", "Yearly Palan", "वार्षिक राशिफल");

    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
}

data class RasiPalanAspects(
    val generalTa: String,
    val generalEn: String,
    val moneyTa: String,
    val moneyEn: String,
    val careerTa: String,
    val careerEn: String,
    val educationTa: String,
    val educationEn: String,
    val familyTa: String,
    val familyEn: String,
    val marriageTa: String,
    val marriageEn: String,
    val healthTa: String,
    val healthEn: String,
    val travelTa: String,
    val travelEn: String,
    val foreignTa: String,
    val foreignEn: String,
    val favourableTa: String,
    val favourableEn: String,
    val cautionTa: String,
    val cautionEn: String,
    val luckyNumber: String,
    val luckyColorTa: String,
    val luckyColorEn: String,
    val pariharamTa: String,
    val pariharamEn: String,
    val generalHi: String = generalEn,
    val moneyHi: String = moneyEn,
    val careerHi: String = careerEn,
    val educationHi: String = educationEn,
    val familyHi: String = familyEn,
    val marriageHi: String = marriageEn,
    val healthHi: String = healthEn,
    val travelHi: String = travelEn,
    val foreignHi: String = foreignEn,
    val favourableHi: String = favourableEn,
    val cautionHi: String = cautionEn,
    val luckyColorHi: String = luckyColorEn,
    val pariharamHi: String = pariharamEn
) {
    fun getGeneral(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> generalTa; AppLanguage.HINDI -> generalHi; AppLanguage.ENGLISH -> generalEn }
    fun getMoney(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> moneyTa; AppLanguage.HINDI -> moneyHi; AppLanguage.ENGLISH -> moneyEn }
    fun getCareer(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> careerTa; AppLanguage.HINDI -> careerHi; AppLanguage.ENGLISH -> careerEn }
    fun getEducation(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> educationTa; AppLanguage.HINDI -> educationHi; AppLanguage.ENGLISH -> educationEn }
    fun getFamily(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> familyTa; AppLanguage.HINDI -> familyHi; AppLanguage.ENGLISH -> familyEn }
    fun getMarriage(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> marriageTa; AppLanguage.HINDI -> marriageHi; AppLanguage.ENGLISH -> marriageEn }
    fun getHealth(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> healthTa; AppLanguage.HINDI -> healthHi; AppLanguage.ENGLISH -> healthEn }
    fun getTravel(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> travelTa; AppLanguage.HINDI -> travelHi; AppLanguage.ENGLISH -> travelEn }
    fun getForeign(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> foreignTa; AppLanguage.HINDI -> foreignHi; AppLanguage.ENGLISH -> foreignEn }
    fun getFavourable(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> favourableTa; AppLanguage.HINDI -> favourableHi; AppLanguage.ENGLISH -> favourableEn }
    fun getCaution(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> cautionTa; AppLanguage.HINDI -> cautionHi; AppLanguage.ENGLISH -> cautionEn }
    fun getLuckyColor(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> luckyColorTa; AppLanguage.HINDI -> luckyColorHi; AppLanguage.ENGLISH -> luckyColorEn }
    fun getPariharam(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> pariharamTa; AppLanguage.HINDI -> pariharamHi; AppLanguage.ENGLISH -> pariharamEn }
}

data class SingleRasiPalan(
    val rasi: Rasi,
    val timeframe: PalanTimeframe,
    val periodLabelTa: String,
    val periodLabelEn: String,
    val aspects: RasiPalanAspects,
    val periodLabelHi: String = periodLabelEn
) {
    fun getPeriodLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> periodLabelTa
        AppLanguage.HINDI -> periodLabelHi
        AppLanguage.ENGLISH -> periodLabelEn
    }
}

data class RasiPalanResult(
    val rasi: Rasi,
    val period: PalanTimeframe,
    val janmaNakshatram: String,
    val janmaPada: Int,
    val rasiLordTa: String,
    val rasiLordEn: String,
    val rasiLordHi: String,
    val rasiSymbol: String,
    val planetaryTransitsSummaryTa: String,
    val planetaryTransitsSummaryEn: String,
    val planetaryTransitsSummaryHi: String,
    val generalTa: String,
    val generalEn: String,
    val generalHi: String,
    val moneyTa: String,
    val moneyEn: String,
    val moneyHi: String,
    val careerTa: String,
    val careerEn: String,
    val careerHi: String,
    val educationTa: String,
    val educationEn: String,
    val educationHi: String,
    val familyTa: String,
    val familyEn: String,
    val familyHi: String,
    val marriageTa: String,
    val marriageEn: String,
    val marriageHi: String,
    val healthTa: String,
    val healthEn: String,
    val healthHi: String,
    val travelTa: String,
    val travelEn: String,
    val travelHi: String,
    val favourablePeriodsTa: String,
    val favourablePeriodsEn: String,
    val favourablePeriodsHi: String,
    val cautionPeriodsTa: String,
    val cautionPeriodsEn: String,
    val cautionPeriodsHi: String,
    val pariharamTa: String,
    val pariharamEn: String,
    val pariharamHi: String,
    val luckyNumber: String,
    val luckyColorTa: String,
    val luckyColorEn: String,
    val luckyColorHi: String,
    val dashaInfluenceTa: String = "",
    val dashaInfluenceEn: String = "",
    val dashaInfluenceHi: String = "",
    val periodYearLabel: String = "2026–2027",
    val elementTa: String = "",
    val elementEn: String = "",
    val elementHi: String = "",
    val qualityTa: String = "",
    val qualityEn: String = "",
    val qualityHi: String = "",
    val nakshatrasTa: String = "",
    val nakshatrasEn: String = "",
    val nakshatrasHi: String = "",
    val luckyGemstoneTa: String = "",
    val luckyGemstoneEn: String = "",
    val luckyGemstoneHi: String = "",
    val luckyDirectionTa: String = "",
    val luckyDirectionEn: String = "",
    val luckyDirectionHi: String = "",
    val luckyDaysTa: String = "",
    val luckyDaysEn: String = "",
    val luckyDaysHi: String = ""
) {
    fun getGeneral(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> generalTa; AppLanguage.HINDI -> generalHi; AppLanguage.ENGLISH -> generalEn }
    fun getMoney(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> moneyTa; AppLanguage.HINDI -> moneyHi; AppLanguage.ENGLISH -> moneyEn }
    fun getCareer(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> careerTa; AppLanguage.HINDI -> careerHi; AppLanguage.ENGLISH -> careerEn }
    fun getEducation(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> educationTa; AppLanguage.HINDI -> educationHi; AppLanguage.ENGLISH -> educationEn }
    fun getFamily(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> familyTa; AppLanguage.HINDI -> familyHi; AppLanguage.ENGLISH -> familyEn }
    fun getMarriage(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> marriageTa; AppLanguage.HINDI -> marriageHi; AppLanguage.ENGLISH -> marriageEn }
    fun getHealth(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> healthTa; AppLanguage.HINDI -> healthHi; AppLanguage.ENGLISH -> healthEn }
    fun getTravel(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> travelTa; AppLanguage.HINDI -> travelHi; AppLanguage.ENGLISH -> travelEn }
    fun getFavourablePeriods(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> favourablePeriodsTa; AppLanguage.HINDI -> favourablePeriodsHi; AppLanguage.ENGLISH -> favourablePeriodsEn }
    fun getCautionPeriods(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> cautionPeriodsTa; AppLanguage.HINDI -> cautionPeriodsHi; AppLanguage.ENGLISH -> cautionPeriodsEn }
    fun getPariharam(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> pariharamTa; AppLanguage.HINDI -> pariharamHi; AppLanguage.ENGLISH -> pariharamEn }
    fun getLuckyColor(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> luckyColorTa; AppLanguage.HINDI -> luckyColorHi; AppLanguage.ENGLISH -> luckyColorEn }
    fun getPlanetaryTransitsSummary(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> planetaryTransitsSummaryTa; AppLanguage.HINDI -> planetaryTransitsSummaryHi; AppLanguage.ENGLISH -> planetaryTransitsSummaryEn }
    fun getRasiLord(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> rasiLordTa; AppLanguage.HINDI -> rasiLordHi; AppLanguage.ENGLISH -> rasiLordEn }
    fun getDashaInfluence(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> dashaInfluenceTa; AppLanguage.HINDI -> dashaInfluenceHi; AppLanguage.ENGLISH -> dashaInfluenceEn }
    fun getElement(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> elementTa; AppLanguage.HINDI -> elementHi; AppLanguage.ENGLISH -> elementEn }
    fun getQuality(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> qualityTa; AppLanguage.HINDI -> qualityHi; AppLanguage.ENGLISH -> qualityEn }
    fun getNakshatras(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> nakshatrasTa; AppLanguage.HINDI -> nakshatrasHi; AppLanguage.ENGLISH -> nakshatrasEn }
    fun getLuckyGemstone(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> luckyGemstoneTa; AppLanguage.HINDI -> luckyGemstoneHi; AppLanguage.ENGLISH -> luckyGemstoneEn }
    fun getLuckyDirection(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> luckyDirectionTa; AppLanguage.HINDI -> luckyDirectionHi; AppLanguage.ENGLISH -> luckyDirectionEn }
    fun getLuckyDays(lang: AppLanguage): String = when (lang) { AppLanguage.TAMIL -> luckyDaysTa; AppLanguage.HINDI -> luckyDaysHi; AppLanguage.ENGLISH -> luckyDaysEn }
}

