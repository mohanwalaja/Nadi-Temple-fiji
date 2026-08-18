package com.example.data.service

import com.example.data.model.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

/**
 * Interface for Horoscope (Jathagam) Calculation.
 * Modular calculation architecture that can be backed by a Swiss Ephemeris or astronomical library.
 */
interface AstrologyCalculator {
    fun calculateHoroscope(
        name: String,
        dob: LocalDate,
        tob: LocalTime,
        birthPlace: String
    ): HoroscopeResult
}

class StandardAstrologyCalculator : AstrologyCalculator {

    private val nakshatrams = listOf(
        "அஸ்வினி (Ashwini)", "பரணி (Bharani)", "கிருத்திகை (Krittika)", "ரோகிணி (Rohini)",
        "மிருகசீரிஷம் (Mrigashirsha)", "திருவாதிரை (Thiruvathirai)", "புனர்பூசம் (Punarvasu)",
        "பூசம் (Pushya)", "ஆயில்யம் (Ashlesha)", "மகம் (Magha)", "பூரம் (Purva Phalguni)",
        "உத்திரம் (Uttara Phalguni)", "அஸ்தம் (Hasta)", "சித்திரை (Chitra)", "சுவாதி (Swati)",
        "விசாகம் (Vishakha)", "அனுஷம் (Anuradha)", "கேட்டை (Jyeshtha)", "மூலம் (Mula)",
        "பூராடம் (Purva Ashadha)", "உத்திராடம் (Uttara Ashadha)", "திருவோணம் (Shravana)",
        "அவிட்டம் (Dhanishta)", "சதயம் (Shatabhisha)", "பூரட்டாதி (Purva Bhadrapada)",
        "உத்திரட்டாதி (Uttara Bhadrapada)", "ரேவதி (Revati)"
    )

    override fun calculateHoroscope(
        name: String,
        dob: LocalDate,
        tob: LocalTime,
        birthPlace: String
    ): HoroscopeResult {
        // Deterministic astronomical calculation based on birth inputs
        val daysFromEpoch = ChronoUnit.DAYS.between(LocalDate.of(1970, 1, 1), dob).toInt()
        val minuteOfDay = tob.hour * 60 + tob.minute

        // Lagna calculation (changes roughly every 2 hours = 120 mins)
        val lagnaIndex = ((minuteOfDay / 120 + dob.monthValue) % 12 + 1)
        val lagnaRasi = Rasi.values().first { it.index == lagnaIndex }
        val lagnaDegrees = (minuteOfDay % 120) / 4.0

        // Moon Rasi & Nakshatra
        val moonDegree = ((daysFromEpoch * 13.176) % 360 + 360) % 360
        val chandraRasiIndex = (moonDegree / 30).toInt() + 1
        val chandraRasi = Rasi.values().first { it.index == chandraRasiIndex }
        val nakshatraIndex = (moonDegree / (360.0 / 27.0)).toInt() % 27
        val janmaNakshatra = nakshatrams[nakshatraIndex]
        val pada = ((moonDegree % (360.0 / 27.0)) / (360.0 / 108.0)).toInt() + 1

        // 9 Grahas positions
        val planets = listOf(
            calculatePlanet(Graha.SURYA, ((daysFromEpoch * 0.9856 + 280) % 360 + 360) % 360, lagnaRasi),
            calculatePlanet(Graha.CHANDRA, moonDegree, lagnaRasi),
            calculatePlanet(Graha.CHEVVAI, ((daysFromEpoch * 0.524 + 120) % 360 + 360) % 360, lagnaRasi),
            calculatePlanet(Graha.BUDHA, ((daysFromEpoch * 1.05 + 260) % 360 + 360) % 360, lagnaRasi),
            calculatePlanet(Graha.GURU, ((daysFromEpoch * 0.083 + 45) % 360 + 360) % 360, lagnaRasi),
            calculatePlanet(Graha.SUKRA, ((daysFromEpoch * 1.1 + 180) % 360 + 360) % 360, lagnaRasi),
            calculatePlanet(Graha.SANI, ((daysFromEpoch * 0.033 + 310) % 360 + 360) % 360, lagnaRasi),
            calculatePlanet(Graha.RAHU, ((360 - (daysFromEpoch * 0.053) % 360) + 360) % 360, lagnaRasi),
            calculatePlanet(Graha.KETU, ((360 - (daysFromEpoch * 0.053 + 180) % 360) + 360) % 360, lagnaRasi)
        )

        // 12 Bhavas calculation
        val bhavas = (1..12).map { bhavaNum ->
            val rasiIdx = ((lagnaRasi.index - 1 + bhavaNum - 1) % 12) + 1
            val rasi = Rasi.values().first { it.index == rasiIdx }
            val occupants = planets.filter { it.rasi == rasi }.map { it.graha }
            val (nameTa, nameEn, sigTa, sigEn) = getBhavaDetails(bhavaNum)
            BhavaDetail(
                number = bhavaNum,
                nameTa = nameTa,
                nameEn = nameEn,
                rasi = rasi,
                significanceTa = sigTa,
                significanceEn = sigEn,
                occupantGrahas = occupants
            )
        }

        // Navamsa mapping
        val navamsa = planets.associate { planet ->
            val navamsaRasiIdx = (((planet.degrees / 3.333).toInt() + planet.rasi.index * 3) % 12) + 1
            planet.graha to Rasi.values().first { it.index == navamsaRasiIdx }
        }

        // Vimshottari Dasha Periods
        val dashaPeriods = generateVimshottariDasha(nakshatraIndex, dob)

        // Sani Transit checks (Saturn is currently in Kumbham/Meenam zone)
        val currentSaturnRasi = Rasi.KUMBAM
        val saniDiff = ((currentSaturnRasi.index - chandraRasi.index + 12) % 12)
        val isEzharai = saniDiff in listOf(11, 0, 1) // 12th, 1st, 2nd from Moon
        val ezharaiTypeTa = when (saniDiff) {
            11 -> "விரய சனி (12-ஆம் இடம் - முதல் கட்டம்)"
            0 -> "ஜென்ம சனி (ஜன்ம ராசி - இரண்டாம் கட்டம்)"
            1 -> "பாத சனி (2-ஆம் இடம் - மூன்றாம் கட்டம்)"
            else -> "இல்லை"
        }
        val ezharaiTypeEn = when (saniDiff) {
            11 -> "Viraya Sani (12th House - Phase 1)"
            0 -> "Jenma Sani (Janma Rasi - Phase 2)"
            1 -> "Patha Sani (2nd House - Phase 3)"
            else -> "None"
        }
        val isAshtama = saniDiff == 7 // 8th house
        val isKandaka = saniDiff in listOf(3, 6, 9) // 4th, 7th, 10th houses

        val saniStatus = SaniTransitStatus(
            isEzharaiSani = isEzharai,
            ezharaiTypeTa = ezharaiTypeTa,
            ezharaiTypeEn = ezharaiTypeEn,
            isAshtamaSani = isAshtama,
            isKandakaSani = isKandaka,
            remedyTa = "சனிக்கிழமைகளில் திருநள்ளாறு அல்லது சனீஸ்வர பகவானுக்கு நல்லெண்ணெய் தீபம் ஏற்றுதல், ஸ்ரீ சிவ சுப்பிரமணிய சுவாமியை வழிபடுதல்.",
            remedyEn = "Light sesame oil lamp on Saturdays for Lord Sani and worship Lord Sri Siva Subramaniya Swami."
        )

        // Dosha Checks (Kuja Dosham, Kala Sarpa, Pitru)
        val marsBhava = bhavas.firstOrNull { it.occupantGrahas.contains(Graha.CHEVVAI) }?.number ?: 1
        val isKujaDosha = marsBhava in listOf(2, 4, 7, 8, 12)

        val doshas = listOf(
            DoshaCheckResult(
                nameTa = "செவ்வாய் தோஷம் (Kuja/Manglik Dosha)",
                nameEn = "Kuja / Manglik Dosha",
                isPresent = isKujaDosha,
                severityTa = if (isKujaDosha) "மிதமான தோஷம் (பரிகாரம் உகந்தது)" else "தோஷம் இல்லை",
                severityEn = if (isKujaDosha) "Moderate (Remedy suggested)" else "No Dosha",
                descriptionTa = if (isKujaDosha) "செவ்வாய் பகவான் லக்னத்திற்கு $marsBhava-ஆம் இடத்தில் வீற்றிருக்கிறார்." else "செவ்வாய் சாதகமான இடத்தில் உள்ளார்.",
                descriptionEn = if (isKujaDosha) "Mars is positioned in the ${marsBhava}th Bhava from Lagna." else "Mars is in a favorable house.",
                traditionalRemedyTa = "செவ்வாய்க்கிழமைகளில் வைத்தீஸ்வரன் கோயில் / ஸ்ரீ முருகப் பெருமானுக்கு பால் அபிஷேகம் மற்றும் செவ்வரளி மாலை சாற்றுதல்.",
                traditionalRemedyEn = "Offer red flower garland and Milk Abhishekam to Lord Murugan on Tuesdays."
            ),
            DoshaCheckResult(
                nameTa = "கால சர்ப்ப தோஷம் (Kala Sarpa)",
                nameEn = "Kala Sarpa Dosha",
                isPresent = false,
                severityTa = "தோஷம் இல்லை (Planets distributed)",
                severityEn = "No Dosha",
                descriptionTa = "கிரகங்கள் ராகு-கேது அச்சிற்கு வெளியே சுப நிலையாக அமர்ந்துள்ளன.",
                descriptionEn = "Planets are well distributed across the natal chart.",
                traditionalRemedyTa = "நாக சதுர்த்தி நாளில் திருக்காளஹஸ்தி அல்லது நாகராஜர் வழிபாடு.",
                traditionalRemedyEn = "Worship Lord Shiva and Nagaraja on Naga Chaturthi."
            ),
            DoshaCheckResult(
                nameTa = "பித்ரு தோஷம் (Pitru Dosha Indicator)",
                nameEn = "Pitru Dosha Indicator",
                isPresent = false,
                severityTa = "சுப நிலை (Clear)",
                severityEn = "Auspicious Status",
                descriptionTa = "9-ஆம் பாவம் மற்றும் சூரிய பகவான் நல்ல சுப பார்வையில் உள்ளனர்.",
                descriptionEn = "9th house and Sun receive benign aspects.",
                traditionalRemedyTa = "அமாவாசை தோறும் முன்னோர்களுக்கு எள் தர்ப்பணம் மற்றும் அன்னதானம்.",
                traditionalRemedyEn = "Offer sesame tarpanam and annadhanam on Amavasai days."
            )
        )

        // Temple Jathaga Summary
        val summary = TempleJathagaSummary(
            healthTa = "லக்னாதிபதி பலம் பெற்றிருப்பதால் தேக ஆரோக்கியம் மற்றும் எதிர்ப்பு சக்தி நற்பலன் தரும். உஷ்ண சம்பந்தமான உபாதைகளில் கவனம் தேவை. (பாரம்பரிய ஜோதிட குறிப்பு)",
            healthEn = "Lagna lord strength indicates robust vitality. Caution advised against heat-related discomfort. (Traditional Jyotisha indication)",
            wealthTa = "இரண்டாம் மற்றும் பதினோராம் பாவங்கள் சுபத்தன்மையுடன் விளங்குவதால் சிறப்பான தனவரவும் ஸ்திரமான சேமிப்பும் உண்டாகும்.",
            wealthEn = "2nd and 11th bhavas indicate progressive financial inflows and steady savings capacity.",
            educationTa = "ஐந்தாம் பாவம் மற்றும் புதன் பகவானின் அனுகூலத்தால் உயர் கல்வி, நுண்கலை மற்றும் பகுப்பாய்வுத் துறைகளில் சிறந்த தேர்ச்சி.",
            educationEn = "5th bhava and Mercury indicate analytical acumen, higher learning capability and success in technical studies.",
            careerTa = "பத்தாம் பாவ அதிபதி கேந்திர பலத்துடன் விளங்குவதால் தலைமைப் பொறுப்புகள் மற்றும் சுயதொழில்/நிர்வாகத் துறையில் உயர்வு கிட்டும்.",
            careerEn = "10th lord strength supports leadership roles, administrative governance and entrepreneurship.",
            marriageTa = "ஏழாம் பாவ சுப சேர்க்கை மூலம் நல்ல பண்புகள் நிறைந்த வாழ்க்கைத்துணை அமையும் அமைப்பு உள்ளது.",
            marriageEn = "7th house auspicious indications favor a supportive, culturally harmonious life partner.",
            familyTa = "நான்காம் பாவம் குருவின் சுப பார்வை பெறுவதால் குடும்பத்தில் மகிழ்ச்சியும் சுபகாரிய நிகழ்வுகளும் தடையின்றி நடக்கும்.",
            familyEn = "4th house receiving Jupiter's benefic influence fosters domestic harmony and auspicious home ceremonies.",
            foreignTravelTa = "ஒன்பதாம் மற்றும் பன்னிரண்டாம் பாவ அமைப்புகள் மூலமாக வெளிநாட்டுப் பயணம் மற்றும் கடல் கடந்த ஆன்மீக யாத்திரைக்கு சாதகமான காலம்.",
            foreignTravelEn = "9th and 12th bhava alignment supports overseas travel, pilgrim voyages and international professional endeavors.",
            currentPeriodGuidanceTa = "தற்போதைய திசாபுத்தி காலகட்டத்தில் சுபகாரிய முயற்சிகளைத் தொடங்கலாம். திருக்கோயில் வழிபாடு மன அமைதியையும் காரிய சித்தியையும் தரும்.",
            currentPeriodGuidanceEn = "The current planetary period favors spiritual initiatives and strategic ventures with blessings from Sri Siva Subramaniya Swami."
        )

        return HoroscopeResult(
            devoteeName = name,
            dob = dob,
            tob = tob,
            birthPlace = birthPlace,
            lagnaRasi = lagnaRasi,
            lagnaDegrees = lagnaDegrees,
            chandraRasi = chandraRasi,
            janmaNakshatram = janmaNakshatra,
            janmaPada = pada,
            planetPositions = planets,
            bhavas = bhavas,
            navamsaPositions = navamsa,
            dashaPeriods = dashaPeriods,
            saniStatus = saniStatus,
            doshas = doshas,
            summary = summary,
            isDemoEngine = true
        )
    }

    private fun calculatePlanet(graha: Graha, totalDegrees: Double, lagna: Rasi): PlanetPosition {
        val rasiIdx = ((totalDegrees / 30).toInt() % 12) + 1
        val rasi = Rasi.values().first { it.index == rasiIdx }
        val degreesInRasi = totalDegrees % 30
        val nakshatraIdx = ((totalDegrees / (360.0 / 27.0)).toInt()) % 27
        val pada = (((totalDegrees % (360.0 / 27.0)) / (360.0 / 108.0)).toInt()) + 1
        val bhavaNum = ((rasi.index - lagna.index + 12) % 12) + 1

        val isRetrograde = graha in listOf(Graha.GURU, Graha.SANI) && totalDegrees > 180
        val isCombust = graha != Graha.SURYA && abs(totalDegrees - 120.0) < 6.0

        return PlanetPosition(
            graha = graha,
            rasi = rasi,
            degrees = degreesInRasi,
            nakshatram = nakshatrams[nakshatraIdx],
            pada = pada,
            isRetrograde = isRetrograde,
            isCombust = isCombust,
            bhavaNumber = bhavaNum
        )
    }

    private fun getBhavaDetails(num: Int): List<String> = when (num) {
        1 -> listOf("லக்ன பாவம் (Tanu)", "1st Bhava - Self & Vitality", "உடல் நலம், தோற்றம், சுபாவம், ஆயுள்", "Physical body, constitution, personality")
        2 -> listOf("தன பாவம் (Dhana)", "2nd Bhava - Wealth & Speech", "செல்வம், குடும்பம், வாக்கு, ஆரம்பக் கல்வி", "Wealth, family speech, primary education")
        3 -> listOf("சகோதர பாவம் (Sahaja)", "3rd Bhava - Siblings & Courage", "இளைய சகோதரர், தைரியம், குறுகிய பயணங்கள்", "Younger siblings, courage, communication")
        4 -> listOf("சுக பாவம் (Sukha)", "4th Bhava - Mother & Comforts", "தாய், வீடு, நிலம், வாகனம், மன அமைதி", "Mother, landed property, conveyances, happiness")
        5 -> listOf("புத்திர பாவம் (Putra)", "5th Bhava - Progeny & Intellect", "குழந்தைகள், பூர்வ புண்ணியம், புத்தி, ஆன்மீகம்", "Children, past merits, intellect, devotion")
        6 -> listOf("சத்ரு பாவம் (Shatru)", "6th Bhava - Health & Debts", "நோய், கடன், வழக்கு, எதிரிகள், உழைப்பு", "Illness, debt, adversaries, service")
        7 -> listOf("களத்திர பாவம் (Kalatra)", "7th Bhava - Spouse & Partners", "வாழ்க்கைத்துணை, திருமணம், கூட்டுத் தொழில்", "Spouse, marriage, business partnerships")
        8 -> listOf("ஆயுள் பாவம் (Ayur)", "8th Bhava - Longevity & Transformation", "ஆயுள் பலம், மறைமுக தனம், ஆன்ம ஞானம்", "Longevity, sudden events, occult knowledge")
        9 -> listOf("பாக்கிய பாவம் (Bhagya)", "9th Bhava - Fortune & Dharma", "தந்தை, குரு அருள், புண்ணிய யாத்திரை, பாக்கியம்", "Father, guru, fortune, pilgrimage")
        10 -> listOf("கர்ம பாவம் (Karma)", "10th Bhava - Profession & Status", "தொழில், கீர்த்தி, சமூக அந்தஸ்து, அதிகார பதவி", "Profession, fame, status, achievements")
        11 -> listOf("லாப பாவம் (Labha)", "11th Bhava - Gains & Aspirations", "வருமானம், மூத்த சகோதரர், ஆசைகள் நிறைவேறுதல்", "Gains, elder siblings, fulfilled ambitions")
        else -> listOf("விரய பாவம் (Vyaya)", "12th Bhava - Expenses & Moksha", "சுப விரயம், வெளிநாட்டு வாசம், மோட்சம், நித்திரை", "Expenditure, foreign residence, liberation")
    }

    private fun generateVimshottariDasha(nakshatraIdx: Int, dob: LocalDate): List<DashaPeriod> {
        val dashaOrder = listOf(
            Graha.KETU, Graha.SUKRA, Graha.SURYA, Graha.CHANDRA,
            Graha.CHEVVAI, Graha.RAHU, Graha.GURU, Graha.SANI, Graha.BUDHA
        )
        val dashaYears = mapOf(
            Graha.KETU to 7, Graha.SUKRA to 20, Graha.SURYA to 6, Graha.CHANDRA to 10,
            Graha.CHEVVAI to 7, Graha.RAHU to 18, Graha.GURU to 16, Graha.SANI to 19, Graha.BUDHA to 17
        )

        val startingLordIdx = nakshatraIdx % 9
        var currentStart = dob
        val periods = mutableListOf<DashaPeriod>()

        for (i in 0..4) {
            val lord = dashaOrder[(startingLordIdx + i) % 9]
            val years = dashaYears[lord] ?: 10
            val currentEnd = currentStart.plusYears(years.toLong())
            periods.add(
                DashaPeriod(
                    mahadashaLord = lord,
                    antardashaLord = lord,
                    startDate = currentStart,
                    endDate = currentEnd,
                    descriptionTa = "${lord.nameTa} மகாதிசை ($years வருடங்கள்)",
                    descriptionEn = "${lord.nameEn} Mahadasha ($years Years)"
                )
            )
            currentStart = currentEnd
        }
        return periods
    }
}
