package com.example.data.service

import com.example.data.model.PoruthamStatus
import com.example.data.model.Rasi
import com.example.data.model.RajjuType
import com.example.data.model.SevvayDoshamAnalysis
import com.example.data.model.SinglePoruthamResult
import com.example.data.model.WeddingMatchResult

object MatchMakingCalculator {

    val NAKSHATRAM_NAMES_TA = listOf(
        "அஸ்வினி", "பரணி", "கார்த்திகை", "ரோகிணி", "மிருகசீரிஷம்", "திருவாதிரை",
        "புனர்பூசம்", "பூசம்", "ஆயில்யம்", "மகம்", "பூரம்", "உத்திரம்",
        "அஸ்தம்", "சித்திரை", "சுவாதி", "விசாகம்", "அனுஷம்", "கேட்டை",
        "மூலம்", "பூராடம்", "உத்திராடம்", "திருவோணம்", "அவிட்டம்", "சதயம்",
        "பூரட்டாதி", "உத்திரட்டாதி", "ரேவதி"
    )

    val NAKSHATRAM_NAMES_EN = listOf(
        "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashirsha", "Arudra",
        "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni",
        "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha",
        "Mula", "Purva Ashadha", "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha",
        "Purva Bhadrapada", "Uttara Bhadrapada", "Revati"
    )

    val NAKSHATRAM_NAMES_HI = listOf(
        "अश्विनी", "भरणी", "कृत्तिका", "रोहिणी", "मृगशिरा", "आर्द्रा",
        "पुनर्वसु", "पुष्य", "आश्लेषा", "मघा", "पूर्वाफाल्गुनी", "उत्तराफाल्गुनी",
        "हस्त", "चित्रा", "स्वाती", "विशाखा", "अनुराधा", "ज्येष्ठा",
        "मूल", "पूर्वाषाढ़ा", "उत्तराषाढ़ा", "श्रवण", "धनिष्ठा", "शतभिषा",
        "पूर्वाभाद्रपद", "उत्तराभाद्रपद", "रेवती"
    )

    // Gana: 0 = Deva, 1 = Manushya, 2 = Rakshasa
    private val NAKSHATRA_GANA = listOf(
        0, 1, 2, 1, 0, 1, 0, 0, 2,
        2, 1, 1, 0, 2, 0, 2, 0, 2,
        2, 1, 1, 0, 2, 2, 1, 1, 0
    )

    // Rajju: 0=Siro, 1=Kanda, 2=Udhara, 3=Uru, 4=Pada
    private val NAKSHATRA_RAJJU = listOf(
        4, 3, 2, 1, 0, 0, 1, 2, 3,
        4, 3, 2, 1, 0, 0, 1, 2, 3,
        4, 3, 2, 1, 0, 0, 1, 2, 3
    )

    // Yoni animal types (14 animals)
    // 0=Horse, 1=Elephant, 2=Sheep, 3=Serpent, 4=Dog, 5=Cat, 6=Rat, 7=Cow, 8=Buffalo, 9=Tiger, 10=Deer, 11=Monkey, 12=Mongoose, 13=Lion
    private val NAKSHATRA_YONI = listOf(
        0, 1, 2, 3, 3, 4, 5, 2, 5,
        6, 6, 7, 8, 9, 8, 9, 10, 10,
        4, 11, 12, 11, 13, 0, 13, 7, 1
    )

    // Inimical Yoni pairs
    private val INIMICAL_YONIS = setOf(
        0 to 8, 8 to 0,   // Horse - Buffalo
        1 to 13, 13 to 1, // Elephant - Lion
        2 to 11, 11 to 2, // Sheep - Monkey
        3 to 12, 12 to 3, // Serpent - Mongoose
        4 to 10, 10 to 4, // Dog - Deer
        5 to 6, 6 to 5,   // Cat - Rat
        7 to 9, 9 to 7    // Cow - Tiger
    )

    // Vedha pairs (star index 0-based)
    private val VEDHA_PAIRS = setOf(
        0 to 17, 17 to 0, // Ashwini - Jyeshtha
        1 to 16, 16 to 1, // Bharani - Anuradha
        2 to 15, 15 to 2, // Krittika - Visakha
        3 to 14, 14 to 3, // Rohini - Swati
        5 to 21, 21 to 5, // Arudra - Shravana
        6 to 20, 20 to 6, // Punarvasu - Uthiradam
        7 to 19, 19 to 7, // Pushya - Pooradam
        8 to 18, 18 to 8, // Ashlesha - Mula
        9 to 26, 26 to 9, // Magha - Revati
        10 to 25, 25 to 10, // Pooram - Uthirattadhi
        11 to 24, 24 to 11, // Uthiram - Poorattadhi
        12 to 23, 23 to 12, // Hasta - Shatabhisha
        4 to 13, 13 to 4, 13 to 22, 22 to 13, 4 to 22, 22 to 4 // Mrigashira - Chitra - Dhanishta
    )

    fun calculateWeddingMatch(
        brideRasi: Rasi,
        brideNakshatraIndex: Int, // 0 to 26
        bridePada: Int,
        groomRasi: Rasi,
        groomNakshatraIndex: Int, // 0 to 26
        groomPada: Int,
        brideMarsHouse: Int = 1, // House of Mars from Lagna (1 to 12)
        groomMarsHouse: Int = 1
    ): WeddingMatchResult {

        val bStar = brideNakshatraIndex
        val gStar = groomNakshatraIndex
        val bRasiIdx = brideRasi.index
        val gRasiIdx = groomRasi.index

        val poruthams = mutableListOf<SinglePoruthamResult>()

        // 1. Dinam Porutham (தினப் பொருத்தம்)
        val starDiff = ((gStar - bStar + 27) % 27) + 1
        val dinaRem = starDiff % 9
        val dinaStatus = when {
            dinaRem in listOf(2, 4, 6, 8, 0) || starDiff == 27 -> PoruthamStatus.UTTHAMAM
            bStar == gStar && bStar in listOf(3, 5, 9, 15, 21, 25, 26) -> PoruthamStatus.UTTHAMAM
            bStar == gStar -> PoruthamStatus.MADHYAMAM
            dinaRem in listOf(1, 3, 5, 7) -> PoruthamStatus.PORUNDHADHU
            else -> PoruthamStatus.MADHYAMAM
        }
        poruthams.add(
            SinglePoruthamResult(
                id = "dina",
                nameTa = "தினப் பொருத்தம்",
                nameEn = "Dina Porutham (Longevity & Health)",
                nameHi = "दिन पोरुथम (दीर्घायु एवं स्वास्थ्य)",
                status = dinaStatus,
                pointsEarned = if (dinaStatus == PoruthamStatus.UTTHAMAM) 1.0 else if (dinaStatus == PoruthamStatus.MADHYAMAM) 0.5 else 0.0,
                maxPoints = 1.0,
                explanationTa = if (dinaStatus == PoruthamStatus.UTTHAMAM) "தம்பதியருக்கு நீண்ட ஆயுள், ஆரோக்கியம் மற்றும் குறைவற்ற சுபீட்சம் தரும் நற்பொருத்தம்."
                else if (dinaStatus == PoruthamStatus.MADHYAMAM) "மிதமான பலன் தரும் தின அமைப்பு. பொதுவான சுப விரதங்களால் சுப பலன் கூடும்."
                else "தின நட்சத்திர எண் பொருத்தமில்லாத அமைப்பு. பரிகாரம் அல்லது மற்ற முக்கிய பொருத்தங்கள் நன்று அமைந்திருக்க வேண்டும்.",
                explanationEn = if (dinaStatus == PoruthamStatus.UTTHAMAM) "Highly favorable for marital health, long life, and prosperity."
                else if (dinaStatus == PoruthamStatus.MADHYAMAM) "Moderate health compatibility; generally acceptable."
                else "Unfavorable star distance. Requires support from other primary poruthams.",
                explanationHi = if (dinaStatus == PoruthamStatus.UTTHAMAM) "दंपति के दीर्घायु, उत्तम स्वास्थ्य एवं शारीरिक सुख के लिए अत्यंत शुभ।"
                else if (dinaStatus == PoruthamStatus.MADHYAMAM) "मध्यम स्वास्थ्य अनुकूलता; सामान्यतः ग्राह्य।"
                else "नक्षत्र दूरी प्रतिकूल है; अन्य मुख्य गुणों का समर्थन आवश्यक है।",
                isCrucial = true
            )
        )

        // 2. Gana Porutham (கணப் பொருத்தம்)
        val bGana = NAKSHATRA_GANA[bStar]
        val gGana = NAKSHATRA_GANA[gStar]
        val ganaStatus = when {
            bGana == gGana -> PoruthamStatus.UTTHAMAM
            gGana == 0 && bGana == 1 -> PoruthamStatus.UTTHAMAM // Deva groom + Manushya bride
            gGana == 1 && bGana == 0 -> PoruthamStatus.MADHYAMAM // Manushya groom + Deva bride
            gGana == 2 && bGana == 0 && starDiff > 14 -> PoruthamStatus.MADHYAMAM
            bGana == 2 && gGana != 2 -> PoruthamStatus.PORUNDHADHU // Rakshasa bride with Deva/Manushya groom
            else -> PoruthamStatus.PORUNDHADHU
        }
        val ganaNamesTa = listOf("தேவ கணம்", "மனுஷ்ய கணம்", "ராட்சச கணம்")
        val ganaNamesEn = listOf("Deva Gana", "Manushya Gana", "Rakshasa Gana")
        val ganaNamesHi = listOf("देव गण", "मनुष्य गण", "राक्षस गण")
        poruthams.add(
            SinglePoruthamResult(
                id = "gana",
                nameTa = "கணப் பொருத்தம்",
                nameEn = "Gana Porutham (Temperament & Harmony)",
                nameHi = "गण पोरुथम (स्वभाव एवं सामंजस्य)",
                status = ganaStatus,
                pointsEarned = if (ganaStatus == PoruthamStatus.UTTHAMAM) 1.0 else if (ganaStatus == PoruthamStatus.MADHYAMAM) 0.5 else 0.0,
                maxPoints = 1.0,
                explanationTa = "பெண்: ${ganaNamesTa[bGana]}, ஆண்: ${ganaNamesTa[gGana]}. " +
                        if (ganaStatus == PoruthamStatus.UTTHAMAM) "இருவரின் எண்ணம், ரசனை மற்றும் குணாதிசயங்கள் ஒற்றுமையுடன் இணையும் உத்தமப் பொருத்தம்."
                        else if (ganaStatus == PoruthamStatus.MADHYAMAM) "சுமாரான மன ஒற்றுமை; ஒருவருக்கொருவர் விட்டுக் கொடுத்து நடப்பது நலம்."
                        else "மாறுபட்ட குணாதிசய அமைப்பைக் குறிக்கிறது.",
                explanationEn = "Bride: ${ganaNamesEn[bGana]}, Groom: ${ganaNamesEn[gGana]}. " +
                        if (ganaStatus == PoruthamStatus.UTTHAMAM) "Excellent psychological and emotional compatibility."
                        else if (ganaStatus == PoruthamStatus.MADHYAMAM) "Average temperament compatibility."
                        else "Divergent natural temperaments.",
                explanationHi = "वधू: ${ganaNamesHi[bGana]}, वर: ${ganaNamesHi[gGana]}। " +
                        if (ganaStatus == PoruthamStatus.UTTHAMAM) "उत्तम मानसिक अनुकूलता एवं स्वभाव सामंजस्य।"
                        else if (ganaStatus == PoruthamStatus.MADHYAMAM) "मध्यम स्वभाव अनुकूलता; सामंजस्य आवश्यक।"
                        else "भिन्न स्वभाव एवं विचारधारा।"
            )
        )

        // 3. Mahendra Porutham (மகேந்திரப் பொருத்தம்)
        val mahendraStatus = if (starDiff in listOf(4, 7, 10, 13, 16, 19, 22, 25)) PoruthamStatus.UTTHAMAM else PoruthamStatus.PORUNDHADHU
        poruthams.add(
            SinglePoruthamResult(
                id = "mahendra",
                nameTa = "மகேந்திரப் பொருத்தம்",
                nameEn = "Mahendra Porutham (Progeny & Lineage)",
                nameHi = "महेंद्र पोरुथम (संतान एवं वंश वृद्धि)",
                status = mahendraStatus,
                pointsEarned = if (mahendraStatus == PoruthamStatus.UTTHAMAM) 1.0 else 0.0,
                maxPoints = 1.0,
                explanationTa = if (mahendraStatus == PoruthamStatus.UTTHAMAM) "வம்ச விருத்தி, புத்திர பாக்கியம் மற்றும் குடும்ப பொருளாதார மேன்மை தரும் உன்னத பொருத்தம்."
                else "மகேந்திர அமைப்பு அமையவில்லை (மற்ற புத்திர ஸ்தான பலன்கள் மூலம் ஆராயப்பட வேண்டும்).",
                explanationEn = if (mahendraStatus == PoruthamStatus.UTTHAMAM) "Auspicious for offspring, family lineage, and prosperity."
                else "Neutral; progeny is evaluated from 5th house in the birth charts.",
                explanationHi = if (mahendraStatus == PoruthamStatus.UTTHAMAM) "संतान सुख, वंश वृद्धि एवं पारिवारिक समृद्धि प्रदान करने वाला उत्तम योग।"
                else "महेंद्र योग नहीं बन रहा है; कुंडली के पंचम भाव से संतान विचार करें।"
            )
        )

        // 4. Sthree Dheergam (ஸ்திரீ தீர்க்கப் பொருத்தம்)
        val sthreeStatus = when {
            starDiff > 13 -> PoruthamStatus.UTTHAMAM
            starDiff in 7..13 -> PoruthamStatus.MADHYAMAM
            else -> PoruthamStatus.PORUNDHADHU
        }
        poruthams.add(
            SinglePoruthamResult(
                id = "sthree_dheergam",
                nameTa = "ஸ்திரீ தீர்க்கப் பொருத்தம்",
                nameEn = "Stree Deerkha Porutham (Wealth & Longevity)",
                nameHi = "स्त्री दीर्घ पोरुथम (स्त्री सौख्य एवं समृद्धि)",
                status = sthreeStatus,
                pointsEarned = if (sthreeStatus == PoruthamStatus.UTTHAMAM) 1.0 else if (sthreeStatus == PoruthamStatus.MADHYAMAM) 0.5 else 0.0,
                maxPoints = 1.0,
                explanationTa = if (sthreeStatus == PoruthamStatus.UTTHAMAM) "மணப்பெண்ணுக்கு நீண்ட ஆயுள், சந்தோஷம், மாங்கல்ய பாக்கியம் மற்றும் குடும்ப சுபீட்சம் தரும்."
                else if (sthreeStatus == PoruthamStatus.MADHYAMAM) "நடுத்தர ஸ்திரீ தீர்க்க பலன்."
                else "நட்சத்திர இடைவெளி குறைவான அமைப்பு.",
                explanationEn = if (sthreeStatus == PoruthamStatus.UTTHAMAM) "Ensures prosperity, long life, and happiness for the bride."
                else if (sthreeStatus == PoruthamStatus.MADHYAMAM) "Moderate distance compatibility."
                else "Star distance is below classical threshold.",
                explanationHi = if (sthreeStatus == PoruthamStatus.UTTHAMAM) "स्त्री को दीर्घायु, अखंड सौभाग्य एवं पारिवारिक सुख-समृद्धि देने वाला योग।"
                else if (sthreeStatus == PoruthamStatus.MADHYAMAM) "मध्यम अनुकूलता।"
                else "नक्षत्र दूरी कम है।"
            )
        )

        // 5. Yoni Porutham (யோனிப் பொருத்தம்)
        val bYoni = NAKSHATRA_YONI[bStar]
        val gYoni = NAKSHATRA_YONI[gStar]
        val isEnemies = (bYoni to gYoni) in INIMICAL_YONIS
        val yoniStatus = when {
            bYoni == gYoni -> PoruthamStatus.UTTHAMAM
            !isEnemies -> PoruthamStatus.MADHYAMAM
            else -> PoruthamStatus.PORUNDHADHU
        }
        poruthams.add(
            SinglePoruthamResult(
                id = "yoni",
                nameTa = "யோனிப் பொருத்தம்",
                nameEn = "Yoni Porutham (Mutual Attraction & Intimacy)",
                nameHi = "योनि पोरुथम (पारस्परिक आकर्षण एवं सामंजस्य)",
                status = yoniStatus,
                pointsEarned = if (yoniStatus == PoruthamStatus.UTTHAMAM) 1.0 else if (yoniStatus == PoruthamStatus.MADHYAMAM) 0.5 else 0.0,
                maxPoints = 1.0,
                explanationTa = if (yoniStatus == PoruthamStatus.UTTHAMAM) "தம்பதியரிடையே சிறந்த உடல்-மன ஈர்ப்பு, அந்நியோன்யம் மற்றும் தாம்பத்ய சுகம் தரும்."
                else if (yoniStatus == PoruthamStatus.MADHYAMAM) "நட்பு அல்லது சம யோனி அமைப்பு. தாம்பத்ய வாழ்க்கைக்கு ஏற்றது."
                else "பகை யோனி அமைப்பு; பரஸ்பர புரிந்துணர்வு தேவை.",
                explanationEn = if (yoniStatus == PoruthamStatus.UTTHAMAM) "Harmonious physical and emotional intimacy."
                else if (yoniStatus == PoruthamStatus.MADHYAMAM) "Neutral/Friendly biological compatibility."
                else "Inimical animal yoni pair.",
                explanationHi = if (yoniStatus == PoruthamStatus.UTTHAMAM) "दंपति के बीच परस्पर आकर्षण, स्नेह एवं सुखी दांपत्य जीवन प्रदान करता है।"
                else if (yoniStatus == PoruthamStatus.MADHYAMAM) "सामान्य एवं मित्र योनि अनुकूलता।"
                else "शत्रु योनि संबंध; परस्पर समझदारी आवश्यक।"
            )
        )

        // 6. Rasi Porutham (ராசிப் பொருத்தம்)
        val rasiDiff = ((gRasiIdx - bRasiIdx + 12) % 12) + 1
        val rasiStatus = when {
            rasiDiff == 7 -> PoruthamStatus.UTTHAMAM // Sama Sapthama
            rasiDiff in listOf(3, 4, 10, 11, 12) -> PoruthamStatus.UTTHAMAM
            rasiDiff in listOf(1, 2, 5, 9) -> PoruthamStatus.MADHYAMAM
            rasiDiff in listOf(6, 8) -> {
                // Shashtashtaka check - friendly lords exception
                if (brideRasi.lordTa == groomRasi.lordTa || (bRasiIdx in listOf(1, 8) && gRasiIdx in listOf(1, 8))) PoruthamStatus.MADHYAMAM
                else PoruthamStatus.PORUNDHADHU
            }
            else -> PoruthamStatus.MADHYAMAM
        }
        poruthams.add(
            SinglePoruthamResult(
                id = "rasi",
                nameTa = "ராசிப் பொருத்தம்",
                nameEn = "Rasi Porutham (Lineage & Harmony)",
                nameHi = "राशि पोरुथम (कुल एवं सौख्य)",
                status = rasiStatus,
                pointsEarned = if (rasiStatus == PoruthamStatus.UTTHAMAM) 1.0 else if (rasiStatus == PoruthamStatus.MADHYAMAM) 0.5 else 0.0,
                maxPoints = 1.0,
                explanationTa = if (rasiStatus == PoruthamStatus.UTTHAMAM) "குடும்ப ஒற்றுமை, சந்தோஷம் மற்றும் இரு குடும்பங்களுக்கும் இடையே நல்வாழ்வு அருளும் உத்தம ராசிப் பொருத்தம்."
                else if (rasiStatus == PoruthamStatus.MADHYAMAM) "சம பலன் தரும் ராசி அமைப்பு."
                else "ஷஷ்டாஷ்டக (6/8) ராசி அமைப்பு. பிற பலன்கள் மூலம் சமன் செய்யப்பட வேண்டும்.",
                explanationEn = if (rasiStatus == PoruthamStatus.UTTHAMAM) "Fosters unity, lineage prosperity, and family bliss."
                else if (rasiStatus == PoruthamStatus.MADHYAMAM) "Moderate sign compatibility."
                else "Shashtashtaka (6/8) distance; requires supportive planetary aspects.",
                explanationHi = if (rasiStatus == PoruthamStatus.UTTHAMAM) "पारिवारिक एकता, सुख-शांति एवं वंश वृद्धि के लिए श्रेष्ठ राशि मिलान।"
                else if (rasiStatus == PoruthamStatus.MADHYAMAM) "मध्यम राशि अनुकूलता।"
                else "षडाष्टक (6/8) संबंध; अन्य ग्रहों के शुभ प्रभाव आवश्यक।",
                isCrucial = true
            )
        )

        // 7. Rasiyadhipathi Porutham (ராசியாதிபதிப் பொருத்தம்)
        val bLord = brideRasi.lordEn
        val gLord = groomRasi.lordEn
        val devaPlanets = setOf("Sun", "Moon", "Mars", "Jupiter")
        val asuraPlanets = setOf("Mercury", "Venus", "Saturn")
        val isSameLord = bLord == gLord
        val isFriendlyLords = (bLord in devaPlanets && gLord in devaPlanets) || (bLord in asuraPlanets && gLord in asuraPlanets)
        val rasiAdhipathiStatus = when {
            isSameLord || isFriendlyLords -> PoruthamStatus.UTTHAMAM
            (bLord == "Mercury" && gLord in devaPlanets) || (gLord == "Mercury" && bLord in devaPlanets) -> PoruthamStatus.MADHYAMAM
            else -> PoruthamStatus.PORUNDHADHU
        }
        poruthams.add(
            SinglePoruthamResult(
                id = "rasiyadhipathi",
                nameTa = "ராசியாதிபதிப் பொருத்தம்",
                nameEn = "Rasiyadhipathi (Lord Friendship & Affinity)",
                nameHi = "राश्याधिपति पोरुथम (ग्रह मित्रता)",
                status = rasiAdhipathiStatus,
                pointsEarned = if (rasiAdhipathiStatus == PoruthamStatus.UTTHAMAM) 1.0 else if (rasiAdhipathiStatus == PoruthamStatus.MADHYAMAM) 0.5 else 0.0,
                maxPoints = 1.0,
                explanationTa = "பெண் ராசிநாதன்: ${brideRasi.lordTa}, ஆண் ராசிநாதன்: ${groomRasi.lordTa}. " +
                        if (rasiAdhipathiStatus == PoruthamStatus.UTTHAMAM) "இருவரின் ராசி அதிபதிகள் சிறந்த மித்திரர்கள்; பரஸ்பர அன்பு மற்றும் வாழ்நாள் நட்பு தொடரும்."
                        else if (rasiAdhipathiStatus == PoruthamStatus.MADHYAMAM) "ராசிநாதர்கள் சம நிலை உடையவர்கள்; சுமாரான இணக்கம்."
                        else "ராசிநாதர்கள் மாறுபட்ட கிரக இயல்பு உடையவர்கள்.",
                explanationEn = "Bride Lord: $bLord, Groom Lord: $gLord. " +
                        if (rasiAdhipathiStatus == PoruthamStatus.UTTHAMAM) "Planetary rulers are mutual friends; fosters enduring companionship."
                        else if (rasiAdhipathiStatus == PoruthamStatus.MADHYAMAM) "Neutral planetary affinity."
                        else "Inimical planetary rulers.",
                explanationHi = "वधू राशि स्वामी: ${brideRasi.lordHi}, वर राशि स्वामी: ${groomRasi.lordHi}। " +
                        if (rasiAdhipathiStatus == PoruthamStatus.UTTHAMAM) "राश्याधिपति में मित्रता होने से आजीवन प्रेम एवं सौहार्द रहेगा।"
                        else if (rasiAdhipathiStatus == PoruthamStatus.MADHYAMAM) "राश्याधिपति सम भाव रखते हैं।"
                        else "राश्याधिपति में शत्रुता या भिन्न प्रकृति है।"
            )
        )

        // 8. Vasiya Porutham (வசியப் பொருத்தம்)
        val vasiyaPairs = mapOf(
            1 to setOf(5, 8),    // Mesham attracts Simham, Viruchigam
            2 to setOf(4, 7),    // Rishabam attracts Kadagam, Thulam
            3 to setOf(6),       // Mithunam attracts Kanni
            4 to setOf(8, 9),    // Kadagam attracts Viruchigam, Dhanusu
            5 to setOf(7, 9),    // Simham attracts Thulam, Dhanusu
            6 to setOf(3, 12),   // Kanni attracts Mithunam, Meenam
            7 to setOf(10, 12),  // Thulam attracts Magaram, Meenam
            8 to setOf(4, 11),   // Viruchigam attracts Kadagam, Kumbam
            9 to setOf(12),      // Dhanusu attracts Meenam
            10 to setOf(1, 11),  // Magaram attracts Mesham, Kumbam
            11 to setOf(1, 10),  // Kumbam attracts Mesham, Magaram
            12 to setOf(3, 10)   // Meenam attracts Mithunam, Magaram
        )
        val isVasiya = vasiyaPairs[bRasiIdx]?.contains(gRasiIdx) == true || vasiyaPairs[gRasiIdx]?.contains(bRasiIdx) == true
        val vasiyaStatus = if (isVasiya) PoruthamStatus.UTTHAMAM else PoruthamStatus.MADHYAMAM
        poruthams.add(
            SinglePoruthamResult(
                id = "vasiya",
                nameTa = "வசியப் பொருத்தம்",
                nameEn = "Vasiya Porutham (Affection & Dedication)",
                nameHi = "वश्य पोरुथम (पारस्परिक आकर्षण एवं वशीकरण)",
                status = vasiyaStatus,
                pointsEarned = if (vasiyaStatus == PoruthamStatus.UTTHAMAM) 1.0 else 0.5,
                maxPoints = 1.0,
                explanationTa = if (isVasiya) "தம்பதியரிடையே ஈடு இணையற்ற அன்பும், ஒருவரை ஒருவர் வசீகரிக்கும் நேசமும் தரும் உத்தமப் பொருத்தம்."
                else "சாதாரண வசிய நிலை. மற்ற மன ஒற்றுமை பொருத்தங்கள் நன்று அமைந்தால் சிறப்பு.",
                explanationEn = if (isVasiya) "Special mutual magnet and lifelong adoration between partners."
                else "Standard affection compatibility.",
                explanationHi = if (isVasiya) "दंपति के बीच अगाध प्रेम, परस्पर आकर्षण एवं निष्ठा प्रदान करता है।"
                else "सामान्य वश्य संबंध।"
            )
        )

        // 9. Rajju Porutham (ரஜ்ஜுப் பொருத்தம்) - MOST CRITICAL
        val bRajju = NAKSHATRA_RAJJU[bStar]
        val gRajju = NAKSHATRA_RAJJU[gStar]
        val rajjuTypes = listOf(RajjuType.SIRO, RajjuType.KANDA, RajjuType.UDHARA, RajjuType.URU, RajjuType.PADA)
        val isSameRajju = bRajju == gRajju
        val rajjuStatus = if (!isSameRajju) PoruthamStatus.UTTHAMAM else PoruthamStatus.PORUNDHADHU
        poruthams.add(
            SinglePoruthamResult(
                id = "rajju",
                nameTa = "ரஜ்ஜுப் பொருத்தம் (மகா பொருத்தம்)",
                nameEn = "Rajju Porutham (Mangalya & Longevity Protection)",
                nameHi = "रज्जु पोरुथम (मांगल्य एवं अखंड सौभाग्य)",
                status = rajjuStatus,
                pointsEarned = if (rajjuStatus == PoruthamStatus.UTTHAMAM) 1.0 else 0.0,
                maxPoints = 1.0,
                explanationTa = if (!isSameRajju) "ரஜ்ஜு பொருத்தம் மிக நன்று (இருவருக்கும் வேறு வேறு ரஜ்ஜு: பெண் - ${rajjuTypes[bRajju].nameTa}, ஆண் - ${rajjuTypes[gRajju].nameTa}). மாங்கல்ய பலம் மற்றும் தீர்க்க சுமங்கலி யோகம் உண்டாகும்."
                else "ஏக ரஜ்ஜு (இருவருக்கும் ஒரே ரஜ்ஜு: ${rajjuTypes[bRajju].nameTa}). ரஜ்ஜு தோஷம் உள்ளதால் ஜோதிட அறிஞரின் சிறப்பு ஆலோசனை அவசியம்.",
                explanationEn = if (!isSameRajju) "Excellent! Different Rajjus (Bride: ${rajjuTypes[bRajju].nameEn}, Groom: ${rajjuTypes[gRajju].nameEn}). Bestows long and blessed married life."
                else "Same Rajju (${rajjuTypes[bRajju].nameEn}). Classical Rajju affliction requires expert astrological mitigation.",
                explanationHi = if (!isSameRajju) "उत्तम रज्जु मिलान (अलग-अलग रज्जु: वधू - ${rajjuTypes[bRajju].nameHi}, वर - ${rajjuTypes[gRajju].nameHi})। अखंड सौभाग्य एवं दीर्घायु योग।"
                else "एक ही रज्जु (${rajjuTypes[bRajju].nameHi}) होने से रज्जु दोष है; विशेषज्ञ ज्योतिषी परामर्श आवश्यक।",
                isCrucial = true
            )
        )

        // 10. Vedhai Porutham (வேதைப் பொருத்தம்)
        val isVedhaAfflicted = (bStar to gStar) in VEDHA_PAIRS
        val vedhaStatus = if (!isVedhaAfflicted) PoruthamStatus.UTTHAMAM else PoruthamStatus.PORUNDHADHU
        poruthams.add(
            SinglePoruthamResult(
                id = "vedhai",
                nameTa = "வேதைப் பொருத்தம்",
                nameEn = "Vedha Porutham (Affliction Immunity)",
                nameHi = "वेधा पोरुथम (दोष निवारण)",
                status = vedhaStatus,
                pointsEarned = if (vedhaStatus == PoruthamStatus.UTTHAMAM) 1.0 else 0.0,
                maxPoints = 1.0,
                explanationTa = if (!isVedhaAfflicted) "வேதை தோஷம் இல்லை. தம்பதியருக்கு இடையே மனக்கசப்பும் துக்கமும் அண்டாது அமைதியான இல்வாழ்க்கை கிட்டும்."
                else "நட்சத்திரங்களுக்கு இடையே வேதை (பகைத் தாக்குதல்) தோஷம் உள்ளது.",
                explanationEn = if (!isVedhaAfflicted) "No Vedha affliction. Safeguards against sorrow, dispute, and discord."
                else "Vedha obstruction exists between the chosen nakshatrams.",
                explanationHi = if (!isVedhaAfflicted) "वेधा दोष रहित। दांपत्य जीवन में सुख, शांति एवं कलह से मुक्ति।"
                else "दोनों नक्षत्रों में वेधा (बाधा) दोष विद्यमान है।",
                isCrucial = true
            )
        )

        // Sevvay Dosham (Kuja Dosha) Analysis
        val doshaHouses = setOf(2, 4, 7, 8, 12)
        val bHasRawDosham = brideMarsHouse in doshaHouses
        val gHasRawDosham = groomMarsHouse in doshaHouses

        // Cancellation rules (Own house, exaltation, etc.)
        val bCancelled = (bHasRawDosham && (brideMarsHouse == 2 && bRasiIdx in listOf(3, 6))) || // 2nd in Gemini/Virgo
                (brideMarsHouse == 4 && bRasiIdx in listOf(1, 8)) || // 4th in Aries/Scorpio
                (brideMarsHouse == 7 && bRasiIdx in listOf(4, 10)) || // 7th in Cancer/Capricorn
                (brideMarsHouse == 8 && bRasiIdx in listOf(9, 12)) || // 8th in Sag/Pisces
                (brideMarsHouse == 12 && bRasiIdx in listOf(2, 7)) // 12th in Taurus/Libra

        val gCancelled = (gHasRawDosham && (groomMarsHouse == 2 && gRasiIdx in listOf(3, 6))) ||
                (groomMarsHouse == 4 && gRasiIdx in listOf(1, 8)) ||
                (groomMarsHouse == 7 && gRasiIdx in listOf(4, 10)) ||
                (groomMarsHouse == 8 && gRasiIdx in listOf(9, 12)) ||
                (groomMarsHouse == 12 && gRasiIdx in listOf(2, 7))

        val bDoshaEffective = bHasRawDosham && !bCancelled
        val gDoshaEffective = gHasRawDosham && !gCancelled

        val sevvayAnalysis = SevvayDoshamAnalysis(
            isBrideHasDosham = bDoshaEffective,
            isGroomHasDosham = gDoshaEffective,
            brideDoshamSeverity = if (!bHasRawDosham) "தோஷம் இல்லை" else if (bCancelled) "விதிவிலக்கு (தோஷ நிவர்த்தி)" else "செவ்வாய் தோஷம் உண்டு (${brideMarsHouse}-ஆம் இடம்)",
            groomDoshamSeverity = if (!gHasRawDosham) "தோஷம் இல்லை" else if (gCancelled) "விதிவிலக்கு (தோஷ நிவர்த்தி)" else "செவ்வாய் தோஷம் உண்டு (${groomMarsHouse}-ஆம் இடம்)",
            brideDoshamSeverityEn = if (!bHasRawDosham) "No Dosha" else if (bCancelled) "Cancelled (Dosha Nivritti)" else "Kuja Dosha Present (House $brideMarsHouse)",
            groomDoshamSeverityEn = if (!gHasRawDosham) "No Dosha" else if (gCancelled) "Cancelled (Dosha Nivritti)" else "Kuja Dosha Present (House $groomMarsHouse)",
            brideDoshamSeverityHi = if (!bHasRawDosham) "दोष नहीं है" else if (bCancelled) "दोष निवृत्ति (रद्द)" else "मंगल दोष है (भाव $brideMarsHouse)",
            groomDoshamSeverityHi = if (!gHasRawDosham) "दोष नहीं है" else if (gCancelled) "दोष निवृत्ति (रद्द)" else "मंगल दोष है (भाव $groomMarsHouse)",
            brideCancellationReasonTa = if (bCancelled) "செவ்வாய் நின்ற ராசி/வீடு அமைப்பால் செவ்வாய் தோஷம் விதிவிலக்கு பெற்று நிவர்த்தியாகியுள்ளது." else null,
            groomCancellationReasonTa = if (gCancelled) "செவ்வாய் நின்ற ராசி/வீடு அமைப்பால் செவ்வாய் தோஷம் விதிவிலக்கு பெற்று நிவர்த்தியாகியுள்ளது." else null,
            brideCancellationReasonEn = if (bCancelled) "Mars placement qualifies for classical cancellation (Kuja Dosha Nivritti)." else null,
            groomCancellationReasonEn = if (gCancelled) "Mars placement qualifies for classical cancellation (Kuja Dosha Nivritti)." else null,
            brideCancellationReasonHi = if (bCancelled) "मंगल की राशि/भाव स्थिति के कारण मंगल दोष रद्द (दोष निवृत्ति) हो जाता है।" else null,
            groomCancellationReasonHi = if (gCancelled) "मंगल की राशि/भाव स्थिति के कारण मंगल दोष रद्द (दोष निवृत्ति) हो जाता है।" else null,
            doshaSamyamStatusTa = when {
                bDoshaEffective && gDoshaEffective -> "தோஷ சாம்யம் உண்டு (இருவருக்கும் செவ்வாய் தோஷம் உள்ளதால் மிகச் சிறந்த பொருத்தம்)"
                !bDoshaEffective && !gDoshaEffective -> "தோஷ சாம்யம் உண்டு (இருவருக்கும் செவ்வாய் தோஷம் இல்லை - நற்பொருத்தம்)"
                bDoshaEffective && !gDoshaEffective -> "தோஷ சாம்யம் இல்லை (பெண்ணுக்கு செவ்வாய் தோஷம் உண்டு; ஆணுக்கு இல்லை)"
                else -> "தோஷ சாம்யம் இல்லை (ஆணுக்கு செவ்வாய் தோஷம் உண்டு; பெண்ணுக்கு இல்லை)"
            },
            doshaSamyamStatusEn = when {
                bDoshaEffective && gDoshaEffective -> "Dosha Samyam Valid (Both have Mars affliction, neutralizing each other perfectly)"
                !bDoshaEffective && !gDoshaEffective -> "Clean Match (Neither chart has Kuja Dosha affliction)"
                bDoshaEffective && !gDoshaEffective -> "Imbalance (Bride has Kuja Dosha, Groom does not)"
                else -> "Imbalance (Groom has Kuja Dosha, Bride does not)"
            },
            doshaSamyamStatusHi = when {
                bDoshaEffective && gDoshaEffective -> "दोष साम्य अनुकूल (दोनों में मंगल दोष होने से दोष शमन होता है)"
                !bDoshaEffective && !gDoshaEffective -> "दोष मुक्त उत्तम योग (दोनों में मंगल दोष नहीं है)"
                else -> "दोष असंतुलन (एक पक्ष में मंगल दोष विद्यमान है)"
            },
            recommendationTa = when {
                (bDoshaEffective && gDoshaEffective) || (!bDoshaEffective && !gDoshaEffective) -> "செவ்வாய் தோஷ அமைப்பில் எந்த தடையும் இல்லை. திருமணம் செய்யலாம்."
                else -> "ஒருவருக்கு மட்டும் செவ்வாய் தோஷம் உள்ளதால், ஜாதக ரீதியாக செவ்வாய்க்குரிய திருத்தல பரிகாரம் அல்லது தகுந்த தோஷ சாம்ய ஜாதகத்தை பரிசீலிப்பது நலம்."
            },
            recommendationEn = when {
                (bDoshaEffective && gDoshaEffective) || (!bDoshaEffective && !gDoshaEffective) -> "Kuja Dosha alignment is fully compatible for marriage."
                else -> "One partner has unmitigated Kuja Dosha. Remedial temple worship or dosha-balanced matching is advised."
            },
            recommendationHi = when {
                (bDoshaEffective && gDoshaEffective) || (!bDoshaEffective && !gDoshaEffective) -> "मंगल दोष की दृष्टि से यह संबंध शुभ एवं ग्राह्य है।"
                else -> "एक पक्ष में मंगल दोष होने से शांति अनुष्ठान अथवा विशेषज्ञ परामर्श अनुशंसित है।"
            }
        )

        val totalPoints = poruthams.sumOf { it.pointsEarned }
        val matchedCount = poruthams.count { it.status == PoruthamStatus.UTTHAMAM || it.status == PoruthamStatus.MADHYAMAM }
        val rajjuOk = !isSameRajju

        val verdictStatus = when {
            totalPoints >= 7.0 && rajjuOk -> PoruthamStatus.UTTHAMAM
            totalPoints >= 5.0 && rajjuOk -> PoruthamStatus.MADHYAMAM
            else -> PoruthamStatus.PORUNDHADHU
        }

        val verdictTa = when (verdictStatus) {
            PoruthamStatus.UTTHAMAM -> "திருமணப் பொருத்தம் மிகவும் உத்தமம் (10-க்கு ${matchedCount} பொருத்தம் உண்டு; ரஜ்ஜு பொருத்தம் சிறப்பு). தாராளமாக திருமணம் செய்யலாம்."
            PoruthamStatus.MADHYAMAM -> "திருமணப் பொருத்தம் மத்திமம் (10-க்கு ${matchedCount} பொருத்தம் உண்டு). பெரியோர்களின் ஆசியுடன் திருமணம் செய்யலாம்."
            PoruthamStatus.PORUNDHADHU -> if (!rajjuOk) "ரஜ்ஜுப் பொருத்தம் பொருந்தவில்லை மற்றும் குறைந்த மதிப்பெண் பெற்றுள்ளது. ஜோதிட ஆலோசனை தேவை."
            else "குறைவான பொருத்தங்கள் (${matchedCount}/10) மட்டுமே பொருந்துகின்றன. மற்ற ஜாதக அமைப்புகளை ஆராயவும்."
        }

        val verdictEn = when (verdictStatus) {
            PoruthamStatus.UTTHAMAM -> "Highly Auspicious Match ($matchedCount/10 Poruthams matched with strong Rajju). Highly recommended for marriage."
            PoruthamStatus.MADHYAMAM -> "Moderate Match ($matchedCount/10 Poruthams matched). Acceptable with familial blessings and standard remedies."
            PoruthamStatus.PORUNDHADHU -> if (!rajjuOk) "Critical Rajju mismatch. Requires detailed astrological review."
            else "Low overall compatibility ($matchedCount/10 Poruthams). Not recommended without mitigating factors."
        }

        val verdictHi = when (verdictStatus) {
            PoruthamStatus.UTTHAMAM -> "अति उत्तम विवाह मिलान ($matchedCount/10 गुण एवं रज्जु अनुकूल)। विवाह के लिए श्रेष्ठ।"
            PoruthamStatus.MADHYAMAM -> "मध्यम मिलान ($matchedCount/10 गुण अनुकूल)। विवाह अनुशंसित।"
            PoruthamStatus.PORUNDHADHU -> "कम गुण मिलान ($matchedCount/10)।"
        }

        return WeddingMatchResult(
            brideRasi = brideRasi,
            brideNakshatram = NAKSHATRAM_NAMES_TA[bStar],
            bridePada = bridePada,
            groomRasi = groomRasi,
            groomNakshatram = NAKSHATRAM_NAMES_TA[gStar],
            groomPada = groomPada,
            poruthams = poruthams,
            totalPoruthamsMatched = matchedCount,
            totalScore = totalPoints,
            maxScore = 10.0,
            overallVerdictTa = verdictTa,
            overallVerdictEn = verdictEn,
            overallVerdictHi = verdictHi,
            verdictStatus = verdictStatus,
            rajjuMatch = rajjuOk,
            sevvayDosham = sevvayAnalysis,
            brideNakshatramEn = NAKSHATRAM_NAMES_EN[bStar],
            brideNakshatramHi = NAKSHATRAM_NAMES_HI[bStar],
            groomNakshatramEn = NAKSHATRAM_NAMES_EN[gStar],
            groomNakshatramHi = NAKSHATRAM_NAMES_HI[gStar]
        )
    }
}
