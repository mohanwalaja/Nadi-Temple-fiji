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
            lordTa = "கேது",
            lordEn = "Ketu",
            ganaTa = "தேவ கணம்",
            yoniTa = "குதிரை (Horse)",
            rajjuTa = "பாத ரஜ்ஜு",
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
            lordTa = "சுக்கிரன்",
            lordEn = "Venus",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "யானை (Elephant)",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
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
            lordTa = "சூரியன்",
            lordEn = "Sun",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "ஆடு (Sheep)",
            rajjuTa = "உதர (நாபி) ரஜ்ஜு",
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
            lordTa = "சந்திரன்",
            lordEn = "Moon",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "பாம்பு (Serpent)",
            rajjuTa = "கண்ட ரஜ்ஜு",
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
            lordTa = "செவ்வாய்",
            lordEn = "Mars",
            ganaTa = "தேவ கணம்",
            yoniTa = "பாம்பு (Serpent)",
            rajjuTa = "சிரோ ரஜ்ஜு",
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
            lordTa = "ராகு",
            lordEn = "Rahu",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "நாய் (Dog)",
            rajjuTa = "சிரோ ரஜ்ஜு",
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
            lordTa = "குரு",
            lordEn = "Jupiter",
            ganaTa = "தேவ கணம்",
            yoniTa = "பூனை (Cat)",
            rajjuTa = "கண்ட ரஜ்ஜு",
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
            lordTa = "சனி",
            lordEn = "Saturn",
            ganaTa = "தேவ கணம்",
            yoniTa = "ஆடு (Sheep)",
            rajjuTa = "உதர ரஜ்ஜு",
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
            lordTa = "புதன்",
            lordEn = "Mercury",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "பூனை (Cat)",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
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
            lordTa = "கேது",
            lordEn = "Ketu",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "எலி (Rat)",
            rajjuTa = "பாத ரஜ்ஜு",
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
            lordTa = "சுக்கிரன்",
            lordEn = "Venus",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "எலி (Rat)",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
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
            lordTa = "சூரியன்",
            lordEn = "Sun",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "பசு (Cow)",
            rajjuTa = "உதர ரஜ்ஜு",
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
            lordTa = "சந்திரன்",
            lordEn = "Moon",
            ganaTa = "தேவ கணம்",
            yoniTa = "எருமை (Buffalo)",
            rajjuTa = "கண்ட ரஜ்ஜு",
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
            lordTa = "செவ்வாய்",
            lordEn = "Mars",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "புலி (Tiger)",
            rajjuTa = "சிரோ ரஜ்ஜு",
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
            lordTa = "ராகு",
            lordEn = "Rahu",
            ganaTa = "தேவ கணம்",
            yoniTa = "எருமை (Buffalo)",
            rajjuTa = "சிரோ ரஜ்ஜு",
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
            lordTa = "குரு",
            lordEn = "Jupiter",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "புலி (Tiger)",
            rajjuTa = "கண்ட ரஜ்ஜு",
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
            lordTa = "சனி",
            lordEn = "Saturn",
            ganaTa = "தேவ கணம்",
            yoniTa = "மான் (Deer)",
            rajjuTa = "உதர ரஜ்ஜு",
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
            lordTa = "புதன்",
            lordEn = "Mercury",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "மான் (Deer)",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
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
            lordTa = "கேது",
            lordEn = "Ketu",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "நாய் (Dog)",
            rajjuTa = "பாத ரஜ்ஜு",
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
            lordTa = "சுக்கிரன்",
            lordEn = "Venus",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "குரங்கு (Monkey)",
            rajjuTa = "தொடை (ஊரு) ரஜ்ஜு",
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
            lordTa = "சூரியன்",
            lordEn = "Sun",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "கீரி (Mongoose)",
            rajjuTa = "உதர ரஜ்ஜு",
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
            lordTa = "சந்திரன்",
            lordEn = "Moon",
            ganaTa = "தேவ கணம்",
            yoniTa = "குரங்கு (Monkey)",
            rajjuTa = "கண்ட ரஜ்ஜு",
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
            lordTa = "செவ்வாய்",
            lordEn = "Mars",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "சிங்கம் (Lion)",
            rajjuTa = "சிரோ ரஜ்ஜு",
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
            lordTa = "ராகு",
            lordEn = "Rahu",
            ganaTa = "ராட்சஸ கணம்",
            yoniTa = "குதிரை (Horse)",
            rajjuTa = "சிரோ ரஜ்ஜு",
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
            lordTa = "குரு",
            lordEn = "Jupiter",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "சிங்கம் (Lion)",
            rajjuTa = "கண்ட ரஜ்ஜு",
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
            lordTa = "சனி",
            lordEn = "Saturn",
            ganaTa = "மனுஷ கணம்",
            yoniTa = "பசு (Cow)",
            rajjuTa = "உதர ரஜ்ஜு",
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
            lordTa = "புதன்",
            lordEn = "Mercury",
            ganaTa = "தேவ கணம்",
            yoniTa = "யானை (Elephant)",
            rajjuTa = "பாத ரஜ்ஜு",
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
}
