package com.example.data.repository

import com.example.data.model.DharmaSastraDisclaimer
import com.example.data.model.SastraTopic

class DharmaSastraRepository {

    val disclaimer = DharmaSastraDisclaimer()

    private val topics = listOf(
        SastraTopic(
            id = "marana_asaucha",
            titleTa = "மரண ஆசௌசம் (தீட்டு விதிகள்)",
            titleEn = "Marana Asaucha (Mourning & Impurity Rules)",
            summaryTa = "குடும்பத்தில் நிகழும் மறைவின் போது அனுஷ்டிக்கப்பட வேண்டிய ஆசௌச கால அளவு மற்றும் சாஸ்திர நியமங்கள்.",
            summaryEn = "Scriptural guidelines on the duration of ceremonial impurity following a demise in the family.",
            detailedTextTa = "மரண ஆசௌசம் என்பது ஜீவன் உடலை விட்டு பிரியும் போது ஏற்படும் ஆத்ம-சரீர தொடர்பின் ஆசௌச நிலையாகும். இந்த காலத்தில் நித்திய சந்தியாவந்தனம் மனதால் காயத்ரி ஜபத்துடன் செய்யப்பட வேண்டும்; தேவ பூஜா, யாகங்கள், சுபகாரியங்கள் தவிர்க்கப்பட வேண்டும். பத்தாம் நாள் வரை ஸ்நானம் மற்றும் வஸ்திர சுத்தி பிரதானமானது.",
            detailedTextEn = "Marana Asaucha represents the period of ritual austerity observed when a soul departs. Routine devotional rituals are performed mentally without temple poojas. Daily purifying baths and ritual simplicity are observed until the culmination of the 10th-day rites.",
            daysCountTa = "10 நாட்கள் / குடும்ப மரபிற்கு ஏற்ப",
            daysCountEn = "10 Days (according to familial tradition)",
            sourceTexts = listOf("மனுஸ்மிருதி (Manusmriti V.59)", "யாஞ்ஞவல்கிய ஸ்மிருதி (Yajnavalkya Smriti III.18)", "பராசர ஸ்மிருதி (Parasara Smriti)"),
            iconEmoji = "📜"
        ),
        SastraTopic(
            id = "sapinda",
            titleTa = "சபிண்டர் முறை (Sapinda Relationship)",
            titleEn = "Sapinda Relations & Rites",
            summaryTa = "தந்தை வழியில் ஏழு தலைமுறை மற்றும் தாய் வழியில் ஐந்து தலைமுறை உறவினர்களுக்குரிய ஆசௌச விதிகள்.",
            summaryEn = "Guidelines regarding seven ancestral paternal generations and five maternal degrees sharing pinda connection.",
            detailedTextTa = "சபிண்டர்கள் என்பவர்கள் ஒரே பிண்ட தானத்தில் பங்குபெறும் மிக நெருங்கிய ரத்த வழி உறவினர்கள். தந்தை வழியில் 7 தலைமுறையினர் பூர்ண சபிண்டர்கள் ஆவர். இவர்களுக்கு மறைவு ஏற்பட்டால் 10 நாட்கள் பூர்ண ஆசௌசம் அனுஷ்டிக்க வேண்டும்.",
            detailedTextEn = "Sapindas are consanguineous relatives within seven generations on the father's side and five generations on the mother's side. They observe complete 10-day Asaucha upon the demise of a fellow Sapinda.",
            daysCountTa = "10 நாட்கள் பூர்ண தீட்டு",
            daysCountEn = "10 Full Days",
            sourceTexts = listOf("கௌதம தர்மசூத்திரம் (Gautama Dharmasutra XIV.1-13)", "மனுஸ்மிருதி (Manusmriti V.60)"),
            iconEmoji = "🌿"
        ),
        SastraTopic(
            id = "samanodaka",
            titleTa = "சமானோதகர் (Samanodaka Relations)",
            titleEn = "Samanodaka Kinship & Period",
            summaryTa = "சபிண்டர்களுக்கு அப்பாற்பட்ட 8 முதல் 14-ஆம் தலைமுறை வரையிலான பங்காளி உறவினர்களுக்கான தீட்டு.",
            summaryEn = "Relatives from 8th to 14th degrees of lineage who share common libation rites.",
            detailedTextTa = "சமானோதகர் எனப்படும் தூரத்து பங்காளிகளுக்கு மரணம் நேர்ந்தால் 3 நாட்கள் அல்லது சவ சம்ஸ்கார செய்தி கேட்ட உடனே ஸ்நானம் செய்து ஆசௌசம் நிவர்த்தி பெறும் விதி பாரம்பரிய நூல்களில் கூறப்பட்டுள்ளது.",
            detailedTextEn = "Samanodakas share extended lineage beyond the 7th generation. Their Asaucha typically spans 3 days or culminates with an immediate purifying sacred bath upon receiving the notification.",
            daysCountTa = "3 நாட்கள் அல்லது உடனடி ஸ்நானம்",
            daysCountEn = "3 Days / Immediate Bath",
            sourceTexts = listOf("போதாயன தர்மசூத்திரம் (Baudhayana Dharmasutra I.5.11)", "வசிஷ்ட தர்மசூத்திரம் (Vasistha IV.17)"),
            iconEmoji = "🌊"
        ),
        SastraTopic(
            id = "other_relatives",
            titleTa = "மற்ற உறவினர்கள் (Other Relatives)",
            titleEn = "Aunty, Uncle, In-Laws & Other Relatives",
            summaryTa = "தாய் மாமன், அத்தை, மாமனார்-மாமியார், மருமகன் போன்ற மற்ற உறவினர்களுக்குரிய ஆசௌச காலங்கள்.",
            summaryEn = "Prescribed Asaucha spans for maternal uncles, in-laws, paternal aunts, and daughters married into other gotras.",
            detailedTextTa = "வாழ்க்கைத்துணையின் பெற்றோர் (மாமனார், மாமியார்), தாய் மாமன், அத்தை, சகோதரியின் மறைவு போன்றவற்றிற்கு அவரவர் சம்பிரதாயப்படி பக்ஷிணி (ஒன்றரை நாள்), மூன்று நாட்கள் அல்லது ஒரு நாள் (ஸ்நான தீட்டு) அனுஷ்டிக்கப்படுகிறது.",
            detailedTextEn = "For maternal uncle, mother-in-law/father-in-law, sister, or married daughter, traditional texts prescribe Pakshini (1.5 days), 3 days, or single-day bath purification depending on family achara.",
            daysCountTa = "1 முதல் 3 நாட்கள் (பக்ஷிணி)",
            daysCountEn = "1 to 3 Days (Pakshini)",
            sourceTexts = listOf("யாஞ்ஞவல்கிய ஸ்மிருதி (Yajnavalkya Smriti III.24)", "மனுஸ்மிருதி (Manusmriti V.81)"),
            iconEmoji = "🤝"
        ),
        SastraTopic(
            id = "death_another_location",
            titleTa = "தேசம் கடந்த மரணம் (Death in Another Country/City)",
            titleEn = "Demise in a Distant Location",
            summaryTa = "தொலைதூர ஊரிலோ அல்லது வெளிநாட்டிலோ மறைவு நிகழும் போது அனுஷ்டிக்க வேண்டிய முறை.",
            summaryEn = "Protocols for observing rites when a family member passes away in another distant city or country.",
            detailedTextTa = "வெளியூரில் அல்லது வெளிநாட்டில் மரணம் நிகழ்ந்து, செய்தி உடனடியாக தெரியவந்தால், செய்தி அறிந்த கணத்திலிருந்து மீதமுள்ள நாட்களுக்கு ஆசௌசம் அனுஷ்டிக்க வேண்டும்.",
            detailedTextEn = "If death occurs in a distant country, upon receiving the news within the 10-day window, Asaucha is observed for the remaining days of that 10-day cycle.",
            daysCountTa = "மீதமுள்ள நாட்கள்",
            daysCountEn = "Remaining days of 10-day span",
            sourceTexts = listOf("வசிஷ்ட தர்மசூத்திரம் (Vasistha Dharmasutra IV.24)", "போதாயன தர்மசூத்திரம்"),
            iconEmoji = "✈️"
        ),
        SastraTopic(
            id = "late_notification",
            titleTa = "காலம் கடந்த செய்தி அறிதல் (Late Notification)",
            titleEn = "Notification After 10 Days / 1 Year",
            summaryTa = "பத்து நாட்களுக்குப் பிறகோ அல்லது வருடத்திற்குப் பிறகோ மரண செய்தி தெரியவரும் போது செய்யும் முறை.",
            summaryEn = "Atonement and purification procedures when death news is received after the 10th day, 3 months, or full year.",
            detailedTextTa = "10 நாட்களுக்குப் பிறகு செய்தி அறிந்தால் 3 நாட்கள் தீட்டு; 3 மாதங்களுக்குப் பிறகு அறிந்தால் பக்ஷிணி (ஒன்றரை நாள்); ஒரு வருடத்திற்குப் பிறகு அறிந்தால் உடனே தலைமுழுகி ஸ்நானம் செய்து ஆசௌசம் நீங்குதல் விதி.",
            detailedTextEn = "If informed after 10 days, 3 days are observed. If informed after 3 months, 1.5 days (Pakshini). If informed after one complete year, an immediate sacred head-bath suffices.",
            daysCountTa = "ஸ்நானம் / 3 நாட்கள்",
            daysCountEn = "Purifying Bath to 3 Days",
            sourceTexts = listOf("மனுஸ்மிருதி (Manusmriti V.75-77)", "கௌதம தர்மசூத்திரம் XIV.18"),
            iconEmoji = "⏳"
        ),
        SastraTopic(
            id = "corpse_rules",
            titleTa = "சரீர சம்ஸ்கார விதி (Funeral & Cremation Rules)",
            titleEn = "Body Sanctification & Cremation Rites",
            summaryTa = "உடலைத் தொடுதல், சுமத்தல் மற்றும் தகனம் செய்வோருக்குரிய நியமங்கள்.",
            summaryEn = "Sanctification protocols for family members carrying the mortal remains and performing Agni Samskara.",
            detailedTextTa = "பூத உடலைச் சுமப்பவர்கள் மற்றும் அந்தியேஷ்டி செய்பவர்கள் புனித நீராடி, உடுத்திய ஆடையை அகற்றி, நெய் தொட்டு சுத்தி செய்து கொள்ள வேண்டும். அந்தியேஷ்டி காரியம் முடிந்தவுடன் பிரேம சம்ஸ்கார ஸ்நானம் கட்டாயமானது.",
            detailedTextEn = "Those carrying the mortal frame and the chief performer (Karta) must undergo full head-bath with clothes immediately after cremation, touch sacred Agni and Ghee for purification before entering homes.",
            daysCountTa = "அந்தியேஷ்டி முடிவில் ஸ்நானம்",
            daysCountEn = "Post-cremation purification bath",
            sourceTexts = listOf("யாஞ்ஞவல்கிய ஸ்மிருதி (Yajnavalkya III.26)", "மனுஸ்மிருதி V.103"),
            iconEmoji = "🔥"
        ),
        SastraTopic(
            id = "water_offerings",
            titleTa = "தர்ப்பணம் & உதகதானம் (Water Offerings)",
            titleEn = "Udaka Dana & Daily Water Libations",
            summaryTa = "முதல் நாள் முதல் பத்தாம் நாள் வரை இறந்த ஆத்மாவின் தாகம் தணிக்க வழங்கப்படும் புனித நீர்க்கடன்கள்.",
            summaryEn = "Daily sacred water libations offered with sesame seeds from Day 1 to Day 10 to pacify the departing soul.",
            detailedTextTa = "மறைந்த ஆத்மா பித்ரு லோகத்தை நோக்கிப் பயணம் செய்யும்போது ஏற்படும் தாபத்தை போக்க தர்பைப் புல்லில் எள் கலந்த தூய நீர் (திலோதகம்) தினசரி வழங்கப்படுகிறது. இது பிரேத ஆத்மாவிற்கு சாந்தியை அளிக்கிறது.",
            detailedTextEn = "Tilodaka (water with black sesame on sacred Darbha grass) is offered daily by the Karta to bring peace and sustenance to the departing consciousness on its transitional journey.",
            daysCountTa = "தினசரி 1 முதல் 10-ஆம் நாள் வரை",
            daysCountEn = "Daily from Day 1 to 10",
            sourceTexts = listOf("போதாயன கிருஹ்ய சூத்திரம் (Baudhayana Grihya Sutra)", "ஆபஸ்தம்ப தர்மசூத்திரம்"),
            iconEmoji = "💧"
        ),
        SastraTopic(
            id = "tenth_day",
            titleTa = "பத்தாம் நாள் காரியங்கள் (10th Day Rites)",
            titleEn = "10th Day Pinda Pradhanam & Shanti",
            summaryTa = "பிரேத சரீர ஆசௌசத்தின் இறுதி நாள் காரியங்கள், க்ஷவரம் மற்றும் பாசான் சுத்தி.",
            summaryEn = "The critical transitional rites of the 10th day marking the conclusion of the immediate bodily Asaucha.",
            detailedTextTa = "பத்தாம் நாளில் பங்காளி சபிண்டர்கள் அனைவரும் நதிக்கரை அல்லது புண்ணிய தீர்த்தத்தில் க்ஷவரம் (முடி இறக்குதல்) செய்து நீராடுவர். பிரேத பிண்ட தானம் நிறைவு பெற்று, ஆசௌச நிவர்த்தி ஸ்நானம் நடைபெறுகிறது.",
            detailedTextEn = "On the 10th day, male Sapindas undergo ceremonial tonsure and sacred river bathing. Final Prata Pinda is offered, concluding the direct physical mourning impurity.",
            daysCountTa = "10-ஆம் நாள்",
            daysCountEn = "10th Day",
            sourceTexts = listOf("மனுஸ்மிருதி V.83", "யாஞ்ஞவல்கிய ஸ்மிருதி III.19"),
            iconEmoji = "🪒"
        ),
        SastraTopic(
            id = "sapindikaranam",
            titleTa = "ஏகாதசாகம் & சபிண்டீகரண விதி (11th, 12th & 13th Day Rites)",
            titleEn = "11th, 12th Day & Sapindikarana Rites",
            summaryTa = "பிரேத நிலை நீங்கி முன்னோர்களான பித்ருக்களோடு இணையும் சபிண்டீகரண மகா புண்ணிய வைபவம்.",
            summaryEn = "The sacred transition where the departed soul graduates from Preta status to join the divine Ancestral Pitrus.",
            detailedTextTa = "11-ஆம் நாளில் ஏகோத்திஷ்ட சிராத்தம் மற்றும் விருஷோத்ஸர்ஜனம் நடைபெறும். 12-ஆம் நாளில் சபிண்டீகரணம் எனப்படும் மகா காரியம் மூலம் பிண்டங்களை இணைத்து, ஆத்மாவை பித்ருக்களோடு சேர்த்து வைப்பர். 13-ஆம் நாளில் சுப ஸ்வீகாரம் நடைபெற்று மீண்டும் சுபகாரியங்கள் தொடங்க தகுதி பெறும்.",
            detailedTextEn = "On the 11th day, Ekoddhishta Sraddha is performed. On the 12th day, Sapindikarana merges the individual Pinda with ancestral Pindas, elevating the soul to Pitru status. 13th day Subha Sweekaram marks the resumption of auspicious household activities.",
            daysCountTa = "11, 12 மற்றும் 13-ஆம் நாட்கள்",
            daysCountEn = "11th, 12th and 13th Days",
            sourceTexts = listOf("கௌதம தர்மசூத்திரம் XIV.30", "மனுஸ்மிருதி III.247-250", "யாஞ்ஞவல்கிய ஸ்மிருதி I.254"),
            iconEmoji = "🕉️"
        )
    )

    fun getAllTopics(): List<SastraTopic> = topics

    fun getTopicById(id: String): SastraTopic? = topics.firstOrNull { it.id == id }
}
