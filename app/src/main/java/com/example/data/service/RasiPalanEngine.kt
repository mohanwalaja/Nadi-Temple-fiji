package com.example.data.service

import com.example.data.model.*
import java.time.LocalDate

/**
 * Astrology Engine for generating personalized Rasi Palan predictions
 * based directly on the user's calculated Jathagam (Janma Rasi, Nakshatram,
 * Planetary Positions, Dasha, and Gochara Transits).
 */
object RasiPalanEngine {

    // Current Ephemeris planetary transit positions (Epoch 2026/2027)
    private val CURRENT_GURU_RASI = Rasi.MITHUNAM  // Guru (Jupiter) in Mithunam
    private val CURRENT_SANI_RASI = Rasi.MEENAM    // Sani (Saturn) in Meenam
    private val CURRENT_RAHU_RASI = Rasi.KUMBAM    // Rahu in Kumbham
    private val CURRENT_KETU_RASI = Rasi.SIMHAM    // Ketu in Simham
    private val CURRENT_SURYA_RASI = Rasi.SIMHAM   // Sun in Simham (Avani month)

    fun generate(
        jathagam: HoroscopeResult,
        period: PalanTimeframe
    ): RasiPalanResult {
        val rasi = jathagam.janmaRasi
        val nakshatra = jathagam.janmaNakshatram
        val pada = jathagam.janmaPada

        // Calculate Gochara (Transit) house distances from Janma Rasi (1-12)
        val guruHouse = ((CURRENT_GURU_RASI.index - rasi.index + 12) % 12) + 1
        val saniHouse = ((CURRENT_SANI_RASI.index - rasi.index + 12) % 12) + 1
        val rahuHouse = ((CURRENT_RAHU_RASI.index - rasi.index + 12) % 12) + 1
        val ketuHouse = ((CURRENT_KETU_RASI.index - rasi.index + 12) % 12) + 1

        val (lordTa, lordEn, lordHi) = getRasiLordNames(rasi)
        val symbol = getRasiSymbol(rasi)

        // Find active Dasha if available
        val now = LocalDate.now()
        val activeDasha = jathagam.dashaPeriods.firstOrNull {
            !now.isBefore(it.startDate) && !now.isAfter(it.endDate)
        } ?: jathagam.dashaPeriods.firstOrNull()

        val dashaInfluenceTa = if (activeDasha != null) {
            "நடப்பு திசை: ${activeDasha.descriptionTa}. ஜாதக கிரக அமைப்பின்படி சுப பலன்கள் அதிகரிக்கும்."
        } else ""
        val dashaInfluenceEn = if (activeDasha != null) {
            "Active Dasha: ${activeDasha.descriptionEn}. Auspicious energy amplified based on natal chart."
        } else ""
        val dashaInfluenceHi = if (activeDasha != null) {
            "सक्रिय महादशा: ${activeDasha.mahadashaLord.nameHi} महादशा। जन्मकुंडली के ग्रहों के अनुसार शुभ फलों में वृद्धि।"
        } else ""

        // Transits summary
        val transitSummary = generateTransitSummary(guruHouse, saniHouse, rahuHouse, ketuHouse, rasi)

        // Aspect details based on period and transit positions
        val aspects = generateAspectsForJathagam(
            rasi = rasi,
            period = period,
            guruHouse = guruHouse,
            saniHouse = saniHouse,
            rahuHouse = rahuHouse,
            ketuHouse = ketuHouse,
            nakshatra = nakshatra,
            saniStatus = jathagam.saniStatus
        )

        return RasiPalanResult(
            rasi = rasi,
            period = period,
            janmaNakshatram = nakshatra,
            janmaPada = pada,
            rasiLordTa = lordTa,
            rasiLordEn = lordEn,
            rasiLordHi = lordHi,
            rasiSymbol = symbol,
            planetaryTransitsSummaryTa = transitSummary.first,
            planetaryTransitsSummaryEn = transitSummary.second,
            planetaryTransitsSummaryHi = transitSummary.third,
            generalTa = aspects.generalTa,
            generalEn = aspects.generalEn,
            generalHi = aspects.generalHi,
            moneyTa = aspects.moneyTa,
            moneyEn = aspects.moneyEn,
            moneyHi = aspects.moneyHi,
            careerTa = aspects.careerTa,
            careerEn = aspects.careerEn,
            careerHi = aspects.careerHi,
            educationTa = aspects.educationTa,
            educationEn = aspects.educationEn,
            educationHi = aspects.educationHi,
            familyTa = aspects.familyTa,
            familyEn = aspects.familyEn,
            familyHi = aspects.familyHi,
            marriageTa = aspects.marriageTa,
            marriageEn = aspects.marriageEn,
            marriageHi = aspects.marriageHi,
            healthTa = aspects.healthTa,
            healthEn = aspects.healthEn,
            healthHi = aspects.healthHi,
            travelTa = aspects.travelTa,
            travelEn = aspects.travelEn,
            travelHi = aspects.travelHi,
            favourablePeriodsTa = aspects.favourablePeriodsTa,
            favourablePeriodsEn = aspects.favourablePeriodsEn,
            favourablePeriodsHi = aspects.favourablePeriodsHi,
            cautionPeriodsTa = aspects.cautionPeriodsTa,
            cautionPeriodsEn = aspects.cautionPeriodsEn,
            cautionPeriodsHi = aspects.cautionPeriodsHi,
            pariharamTa = aspects.pariharamTa,
            pariharamEn = aspects.pariharamEn,
            pariharamHi = aspects.pariharamHi,
            luckyNumber = aspects.luckyNumber,
            luckyColorTa = aspects.luckyColorTa,
            luckyColorEn = aspects.luckyColorEn,
            luckyColorHi = aspects.luckyColorHi,
            dashaInfluenceTa = dashaInfluenceTa,
            dashaInfluenceEn = dashaInfluenceEn,
            dashaInfluenceHi = dashaInfluenceHi,
            periodYearLabel = when (period) {
                PalanTimeframe.DAILY -> "இன்று (Today / आज)"
                PalanTimeframe.WEEKLY -> "இந்த வாரம் (This Week / इस सप्ताह)"
                PalanTimeframe.MONTHLY -> "இந்த மாதம் (This Month / इस माह)"
                PalanTimeframe.YEARLY -> "2026–2027 (ஸ்ரீ குரோதி / விசுவாவசு)"
            }
        )
    }

    fun generateForRasi(
        rasi: Rasi,
        period: PalanTimeframe
    ): RasiPalanResult {
        val guruHouse = ((CURRENT_GURU_RASI.index - rasi.index + 12) % 12) + 1
        val saniHouse = ((CURRENT_SANI_RASI.index - rasi.index + 12) % 12) + 1
        val rahuHouse = ((CURRENT_RAHU_RASI.index - rasi.index + 12) % 12) + 1
        val ketuHouse = ((CURRENT_KETU_RASI.index - rasi.index + 12) % 12) + 1

        val (lordTa, lordEn, lordHi) = getRasiLordNames(rasi)
        val symbol = getRasiSymbol(rasi)
        val transitSummary = generateTransitSummary(guruHouse, saniHouse, rahuHouse, ketuHouse, rasi)

        val saniStatus = SaniTransitStatus(
            isEzharaiSani = saniHouse in listOf(12, 1, 2),
            ezharaiTypeTa = when (saniHouse) {
                12 -> "விரய சனி (12-ஆம் இடம்)"
                1 -> "ஜென்ம சனி (1-ஆம் இடம்)"
                2 -> "பாத சனி (2-ஆம் இடம்)"
                else -> "இல்லை"
            },
            ezharaiTypeEn = when (saniHouse) {
                12 -> "Viraya Sani (12th House)"
                1 -> "Jenma Sani (1st House)"
                2 -> "Patha Sani (2nd House)"
                else -> "None"
            },
            isAshtamaSani = saniHouse == 8,
            isKandakaSani = saniHouse in listOf(4, 7, 10),
            remedyTa = "சனிக்கிழமை தோறும் ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயிலில் சனீஸ்வரர் வழிபாடு மற்றும் நற்பணிகள் சிறந்தது.",
            remedyEn = "Perform Saturday prayers and good deeds for peace and planetary blessings."
        )

        val aspects = generateAspectsForJathagam(
            rasi = rasi,
            period = period,
            guruHouse = guruHouse,
            saniHouse = saniHouse,
            rahuHouse = rahuHouse,
            ketuHouse = ketuHouse,
            nakshatra = "ராசி பொது",
            saniStatus = saniStatus
        )

        return RasiPalanResult(
            rasi = rasi,
            period = period,
            janmaNakshatram = "ராசி பலன்",
            janmaPada = 1,
            rasiLordTa = lordTa,
            rasiLordEn = lordEn,
            rasiLordHi = lordHi,
            rasiSymbol = symbol,
            planetaryTransitsSummaryTa = transitSummary.first,
            planetaryTransitsSummaryEn = transitSummary.second,
            planetaryTransitsSummaryHi = transitSummary.third,
            generalTa = aspects.generalTa,
            generalEn = aspects.generalEn,
            generalHi = aspects.generalHi,
            moneyTa = aspects.moneyTa,
            moneyEn = aspects.moneyEn,
            moneyHi = aspects.moneyHi,
            careerTa = aspects.careerTa,
            careerEn = aspects.careerEn,
            careerHi = aspects.careerHi,
            educationTa = aspects.educationTa,
            educationEn = aspects.educationEn,
            educationHi = aspects.educationHi,
            familyTa = aspects.familyTa,
            familyEn = aspects.familyEn,
            familyHi = aspects.familyHi,
            marriageTa = aspects.marriageTa,
            marriageEn = aspects.marriageEn,
            marriageHi = aspects.marriageHi,
            healthTa = aspects.healthTa,
            healthEn = aspects.healthEn,
            healthHi = aspects.healthHi,
            travelTa = aspects.travelTa,
            travelEn = aspects.travelEn,
            travelHi = aspects.travelHi,
            favourablePeriodsTa = aspects.favourablePeriodsTa,
            favourablePeriodsEn = aspects.favourablePeriodsEn,
            favourablePeriodsHi = aspects.favourablePeriodsHi,
            cautionPeriodsTa = aspects.cautionPeriodsTa,
            cautionPeriodsEn = aspects.cautionPeriodsEn,
            cautionPeriodsHi = aspects.cautionPeriodsHi,
            pariharamTa = aspects.pariharamTa,
            pariharamEn = aspects.pariharamEn,
            pariharamHi = aspects.pariharamHi,
            luckyNumber = aspects.luckyNumber,
            luckyColorTa = aspects.luckyColorTa,
            luckyColorEn = aspects.luckyColorEn,
            luckyColorHi = aspects.luckyColorHi,
            dashaInfluenceTa = "கோச்சார கிரக நிலைகளின் அடிப்படையிலான பலன்கள்",
            dashaInfluenceEn = "Planetary predictions based on current transit alignment",
            dashaInfluenceHi = "गोचर ग्रह स्थिति अनुसार फल",
            periodYearLabel = when (period) {
                PalanTimeframe.DAILY -> "இன்று (Today / आज)"
                PalanTimeframe.WEEKLY -> "இந்த வாரம் (This Week / इस सप्ताह)"
                PalanTimeframe.MONTHLY -> "இந்த மாதம் (This Month / इस माह)"
                PalanTimeframe.YEARLY -> "2026–2027 (ஸ்ரீ குரோதி / விசுவாவசு)"
            }
        )
    }

    private fun getRasiLordNames(rasi: Rasi): Triple<String, String, String> = when (rasi) {
        Rasi.MESHAM, Rasi.VIRUCHIGAM -> Triple("செவ்வாய் (Mars)", "Mars (Sevvai)", "मंगल (Mars)")
        Rasi.RISHABAM, Rasi.THULAM -> Triple("சுக்கிரன் (Venus)", "Venus (Sukra)", "शुक्र (Venus)")
        Rasi.MITHUNAM, Rasi.KANNI -> Triple("புதன் (Mercury)", "Mercury (Budha)", "बुध (Mercury)")
        Rasi.KADAGAM -> Triple("சந்திரன் (Moon)", "Moon (Chandra)", "चंद्र (Moon)")
        Rasi.SIMHAM -> Triple("சூரியன் (Sun)", "Sun (Surya)", "सूर्य (Sun)")
        Rasi.DHANUSU, Rasi.MEENAM -> Triple("குரு (Jupiter)", "Jupiter (Guru)", "बृहस्पति / गुरु (Jupiter)")
        Rasi.MAGARAM, Rasi.KUMBAM -> Triple("சனி (Saturn)", "Saturn (Sani)", "शनि (Saturn)")
    }

    private fun getRasiSymbol(rasi: Rasi): String = when (rasi) {
        Rasi.MESHAM -> "♈"
        Rasi.RISHABAM -> "♉"
        Rasi.MITHUNAM -> "♊"
        Rasi.KADAGAM -> "♋"
        Rasi.SIMHAM -> "♌"
        Rasi.KANNI -> "♍"
        Rasi.THULAM -> "♎"
        Rasi.VIRUCHIGAM -> "♏"
        Rasi.DHANUSU -> "♐"
        Rasi.MAGARAM -> "♑"
        Rasi.KUMBAM -> "♒"
        Rasi.MEENAM -> "♓"
    }

    private fun generateTransitSummary(
        guruHouse: Int,
        saniHouse: Int,
        rahuHouse: Int,
        ketuHouse: Int,
        rasi: Rasi
    ): Triple<String, String, String> {
        val guruStatusTa = if (guruHouse in listOf(2, 5, 7, 9, 11)) "குரு பகவான் $guruHouse-ஆம் இடத்தில் சுப பலம் (Benefic)" else "குரு பகவான் $guruHouse-ஆம் இட சஞ்சாரம்"
        val saniStatusTa = when (saniHouse) {
            12 -> "விரய சனி (12-ஆம் இடம் - ஏழரை முதல் கட்டம்)"
            1 -> "ஜென்ம சனி (1-ஆம் இடம் - ஏழரை இரண்டாம் கட்டம்)"
            2 -> "பாத சனி (2-ஆம் இடம் - ஏழரை மூன்றாம் கட்டம்)"
            8 -> "அஷ்டம சனி (8-ஆம் இடம்)"
            4 -> "அர்த்தாஷ்டம சனி (4-ஆம் இடம்)"
            3, 6, 11 -> "சனி $saniHouse-ஆம் இடத்தில் வெற்றி & தன பலம் (Auspicious)"
            else -> "சனி பகவான் $saniHouse-ஆம் இட சஞ்சாரம்"
        }

        val guruStatusEn = if (guruHouse in listOf(2, 5, 7, 9, 11)) "Jupiter in ${guruHouse}th House (Benefic)" else "Jupiter in ${guruHouse}th House"
        val saniStatusEn = when (saniHouse) {
            12 -> "Viraya Sani (12th House - 7.5 Sani Phase 1)"
            1 -> "Jenma Sani (1st House - 7.5 Sani Peak Phase)"
            2 -> "Patha Sani (2nd House - 7.5 Sani Concluding Phase)"
            8 -> "Ashtama Sani (8th House Transit)"
            4 -> "Ardhashtama Sani (4th House Transit)"
            3, 6, 11 -> "Saturn in ${saniHouse}th House (Favorable)"
            else -> "Saturn in ${saniHouse}th House"
        }

        val guruStatusHi = if (guruHouse in listOf(2, 5, 7, 9, 11)) "गुरु ${guruHouse}वें भाव में शुभ फलदायक" else "गुरु ${guruHouse}वें भाव में गोचर"
        val saniStatusHi = when (saniHouse) {
            12 -> "द्वादश शनि (साढ़े साती प्रथम चरण)"
            1 -> "जन्म शनि (साढ़े साती मध्य चरण)"
            2 -> "द्वितीय शनि (साढ़े साती अंतिम चरण)"
            8 -> "अष्टम शनि गोचर"
            4 -> "चतुर्थ शनि (कंटक/अर्धाष्टम शनि)"
            3, 6, 11 -> "शनि ${saniHouse}वें भाव में अति शुभ"
            else -> "शनि ${saniHouse}वें भाव में गोचर"
        }

        val ta = "$guruStatusTa | $saniStatusTa | ராகு $rahuHouse-ல், கேது $ketuHouse-ல்"
        val en = "$guruStatusEn | $saniStatusEn | Rahu in ${rahuHouse}th, Ketu in ${ketuHouse}th"
        val hi = "$guruStatusHi | $saniStatusHi | राहु ${rahuHouse}वें, केतु ${ketuHouse}वें भाव में"

        return Triple(ta, en, hi)
    }

    private data class AspectOutput(
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
        val luckyColorHi: String
    )

    private fun generateAspectsForJathagam(
        rasi: Rasi,
        period: PalanTimeframe,
        guruHouse: Int,
        saniHouse: Int,
        rahuHouse: Int,
        ketuHouse: Int,
        nakshatra: String,
        saniStatus: SaniTransitStatus
    ): AspectOutput {
        val rasiTa = rasi.nameTa
        val rasiEn = rasi.nameEn
        val rasiHi = rasi.nameHi

        val isGuruBenefic = guruHouse in listOf(2, 5, 7, 9, 11)
        val isSaniBenefic = saniHouse in listOf(3, 6, 11)
        val isEzharai = saniHouse in listOf(12, 1, 2)
        val isAshtama = saniHouse == 8

        // General interpretation
        val generalTa = when (period) {
            PalanTimeframe.DAILY -> "உங்கள் ஜாதக ராசியான $rasiTa ராசி, $nakshatra நட்சத்திரத்திற்கு இன்றைய தினம் சந்திரனின் சுப சஞ்சாரத்தால் மன அமைதியும் புத்துணர்ச்சியும் மேலோங்கும். புதிய பணிகளை தன்னம்பிக்கையுடன் தொடங்கலாம்."
            PalanTimeframe.WEEKLY -> "இந்த வாரம் $rasiTa ராசி அன்பர்களுக்கு கிரக நிலைகள் அனுகூலமாக அமைந்துள்ளன. திட்டமிட்ட சுப முயற்சிகள் வெற்றி பெறும். உறவினர்கள் மற்றும் நண்பர்களின் ஆதரவு கிட்டும்."
            PalanTimeframe.MONTHLY -> "இந்த மாதத்தில் $rasiTa ராசிக்கு குருவின் சுப பார்வையும் கிரகச் சேர்க்கைகளும் சாதகமான பலன்களைத் தரும். பொருளாதார வளர்ச்சி மற்றும் குடும்ப மகிழ்ச்சி உண்டாகும்."
            PalanTimeframe.YEARLY -> if (isGuruBenefic) {
                "2026-2027 வருட பலன்: $rasiTa ராசி அன்பர்களுக்கு குரு பகவான் $guruHouse-ஆம் இடத்தில் சுப பலம் பெற்று விளங்குவதால் காரிய சித்தி, தெய்வ கடாட்சம் மற்றும் சமூகத்தில் கௌரவம் உயரும் ஆண்டாக அமையும்."
            } else if (isEzharai || isAshtama) {
                "2026-2027 வருட பலன்: $rasiTa ராசி அன்பர்களுக்கு சனி பகவானின் சஞ்சாரம் பொறுமையையும் விவேகத்தையும் கோருகிறது. இறை வழிபாடும் திட்டமிட்ட செயல்பாடுகளும் நற்பலன்களைத் தரும்."
            } else {
                "2026-2027 வருட பலன்: $rasiTa ராசி அன்பர்களுக்கு இவ்வாண்டு மிதமான நற்பலன்களையும் தொழில் முன்னேற்றத்தையும் தரும். விடாமுயற்சிக்கு உரிய வெற்றி கிடைக்கும்."
            }
        }

        val generalEn = when (period) {
            PalanTimeframe.DAILY -> "For your calculated Janma Rasi $rasiEn ($nakshatra), today's lunar transit brings mental poise, clarity, and refreshed energy. Favorable for initiating constructive tasks."
            PalanTimeframe.WEEKLY -> "This week favors $rasiEn natives with positive transit alignment. Scheduled endeavors progress smoothly with encouraging support from peers and family."
            PalanTimeframe.MONTHLY -> "Monthly astrological overview indicates progressive trends for $rasiEn. Benefic aspects promote financial stability and domestic peace."
            PalanTimeframe.YEARLY -> if (isGuruBenefic) {
                "Yearly Transit (2026–2027): With Jupiter positioned beneficially in the ${guruHouse}th House from Janma Rasi $rasiEn, this period heralds exceptional divine grace, achievement, and elevated respect."
            } else if (isEzharai || isAshtama) {
                "Yearly Transit (2026–2027): For $rasiEn, Saturn's current transit advises deliberate patience, ethical focus, and diligent temple worship to transform challenges into spiritual strength."
            } else {
                "Yearly Transit (2026–2027): A steady, productive year for $rasiEn with steady professional advancement through consistent perseverance."
            }
        }

        val generalHi = when (period) {
            PalanTimeframe.DAILY -> "आपकी जन्मकुंडली के आधार पर $rasiHi राशि ($nakshatra) के लिए आज का दिन चंद्र गोचर के शुभ प्रभाव से मानसिक शांति और उत्साह लेकर आएगा।"
            PalanTimeframe.WEEKLY -> "इस सप्ताह $rasiHi राशि के जातकों को ग्रह गोचर का अनुकूल सहयोग प्राप्त होगा। योजनाबद्ध कार्यों में सफलता मिलेगी।"
            PalanTimeframe.MONTHLY -> "इस माह $rasiHi राशि के लिए आर्थिक स्थिरता और पारिवारिक सौहार्द के उत्तम योग बन रहे हैं।"
            PalanTimeframe.YEARLY -> if (isGuruBenefic) {
                "वार्षिक राशिफल (2026–2027): गुरु के ${guruHouse}वें भाव में शुभ गोचर से $rasiHi राशि के लिए भाग्योदय, पद-प्रतिष्ठा और दैवीय कृपा में वृद्धि होगी।"
            } else if (isEzharai || isAshtama) {
                "वार्षिक राशिफल (2026–2027): शनि गोचर के प्रभाव से धैर्य, संयम और श्री शिव सुब्रमण्य स्वामी की नियमित आराधना से सभी कार्य निर्विघ्न संपन्न होंगे।"
            } else {
                "वार्षिक राशिफल (2026–2027): $rasiHi राशि के लिए यह वर्ष प्रगतिशील और कर्मक्षेत्र में स्थिरता प्रदान करने वाला रहेगा।"
            }
        }

        // Money & Finance
        val moneyTa = if (guruHouse in listOf(2, 11, 9) || isSaniBenefic) {
            "தன ஸ்தானம் மற்றும் லாப ஸ்தானம் மிக பலமாக உள்ளது. நீண்ட நாட்களாக வராத பழைய பாக்கிகள் வசூலாகும். புதிய சேமிப்பு திட்டங்களில் முதலீடு செய்ய உகந்த காலம்."
        } else {
            "பணவரவு சீராக இருக்கும். ஆடம்பர மற்றும் எதிர்பாராத விரயச் செலவுகளைக் கட்டுப்படுத்துவது நிதி ஸ்திரத்தன்மையை உறுதி செய்யும்."
        }

        val moneyEn = if (guruHouse in listOf(2, 11, 9) || isSaniBenefic) {
            "Strong financial houses (2nd & 11th). Pending dues will be recovered smoothly. Favorable period for sound investments and steady wealth accumulation."
        } else {
            "Steady inflow of funds. Prudent budgeting and curtailing speculative or impulse expenses will maintain optimal financial balance."
        }

        val moneyHi = if (guruHouse in listOf(2, 11, 9) || isSaniBenefic) {
            "धन एवं लाभ भाव अत्यंत मजबूत स्थिति में हैं। रुका हुआ धन वापस मिलेगा और नए निवेश में लाभ के योग हैं।"
        } else {
            "आर्थिक स्थिति सामान्य रहेगी। अनावश्यक खर्चों पर नियंत्रण रखना लाभकारी रहेगा।"
        }

        // Career & Profession
        val careerTa = if (guruHouse in listOf(5, 9, 10, 11) || isSaniBenefic) {
            "உத்தியோகத்தில் பதவி உயர்வு, ஊதிய உயர்வு மற்றும் புதிய பொறுப்புகள் தேடி வரும். தொழில் முனைவோருக்கு புதிய வாடிக்கையாளர்கள் மற்றும் விரிவாக்க வாய்ப்புகள் கிட்டும்."
        } else {
            "பணிபுரியும் இடத்தில் அமைதியாகவும் கவனத்துடனும் செயல்படவும். சக ஊழியர்களிடம் இணக்கமான போக்கை கடைபிடிப்பது நலம் பயக்கும்."
        }

        val careerEn = if (guruHouse in listOf(5, 9, 10, 11) || isSaniBenefic) {
            "Promising career trajectory with promotions, salary increments, and leadership roles. Business ventures encounter expansion opportunities."
        } else {
            "Maintain diplomatic composure at workplace. Fulfilling duties with dedication ensures sustained professional security."
        }

        val careerHi = if (guruHouse in listOf(5, 9, 10, 11) || isSaniBenefic) {
            "कार्यक्षेत्र में पदोन्नति, वेतन वृद्धि और नए अवसरों की प्राप्ति होगी। व्यापार में वृद्धि के उत्तम योग हैं।"
        } else {
            "नौकरी एवं व्यवसाय में संयम और ईमानदारी से कार्य करें, सहकर्मियों का सहयोग बना रहेगा।"
        }

        // Education
        val educationTa = "மாணவர்களுக்கு ஞாபக சக்தியும் கவனக் குவிப்பும் அதிகரிக்கும். போட்டித் தேர்வுகள் மற்றும் உயர் கல்விக்கான முயற்சிகளில் நற்பலன்கள் கிட்டும்."
        val educationEn = "Students will witness high concentration, analytical clarity, and praiseworthy results in competitive exams and higher education."
        val educationHi = "विद्यार्थियों के लिए विद्या और एकाग्रता में वृद्धि होगी। प्रतियोगी परीक्षाओं में सफलता के शुभ संकेत हैं।"

        // Family
        val familyTa = "குடும்பத்தில் சுபகாரிய பேச்சுவார்த்தைகள் கைகூடும். பெரியோர்களின் ஆசிகளும் குடும்ப உறுப்பினர்களிடையே பரஸ்பர அன்பும் அதிகரிக்கும்."
        val familyEn = "Domestic environment reflects harmony. Auspicious events materialize with the hearty blessings of family elders."
        val familyHi = "परिवार में मांगलिक कार्यों की रूपरेखा बनेगी। बड़ों का आशीर्वाद और पारिवारिक सौहार्द बढ़ेगा।"

        // Marriage
        val marriageTa = if (guruHouse in listOf(2, 5, 7, 9, 11)) {
            "திருமண வரன் தேடும் அன்பர்களுக்கு மனதிற்கு பிடித்த நல்ல வரன் அமையும். திருமணமான தம்பதியரிடையே அன்யோன்யமும் மகிழ்ச்சியும் பெருகும்."
        } else {
            "திருமண முயற்சிகள் நிதானமாக நகரும். தம்பதியர் ஒருவருக்கொருவர் விட்டுக் கொடுத்துச் செல்வது அமைதியைத் தரும்."
        }

        val marriageEn = if (guruHouse in listOf(2, 5, 7, 9, 11)) {
            "Auspicious period for matrimonial alliances. Compatible life partner matches emerge. Married couples experience joy and mutual respect."
        } else {
            "Matrimonial proposals progress with measured steps. Mutual understanding strengthens marital harmony."
        }

        val marriageHi = if (guruHouse in listOf(2, 5, 7, 9, 11)) {
            "विवाह योग्य जातकों के लिए उत्तम रिश्ते आएंगे। दांपत्य जीवन में मधुरता और प्रेम बढ़ेगा।"
        } else {
            "विवाह से संबंधित बातचीत में धैर्य रखें। आपसी तालमेल से दांपत्य सुख में वृद्धि होगी।"
        }

        // Health
        val healthTa = if (isEzharai || isAshtama) {
            "உடல் ஆரோக்கியத்தில் கூடுதல் கவனம் தேவை. சரியான நேரத்திற்கு உணவு, போதுமான தூக்கம் மற்றும் யோகா/தியானம் மன அமைதியைத் தரும்."
        } else {
            "ஆரோக்கியம் சீராக இருக்கும். சுறுசுறுப்பும் தேக பலமும் அதிகரிக்கும். சிறு உஷ்ண உபாதைகளைத் தவிர்க்க நீர்ச்சத்து அதிகம் எடுத்துக்கொள்ளவும்."
        }

        val healthEn = if (isEzharai || isAshtama) {
            "Prioritize physical wellness and rest. Wholesome nutrition, hydration, and light meditation will maintain optimal equilibrium."
        } else {
            "General health remains sound with robust vitality. Stay well-hydrated to ward off minor heat-related fatigue."
        }

        val healthHi = if (isEzharai || isAshtama) {
            "स्वास्थ्य का विशेष ध्यान रखें। संतुलित खान-पान और योग-प्राणायाम से लाभ होगा।"
        } else {
            "स्वास्थ्य उत्तम रहेगा। शारीरिक ऊर्जा और ताजगी बनी रहेगी।"
        }

        // Travel
        val travelTa = "திருக்கோயில் ஆன்மீக யாத்திரைகளும் தொழில் நிமித்தமான பயணங்களும் இனிதாகவும் ஆதாயகரமாகவும் அமையும். வெளிநாட்டு முயற்சிகளுக்கு அனுகூலம்."
        val travelEn = "Pilgrimages, sacred temple yatras, and professional journeys yield peace and productive outcomes. Overseas endeavors gain traction."
        val travelHi = "धार्मिक यात्राओं एवं व्यावसायिक यात्राओं से मन प्रसन्न रहेगा। विदेश यात्रा के प्रयासों में अनुकूलता रहेगी।"

        // Favourable Periods & Caution Periods
        val favourablePeriodsTa = when (rasi) {
            Rasi.MESHAM, Rasi.VIRUCHIGAM -> "செவ்வாய், வியாழக்கிழமைகள், வளர்பிறை திரிதியை/சஷ்டி/ஏகாதசி திதிகள்."
            Rasi.RISHABAM, Rasi.THULAM -> "வெள்ளி, புதன்கிழமைகள், வளர்பிறை பஞ்சமி/சப்தமி/பௌர்ணமி திதிகள்."
            Rasi.MITHUNAM, Rasi.KANNI -> "புதன், வெள்ளிக்கிழமைகள், வளர்பிறை துவிதியை/தசமி திதிகள்."
            Rasi.KADAGAM -> "திங்கள், வியாழக்கிழமைகள், ரோகிணி/பூச நட்சத்திர நாட்கள், பௌர்ணமி."
            Rasi.SIMHAM -> "ஞாயிறு, செவ்வாய்க்கிழமைகள், கார்த்திகை/உத்திர நட்சத்திர நாட்கள்."
            Rasi.DHANUSU, Rasi.MEENAM -> "வியாழன், ஞாயிற்றுக்கிழமைகள், புனர்பூசம்/விசாக நட்சத்திர நாட்கள்."
            Rasi.MAGARAM, Rasi.KUMBAM -> "சனி, புதன்கிழமைகள், அவிட்டம்/சதய நட்சத்திர நாட்கள்."
        }

        val favourablePeriodsEn = when (rasi) {
            Rasi.MESHAM, Rasi.VIRUCHIGAM -> "Tuesdays & Thursdays; Waxing Moon Tritiya, Shashti, Ekadashi."
            Rasi.RISHABAM, Rasi.THULAM -> "Fridays & Wednesdays; Waxing Moon Panchami, Saptami, Pournami."
            Rasi.MITHUNAM, Rasi.KANNI -> "Wednesdays & Fridays; Waxing Moon Dvitiya, Dashami."
            Rasi.KADAGAM -> "Mondays & Thursdays; Rohini/Pushya stars, Pournami."
            Rasi.SIMHAM -> "Sundays & Tuesdays; Krittika/Uttara stars."
            Rasi.DHANUSU, Rasi.MEENAM -> "Thursdays & Sundays; Punarvasu/Vishakha stars."
            Rasi.MAGARAM, Rasi.KUMBAM -> "Saturdays & Wednesdays; Dhanishta/Shatabhisha stars."
        }

        val favourablePeriodsHi = when (rasi) {
            Rasi.MESHAM, Rasi.VIRUCHIGAM -> "मंगलवार एवं गुरुवार; शुक्ल पक्ष तृतीया, षष्ठी, एकादशी।"
            Rasi.RISHABAM, Rasi.THULAM -> "शुक्रवार एवं बुधवार; शुक्ल पक्ष पंचमी, सप्तमी, पूर्णिमा।"
            Rasi.MITHUNAM, Rasi.KANNI -> "बुधवार एवं शुक्रवार; शुक्ल पक्ष द्वितीया, दशमी।"
            Rasi.KADAGAM -> "सोमवार एवं गुरुवार; रोहिणी/पुष्य नक्षत्र, पूर्णिमा।"
            Rasi.SIMHAM -> "रविवार एवं मंगलवार; कृत्तिका/उत्तराफाल्गुनी नक्षत्र।"
            Rasi.DHANUSU, Rasi.MEENAM -> "गुरुवार एवं रविवार; पुनर्वसु/विशाखा नक्षत्र।"
            Rasi.MAGARAM, Rasi.KUMBAM -> "शनिवार एवं बुधवार; धनिष्ठा/शतभिषा नक्षत्र।"
        }

        val cautionPeriodsTa = "சந்திராஷ்டம நாட்கள் மற்றும் ராகு காலம், எமகண்ட நேரங்களில் முக்கிய ஒப்பந்தங்கள் மற்றும் விவாதங்களைத் தவிர்க்கவும்."
        val cautionPeriodsEn = "Avoid major binding decisions during Chandrashtama days, Rahu Kalam, and Yamagandam intervals."
        val cautionPeriodsHi = "चंद्राष्टम के दिनों एवं राहुकाल के समय महत्वपूर्ण निर्णय लेने से बचें।"

        // Temple Pariharam
        val pariharamTa = when (rasi) {
            Rasi.MESHAM, Rasi.VIRUCHIGAM -> "நாடி ஸ்ரீ சிவ சுப்பிரமணிய சுவாமிக்கு செவ்வாய்க்கிழமை அல்லது சஷ்டி நாளில் நெய் தீபம் ஏற்றி, செவ்வரளி மாலை சாற்றி வழிபடவும்."
            Rasi.RISHABAM, Rasi.THULAM -> "வெள்ளிக்கிழமைகளில் ஸ்ரீ மீனாட்சி அம்பாளுக்கு குங்கும அர்ச்சனை செய்து வழிபட சகல சவுபாக்கியங்களும் உண்டாகும்."
            Rasi.MITHUNAM, Rasi.KANNI -> "புதன்கிழமைகளில் ஸ்ரீ மகாவிஷ்ணு மற்றும் ஸ்ரீ சிவ சுப்பிரமணியருக்கு துளசி மாலை சாற்றி வழிபடவும்."
            Rasi.KADAGAM -> "திங்கட்கிழமைகளில் ஸ்ரீ சந்திரமௌலீஸ்வரருக்கு பால் அபிஷேகம் செய்து, ஏழை எளியோருக்கு அன்னதானம் வழங்கவும்."
            Rasi.SIMHAM -> "ஞாயிற்றுக்கிழமைகளில் சூரிய நமஸ்காரம் செய்து, ஸ்ரீ சிவபெருமானுக்கு வில்வ இலைகளால் அர்ச்சனை செய்யவும்."
            Rasi.DHANUSU, Rasi.MEENAM -> "வியாழக்கிழமைகளில் தட்சிணாமூர்த்தி பகவானுக்கு கொண்டைக்கடலை மாலை சாற்றி, குரு பகவானை வழிபடவும்."
            Rasi.MAGARAM, Rasi.KUMBAM -> "சனிக்கிழமைகளில் சனீஸ்வர பகவானுக்கு நல்லெண்ணெய் தீபம் ஏற்றி, ஸ்ரீ சிவ சுப்பிரமணிய சுவாமியை மனமுருகி வழிபடவும்."
        }

        val pariharamEn = when (rasi) {
            Rasi.MESHAM, Rasi.VIRUCHIGAM -> "Offer ghee lamps and red flower garlands to Lord Sri Siva Subramaniya Swami at Nadi Temple on Tuesdays or Shashti."
            Rasi.RISHABAM, Rasi.THULAM -> "Perform Kumkum Archana for Goddess Sri Meenakshi Amman on Fridays for prosperity and grace."
            Rasi.MITHUNAM, Rasi.KANNI -> "Offer Tulasi garland to Lord Maha Vishnu and pray to Lord Murugan on Wednesdays."
            Rasi.KADAGAM -> "Perform milk abhishekam to Lord Chandramouleeswara and support food charity (annadhanam) on Mondays."
            Rasi.SIMHAM -> "Perform Surya Namaskar and offer sacred Bilva leaves to Lord Shiva on Sundays."
            Rasi.DHANUSU, Rasi.MEENAM -> "Offer yellow chickpea garland to Lord Dakshinamoorthy and worship Guru on Thursdays."
            Rasi.MAGARAM, Rasi.KUMBAM -> "Light sesame oil lamps for Lord Sani on Saturdays and seek blessings from Lord Sri Siva Subramaniya Swami."
        }

        val pariharamHi = when (rasi) {
            Rasi.MESHAM, Rasi.VIRUCHIGAM -> "नादी श्री शिव सुब्रमण्य स्वामी मंदिर में मंगलवार को घी का दीपक जलाएं और लाल पुष्प अर्पित करें।"
            Rasi.RISHABAM, Rasi.THULAM -> "शुक्रवार को देवी मीनाक्षी अम्मन की कुमकुम अर्चना करें।"
            Rasi.MITHUNAM, Rasi.KANNI -> "बुधवार को भगवान विष्णु और मुरुगन स्वामी को तुलसी माला अर्पित करें।"
            Rasi.KADAGAM -> "सोमवार को भगवान शिव का दुग्धाभिषेक करें और अन्नदान करें।"
            Rasi.SIMHAM -> "रविवार को सूर्य नमस्कार करें और शिवलिंग पर बेलपत्र अर्पित करें।"
            Rasi.DHANUSU, Rasi.MEENAM -> "गुरुवार को भगवान दक्षिणामूर्ति को चने की माला अर्पित कर गुरु वंदना करें।"
            Rasi.MAGARAM, Rasi.KUMBAM -> "शनिवार को शनिदेव को तिल के तेल का दीपक जलाएं और श्री मुरुगन स्वामी की आराधना करें।"
        }

        val luckyNumber = "${(rasi.index * 3) % 9 + 1}, ${(rasi.index * 7) % 9 + 1}"
        val (luckyColorTa, luckyColorEn, luckyColorHi) = when (rasi) {
            Rasi.MESHAM, Rasi.VIRUCHIGAM -> Triple("சிவப்பு, மெரூன்", "Crimson Red, Maroon", "लाल, मैरून")
            Rasi.RISHABAM, Rasi.THULAM -> Triple("வெள்ளை, சந்தனம்", "Pure White, Sandal", "सफेद, चंदन")
            Rasi.MITHUNAM, Rasi.KANNI -> Triple("பச்சை, கிளிப்பச்சை", "Emerald Green, Light Green", "हरा, तोता हरा")
            Rasi.KADAGAM -> Triple("முத்து வெள்ளை, இளம்பச்சை", "Pearl White, Soft Green", "मोती सफेद, हल्का हरा")
            Rasi.SIMHAM -> Triple("பொன்னிறம், காவி", "Golden Amber, Saffron", "सुनहरा, केसरिया")
            Rasi.DHANUSU, Rasi.MEENAM -> Triple("மஞ்சள், தங்கம்", "Turmeric Yellow, Gold", "पीला, स्वर्णिम")
            Rasi.MAGARAM, Rasi.KUMBAM -> Triple("நீலம், கருநீலம்", "Royal Blue, Navy", "नीला, गहरा नीला")
        }

        return AspectOutput(
            generalTa = generalTa,
            generalEn = generalEn,
            generalHi = generalHi,
            moneyTa = moneyTa,
            moneyEn = moneyEn,
            moneyHi = moneyHi,
            careerTa = careerTa,
            careerEn = careerEn,
            careerHi = careerHi,
            educationTa = educationTa,
            educationEn = educationEn,
            educationHi = educationHi,
            familyTa = familyTa,
            familyEn = familyEn,
            familyHi = familyHi,
            marriageTa = marriageTa,
            marriageEn = marriageEn,
            marriageHi = marriageHi,
            healthTa = healthTa,
            healthEn = healthEn,
            healthHi = healthHi,
            travelTa = travelTa,
            travelEn = travelEn,
            travelHi = travelHi,
            favourablePeriodsTa = favourablePeriodsTa,
            favourablePeriodsEn = favourablePeriodsEn,
            favourablePeriodsHi = favourablePeriodsHi,
            cautionPeriodsTa = cautionPeriodsTa,
            cautionPeriodsEn = cautionPeriodsEn,
            cautionPeriodsHi = cautionPeriodsHi,
            pariharamTa = pariharamTa,
            pariharamEn = pariharamEn,
            pariharamHi = pariharamHi,
            luckyNumber = luckyNumber,
            luckyColorTa = luckyColorTa,
            luckyColorEn = luckyColorEn,
            luckyColorHi = luckyColorHi
        )
    }
}
