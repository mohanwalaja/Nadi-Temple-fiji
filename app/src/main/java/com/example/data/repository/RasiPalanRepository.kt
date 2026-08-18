package com.example.data.repository

import com.example.data.model.*
import com.example.data.service.RasiPalanEngine

class RasiPalanRepository {

    fun generateFromJathagam(jathagam: HoroscopeResult, timeframe: PalanTimeframe): RasiPalanResult {
        return RasiPalanEngine.generate(jathagam, timeframe)
    }

    fun getPalan(rasi: Rasi, timeframe: PalanTimeframe): SingleRasiPalan {

        val (periodTa, periodEn) = when (timeframe) {
            PalanTimeframe.DAILY -> "இன்றைய நாள் பலன்" to "Today's Planetary Transit"
            PalanTimeframe.WEEKLY -> "இந்த வார பலன்" to "Weekly Transit Predictions"
            PalanTimeframe.MONTHLY -> "இந்த மாத ராசி பலன்" to "Monthly Astrological Overview"
            PalanTimeframe.YEARLY -> "ஸ்ரீ குரோதி வருட பலன் (2024-2025/2026)" to "Sri Krodhi Tamil New Year Predictions"
        }

        val aspects = generateAspectsForRasi(rasi, timeframe)
        return SingleRasiPalan(
            rasi = rasi,
            timeframe = timeframe,
            periodLabelTa = periodTa,
            periodLabelEn = periodEn,
            aspects = aspects
        )
    }

    private fun generateAspectsForRasi(rasi: Rasi, timeframe: PalanTimeframe): RasiPalanAspects {
        val rasiName = rasi.nameTa
        val rasiEn = rasi.nameEn

        return RasiPalanAspects(
            generalTa = "$rasiName ராசி அன்பர்களுக்கு இந்த காலகட்டத்தில் தெய்வ அனுகூலமும் தன்னம்பிக்கையும் கூடும். எடுத்த காரியங்களில் விவேகத்துடன் செயல்பட்டால் நற்பலன்கள் கிட்டும்.",
            generalEn = "For $rasiEn natives, this period enhances self-confidence and spiritual grace. Deliberate, mindful actions will yield favorable outcomes.",
            moneyTa = "தன ஸ்தானம் பலமாக உள்ளதால் எதிர்பார்த்த பணவரவு தடையின்றி வந்து சேரும். தேவையற்ற ஆடம்பர செலவுகளைக் குறைப்பது சேமிப்பை உயர்த்தும்.",
            moneyEn = "Financial inflow remains steady with positive gains. Prudent budgeting and curtailing impulse expenses will build long-term savings.",
            careerTa = "தொழில் மற்றும் உத்தியோகத்தில் புதிய பொறுப்புகள் தேடி வரும். உயர் அதிகாரிகளின் நன்மதிப்பும் சக ஊழியர்களின் ஒத்துழைப்பும் கிட்டும்.",
            careerEn = "Promising career advancement with expanded responsibilities. Positive recognition from leadership and supportive team cooperation.",
            educationTa = "மாணவ கண்மணிகளுக்கு கல்வியில் ஈடுபாடும் ஞாபக சக்தியும் அதிகரிக்கும். போட்டித் தேர்வுகளில் நல்ல மதிப்பெண் பெற்று தேர்ச்சி பெறுவர்.",
            educationEn = "Students will experience sharp focus, improved memory retention, and notable success in competitive assessments.",
            familyTa = "குடும்பத்தில் சுபகாரிய பேச்சுவார்த்தைகள் நல்ல முறையில் முடிவடையும். பெரியோர்களின் ஆசியும் வழிகாட்டுதலும் கிடைக்கும்.",
            familyEn = "Harmonious domestic environment with auspicious family discussions proceeding smoothly under elders' blessings.",
            marriageTa = "திருமணம் மற்றும் வாழ்க்கைத் துணை தேடும் முயற்சிகளில் அனுகூலமான தகவல்கள் வந்து சேரும். தம்பதியரிடையே அன்யோன்யம் பெருகும்.",
            marriageEn = "Matrimonial proposals progress affirmatively. Married couples experience mutual understanding and deeper companionship.",
            healthTa = "உடல் நலம் சீராக இருக்கும். சரியான உணவுப் பழக்கமும் நடைபயிற்சியும் மன அழுத்தத்தைக் குறைக்கும். உஷ்ண சம்பந்தமான உபாதைகளைத் தவிர்க்கவும்.",
            healthEn = "Vitality remains sound. Balanced nutrition, adequate hydration, and light physical routines will ensure sustained wellness.",
            travelTa = "ஆன்மீக யாத்திரைகளும் தொழில் சார்ந்த குறுகிய பயணங்களும் மனதிற்கு மகிழ்ச்சியையும் ஆதாயத்தையும் தரும்.",
            travelEn = "Pilgrimage journeys and professional excursions will prove spiritually uplifting and commercially productive.",
            foreignTa = "வெளிநாடு சென்று கல்வி கற்கவும் பணிபுரியவும் எடுக்கும் முயற்சிகளில் விசா மற்றும் அனுமதிகள் சுமூகமாக கிட்டும்.",
            foreignEn = "Overseas travel, immigration procedures, and global professional aspirations meet with encouraging developments.",
            favourableTa = "வளர்பிறை நாட்கள், திங்கட்கிழமை மற்றும் வியாழக்கிழமைகள், உத்திர மற்றும் பூச நட்சத்திர நாட்கள்.",
            favourableEn = "Waxing moon days, Mondays and Thursdays, during auspicious transit hours.",
            cautionTa = "ராகு காலம், எமகண்ட நேரங்களில் அவசர முடிவுகளைத் தவிர்ப்பது நலம் தரும்.",
            cautionEn = "Exercise caution during Rahu Kalam and avoid hasty contractual commitments.",
            luckyNumber = "${(rasi.index * 3) % 9 + 1}, ${(rasi.index * 7) % 9 + 1}",
            luckyColorTa = when (rasi) {
                Rasi.MESHAM, Rasi.VIRUCHIGAM -> "சிவப்பு, மெரூன்"
                Rasi.RISHABAM, Rasi.THULAM -> "வெள்ளை, சந்தனம்"
                Rasi.MITHUNAM, Rasi.KANNI -> "பச்சை, கிளிப்பச்சை"
                Rasi.KADAGAM -> "முத்து வெள்ளை, இளம்பச்சை"
                Rasi.SIMHAM -> "பொன்னிறம், காவி"
                Rasi.DHANUSU, Rasi.MEENAM -> "மஞ்சள், தங்கம்"
                Rasi.MAGARAM, Rasi.KUMBAM -> "நீலம், கருநீலம்"
            },
            luckyColorEn = when (rasi) {
                Rasi.MESHAM, Rasi.VIRUCHIGAM -> "Crimson Red, Maroon"
                Rasi.RISHABAM, Rasi.THULAM -> "Pure White, Sandal"
                Rasi.MITHUNAM, Rasi.KANNI -> "Emerald Green, Light Green"
                Rasi.KADAGAM -> "Pearl White, Soft Green"
                Rasi.SIMHAM -> "Golden Amber, Saffron"
                Rasi.DHANUSU, Rasi.MEENAM -> "Turmeric Yellow, Gold"
                Rasi.MAGARAM, Rasi.KUMBAM -> "Royal Blue, Navy"
            },
            pariharamTa = "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமிக்கு செவ்வாய் அல்லது சஷ்டி நாளில் நெய் தீபம் ஏற்றி வழிபடுவது சகல நன்மைகளையும் தரும்.",
            pariharamEn = "Light a pure ghee lamp for Lord Sri Siva Subramaniya Swami on Tuesdays or Shashti days for enduring peace and success."
        )
    }
}
