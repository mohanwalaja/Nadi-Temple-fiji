package com.example.data.model

data class SastraTopic(
    val id: String,
    val titleTa: String,
    val titleEn: String,
    val summaryTa: String,
    val summaryEn: String,
    val detailedTextTa: String,
    val detailedTextEn: String,
    val daysCountTa: String,
    val daysCountEn: String,
    val sourceTexts: List<String>,
    val iconEmoji: String,
    val titleHi: String = titleEn,
    val summaryHi: String = summaryEn,
    val detailedTextHi: String = detailedTextEn,
    val daysCountHi: String = daysCountEn
) {
    fun getTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> titleTa
        AppLanguage.HINDI -> titleHi
        AppLanguage.ENGLISH -> titleEn
    }
    fun getSummary(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> summaryTa
        AppLanguage.HINDI -> summaryHi
        AppLanguage.ENGLISH -> summaryEn
    }
    fun getDetailedText(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> detailedTextTa
        AppLanguage.HINDI -> detailedTextHi
        AppLanguage.ENGLISH -> detailedTextEn
    }
    fun getDaysCount(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> daysCountTa
        AppLanguage.HINDI -> daysCountHi
        AppLanguage.ENGLISH -> daysCountEn
    }
}

data class DharmaSastraDisclaimer(
    val textTa: String = "பாரம்பரிய தர்ம சாஸ்திர விதிகள் குடும்ப ஆசாரம், சம்பிரதாயம், குல வழக்கம் மற்றும் சூழ்நிலைக்கு ஏற்ப மாறுபடலாம். சந்தேகங்களுக்கு உங்கள் குடும்ப புரோகிதர் அல்லது ஆசார்யரை அணுகவும்.",
    val textEn: String = "Traditional Dharma Sastra practices may vary according to sampradaya, family achara, regional tradition, and circumstances. Please consult your family priest or acharya for specific observance.",
    val textHi: String = "पारंपरिक धर्म शास्त्र नियम पारिवारिक आचार, संप्रदाय, कुल परम्परा और परिस्थितियों के अनुसार भिन्न हो सकते हैं। विशिष्ट मार्गदर्शन के लिए कृपया अपने कुलगुरु या पुरोहित से परामर्श लें।"
) {
    fun get(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> textTa
        AppLanguage.HINDI -> textHi
        AppLanguage.ENGLISH -> textEn
    }
}
