package com.example.data.repository

import com.example.data.model.*

object RasiTransit2026Repository {

    val rasiPalan2026List: List<RasiTransitPalan2026> = listOf(
        // ♈ 1. மேஷம் (Aries)
        RasiTransitPalan2026(
            rasi = Rasi.MESHAM,
            severity = AlertSeverity.HIGH_ALERT,
            alertTagTa = "🪐 சனி — முக்கிய கவனம்",
            alertTagEn = "🪐 Saturn (Sani) — Major Attention",
            alertTagHi = "🪐 शनि — विशेष सावधानी",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "🪐 சனி",
                    planetEn = "🪐 Saturn (Sani)",
                    planetHi = "🪐 शनि (Saturn)",
                    headlineTa = "சனி 12-ஆம் இடம் (விரய சனி காலம்)",
                    headlineEn = "Saturn in 12th House (Viraya Sani)",
                    headlineHi = "12वें भाव में शनि (व्यय शनि)",
                    detailsTa = "சனி 12-ஆம் இடத்தில் இருப்பதால் செலவுகள், உடல் சோர்வு, மன அழுத்தம், வேலைக்காக அலைச்சல் போன்றவற்றில் கவனம் தேவை.",
                    detailsEn = "Saturn transiting the 12th house necessitates caution regarding expenditure, physical fatigue, stress, and career-related travel.",
                    detailsHi = "12वें भाव में शनि के कारण ख़र्च, शारीरिक थकान, मानसिक तनाव और भागदौड़ में सावधानी आवश्यक है।"
                )
            ),
            careerTa = "முயற்சிக்குப் பிறகு முன்னேற்றம்; சில பணிகளில் தாமதம் இருக்கலாம்.",
            careerEn = "Progress after sustained effort; occasional delays in outcomes.",
            careerHi = "कड़े परिश्रम के बाद प्रगति; कार्यों में कुछ विलंब हो सकता है।",
            financeTa = "வருமானம் இருந்தாலும் செலவு அதிகரிக்கலாம்.",
            financeEn = "Steady income stream, but expenses may rise substantially.",
            financeHi = "आय बनी रहेगी, परंतु व्यय में वृद्धि संभव है।",
            marriageTa = "பொறுமை மற்றும் புரிதல் அவசியம்.",
            marriageEn = "Patience and mutual understanding are essential.",
            marriageHi = "धैर्य एवं आपसी समझ अत्यंत आवश्यक है।",
            educationTa = "தொடர்ந்து முயற்சி செய்தால் நல்ல பலன்.",
            educationEn = "Consistent hard work will yield commendable academic results.",
            educationHi = "निरंतर अध्ययन से उत्तम परिणाम प्राप्त होंगे।",
            healthTa = "சோர்வு மற்றும் மன அழுத்தத்தை அலட்சியம் செய்ய வேண்டாம்.",
            healthEn = "Do not neglect fatigue, proper rest, and stress management.",
            healthHi = "थकान और मानसिक तनाव को अनदेखा न करें।",
            familyTa = "சிறிய கருத்து வேறுபாடுகளை அமைதியாக தீர்க்கவும்.",
            familyEn = "Resolve minor differences peacefully with diplomatic dialogue.",
            familyHi = "पारिवारिक मतभेदों को शांतिपूर्वक सुलझाएं।",
            primaryAdviceTa = "செலவு மற்றும் உடல்நலத்தில் கட்டுப்பாடு மற்றும் நிதானம் அவசியம்.",
            primaryAdviceEn = "Maintain strict budget control and prioritize physical well-being.",
            primaryAdviceHi = "ख़र्चों पर नियंत्रण रखें और स्वास्थ्य का ध्यान रखें।"
        ),

        // ♉ 2. ரிஷபம் (Taurus)
        RasiTransitPalan2026(
            rasi = Rasi.RISHABAM,
            severity = AlertSeverity.STANDARD_WATCH,
            alertTagTa = "🪐 சனி — சுப பார்வை & கவனம்",
            alertTagEn = "🪐 Saturn (Sani) — Favorable & Alert",
            alertTagHi = "🪐 शनि — शुभ प्रभाव एवं सावधानी",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "🪐 சனி",
                    planetEn = "🪐 Saturn (Sani)",
                    planetHi = "🪐 शनि (Saturn)",
                    headlineTa = "சனியின் 11-ஆம் இடப் பயணம் (லாப ஸ்தானம்)",
                    headlineEn = "Saturn in 11th House (House of Gains)",
                    headlineHi = "11वें भाव में शनि (लाभ स्थान)",
                    detailsTa = "சனியின் 11-ஆம் இடப் பயணம் பெரிய பாதிப்பாக இல்லாவிட்டாலும், சில முயற்சிகளில் ஆரம்ப தாமதம் ஏற்படலாம்.",
                    detailsEn = "Saturn in the 11th house is generally favorable for gains, though initial delays in new ventures may occur.",
                    detailsHi = "11वें भाव का शनि शुभ फलदायी है, यद्यपि नए कार्यों में थोड़ा विलंब हो सकता है।"
                )
            ),
            careerTa = "முன்னேற்ற வாய்ப்புகள் மற்றும் புதிய பொறுப்புகள்.",
            careerEn = "Promising career advancement opportunities and new roles.",
            careerHi = "करियर में पदोन्नति एवं प्रगति के शुभ अवसर।",
            financeTa = "வருமானம் மற்றும் சேமிப்புக்கு நல்ல வாய்ப்பு.",
            financeEn = "Excellent potential for income growth and solid financial savings.",
            financeHi = "आय और बचत में अच्छी वृद्धि के योग।",
            marriageTa = "திருமணத்திற்கு சாதகமான காலம்.",
            marriageEn = "Highly favorable time for marriage and relationship harmony.",
            marriageHi = "विवाह और प्रेम संबंधों के लिए अनुकूल समय।",
            educationTa = "மாணவர்களுக்கு நல்ல முன்னேற்றம்.",
            educationEn = "Notable progress and high achievements for students.",
            educationHi = "विद्यार्थियों के लिए उत्तम और उत्साहवर्धक वर्ष।",
            healthTa = "பொதுவாக உடல்நலம் நன்றாக இருக்கும்.",
            healthEn = "Overall robust health and good vitality.",
            healthHi = "स्वास्थ्य सामान्यतः उत्तम और स्फूर्तिदायक रहेगा।",
            familyTa = "குடும்ப ஆதரவு மற்றும் நல்ல சுப நிகழ்வுகள்.",
            familyEn = "Warm family support and joyous domestic celebrations.",
            familyHi = "परिवार का पूरा सहयोग और मांगलिक कार्य संपन्न होंगे।",
            primaryAdviceTa = "அவசர முதலீடுகளைத் தவிர்க்கவும்; சேமிப்பை வலுப்படுத்தவும்.",
            primaryAdviceEn = "Avoid speculative or hurried investments; focus on secure savings.",
            primaryAdviceHi = "जल्दबाजी में निवेश से बचें, सुरक्षित बचत पर ध्यान दें।"
        ),

        // ♊ 3. மிதுனம் (Gemini)
        RasiTransitPalan2026(
            rasi = Rasi.MITHUNAM,
            severity = AlertSeverity.STANDARD_WATCH,
            alertTagTa = "🪐 சனி — தொழில் கவனம்",
            alertTagEn = "🪐 Saturn (Sani) — Career Focus",
            alertTagHi = "🪐 शनि — कार्यक्षेत्र में सावधानी",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "🪐 சனி",
                    planetEn = "🪐 Saturn (Sani)",
                    planetHi = "🪐 शनि (Saturn)",
                    headlineTa = "சனி 10-ஆம் இடம் (கர்ம ஸ்தானம்)",
                    headlineEn = "Saturn in 10th House (Karma Sthana)",
                    headlineHi = "10वें भाव में शनि (कर्म स्थान)",
                    detailsTa = "சனி 10-ஆம் இடத்தில் இருப்பதால் வேலைப்பளு மற்றும் பொறுப்புகள் அதிகரிக்கலாம்.",
                    detailsEn = "Saturn in the 10th house brings higher workload, increased responsibilities, and professional tests.",
                    detailsHi = "10वें भाव में शनि के प्रभाव से कार्यभार और जिम्मेदारियों में वृद्धि होगी।"
                )
            ),
            careerTa = "பொறுப்புகள் அதிகரிக்கும்; கடின உழைப்பால் முன்னேற்றம்.",
            careerEn = "Responsibilities will rise; persistent hard work ensures success.",
            careerHi = "दायित्व बढ़ेंगे; कठोर परिश्रम से सफलता सुनिश्चित होगी।",
            financeTa = "வருமான வாய்ப்பு அதிகரிக்கலாம்; புதிய வழிகள் திறக்கும்.",
            financeEn = "Expanding avenues for earnings and fiscal stability.",
            financeHi = "आय के नए स्रोत खुलेंगे और आर्थिक स्थिति सुधरेगी।",
            marriageTa = "நல்ல வாய்ப்புகள் மற்றும் சுப பேச்சுவார்த்தைகள்.",
            marriageEn = "Promising alliance prospects and mutual understanding.",
            marriageHi = "विवाह के अच्छे प्रस्ताव और दांपत्य में मधुरता।",
            educationTa = "மாணவர்களுக்கு சாதகமான மற்றும் வெற்றிகரமான ஆண்டு.",
            educationEn = "Favorable academic year with competitive exam success.",
            educationHi = "प्रतियोगी परीक्षाओं में सफलता और शिक्षा में प्रगति।",
            healthTa = "வேலைப்பளுவால் ஏற்படும் மன அழுத்தத்தைக் கட்டுப்படுத்தவும்.",
            healthEn = "Manage stress and take breaks from heavy work routines.",
            healthHi = "कार्यभार से होने वाले मानसिक तनाव को नियंत्रित रखें।",
            familyTa = "குடும்பத்தினருக்கு போதிய நேரம் ஒதுக்குவது நல்லது.",
            familyEn = "Allocate dedicated quality time for family members.",
            familyHi = "परिवार के लिए पर्याप्त समय अवश्य निकालें।",
            primaryAdviceTa = "வேலை மற்றும் குடும்பத்தை சமநிலையில் வைத்துக்கொள்ளவும்.",
            primaryAdviceEn = "Maintain healthy work-life balance and avoid burnout.",
            primaryAdviceHi = "कार्य और पारिवारिक जीवन में उचित संतुलन बनाए रखें।"
        ),

        // ♋ 4. கடகம் (Cancer)
        RasiTransitPalan2026(
            rasi = Rasi.KADAGAM,
            severity = AlertSeverity.HIGH_ALERT,
            alertTagTa = "☊ ராகு / ☋ கேது — மிக முக்கிய கவனம்",
            alertTagEn = "☊ Rahu / ☋ Ketu — Major Transit Alert",
            alertTagHi = "☊ राहु / ☋ केतु — अति महत्वपूर्ण सावधानी",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "☊ ராகு",
                    planetEn = "☊ Rahu",
                    planetHi = "☊ राहु (Rahu)",
                    headlineTa = "ராகு 8-ஆம் இடம் (அஷ்டம ராகு)",
                    headlineEn = "Rahu in 8th House (Ashtama Rahu)",
                    headlineHi = "8वें भाव में राहु",
                    detailsTa = "ஆண்டின் பெரும்பகுதியில் ராகுவின் தாக்கம் 8-ஆம் இடம் சார்ந்த விஷயங்களில் இருப்பதால், எதிர்பாராத மாற்றங்கள், மனக்குழப்பம் மற்றும் உடல்நலத்தில் கவனம் தேவை.",
                    detailsEn = "Rahu transiting the 8th house for most of 2026 brings sudden transitions, mental perplexity, and health sensitivity.",
                    detailsHi = "वर्ष के अधिकांश भाग में 8वें भाव में राहु अचानक परिवर्तन, मानसिक भ्रम और स्वास्थ्य में सावधानी मांगता है।"
                ),
                PlanetTransitAlert(
                    planetTa = "☋ கேது",
                    planetEn = "☋ Ketu",
                    planetHi = "☋ केतु (Ketu)",
                    headlineTa = "கேது 2-ஆம் இடம் (தன ஸ்தானம்)",
                    headlineEn = "Ketu in 2nd House (Dhana Sthana)",
                    headlineHi = "2रे भाव में केतु (वाणी/धन स्थान)",
                    detailsTa = "பண விஷயங்கள் மற்றும் குடும்பப் பேச்சுகளில் நிதானமும் பொறுமையும் தேவை.",
                    detailsEn = "Ketu in the 2nd house requires prudence in financial transactions and gentleness in family conversations.",
                    detailsHi = "वित्तीय मामलों और पारिवारिक बातचीत में संयम व धैर्य बरतें।"
                )
            ),
            careerTa = "மாற்றங்கள் இருந்தாலும் விடாமுயற்சியால் முன்னேற்றம் கிடைக்கும்.",
            careerEn = "Despite organizational changes, steady perseverance brings growth.",
            careerHi = "कार्यस्थल पर बदलाव के बावजूद परिश्रम से उन्नति मिलेगी।",
            financeTa = "செலவுகளை திட்டமிடவும்; தேவையற்ற கடன் வேண்டாம்.",
            financeEn = "Plan budgets meticulously; avoid taking unnecessary loans.",
            financeHi = "व्यय की सुनियोजित योजना बनाएं, अनावश्यक कर्ज से बचें।",
            marriageTa = "உறவில் புரிதல் மற்றும் வெளிப்படைத்தன்மை முக்கியம்.",
            marriageEn = "Mutual clarity, transparency, and patience in relationship.",
            marriageHi = "संबंधों में स्पष्टता, विश्वास और समझदारी आवश्यक है।",
            educationTa = "முயற்சிக்கு ஏற்ப நல்ல பலன் கிடைக்கும்.",
            educationEn = "Sincere preparation will yield rewarding results.",
            educationHi = "एकाग्रता और नियमित अध्ययन से शुभ फल मिलेंगे।",
            healthTa = "சிறு உபாதைகளையும் அலட்சியம் செய்ய வேண்டாம்.",
            healthEn = "Do not neglect subtle symptoms; maintain regular checkups.",
            healthHi = "स्वास्थ्य समस्याओं को अनदेखा न करें, समय पर उपचार लें।",
            familyTa = "பேச்சில் நிதானம்; வார்த்தைகளில் கவனம் தேவை.",
            familyEn = "Guard your speech; avoid harsh words in household dialogues.",
            familyHi = "वाणी में मधुरता रखें, कटु शब्दों से बचें।",
            primaryAdviceTa = "எதிர்பாராத மாற்றங்களில் அவசர முடிவுகளைத் தவிர்க்கவும்.",
            primaryAdviceEn = "Do not take hasty decisions when facing sudden life changes.",
            primaryAdviceHi = "अचानक आए बदलावों में जल्दबाजी में निर्णय न लें।"
        ),

        // ♌ 5. சிம்மம் (Leo)
        RasiTransitPalan2026(
            rasi = Rasi.SIMHAM,
            severity = AlertSeverity.HIGH_ALERT,
            alertTagTa = "☊ ராகு / ☋ கேது — உறவு & கூட்டு கவனம்",
            alertTagEn = "☊ Rahu / ☋ Ketu — Relations & Partnership Alert",
            alertTagHi = "☊ राहु / ☋ केतु — साझेदारी एवं संबंध सावधानी",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "☊ ராகு",
                    planetEn = "☊ Rahu",
                    planetHi = "☊ राहु (Rahu)",
                    headlineTa = "ராகு 7-ஆம் இடம் (களத்திர ஸ்தானம்)",
                    headlineEn = "Rahu in 7th House (Kalathra Sthana)",
                    headlineHi = "7वें भाव में राहु",
                    detailsTa = "ராகு 7-ஆம் இடத்தில் இருப்பதால் திருமணம், கூட்டுத் தொழில் மற்றும் உறவுகளில் தெளிவு மற்றும் நம்பிக்கை அவசியம்.",
                    detailsEn = "Rahu in the 7th house calls for extreme clarity and transparency in matrimony, business partnerships, and public relations.",
                    detailsHi = "7वें भाव में राहु विवाह, साझेदारी व्यापार और रिश्तों में अत्यधिक स्पष्टता की मांग करता है।"
                ),
                PlanetTransitAlert(
                    planetTa = "☋ கேது",
                    planetEn = "☋ Ketu",
                    planetHi = "☋ केतु (Ketu)",
                    headlineTa = "கேது 1-ஆம் இடம் (ஜென்ம கேது)",
                    headlineEn = "Ketu in 1st House (Janma Ketu)",
                    headlineHi = "लग्न/1ले भाव में केतु",
                    detailsTa = "தனிப்பட்ட முடிவுகள், மனநிலை மற்றும் ஆன்மீக மாற்றங்கள் ஏற்படலாம்.",
                    detailsEn = "Ketu in the 1st house can trigger introspective thoughts, mood fluctuations, and spiritual awakening.",
                    detailsHi = "व्यक्तिगत निर्णयों में वैराग्य, अनिर्णय या आंतरिक सोच में बदलाव आ सकता है।"
                )
            ),
            careerTa = "தனி முயற்சியால் முன்னேற்றம்; கூட்டாளிகளிடம் விழிப்புணர்வு.",
            careerEn = "Individual efforts bear fruit; maintain vigilance with business partners.",
            careerHi = "व्यक्तिगत प्रयासों से प्रगति होगी; साझेदारों से सतर्क रहें।",
            financeTa = "வருமானம் இருந்தாலும் சுப செலவுகள் அதிகரிக்கலாம்.",
            financeEn = "Steady cash flow alongside increased expenditure on auspicious events.",
            financeHi = "आय अच्छी रहेगी, पर मांगलिक व आवश्यक कार्यों में ख़र्च बढ़ेगा।",
            marriageTa = "உறவில் பொறுமை மற்றும் விட்டுக்கொடுத்தல் மிகவும் முக்கியம்.",
            marriageEn = "Patience, compromise, and mutual empathy are vital in married life.",
            marriageHi = "वैवाहिक जीवन में धैर्य और समझौते की भावना बहुत जरूरी है।",
            educationTa = "கவனம் செலுத்தினால் நல்ல பலன்கள் கிட்டும்.",
            educationEn = "Deep focus and dedication will ensure academic distinction.",
            educationHi = "एकाग्रचित्त होकर अध्ययन करने पर उत्तम परिणाम मिलेंगे।",
            healthTa = "உடல் சோர்வு மற்றும் அலைச்சலை அலட்சியம் செய்ய வேண்டாம்.",
            healthEn = "Prioritize rest and do not ignore fatigue or headaches.",
            healthHi = "शारीरिक थकान और भागदौड़ को नजरअंदाज न करें।",
            familyTa = "தேவையற்ற வாக்குவாதங்களைத் தவிர்ப்பது அமைதி தரும்.",
            familyEn = "Steer clear of needless arguments to preserve domestic peace.",
            familyHi = "व्यर्थ के वाद-विवाद से बचकर पारिवारिक शांति बनाए रखें।",
            primaryAdviceTa = "திருமணம் மற்றும் கூட்டுத் தொடர்புகளில் அவசர முடிவுகள் வேண்டாம்.",
            primaryAdviceEn = "Exercise patience in marital matters and joint contractual agreements.",
            primaryAdviceHi = "विवाह और साझेदारी के मामलों में जल्दबाजी बिल्कुल न करें।"
        ),

        // ♍ 6. கன்னி (Virgo)
        RasiTransitPalan2026(
            rasi = Rasi.KANNI,
            severity = AlertSeverity.STANDARD_WATCH,
            alertTagTa = "☋ கேது — பேச்சு & குடும்ப கவனம்",
            alertTagEn = "☋ Ketu — Speech & Family Watch",
            alertTagHi = "☋ केतु — वाणी एवं परिवार सावधानी",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "☋ கேது",
                    planetEn = "☋ Ketu",
                    planetHi = "☋ केतु (Ketu)",
                    headlineTa = "கேது 12-ஆம் இடம் & ராகு 6-ஆம் இடம்",
                    headlineEn = "Ketu in 12th / Rahu in 6th (Victory over Obstacles)",
                    headlineHi = "12वें में केतु एवं 6ठे में राहु (शत्रु विजय)",
                    detailsTa = "குடும்பம், பணம் மற்றும் பேச்சு தொடர்பான விஷயங்களில் கவனம் தேவை. ராகு 6-ல் இருப்பது எதிரிகளை வெல்லும் ஆற்றல் தரும்.",
                    detailsEn = "Exercise prudence with financial allocations and speech. Rahu in the 6th grants immense power to overcome adversaries.",
                    detailsHi = "पारिवारिक मामलों व वाणी पर संयम रखें। 6ठे भाव का राहु विरोधियों पर विजय दिलाएगा।"
                )
            ),
            careerTa = "தொழில் மற்றும் வேலையில் நல்ல முன்னேற்ற வாய்ப்புகள்.",
            careerEn = "Favorable opportunities for job growth and enterprise expansion.",
            careerHi = "नौकरी और व्यवसाय में शानदार उन्नति के अवसर।",
            financeTa = "பொதுவாக நல்ல நிதி நிலை; சேமிப்பில் விழிப்புணர்வு.",
            financeEn = "Generally sound financial standing; vigilance needed for savings.",
            financeHi = "वित्तीय स्थिति संतोषजनक रहेगी; बचत बढ़ाने पर ध्यान दें।",
            marriageTa = "சாதகமான திருமண வாய்ப்புகள் மற்றும் சுப பேச்சுகள்.",
            marriageEn = "Auspicious marital proposals and joyful family gatherings.",
            marriageHi = "विवाह योग्य जातकों के लिए उत्तम रिश्ते और शुभ वार्ता।",
            educationTa = "மாணவர்களுக்கு மிகவும் நல்ல முன்னேற்றம் மற்றும் சிறப்பிடம்.",
            educationEn = "Superb educational advancement and honors in studies.",
            educationHi = "विद्यार्थियों के लिए असाधारण प्रगति और उत्कृष्ट परिणाम।",
            healthTa = "உணவு முறையில் ஒழுங்கும், செரிமானத்தில் கவனமும் தேவை.",
            healthEn = "Maintain dietary discipline and attend to digestive wellness.",
            healthHi = "खान-पान में संयम रखें और पाचन स्वास्थ्य का ध्यान रखें।",
            familyTa = "குடும்பப் பேச்சில் பொறுமையும் நிதானமும் அமைதி தரும்.",
            familyEn = "Patience and mild words in family interactions ensure harmony.",
            familyHi = "परिवार में बातचीत के दौरान धैर्य और विनम्रता बनाए रखें।",
            primaryAdviceTa = "குடும்பம் மற்றும் பண விஷயங்களில் அவசரப்படாமல் செயல்படவும்.",
            primaryAdviceEn = "Avoid hasty moves in domestic financial matters.",
            primaryAdviceHi = "पारिवारिक और धन संबंधी निर्णयों में जल्दबाजी न करें।"
        ),

        // ♎ 7. துலாம் (Libra)
        RasiTransitPalan2026(
            rasi = Rasi.THULAM,
            severity = AlertSeverity.STANDARD_WATCH,
            alertTagTa = "🪐 சனி — 6-ஆம் இட வெற்றி & உழைப்பு",
            alertTagEn = "🪐 Saturn (Sani) — 6th House Victory & Hard Work",
            alertTagHi = "🪐 शनि — 6ठे भाव में सफलता एवं परिश्रम",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "🪐 சனி",
                    planetEn = "🪐 Saturn (Sani)",
                    planetHi = "🪐 शनि (Saturn)",
                    headlineTa = "சனி 6-ஆம் இடம் (சத்ரு ஜெய ஸ்தானம்)",
                    headlineEn = "Saturn in 6th House (Overcoming Hurdles)",
                    headlineHi = "6वें भाव में शनि (शत्रु हंता)",
                    detailsTa = "சனி 6-ஆம் இடத்தில் இருப்பதால் வேலைப்பளு அதிகரித்தாலும் எதிரிகளை சமாளிக்கும் திறன் கிடைக்கும். உடல் சோர்வில் மட்டும் கவனம்.",
                    detailsEn = "Saturn in the 6th house brings high work pressure but also grants stamina to overcome all rivals and debts.",
                    detailsHi = "6ठे भाव का शनि काम का दबाव बढ़ाएगा, पर सभी बाधाओं और विरोधियों पर विजय भी दिलाएगा।"
                )
            ),
            careerTa = "போட்டிகளை முறியடித்து தொழில் மற்றும் பணியில் முன்னேற்றம்.",
            careerEn = "Triumphing over competition to achieve career milestones.",
            careerHi = "प्रतिस्पर्धा में विजय और कार्यक्षेत्र में मजबूत पहचान।",
            financeTa = "படிப்படியாக நிதிநிலை மேம்படலாம்; கடன் குறையும்.",
            financeEn = "Steady financial appreciation; reduction of outstanding debts.",
            financeHi = "आर्थिक स्थिति सुदृढ़ होगी और कर्जों से मुक्ति मिलेगी।",
            marriageTa = "பொதுவாக நல்ல மற்றும் அமைதியான ஆண்டு.",
            marriageEn = "A pleasant, harmonious, and supportive marital year.",
            marriageHi = "दांपत्य जीवन में सुख, शांति और सामंजस्य बना रहेगा।",
            educationTa = "கவனச்சிதறலைத் தவிர்த்து இலக்கை நோக்கி படிக்கவும்.",
            educationEn = "Eliminate distractions and focus sharply on learning goals.",
            educationHi = "ध्यान भटकने से बचें और पढ़ाई पर पूरा मन लगाएं।",
            healthTa = "உணவு, தூக்கம் மற்றும் ஓய்வில் உரிய கவனம் தேவை.",
            healthEn = "Prioritize timely meals, sleep hygiene, and adequate rest.",
            healthHi = "समय पर भोजन और पर्याप्त नींद का विशेष ध्यान रखें।",
            familyTa = "அனைவரிடமும் அமைதியான அணுகுமுறை குடும்பத்தில் நலம் பயக்கும்.",
            familyEn = "A calm and accommodating demeanor fosters warm family bonds.",
            familyHi = "शांत और सकारात्मक व्यवहार से घर में सौहार्द बना रहेगा।",
            primaryAdviceTa = "வேலைப்பளுவால் உடல்நலத்தை பாதிக்க விட வேண்டாம்.",
            primaryAdviceEn = "Do not let intense workload compromise your health.",
            primaryAdviceHi = "काम के अत्यधिक दबाव को अपने स्वास्थ्य पर हावी न होने दें।"
        ),

        // ♏ 8. விருச்சிகம் (Scorpio)
        RasiTransitPalan2026(
            rasi = Rasi.VIRUCHIGAM,
            severity = AlertSeverity.PERIOD_ALERT,
            alertTagTa = "🌟 குரு நிலைமாற்றம் — காலப்பகுதி பலன்",
            alertTagEn = "🌟 Guru Transit — Periodic Transition",
            alertTagHi = "🌟 गुरु गोचर — समयानुसार फल",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "🌟 குரு & கிரக நிலைகள்",
                    planetEn = "🌟 Jupiter & Transits",
                    planetHi = "🌟 गुरु एवं ग्रह स्थिति",
                    headlineTa = "குறிப்பிடத்தக்க பெரிய எச்சரிக்கை இல்லை",
                    headlineEn = "No Major Negative Alert",
                    headlineHi = "कोई बड़ा अनिष्टकारी अलर्ट नहीं",
                    detailsTa = "குறிப்பிடத்தக்க பெரிய எச்சரிக்கை இல்லை. 2026-ல் குருவின் நிலைமாற்றங்களைப் பொறுத்து ஆண்டின் முதல் மற்றும் இரண்டாம் பகுதிகளில் பலன்கள் மாறுபடும்.",
                    detailsEn = "No severe planetary afflictions. Outcomes vary across the 1st and 2nd halves of 2026 based on Jupiter's transit to Cancer & Leo.",
                    detailsHi = "कोई बड़ा संकट नहीं। 2026 में गुरु के गोचर बदलाव के कारण वर्ष के पूर्वार्ध और उत्तरार्ध में परिणाम बदलेंगे।"
                )
            ),
            careerTa = "முயற்சிக்கு பிறகு நல்ல பலன் மற்றும் பாராட்டுக்கள்.",
            careerEn = "Rewarding outcomes and professional recognition after dedicated efforts.",
            careerHi = "परिश्रम के बाद उत्तम परिणाम और पद-प्रतिष्ठा में वृद्धि।",
            financeTa = "பணத்தை திட்டமிட்டு பயன்படுத்தவும்; சேமிப்பு பெருகும்.",
            financeEn = "Prudent budgeting ensures steady wealth accumulation.",
            financeHi = "योजनाबद्ध तरीक़े से ख़र्च करें; बचत में वृद्धि होगी।",
            marriageTa = "திருமணத்திற்கு நல்ல வாய்ப்புகள் மற்றும் சுப காரியங்கள்.",
            marriageEn = "Favorable marital proposals and joyous family milestones.",
            marriageHi = "विवाह के शुभ योग और मांगलिक कार्यों की रूपरेखा बनेगी।",
            educationTa = "தொடர்ந்து சீரான முயற்சி வெற்றியைத் தரும்.",
            educationEn = "Consistent discipline leads to academic success.",
            educationHi = "लगातार अभ्यास से परीक्षा में उत्तम अंक प्राप्त होंगे।",
            healthTa = "பொதுவான உடல் பராமரிப்பும், உடற்பயிற்சியும் அவசியம்.",
            healthEn = "Routine wellness care, hydration, and regular exercise.",
            healthHi = "सामान्य स्वास्थ्य रक्षा और नियमित व्यायाम आवश्यक है।",
            familyTa = "குடும்ப ஆதரவு முழுமையாக கிடைக்கும்; மகிழ்ச்சி நிறையும்.",
            familyEn = "Heartfelt support from family members; peaceful home life.",
            familyHi = "परिवार का भरपूर स्नेह और समर्थन प्राप्त होगा।",
            primaryAdviceTa = "அவசர முடிவுகளைத் தவிர்த்து நிதானமாக திட்டமிடவும்.",
            primaryAdviceEn = "Avoid impulsive leaps; plan every venture with calm foresight.",
            primaryAdviceHi = "जल्दबाजी के निर्णयों से बचें और सोच-समझकर कदम उठाएं।"
        ),

        // ♐ 9. தனுசு (Sagittarius)
        RasiTransitPalan2026(
            rasi = Rasi.DHANUSU,
            severity = AlertSeverity.PERIOD_ALERT,
            alertTagTa = "🟠 குரு — ஜூன் 2 முதல் அஷ்டம குரு கவனம்",
            alertTagEn = "🟠 Guru — 8th House Transit from June 2",
            alertTagHi = "🟠 गुरु — 2 जून से 8वें भाव में सावधानी",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "🌟 குரு",
                    planetEn = "🌟 Guru (Jupiter)",
                    planetHi = "🌟 गुरु (Jupiter)",
                    headlineTa = "ஜூன் 2 முதல் குரு கடகத்தில் (அஷ்டம குரு)",
                    headlineEn = "Jupiter in Cancer from June 2 (8th House)",
                    headlineHi = "2 जून से कर्क में गुरु (अष्टम गुरु)",
                    detailsTa = "ஜூன் 2 முதல் குரு கடகத்தில் செல்வதால், தனுசுக்கு 8-ஆம் இட குரு நிலை ஏற்படுகிறது. முக்கிய முடிவுகள், நிதி மற்றும் உடல்நலத்தில் கூடுதல் கவனம் நல்லது.",
                    detailsEn = "From June 2, Jupiter enters Cancer (8th house for Sagittarius). Exercise extra caution in major life choices, investments, and health.",
                    detailsHi = "2 जून से गुरु के कर्क राशि में जाने से 8वां गुरु रहेगा। महत्वपूर्ण निर्णयों, वित्त और स्वास्थ्य में अतिरिक्त सावधानी बरतें।"
                )
            ),
            careerTa = "பொறுமையுடன் செயல்பட வேண்டும்; அவசர மாற்றங்கள் வேண்டாம்.",
            careerEn = "Work patiently and methodically; avoid impulsive job shifts.",
            careerHi = "धैर्यपूर्वक कार्य करें; जल्दबाजी में नौकरी या काम न बदलें।",
            financeTa = "தேவையற்ற ஆடம்பர செலவுகளைத் தவிர்ப்பது நல்லது.",
            financeEn = "Eliminate frivolous expenses and keep emergency reserves.",
            financeHi = "अनावश्यक ख़र्चों पर लगाम लगाएं और बचत सुरक्षित रखें।",
            marriageTa = "ஆண்டின் முதல் பாதியில் சாதகமான வாய்ப்புகள் மற்றும் சுப நிகழ்வுகள்.",
            marriageEn = "First half of the year is highly favorable for marriage alliances.",
            marriageHi = "वर्ष का पूर्वार्ध विवाह और रिश्तों के लिए अत्यंत अनुकूल है।",
            educationTa = "விடாமுயற்சிக்கு நிச்சயம் நல்ல பலன் கிடைக்கும்.",
            educationEn = "Sustained focus ensures commendable exam outcomes.",
            educationHi = "निरंतर मेहनत से शैक्षणिक सफलता हासिल होगी।",
            healthTa = "ஆண்டின் இரண்டாம் பகுதியில் உடல்நலத்தில் கூடுதல் கவனம் தேவை.",
            healthEn = "Take extra care of diet, digestion, and stamina in the 2nd half.",
            healthHi = "वर्ष के उत्तरार्ध में स्वास्थ्य का विशेष ध्यान रखें।",
            familyTa = "குடும்ப விவகாரங்களை அமைதியாகவும் பக்குவமாகவும் அணுகவும்.",
            familyEn = "Handle domestic matters with diplomatic understanding.",
            familyHi = "पारिवारिक मुद्दों को शांति और सूझबूझ से सुलझाएं।",
            primaryAdviceTa = "ஜூன் முதல் முக்கிய நிதி மற்றும் வாழ்க்கை முடிவுகளில் நிதானம்.",
            primaryAdviceEn = "Exercise great discretion in major decisions from June onwards.",
            primaryAdviceHi = "जून के बाद बड़े वित्तीय और व्यक्तिगत निर्णयों में सतर्क रहें।"
        ),

        // ♑ 10. மகரம் (Capricorn)
        RasiTransitPalan2026(
            rasi = Rasi.MAGARAM,
            severity = AlertSeverity.HIGH_ALERT,
            alertTagTa = "☊ ராகு / 🪐 சனி — மிக முக்கிய கவனம்",
            alertTagEn = "☊ Rahu / 🪐 Saturn — Dual Transit Alert",
            alertTagHi = "☊ राहु / 🪐 शनि — दोहरा ग्रह अलर्ट",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "☊ ராகு",
                    planetEn = "☊ Rahu",
                    planetHi = "☊ राहु (Rahu)",
                    headlineTa = "ராகு 2-ஆம் இடம் & டிசம்பரில் ஜென்ம ராகு",
                    headlineEn = "Rahu in 2nd House & entering 1st House in Dec",
                    headlineHi = "2रे भाव में राहु एवं दिसंबर में 1ले भाव में",
                    detailsTa = "ஆண்டின் பெரும்பகுதியில் ராகு 2-ஆம் இடத்தில் இருப்பதால் பணம், குடும்பம் மற்றும் பேச்சில் கவனம் தேவை. டிசம்பரில் ராகு மகரத்திற்கு வருவதால் தனிப்பட்ட முடிவுகளில் நிதானம் அவசியம்.",
                    detailsEn = "Rahu in the 2nd house mandates care in financial dealings and speech. In December, Rahu transits into Capricorn, requiring mental calm.",
                    detailsHi = "अधिकांश वर्ष राहु 2रे भाव में धन व वाणी में सावधानी मांगता है। दिसंबर में मकर में आने पर निर्णयों में संयम रखें।"
                ),
                PlanetTransitAlert(
                    planetTa = "🪐 சனி",
                    planetEn = "🪐 Saturn (Sani)",
                    planetHi = "🪐 शनि (Saturn)",
                    headlineTa = "சனி 3-ஆம் இடம் (தைரிய ஸ்தானம்)",
                    headlineEn = "Saturn in 3rd House (Effort & Courage)",
                    headlineHi = "3रे भाव में शनि (पराक्रम स्थान)",
                    detailsTa = "சனி 3-ஆம் இடத்தில் இருப்பதால் முயற்சி அதிகம் தேவைப்படும். பலன்கள் தாமதமாக வந்தாலும் உழைப்பால் முன்னேற்றம் பெறலாம்.",
                    detailsEn = "Saturn in the 3rd house demands tireless initiative. Success may arrive with delay, but industrious work brings lasting progress.",
                    detailsHi = "3रे भाव में शनि अधिक परिश्रम की मांग करता है। परिणाम भले धीरे मिलें, मेहनत से उन्नति अवश्य होगी।"
                )
            ),
            careerTa = "கடின உழைப்பால் மட்டுமே முன்னேற்றம் சாத்தியமாகும்.",
            careerEn = "Career triumphs achieved through perseverance and fortitude.",
            careerHi = "कठिन परिश्रम और लगन से ही कार्यक्षेत्र में सफलता मिलेगी।",
            financeTa = "சேமிப்பில் தீவிர கவனம்; வாக்குறுதிகளில் நிதானம்.",
            financeEn = "Sharp focus on saving; be cautious with monetary commitments.",
            financeHi = "बचत पर विशेष ध्यान दें; पैसों के लेन-देन में सतर्क रहें।",
            marriageTa = "ஆண்டின் இரண்டாம் பாதியில் நல்ல வாய்ப்புகள் அமையும்.",
            marriageEn = "Prospective marital alliances flourish in the second half of 2026.",
            marriageHi = "वर्ष के उत्तरार्ध में विवाह के उत्तम प्रस्ताव प्राप्त होंगे।",
            educationTa = "விடாமுயற்சி நிச்சயம் வெற்றியைத் தரும்.",
            educationEn = "Disciplined preparation ensures scholastic victories.",
            educationHi = "एकाग्रता और नियमित प्रयास से उत्तम सफलता मिलेगी।",
            healthTa = "அலைச்சல் மற்றும் உடல் சோர்வை உடனே கவனிக்கவும்.",
            healthEn = "Heed physical exhaustion; do not delay health checkups.",
            healthHi = "शारीरिक थकान और तनाव को तुरंत दूर करने का प्रयास करें।",
            familyTa = "குடும்பத்தில் பேச்சில் நிதானம்; வாக்குவாதங்களைத் தவிர்க்கவும்.",
            familyEn = "Speak gently with family members and avert disputes.",
            familyHi = "परिवार में कटु वाणी से बचें और मधुरता बनाए रखें।",
            primaryAdviceTa = "பணம், குடும்பம் மற்றும் பேச்சில் அதிக நிதானம் அவசியம்.",
            primaryAdviceEn = "Prudence in financial management and family interactions is paramount.",
            primaryAdviceHi = "धन, वाणी और पारिवारिक मामलों में अत्यधिक संयम रखें।"
        ),

        // ♒ 11. கும்பம் (Aquarius)
        RasiTransitPalan2026(
            rasi = Rasi.KUMBAM,
            severity = AlertSeverity.HIGH_ALERT,
            alertTagTa = "🪐 சனி / ☋ கேது — மிக முக்கிய கவனம் (பாத சனி)",
            alertTagEn = "🪐 Saturn / ☋ Ketu — Critical Transit Alert (Patha Sani)",
            alertTagHi = "🪐 शनि / ☋ केतु — अति महत्वपूर्ण सावधानी (पाद शनि)",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "🪐 சனி",
                    planetEn = "🪐 Saturn (Sani)",
                    planetHi = "🪐 शनि (Saturn)",
                    headlineTa = "சனி கும்பத்திற்கு 2-ஆம் இடம் (பாத சனி காலம்)",
                    headlineEn = "Saturn in 2nd House (Patha Sani)",
                    headlineHi = "2रे भाव में शनि (पाद शनि)",
                    detailsTa = "சனி கும்பத்திற்கு 2-ஆம் இடத்தில் இருப்பதால் இது ஏழரைச் சனியின் இறுதிப் பகுதி (பாத சனி). பணம், குடும்பம், பேச்சு மற்றும் உடல்நலத்தில் கட்டுப்பாடு தேவை.",
                    detailsEn = "Saturn transiting the 2nd house marks the final phase of Sade Sati (Patha Sani). Strict discipline is needed in finances, speech, and health.",
                    detailsHi = "2रे भाव में शनि साढ़ेसाती का अंतिम चरण (पाद शनि) है। धन, परिवार, वाणी और स्वास्थ्य पर पूर्ण नियंत्रण रखें।"
                ),
                PlanetTransitAlert(
                    planetTa = "☋ கேது",
                    planetEn = "☋ Ketu",
                    planetHi = "☋ केतु (Ketu)",
                    headlineTa = "கேது 7-ஆம் இடம் (களத்திர ஸ்தானம்)",
                    headlineEn = "Ketu in 7th House (Partnerships)",
                    headlineHi = "7वें भाव में केतु",
                    detailsTa = "ஆண்டின் பெரும்பகுதியில் கேது 7-ஆம் இடத்தில் இருப்பதால் திருமணம் மற்றும் கூட்டுத் தொடர்புகளில் புரிதல் அவசியம்.",
                    detailsEn = "Ketu in the 7th house calls for patience and mutual compromise in marriage and business partnerships.",
                    detailsHi = "7वें भाव में केतु वैवाहिक जीवन और व्यावसायिक साझेदारी में आपसी तालमेल की मांग करता है।"
                )
            ),
            careerTa = "பொறுமையுடன் செயல்பட்டால் படிப்படியான முன்னேற்றம்.",
            careerEn = "Methodical and patient execution guarantees steady professional growth.",
            careerHi = "धैर्य और अनुशासन के साथ काम करने पर धीरे-धीरे प्रगति होगी।",
            financeTa = "சேமிப்பு மிகவும் முக்கியம்; அவசர செலவுகளைத் தவிர்க்கவும்.",
            financeEn = "Financial savings are vital; avoid sudden or luxury spending.",
            financeHi = "बचत अत्यंत आवश्यक है; अनावश्यक ख़र्चों पर कड़ा अंकुश लगाएं।",
            marriageTa = "உறவில் பொறுமை மற்றும் பரஸ்பர புரிதல் தேவை.",
            marriageEn = "Patience and understanding are necessary to overcome minor friction.",
            marriageHi = "दांपत्य संबंधों में धैर्य, सम्मान और समझदारी जरूरी है।",
            educationTa = "மாணவர்களுக்கு நல்ல வாய்ப்புகள் மற்றும் தேர்வு வெற்றி.",
            educationEn = "Promising educational prospects with sustained academic effort.",
            educationHi = "विद्यार्थियों के लिए अच्छे अवसर और परीक्षाओं में सफलता।",
            healthTa = "உணவு மற்றும் வாழ்க்கை முறையில் சீரான ஒழுக்கம் தேவை.",
            healthEn = "Strict adherence to wholesome diet and balanced daily routines.",
            healthHi = "खान-पान और जीवनशैली में नियमितता और अनुशासन रखें।",
            familyTa = "வார்த்தைகளில் கட்டுப்பாடு; குடும்ப ஒற்றுமைக்கு முதலிடம்.",
            familyEn = "Restrain words in heated moments; prioritize domestic harmony.",
            familyHi = "बातचीत में संयम रखें और पारिवारिक एकता को सर्वोपरि मानें।",
            primaryAdviceTa = "பணம், குடும்பம், திருமண உறவு ஆகியவற்றில் கூடுதல் கவனம் செலுத்துங்கள்.",
            primaryAdviceEn = "Give utmost attention to wealth, speech, and marital harmony.",
            primaryAdviceHi = "धन, वाणी, परिवार और दांपत्य जीवन में विशेष सावधानी बरतें।"
        ),

        // ♓ 12. மீனம் (Pisces)
        RasiTransitPalan2026(
            rasi = Rasi.MEENAM,
            severity = AlertSeverity.HIGH_ALERT,
            alertTagTa = "🪐 சனி / ☋ கேது — மிக முக்கிய கவனம் (ஜென்ம சனி)",
            alertTagEn = "🪐 Saturn / ☋ Ketu — Critical Transit Alert (Jenma Sani)",
            alertTagHi = "🪐 शनि / ☋ केतु — अति महत्वपूर्ण सावधानी (जन्म शनि)",
            keyPlanets = listOf(
                PlanetTransitAlert(
                    planetTa = "🪐 சனி",
                    planetEn = "🪐 Saturn (Sani)",
                    planetHi = "🪐 शनि (Saturn)",
                    headlineTa = "சனி மீனத்தில் (ஜென்ம சனி - 1-ஆம் இடம்)",
                    headlineEn = "Saturn in Pisces (Jenma Sani - 1st House)",
                    headlineHi = "मीन राशि में शनि (जन्म शनि - 1ला भाव)",
                    detailsTa = "சனி மீனத்தில் இருப்பதால் இது சனியின் 1-ஆம் இடப் பயணம் (ஜென்ம சனி - ஏழரைச் சனியின் மையக்கட்டம்). உடல்நலம், மன அழுத்தம், பொறுப்புகள் மற்றும் வாழ்க்கைத் திட்டங்களில் அதிக கவனம் தேவை. சனி மீனத்தில் 2026 முழுவதும் நிலைகொள்கிறது.",
                    detailsEn = "Saturn transiting Pisces is Jenma Sani (core Sade Sati). Heightened care is needed for mental peace, physical health, and major commitments throughout 2026.",
                    detailsHi = "मीन में शनि का गोचर जन्म शनि (साढ़ेसाती का मध्य चरण) है। पूरे 2026 में स्वास्थ्य, मानसिक शांति और जिम्मेदारियों में अत्यंत सावधानी रखें।"
                ),
                PlanetTransitAlert(
                    planetTa = "☋ கேது",
                    planetEn = "☋ Ketu",
                    planetHi = "☋ केतु (Ketu)",
                    headlineTa = "கேது 6-ஆம் இடம் (சத்ரு நாசம்)",
                    headlineEn = "Ketu in 6th House (Overcoming Difficulties)",
                    headlineHi = "6वें भाव में केतु",
                    detailsTa = "ஆண்டின் பெரும்பகுதியில் கேது 6-ஆம் இடத்தில் இருப்பதால் உடல்நலம் மற்றும் அன்றாட பொறுப்புகளில் ஒழுங்கு அவசியம்.",
                    detailsEn = "Ketu in the 6th house helps neutralize rivals, though strict discipline in daily routine is essential.",
                    detailsHi = "6ठे भाव में केतु शत्रुओं का शमन करता है, परंतु दिनचर्या में अनुशासन बनाए रखें।"
                )
            ),
            careerTa = "கடின உழைப்புக்குப் பிறகே நற்பலன்; பொறுப்புகளை கவனமாக கையாளவும்.",
            careerEn = "Gains achieved after diligent work; handle workplace tasks conscientiously.",
            careerHi = "कड़ी मेहनत के बाद ही अच्छे परिणाम मिलेंगे; कार्यों को सावधानी से करें।",
            financeTa = "திட்டமிட்டு செலவு செய்யவும்; ஆடம்பர முதலீடுகளைத் தவிர்க்கவும்.",
            financeEn = "Spend wisely according to budget; avoid speculative investments.",
            financeHi = "योजना बनाकर ख़र्च करें; जोखिम भरे निवेश से बचें।",
            marriageTa = "பொறுமை, விட்டுக்கொடுத்தல் மற்றும் புரிதல் முக்கியம்.",
            marriageEn = "Patience, tolerance, and empathy are crucial in relationships.",
            marriageHi = "आपसी समझ, धैर्य और त्याग की भावना संबंध बनाए रखेगी।",
            educationTa = "நல்ல முயற்சியால் சிறந்த மற்றும் திருப்திகரமான முடிவுகள்.",
            educationEn = "Earnest efforts will bring satisfying educational achievements.",
            educationHi = "गंभीर अध्ययन से संतोषजनक और उत्तम परिणाम मिलेंगे।",
            healthTa = "உடல்நலம் மற்றும் மன அமைதிக்கு கூடுதல் கவனம் தேவை.",
            healthEn = "Dedicated attention required for physical wellness and mental tranquility.",
            healthHi = "स्वास्थ्य और मानसिक शांति पर विशेष ध्यान देने की आवश्यकता है।",
            familyTa = "குடும்ப விவகாரங்களை பொறுமையாகவும் மென்மையாகவும் அணுகவும்.",
            familyEn = "Handle family issues with gentle patience and understanding.",
            familyHi = "पारिवारिक मामलों को धैर्य और कोमलता से सुलझाएं।",
            primaryAdviceTa = "உடல்நலம் மற்றும் மன அமைதியை எப்போதும் முதன்மைப்படுத்தவும்; திருக்கோயில் நவகிரக வழிபாடு சிறந்தது.",
            primaryAdviceEn = "Prioritize physical health and inner peace above all; regular temple worship brings relief.",
            primaryAdviceHi = "स्वास्थ्य और मानसिक शांति को प्राथमिकता दें; मंदिर में नवग्रह पूजा से शांति मिलेगी।"
        )
    )

    fun getPalanForRasi(rasi: Rasi): RasiTransitPalan2026 {
        return rasiPalan2026List.firstOrNull { it.rasi == rasi } ?: rasiPalan2026List.first()
    }
}
