package com.example.data.service

import com.example.data.model.ObservanceType
import com.example.data.model.PanchangamDetail
import com.example.data.model.TamilMonth
import com.example.data.model.TamilSamvatsaraEngine
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.*

data class Tuple6<A, B, C, D, E, F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F)

data class EphemerisData(
    val sunTropical: Double,
    val sunSidereal: Double,
    val moonTropical: Double,
    val moonSidereal: Double,
    val ayanamsha: Double
)

/**
 * Interface for Drik Panchangam Calculation.
 * Defaults to Nadi, Fiji Islands (UTC+12:00 / Pacific/Fiji).
 */
interface PanchangamCalculator {
    fun calculatePanchangam(date: LocalDate, location: String = "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)"): PanchangamDetail
}

class StandardPanchangamCalculator : PanchangamCalculator {

    // 15 Shukla Tithis + 15 Krishna Tithis
    private val tithisTa = listOf(
        "பிரதமை", "துவிதியை", "திருதியை", "சதுர்த்தி", "பஞ்சமி", "சஷ்டி", "சப்தமி", "அஷ்டமி", "நவமி", "தசமி", "ஏகாதசி", "துவாதசி", "திரயோதசி", "சதுர்த்தசி", "பௌர்ணமி",
        "பிரதமை", "துவிதியை", "திருதியை", "சதுர்த்தி", "பஞ்சமி", "சஷ்டி", "சப்தமி", "அஷ்டமி", "நவமி", "தசமி", "ஏகாதசி", "துவாதசி", "திரயோதசி", "சதுர்த்தசி", "அமாவாசை"
    )
    private val tithisEn = listOf(
        "Prathama", "Dvitiya", "Tritiya", "Chaturthi", "Panchami", "Shashti", "Saptami", "Ashtami", "Navami", "Dashami", "Ekadashi", "Dvadashi", "Trayodashi", "Chaturdashi", "Pournami (Full Moon)",
        "Prathama", "Dvitiya", "Tritiya", "Chaturthi", "Panchami", "Shashti", "Saptami", "Ashtami", "Navami", "Dashami", "Ekadashi", "Dvadashi", "Trayodashi", "Chaturdashi", "Amavasya (New Moon)"
    )
    private val tithisHi = listOf(
        "प्रतिपदा", "द्वितीया", "तृतीया", "चतुर्थी", "पंचमी", "षष्ठी", "सप्तमी", "अष्टमी", "नवमी", "दशमी", "एकादशी", "द्वादशी", "त्रयोदशी", "चतुर्दशी", "पूर्णिमा",
        "प्रतिपदा", "द्वितीया", "तृतीया", "चतुर्थी", "पंचमी", "षष्ठी", "सप्तमी", "अष्टमी", "नवमी", "दशमी", "एकादशी", "द्वादशी", "त्रयोदशी", "चतुर्दशी", "अमावस्या"
    )

    // 27 Drik Nakshatras
    private val nakshatramsTa = listOf(
        "அசுவினி", "பரணி", "கார்த்திகை", "ரோகிணி", "மிருகசீரிஷம்", "திருவாதிரை", "புனர்பூசம்", "பூசம்", "ஆயில்யம்",
        "மகம்", "பூரம்", "உத்திரம்", "அஸ்தம்", "சித்திரை", "சுவாதி", "விசாகம்", "அனுஷம்", "கேட்டை",
        "மூலம்", "பூராடம்", "உத்திராடம்", "திருவோணம்", "அவிட்டம்", "சதயம்", "பூரட்டாதி", "உத்திரட்டாதி", "ரேவதி"
    )
    private val nakshatramsEn = listOf(
        "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashirsha", "Ardra", "Punarvasu", "Pushya", "Ashlesha",
        "Magha", "Purva Phalguni", "Uttara Phalguni", "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha",
        "Moola", "Purvashada", "Uttarashada", "Shravana", "Dhanishta", "Shatabhisha", "Purva Bhadrapada", "Uttara Bhadrapada", "Revati"
    )
    private val nakshatramsHi = listOf(
        "अश्विनी", "भरणी", "कृत्तिका", "रोहिणी", "मृगशिरा", "आर्द्रा", "पुनर्वसु", "पुष्य", "आश्लेषा",
        "मघा", "पूर्वाफाल्गुनी", "उत्तराफाल्गुनी", "हस्त", "चित्रा", "स्वाति", "विशाखा", "अनुराधा", "ज्येष्ठा",
        "मूल", "पूर्वाषाढ़ा", "उत्तराषाढ़ा", "श्रवण", "धनिष्ठा", "शतभिषा", "पूर्वाभाद्रपद", "उत्तराभाद्रपद", "रेवती"
    )

    // 27 Nithya Yogas
    private val nithyaYogamsTa = listOf(
        "விஷ்கம்பம்", "ப்ரீதி", "ஆயுஷ்மான்", "சௌபாக்யம்", "சோபனம்", "அதிகண்டம்", "சுகர்மம்", "திருதி", "சூலம்",
        "கண்டம்", "விருத்தி", "துருவம்", "வியாகாதம்", "ஹர்ஷணம்", "வஜ்ரம்", "சித்தி", "வியதீபாதம்", "வாரியான்",
        "பரிகம்", "சிவம்", "சித்தம்", "சாத்தியம்", "சுபம்", "சுக்லம்", "பிரம்மம்", "ஐந்திரம்", "வைதிருதி"
    )
    private val nithyaYogamsEn = listOf(
        "Vishkambha", "Priti", "Ayushman", "Saubhagya", "Shobhana", "Atiganda", "Sukarma", "Dhriti", "Shoola",
        "Ganda", "Vriddhi", "Dhruva", "Vyaghata", "Harshana", "Vajra", "Siddhi", "Vyatipata", "Variyan",
        "Parigha", "Shiva", "Siddha", "Sadhya", "Shubha", "Shukla", "Brahma", "Indra", "Vaidhriti"
    )
    private val nithyaYogamsHi = listOf(
        "विष्कुम्भ", "प्रीति", "आयुष्मान्", "सौभाग्य", "शोभन", "अतिगण्ड", "सुकर्मा", "धृति", "शूल",
        "गण्ड", "वृद्धि", "ध्रुव", "व्याघात", "हर्षण", "वज्र", "सिद्धि", "व्यतीपात", "वरीयान्",
        "परिघ", "शिव", "सिद्ध", "साध्य", "शुभ", "शुक्ल", "ब्रह्म", "ऐन्द्र", "वैधृति"
    )

    // 7 Chara Karanas
    private val charaKaranamsTa = listOf("பவம்", "பாலவம்", "கௌலவம்", "தைதுலம்", "கரஜை", "வனஜை", "பத்திரை")
    private val charaKaranamsEn = listOf("Bava", "Balava", "Kaulava", "Taitila", "Gara", "Vanija", "Vishti (Bhadra)")
    private val charaKaranamsHi = listOf("बव", "बालव", "कौलव", "तैतिल", "गर", "वणिज", "विष्टि (भद्रा)")

    private val rasiNamesTa = listOf("மேஷம்", "ரிஷபம்", "மிதுனம்", "கடகம்", "சிம்மம்", "கன்னி", "துலாம்", "விருச்சிகம்", "தனுசு", "மகரம்", "கும்பம்", "மீனம்")
    private val rasiNamesEn = listOf("Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces")
    private val rasiNamesHi = listOf("मेष", "वृषभ", "मिथुन", "कर्क", "सिंह", "कन्या", "तुला", "वृश्चिक", "धनु", "मकर", "कुम्भ", "मीन")

    data class LocationCoordinates(
        val lat: Double,
        val lon: Double,
        val timeZoneOffsetHours: Double,
        val nameTa: String,
        val nameEn: String = nameTa,
        val nameHi: String = nameTa
    )

    private val locationMap = mapOf(
        "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)" to LocationCoordinates(-17.7765, 177.4356, 12.0, "நாடி, பிஜி", "Nadi, Fiji", "नादी, फिजी"),
        "சுவா, பிஜி தீவுகள் (Suva, Fiji Islands)" to LocationCoordinates(-18.1416, 178.4419, 12.0, "சுவா, பிஜி", "Suva, Fiji", "सुवा, फिजी"),
        "லௌடோகா, பிஜி (Lautoka, Fiji)" to LocationCoordinates(-17.6167, 177.4667, 12.0, "லௌடோகா, பிஜி", "Lautoka, Fiji", "लौटोका, फिजी"),
        "லம்பாசா, பிஜி (Labasa, Fiji)" to LocationCoordinates(-16.4333, 179.3667, 12.0, "லம்பாசா, பிஜி", "Labasa, Fiji", "लम्बासा, फिजी"),
        "சென்னை (Chennai, India)" to LocationCoordinates(13.0827, 80.2707, 5.5, "சென்னை, இந்தியா", "Chennai, India", "चेन्नई, भारत"),
        "மதுரை (Madurai, India)" to LocationCoordinates(9.9252, 78.1198, 5.5, "மதுரை, இந்தியா", "Madurai, India", "मदुरै, भारत"),
        "சிட்னி (Sydney, Australia)" to LocationCoordinates(-33.8688, 151.2093, 10.0, "சிட்னி, ஆஸ்திரேலியா", "Sydney, Australia", "सिडनी, ऑस्ट्रेलिया"),
        "ஆக்லாந்து (Auckland, NZ)" to LocationCoordinates(-36.8485, 174.7633, 12.0, "ஆக்லாந்து, நியூசிலாந்து", "Auckland, NZ", "ऑकलैंड, न्यूजीलैंड"),
        "சிங்கப்பூர் (Singapore)" to LocationCoordinates(1.3521, 103.8198, 8.0, "சிங்கப்பூர்", "Singapore", "सिंगापुर"),
        "லண்டன் (London, UK)" to LocationCoordinates(51.5074, -0.1278, 0.0, "லண்டன், இங்கிலாந்து", "London, UK", "लंदन, यूके"),
        "நியூயார்க் (New York, USA)" to LocationCoordinates(40.7128, -74.0060, -5.0, "நியூயார்க், அமெரிக்கா", "New York, USA", "न्यूयॉर्क, यूएसए")
    )

    override fun calculatePanchangam(date: LocalDate, location: String): PanchangamDetail {
        val loc = resolveLocation(location)
        val ephem = calculateEphemeris(date, 6.0 / 24.0) // 6:00 AM UTC approximation

        val sunTrop = ephem.sunTropical
        val sunSid = ephem.sunSidereal
        val moonTrop = ephem.moonTropical
        val moonSid = ephem.moonSidereal

        val (sunriseDec, sunsetDec) = calculateSunriseSunsetDec(date, loc.lat, loc.lon, loc.timeZoneOffsetHours)
        val sunriseLocal = formatDecTime(sunriseDec)
        val sunsetLocal = formatDecTime(sunsetDec)

        val (tamilMonth, tamilDate) = TamilSamvatsaraEngine.getTamilDate(date)
        val tamilYear = TamilSamvatsaraEngine.getSamvatsaraForDate(date)
        val sunRasiIndex = (sunSid / 30.0).toInt() % 12

        // 1. Samvatsara (Tamil Year)
        val samvatsaraName = "${tamilYear.tamilName} வருடம் (${tamilYear.number}/60)"

        // 2. Ayanam: Uttarayanam (Makara/Thai to Mithuna/Aani), Dakshinayanam (Kataka/Aadi to Dhanus/Margazhi)
        val isUttarayanam = tamilMonth.index in listOf(10, 11, 12, 1, 2, 3)
        val ayanamTa = if (isUttarayanam) "உத்தராயணம்" else "தக்ஷிணாயணம்"
        val ayanamEn = if (isUttarayanam) "Uttarayanam" else "Dakshinayanam"
        val ayanamHi = if (isUttarayanam) "उत्तरायण" else "दक्षिणायन"

        // 3. Rithu (Vedic Season)
        val (rituTa, rituEn, rituHi) = when (tamilMonth) {
            TamilMonth.CHITHIRAI, TamilMonth.VAIKASI -> Triple("வசந்த ருது", "Vasanta Ritu", "वसन्त ऋतु")
            TamilMonth.AANI, TamilMonth.AADI -> Triple("கிரீஷ்ம ருது", "Greeshma Ritu", "ग्रीष्म ऋतु")
            TamilMonth.AVANI, TamilMonth.PURATTASI -> Triple("வர்ஷ ருது", "Varsha Ritu", "वर्षा ऋतु")
            TamilMonth.AIPASI, TamilMonth.KARTHIGAI -> Triple("சரத் ருது", "Sharad Ritu", "शरद् ऋतु")
            TamilMonth.MARGHAZHI, TamilMonth.THAI -> Triple("ஹேமந்த ருது", "Hemanta Ritu", "हेमन्त ऋतु")
            TamilMonth.MASI, TamilMonth.PANGUNI -> Triple("சிசிர ருது", "Shishira Ritu", "शिशिर ऋतु")
        }

        // 4. Tamil Masam in Sanskrit
        val sanskritMonth = tamilMonth.sanskritMasa

        // 5. Paksham (Shukla / Krishna) & 6. Tithi
        val elongation = normalizeDegrees(moonTrop - sunTrop)
        val tithiIndex = (elongation / 12.0).toInt() % 30
        val tithiNameTa = tithisTa[tithiIndex]
        val tithiNameEn = tithisEn[tithiIndex]
        val tithiNameHi = tithisHi[tithiIndex]
        val (pakshaTa, pakshaEn, pakshaHi) = if (tithiIndex < 15) {
            Triple("சுக்ல பக்ஷம் (வளர்பிறை)", "Shukla Paksha (Waxing)", "शुक्ल पक्ष")
        } else {
            Triple("கிருஷ்ண பக்ஷம் (தேய்பிறை)", "Krishna Paksha (Waning)", "कृष्ण पक्ष")
        }

        // Tithi End Time & Next Tithi calculation relative to sunrise
        val remTithiDeg = 12.0 - (elongation % 12.0)
        val tithiHoursRemaining = (remTithiDeg / 12.19) * 24.0
        val tithiEndTime = formatLocalEndTime(sunriseDec + tithiHoursRemaining)
        val nextTithiIndex = (tithiIndex + 1) % 30
        val nextTithi = tithisTa[nextTithiIndex]

        // 7. Vasaram (Vedic Day of Week)
        val (vasaramTa, vasaramEn, vasaramHi, dayTa, dayEn, dayHi) = when (date.dayOfWeek) {
            DayOfWeek.SUNDAY -> Tuple6("பானு வாசரம்", "Bhanu Vasaram", "भानुवासरः", "ஞாயிற்றுக்கிழமை", "Sunday", "रविवार")
            DayOfWeek.MONDAY -> Tuple6("சோம வாசரம்", "Soma Vasaram", "सोमवासरः", "திங்கட்கிழமை", "Monday", "सोमवार")
            DayOfWeek.TUESDAY -> Tuple6("பௌம வாசரம்", "Bhauma Vasaram", "भौमवासरः", "செவ்வாய்க்கிழமை", "Tuesday", "मंगलवार")
            DayOfWeek.WEDNESDAY -> Tuple6("ஸௌம்ய வாசரம்", "Saumya Vasaram", "सौम्यवासरः", "புதன்கிழமை", "Wednesday", "बुधवार")
            DayOfWeek.THURSDAY -> Tuple6("குரு வாசரம்", "Guru Vasaram", "गुरुवासरः", "வியாழக்கிழமை", "Thursday", "गुरुवार")
            DayOfWeek.FRIDAY -> Tuple6("ப்ருகு வாசரம்", "Bhrigu Vasaram", "भृगुवासरः", "வெள்ளிக்கிழமை", "Friday", "शुक्रवार")
            DayOfWeek.SATURDAY -> Tuple6("ஸ்திர வாசரம்", "Sthira Vasaram", "स्थिरवासरः", "சனிக்கிழமை", "Saturday", "शनिवार")
        }

        // 8. Nakshatram & Pada
        val nakshatraIndex = (moonSid / (360.0 / 27.0)).toInt() % 27
        val nakshatraNameTa = nakshatramsTa[nakshatraIndex]
        val nakshatraNameEn = nakshatramsEn[nakshatraIndex]
        val nakshatraNameHi = nakshatramsHi[nakshatraIndex]
        val pada = (((moonSid % (360.0 / 27.0)) / (360.0 / 108.0)).toInt() % 4) + 1

        val remNakDeg = (360.0 / 27.0) - (moonSid % (360.0 / 27.0))
        val nakHoursRemaining = (remNakDeg / 13.176) * 24.0
        val nakshatraEndTime = formatLocalEndTime(sunriseDec + nakHoursRemaining)
        val nextNakshatraIndex = (nakshatraIndex + 1) % 27
        val nextNakshatra = nakshatramsTa[nextNakshatraIndex]

        // 9. Nithya Yogam & Dina Yogam
        val yogaDeg = normalizeDegrees(sunSid + moonSid)
        val nithyaYogaIndex = (yogaDeg / (360.0 / 27.0)).toInt() % 27
        val nithyaYogaNameTa = nithyaYogamsTa[nithyaYogaIndex]
        val nithyaYogaNameEn = nithyaYogamsEn[nithyaYogaIndex]
        val nithyaYogaNameHi = nithyaYogamsHi[nithyaYogaIndex]
        val remYogaDeg = (360.0 / 27.0) - (yogaDeg % (360.0 / 27.0))
        val yogaHoursRemaining = (remYogaDeg / 14.16) * 24.0
        val yogaEndTime = formatLocalEndTime(sunriseDec + yogaHoursRemaining)
        val dinaYogaTa = calculateDinaYoga(date.dayOfWeek, nakshatraIndex)
        val dinaYogaEn = when {
            dinaYogaTa.contains("அமிர்த") -> "Amrita Yoga"
            dinaYogaTa.contains("சித்த") -> "Siddha Yoga"
            else -> "Marana Yoga"
        }
        val dinaYogaHi = when {
            dinaYogaTa.contains("அமிர்த") -> "अमृत योग"
            dinaYogaTa.contains("சித்த") -> "सिद्ध योग"
            else -> "मरण योग"
        }

        // 10. Karanam
        val halfTithiIndex = (elongation / 6.0).toInt() % 60
        val (karanamNameTa, karanamNameEn, karanamNameHi) = when (halfTithiIndex) {
            0 -> Triple("கிம்ஸ்துக்னம்", "Kimstughna", "किंस्तुघ्न")
            in 1..56 -> Triple(
                charaKaranamsTa[(halfTithiIndex - 1) % 7],
                charaKaranamsEn[(halfTithiIndex - 1) % 7],
                charaKaranamsHi[(halfTithiIndex - 1) % 7]
            )
            57 -> Triple("சகுனி", "Shakuni", "शकुनि")
            58 -> Triple("சதுஷ்பாதம்", "Chatushpada", "चतुष्पद")
            else -> Triple("நாகவம்", "Naga", "नाग")
        }
        val remKarDeg = 6.0 - (elongation % 6.0)
        val karHoursRemaining = (remKarDeg / 12.19) * 24.0
        val karanamEndTime = formatLocalEndTime(sunriseDec + karHoursRemaining)

        val nextHalfTithiIndex = (halfTithiIndex + 1) % 60
        val nextKaranam = when (nextHalfTithiIndex) {
            0 -> "கிம்ஸ்துக்னம்"
            in 1..56 -> charaKaranamsTa[(nextHalfTithiIndex - 1) % 7]
            57 -> "சகுனி"
            58 -> "சதுஷ்பாதம்"
            else -> "நாகவம்"
        }

        // Rasis & Chandrashtamam
        val moonRasiIndex = (moonSid / 30.0).toInt() % 12
        val chandraRasiTa = rasiNamesTa[moonRasiIndex]
        val chandraRasiEn = rasiNamesEn[moonRasiIndex]
        val chandraRasiHi = rasiNamesHi[moonRasiIndex]
        val suryaRasiTa = rasiNamesTa[sunRasiIndex]
        val suryaRasiEn = rasiNamesEn[sunRasiIndex]
        val suryaRasiHi = rasiNamesHi[sunRasiIndex]

        val chandrashtamaRasiIdx = (moonRasiIndex - 7 + 12) % 12
        val chandrashtamamTa = "${rasiNamesTa[chandrashtamaRasiIdx]} ராசி அன்பர்களுக்கு இன்றைய நாள் சந்திராஷ்டமம்."
        val chandrashtamamEn = "Chandrashtamam today for ${rasiNamesEn[chandrashtamaRasiIdx]} (Moon in 8th house)."
        val chandrashtamamHi = "${rasiNamesHi[chandrashtamaRasiIdx]} राशि वालों के लिए आज चंद्राष्टम है।"

        // Sun & Moon Times
        val (moonriseLocal, moonsetLocal) = calculateMoonriseMoonset(date, loc.lat, loc.lon, loc.timeZoneOffsetHours, elongation)

        // Inauspicious Times (Dynamic 8-part division of daytime)
        val (rahu, yama, kuli) = calculateDynamicInauspiciousTimes(date.dayOfWeek, sunriseDec, sunsetDec)

        // Nalla Neram
        val (nallaMorn, nallaEve) = getNallaNeram(date.dayOfWeek)
        val (gowriMorn, gowriEve) = getGowriNallaNeram(date.dayOfWeek)

        // Muhurtham & Disha Soola
        val abhijitMuhurtham = "11:52 AM - 12:44 PM"
        val durMuhurtham = when (date.dayOfWeek) {
            DayOfWeek.SUNDAY -> "04:50 PM - 05:38 PM"
            DayOfWeek.MONDAY -> "12:45 PM - 01:35 PM"
            DayOfWeek.TUESDAY -> "08:35 AM - 09:25 AM"
            DayOfWeek.WEDNESDAY -> "11:55 AM - 12:45 PM"
            DayOfWeek.THURSDAY -> "10:15 AM - 11:05 AM"
            DayOfWeek.FRIDAY -> "08:50 AM - 09:40 AM"
            DayOfWeek.SATURDAY -> "07:20 AM - 08:10 AM"
        }
        val varjyam = "01:40 PM - 03:10 PM"

        val (dishaSoolaTa, dishaSoolaEn, dishaSoolaHi, soolaPariharamTa, soolaPariharamEn, soolaPariharamHi) = when (date.dayOfWeek) {
            DayOfWeek.SUNDAY -> Tuple6("மேற்கு", "West", "पश्चिम", "வெல்லம்", "Jaggery", "गुड़")
            DayOfWeek.MONDAY -> Tuple6("கிழக்கு", "East", "पूर्व", "தயிர்", "Curd", "दही")
            DayOfWeek.TUESDAY -> Tuple6("வடக்கு", "North", "उत्तर", "பால்", "Milk", "दूध")
            DayOfWeek.WEDNESDAY -> Tuple6("வடக்கு", "North", "उत्तर", "பால்", "Milk", "दूध")
            DayOfWeek.THURSDAY -> Tuple6("தெற்கு", "South", "दक्षिण", "நெய்", "Ghee", "घी")
            DayOfWeek.FRIDAY -> Tuple6("மேற்கு", "West", "पश्चिम", "வெல்லம்", "Jaggery", "गुड़")
            DayOfWeek.SATURDAY -> Tuple6("கிழக்கு", "East", "पूर्व", "தயிர்", "Curd", "दही")
        }

        // Special Observances
        val observances = mutableListOf<ObservanceType>()
        if (tithiIndex == 14) observances.add(ObservanceType.POURNAMI)
        if (tithiIndex == 29) observances.add(ObservanceType.AMAVASAI)
        if (tithiIndex == 10 || tithiIndex == 25) observances.add(ObservanceType.EKADASHI)
        if (tithiIndex == 12 || tithiIndex == 27) observances.add(ObservanceType.PRADOSHAM)
        if (tithiIndex == 5) observances.add(ObservanceType.SHUKLA_SHASHTI)
        if (tithiIndex == 18) observances.add(ObservanceType.SANKATAHARA_CHATURTHI)
        if (tithiIndex == 3) observances.add(ObservanceType.CHATURTHI)
        if (tithiIndex == 7 || tithiIndex == 22) observances.add(ObservanceType.ASHTAMI)
        if (tithiIndex == 8 || tithiIndex == 23) observances.add(ObservanceType.NAVAMI)
        if (nakshatraIndex == 2) observances.add(ObservanceType.KRITTIKAI)
        if (nakshatraIndex == 21) observances.add(ObservanceType.TIRUVONAM)
        if (tithiIndex == 28) observances.add(ObservanceType.MONTHLY_SHIVARATRI)

        return PanchangamDetail(
            gregorianDate = date,
            tamilDate = tamilDate,
            tamilMonth = tamilMonth,
            tamilYear = tamilYear,
            samvatsaraName = samvatsaraName,
            ayanam = ayanamTa,
            ritu = rituTa,
            sanskritMonth = sanskritMonth,
            paksha = pakshaTa,
            tithi = tithiNameTa,
            tithiEndTime = tithiEndTime,
            nextTithi = nextTithi,
            vasaram = vasaramTa,
            dayOfWeek = dayTa,
            nakshatram = nakshatraNameTa,
            nakshatramEndTime = nakshatraEndTime,
            pada = pada,
            nextNakshatram = nextNakshatra,
            yogam = nithyaYogaNameTa,
            yogamEndTime = yogaEndTime,
            dinaYogam = dinaYogaTa,
            karanam = karanamNameTa,
            karanamEndTime = karanamEndTime,
            nextKaranam = nextKaranam,
            sunrise = "$sunriseLocal (${loc.nameTa})",
            sunset = "$sunsetLocal (${loc.nameTa})",
            moonrise = moonriseLocal,
            moonset = moonsetLocal,
            chandraRasi = chandraRasiTa,
            suryaRasi = suryaRasiTa,
            chandrashtamam = chandrashtamamTa,
            nallaNeramMorning = nallaMorn,
            nallaNeramEvening = nallaEve,
            gowriNallaNeramMorning = gowriMorn,
            gowriNallaNeramEvening = gowriEve,
            rahuKalam = rahu,
            yamagandam = yama,
            kuligai = kuli,
            abhijitMuhurtham = abhijitMuhurtham,
            durMuhurtham = durMuhurtham,
            varjyam = varjyam,
            dishaSoola = dishaSoolaTa,
            soolaPariharam = soolaPariharamTa,
            specialObservances = observances,
            isDemoData = false,
            
            tithiTa = tithiNameTa,
            tithiEn = tithiNameEn,
            tithiHi = tithiNameHi,
            nakshatramTa = nakshatraNameTa,
            nakshatramEn = nakshatraNameEn,
            nakshatramHi = nakshatraNameHi,
            yogamTa = nithyaYogaNameTa,
            yogamEn = nithyaYogaNameEn,
            yogamHi = nithyaYogaNameHi,
            karanamTa = karanamNameTa,
            karanamEn = karanamNameEn,
            karanamHi = karanamNameHi,
            pakshaTa = pakshaTa,
            pakshaEn = pakshaEn,
            pakshaHi = pakshaHi,
            ayanamTa = ayanamTa,
            ayanamEn = ayanamEn,
            ayanamHi = ayanamHi,
            rituTa = rituTa,
            rituEn = rituEn,
            rituHi = rituHi,
            dayOfWeekTa = dayTa,
            dayOfWeekEn = dayEn,
            dayOfWeekHi = dayHi,
            vasaramTa = vasaramTa,
            vasaramEn = vasaramEn,
            vasaramHi = vasaramHi,
            chandraRasiTa = chandraRasiTa,
            chandraRasiEn = chandraRasiEn,
            chandraRasiHi = chandraRasiHi,
            suryaRasiTa = suryaRasiTa,
            suryaRasiEn = suryaRasiEn,
            suryaRasiHi = suryaRasiHi,
            chandrashtamamTa = chandrashtamamTa,
            chandrashtamamEn = chandrashtamamEn,
            chandrashtamamHi = chandrashtamamHi,
            dishaSoolaTa = dishaSoolaTa,
            dishaSoolaEn = dishaSoolaEn,
            dishaSoolaHi = dishaSoolaHi,
            soolaPariharamTa = soolaPariharamTa,
            soolaPariharamEn = soolaPariharamEn,
            soolaPariharamHi = soolaPariharamHi,
            dinaYogamTa = dinaYogaTa,
            dinaYogamEn = dinaYogaEn,
            dinaYogamHi = dinaYogaHi
        )
    }

    private fun calculateEphemeris(date: LocalDate, hourFractionUtc: Double): EphemerisData {
        val y = date.year
        val m = date.monthValue
        val d = date.dayOfMonth

        val jd = getJulianDay(y, m, d, hourFractionUtc)
        val t = (jd - 2451545.0) / 36525.0

        // Lahiri Ayanamsha (Chitra Paksha)
        val ayanamsha = 23.8564 + (1.396887 * t)

        // 1. Sun Tropical Longitude
        val l0 = normalizeDegrees(280.46646 + 36000.76983 * t + 0.0003032 * t * t)
        val mSun = normalizeDegrees(357.52911 + 35999.05029 * t - 0.0001537 * t * t)
        val mSunRad = Math.toRadians(mSun)
        val cSun = (1.914602 - 0.004817 * t - 0.000014 * t * t) * sin(mSunRad) +
                (0.019993 - 0.000101 * t) * sin(2 * mSunRad) +
                0.000289 * sin(3 * mSunRad)
        val sunTropical = normalizeDegrees(l0 + cSun)
        val sunSidereal = normalizeDegrees(sunTropical - ayanamsha)

        // 2. Moon Tropical Longitude
        val lMoon = normalizeDegrees(218.3164477 + 481267.881279 * t - 0.0015786 * t * t)
        val dMoon = normalizeDegrees(297.8501921 + 445267.1114034 * t - 0.0018819 * t * t)
        val mMoon = normalizeDegrees(134.9633964 + 477198.8675055 * t + 0.0087414 * t * t)
        val fMoon = normalizeDegrees(93.2720950 + 483202.0175233 * t - 0.0036539 * t * t)

        val dRad = Math.toRadians(dMoon)
        val mMoonRad = Math.toRadians(mMoon)
        val fRad = Math.toRadians(fMoon)

        val deltaLMoon = 6.288774 * sin(mMoonRad) +
                1.274027 * sin(2 * dRad - mMoonRad) +
                0.658314 * sin(2 * dRad) +
                0.213618 * sin(2 * mMoonRad) -
                0.185116 * sin(mSunRad) -
                0.114332 * sin(2 * fRad) +
                0.058793 * sin(2 * dRad - 2 * mMoonRad) +
                0.057066 * sin(2 * dRad - mSunRad - mMoonRad) +
                0.053322 * sin(2 * dRad + mMoonRad) +
                0.046100 * sin(2 * dRad - mSunRad) +
                0.041024 * sin(mMoonRad - mSunRad) -
                0.034728 * sin(dRad) -
                0.030465 * sin(mMoonRad + mSunRad)

        val moonTropical = normalizeDegrees(lMoon + deltaLMoon)
        val moonSidereal = normalizeDegrees(moonTropical - ayanamsha)

        return EphemerisData(
            sunTropical = sunTropical,
            sunSidereal = sunSidereal,
            moonTropical = moonTropical,
            moonSidereal = moonSidereal,
            ayanamsha = ayanamsha
        )
    }

    private fun getJulianDay(year: Int, month: Int, day: Int, dayFraction: Double): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + (a / 4)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + dayFraction + b - 1524.5
    }

    private fun normalizeDegrees(deg: Double): Double {
        var d = deg % 360.0
        if (d < 0) d += 360.0
        return d
    }

    private fun calculateDinaYoga(day: DayOfWeek, nakshatraIndex: Int): String {
        return when (day) {
            DayOfWeek.SUNDAY -> when (nakshatraIndex) {
                0, 11, 12, 19, 21, 25, 26 -> "அமிர்த யோகம் (Amrita Yoga)"
                1, 5, 9, 15, 17 -> "மரண யோகம் (Marana Yoga)"
                else -> "சித்த யோகம் (Siddha Yoga)"
            }
            DayOfWeek.MONDAY -> when (nakshatraIndex) {
                3, 4, 5, 6, 7, 12, 14, 21, 23, 24, 25 -> "அமிர்த யோகம் (Amrita Yoga)"
                2, 8, 13, 18, 22 -> "மரண யோகம் (Marana Yoga)"
                else -> "சித்த யோகம் (Siddha Yoga)"
            }
            DayOfWeek.TUESDAY -> when (nakshatraIndex) {
                9, 10, 16, 20 -> "அமிர்த யோகம் (Amrita Yoga)"
                3, 12, 23 -> "மரண யோகம் (Marana Yoga)"
                else -> "சித்த யோகம் (Siddha Yoga)"
            }
            DayOfWeek.WEDNESDAY -> when (nakshatraIndex) {
                1, 2, 16, 17, 18, 22 -> "அமிர்த யோகம் (Amrita Yoga)"
                10, 24 -> "மரண யோகம் (Marana Yoga)"
                else -> "சித்த யோகம் (Siddha Yoga)"
            }
            DayOfWeek.THURSDAY -> when (nakshatraIndex) {
                0, 6, 7, 8, 11, 14, 15, 26 -> "அமிர்த யோகம் (Amrita Yoga)"
                4, 19 -> "மரண யோகம் (Marana Yoga)"
                else -> "சித்த யோகம் (Siddha Yoga)"
            }
            DayOfWeek.FRIDAY -> when (nakshatraIndex) {
                8, 9, 10, 13, 23 -> "அமிர்த யோகம் (Amrita Yoga)"
                5, 17, 21, 26 -> "மரண யோகம் (Marana Yoga)"
                else -> "சித்த யோகம் (Siddha Yoga)"
            }
            DayOfWeek.SATURDAY -> when (nakshatraIndex) {
                1, 14, 15 -> "அமிர்த யோகம் (Amrita Yoga)"
                0, 7, 12, 20, 25 -> "மரண யோகம் (Marana Yoga)"
                else -> "சித்த யோகம் (Siddha Yoga)"
            }
        }
    }

    private fun resolveLocation(location: String): LocationCoordinates {
        // Direct match
        locationMap[location]?.let { return it }
        // Partial / fuzzy match
        for ((key, value) in locationMap) {
            if (location.contains(key, ignoreCase = true) || key.contains(location, ignoreCase = true) ||
                location.contains(value.nameTa, ignoreCase = true) || (location.contains("Chennai", ignoreCase = true) && key.contains("Chennai", ignoreCase = true)) ||
                (location.contains("சென்னை", ignoreCase = true) && key.contains("சென்னை", ignoreCase = true)) ||
                (location.contains("India", ignoreCase = true) && key.contains("India", ignoreCase = true)) ||
                (location.contains("Nadi", ignoreCase = true) && key.contains("Nadi", ignoreCase = true)) ||
                (location.contains("நாடி", ignoreCase = true) && key.contains("நாடி", ignoreCase = true))) {
                return value
            }
        }
        return LocationCoordinates(-17.80, 177.41, 12.0, "நாடி, பிஜி", "Nadi, Fiji", "नादी, फिजी")
    }

    private fun calculateSunriseSunsetDec(date: LocalDate, lat: Double, lon: Double, tzOffset: Double): Pair<Double, Double> {
        val dayOfYear = date.dayOfYear
        val b = 2.0 * Math.PI * (dayOfYear - 81) / 365.0
        val eot = 9.87 * sin(2 * b) - 7.53 * cos(b) - 1.5 * sin(b)
        val declination = 23.45 * sin(b)

        val latRad = Math.toRadians(lat)
        val decRad = Math.toRadians(declination)

        val cosH = (cos(Math.toRadians(90.833)) - (sin(latRad) * sin(decRad))) / (cos(latRad) * cos(decRad))
        val hHours = if (cosH in -1.0..1.0) Math.toDegrees(acos(cosH)) / 15.0 else 6.0

        val solarNoonUtc = 12.0 - (lon / 15.0) - (eot / 60.0)
        val sunriseLocalDec = (solarNoonUtc - hHours + tzOffset + 24.0) % 24.0
        val sunsetLocalDec = (solarNoonUtc + hHours + tzOffset + 24.0) % 24.0

        return sunriseLocalDec to sunsetLocalDec
    }

    private fun calculateMoonriseMoonset(date: LocalDate, lat: Double, lon: Double, tzOffset: Double, elongation: Double): Pair<String, String> {
        val moonLagHours = (elongation / 360.0) * 24.0
        val moonriseDec = (6.0 + moonLagHours) % 24.0
        val moonsetDec = (18.0 + moonLagHours) % 24.0
        return formatDecTime(moonriseDec) to formatDecTime(moonsetDec)
    }

    private fun calculateDynamicInauspiciousTimes(day: DayOfWeek, sunriseDec: Double, sunsetDec: Double): Triple<String, String, String> {
        val dayDuration = if (sunsetDec > sunriseDec) sunsetDec - sunriseDec else (sunsetDec + 24.0 - sunriseDec)
        val part = dayDuration / 8.0

        fun getSegment(partIndex1Based: Int): String {
            val start = (sunriseDec + (partIndex1Based - 1) * part) % 24.0
            val end = (sunriseDec + partIndex1Based * part) % 24.0
            return "${formatDecTime(start)} - ${formatDecTime(end)}"
        }

        return when (day) {
            DayOfWeek.SUNDAY -> Triple(getSegment(8), getSegment(5), getSegment(7))
            DayOfWeek.MONDAY -> Triple(getSegment(2), getSegment(4), getSegment(6))
            DayOfWeek.TUESDAY -> Triple(getSegment(7), getSegment(3), getSegment(5))
            DayOfWeek.WEDNESDAY -> Triple(getSegment(5), getSegment(2), getSegment(4))
            DayOfWeek.THURSDAY -> Triple(getSegment(6), getSegment(1), getSegment(3))
            DayOfWeek.FRIDAY -> Triple(getSegment(4), getSegment(7), getSegment(2))
            DayOfWeek.SATURDAY -> Triple(getSegment(3), getSegment(6), getSegment(1))
        }
    }

    private fun getNallaNeram(day: DayOfWeek): Pair<String, String> {
        return when (day) {
            DayOfWeek.SUNDAY -> "07:30 AM - 08:30 AM" to "03:30 PM - 04:30 PM"
            DayOfWeek.MONDAY -> "06:30 AM - 07:30 AM" to "04:30 PM - 05:30 PM"
            DayOfWeek.TUESDAY -> "07:30 AM - 08:30 AM" to "04:30 PM - 05:30 PM"
            DayOfWeek.WEDNESDAY -> "09:30 AM - 10:30 AM" to "01:30 PM - 02:30 PM"
            DayOfWeek.THURSDAY -> "09:30 AM - 10:30 AM" to "04:30 PM - 05:30 PM"
            DayOfWeek.FRIDAY -> "06:30 AM - 07:30 AM" to "04:30 PM - 05:30 PM"
            DayOfWeek.SATURDAY -> "07:30 AM - 08:30 AM" to "05:00 PM - 06:00 PM"
        }
    }

    private fun getGowriNallaNeram(day: DayOfWeek): Pair<String, String> {
        return when (day) {
            DayOfWeek.SUNDAY -> "10:30 AM - 11:30 AM" to "09:30 PM - 10:30 PM"
            DayOfWeek.MONDAY -> "09:30 AM - 10:30 AM" to "07:30 PM - 08:30 PM"
            DayOfWeek.TUESDAY -> "10:30 AM - 11:30 AM" to "07:30 PM - 08:30 PM"
            DayOfWeek.WEDNESDAY -> "06:30 AM - 07:30 AM" to "06:30 PM - 07:30 PM"
            DayOfWeek.THURSDAY -> "06:30 AM - 07:30 AM" to "07:30 PM - 08:30 PM"
            DayOfWeek.FRIDAY -> "09:30 AM - 10:30 AM" to "06:30 PM - 07:30 PM"
            DayOfWeek.SATURDAY -> "10:30 AM - 11:30 AM" to "09:30 PM - 10:30 PM"
        }
    }

    private fun formatDecTime(decHours: Double): String {
        val totalMins = (decHours * 60).toInt() % (24 * 60)
        val h = totalMins / 60
        val m = totalMins % 60
        val amPm = if (h >= 12) "PM" else "AM"
        val displayH = if (h == 0) 12 else if (h > 12) h - 12 else h
        return String.format(Locale.ENGLISH, "%02d:%02d %s", displayH, m, amPm)
    }

    private fun formatLocalEndTime(hoursFromMidnight: Double): String {
        val normalized = (hoursFromMidnight + 24.0) % 24.0
        return formatDecTime(normalized)
    }
}
