package com.example.data.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Nature/Quality of a Hora time block in Vedic Astrology
 */
enum class HoraNature(
    val labelTa: String,
    val labelEn: String,
    val labelHi: String,
    val isAuspicious: Boolean
) {
    AUSPICIOUS("சுப ஹோரை (நன்று)", "Auspicious (Benefic)", "शुभ होरा (अति उत्तम)", true),
    MODERATE("மத்தியம ஹோரை (சராசரி)", "Moderate / Neutral", "मध्यम होरा (सामान्य)", true),
    INAUSPICIOUS("அசுப ஹோரை (தவிர்க்கவும்)", "Inauspicious (Malefic)", "अशुभ होरा (त्याज्य)", false);

    fun getLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> labelTa
        AppLanguage.HINDI -> labelHi
        AppLanguage.ENGLISH -> labelEn
    }
}

/**
 * 7 Planetary Rulers of Vedic Horas in Chaldean Order:
 * Sun -> Venus -> Mercury -> Moon -> Saturn -> Jupiter -> Mars -> (Sun)
 */
enum class HoraRuler(
    val planetNameTa: String,
    val planetNameEn: String,
    val planetNameHi: String,
    val symbolEmoji: String,
    val nature: HoraNature,
    val deityTa: String,
    val deityEn: String,
    val deityHi: String,
    val shortSummaryTa: String,
    val shortSummaryEn: String,
    val shortSummaryHi: String,
    val favorableActivitiesTa: List<String>,
    val favorableActivitiesEn: List<String>,
    val favorableActivitiesHi: List<String>,
    val unfavorableActivitiesTa: List<String>,
    val unfavorableActivitiesEn: List<String>,
    val unfavorableActivitiesHi: List<String>
) {
    SUN(
        planetNameTa = "சூரியன் (Surya)",
        planetNameEn = "Sun (Surya)",
        planetNameHi = "सूर्य (Surya)",
        symbolEmoji = "☀️",
        nature = HoraNature.MODERATE,
        deityTa = "சிவபெருமான் / சூரிய நாராயணர்",
        deityEn = "Lord Shiva / Surya Narayana",
        deityHi = "भगवान शिव / सूर्य देव",
        shortSummaryTa = "அரசு பணிகள், உயர் அதிகாரிகளை சந்திக்க, மருத்துவ சிகிச்சை, யோகா & ஹோமம் செய்ய நன்று.",
        shortSummaryEn = "Favorable for government matters, leadership, medical treatments, meeting authorities, and spiritual rituals.",
        shortSummaryHi = "सरकारी कार्य, उच्चाधिकारियों से भेंट, चिकित्सा, स्वास्थ्य लाभ एवं आध्यात्मिक अनुष्ठान हेतु उत्तम।",
        favorableActivitiesTa = listOf("அரசு காரியங்கள்", "உயர் அதிகாரிகள் சந்திப்பு", "மருத்துவ சிகிச்சை", "ஹோமம், யாகம்", "பொறுப்புகள் ஏற்றல்"),
        favorableActivitiesEn = listOf("Government matters", "Meeting superiors/officials", "Medical treatments", "Fire rituals & Puja", "Assuming authority/leadership"),
        favorableActivitiesHi = listOf("शासकीय कार्य", "उच्चाधिकारियों से भेंट", "औषधि सेवन एवं चिकित्सा", "यज्ञ एवं अनुष्ठान", "नेतृत्व भार ग्रहण"),
        unfavorableActivitiesTa = listOf("சுப சமாதானப் பேச்சுக்கள்", "புதிய ஆடை ஆபரணம் வாங்குதல்", "நீண்ட தூர பிரயாணம்"),
        unfavorableActivitiesEn = listOf("Peace treaties/negotiations", "Purchasing clothes/luxuries", "Long distance leisure travel"),
        unfavorableActivitiesHi = listOf("विवाह वार्ता", "वस्त्राभूषण क्रय", "मनोरंजन यात्रा")
    ),

    VENUS(
        planetNameTa = "சுக்கிரன் (Shukra)",
        planetNameEn = "Venus (Shukra)",
        planetNameHi = "शुक्र (Shukra)",
        symbolEmoji = "💖",
        nature = HoraNature.AUSPICIOUS,
        deityTa = "ஸ்ரீ மகாலட்சுமி தாயார்",
        deityEn = "Goddess Mahalakshmi",
        deityHi = "माता महालक्ष्मी",
        shortSummaryTa = "ஆபரணம், வண்டி வாகனம் வாங்க, கலைகள், திருமணம், நிதி முதலீடு செய்ய மிக உன்னதமான சுப ஹோரை.",
        shortSummaryEn = "Highly auspicious for buying jewelry, vehicles, clothes, luxury items, marriage talks, arts, and investments.",
        shortSummaryHi = "आभूषण, वाहन, नवीन वस्त्र, कला, संगीत, विवाह वार्ता एवं आर्थिक निवेश हेतु सर्वोत्तम शुभ होरा।",
        favorableActivitiesTa = listOf("நகை, ஆடை, வாகனம் வாங்குதல்", "திருமணப் பேச்சுவார்த்தை", "கலை, இசை கற்றல்", "வியாபாரம் தொடங்குதல்", "சொத்து வாங்குதல்"),
        favorableActivitiesEn = listOf("Purchasing gold, ornaments & vehicles", "Marriage proposals & auspicious talks", "Arts, music & aesthetics", "New business ventures", "Real estate registration"),
        favorableActivitiesHi = listOf("स्वर्ण व वाहन क्रय", "विवाह संबंधित शुभ कार्य", "कला, संगीत एवं सौंदर्य प्रसाधन", "नया व्यवसाय आरंभ", "वित्तीय निवेश"),
        unfavorableActivitiesTa = listOf("சண்டை, வழக்கு", "கடன் கொடுத்தல்", "கடுமையான வாக்குவாதம்"),
        unfavorableActivitiesEn = listOf("Litigation & disputes", "Lending money without guarantee", "Hostile confrontations"),
        unfavorableActivitiesHi = listOf("शत्रुता व विवाद", "ऋण देना", "कठोर वाद-विवाद")
    ),

    MERCURY(
        planetNameTa = "புதன் (Budha)",
        planetNameEn = "Mercury (Budha)",
        planetNameHi = "बुध (Budha)",
        symbolEmoji = "💚",
        nature = HoraNature.AUSPICIOUS,
        deityTa = "ஸ்ரீ மகாவிஷ்ணு / பெருமாள்",
        deityEn = "Lord Maha Vishnu",
        deityHi = "भगवान श्री हरि विष्णु",
        shortSummaryTa = "கல்வி ஆரம்பம், வியாபார ஒப்பந்தம், கணக்கு துவங்குதல், தகவல் தொழில்நுட்பம், எழுத்துப் பணிகளுக்கு சுப ஹோரை.",
        shortSummaryEn = "Auspicious for education, trade, signing contracts, commerce, accounting, IT/software work, and communication.",
        shortSummaryHi = "विद्यारंभ, व्यापारिक समझौते, खाता-बही, संचार, सूचना प्रौद्योगिकी एवं लेखन कार्य हेतु शुभ।",
        favorableActivitiesTa = listOf("கல்வி துவக்கம் (வித்யாரம்பம்)", "வியாபார ஒப்பந்தம் கையெழுத்திடல்", "வங்கி கணக்கு, முதலீடு", "புதிய பாடம் படித்தல்", "பயணம் மேற்கொள்ளுதல்"),
        favorableActivitiesEn = listOf("Education & learning", "Signing commercial contracts", "Banking & trade", "Publishing & writing", "Travel & communications"),
        favorableActivitiesHi = listOf("विद्यारंभ एवं अध्ययन", "व्यापारिक अनुबंध हस्ताक्षर", "बैंकिंग एवं लेखा कार्य", "दस्तावेज़ रजिस्ट्री", "नवीन यात्रा"),
        unfavorableActivitiesTa = listOf("நிலம், வீடு அடமானம் வைத்தல்", "பகைவருடன் சமாதானம்"),
        unfavorableActivitiesEn = listOf("Mortgaging fixed properties", "Unverified settlements"),
        unfavorableActivitiesHi = listOf("अचल संपत्ति बंधक रखना", "विवादास्पद समझौते")
    ),

    MOON(
        planetNameTa = "சந்திரன் (Chandra)",
        planetNameEn = "Moon (Chandra)",
        planetNameHi = "चन्द्र (Chandra)",
        symbolEmoji = "🌙",
        nature = HoraNature.AUSPICIOUS,
        deityTa = "ஸ்ரீ பார்வதி தேவி / புவனேஸ்வரி",
        deityEn = "Goddess Parvati / Bhuvaneshwari",
        deityHi = "माता पार्वती / भुवनेश्वरी",
        shortSummaryTa = "சுப பயணம், விவசாயம், பால்/நீர் வியாபாரம், பெண்களின் காரியங்கள், முத்து/வெள்ளி ஆபரணம் வாங்க நன்று.",
        shortSummaryEn = "Favorable for journeys, agriculture, liquids/dairy trade, buying silver/pearls, food ventures, and creative ideas.",
        shortSummaryHi = "शुभ यात्रा, कृषि, दुग्ध एवं जल व्यवसाय, चांदी-मोती क्रय, मातृ-सेवा एवं नवीन मित्रता हेतु शुभ।",
        favorableActivitiesTa = listOf("சுப பிரயாணம் புறப்படுதல்", "பால், நீர், மளிகை வியாபாரம்", "விவசாயப் பணிகள்", "வெள்ளி & முத்து வாங்குதல்", "குடும்ப சமாதானம்"),
        favorableActivitiesEn = listOf("Starting auspicious journeys", "Dairy, liquid & culinary commerce", "Agriculture & gardening", "Buying silver and pearls", "Family reconciliations"),
        favorableActivitiesHi = listOf("मंगलमय यात्रा आरंभ", "जल, दुग्ध व खाद्य व्यवसाय", "कृषि एवं बागवानी", "चांदी-मोती क्रय", "पारिवारिक सौहार्द"),
        unfavorableActivitiesTa = listOf("வழக்கு தொடருதல்", "கடுமையான முடிவுகள்", "ஆயுதம் கையாளுதல்"),
        unfavorableActivitiesEn = listOf("Initiating lawsuits", "Harsh punitive actions", "Handling weapons/warfare"),
        unfavorableActivitiesHi = listOf("मुकदमा दायर करना", "कठोर दंड देना", "शस्त्र निर्माण")
    ),

    SATURN(
        planetNameTa = "சனி (Shani)",
        planetNameEn = "Saturn (Shani)",
        planetNameHi = "शनि (Shani)",
        symbolEmoji = "🪐",
        nature = HoraNature.INAUSPICIOUS,
        deityTa = "ஸ்ரீ சனீஸ்வரர் / எமதர்மராஜன்",
        deityEn = "Lord Shaneeshwara / Yama Dharma Raja",
        deityHi = "शनि देव / यमराज",
        shortSummaryTa = "பொதுவாக சுப காரியங்களைத் தவிர்க்கவும். இரும்பு, எந்திரம், நிலக்கரி, பழைய பொருள் விற்க, தியானம் செய்ய நன்று.",
        shortSummaryEn = "Generally inauspicious for auspicious milestones. Suitable for iron/machinery work, oil/fuel trade, cleaning, and deep meditation.",
        shortSummaryHi = "सामान्यतः शुभ कार्यों के लिए त्याज्य। लोहा, मशीनरी, तेल, प्राचीन वस्तुएं एवं गहन वैराग्य-ध्यान हेतु उपयोगी।",
        favorableActivitiesTa = listOf("இரும்பு, எந்திர வேலைகள்", "எண்ணெய், பெட்ரோல் வியாபாரம்", "பழைய பொருட்கள் விற்பனை", "கட்டட அடித்தளப் பணிகள்", "வைராக்கிய தியானம்"),
        favorableActivitiesEn = listOf("Iron, steel & machinery work", "Oil, petroleum & mining trade", "Disposal of old scrap", "Laying deep foundations", "Detachment & meditation"),
        favorableActivitiesHi = listOf("लोहा एवं मशीनरी कार्य", "तेल एवं खनिज व्यवसाय", "कबाड़ निस्तारण", "मजबूत नींव निर्माण", "वैराग्य व ध्यान"),
        unfavorableActivitiesTa = listOf("திருமணம், சீமந்தம்", "புதிய வீடு குடிபுகுதல்", "புதிய ஆடை அணிதல்", "முக்கிய ஒப்பந்தங்கள்", "சுப பயணம்"),
        unfavorableActivitiesEn = listOf("Weddings & auspicious events", "House warming (Grihapravesham)", "Wearing new clothes", "Major investments", "Joyous travel"),
        unfavorableActivitiesHi = listOf("विवाह एवं गृहप्रवेश", "नवीन वस्त्राभूषण धारण", "शुभ यात्रा आरंभ", "महत्वपूर्ण अनुबंध", "मांगलिक उत्सव")
    ),

    JUPITER(
        planetNameTa = "குரு (Guru / Brihaspati)",
        planetNameEn = "Jupiter (Guru)",
        planetNameHi = "बृहस्पति (Guru)",
        symbolEmoji = "👑",
        nature = HoraNature.AUSPICIOUS,
        deityTa = "ஸ்ரீ பிரம்மா / தட்சிணாமூர்த்தி",
        deityEn = "Lord Brahma / Dakshinamurthy",
        deityHi = "भगवान ब्रह्मा / दक्षिणामूर्ति",
        shortSummaryTa = "சர்வ சுப காரியங்களுக்கும் தலையாய ஹோரை. தங்கம் வாங்க, திருமணம், பூணூல், குரு உபதேசம், புதிய முதலீடு செய்ய சர்வ உத்தமம்.",
        shortSummaryEn = "The most auspicious Hora for all sacred milestones. Ideal for buying gold, weddings, spiritual initiation, banking, and major investments.",
        shortSummaryHi = "सर्वश्रेष्ठ महाशुभ होरा। स्वर्ण क्रय, विवाह, उपनयन, दीक्षा, उच्च अध्ययन, नवीन प्रतिष्ठान एवं धार्मिक अनुष्ठान हेतु उत्तम।",
        favorableActivitiesTa = listOf("தங்கம் & விலைமதிப்பற்ற பொருள் வாங்குதல்", "திருமணம், நிச்சயதார்த்தம்", "உபநயனம், மந்திர உபதேசம்", "புதிய தொழில், முதலீடு", "ஆன்மீக யாத்திரை, குரு தரிசனம்"),
        favorableActivitiesEn = listOf("Purchasing gold & major assets", "Weddings & sacred ceremonies", "Upanayanam & spiritual initiations", "Opening banks/new ventures", "Meeting spiritual masters & Gurus"),
        favorableActivitiesHi = listOf("स्वर्ण एवं बहुमूल्य वस्तु क्रय", "विवाह एवं मांगलिक कार्य", "उपनयन व दीक्षा ग्रहण", "नवीन प्रतिष्ठान आरंभ", "गुरु दर्शन एवं तीर्थ यात्रा"),
        unfavorableActivitiesTa = listOf("கொடுமையான செயல்கள்", "பகைமை வளர்த்தல்"),
        unfavorableActivitiesEn = listOf("Cruel or unethical activities", "Fomenting animosity"),
        unfavorableActivitiesHi = listOf("क्रूर कार्य", "अनैतिक व्यवहार")
    ),

    MARS(
        planetNameTa = "செவ்வாய் (Mangala / Sevvai)",
        planetNameEn = "Mars (Mangala)",
        planetNameHi = "मंगल (Mangala)",
        symbolEmoji = "🔥",
        nature = HoraNature.INAUSPICIOUS,
        deityTa = "ஸ்ரீ முருகப்பெருமான் / சுப்பிரமணியர்",
        deityEn = "Lord Murugan / Sri Siva Subramaniya Swami",
        deityHi = "भगवान मुरुगन / कार्तिकेय / मंगल देव",
        shortSummaryTa = "பொதுவான சுப காரியங்களைத் தவிர்க்கவும். பூமி/நில பரிவர்த்தனை, அறுவை சிகிச்சை, விளையாட்டு, பாதுகாப்பு, வழக்குகளை எதிர்கொள்ள நன்று.",
        shortSummaryEn = "Avoid auspicious celebrations. Favorable for land/property acquisition, surgery, sports, military, machinery, and debt settlement.",
        shortSummaryHi = "सामान्य शुभ कार्यों हेतु त्याज्य। भूमि क्रय, शल्य चिकित्सा (सर्जरी), खेल-कूद, साहस, सेना एवं ऋण मुक्ति हेतु उत्तम।",
        favorableActivitiesTa = listOf("பூமி/நிலம் வாங்குதல்", "மருத்துவ அறுவை சிகிச்சை", "விளையாட்டு & உடற்பயிற்சி", "எந்திரம் பழுதுபார்த்தல்", "கடன் அடைத்தல், வழக்குகளை வெல்லுதல்"),
        favorableActivitiesEn = listOf("Land & real estate transactions", "Surgical operations", "Sports & physical fitness", "Machinery repairs", "Settling debts & legal battles"),
        favorableActivitiesHi = listOf("भूमि एवं भवन क्रय-विक्रय", "शल्य चिकित्सा (ऑपरेशन)", "खेल-कूद एवं व्यायाम", "यंत्र मरम्मत", "ऋण चुकाना एवं विजय प्राप्ति"),
        unfavorableActivitiesTa = listOf("திருமணம்", "சுப பயணம்", "சமாதான பேச்சுவார்த்தை", "புதுமனை புகுதல்"),
        unfavorableActivitiesEn = listOf("Weddings & engagements", "Pleasant long journeys", "Peace discussions", "Grihapravesham"),
        unfavorableActivitiesHi = listOf("विवाह संस्कार", "आनंद यात्रा", "शांति वार्ता", "गृहप्रवेश")
    );

    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> planetNameTa
        AppLanguage.HINDI -> planetNameHi
        AppLanguage.ENGLISH -> planetNameEn
    }

    fun getDeity(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> deityTa
        AppLanguage.HINDI -> deityHi
        AppLanguage.ENGLISH -> deityEn
    }

    fun getSummary(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> shortSummaryTa
        AppLanguage.HINDI -> shortSummaryHi
        AppLanguage.ENGLISH -> shortSummaryEn
    }

    fun getFavorableActivities(lang: AppLanguage): List<String> = when (lang) {
        AppLanguage.TAMIL -> favorableActivitiesTa
        AppLanguage.HINDI -> favorableActivitiesHi
        AppLanguage.ENGLISH -> favorableActivitiesEn
    }

    fun getUnfavorableActivities(lang: AppLanguage): List<String> = when (lang) {
        AppLanguage.TAMIL -> unfavorableActivitiesTa
        AppLanguage.HINDI -> unfavorableActivitiesHi
        AppLanguage.ENGLISH -> unfavorableActivitiesEn
    }
}

/**
 * A single 1-hour/fractional Hora block of the 24 Horas
 */
data class HoraBlock(
    val horaNumber: Int, // 1 to 24
    val isDaytime: Boolean, // true = 1 to 12 (Sunrise to Sunset), false = 13 to 24 (Sunset to Next Sunrise)
    val startTime: LocalTime,
    val endTime: LocalTime,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val durationMinutes: Double,
    val ruler: HoraRuler
) {
    val nature: HoraNature get() = ruler.nature
    val isAuspicious: Boolean get() = ruler.nature.isAuspicious

    fun getPeriodLabel(lang: AppLanguage): String {
        return if (isDaytime) {
            when (lang) {
                AppLanguage.TAMIL -> "பகல் ஹோரை $horaNumber"
                AppLanguage.HINDI -> "दिन होरा $horaNumber"
                AppLanguage.ENGLISH -> "Day Hora $horaNumber"
            }
        } else {
            val nightIndex = horaNumber - 12
            when (lang) {
                AppLanguage.TAMIL -> "இரவு ஹோரை $nightIndex (மொத்தம்: $horaNumber)"
                AppLanguage.HINDI -> "रात्रि होरा $nightIndex (कुल: $horaNumber)"
                AppLanguage.ENGLISH -> "Night Hora $nightIndex (Total: $horaNumber)"
            }
        }
    }

    fun formatTimeRange(formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)): String {
        return "${startTime.format(formatter)} - ${endTime.format(formatter)}"
    }

    /**
     * Checks if the specified instant is inside this Hora block
     */
    fun isCurrent(targetDateTime: LocalDateTime): Boolean {
        return (targetDateTime.isEqual(startDateTime) || targetDateTime.isAfter(startDateTime)) &&
                targetDateTime.isBefore(endDateTime)
    }
}

/**
 * Result object containing the complete 24 Hora calculations for a given Vedic Day
 */
data class HoraCalculationResult(
    val targetDate: LocalDate,
    val dayOfWeek: DayOfWeek,
    val vedicDayLord: HoraRuler,
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val nextDaySunrise: LocalTime,
    val daytimeDurationMinutes: Long,
    val nighttimeDurationMinutes: Long,
    val dayHoraDurationMinutes: Double,
    val nightHoraDurationMinutes: Double,
    val allHoras: List<HoraBlock>
) {
    init {
        require(allHoras.size == 24) { "A Vedic day must contain exactly 24 Horas" }
    }

    val daytimeHoras: List<HoraBlock> get() = allHoras.subList(0, 12)
    val nighttimeHoras: List<HoraBlock> get() = allHoras.subList(12, 24)

    val auspiciousHoras: List<HoraBlock> get() = allHoras.filter { it.isAuspicious }

    fun getCurrentHora(currentTime: LocalDateTime): HoraBlock? {
        return allHoras.firstOrNull { it.isCurrent(currentTime) }
    }
}
