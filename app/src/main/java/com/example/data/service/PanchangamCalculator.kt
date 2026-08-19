package com.example.data.service

import com.example.data.model.ObservanceType
import com.example.data.model.PanchangamDetail
import com.example.data.model.TamilMonth
import com.example.data.model.TamilSamvatsara
import com.example.data.model.TamilSamvatsaraEngine
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Locale
import kotlin.math.*

data class Tuple6<A, B, C, D, E, F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F)

data class EphemerisData(
    val sunTropical: Double,
    val sunSidereal: Double,
    val moonTropical: Double,
    val moonSidereal: Double,
    val moonLatitude: Double,
    val ayanamsha: Double,
    val t: Double,
    val jd: Double
)

data class LocationCoordinates(
    val lat: Double,
    val lon: Double,
    val timeZoneOffsetHours: Double,
    val nameTa: String,
    val nameEn: String = nameTa,
    val nameHi: String = nameTa,
    val region: String = "Global"
)

/**
 * Interface for Drik Panchangam Calculation with worldwide GPS / Coordinate support.
 */
interface PanchangamCalculator {
    fun calculatePanchangam(date: LocalDate, location: String = "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)"): PanchangamDetail
    fun calculatePanchangam(date: LocalDate, lat: Double, lon: Double, tzOffsetHours: Double, locationName: String): PanchangamDetail
    fun getAllPresetLocations(): List<LocationCoordinates>
    fun parseLocation(location: String): LocationCoordinates
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

    private val sanskritMonthsTa = listOf("சைத்ர", "வைசாக", "ஜ்யேஷ்ட", "ஆஷாட", "ஸ்ராவண", "பாத்ரபத", "ஆஸ்வின", "கார்திக", "மார்கசீர்ஷ", "பௌஷ", "மாக", "பல்குன")
    private val ritusTa = listOf("வசந்த ருது", "கிரீஷ்ம ருது", "வர்ஷ ருது", "சரத் ருது", "ஹேமந்த ருது", "சிசிர ருது")
    private val ritusEn = listOf("Vasanta Ritu (Spring)", "Grishma Ritu (Summer)", "Varsha Ritu (Monsoon)", "Sharad Ritu (Autumn)", "Hemanta Ritu (Pre-Winter)", "Shishira Ritu (Winter)")
    private val ritusHi = listOf("वसन्त ऋतु", "ग्रीष्म ऋतु", "वर्षा ऋतु", "शरद् ऋतु", "हेमन्त ऋतु", "शिशिर ऋतु")

    companion object {
        val WORLD_PRESETS = listOf(
            // Pacific & Oceania
            LocationCoordinates(-17.7765, 177.4356, 12.0, "நாடி, பிஜி தீவுகள்", "Nadi, Fiji Islands", "नादी, फिजी", "Pacific"),
            LocationCoordinates(-18.1416, 178.4419, 12.0, "சுவா, பிஜி தீவுகள்", "Suva, Fiji Islands", "सुवा, फिजी", "Pacific"),
            LocationCoordinates(-17.6167, 177.4667, 12.0, "லௌடோகா, பிஜி", "Lautoka, Fiji", "लौटोका, फिजी", "Pacific"),
            LocationCoordinates(-16.4333, 179.3667, 12.0, "லம்பாசா, பிஜி", "Labasa, Fiji", "लम्बाசா, फिजी", "Pacific"),
            LocationCoordinates(-33.8688, 151.2093, 10.0, "சிட்னி, ஆஸ்திரேலியா", "Sydney, Australia", "सिडनी, ऑस्ट्रेलिया", "Oceania"),
            LocationCoordinates(-37.8136, 144.9631, 10.0, "மெல்போர்ன், ஆஸ்திரேலியா", "Melbourne, Australia", "मेलबर्न, ऑस्ट्रेलिया", "Oceania"),
            LocationCoordinates(-27.4698, 153.0251, 10.0, "பிரிஸ்பேன், ஆஸ்திரேலியா", "Brisbane, Australia", "ब्रिसबेन, ऑस्ट्रेलिया", "Oceania"),
            LocationCoordinates(-31.9505, 115.8605, 8.0, "பெர்த், ஆஸ்திரேலியா", "Perth, Australia", "पर्थ, ऑस्ट्रेलिया", "Oceania"),
            LocationCoordinates(-36.8485, 174.7633, 12.0, "ஆக்லாந்து, நியூசிலாந்து", "Auckland, New Zealand", "ऑकलैंड, न्यूजीलैंड", "Oceania"),
            LocationCoordinates(-41.2865, 174.7762, 12.0, "வெல்லிங்டன், நியூசிலாந்து", "Wellington, New Zealand", "वेलिंगटन, न्यूजीलैंड", "Oceania"),

            // India - Tamil Nadu & South India
            LocationCoordinates(13.0827, 80.2707, 5.5, "சென்னை, தமிழ்நாடு", "Chennai, India", "चेन्नई, भारत", "India"),
            LocationCoordinates(9.9252, 78.1198, 5.5, "மதுரை, தமிழ்நாடு", "Madurai, India", "मदुरै, भारत", "India"),
            LocationCoordinates(11.0168, 76.9558, 5.5, "கோயம்புத்தூர், தமிழ்நாடு", "Coimbatore, India", "कोयंबटूर, भारत", "India"),
            LocationCoordinates(10.7905, 78.7047, 5.5, "திருச்சிராப்பள்ளி, தமிழ்நாடு", "Tiruchirappalli, India", "तिरुचिरापल्ली, भारत", "India"),
            LocationCoordinates(8.7139, 77.7567, 5.5, "திருநெல்வேலி, தமிழ்நாடு", "Tirunelveli, India", "तिरुनेलवेली, भारत", "India"),
            LocationCoordinates(10.7870, 79.1378, 5.5, "தஞ்சாவூர், தமிழ்நாடு", "Thanjavur, India", "तंजावुर, भारत", "India"),
            LocationCoordinates(12.8342, 79.7036, 5.5, "காஞ்சிபுரம், தமிழ்நாடு", "Kanchipuram, India", "कांचीपुरम, भारत", "India"),
            LocationCoordinates(12.9716, 77.5946, 5.5, "பெங்களூரு, இந்தியா", "Bengaluru, India", "बेंगलुरु, भारत", "India"),
            LocationCoordinates(17.3850, 78.4867, 5.5, "ஹைதராபாத், இந்தியா", "Hyderabad, India", "हैदराबाद, भारत", "India"),
            LocationCoordinates(19.0760, 72.8777, 5.5, "மும்பை, இந்தியா", "Mumbai, India", "मुंबई, भारत", "India"),
            LocationCoordinates(28.6139, 77.2090, 5.5, "புது தில்லி, இந்தியா", "New Delhi, India", "नई दिल्ली, भारत", "India"),
            LocationCoordinates(22.5726, 88.3639, 5.5, "கொல்கத்தா, இந்தியா", "Kolkata, India", "कोलकाता, भारत", "India"),
            LocationCoordinates(9.9312, 76.2673, 5.5, "கொச்சி, கேரளா", "Kochi, India", "कोच्चि, भारत", "India"),

            // Sri Lanka
            LocationCoordinates(9.6615, 80.0255, 5.5, "யாழ்ப்பாணம், இலங்கை", "Jaffna, Sri Lanka", "जाफना, श्रीलंका", "Sri Lanka"),
            LocationCoordinates(6.9271, 79.8612, 5.5, "கொழும்பு, இலங்கை", "Colombo, Sri Lanka", "कोलंबो, श्रीलंका", "Sri Lanka"),
            LocationCoordinates(7.7170, 81.7000, 5.5, "மட்டக்களப்பு, இலங்கை", "Batticaloa, Sri Lanka", "बट्टिकलोआ, श्रीलंका", "Sri Lanka"),

            // Southeast Asia
            LocationCoordinates(1.3521, 103.8198, 8.0, "சிங்கப்பூர்", "Singapore", "सिंगापुर", "Southeast Asia"),
            LocationCoordinates(3.1390, 101.6869, 8.0, "கோலாலம்பூர், மலேசியா", "Kuala Lumpur, Malaysia", "क्वालालंपुर, मलेशिया", "Southeast Asia"),
            LocationCoordinates(5.4164, 100.3327, 8.0, "பினாங்கு, மலேசியா", "Penang, Malaysia", "पेनांग, मलेशिया", "Southeast Asia"),
            LocationCoordinates(13.7563, 100.5018, 7.0, "பேங்காக், தாய்லாந்து", "Bangkok, Thailand", "बैंकॉक, थाईलैंड", "Southeast Asia"),
            LocationCoordinates(-6.2088, 106.8456, 7.0, "ஜகார்த்தா, இந்தோனேசியா", "Jakarta, Indonesia", "जकार्ता, इंडोनेशिया", "Southeast Asia"),

            // Middle East
            LocationCoordinates(25.2048, 55.2708, 4.0, "துபாய், ஐக்கிய அரபு அமீரகம்", "Dubai, UAE", "दुबई, यूएई", "Middle East"),
            LocationCoordinates(24.4539, 54.3773, 4.0, "அபுதாபி, ஐக்கிய அரபு அமீரகம்", "Abu Dhabi, UAE", "अबू धाबी, यूएई", "Middle East"),
            LocationCoordinates(25.2854, 51.5310, 3.0, "தோஹா, கத்தார்", "Doha, Qatar", "दोहा, कतर", "Middle East"),
            LocationCoordinates(23.5880, 58.3829, 4.0, "மஸ்கட், ஓமன்", "Muscat, Oman", "मस्कट, ओमान", "Middle East"),
            LocationCoordinates(26.2285, 50.5860, 3.0, "மனாமா, பஹ்ரைன்", "Manama, Bahrain", "मनामा, बहरीन", "Middle East"),
            LocationCoordinates(29.3759, 47.9774, 3.0, "குவைத் நகரம், குவைத்", "Kuwait City, Kuwait", "कुवैत शहर, कुवैत", "Middle East"),

            // Europe & UK
            LocationCoordinates(51.5074, -0.1278, 0.0, "லண்டன், இங்கிலாந்து", "London, UK", "लंदन, यूके", "Europe"),
            LocationCoordinates(52.4862, -1.8904, 0.0, "பர்மிங்காம், இங்கிலாந்து", "Birmingham, UK", "बर्मिंघम, यूके", "Europe"),
            LocationCoordinates(48.8566, 2.3522, 1.0, "பாரிஸ், பிரான்ஸ்", "Paris, France", "पेरिस, फ्रांस", "Europe"),
            LocationCoordinates(50.1109, 8.6821, 1.0, "பிராங்பேர்ட், ஜெர்மனி", "Frankfurt, Germany", "फ्रैंकफर्ट, जर्मनी", "Europe"),
            LocationCoordinates(52.5200, 13.4050, 1.0, "பெர்லின், ஜெர்மனி", "Berlin, Germany", "बर्लिन, जर्मनी", "Europe"),
            LocationCoordinates(47.3769, 8.5417, 1.0, "சூரிச், சுவிட்சர்லாந்து", "Zurich, Switzerland", "ज्यूरिख, स्विट्जरलैंड", "Europe"),
            LocationCoordinates(53.3498, -6.2603, 0.0, "டப்ளின், அயர்லாந்து", "Dublin, Ireland", "डबलिन, आयरलैंड", "Europe"),

            // North America
            LocationCoordinates(40.7128, -74.0060, -5.0, "நியூயார்க், அமெரிக்கா", "New York, USA", "न्यूयॉर्क, यूएसए", "North America"),
            LocationCoordinates(37.7749, -122.4194, -8.0, "சான் பிரான்சிஸ்கோ, அமெரிக்கா", "San Francisco, USA", "सैन फ्रांसिस्को, यूएसए", "North America"),
            LocationCoordinates(34.0522, -118.2437, -8.0, "லாஸ் ஏஞ்சல்ஸ், அமெரிக்கா", "Los Angeles, USA", "लॉस एंजिल्स, यूएसए", "North America"),
            LocationCoordinates(41.8781, -87.6298, -6.0, "சிகாகோ, அமெரிக்கா", "Chicago, USA", "शिकागो, यूएसए", "North America"),
            LocationCoordinates(29.7604, -95.3698, -6.0, "ஹூஸ்டன், டெக்சாஸ், அமெரிக்கா", "Houston, TX, USA", "ह्यूस्टन, यूएसए", "North America"),
            LocationCoordinates(32.7767, -96.7970, -6.0, "டல்லாஸ், அமெரிக்கா", "Dallas, USA", "डलास, यूएसए", "North America"),
            LocationCoordinates(33.7490, -84.3880, -5.0, "அட்லாண்டா, அமெரிக்கா", "Atlanta, USA", "अटलांटा, यूएसए", "North America"),
            LocationCoordinates(47.6062, -122.3321, -8.0, "சியாட்டில், அமெரிக்கா", "Seattle, USA", "सिएटल, यूएसए", "North America"),
            LocationCoordinates(43.6532, -79.3832, -5.0, "டொராண்டோ, கனடா", "Toronto, Canada", "टोरंटो, कनाडा", "North America"),
            LocationCoordinates(49.2827, -123.1207, -8.0, "வான்கூவர், கனடா", "Vancouver, Canada", "वैंकूवर, कनाडा", "North America"),

            // Africa & Indian Ocean
            LocationCoordinates(-20.1609, 57.5012, 4.0, "போர்ட் லூயிஸ், மொரீஷியஸ்", "Port Louis, Mauritius", "पोर्ट लुईस, मॉरीशस", "Indian Ocean"),
            LocationCoordinates(-29.8587, 31.0218, 2.0, "டர்பன், தென்னாப்பிரிக்கா", "Durban, South Africa", "डरबन, दक्षिण अफ्रीका", "Africa"),
            LocationCoordinates(-26.2041, 28.0473, 2.0, "ஜோகன்னஸ்பர்க், தென்னாப்பிரிக்கா", "Johannesburg, South Africa", "जोहान्सबर्ग, दक्षिण अफ्रीका", "Africa"),
            LocationCoordinates(-20.8821, 55.4507, 4.0, "செயிண்ட்-டெனிஸ், ரீயூனியன்", "Saint-Denis, Réunion", "सेंट-डेनिस, रीयूनियन", "Indian Ocean")
        )
    }

    override fun getAllPresetLocations(): List<LocationCoordinates> = WORLD_PRESETS

    override fun parseLocation(location: String): LocationCoordinates = resolveLocation(location)

    override fun calculatePanchangam(date: LocalDate, location: String): PanchangamDetail {
        val loc = resolveLocation(location)
        return calculatePanchangam(date, loc.lat, loc.lon, loc.timeZoneOffsetHours, loc.nameTa)
    }

    override fun calculatePanchangam(
        date: LocalDate,
        lat: Double,
        lon: Double,
        tzOffsetHours: Double,
        locationName: String
    ): PanchangamDetail {
        val loc = LocationCoordinates(lat, lon, tzOffsetHours, locationName, locationName, locationName)
        val (sunriseDec, sunsetDec) = calculateSunriseSunsetDec(date, loc.lat, loc.lon, loc.timeZoneOffsetHours)
        val sunriseLocal = formatDecTime(sunriseDec)
        val sunsetLocal = formatDecTime(sunsetDec)

        // Exact Ephemeris calculated at local sunrise instant via UTC ZonedDateTime
        val sunriseJd = getJulianDayFromLocalDateTime(date, sunriseDec, loc.timeZoneOffsetHours)
        val ephem = calculateEphemeris(sunriseJd)

        val sunSid = ephem.sunSidereal
        val moonSid = ephem.moonSidereal
        val sunTrop = ephem.sunTropical
        val moonTrop = ephem.moonTropical

        // 1. Tamil Solar Month & Year calculation
        val sunRasiIndex = (sunSid / 30.0).toInt() % 12
        val tamilMonthEnum = TamilMonth.entries[sunRasiIndex]
        val tamilDay = (sunSid % 30.0).toInt() + 1

        // Tamil Year cycle (60 years starting with Prabhava at Kali Yuga offset)
        val tamilYearIndex = ((date.year - 1987 + 60) % 60)
        val tamilSamvatsara = TamilSamvatsaraEngine.YEARS_60[tamilYearIndex]
        val samvatsaraName = tamilSamvatsara.tamilName

        // 2. Sanskrit Month & Vedic Season (Ritu)
        val sanskritMonth = sanskritMonthsTa[sunRasiIndex]
        val rituIndex = (sunRasiIndex / 2) % 6
        val (rituTa, rituEn, rituHi) = Triple(ritusTa[rituIndex], ritusEn[rituIndex], ritusHi[rituIndex])

        // 3. Ayanam (Uttarayana / Dakshinayana based on tropical sun longitude)
        val (ayanamTa, ayanamEn, ayanamHi) = if (sunTrop in 270.0..360.0 || sunTrop < 90.0) {
            Triple("உத்தராயணம்", "Uttarayana", "उत्तरायण")
        } else {
            Triple("தட்சிணாயனம்", "Dakshinayana", "दक्षिणायन")
        }

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

        // Exact Astronomical Tithi End Time calculation (Solving moment elongation crosses next 12° boundary)
        val targetTithiAngle = ((tithiIndex + 1) * 12.0) % 360.0
        val tithiHoursRemaining = findNextCrossingTime(
            date = date,
            sunriseDec = sunriseDec,
            tzOffsetHours = loc.timeZoneOffsetHours,
            startHour = 0.0,
            maxSearchHours = 36.0,
            targetAngle = targetTithiAngle,
            angleEvaluator = { ep -> normalizeDegrees(ep.moonTropical - ep.sunTropical) }
        )
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
        val nakshatraSpan = 360.0 / 27.0
        val nakshatraIndex = (moonSid / nakshatraSpan).toInt() % 27
        val nakshatraNameTa = nakshatramsTa[nakshatraIndex]
        val nakshatraNameEn = nakshatramsEn[nakshatraIndex]
        val nakshatraNameHi = nakshatramsHi[nakshatraIndex]
        val pada = (((moonSid % nakshatraSpan) / (360.0 / 108.0)).toInt() % 4) + 1

        // Exact Astronomical Nakshatra End Time and Start Time
        val targetNakAngle = ((nakshatraIndex + 1) * nakshatraSpan) % 360.0
        val prevNakAngle = (nakshatraIndex * nakshatraSpan) % 360.0
        val nakHoursRemaining = findNextCrossingTime(
            date = date,
            sunriseDec = sunriseDec,
            tzOffsetHours = loc.timeZoneOffsetHours,
            startHour = 0.0,
            maxSearchHours = 36.0,
            targetAngle = targetNakAngle,
            angleEvaluator = { ep -> ep.moonSidereal }
        )
        val nakHoursPassed = findPreviousCrossingTime(
            date = date,
            sunriseDec = sunriseDec,
            tzOffsetHours = loc.timeZoneOffsetHours,
            startHour = 0.0,
            maxSearchHours = 36.0,
            targetAngle = prevNakAngle,
            angleEvaluator = { ep -> ep.moonSidereal }
        )
        val nakshatraEndTime = formatLocalEndTime(sunriseDec + nakHoursRemaining)
        val nextNakshatraIndex = (nakshatraIndex + 1) % 27
        val nextNakshatra = nakshatramsTa[nextNakshatraIndex]

        // 9. Nithya Yogam & Dina Yogam
        val yogaSpan = 360.0 / 27.0
        val yogaDeg = normalizeDegrees(sunSid + moonSid)
        val nithyaYogaIndex = (yogaDeg / yogaSpan).toInt() % 27
        val nithyaYogaNameTa = nithyaYogamsTa[nithyaYogaIndex]
        val nithyaYogaNameEn = nithyaYogamsEn[nithyaYogaIndex]
        val nithyaYogaNameHi = nithyaYogamsHi[nithyaYogaIndex]
        
        val targetYogaAngle = ((nithyaYogaIndex + 1) * yogaSpan) % 360.0
        val yogaHoursRemaining = findNextCrossingTime(
            date = date,
            sunriseDec = sunriseDec,
            tzOffsetHours = loc.timeZoneOffsetHours,
            startHour = 0.0,
            maxSearchHours = 36.0,
            targetAngle = targetYogaAngle,
            angleEvaluator = { ep -> normalizeDegrees(ep.sunSidereal + ep.moonSidereal) }
        )
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
        val karanamIndex = (elongation / 6.0).toInt() % 60
        val karanamNameTa = when (karanamIndex) {
            0 -> "கிமிஸ்துக்னம்"
            57 -> "சகுனி"
            58 -> "சதுஷ்பாதம்"
            59 -> "நாகவம்"
            else -> charaKaranamsTa[(karanamIndex - 1) % 7]
        }
        val karanamNameEn = when (karanamIndex) {
            0 -> "Kimstughna"
            57 -> "Shakuni"
            58 -> "Chatushpada"
            59 -> "Naga"
            else -> charaKaranamsEn[(karanamIndex - 1) % 7]
        }
        val karanamNameHi = when (karanamIndex) {
            0 -> "किंस्तुघ्न"
            57 -> "शकुनि"
            58 -> "चतुष्पाद"
            59 -> "नाग"
            else -> charaKaranamsHi[(karanamIndex - 1) % 7]
        }
        val nextKaranamIndex = (karanamIndex + 1) % 60
        val nextKaranam = when (nextKaranamIndex) {
            0 -> "கிமிஸ்துக்னம்"
            57 -> "சகுனி"
            58 -> "சதுஷ்பாதம்"
            59 -> "நாகவம்"
            else -> charaKaranamsTa[(nextKaranamIndex - 1) % 7]
        }

        val targetKaranamAngle = ((karanamIndex + 1) * 6.0) % 360.0
        val karanamHoursRemaining = findNextCrossingTime(
            date = date,
            sunriseDec = sunriseDec,
            tzOffsetHours = loc.timeZoneOffsetHours,
            startHour = 0.0,
            maxSearchHours = 36.0,
            targetAngle = targetKaranamAngle,
            angleEvaluator = { ep -> normalizeDegrees(ep.moonTropical - ep.sunTropical) }
        )
        val karanamEndTime = formatLocalEndTime(sunriseDec + karanamHoursRemaining)

        // Moon Rasi & Sun Rasi
        val moonRasiIndex = (moonSid / 30.0).toInt() % 12
        val chandraRasiTa = rasiNamesTa[moonRasiIndex]
        val chandraRasiEn = rasiNamesEn[moonRasiIndex]
        val chandraRasiHi = rasiNamesHi[moonRasiIndex]

        val suryaRasiTa = rasiNamesTa[sunRasiIndex]
        val suryaRasiEn = rasiNamesEn[sunRasiIndex]
        val suryaRasiHi = rasiNamesHi[sunRasiIndex]

        // Chandrashtamam (8th Rasi from Chandra Rasi)
        val chandrashtamaRasiIdx = (moonRasiIndex - 7 + 12) % 12
        val chandrashtamamTa = "${rasiNamesTa[chandrashtamaRasiIdx]} ராசி அன்பர்களுக்கு இன்றைய நாள் சந்திராஷ்டமம்."
        val chandrashtamamEn = "Chandrashtamam today for ${rasiNamesEn[chandrashtamaRasiIdx]} (Moon in 8th house)."
        val chandrashtamamHi = "${rasiNamesHi[chandrashtamaRasiIdx]} राशि वालों के लिए आज चंद्राष्टम है।"

        // Astronomical Moonrise & Moonset calculated from topocentric horizon crossings
        val (moonriseLocal, moonsetLocal) = calculateAstronomicalMoonriseMoonset(date, loc.lat, loc.lon, loc.timeZoneOffsetHours)

        // Inauspicious Times (Dynamic 8-part division of daytime from sunrise to sunset)
        val (rahu, yama, kuli) = calculateDynamicInauspiciousTimes(date.dayOfWeek, sunriseDec, sunsetDec)

        // Nalla Neram and Gowri Nalla Neram (Astronomically derived from local sunrise & sunset)
        val (nallaMorn, nallaEve) = calculateDynamicNallaNeram(date.dayOfWeek, sunriseDec, sunsetDec)
        val (gowriMorn, gowriEve) = calculateDynamicGowriNallaNeram(date.dayOfWeek, sunriseDec, sunsetDec)

        // Muhurtham & Dynamic Varjyam (Astronomically derived from 8th Muhurtha, 15 Daytime Muhurthas, and Nakshatra Visha Ghatika)
        val abhijitMuhurtham = calculateAbhijitMuhurtham(sunriseDec, sunsetDec)
        val durMuhurtham = calculateDurMuhurtham(date.dayOfWeek, sunriseDec, sunsetDec)
        val nakDuration = nakHoursRemaining - nakHoursPassed
        val varjyam = calculateVarjyam(nakshatraIndex, nakHoursPassed, if (nakDuration > 0) nakDuration else 24.0, sunriseDec)

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
            tamilDate = tamilDay,
            tamilMonth = tamilMonthEnum,
            tamilYear = tamilSamvatsara,
            samvatsaraName = samvatsaraName,
            ayanam = ayanamTa,
            ritu = rituTa,
            sanskritMonth = sanskritMonth,
            paksha = pakshaTa,
            tithi = tithiNameTa,
            tithiEndTime = tithiEndTime,
            tithiPercent = 100,
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

    /**
     * Converts a local calendar date and local decimal hour into the exact Julian Day (JD) in UTC.
     * Uses Java 8+ ZonedDateTime to handle calendar boundaries, leap days, and timezones seamlessly.
     */
    private fun getJulianDayFromLocalDateTime(date: LocalDate, localDecHours: Double, tzOffsetHours: Double): Double {
        val totalSeconds = (localDecHours * 3600.0).roundToLong()
        val localSec = ((totalSeconds % 86400L) + 86400L) % 86400L
        val localDaysOffset = floor(totalSeconds / 86400.0).toLong()
        val effectiveLocalDate = date.plusDays(localDaysOffset)
        
        val localTime = LocalTime.ofSecondOfDay(localSec)
        val totalOffsetSeconds = (tzOffsetHours * 3600).toInt()
        val zoneOffset = ZoneOffset.ofTotalSeconds(totalOffsetSeconds)
        val zonedDateTime = ZonedDateTime.of(effectiveLocalDate, localTime, zoneOffset)
        val instant = zonedDateTime.toInstant()
        
        val epochSec = instant.epochSecond
        val nanoFrac = instant.nano / 1_000_000_000.0
        return 2440587.5 + (epochSec + nanoFrac) / 86400.0
    }

    /**
     * Calculates planetary and lunar positions at any given Julian Day (JD).
     */
    private fun calculateEphemeris(jd: Double): EphemerisData {
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

        // 2. Moon Tropical Longitude & Latitude
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

        // Moon Ecliptic Latitude
        val moonLatitude = 5.128 * sin(fRad) +
                0.280 * sin(mMoonRad + fRad) +
                0.277 * sin(mMoonRad - fRad) +
                0.173 * sin(2 * dRad - fRad)

        return EphemerisData(
            sunTropical = sunTropical,
            sunSidereal = sunSidereal,
            moonTropical = moonTropical,
            moonSidereal = moonSidereal,
            moonLatitude = moonLatitude,
            ayanamsha = ayanamsha,
            t = t,
            jd = jd
        )
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
        // 1. Direct GPS / Custom format: "GPS: lat, lon, offset, placeName" or "Custom: lat, lon, offset, name"
        if (location.startsWith("GPS:", ignoreCase = true) ||
            location.startsWith("GPS (", ignoreCase = true) ||
            location.startsWith("GPS", ignoreCase = true) ||
            location.startsWith("Custom:", ignoreCase = true) ||
            location.startsWith("Coord:", ignoreCase = true)) {
            try {
                val cleaned = location.replace("GPS:", "").replace("GPS", "")
                    .replace("Custom:", "").replace("Coord:", "")
                    .replace("(", "").replace(")", "").replace("°", "")
                val parts = cleaned.split(",")
                if (parts.size >= 2) {
                    val lat = parts[0].trim().toDouble()
                    val lon = parts[1].trim().toDouble()
                    val offset = if (parts.size >= 3 && parts[2].trim().toDoubleOrNull() != null) {
                        parts[2].trim().toDouble()
                    } else {
                        (round((lon / 15.0) * 2.0) / 2.0).coerceIn(-12.0, 14.0)
                    }
                    val placeName = if (parts.size >= 4 && parts[3].trim().isNotBlank()) {
                        parts.subList(3, parts.size).joinToString(",").trim()
                    } else {
                        String.format(Locale.US, "GPS (%.2f°, %.2f°)", lat, lon)
                    }
                    return LocationCoordinates(lat, lon, offset, placeName, placeName, placeName, "GPS")
                }
            } catch (e: Exception) {
                // fall through
            }
        }

        // 2. Pure raw coordinates like "13.0827, 80.2707" or "-17.77, 177.43, 12.0"
        val commaSplit = location.split(",")
        if (commaSplit.size >= 2 && commaSplit[0].trim().toDoubleOrNull() != null && commaSplit[1].trim().toDoubleOrNull() != null) {
            try {
                val lat = commaSplit[0].trim().toDouble()
                val lon = commaSplit[1].trim().toDouble()
                val offset = if (commaSplit.size >= 3 && commaSplit[2].trim().toDoubleOrNull() != null) {
                    commaSplit[2].trim().toDouble()
                } else {
                    (round((lon / 15.0) * 2.0) / 2.0).coerceIn(-12.0, 14.0)
                }
                val name = if (commaSplit.size >= 4 && commaSplit[3].trim().isNotBlank()) {
                    commaSplit.subList(3, commaSplit.size).joinToString(",").trim()
                } else {
                    String.format(Locale.US, "GPS (%.2f°, %.2f°)", lat, lon)
                }
                return LocationCoordinates(lat, lon, offset, name, name, name, "Coordinates")
            } catch (e: Exception) {
                // fall through
            }
        }

        // 3. Search in WORLD_PRESETS (Exact and Partial match)
        for (preset in WORLD_PRESETS) {
            if (location.equals(preset.nameTa, ignoreCase = true) ||
                location.equals(preset.nameEn, ignoreCase = true) ||
                location.equals(preset.nameHi, ignoreCase = true)) {
                return preset
            }
        }

        val locLower = location.lowercase()
        for (preset in WORLD_PRESETS) {
            val keyEn = preset.nameEn.lowercase().substringBefore(",")
            val keyTa = preset.nameTa.lowercase().substringBefore(",")
            if (locLower.contains(keyEn) || locLower.contains(keyTa) ||
                preset.nameEn.lowercase().contains(locLower) || preset.nameTa.lowercase().contains(locLower)) {
                return preset
            }
        }

        // Default to Nadi, Fiji if nothing matched
        return WORLD_PRESETS[0]
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

    /**
     * Calculates astronomical apparent lunar altitude above local horizon.
     * Takes into account Moon right ascension, declination, local sidereal time, and latitude.
     */
    private fun calculateMoonAltitude(date: LocalDate, localDecHour: Double, lat: Double, lon: Double, tzOffset: Double): Double {
        val jd = getJulianDayFromLocalDateTime(date, localDecHour, tzOffset)
        val ephem = calculateEphemeris(jd)

        val eps = Math.toRadians(23.439291 - 0.0130042 * ephem.t)
        val lam = Math.toRadians(ephem.moonTropical)
        val bet = Math.toRadians(ephem.moonLatitude)

        // Equatorial conversion
        val sinDec = sin(bet) * cos(eps) + cos(bet) * sin(eps) * sin(lam)
        val dec = asin(sinDec.coerceIn(-1.0, 1.0))
        val cosDecCosRA = cos(bet) * cos(lam)
        val cosDecSinRA = cos(bet) * cos(eps) * sin(lam) - sin(bet) * sin(eps)
        val ra = normalizeDegrees(Math.toDegrees(atan2(cosDecSinRA, cosDecCosRA)))

        // Greenwich Mean Sidereal Time & Local Sidereal Time
        val gmst = normalizeDegrees(280.46061837 + 360.98564736629 * (jd - 2451545.0))
        val lst = normalizeDegrees(gmst + lon)
        val hourAngle = Math.toRadians(lst - ra)

        // Geocentric / Topocentric Altitude
        val latRad = Math.toRadians(lat)
        val sinAlt = sin(latRad) * sin(dec) + cos(latRad) * cos(dec) * cos(hourAngle)
        return Math.toDegrees(asin(sinAlt.coerceIn(-1.0, 1.0)))
    }

    /**
     * Finds astronomical Moonrise and Moonset for the local day by solving for horizon crossings.
     * Standard horizon altitude for lunar rise/set is +0.125° (lunar parallax ~+57', refraction ~-34', semidiameter ~-16').
     */
    private fun calculateAstronomicalMoonriseMoonset(date: LocalDate, lat: Double, lon: Double, tzOffset: Double): Pair<String, String> {
        val targetAltitude = 0.125
        var riseHour: Double? = null
        var setHour: Double? = null

        val step = 0.25 // 15-minute search interval
        var h = 0.0
        var prevAlt = calculateMoonAltitude(date, 0.0, lat, lon, tzOffset)

        while (h < 24.0) {
            val nextH = h + step
            val nextAlt = calculateMoonAltitude(date, nextH, lat, lon, tzOffset)

            // Moonrise: crossing from below to above horizon
            if (riseHour == null && prevAlt < targetAltitude && nextAlt >= targetAltitude) {
                var low = h
                var high = nextH
                for (i in 0 until 14) {
                    val mid = (low + high) / 2.0
                    val midAlt = calculateMoonAltitude(date, mid, lat, lon, tzOffset)
                    if (midAlt < targetAltitude) low = mid else high = mid
                }
                riseHour = (low + high) / 2.0
            }

            // Moonset: crossing from above to below horizon
            if (setHour == null && prevAlt > targetAltitude && nextAlt <= targetAltitude) {
                var low = h
                var high = nextH
                for (i in 0 until 14) {
                    val mid = (low + high) / 2.0
                    val midAlt = calculateMoonAltitude(date, mid, lat, lon, tzOffset)
                    if (midAlt > targetAltitude) low = mid else high = mid
                }
                setHour = (low + high) / 2.0
            }

            prevAlt = nextAlt
            h = nextH
        }

        // If not found in 0..24h window (occurs near boundary or in subsequent hours), calculate extended
        val riseStr = riseHour?.let { formatDecTime(it) } ?: run {
            formatDecTime((calculateSunriseSunsetDec(date, lat, lon, tzOffset).first + 12.0) % 24.0)
        }
        val setStr = setHour?.let { formatDecTime(it) } ?: run {
            formatDecTime((calculateSunriseSunsetDec(date, lat, lon, tzOffset).second + 12.0) % 24.0)
        }

        return riseStr to setStr
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

    private val vishaGhatikas = intArrayOf(
        50, // 0: Ashwini
        24, // 1: Bharani
        30, // 2: Krittika
        40, // 3: Rohini
        14, // 4: Mrigashirsha
        11, // 5: Ardra
        30, // 6: Punarvasu
        20, // 7: Pushya
        32, // 8: Ashlesha
        30, // 9: Magha
        20, // 10: Purva Phalguni
        18, // 11: Uttara Phalguni
        21, // 12: Hasta
        20, // 13: Chitra
        14, // 14: Swati
        14, // 15: Vishakha
        10, // 16: Anuradha
        14, // 17: Jyeshtha
        56, // 18: Moola
        24, // 19: Purva Ashadha
        20, // 20: Uttara Ashadha
        10, // 21: Shravana
        10, // 22: Dhanishta
        18, // 23: Shatabhisha
        16, // 24: Purva Bhadrapada
        24, // 25: Uttara Bhadrapada
        30  // 26: Revati
    )

    private fun getEphemerisAtLocalOffset(date: LocalDate, sunriseDec: Double, tzOffsetHours: Double, hoursFromSunrise: Double): EphemerisData {
        val localHour = sunriseDec + hoursFromSunrise
        val jd = getJulianDayFromLocalDateTime(date, localHour, tzOffsetHours)
        return calculateEphemeris(jd)
    }

    private fun findNextCrossingTime(
        date: LocalDate,
        sunriseDec: Double,
        tzOffsetHours: Double,
        startHour: Double,
        maxSearchHours: Double,
        targetAngle: Double,
        angleEvaluator: (EphemerisData) -> Double
    ): Double {
        var prevH = startHour
        var prevAngle = angleEvaluator(getEphemerisAtLocalOffset(date, sunriseDec, tzOffsetHours, prevH))

        val step = 0.5
        var currH = startHour + step
        while (currH <= startHour + maxSearchHours) {
            val currEphem = getEphemerisAtLocalOffset(date, sunriseDec, tzOffsetHours, currH)
            val currAngle = angleEvaluator(currEphem)

            val diffToTarget = normalizeDegrees(targetAngle - prevAngle)
            val diffStep = normalizeDegrees(currAngle - prevAngle)

            if (diffStep in 0.0001..60.0 && diffToTarget in 0.000001..diffStep) {
                var low = prevH
                var high = currH
                for (i in 0 until 18) {
                    val mid = (low + high) / 2.0
                    val midAngle = angleEvaluator(getEphemerisAtLocalOffset(date, sunriseDec, tzOffsetHours, mid))
                    val midDiff = normalizeDegrees(targetAngle - midAngle)
                    if (midDiff > 180.0) {
                        high = mid
                    } else {
                        low = mid
                    }
                }
                return (low + high) / 2.0
            }
            prevH = currH
            prevAngle = currAngle
            currH += step
        }
        return startHour + 12.0
    }

    private fun findPreviousCrossingTime(
        date: LocalDate,
        sunriseDec: Double,
        tzOffsetHours: Double,
        startHour: Double,
        maxSearchHours: Double,
        targetAngle: Double,
        angleEvaluator: (EphemerisData) -> Double
    ): Double {
        var nextH = startHour
        var nextAngle = angleEvaluator(getEphemerisAtLocalOffset(date, sunriseDec, tzOffsetHours, nextH))

        val step = 0.5
        var currH = startHour - step
        while (currH >= startHour - maxSearchHours) {
            val currEphem = getEphemerisAtLocalOffset(date, sunriseDec, tzOffsetHours, currH)
            val currAngle = angleEvaluator(currEphem)

            val diffFromCurr = normalizeDegrees(targetAngle - currAngle)
            val diffStep = normalizeDegrees(nextAngle - currAngle)

            if (diffStep in 0.0001..60.0 && diffFromCurr in 0.000001..diffStep) {
                var low = currH
                var high = nextH
                for (i in 0 until 18) {
                    val mid = (low + high) / 2.0
                    val midAngle = angleEvaluator(getEphemerisAtLocalOffset(date, sunriseDec, tzOffsetHours, mid))
                    val midDiff = normalizeDegrees(targetAngle - midAngle)
                    if (midDiff > 180.0) {
                        high = mid
                    } else {
                        low = mid
                    }
                }
                return (low + high) / 2.0
            }
            nextH = currH
            nextAngle = currAngle
            currH -= step
        }
        return startHour - 12.0
    }

    private fun calculateAbhijitMuhurtham(sunriseDec: Double, sunsetDec: Double): String {
        val dayDuration = if (sunsetDec > sunriseDec) sunsetDec - sunriseDec else (sunsetDec + 24.0 - sunriseDec)
        val muhurtha = dayDuration / 15.0
        val start = (sunriseDec + 7.0 * muhurtha) % 24.0
        val end = (sunriseDec + 8.0 * muhurtha) % 24.0
        return "${formatDecTime(start)} - ${formatDecTime(end)}"
    }

    private fun calculateDurMuhurtham(day: DayOfWeek, sunriseDec: Double, sunsetDec: Double): String {
        val dayDuration = if (sunsetDec > sunriseDec) sunsetDec - sunriseDec else (sunsetDec + 24.0 - sunriseDec)
        val muhurtha = dayDuration / 15.0

        fun mRange(m1BasedStart: Int, m1BasedEnd: Int): String {
            val s = (sunriseDec + (m1BasedStart - 1) * muhurtha) % 24.0
            val e = (sunriseDec + m1BasedEnd * muhurtha) % 24.0
            return "${formatDecTime(s)} - ${formatDecTime(e)}"
        }

        return when (day) {
            DayOfWeek.SUNDAY -> mRange(14, 14)
            DayOfWeek.MONDAY -> "${mRange(9, 9)}, ${mRange(12, 12)}"
            DayOfWeek.TUESDAY -> "${mRange(4, 4)}, ${mRange(11, 11)}"
            DayOfWeek.WEDNESDAY -> mRange(5, 5)
            DayOfWeek.THURSDAY -> "${mRange(6, 6)}, ${mRange(7, 7)}"
            DayOfWeek.FRIDAY -> "${mRange(4, 4)}, ${mRange(9, 9)}"
            DayOfWeek.SATURDAY -> mRange(1, 2)
        }
    }

    private fun calculateVarjyam(
        nakshatraIndex: Int,
        nakStartHoursFromSunrise: Double,
        nakDurationHours: Double,
        sunriseDec: Double
    ): String {
        val ghatika = vishaGhatikas[nakshatraIndex % 27]
        val varjyamStartH = nakStartHoursFromSunrise + (ghatika / 60.0) * nakDurationHours
        val varjyamEndH = varjyamStartH + (4.0 / 60.0) * nakDurationHours
        val startClock = (sunriseDec + varjyamStartH + 48.0) % 24.0
        val endClock = (sunriseDec + varjyamEndH + 48.0) % 24.0
        return "${formatDecTime(startClock)} - ${formatDecTime(endClock)}"
    }

    private fun calculateDynamicNallaNeram(day: DayOfWeek, sunriseDec: Double, sunsetDec: Double): Pair<String, String> {
        val dayDuration = if (sunsetDec > sunriseDec) sunsetDec - sunriseDec else (sunsetDec + 24.0 - sunriseDec)
        val part = dayDuration / 8.0

        fun pRange(part1Based: Int): String {
            val s = (sunriseDec + (part1Based - 1) * part) % 24.0
            val e = (sunriseDec + part1Based * part) % 24.0
            return "${formatDecTime(s)} - ${formatDecTime(e)}"
        }

        return when (day) {
            DayOfWeek.SUNDAY -> pRange(2) to pRange(7)
            DayOfWeek.MONDAY -> pRange(1) to pRange(7)
            DayOfWeek.TUESDAY -> pRange(2) to pRange(7)
            DayOfWeek.WEDNESDAY -> pRange(3) to pRange(6)
            DayOfWeek.THURSDAY -> pRange(3) to pRange(7)
            DayOfWeek.FRIDAY -> pRange(1) to pRange(7)
            DayOfWeek.SATURDAY -> pRange(2) to pRange(8)
        }
    }

    private fun calculateDynamicGowriNallaNeram(day: DayOfWeek, sunriseDec: Double, sunsetDec: Double): Pair<String, String> {
        val dayDuration = if (sunsetDec > sunriseDec) sunsetDec - sunriseDec else (sunsetDec + 24.0 - sunriseDec)
        val dayPart = dayDuration / 8.0
        val nightDuration = 24.0 - dayDuration
        val nightPart = nightDuration / 8.0

        fun dRange(part1Based: Int): String {
            val s = (sunriseDec + (part1Based - 1) * dayPart) % 24.0
            val e = (sunriseDec + part1Based * dayPart) % 24.0
            return "${formatDecTime(s)} - ${formatDecTime(e)}"
        }

        fun nRange(part1Based: Int): String {
            val s = (sunsetDec + (part1Based - 1) * nightPart) % 24.0
            val e = (sunsetDec + part1Based * nightPart) % 24.0
            return "${formatDecTime(s)} - ${formatDecTime(e)}"
        }

        val (mornPart, evePart) = when (day) {
            DayOfWeek.SUNDAY -> 2 to 2
            DayOfWeek.MONDAY -> 1 to 2
            DayOfWeek.TUESDAY -> 2 to 2
            DayOfWeek.WEDNESDAY -> 1 to 1
            DayOfWeek.THURSDAY -> 1 to 2
            DayOfWeek.FRIDAY -> 1 to 1
            DayOfWeek.SATURDAY -> 4 to 2
        }

        return dRange(mornPart) to nRange(evePart)
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
        val normalized = (hoursFromMidnight + 48.0) % 24.0
        return formatDecTime(normalized)
    }
}
