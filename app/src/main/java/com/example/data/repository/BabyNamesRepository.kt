package com.example.data.repository

import com.example.data.model.NakshatraBabyLetters
import com.example.data.model.PadaLetterInfo
import com.example.data.model.Rasi

class BabyNamesRepository {

    val allNakshatraLetters: List<NakshatraBabyLetters> = listOf(
        NakshatraBabyLetters(
            nakshatraIndex = 1,
            nakshatraNameTa = "அஸ்வினி",
            nakshatraNameEn = "Ashwini",
            nakshatraNameHi = "अश्विनी",
            deityTa = "அஸ்வினி தேவர்கள்",
            deityEn = "Ashwini Kumaras",
            deityHi = "अश्विनी कुमार",
            lordTa = "கேது",
            lordEn = "Ketu",
            lordHi = "केतु",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "குதிரை (Horse)",
            yoniEn = "Horse",
            yoniHi = "घोड़ा",
            rajjuTa = "பாத ரஜ்ஜு",
            rajjuEn = "Pada (Foot) Rajju",
            rajjuHi = "पाद रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "சு", "Chu / Su", "चु", Rasi.MESHAM),
                PadaLetterInfo(2, "சே", "Che / Se", "चे", Rasi.MESHAM),
                PadaLetterInfo(3, "சோ", "Cho / So", "चो", Rasi.MESHAM),
                PadaLetterInfo(4, "லா", "La", "ला", Rasi.MESHAM)
            ),
            allLettersSummaryTa = "சு, சே, சோ, லா",
            allLettersSummaryEn = "Chu, Che, Cho, La",
            allLettersSummaryHi = "चु, चे, चो, ला"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 2,
            nakshatraNameTa = "பரணி",
            nakshatraNameEn = "Bharani",
            nakshatraNameHi = "भरणी",
            deityTa = "எமதர்மன்",
            deityEn = "Yama",
            deityHi = "यम",
            lordTa = "சுக்கிரன்",
            lordEn = "Venus",
            lordHi = "शुक्र",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "யானை (Elephant)",
            yoniEn = "Elephant",
            yoniHi = "हाथी",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
            rajjuEn = "Uru (Thigh) Rajju",
            rajjuHi = "ऊरु (जंघा) रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "லீ", "Lee / Li", "ली", Rasi.MESHAM),
                PadaLetterInfo(2, "லூ", "Loo / Lu", "लू", Rasi.MESHAM),
                PadaLetterInfo(3, "லே", "Le / Lay", "ले", Rasi.MESHAM),
                PadaLetterInfo(4, "லோ", "Lo / Low", "लो", Rasi.MESHAM)
            ),
            allLettersSummaryTa = "லீ, லூ, லே, லோ",
            allLettersSummaryEn = "Lee, Loo, Le, Lo",
            allLettersSummaryHi = "ली, लू, ले, लो"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 3,
            nakshatraNameTa = "கார்த்திகை",
            nakshatraNameEn = "Krittika",
            nakshatraNameHi = "कृत्तिका",
            deityTa = "அக்னி தேவன் / முருகப்பெருமான்",
            deityEn = "Agni / Lord Murugan",
            deityHi = "अग्नि / भगवान मुरुगन",
            lordTa = "சூரியன்",
            lordEn = "Sun",
            lordHi = "सूर्य",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "ஆடு (Sheep)",
            yoniEn = "Sheep",
            yoniHi = "भेड़",
            rajjuTa = "உதர (நாபி) ரஜ்ஜு",
            rajjuEn = "Udhara (Navel) Rajju",
            rajjuHi = "उदर (नाभि) रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "அ", "A", "अ", Rasi.MESHAM),
                PadaLetterInfo(2, "இ", "I / Ee", "ई", Rasi.RISHABAM),
                PadaLetterInfo(3, "உ", "U / Oo", "उ", Rasi.RISHABAM),
                PadaLetterInfo(4, "ஏ", "E / Ae", "ए", Rasi.RISHABAM)
            ),
            allLettersSummaryTa = "அ, இ, உ, ஏ",
            allLettersSummaryEn = "A, I, U, E",
            allLettersSummaryHi = "अ, ई, उ, ए"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 4,
            nakshatraNameTa = "ரோகிணி",
            nakshatraNameEn = "Rohini",
            nakshatraNameHi = "रोहिणी",
            deityTa = "பிரம்ம தேவன்",
            deityEn = "Brahma",
            deityHi = "ब्रह्मा",
            lordTa = "சந்திரன்",
            lordEn = "Moon",
            lordHi = "चंद्र",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "பாம்பு (Serpent)",
            yoniEn = "Serpent",
            yoniHi = "सर्प",
            rajjuTa = "கண்ட ரஜ்ஜு",
            rajjuEn = "Kanda (Neck) Rajju",
            rajjuHi = "कंठ रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "ஓ", "O", "ओ", Rasi.RISHABAM),
                PadaLetterInfo(2, "வா", "Vaa", "वा", Rasi.RISHABAM),
                PadaLetterInfo(3, "வி", "Vee / Vi", "वी", Rasi.RISHABAM),
                PadaLetterInfo(4, "வு", "Vu / Woo", "वू", Rasi.RISHABAM)
            ),
            allLettersSummaryTa = "ஓ, வா, வி, வு",
            allLettersSummaryEn = "O, Vaa, Vi, Vu",
            allLettersSummaryHi = "ओ, वा, वी, वू"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 5,
            nakshatraNameTa = "மிருகசீரிஷம்",
            nakshatraNameEn = "Mrigashira",
            nakshatraNameHi = "मृगशिरा",
            deityTa = "சந்திர பகவான்",
            deityEn = "Chandra (Moon)",
            deityHi = "चंद्र देव",
            lordTa = "செவ்வாய்",
            lordEn = "Mars",
            lordHi = "मंगल",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "பாம்பு (Serpent)",
            yoniEn = "Serpent",
            yoniHi = "सर्प",
            rajjuTa = "சிரோ ரஜ்ஜு",
            rajjuEn = "Siro (Head) Rajju",
            rajjuHi = "शिरो रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "வே", "Ve / Way", "वे", Rasi.RISHABAM),
                PadaLetterInfo(2, "வோ", "Vo / Woh", "वो", Rasi.RISHABAM),
                PadaLetterInfo(3, "கா", "Kaa", "का", Rasi.MITHUNAM),
                PadaLetterInfo(4, "கீ", "Kee / Ki", "की", Rasi.MITHUNAM)
            ),
            allLettersSummaryTa = "வே, வோ, கா, கீ",
            allLettersSummaryEn = "Ve, Vo, Kaa, Kee",
            allLettersSummaryHi = "वे, वो, का, की"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 6,
            nakshatraNameTa = "திருவாதிரை",
            nakshatraNameEn = "Arudra",
            nakshatraNameHi = "आर्द्रा",
            deityTa = "ருத்ரன் (சிவபெருமான்)",
            deityEn = "Rudra (Lord Shiva)",
            deityHi = "रुद्र (भगवान शिव)",
            lordTa = "ராகு",
            lordEn = "Rahu",
            lordHi = "राहु",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "நாய் (Dog)",
            yoniEn = "Dog",
            yoniHi = "कुत्ता",
            rajjuTa = "சிரோ ரஜ்ஜு",
            rajjuEn = "Siro (Head) Rajju",
            rajjuHi = "शिरो रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "கு", "Ku / Koo", "कु", Rasi.MITHUNAM),
                PadaLetterInfo(2, "க", "Gha / Kha", "घ", Rasi.MITHUNAM),
                PadaLetterInfo(3, "ங", "Inga / Nga", "ङ", Rasi.MITHUNAM),
                PadaLetterInfo(4, "ச", "Cha / Chha", "छ", Rasi.MITHUNAM)
            ),
            allLettersSummaryTa = "கு, க, ங, ச",
            allLettersSummaryEn = "Ku, Gha, Nga, Cha",
            allLettersSummaryHi = "कु, घ, ङ, छ"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 7,
            nakshatraNameTa = "புனர்பூசம்",
            nakshatraNameEn = "Punarvasu",
            nakshatraNameHi = "पुनर्वसु",
            deityTa = "அதிதி (தேவ மாதா)",
            deityEn = "Aditi",
            deityHi = "अदिति",
            lordTa = "குரு",
            lordEn = "Jupiter",
            lordHi = "बृहस्पति",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "பூனை (Cat)",
            yoniEn = "Cat",
            yoniHi = "बिल्ली",
            rajjuTa = "கண்ட ரஜ்ஜு",
            rajjuEn = "Kanda (Neck) Rajju",
            rajjuHi = "कंठ रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "கே", "Ke / Kay", "के", Rasi.MITHUNAM),
                PadaLetterInfo(2, "கோ", "Ko / Koh", "को", Rasi.MITHUNAM),
                PadaLetterInfo(3, "ஹா", "Haa", "हा", Rasi.MITHUNAM),
                PadaLetterInfo(4, "ஹீ", "Hee / Hi", "ही", Rasi.KADAGAM)
            ),
            allLettersSummaryTa = "கே, கோ, ஹா, ஹீ",
            allLettersSummaryEn = "Ke, Ko, Haa, Hee",
            allLettersSummaryHi = "के, को, हा, ही"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 8,
            nakshatraNameTa = "பூசம்",
            nakshatraNameEn = "Pushya",
            nakshatraNameHi = "पुष्य",
            deityTa = "பிரகஸ்பதி (குரு)",
            deityEn = "Brihaspati",
            deityHi = "बृहस्पति देव",
            lordTa = "சனி",
            lordEn = "Saturn",
            lordHi = "शनि",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "ஆடு (Sheep)",
            yoniEn = "Sheep",
            yoniHi = "भेड़",
            rajjuTa = "உதர ரஜ்ஜு",
            rajjuEn = "Udhara Rajju",
            rajjuHi = "उदर रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "ஹு", "Hu / Hoo", "हु", Rasi.KADAGAM),
                PadaLetterInfo(2, "ஹே", "He / Hay", "हे", Rasi.KADAGAM),
                PadaLetterInfo(3, "ஹோ", "Ho / Hoh", "हो", Rasi.KADAGAM),
                PadaLetterInfo(4, "டா", "Daa", "डा", Rasi.KADAGAM)
            ),
            allLettersSummaryTa = "ஹு, ஹே, ஹோ, டா",
            allLettersSummaryEn = "Hu, He, Ho, Daa",
            allLettersSummaryHi = "हु, हे, हो, डा"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 9,
            nakshatraNameTa = "ஆயில்யம்",
            nakshatraNameEn = "Ashlesha",
            nakshatraNameHi = "आश्लेषा",
            deityTa = "நாகராஜன் / ஆதிசேஷன்",
            deityEn = "Nagaraja (Serpent King)",
            deityHi = "नागराज",
            lordTa = "புதன்",
            lordEn = "Mercury",
            lordHi = "बुध",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "பூனை (Cat)",
            yoniEn = "Cat",
            yoniHi = "बिल्ली",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
            rajjuEn = "Uru (Thigh) Rajju",
            rajjuHi = "ऊरु (जंघा) रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "டீ", "Dee / Di", "डी", Rasi.KADAGAM),
                PadaLetterInfo(2, "டூ", "Doo / Du", "डू", Rasi.KADAGAM),
                PadaLetterInfo(3, "டே", "De / Day", "डे", Rasi.KADAGAM),
                PadaLetterInfo(4, "டோ", "Do / Doh", "डो", Rasi.KADAGAM)
            ),
            allLettersSummaryTa = "டீ, டூ, டே, டோ",
            allLettersSummaryEn = "Dee, Doo, De, Do",
            allLettersSummaryHi = "डी, डू, डे, डो"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 10,
            nakshatraNameTa = "மகம்",
            nakshatraNameEn = "Magha",
            nakshatraNameHi = "मघा",
            deityTa = "பித்ருக்கள் (Ancestors)",
            deityEn = "Pitras (Ancestral deities)",
            deityHi = "पितृ देवता",
            lordTa = "கேது",
            lordEn = "Ketu",
            lordHi = "केतु",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "எலி (Rat)",
            yoniEn = "Rat",
            yoniHi = "चूहा",
            rajjuTa = "பாத ரஜ்ஜு",
            rajjuEn = "Pada (Foot) Rajju",
            rajjuHi = "पाद रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "மா", "Maa", "मा", Rasi.SIMHAM),
                PadaLetterInfo(2, "மீ", "Mee / Mi", "मी", Rasi.SIMHAM),
                PadaLetterInfo(3, "மூ", "Moo / Mu", "मू", Rasi.SIMHAM),
                PadaLetterInfo(4, "மே", "Me / May", "मे", Rasi.SIMHAM)
            ),
            allLettersSummaryTa = "மா, மீ, மூ, மே",
            allLettersSummaryEn = "Maa, Mee, Moo, Me",
            allLettersSummaryHi = "मा, मी, मू, मे"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 11,
            nakshatraNameTa = "பூரம்",
            nakshatraNameEn = "Purva Phalguni",
            nakshatraNameHi = "पूर्वाफाल्गुनी",
            deityTa = "பகன் (சூரிய வடிவம்)",
            deityEn = "Bhaga",
            deityHi = "भग",
            lordTa = "சுக்கிரன்",
            lordEn = "Venus",
            lordHi = "शुक्र",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "எலி (Rat)",
            yoniEn = "Rat",
            yoniHi = "चूहा",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
            rajjuEn = "Uru (Thigh) Rajju",
            rajjuHi = "ऊरु (जंघा) रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "மோ", "Mo / Moh", "मो", Rasi.SIMHAM),
                PadaLetterInfo(2, "டா", "Taa", "टा", Rasi.SIMHAM),
                PadaLetterInfo(3, "டீ", "Tee / Ti", "टी", Rasi.SIMHAM),
                PadaLetterInfo(4, "டூ", "Too / Tu", "टू", Rasi.SIMHAM)
            ),
            allLettersSummaryTa = "மோ, டா, டீ, டூ",
            allLettersSummaryEn = "Mo, Taa, Tee, Too",
            allLettersSummaryHi = "मो, टा, टी, टू"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 12,
            nakshatraNameTa = "உத்திரம்",
            nakshatraNameEn = "Uttara Phalguni",
            nakshatraNameHi = "उत्तराफाल्गुनी",
            deityTa = "ஆர்யமான்",
            deityEn = "Aryaman",
            deityHi = "अर्यमा",
            lordTa = "சூரியன்",
            lordEn = "Sun",
            lordHi = "सूर्य",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "பசு (Cow)",
            yoniEn = "Cow",
            yoniHi = "गाय",
            rajjuTa = "உதர ரஜ்ஜு",
            rajjuEn = "Udhara Rajju",
            rajjuHi = "उदर रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "டே", "Tay / Te", "टे", Rasi.SIMHAM),
                PadaLetterInfo(2, "டோ", "Toh / To", "टो", Rasi.KANNI),
                PadaLetterInfo(3, "பா", "Paa", "पा", Rasi.KANNI),
                PadaLetterInfo(4, "பீ", "Pee / Pi", "पी", Rasi.KANNI)
            ),
            allLettersSummaryTa = "டே, டோ, பா, பீ",
            allLettersSummaryEn = "Te, To, Paa, Pee",
            allLettersSummaryHi = "टे, टो, पा, पी"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 13,
            nakshatraNameTa = "அஸ்தம்",
            nakshatraNameEn = "Hasta",
            nakshatraNameHi = "हस्त",
            deityTa = "சூரிய பகவான் (சாவித்ரி)",
            deityEn = "Savitri (Sun god)",
            deityHi = "सवित्र (सूर्य देव)",
            lordTa = "சந்திரன்",
            lordEn = "Moon",
            lordHi = "चंद्र",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "எருமை (Buffalo)",
            yoniEn = "Buffalo",
            yoniHi = "भैंस",
            rajjuTa = "கண்ட ரஜ்ஜு",
            rajjuEn = "Kanda (Neck) Rajju",
            rajjuHi = "कंठ रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "பூ", "Poo / Pu", "पू", Rasi.KANNI),
                PadaLetterInfo(2, "ஷ", "Sha", "ष", Rasi.KANNI),
                PadaLetterInfo(3, "ண", "Na / Nna", "ण", Rasi.KANNI),
                PadaLetterInfo(4, "ட", "Tha / Dha", "ठ", Rasi.KANNI)
            ),
            allLettersSummaryTa = "பூ, ஷ, ண, ட",
            allLettersSummaryEn = "Poo, Sha, Na, Tha",
            allLettersSummaryHi = "पू, ष, ण, ठ"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 14,
            nakshatraNameTa = "சித்திரை",
            nakshatraNameEn = "Chitra",
            nakshatraNameHi = "चित्रा",
            deityTa = "விஸ்வகர்மா (தெய்வீக சிற்பி)",
            deityEn = "Vishwakarma",
            deityHi = "विश्वकर्मा",
            lordTa = "செவ்வாய்",
            lordEn = "Mars",
            lordHi = "मंगल",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "புலி (Tiger)",
            yoniEn = "Tiger",
            yoniHi = "बाघ",
            rajjuTa = "சிரோ ரஜ்ஜு",
            rajjuEn = "Siro (Head) Rajju",
            rajjuHi = "शिरो रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "பே", "Pe / Pay", "पे", Rasi.KANNI),
                PadaLetterInfo(2, "போ", "Po / Poh", "पो", Rasi.KANNI),
                PadaLetterInfo(3, "ரா", "Raa", "रा", Rasi.THULAM),
                PadaLetterInfo(4, "ரீ", "Ree / Ri", "री", Rasi.THULAM)
            ),
            allLettersSummaryTa = "பே, போ, ரா, ரீ",
            allLettersSummaryEn = "Pe, Po, Raa, Ree",
            allLettersSummaryHi = "पे, पो, रा, री"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 15,
            nakshatraNameTa = "சுவாதி",
            nakshatraNameEn = "Swati",
            nakshatraNameHi = "स्वाती",
            deityTa = "வாயு பகவான்",
            deityEn = "Vayu (Wind god)",
            deityHi = "वायु देव",
            lordTa = "ராகு",
            lordEn = "Rahu",
            lordHi = "राहु",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "எருமை (Buffalo)",
            yoniEn = "Buffalo",
            yoniHi = "भैंस",
            rajjuTa = "சிரோ ரஜ்ஜு",
            rajjuEn = "Siro (Head) Rajju",
            rajjuHi = "शिरो रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "ரூ", "Roo / Ru", "रू", Rasi.THULAM),
                PadaLetterInfo(2, "ரே", "Re / Ray", "रे", Rasi.THULAM),
                PadaLetterInfo(3, "ரோ", "Ro / Roh", "रो", Rasi.THULAM),
                PadaLetterInfo(4, "தா", "Thaa / Ta", "ता", Rasi.THULAM)
            ),
            allLettersSummaryTa = "ரூ, ரே, ரோ, தா",
            allLettersSummaryEn = "Roo, Re, Ro, Thaa",
            allLettersSummaryHi = "रू, रे, रो, ता"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 16,
            nakshatraNameTa = "விசாகம்",
            nakshatraNameEn = "Vishakha",
            nakshatraNameHi = "विशाखा",
            deityTa = "இந்திராக்னி (இந்திரன் & அக்னி)",
            deityEn = "Indragni",
            deityHi = "इन्द्राग्नि",
            lordTa = "குரு",
            lordEn = "Jupiter",
            lordHi = "बृहस्पति",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "புலி (Tiger)",
            yoniEn = "Tiger",
            yoniHi = "बाघ",
            rajjuTa = "கண்ட ரஜ்ஜு",
            rajjuEn = "Kanda (Neck) Rajju",
            rajjuHi = "कंठ रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "தீ", "Thee / Ti", "ती", Rasi.THULAM),
                PadaLetterInfo(2, "தூ", "Thoo / Tu", "तू", Rasi.THULAM),
                PadaLetterInfo(3, "தே", "They / Te", "ते", Rasi.THULAM),
                PadaLetterInfo(4, "தோ", "Tho / Toh", "तो", Rasi.VIRUCHIGAM)
            ),
            allLettersSummaryTa = "தீ, தூ, தே, தோ",
            allLettersSummaryEn = "Thee, Thoo, They, Tho",
            allLettersSummaryHi = "ती, तू, ते, तो"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 17,
            nakshatraNameTa = "அனுஷம்",
            nakshatraNameEn = "Anuradha",
            nakshatraNameHi = "अनुराधा",
            deityTa = "மித்ரன் (நட்பு & சூரியன்)",
            deityEn = "Mitra",
            deityHi = "मित्र",
            lordTa = "சனி",
            lordEn = "Saturn",
            lordHi = "शनि",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "மான் (Deer)",
            yoniEn = "Deer",
            yoniHi = "हिरण",
            rajjuTa = "உதர ரஜ்ஜு",
            rajjuEn = "Udhara Rajju",
            rajjuHi = "उदर रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "நா", "Naa", "ना", Rasi.VIRUCHIGAM),
                PadaLetterInfo(2, "நீ", "Nee / Ni", "नी", Rasi.VIRUCHIGAM),
                PadaLetterInfo(3, "நூ", "Noo / Nu", "नू", Rasi.VIRUCHIGAM),
                PadaLetterInfo(4, "நே", "Ne / Nay", "ने", Rasi.VIRUCHIGAM)
            ),
            allLettersSummaryTa = "நா, நீ, நூ, நே",
            allLettersSummaryEn = "Naa, Nee, Noo, Ne",
            allLettersSummaryHi = "ना, नी, नू, ने"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 18,
            nakshatraNameTa = "கேட்டை",
            nakshatraNameEn = "Jyeshtha",
            nakshatraNameHi = "ज्येष्ठा",
            deityTa = "இந்திரன்",
            deityEn = "Indra",
            deityHi = "इन्द्र",
            lordTa = "புதன்",
            lordEn = "Mercury",
            lordHi = "बुध",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "மான் (Deer)",
            yoniEn = "Deer",
            yoniHi = "हिरण",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
            rajjuEn = "Uru (Thigh) Rajju",
            rajjuHi = "ऊरु (जंघा) रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "நோ", "No / Noh", "नो", Rasi.VIRUCHIGAM),
                PadaLetterInfo(2, "யா", "Yaa", "या", Rasi.VIRUCHIGAM),
                PadaLetterInfo(3, "யீ", "Yee / Yi", "यी", Rasi.VIRUCHIGAM),
                PadaLetterInfo(4, "யூ", "Yoo / Yu", "यू", Rasi.VIRUCHIGAM)
            ),
            allLettersSummaryTa = "நோ, யா, யீ, யூ",
            allLettersSummaryEn = "No, Yaa, Yee, Yoo",
            allLettersSummaryHi = "नो, या, यी, यू"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 19,
            nakshatraNameTa = "மூலம்",
            nakshatraNameEn = "Mula",
            nakshatraNameHi = "मूल",
            deityTa = "நிருதி (அதிதேவதை)",
            deityEn = "Nirriti",
            deityHi = "निऋति",
            lordTa = "கேது",
            lordEn = "Ketu",
            lordHi = "केतु",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "நாய் (Dog)",
            yoniEn = "Dog",
            yoniHi = "कुत्ता",
            rajjuTa = "பாத ரஜ்ஜு",
            rajjuEn = "Pada (Foot) Rajju",
            rajjuHi = "पाद रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "யே", "Ye / Yay", "ये", Rasi.DHANUSU),
                PadaLetterInfo(2, "யோ", "Yo / Yoh", "यो", Rasi.DHANUSU),
                PadaLetterInfo(3, "பா", "Baa", "बा", Rasi.DHANUSU),
                PadaLetterInfo(4, "பீ", "Bee / Bi", "बी", Rasi.DHANUSU)
            ),
            allLettersSummaryTa = "யே, யோ, பா, பீ",
            allLettersSummaryEn = "Ye, Yo, Baa, Bee",
            allLettersSummaryHi = "ये, यो, बा, बी"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 20,
            nakshatraNameTa = "பூராடம்",
            nakshatraNameEn = "Purva Ashadha",
            nakshatraNameHi = "पूर्वाषाढ़ा",
            deityTa = "அபஸ் (நீர் தேவதை)",
            deityEn = "Apas (Water goddess)",
            deityHi = "आपः (जल देवी)",
            lordTa = "சுக்கிரன்",
            lordEn = "Venus",
            lordHi = "शुक्र",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "குரங்கு (Monkey)",
            yoniEn = "Monkey",
            yoniHi = "बंदर",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
            rajjuEn = "Uru (Thigh) Rajju",
            rajjuHi = "ऊरु (जंघा) रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "பூ", "Bhoo / Bu", "भू", Rasi.DHANUSU),
                PadaLetterInfo(2, "தா", "Dhaa", "धा", Rasi.DHANUSU),
                PadaLetterInfo(3, "பா", "Phaa / Pha", "फा", Rasi.DHANUSU),
                PadaLetterInfo(4, "டா", "Dhaa / Dha", "ढा", Rasi.DHANUSU)
            ),
            allLettersSummaryTa = "பூ, தா, பா, டா",
            allLettersSummaryEn = "Bhoo, Dhaa, Phaa, Dhaa",
            allLettersSummaryHi = "भू, धा, फा, ढा"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 21,
            nakshatraNameTa = "உத்திராடம்",
            nakshatraNameEn = "Uttara Ashadha",
            nakshatraNameHi = "उत्तराषाढ़ा",
            deityTa = "விஸ்வேதேவர்கள் (பத்து தேவ கணங்கள்)",
            deityEn = "Vishvadevas",
            deityHi = "विश्वेदेवा",
            lordTa = "சூரியன்",
            lordEn = "Sun",
            lordHi = "सूर्य",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "கீரி (Mongoose)",
            yoniEn = "Mongoose",
            yoniHi = "नेवला",
            rajjuTa = "உதர ரஜ்ஜு",
            rajjuEn = "Udhara Rajju",
            rajjuHi = "उदर रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "பே", "Bhe / Bay", "भे", Rasi.DHANUSU),
                PadaLetterInfo(2, "போ", "Bho / Boh", "भो", Rasi.MAGARAM),
                PadaLetterInfo(3, "ஜா", "Jaa", "जा", Rasi.MAGARAM),
                PadaLetterInfo(4, "ஜீ", "Jee / Ji", "जी", Rasi.MAGARAM)
            ),
            allLettersSummaryTa = "பே, போ, ஜா, ஜீ",
            allLettersSummaryEn = "Bhe, Bho, Jaa, Jee",
            allLettersSummaryHi = "भे, भो, जा, जी"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 22,
            nakshatraNameTa = "திருவோணம்",
            nakshatraNameEn = "Shravana",
            nakshatraNameHi = "श्रवण",
            deityTa = "மகாவிஷ்ணு",
            deityEn = "Lord Maha Vishnu",
            deityHi = "भगवान महाविष्णु",
            lordTa = "சந்திரன்",
            lordEn = "Moon",
            lordHi = "चंद्र",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "குரங்கு (Monkey)",
            yoniEn = "Monkey",
            yoniHi = "बंदर",
            rajjuTa = "கண்ட ரஜ்ஜு",
            rajjuEn = "Kanda (Neck) Rajju",
            rajjuHi = "कंठ रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "கீ", "Khee / Khi", "खी", Rasi.MAGARAM),
                PadaLetterInfo(2, "கூ", "Khoo / Khu", "खू", Rasi.MAGARAM),
                PadaLetterInfo(3, "கே", "Khe / Khay", "खे", Rasi.MAGARAM),
                PadaLetterInfo(4, "கோ", "Kho / Khoh", "खो", Rasi.MAGARAM)
            ),
            allLettersSummaryTa = "கீ, கூ, கே, கோ",
            allLettersSummaryEn = "Khee, Khoo, Khe, Kho",
            allLettersSummaryHi = "खी, खू, खे, खो"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 23,
            nakshatraNameTa = "அவிட்டம்",
            nakshatraNameEn = "Dhanishta",
            nakshatraNameHi = "धनिष्ठा",
            deityTa = "அஷ்ட வசுக்கள்",
            deityEn = "Ashta Vasus",
            deityHi = "अष्ट वसु",
            lordTa = "செவ்வாய்",
            lordEn = "Mars",
            lordHi = "मंगल",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "சிங்கம் (Lion)",
            yoniEn = "Lion",
            yoniHi = "सिंह",
            rajjuTa = "சிரோ ரஜ்ஜு",
            rajjuEn = "Siro (Head) Rajju",
            rajjuHi = "शिरो रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "கா", "Gaa", "गा", Rasi.MAGARAM),
                PadaLetterInfo(2, "கீ", "Gee / Gi", "गी", Rasi.MAGARAM),
                PadaLetterInfo(3, "கூ", "Goo / Gu", "गू", Rasi.KUMBAM),
                PadaLetterInfo(4, "கே", "Ge / Gay", "गे", Rasi.KUMBAM)
            ),
            allLettersSummaryTa = "கா, கீ, கூ, கே",
            allLettersSummaryEn = "Gaa, Gee, Goo, Ge",
            allLettersSummaryHi = "गा, गी, गू, गे"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 24,
            nakshatraNameTa = "சதயம்",
            nakshatraNameEn = "Shatabhisha",
            nakshatraNameHi = "शतभिषा",
            deityTa = "வருண பகவான்",
            deityEn = "Varuna (Ocean / Rain god)",
            deityHi = "वरुण देव",
            lordTa = "ராகு",
            lordEn = "Rahu",
            lordHi = "राहु",
            ganaTa = "ராட்சஸ கணம்",
            ganaEn = "Rakshasa Gana",
            ganaHi = "राक्षस गण",
            yoniTa = "குதிரை (Horse)",
            yoniEn = "Horse",
            yoniHi = "घोड़ा",
            rajjuTa = "சிரோ ரஜ்ஜு",
            rajjuEn = "Siro (Head) Rajju",
            rajjuHi = "शिरो रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "கோ", "Go / Goh", "गो", Rasi.KUMBAM),
                PadaLetterInfo(2, "ஸா", "Saa", "सा", Rasi.KUMBAM),
                PadaLetterInfo(3, "ஸீ", "See / Si", "सी", Rasi.KUMBAM),
                PadaLetterInfo(4, "ஸூ", "Soo / Su", "सू", Rasi.KUMBAM)
            ),
            allLettersSummaryTa = "கோ, ஸா, ஸீ, ஸூ",
            allLettersSummaryEn = "Go, Saa, See, Soo",
            allLettersSummaryHi = "गो, सा, सी, सू"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 25,
            nakshatraNameTa = "பூரட்டாதி",
            nakshatraNameEn = "Purva Bhadrapada",
            nakshatraNameHi = "पूर्वाभाद्रपद",
            deityTa = "அஜைகபாதர் (சிவ வடிவம்)",
            deityEn = "Aja Ekapada",
            deityHi = "अज एकपाद",
            lordTa = "குரு",
            lordEn = "Jupiter",
            lordHi = "बृहस्पति",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "சிங்கம் (Lion)",
            yoniEn = "Lion",
            yoniHi = "सिंह",
            rajjuTa = "கண்ட ரஜ்ஜு",
            rajjuEn = "Kanda (Neck) Rajju",
            rajjuHi = "कंठ रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "ஸே", "Se / Say", "से", Rasi.KUMBAM),
                PadaLetterInfo(2, "ஸோ", "So / Soh", "सो", Rasi.KUMBAM),
                PadaLetterInfo(3, "தா", "Daa", "दा", Rasi.KUMBAM),
                PadaLetterInfo(4, "தீ", "Dee / Di", "दी", Rasi.MEENAM)
            ),
            allLettersSummaryTa = "ஸே, ஸோ, தா, தீ",
            allLettersSummaryEn = "Se, So, Daa, Dee",
            allLettersSummaryHi = "से, सो, दा, दी"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 26,
            nakshatraNameTa = "உத்திரட்டாதி",
            nakshatraNameEn = "Uttara Bhadrapada",
            nakshatraNameHi = "उत्तराभाद्रपद",
            deityTa = "அஹிர்புத்னியன் (ஆதிசேஷன்)",
            deityEn = "Ahirbudhnya",
            deityHi = "अहिर्बुध्न्य",
            lordTa = "சனி",
            lordEn = "Saturn",
            lordHi = "शनि",
            ganaTa = "மனுஷ கணம்",
            ganaEn = "Manushya Gana",
            ganaHi = "मनुष्य गण",
            yoniTa = "பசு (Cow)",
            yoniEn = "Cow",
            yoniHi = "गाय",
            rajjuTa = "உதர ரஜ்ஜு",
            rajjuEn = "Udhara Rajju",
            rajjuHi = "उदर रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "தூ", "Doo / Du", "दू", Rasi.MEENAM),
                PadaLetterInfo(2, "த", "Tha / Ttha", "थ", Rasi.MEENAM),
                PadaLetterInfo(3, "ஜ", "Jha / Za", "झ", Rasi.MEENAM),
                PadaLetterInfo(4, "ஞ", "Nya / Tra", "ञ", Rasi.MEENAM)
            ),
            allLettersSummaryTa = "தூ, த, ஜ, ஞ",
            allLettersSummaryEn = "Doo, Tha, Jha, Nya",
            allLettersSummaryHi = "दू, थ, झ, ञ"
        ),
        NakshatraBabyLetters(
            nakshatraIndex = 27,
            nakshatraNameTa = "ரேவதி",
            nakshatraNameEn = "Revati",
            nakshatraNameHi = "रेवती",
            deityTa = "பூஷா (சூரிய வடிவம்)",
            deityEn = "Pushan",
            deityHi = "पूषन",
            lordTa = "புதன்",
            lordEn = "Mercury",
            lordHi = "बुध",
            ganaTa = "தேவ கணம்",
            ganaEn = "Deva Gana",
            ganaHi = "देव गण",
            yoniTa = "யானை (Elephant)",
            yoniEn = "Elephant",
            yoniHi = "हाथी",
            rajjuTa = "பாத ரஜ்ஜு",
            rajjuEn = "Pada (Foot) Rajju",
            rajjuHi = "पाद रज्जु",
            padas = listOf(
                PadaLetterInfo(1, "தே", "De / Day", "दे", Rasi.MEENAM),
                PadaLetterInfo(2, "தோ", "Do / Doh", "दो", Rasi.MEENAM),
                PadaLetterInfo(3, "சா", "Chaa", "चा", Rasi.MEENAM),
                PadaLetterInfo(4, "சீ", "Chee / Chi", "ची", Rasi.MEENAM)
            ),
            allLettersSummaryTa = "தே, தோ, சா, சீ",
            allLettersSummaryEn = "De, Do, Chaa, Chee",
            allLettersSummaryHi = "दे, दो, चा, ची"
        )
    )

    fun getNakshatraLetters(index: Int): NakshatraBabyLetters {
        return allNakshatraLetters.firstOrNull { it.nakshatraIndex == index } ?: allNakshatraLetters[0]
    }

    fun getByNakshatraName(name: String): NakshatraBabyLetters {
        val clean = name.trim().lowercase()
        return allNakshatraLetters.firstOrNull { star ->
            star.nakshatraNameTa.lowercase() == clean ||
            star.nakshatraNameEn.lowercase() == clean ||
            star.nakshatraNameHi.lowercase() == clean ||
            clean.contains(star.nakshatraNameTa.lowercase()) ||
            star.nakshatraNameTa.lowercase().contains(clean) ||
            clean.contains(star.nakshatraNameEn.lowercase()) ||
            star.nakshatraNameEn.lowercase().contains(clean)
        } ?: allNakshatraLetters[0]
    }

    fun searchNakshatraByQuery(query: String): List<NakshatraBabyLetters> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return allNakshatraLetters
        return allNakshatraLetters.filter { star ->
            star.nakshatraNameTa.lowercase().contains(q) ||
            star.nakshatraNameEn.lowercase().contains(q) ||
            star.nakshatraNameHi.lowercase().contains(q) ||
            star.allLettersSummaryTa.lowercase().contains(q) ||
            star.allLettersSummaryEn.lowercase().contains(q) ||
            star.padas.any { p ->
                p.letterTa.lowercase().contains(q) ||
                p.letterEn.lowercase().contains(q) ||
                p.letterHi.lowercase().contains(q) ||
                p.rasiTa.lowercase().contains(q) ||
                p.rasiEn.lowercase().contains(q)
            }
        }
    }

    fun getSuggestedNames(nakshatraIndex: Int, padaNumber: Int, gender: String = "ALL"): List<com.example.data.model.BabyNameSuggestion> {
        val list = namesDatabase[nakshatraIndex] ?: emptyList()
        return if (gender == "ALL") list else list.filter { it.gender.equals(gender, ignoreCase = true) }
    }

    private val namesDatabase: Map<Int, List<com.example.data.model.BabyNameSuggestion>> = mapOf(
        1 to listOf( // Ashwini: Chu, Che, Cho, La
            com.example.data.model.BabyNameSuggestion("சுதர்சன்", "Sudharsan", "M", "அழகான தோற்றம் உடையவர் / விஷ்ணுவின் சக்கரம்", "Handsome / Lord Vishnu's Discus"),
            com.example.data.model.BabyNameSuggestion("சுரேஷ்", "Suresh", "M", "தேவர்களின் தலைவர்", "Ruler of the Gods"),
            com.example.data.model.BabyNameSuggestion("சேதுராமன்", "Sethuraman", "M", "சேதுபாலத்தை அமைத்த ஸ்ரீராமர்", "Lord Rama"),
            com.example.data.model.BabyNameSuggestion("சோமசுந்தரம்", "Somasundaram", "M", "சந்திரனை அணிந்த சிவபெருமான்", "Lord Shiva"),
            com.example.data.model.BabyNameSuggestion("லக்ஷ்மணன்", "Lakshmanan", "M", "அதிர்ஷ்டம் நிறைந்தவர் / ஸ்ரீராமரின் சகோதரர்", "Prosperous / Lord Rama's Brother"),
            com.example.data.model.BabyNameSuggestion("லோகேஷ்", "Lokesh", "M", "உலகை ஆளும் அரசன்", "King of the World"),
            com.example.data.model.BabyNameSuggestion("சுபஸ்ரீ", "Subhashree", "F", "சுபகரமான மங்கல லட்சுமி", "Auspicious Goddess Lakshmi"),
            com.example.data.model.BabyNameSuggestion("சுவாதி", "Swathi", "F", "தூய்மையான பிரகாசமான நட்சத்திரம்", "Pure and Radiant"),
            com.example.data.model.BabyNameSuggestion("சேதுலட்சுமி", "Sethulakshmi", "F", "மங்கல லட்சுமி", "Goddess of Prosperity"),
            com.example.data.model.BabyNameSuggestion("சோபனா", "Shobana", "F", "அழகும் கம்பீரமும் நிறைந்தவள்", "Splendid & Radiant"),
            com.example.data.model.BabyNameSuggestion("லாவண்யா", "Lavanya", "F", "பேரழகு மற்றும் கருணை உடையவள்", "Grace and Elegance"),
            com.example.data.model.BabyNameSuggestion("லலிதா", "Lalitha", "F", "அம்பிகையின் திவ்ய நாமரூபம்", "Goddess Lalitha Tripurasundari")
        ),
        2 to listOf( // Bharani: Lee, Loo, Le, Lo
            com.example.data.model.BabyNameSuggestion("லீலாதரன்", "Leeladharan", "M", "திவ்ய லீலைகளைப் புரிபவர் (ஸ்ரீ கிருஷ்ணர்)", "Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("லோகநாதன்", "Loganathan", "M", "உலகத்தின் நாதன்", "Lord of the Universe"),
            com.example.data.model.BabyNameSuggestion("லோகேஸ்வரன்", "Lokeshwaran", "M", "அகில உலகை ஆளும் ஈசன்", "Ruler of the Cosmos"),
            com.example.data.model.BabyNameSuggestion("லோஹித்", "Lohith", "M", "செம்மையான சூரிய ஒளி", "Red radiant light / Sun"),
            com.example.data.model.BabyNameSuggestion("லீலாவதி", "Leelavathi", "F", "அழகும் அறிவும் கொண்ட தெய்வீகப் பெண்", "Charming & Intelligent Goddess"),
            com.example.data.model.BabyNameSuggestion("லோகநாயகி", "Loganayaki", "F", "அகில உலக நாயகி (அம்பாள்)", "Queen of the Universe"),
            com.example.data.model.BabyNameSuggestion("லோபமுத்ரா", "Lopamudra", "F", "அகத்திய முனிவரின் தர்மபத்தினி", "Sage Agastya's Wife / Scholar"),
            com.example.data.model.BabyNameSuggestion("லோகிதா", "Lohitha", "F", "பிரகாசமான அழகுடையவள்", "Bright and Beautiful")
        ),
        3 to listOf( // Krittika: A, I, U, E
            com.example.data.model.BabyNameSuggestion("அஸ்வின்", "Ashwin", "M", "ஒளிரும் நட்சத்திரம்", "Bright Star"),
            com.example.data.model.BabyNameSuggestion("அன்பரசன்", "Anbarasan", "M", "அன்பின் அரசன்", "King of Love"),
            com.example.data.model.BabyNameSuggestion("இளங்கோவன்", "Ilangovan", "M", "இளவரசன் / சிலப்பதிகார ஆசிரியர்", "Prince / Ancient Poet"),
            com.example.data.model.BabyNameSuggestion("உதயன்", "Udhayan", "M", "சூரிய உதயம் போன்ற பிரகாசம்", "Rising Sun"),
            com.example.data.model.BabyNameSuggestion("எழிலரசன்", "Ezhilarasan", "M", "அழகின் அரசன்", "King of Beauty"),
            com.example.data.model.BabyNameSuggestion("அபிராமி", "Abirami", "F", "என்றும் பேரருள் புரியும் அன்னை", "Goddess Abirami"),
            com.example.data.model.BabyNameSuggestion("அகல்யா", "Agalya", "F", "தூய அழகுடையவள்", "Virtuous & Flawless"),
            com.example.data.model.BabyNameSuggestion("இனியா", "Iniya", "F", "இனிமையான குணமுடையவள்", "Sweet and Lovely"),
            com.example.data.model.BabyNameSuggestion("உமையாள்", "Umayal", "F", "பார்வதி தேவி", "Goddess Parvathi"),
            com.example.data.model.BabyNameSuggestion("எழிலரசி", "Ezhilarasi", "F", "பேரழகின் ராணி", "Queen of Beauty")
        ),
        4 to listOf( // Rohini: O, Vaa, Vi, Vu
            com.example.data.model.BabyNameSuggestion("ஓம்கார்", "Omkar", "M", "பிரணவ மந்திரத்தின் வடிவம்", "Sacred Sound of Om"),
            com.example.data.model.BabyNameSuggestion("வாசுதேவன்", "Vasudevan", "M", "ஸ்ரீ கிருஷ்ணர்", "Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("விஷ்ணு", "Vishnu", "M", "காக்கும் கடவுள்", "The Preserver Lord"),
            com.example.data.model.BabyNameSuggestion("விக்னேஷ்", "Vignesh", "M", "விக்னங்களை நீக்கும் கணபதி", "Lord Ganesha"),
            com.example.data.model.BabyNameSuggestion("விஜயன்", "Vijayan", "M", "எப்போதும் வெற்றி பெறுபவர்", "Victorious One"),
            com.example.data.model.BabyNameSuggestion("ஓவியா", "Oviya", "F", "ஓவியம் போன்ற பேரழகு", "Beautiful Painting / Art"),
            com.example.data.model.BabyNameSuggestion("வசுந்தரா", "Vasundhara", "F", "பூமித்தாய் / ஐஸ்வர்யம்", "Mother Earth / Wealth"),
            com.example.data.model.BabyNameSuggestion("வித்யா", "Vidhya", "F", "ஞானக் கலைமகள்", "Knowledge & Wisdom"),
            com.example.data.model.BabyNameSuggestion("வைஷ்ணவி", "Vaishnavi", "F", "மகாவிஷ்ணுவின் சக்தி", "Goddess Vaishnavi"),
            com.example.data.model.BabyNameSuggestion("விமலா", "Vimala", "F", "மாசற்ற தூய்மையானவள்", "Pure and Spotless")
        ),
        5 to listOf( // Mrigashira: Ve, Vo, Kaa, Kee
            com.example.data.model.BabyNameSuggestion("வேல்முருகன்", "Velmurugan", "M", "ஞானவேல் தாங்கிய கந்தன்", "Lord Murugan with Divine Spear"),
            com.example.data.model.BabyNameSuggestion("வெற்றிவேல்", "Vetrivel", "M", "வெற்றி தரும் ஞானவேல்", "Victorious Spear of Murugan"),
            com.example.data.model.BabyNameSuggestion("கார்த்திகேயன்", "Karthikeyan", "M", "முருகப்பெருமான்", "Lord Murugan"),
            com.example.data.model.BabyNameSuggestion("கீர்த்திவாசன்", "Keerthivasan", "M", "புகழ்மிக்கவர்", "One with Eternal Fame"),
            com.example.data.model.BabyNameSuggestion("வேதவல்லி", "Vedavalli", "F", "வேதங்களின் தத்துவ வடிவம்", "Goddess of Vedas"),
            com.example.data.model.BabyNameSuggestion("கார்த்திகா", "Karthika", "F", "கார்த்திகை தீப ஒளி", "Radiant Lamp / Star"),
            com.example.data.model.BabyNameSuggestion("காவ்யா", "Kavya", "F", "கவிதை நயம் மிக்கவள்", "Poetry and Art"),
            com.example.data.model.BabyNameSuggestion("கீர்த்தனா", "Keerthana", "F", "பக்திப் பாடல் / புகழ்", "Devotional Song / Praise")
        ),
        6 to listOf( // Arudra: Ku, Gha, Nga, Cha
            com.example.data.model.BabyNameSuggestion("குமரகுருபரன்", "Kumaragurubaran", "M", "முருகப்பெருமான்", "Lord Murugan"),
            com.example.data.model.BabyNameSuggestion("குணசேகரன்", "Gunasekaran", "M", "நற்குணங்களின் சிகரம்", "Virtuous Leader"),
            com.example.data.model.BabyNameSuggestion("சந்திரசேகர்", "Chandrasekar", "M", "பிறை சூடிய சிவன்", "Lord Shiva with Crescent Moon"),
            com.example.data.model.BabyNameSuggestion("குமுதவல்லி", "Kumudhavalli", "F", "அல்லி மலர் போன்றவள்", "Lotus / Lily Blossom"),
            com.example.data.model.BabyNameSuggestion("குணவதி", "Gunavathi", "F", "உயரிய குணங்கள் நிறைந்தவள்", "Virtuous & Noble"),
            com.example.data.model.BabyNameSuggestion("சந்தியா", "Sandhiya", "F", "அந்தி மாலைப் பொழுது / தெய்வீகம்", "Twilight / Sacred Prayer")
        ),
        7 to listOf( // Punarvasu: Ke, Ko, Haa, Hee
            com.example.data.model.BabyNameSuggestion("கேசவன்", "Keshavan", "M", "மகாவிஷ்ணு", "Lord Vishnu"),
            com.example.data.model.BabyNameSuggestion("கோகுலன்", "Gokulan", "M", "ஆயர்பாடி கண்ணன்", "Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("ஹரிஹரன்", "Hariharan", "M", "ஹரியும் சிவனும் இணைந்த வடிவம்", "Lord Shiva & Vishnu"),
            com.example.data.model.BabyNameSuggestion("ஹேமந்த்", "Hemanth", "M", "பொன் போன்ற ஒளி", "Golden Season / Radiance"),
            com.example.data.model.BabyNameSuggestion("கோகிலா", "Kokila", "F", "இன்குரல் குயில்", "Nightingale / Sweet voice"),
            com.example.data.model.BabyNameSuggestion("ஹேமாவதி", "Hemavathi", "F", "பொன் நிற அன்னை பார்வதி", "Golden Goddess Parvathi"),
            com.example.data.model.BabyNameSuggestion("ஹரிணி", "Harini", "F", "மான் போன்ற அழகுடையவள்", "Gentle Deer-eyed Beauty")
        ),
        8 to listOf( // Pushya: Hu, He, Ho, Daa
            com.example.data.model.BabyNameSuggestion("ஹேமராஜன்", "Hemarajan", "M", "பொன் அரசன்", "Golden King"),
            com.example.data.model.BabyNameSuggestion("ஹோமேஷ்", "Homesh", "M", "யாகத்தின் புனித ஒளி", "Sacred Radiance"),
            com.example.data.model.BabyNameSuggestion("தாமோதரன்", "Damodharan", "M", "ஸ்ரீ கிருஷ்ணர்", "Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("ஹேமலதா", "Hemalatha", "F", "தங்கக் கொடி போன்ற அழகு", "Golden Vine"),
            com.example.data.model.BabyNameSuggestion("தமயந்தி", "Damayanthi", "F", "அழகும் கற்பும் நிறைந்தவள்", "Queen of Virtue"),
            com.example.data.model.BabyNameSuggestion("தாரணி", "Dharani", "F", "பூமித்தாய்", "Earth / Protector")
        ),
        9 to listOf( // Ashlesha: Dee, Doo, De, Do
            com.example.data.model.BabyNameSuggestion("தீபக்", "Deepak", "M", "ஞான விளக்கு", "Lamp of Light"),
            com.example.data.model.BabyNameSuggestion("தினேஷ்குமார்", "Dineshkumar", "M", "சூரியன்", "Sun God"),
            com.example.data.model.BabyNameSuggestion("தேவராஜன்", "Devarajan", "M", "தேவர்களின் தலைவன்", "King of Gods"),
            com.example.data.model.BabyNameSuggestion("தீபிகா", "Deepika", "F", "ஒளிச்சுடர்", "Ray of Light"),
            com.example.data.model.BabyNameSuggestion("திவ்யா", "Divya", "F", "தெய்வீக ஒளி", "Divine Light"),
            com.example.data.model.BabyNameSuggestion("தேவசேனா", "Devasena", "F", "முருகப்பெருமானின் தேவி", "Consort of Lord Murugan")
        ),
        10 to listOf( // Magha: Maa, Mee, Moo, Me
            com.example.data.model.BabyNameSuggestion("மாதவன்", "Madhavan", "M", "மகாவிஷ்ணு", "Lord Krishna / Vishnu"),
            com.example.data.model.BabyNameSuggestion("மணிகண்டன்", "Manikandan", "M", "ஸ்ரீ ஐயப்பன்", "Lord Ayyappan"),
            com.example.data.model.BabyNameSuggestion("முகுந்தன்", "Mukundan", "M", "முக்தி அளிப்பவர்", "Lord Krishna / Giver of Liberation"),
            com.example.data.model.BabyNameSuggestion("மகேஷ்வரன்", "Maheshwaran", "M", "பரமேஸ்வரன்", "Great Lord Shiva"),
            com.example.data.model.BabyNameSuggestion("மகாலட்சுமி", "Mahalakshmi", "F", "செல்வத் திருமகள்", "Goddess of Wealth"),
            com.example.data.model.BabyNameSuggestion("மீனாட்சி", "Meenakshi", "F", "மதுரை அன்னை", "Goddess Meenakshi"),
            com.example.data.model.BabyNameSuggestion("மஞ்சுளா", "Manjula", "F", "அழகான மனம் கொண்டவள்", "Charming & Lovely")
        ),
        11 to listOf( // Purva Phalguni (Pooram): Mo, Taa, Tee, Too
            com.example.data.model.BabyNameSuggestion("மோகன்", "Mohan", "M", "அனைவரையும் வசீகரிப்பவர்", "Charming Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("தாமோதரன்", "Damodaran", "M", "கண்ணபிரான்", "Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("மோகனா", "Mohana", "F", "வசீகரமானவள்", "Attractive & Charming"),
            com.example.data.model.BabyNameSuggestion("தாரிகா", "Tharika", "F", "நட்சத்திர ஒளி", "Star / Pearl")
        ),
        12 to listOf( // Uttara Phalguni (Uthiram): Te, To, Paa, Pee
            com.example.data.model.BabyNameSuggestion("தேவநாதன்", "Devanathan", "M", "தேவர்களின் தலைவன்", "Lord of Celestials"),
            com.example.data.model.BabyNameSuggestion("பார்த்தசாரதி", "Parthasarathy", "M", "ஸ்ரீ கிருஷ்ணர்", "Lord Krishna / Charioteer"),
            com.example.data.model.BabyNameSuggestion("பாஸ்கரன்", "Bhaskaran", "M", "சூரிய பகவான்", "Sun God"),
            com.example.data.model.BabyNameSuggestion("பவித்ரா", "Pavithra", "F", "பரிசுத்தமானவள்", "Pure and Holy"),
            com.example.data.model.BabyNameSuggestion("பத்மாவதி", "Padmavathi", "F", "அலர்மேல்மங்கைத் தாயார்", "Goddess Padmavathi / Lakshmi")
        ),
        13 to listOf( // Hasta: Pu, Sha, Na, Taa
            com.example.data.model.BabyNameSuggestion("புகழேந்தி", "Pugazhendhi", "M", "புகழ் பெற்ற கவிஞர்", "Famed Poet"),
            com.example.data.model.BabyNameSuggestion("புருஷோத்தமன்", "Purushothaman", "M", "உயரிய புருஷன் / மகாவிஷ்ணு", "Supreme Lord Vishnu"),
            com.example.data.model.BabyNameSuggestion("நந்தகோபன்", "Nandhagopan", "M", "கண்ணனின் தந்தை", "Nanda"),
            com.example.data.model.BabyNameSuggestion("புவனா", "Bhuvana", "F", "அகில உலகம் / அன்னை புவனேஸ்வரி", "Goddess of the Universe"),
            com.example.data.model.BabyNameSuggestion("நந்தினி", "Nandhini", "F", "ஆனந்தம் அருள்பவள்", "Bringer of Joy")
        ),
        14 to listOf( // Chitra: Pe, Po, Raa, Ree
            com.example.data.model.BabyNameSuggestion("பிரபாகரன்", "Prabhakaran", "M", "ஒளி வீசுபவர்", "Radiant Sun"),
            com.example.data.model.BabyNameSuggestion("ராகவேந்திரன்", "Raghavendran", "M", "ஸ்ரீ ராகவேந்திரர் / ஸ்ரீராமர்", "Lord Rama / Saint Raghavendra"),
            com.example.data.model.BabyNameSuggestion("ராஜராஜன்", "Rajarajan", "M", "மன்னர் மன்னன்", "King of Kings"),
            com.example.data.model.BabyNameSuggestion("ராதிகா", "Radhika", "F", "ராதா பிராட்டி", "Beloved Radha"),
            com.example.data.model.BabyNameSuggestion("ரேவதி", "Revathi", "F", "ஐஸ்வர்யம் நிறைந்தவள்", "Prosperity & Grace")
        ),
        15 to listOf( // Swathi: Ru, Re, Ro, Taa
            com.example.data.model.BabyNameSuggestion("ருத்ரன்", "Rudhran", "M", "சிவபெருமான்", "Lord Shiva"),
            com.example.data.model.BabyNameSuggestion("ரோஹித்", "Rohit", "M", "சூரியனின் முதல் ஒளிக்கதிர்", "First Ray of Sun"),
            com.example.data.model.BabyNameSuggestion("ரூபிணி", "Roopini", "F", "அழகிய உருவமுடையவள்", "Goddess of Beauty"),
            com.example.data.model.BabyNameSuggestion("ரோஷிணி", "Roshini", "F", "ஒளி மற்றும் பிரகாசம்", "Light & Brilliance")
        ),
        16 to listOf( // Vishakha: Tee, Too, Te, To
            com.example.data.model.BabyNameSuggestion("தீனதயாளன்", "Deenadhayalan", "M", "எளியோரிடம் கருணை கொண்டவர்", "Merciful Lord"),
            com.example.data.model.BabyNameSuggestion("தேவபிரசாத்", "Devaprasad", "M", "இறைவனின் பிரசாதம்", "God's Grace"),
            com.example.data.model.BabyNameSuggestion("தீபிகா", "Deepika", "F", "ஒளி விளக்கு", "Radiant Lamp"),
            com.example.data.model.BabyNameSuggestion("தேவகி", "Devaki", "F", "கண்ணனின் தாய்", "Mother of Lord Krishna")
        ),
        17 to listOf( // Anuradha: Naa, Nee, Noo, Ne
            com.example.data.model.BabyNameSuggestion("நாராயணன்", "Narayanan", "M", "மகாவிஷ்ணு", "Lord Vishnu"),
            com.example.data.model.BabyNameSuggestion("நவீன்குமார்", "Naveenkumar", "M", "புதுமையானவர்", "Ever Fresh & Youthful"),
            com.example.data.model.BabyNameSuggestion("நீலமேகம்", "Neelamegham", "M", "நீல மேகம் போன்ற கண்ணன்", "Blue Cloud / Krishna"),
            com.example.data.model.BabyNameSuggestion("நளினி", "Nalini", "F", "தாமரை மலர்", "Lotus Flower"),
            com.example.data.model.BabyNameSuggestion("நீலாம்பரி", "Neelambari", "F", "நீல வானம் போன்ற தெய்வீக ராகம்", "Divine Musical Melody")
        ),
        18 to listOf( // Jyeshtha: No, Yaa, Yee, Yoo
            com.example.data.model.BabyNameSuggestion("யோகேஸ்வரன்", "Yogeshwaran", "M", "யோகிகளின் தலைவன் / சிவன்", "Lord of Yoga / Shiva"),
            com.example.data.model.BabyNameSuggestion("யுவராஜ்", "Yuvaraj", "M", "இளவரசன்", "Crown Prince"),
            com.example.data.model.BabyNameSuggestion("யசோதா", "Yasodha", "F", "கண்ணனை வளர்த்த அன்னை", "Mother Yashoda"),
            com.example.data.model.BabyNameSuggestion("யமுனா", "Yamuna", "F", "புனித நதி", "Sacred River Yamuna")
        ),
        19 to listOf( // Mula: Ye, Yo, Bhaa, Bhee
            com.example.data.model.BabyNameSuggestion("யோகேஷ்", "Yogesh", "M", "யோகத்தின் அதிபதி", "Master of Yoga"),
            com.example.data.model.BabyNameSuggestion("பாரதி", "Bharathi", "M", "ஞானத்தின் வடிவம்", "Scholar / Sage"),
            com.example.data.model.BabyNameSuggestion("பாக்யராஜ்", "Bhagyaraj", "M", "பாக்கியங்களின் அரசன்", "King of Luck"),
            com.example.data.model.BabyNameSuggestion("பவானி", "Bhavani", "F", "அன்னை பராசக்தி", "Goddess Bhavani"),
            com.example.data.model.BabyNameSuggestion("பாக்யலட்சுமி", "Bhagyalakshmi", "F", "அதிர்ஷ்ட லட்சுமி", "Goddess of Fortune")
        ),
        20 to listOf( // Purvashada: Bhoo, Dhaa, Phaa, Dhaa
            com.example.data.model.BabyNameSuggestion("பூபதிராஜ்", "Boopathiraj", "M", "பூமியின் அரசன்", "King of the Earth"),
            com.example.data.model.BabyNameSuggestion("தனுஷ்கோடி", "Dhanushkodi", "M", "புனித தலம் / ஸ்ரீராமரின் வில்", "Sacred Shore / Rama's Bow"),
            com.example.data.model.BabyNameSuggestion("பூமிதா", "Bhumitha", "F", "பூமித்தாயின் அருள்", "Blessed by Mother Earth"),
            com.example.data.model.BabyNameSuggestion("தன்யா", "Dhanya", "F", "பாக்கியசாலி", "Thankful and Blessed")
        ),
        21 to listOf( // Uttarashada: Bhe, Bho, Jaa, Jee
            com.example.data.model.BabyNameSuggestion("ஜெயக்குமார்", "Jayakumar", "M", "வெற்றியின் மைந்தன்", "Victorious Son"),
            com.example.data.model.BabyNameSuggestion("ஜனார்த்தனன்", "Janarthanan", "M", "மகாவிஷ்ணு", "Lord Vishnu"),
            com.example.data.model.BabyNameSuggestion("ஜெயஸ்ரீ", "Jayashree", "F", "வெற்றி தரும் லட்சுமி", "Goddess of Victory"),
            com.example.data.model.BabyNameSuggestion("ஜானகி", "Janaki", "F", "சீதா பிராட்டி", "Goddess Sita")
        ),
        22 to listOf( // Shravana: Khee, Khoo, Khe, Kho
            com.example.data.model.BabyNameSuggestion("ஜூஹித்", "Juhith", "M", "ஒளிர்பவர்", "Bright & Shining"),
            com.example.data.model.BabyNameSuggestion("கேசவமூர்த்தி", "Kesavamoorthy", "M", "மகாவிஷ்ணுவின் திருவுருவம்", "Form of Lord Vishnu"),
            com.example.data.model.BabyNameSuggestion("கிருத்திகா", "Kiruthika", "F", "நட்சத்திர தீபம்", "Radiant Star")
        ),
        23 to listOf( // Dhanishta: Gaa, Gee, Goo, Ge
            com.example.data.model.BabyNameSuggestion("கணேசன்", "Ganesan", "M", "விநாயகப் பெருமான்", "Lord Ganesha"),
            com.example.data.model.BabyNameSuggestion("கிரிதரன்", "Giridharan", "M", "கோவர்த்தன மலையைத் தாங்கிய கண்ணன்", "Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("காயத்ரி", "Gayathri", "F", "வேத மாதா காயத்ரி", "Mother of Vedas"),
            com.example.data.model.BabyNameSuggestion("கீதாஞ்சலி", "Geethanjali", "F", "இசைப் பாமாலை", "Musical Offering")
        ),
        24 to listOf( // Shatabhisha: Go, Saa, See, Soo
            com.example.data.model.BabyNameSuggestion("கோவிந்தன்", "Govindhan", "M", "பசுக்களைக் காக்கும் கண்ணன்", "Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("சசிதரன்", "Sasidharan", "M", "சந்திரனை அணிந்த சிவன்", "Lord Shiva"),
            com.example.data.model.BabyNameSuggestion("சாய்ராம்", "Sairam", "M", "ஸ்ரீ சாயிநாத்", "Lord Sai Baba"),
            com.example.data.model.BabyNameSuggestion("சரஸ்வதி", "Saraswathi", "F", "கல்விக் கடவுள்", "Goddess of Knowledge"),
            com.example.data.model.BabyNameSuggestion("சௌந்தர்யா", "Soundharya", "F", "பேரழகி", "Ultimate Beauty")
        ),
        25 to listOf( // Purvabhadrapada: Se, So, Daa, Dee
            com.example.data.model.BabyNameSuggestion("சேதுபதி", "Sethupathi", "M", "சேதுவின் காவலர்", "Protector of the Bridge"),
            com.example.data.model.BabyNameSuggestion("சோமேஷ்வர்", "Someshwar", "M", "சந்திர ஈஸ்வரன்", "Lord Shiva"),
            com.example.data.model.BabyNameSuggestion("தினகரன்", "Dhinakaran", "M", "சூரிய பகவான்", "Sun God"),
            com.example.data.model.BabyNameSuggestion("சௌமியா", "Sowmya", "F", "சாந்தமான அமைதி வடிவம்", "Gentle & Calm Goddess"),
            com.example.data.model.BabyNameSuggestion("தீபிகா", "Deepika", "F", "ஒளிச்சுடர்", "Radiant Lamp")
        ),
        26 to listOf( // Uttarabhadrapada: Doo, Tha, Jha, Nya
            com.example.data.model.BabyNameSuggestion("துரைமுருகன்", "Duraimurugan", "M", "தலைவன் முருகன்", "Lord Murugan"),
            com.example.data.model.BabyNameSuggestion("தாமோதரன்", "Dhamodharan", "M", "ஸ்ரீ கிருஷ்ணர்", "Lord Krishna"),
            com.example.data.model.BabyNameSuggestion("துர்காதேவி", "Durgadevi", "F", "துர்கா பரமேஸ்வரி", "Goddess Durga"),
            com.example.data.model.BabyNameSuggestion("தன்விகா", "Thanvika", "F", "அழகிய தாமரை வடிவம்", "Goddess Lakshmi")
        ),
        27 to listOf( // Revati: De, Do, Chaa, Chee
            com.example.data.model.BabyNameSuggestion("தேவராஜன்", "Devarajan", "M", "தேவ தலைவன்", "King of Devas"),
            com.example.data.model.BabyNameSuggestion("சந்திரமௌலி", "Chandramouli", "M", "சிவன்", "Lord Shiva"),
            com.example.data.model.BabyNameSuggestion("சாரங்கபாணி", "Sarangapani", "M", "வில்லேந்திய விஷ்ணு", "Lord Vishnu"),
            com.example.data.model.BabyNameSuggestion("தேவகி", "Devaki", "F", "கண்ணனின் தாய்", "Mother Devaki"),
            com.example.data.model.BabyNameSuggestion("சாருலதா", "Charulatha", "F", "அழகான கொடி போன்றவள்", "Beautiful Vine")
        )
    )
}

