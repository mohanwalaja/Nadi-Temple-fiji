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
        "பிரதமை (Prathama / प्रतिपदा)", "துவிதியை (Dvitiya / द्वितीया)", "திருதியை (Tritiya / तृतीया)",
        "சதுர்த்தி (Chaturthi / चतुर्थी)", "பஞ்சமி (Panchami / पंचमी)", "சஷ்டி (Shashti / षष्ठी)",
        "சப்தமி (Saptami / सप्तमी)", "அஷ்டமி (Ashtami / अष्टमी)", "நவமி (Navami / नवमी)",
        "தசமி (Dashami / दशमी)", "ஏகாதசி (Ekadashi / एकादशी)", "துவாதசி (Dvadashi / द्वादशी)",
        "திரயோதசி (Trayodashi / त्रयोदशी)", "சதுர்த்தசி (Chaturdashi / चतुर्दशी)", "பௌர்ணமி (Pournami / पूर्णिमा)",
        "பிரதமை (Prathama / प्रतिपदा)", "துவிதியை (Dvitiya / द्वितीया)", "திருதியை (Tritiya / तृतीया)",
        "சதுர்த்தி (Chaturthi / चतुर्थी)", "பஞ்சமி (Panchami / पंचमी)", "சஷ்டி (Shashti / षष्ठी)",
        "சப்தமி (Saptami / सप्तमी)", "அஷ்டமி (Ashtami / अष्टमी)", "நவமி (Navami / नवमी)",
        "தசமி (Dashami / दशमी)", "ஏகாதசி (Ekadashi / एकादशी)", "துவாதசி (Dvadashi / द्वादशी)",
        "திரயோதசி (Trayodashi / त्रयोदशी)", "சதுர்த்தசி (Chaturdashi / चतुर्दशी)", "அமாவாசை (Amavasya / अमावस्या)"
    )

    // 27 Drik Nakshatras
    private val nakshatramsTa = listOf(
        "அஸ்வினி (Ashwini / अश्विनी)", "பரணி (Bharani / भरणी)", "கார்த்திகை (Krittika / कृत्तिका)",
        "ரோகிணி (Rohini / रोहिणी)", "மிருகசீரிஷம் (Mrigashirsha / मृगशिरा)", "திருவாதிரை (Ardra / आर्द्रा)",
        "புனர்பூசம் (Punarvasu / पुनर्वसु)", "பூசம் (Pushya / पुष्य)", "ஆயில்யம் (Ashlesha / अश्लेषा)",
        "மகம் (Magha / मघा)", "பூரம் (Purva Phalguni / पूर्वाफाल्गुनी)", "உத்திரம் (Uttara Phalguni / उत्तराफाल्गुनी)",
        "அஸ்தம் (Hasta / हस्त)", "சித்திரை (Chitra / चित्रा)", "சுவாதி (Swati / स्वाति)",
        "விசாகம் (Vishakha / विशाखा)", "அனுஷம் (Anuradha / अनुराधा)", "கேட்டை (Jyeshtha / ज्येष्ठा)",
        "மூலம் (Moola / मूल)", "பூராடம் (Purvashada / पूर्वाषाढ़ा)", "உத்திராடம் (Uttarashada / उत्तराषाढ़ा)",
        "திருவோணம் (Shravana / श्रवण)", "அவிட்டம் (Dhanishta / धनिष्ठा)", "சதயம் (Shatabhisha / शतभिषा)",
        "பூரட்டாதி (Purva Bhadrapada / पूर्वाभाद्रपद)", "உத்திரட்டாதி (Uttara Bhadrapada / उत्तराभाद्रपद)", "ரேவதி (Revati / रेवती)"
    )

    // 27 Nithya Yogas
    private val nithyaYogamsTa = listOf(
        "விஷ்கம்பம் (Vishkambha / विष्कुम्भ)", "ப்ரீதி (Priti / प्रीति)", "ஆயுஷ்மான் (Ayushman / आयुष्मान्)",
        "சௌபாக்யம் (Saubhagya / सौभाग्य)", "சோபனம் (Shobhana / शोभन)", "அதிகண்டம் (Atiganda / अतिगण्ड)",
        "சுகர்மம் (Sukarma / सुकर्मा)", "திருதி (Dhriti / धृति)", "சூலம் (Shoola / शूल)",
        "கண்டம் (Ganda / गण्ड)", "விருத்தி (Vriddhi / वृद्धि)", "துருவம் (Dhruva / ध्रुव)",
        "வியாகாதம் (Vyaghata / व्याघात)", "ஹர்ஷணம் (Harshana / हर्षण)", "வஜ்ரம் (Vajra / वज्र)",
        "சித்தி (Siddhi / सिद्धि)", "வியதீபாதம் (Vyatipata / व्यतीपात)", "வாரியான் (Variyan / वरीयान्)",
        "பரிகம் (Parigha / परिघ)", "சிவம் (Shiva / शिव)", "சித்தம் (Siddha / सिद्ध)",
        "சாத்தியம் (Sadhya / साध्य)", "சுபம் (Shubha / शुभ)", "சுக்லம் (Shukla / शुक्ल)",
        "பிரம்மம் (Brahma / ब्रह्म)", "ஐந்திரம் (Indra / ऐन्द्र)", "வைதிருதி (Vaidhriti / वैधृति)"
    )

    // 7 Chara Karanas
    private val charaKaranamsTa = listOf(
        "பவம் (Bava / बव)", "பாலவம் (Balava / बालव)", "கௌலவம் (Kaulava / कौलव)",
        "தைதுலம் (Taitila / तैतिल)", "கரஜை (Gara / गर)", "வனஜை (Vanija / वणिज)",
        "பத்திரை / விஷ்டி (Vishti / विष्टि)"
    )

    private val rasiNamesTa = listOf(
        "மேஷம் (Aries)", "ரிஷபம் (Taurus)", "மிதுனம் (Gemini)",
        "கடகம் (Cancer)", "சிம்மம் (Leo)", "கன்னி (Virgo)",
        "துலாம் (Libra)", "விருச்சிகம் (Scorpio)", "தனுசு (Sagittarius)",
        "மகரம் (Capricorn)", "கும்பம் (Aquarius)", "மீனம் (Pisces)"
    )

    data class LocationCoordinates(
        val lat: Double,
        val lon: Double,
        val timeZoneOffsetHours: Double,
        val nameTa: String
    )

    private val locationMap = mapOf(
        "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)" to LocationCoordinates(-17.80, 177.41, 12.0, "நாடி, பிஜி"),
        "சுவா, பிஜி (Suva, Fiji Islands)" to LocationCoordinates(-18.14, 178.44, 12.0, "சுவா, பிஜி"),
        "லவுடோகா, பிஜி (Lautoka, Fiji Islands)" to LocationCoordinates(-17.61, 177.45, 12.0, "லவுடோகா, பிஜி"),
        "லபாசா, பிஜி (Labasa, Fiji Islands)" to LocationCoordinates(-16.43, 179.37, 12.0, "லபாசா, பிஜி"),
        "சென்னை (Chennai, India)" to LocationCoordinates(13.08, 80.27, 5.5, "சென்னை"),
        "மதுரை (Madurai, India)" to LocationCoordinates(9.93, 78.12, 5.5, "மதுரை"),
        "யாழ்ப்பாணம் (Jaffna, Sri Lanka)" to LocationCoordinates(9.66, 80.01, 5.5, "யாழ்ப்பாணம்"),
        "கொழும்பு (Colombo, Sri Lanka)" to LocationCoordinates(6.93, 79.86, 5.5, "கொழும்பு"),
        "சிங்கப்பூர் (Singapore)" to LocationCoordinates(1.35, 103.82, 8.0, "சிங்கப்பூர்"),
        "கோலாலம்பூர் (Kuala Lumpur, Malaysia)" to LocationCoordinates(3.14, 101.69, 8.0, "கோலாலம்பூர்"),
        "சிட்னி (Sydney, Australia)" to LocationCoordinates(-33.87, 151.21, 10.0, "சிட்னி"),
        "ஆக்லாந்து (Auckland, New Zealand)" to LocationCoordinates(-36.85, 174.76, 12.0, "ஆக்லாந்து"),
        "லண்டன் (London, UK)" to LocationCoordinates(51.51, -0.13, 0.0, "லண்டன்"),
        "டொராண்டோ (Toronto, Canada)" to LocationCoordinates(43.65, -79.38, -5.0, "டொராண்டோ")
    )

    data class EphemerisData(
        val sunTropical: Double,
        val sunSidereal: Double,
        val moonTropical: Double,
        val moonSidereal: Double,
        val ayanamsha: Double
    )

    override fun calculatePanchangam(date: LocalDate, location: String): PanchangamDetail {
        val loc = locationMap[location] ?: LocationCoordinates(-17.80, 177.41, 12.0, "நாடி, பிஜி")

        // 1. Julian Day and Ephemeris Calculation at local noon
        val ephemeris = calculateEphemeris(date, 0.5 - (loc.timeZoneOffsetHours / 24.0))
        val sunSid = ephemeris.sunSidereal
        val moonSid = ephemeris.moonSidereal
        val sunTrop = ephemeris.sunTropical
        val moonTrop = ephemeris.moonTropical

        // 2. Solar Month & Tamil Date from Sidereal Sun
        val sunRasiIndex = (sunSid / 30.0).toInt() % 12
        val tamilMonth = TamilMonth.values()[sunRasiIndex]
        val tamilDate = (sunSid % 30.0).toInt() + 1
        val tamilYear = TamilSamvatsaraEngine.getSamvatsaraForDate(date)

        // 1. Samvatsara (Tamil Year) - Full display name like "பராபவ வருடம் (Parabhava Samvatsara)"
        val samvatsaraName = "${tamilYear.tamilName} வருடம் (${tamilYear.englishName} Samvatsara - ${tamilYear.number})"

        // 2. Ayanam: Uttarayanam (Makara/Thai to Mithuna/Aani), Dakshinayanam (Kataka/Aadi to Dhanus/Margazhi)
        val isUttarayanam = tamilMonth.index in listOf(10, 11, 12, 1, 2, 3)
        val ayanam = if (isUttarayanam) {
            "உத்தராயணம் (Uttarayanam / उत्तरायण)"
        } else {
            "தக்ஷிணாயணம் (Dakshinayanam / दक्षिणायन)"
        }

        // 3. Rithu (Vedic Season)
        val ritu = when (tamilMonth) {
            TamilMonth.CHITHIRAI, TamilMonth.VAIKASI -> "வசந்த ருது (Vasanta Ritu / वसन्त ऋतु)"
            TamilMonth.AANI, TamilMonth.AADI -> "கிரீஷ்ம ருது (Greeshma Ritu / ग्रीष्म ऋतु)"
            TamilMonth.AVANI, TamilMonth.PURATTASI -> "வர்ஷ ருது (Varsha Ritu / वर्षा ऋतु)"
            TamilMonth.AIPASI, TamilMonth.KARTHIGAI -> "சரத் ருது (Sharad Ritu / शरद् ऋतु)"
            TamilMonth.MARGHAZHI, TamilMonth.THAI -> "ஹேமந்த ருது (Hemanta Ritu / हेमन्त ऋतु)"
            TamilMonth.MASI, TamilMonth.PANGUNI -> "சிசிர ருது (Shishira Ritu / शिशिर ऋतु)"
        }

        // 4. Tamil Masam in Sanskrit (e.g. மேஷ மாதம் / Mesha Masa)
        val sanskritMonth = "${tamilMonth.sanskritMasa} (${tamilMonth.sanskritMasaEn} / ${tamilMonth.tamilName})"

        // 5. Paksham (Shukla / Krishna) & 6. Tithi
        val elongation = normalizeDegrees(moonTrop - sunTrop)
        val tithiIndex = (elongation / 12.0).toInt() % 30
        val tithiName = tithisTa[tithiIndex]
        val paksha = if (tithiIndex < 15) {
            "சுக்ல பக்ஷம் (Shukla Paksha / शुक्ल पक्ष - வளர்பிறை)"
        } else {
            "கிருஷ்ண பக்ஷம் (Krishna Paksha / कृष्ण पक्ष - தேய்பிறை)"
        }

        // Tithi End Time & Next Tithi calculation
        val remTithiDeg = 12.0 - (elongation % 12.0)
        val tithiHoursRemaining = (remTithiDeg / 12.19) * 24.0
        val tithiEndTime = formatLocalEndTime(6.0 + tithiHoursRemaining)
        val nextTithiIndex = (tithiIndex + 1) % 30
        val nextTithi = tithisTa[nextTithiIndex]

        // 7. Vasaram (Vedic Day of Week)
        val (vasaram, dayOfWeekTa) = when (date.dayOfWeek) {
            DayOfWeek.SUNDAY -> "பானு வாசரம் (Bhanu Vasaram / भानुवासरः)" to "ஞாயிற்றுக்கிழமை (Sunday / रविवार)"
            DayOfWeek.MONDAY -> "இந்து / சோம வாசரம் (Indu/Soma Vasaram / सोमवासरः)" to "திங்கட்கிழமை (Monday / सोमवार)"
            DayOfWeek.TUESDAY -> "பௌம / மங்கள வாசரம் (Bhauma/Mangala Vasaram / भौमवासरः)" to "செவ்வாய்க்கிழமை (Tuesday / मंगलवार)"
            DayOfWeek.WEDNESDAY -> "ஸௌம்ய / புத வாசரம் (Saumya/Budha Vasaram / सौम्यवासरः)" to "புதன்கிழமை (Wednesday / बुधवार)"
            DayOfWeek.THURSDAY -> "குரு / பிருஹஸ்பதி வாசரம் (Guru Vasaram / गुरुवासरः)" to "வியாழக்கிழமை (Thursday / गुरुवार)"
            DayOfWeek.FRIDAY -> "ப்ருகு / சுக்ர வாசரம் (Bhrigu/Shukra Vasaram / भृगुवासरः)" to "வெள்ளிக்கிழமை (Friday / शुक्रवार)"
            DayOfWeek.SATURDAY -> "ஸ்திர / சனி வாசரம் (Sthira/Shani Vasaram / स्थिरवासरः)" to "சனிக்கிழமை (Saturday / शनिवार)"
        }

        // 8. Nakshatram & Pada
        val nakshatraIndex = (moonSid / (360.0 / 27.0)).toInt() % 27
        val nakshatraName = nakshatramsTa[nakshatraIndex]
        val pada = (((moonSid % (360.0 / 27.0)) / (360.0 / 108.0)).toInt() % 4) + 1

        val remNakDeg = (360.0 / 27.0) - (moonSid % (360.0 / 27.0))
        val nakHoursRemaining = (remNakDeg / 13.176) * 24.0
        val nakshatraEndTime = formatLocalEndTime(6.0 + nakHoursRemaining)
        val nextNakshatraIndex = (nakshatraIndex + 1) % 27
        val nextNakshatra = nakshatramsTa[nextNakshatraIndex]

        // 9. Nithya Yogam & Dina Yogam
        val yogaDeg = normalizeDegrees(sunSid + moonSid)
        val nithyaYogaIndex = (yogaDeg / (360.0 / 27.0)).toInt() % 27
        val nithyaYogaName = nithyaYogamsTa[nithyaYogaIndex]
        val remYogaDeg = (360.0 / 27.0) - (yogaDeg % (360.0 / 27.0))
        val yogaHoursRemaining = (remYogaDeg / 14.16) * 24.0
        val yogaEndTime = formatLocalEndTime(6.0 + yogaHoursRemaining)
        val dinaYoga = calculateDinaYoga(date.dayOfWeek, nakshatraIndex)

        // 10. Karanam
        val halfTithiIndex = (elongation / 6.0).toInt() % 60
        val karanamName = when (halfTithiIndex) {
            0 -> "கிம்ஸ்துக்னம் (Kimstughna / किंस्तुघ्न)"
            in 1..56 -> charaKaranamsTa[(halfTithiIndex - 1) % 7]
            57 -> "சகுனி (Shakuni / शकुनि)"
            58 -> "சதுஷ்பாதம் (Chatushpada / चतुष्पद)"
            else -> "நாகவம் (Naga / नाग)"
        }
        val remKarDeg = 6.0 - (elongation % 6.0)
        val karHoursRemaining = (remKarDeg / 12.19) * 24.0
        val karanamEndTime = formatLocalEndTime(6.0 + karHoursRemaining)

        val nextHalfTithiIndex = (halfTithiIndex + 1) % 60
        val nextKaranam = when (nextHalfTithiIndex) {
            0 -> "கிம்ஸ்துக்னம் (Kimstughna / किंस्तुघ्न)"
            in 1..56 -> charaKaranamsTa[(nextHalfTithiIndex - 1) % 7]
            57 -> "சகுனி (Shakuni / शकुनि)"
            58 -> "சதுஷ்பாதம் (Chatushpada / चतुष्पद)"
            else -> "நாகவம் (Naga / नाग)"
        }

        // Rasis & Chandrashtamam
        val moonRasiIndex = (moonSid / 30.0).toInt() % 12
        val chandraRasi = rasiNamesTa[moonRasiIndex]
        val suryaRasi = rasiNamesTa[sunRasiIndex]

        val chandrashtamaRasiIdx = (moonRasiIndex - 7 + 12) % 12
        val chandrashtamam = "${rasiNamesTa[chandrashtamaRasiIdx]} ராசி அன்பர்களுக்கு இன்றைய நாள் சந்திராஷ்டமம்."

        // Sun & Moon Times
        val (sunriseLocal, sunsetLocal) = calculateSunriseSunset(date, loc.lat, loc.lon, loc.timeZoneOffsetHours)
        val (moonriseLocal, moonsetLocal) = calculateMoonriseMoonset(date, loc.lat, loc.lon, loc.timeZoneOffsetHours, elongation)

        // Inauspicious Times
        val (rahu, yama, kuli) = calculateDynamicInauspiciousTimes(date.dayOfWeek, sunriseLocal, sunsetLocal)

        // Nalla Neram
        val (nallaMorn, nallaEve) = getNallaNeram(date.dayOfWeek)
        val (gowriMorn, gowriEve) = getGowriNallaNeram(date.dayOfWeek)

        // Muhurtham & Disha Soola
        val abhijitMuhurtham = if (date.dayOfWeek != DayOfWeek.WEDNESDAY) "11:52 AM - 12:44 PM" else "பிற்பகல் 12:00 - 12:45 (புதன் பரிகாரம்)"
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

        val (dishaSoola, soolaPariharam) = when (date.dayOfWeek) {
            DayOfWeek.SUNDAY -> "மேற்கு (West)" to "வெல்லம் (Jaggery)"
            DayOfWeek.MONDAY -> "கிழக்கு (East)" to "தயிர் (Curd)"
            DayOfWeek.TUESDAY -> "வடக்கு (North)" to "பால் (Milk)"
            DayOfWeek.WEDNESDAY -> "வடக்கு (North)" to "பால் (Milk)"
            DayOfWeek.THURSDAY -> "தெற்கு (South)" to "தைலம் / நெய் (Oil/Ghee)"
            DayOfWeek.FRIDAY -> "மேற்கு (West)" to "வெல்லம் (Jaggery)"
            DayOfWeek.SATURDAY -> "கிழக்கு (East)" to "தயிர் (Curd)"
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
            ayanam = ayanam,
            ritu = ritu,
            sanskritMonth = sanskritMonth,
            paksha = paksha,
            tithi = tithiName,
            tithiEndTime = tithiEndTime,
            nextTithi = nextTithi,
            vasaram = vasaram,
            dayOfWeek = dayOfWeekTa,
            nakshatram = nakshatraName,
            nakshatramEndTime = nakshatraEndTime,
            pada = pada,
            nextNakshatram = nextNakshatra,
            yogam = nithyaYogaName,
            yogamEndTime = yogaEndTime,
            dinaYogam = dinaYoga,
            karanam = karanamName,
            karanamEndTime = karanamEndTime,
            nextKaranam = nextKaranam,
            sunrise = "$sunriseLocal (${loc.nameTa})",
            sunset = "$sunsetLocal (${loc.nameTa})",
            moonrise = moonriseLocal,
            moonset = moonsetLocal,
            chandraRasi = chandraRasi,
            suryaRasi = suryaRasi,
            chandrashtamam = chandrashtamam,
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
            dishaSoola = dishaSoola,
            soolaPariharam = soolaPariharam,
            specialObservances = observances,
            isDemoData = false
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

    private fun calculateSunriseSunset(date: LocalDate, lat: Double, lon: Double, tzOffset: Double): Pair<String, String> {
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

        return formatDecTime(sunriseLocalDec) to formatDecTime(sunsetLocalDec)
    }

    private fun calculateMoonriseMoonset(date: LocalDate, lat: Double, lon: Double, tzOffset: Double, elongation: Double): Pair<String, String> {
        val moonLagHours = (elongation / 360.0) * 24.0
        val moonriseDec = (6.0 + moonLagHours) % 24.0
        val moonsetDec = (18.0 + moonLagHours) % 24.0
        return formatDecTime(moonriseDec) to formatDecTime(moonsetDec)
    }

    private fun calculateDynamicInauspiciousTimes(day: DayOfWeek, sunriseStr: String, sunsetStr: String): Triple<String, String, String> {
        return when (day) {
            DayOfWeek.SUNDAY -> Triple("04:30 PM - 06:00 PM", "12:00 PM - 01:30 PM", "03:00 PM - 04:30 PM")
            DayOfWeek.MONDAY -> Triple("07:30 AM - 09:00 AM", "10:30 AM - 12:00 PM", "01:30 PM - 03:00 PM")
            DayOfWeek.TUESDAY -> Triple("03:00 PM - 04:30 PM", "09:00 AM - 10:30 AM", "12:00 PM - 01:30 PM")
            DayOfWeek.WEDNESDAY -> Triple("12:00 PM - 01:30 PM", "07:30 AM - 09:00 AM", "10:30 AM - 12:00 PM")
            DayOfWeek.THURSDAY -> Triple("01:30 PM - 03:00 PM", "06:00 AM - 07:30 AM", "09:00 AM - 10:30 AM")
            DayOfWeek.FRIDAY -> Triple("10:30 AM - 12:00 PM", "03:00 PM - 04:30 PM", "07:30 AM - 09:00 AM")
            DayOfWeek.SATURDAY -> Triple("09:00 AM - 10:30 AM", "01:30 PM - 03:00 PM", "06:00 AM - 07:30 AM")
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
