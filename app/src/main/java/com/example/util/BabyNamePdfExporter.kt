package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.example.data.model.AppLanguage
import com.example.data.model.BabyNamingBirthResult
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.util.Locale

object BabyNamePdfExporter {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842

    private val COLOR_MAROON = Color.rgb(128, 0, 32)
    private val COLOR_GOLD = Color.rgb(212, 175, 55)
    private val COLOR_LIGHT_GOLD = Color.rgb(255, 248, 225)
    private val COLOR_DARK_TEXT = Color.rgb(33, 33, 33)
    private val COLOR_MUTED_TEXT = Color.rgb(100, 100, 100)
    private val COLOR_BORDER = Color.rgb(220, 200, 160)
    private val COLOR_CARD_BG = Color.rgb(253, 251, 247)

    fun exportBabyNamingCertificatePdf(
        context: Context,
        result: BabyNamingBirthResult,
        lang: AppLanguage = AppLanguage.ENGLISH
    ): File? {
        return try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG)

            // 1. Clean White Background
            fillPaint.color = Color.WHITE
            canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), fillPaint)

            // 2. Ornate Double Borders (Maroon & Gold)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 2.5f
            canvas.drawRoundRect(RectF(16f, 16f, (PAGE_WIDTH - 16).toFloat(), (PAGE_HEIGHT - 16).toFloat()), 10f, 10f, borderPaint)

            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(20f, 20f, (PAGE_WIDTH - 20).toFloat(), (PAGE_HEIGHT - 20).toFloat()), 8f, 8f, borderPaint)

            // Corner Accents
            drawCornerAccent(canvas, fillPaint, 21f, 21f)
            drawCornerAccent(canvas, fillPaint, (PAGE_WIDTH - 21).toFloat(), 21f)
            drawCornerAccent(canvas, fillPaint, 21f, (PAGE_HEIGHT - 21).toFloat())
            drawCornerAccent(canvas, fillPaint, (PAGE_WIDTH - 21).toFloat(), (PAGE_HEIGHT - 21).toFloat())

            val boxLeft = 28f
            val boxRight = (PAGE_WIDTH - 28).toFloat()
            val contentWidth = (boxRight - boxLeft).toInt()
            var currentY = 26f

            // 3. Temple Header Banner (Pure Single Language)
            val headerHeight = 66f
            fillPaint.color = COLOR_MAROON
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + headerHeight), 8f, 8f, fillPaint)

            headerPaint.textAlign = Paint.Align.CENTER
            headerPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)

            val templeTitle = when (lang) {
                AppLanguage.TAMIL -> "🕉️ ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில் 🕉️"
                AppLanguage.HINDI -> "॥ श्री गणेशाय नमः ॥ श्री शिव सुब्रमण्य स्वामी मंदिर"
                AppLanguage.ENGLISH -> "SRI SIVA SUBRAMANIYA SWAMI TEMPLE"
            }
            headerPaint.color = Color.WHITE
            headerPaint.textSize = 12.5f
            canvas.drawText(templeTitle, (PAGE_WIDTH / 2).toFloat(), currentY + 20f, headerPaint)

            val locationSub = when (lang) {
                AppLanguage.TAMIL -> "நாடி, பிஜி தீவுகள் • துல்லிய வேத ஜோதிட சேவை"
                AppLanguage.HINDI -> "नादी, फिजी द्वीप • वैदिक ज्योतिष एवं नामकरण संस्कार"
                AppLanguage.ENGLISH -> "Nadi, Fiji Islands • Vedic Astrological Services"
            }
            headerPaint.color = COLOR_GOLD
            headerPaint.textSize = 9.5f
            canvas.drawText(locationSub, (PAGE_WIDTH / 2).toFloat(), currentY + 36f, headerPaint)

            val certTitle = when (lang) {
                AppLanguage.TAMIL -> "வேத நாமகரண சுப நட்சத்திர அட்சர சான்றிதழ்"
                AppLanguage.HINDI -> "वैदिक नामकरण एवं नक्षत्र शुभ अक्षर प्रमाण पत्र"
                AppLanguage.ENGLISH -> "VEDIC BABY NAMING & NAKSHATRA LETTERS CERTIFICATE"
            }
            headerPaint.color = COLOR_LIGHT_GOLD
            headerPaint.textSize = 10f
            canvas.drawText(certTitle, (PAGE_WIDTH / 2).toFloat(), currentY + 52f, headerPaint)

            currentY += headerHeight + 8f

            // 4. Baby & Birth Particulars Card (Pure Single Language)
            val infoTop = currentY
            val infoHeight = 74f
            fillPaint.color = COLOR_CARD_BG
            canvas.drawRoundRect(RectF(boxLeft, infoTop, boxRight, infoTop + infoHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(boxLeft, infoTop, boxRight, infoTop + infoHeight), 6f, 6f, borderPaint)

            val dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
            val timeFmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

            val col1X = boxLeft + 12f
            val col2X = boxLeft + 270f
            val r1Y = infoTop + 18f
            val r2Y = infoTop + 38f
            val r3Y = infoTop + 58f

            val babyDisplayName = if (result.babyName.isNotBlank()) {
                result.babyName
            } else {
                when (lang) {
                    AppLanguage.TAMIL -> "பிறந்த குழந்தை"
                    AppLanguage.HINDI -> "नवजात शिशु"
                    AppLanguage.ENGLISH -> "Newborn Baby"
                }
            }

            val genderLabel = when (result.gender) {
                "M" -> when (lang) {
                    AppLanguage.TAMIL -> "ஆண் குழந்தை"
                    AppLanguage.HINDI -> "बालक (लड़का)"
                    AppLanguage.ENGLISH -> "Baby Boy"
                }
                else -> when (lang) {
                    AppLanguage.TAMIL -> "பெண் குழந்தை"
                    AppLanguage.HINDI -> "बालिका (लड़की)"
                    AppLanguage.ENGLISH -> "Baby Girl"
                }
            }

            val nameLabel = when (lang) {
                AppLanguage.TAMIL -> "பெயர்:"
                AppLanguage.HINDI -> "नाम:"
                AppLanguage.ENGLISH -> "Name:"
            }
            val genderTag = when (lang) {
                AppLanguage.TAMIL -> "பாலினம்:"
                AppLanguage.HINDI -> "लिंग:"
                AppLanguage.ENGLISH -> "Gender:"
            }
            val dobLabel = when (lang) {
                AppLanguage.TAMIL -> "பிறந்த தேதி:"
                AppLanguage.HINDI -> "जन्म तिथि:"
                AppLanguage.ENGLISH -> "Date of Birth:"
            }
            val placeLabel = when (lang) {
                AppLanguage.TAMIL -> "பிறந்த இடம்:"
                AppLanguage.HINDI -> "जन्म स्थान:"
                AppLanguage.ENGLISH -> "Birth Place:"
            }
            val tobLabel = when (lang) {
                AppLanguage.TAMIL -> "பிறந்த நேரம்:"
                AppLanguage.HINDI -> "जन्म समय:"
                AppLanguage.ENGLISH -> "Time of Birth:"
            }
            val lagnaLabel = when (lang) {
                AppLanguage.TAMIL -> "லக்னம்:"
                AppLanguage.HINDI -> "लग्न:"
                AppLanguage.ENGLISH -> "Ascendant (Lagna):"
            }

            val lagnaVal = when (lang) {
                AppLanguage.TAMIL -> result.lagnaRasi.nameTa
                AppLanguage.HINDI -> result.lagnaRasi.nameHi
                AppLanguage.ENGLISH -> result.lagnaRasi.nameEn
            }

            // Row 1 - Col 1 & 2
            drawLabelValue(canvas, nameLabel, babyDisplayName, col1X, r1Y, 100f)
            drawLabelValue(canvas, genderTag, genderLabel, col2X, r1Y, 110f)

            // Row 2 - Col 1 & 2
            drawLabelValue(canvas, dobLabel, result.dob.format(dateFmt), col1X, r2Y, 100f)
            val birthPlaceTrimmed = result.birthPlace.take(30)
            drawLabelValue(canvas, placeLabel, birthPlaceTrimmed, col2X, r2Y, 110f)

            // Row 3 - Col 1 & 2
            drawLabelValue(canvas, tobLabel, result.tob.format(timeFmt), col1X, r3Y, 100f)
            drawLabelValue(canvas, lagnaLabel, lagnaVal, col2X, r3Y, 110f)

            currentY = infoTop + infoHeight + 8f

            // 5. Astrological Star & Moon Sign Banner (Pure Single Language)
            val starBannerHeight = 44f
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + starBannerHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_GOLD
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + starBannerHeight), 6f, 6f, borderPaint)

            val starName = when (lang) {
                AppLanguage.TAMIL -> result.nakshatraLetters.nakshatraNameTa
                AppLanguage.HINDI -> result.nakshatraLetters.nakshatraNameHi
                AppLanguage.ENGLISH -> result.nakshatraLetters.nakshatraNameEn
            }
            val rasiName = when (lang) {
                AppLanguage.TAMIL -> result.chandraRasi.nameTa
                AppLanguage.HINDI -> result.chandraRasi.nameHi
                AppLanguage.ENGLISH -> result.chandraRasi.nameEn
            }
            val padaStr = when (lang) {
                AppLanguage.TAMIL -> "${result.janmaPada}-ஆம் பாதம்"
                AppLanguage.HINDI -> "चरण ${result.janmaPada}"
                AppLanguage.ENGLISH -> "Pada ${result.janmaPada}"
            }
            val lordName = when (lang) {
                AppLanguage.TAMIL -> result.nakshatraLetters.lordTa
                AppLanguage.HINDI -> result.nakshatraLetters.lordHi
                AppLanguage.ENGLISH -> result.nakshatraLetters.lordEn
            }
            val deityName = when (lang) {
                AppLanguage.TAMIL -> result.nakshatraLetters.deityTa
                AppLanguage.HINDI -> result.nakshatraLetters.deityHi
                AppLanguage.ENGLISH -> result.nakshatraLetters.deityEn
            }

            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 11.5f
            canvas.drawText("⭐ $starName • $padaStr", (PAGE_WIDTH / 2).toFloat(), currentY + 16f, textPaint)

            textPaint.textSize = 9f
            textPaint.color = COLOR_DARK_TEXT
            val attrText = when (lang) {
                AppLanguage.TAMIL -> "🌙 ராசி: $rasiName  •  நட்சத்திர அதிபதி: $lordName  •  அதிதேவதை: $deityName"
                AppLanguage.HINDI -> "🌙 राशि: $rasiName  •  नक्षत्र स्वामी: $lordName  •  अधिष्ठाता देवता: $deityName"
                AppLanguage.ENGLISH -> "🌙 Moon Sign: $rasiName  •  Planetary Lord: $lordName  •  Presiding Deity: $deityName"
            }
            canvas.drawText(attrText, (PAGE_WIDTH / 2).toFloat(), currentY + 33f, textPaint)

            currentY += starBannerHeight + 8f

            // 6. Highlight: Primary Auspicious Starting Letter for Exact Birth Pada
            val medHeight = 84f
            fillPaint.color = Color.WHITE
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + medHeight), 8f, 8f, fillPaint)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 1.6f
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + medHeight), 8f, 8f, borderPaint)

            // Inner title strip
            fillPaint.color = COLOR_MAROON
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + 20f), 8f, 8f, fillPaint)
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = Color.WHITE
            textPaint.textSize = 9.5f

            val primaryTitle = when (lang) {
                AppLanguage.TAMIL -> "✨ ஜென்ம பாதத்திற்கான முதன்மை சுப ஆரம்ப அட்சரம் ($padaStr) ✨"
                AppLanguage.HINDI -> "✨ जन्म चरण का मुख्य शुभ नामकरण अक्षर ($padaStr) ✨"
                AppLanguage.ENGLISH -> "✨ PRIMARY AUSPICIOUS INITIAL LETTER ($padaStr) ✨"
            }
            canvas.drawText(primaryTitle, (PAGE_WIDTH / 2).toFloat(), currentY + 14f, textPaint)

            val primaryLetter = when (lang) {
                AppLanguage.TAMIL -> result.primaryPadaInfo.letterTa
                AppLanguage.HINDI -> result.primaryPadaInfo.letterHi
                AppLanguage.ENGLISH -> result.primaryPadaInfo.letterEn
            }

            // Large letter in the selected script
            textPaint.textSize = 28f
            textPaint.color = COLOR_MAROON
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(primaryLetter, (PAGE_WIDTH / 2).toFloat(), currentY + 48f, textPaint)

            // Explanatory note
            textPaint.textSize = 8f
            textPaint.color = COLOR_MUTED_TEXT
            textPaint.typeface = Typeface.DEFAULT
            val padaNote = when (lang) {
                AppLanguage.TAMIL -> "ஜோதிட சாஸ்திரப்படி $starName $padaStr-ல் பிறந்த குழந்தைக்கு '$primaryLetter' என்ற சுப அட்சரத்தில் பெயர் சூட்டுவது சகல சௌபாக்கியங்களையும் தரும்."
                AppLanguage.HINDI -> "वैदिक ज्योतिष अनुसार $starName के $padaStr में जन्मे शिशु के लिए '$primaryLetter' अक्षर से नामकरण अत्यंत शुभ एवं कल्याणकारी है।"
                AppLanguage.ENGLISH -> "As per Vedic Astrology, for a child born in $starName $padaStr, naming with initial syllable '$primaryLetter' brings longevity and wisdom."
            }
            canvas.drawText(padaNote, (PAGE_WIDTH / 2).toFloat(), currentY + 68f, textPaint)

            currentY += medHeight + 8f

            // 7. Grid for All 4 Padas of the Star
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            val padasSectionTitle = when (lang) {
                AppLanguage.TAMIL -> "நட்சத்திரத்தின் 4 பாத சுப ஆரம்ப எழுத்துக்கள் ($starName):"
                AppLanguage.HINDI -> "नक्षत्र के सभी 4 चरणों के शुभ अक्षर ($starName):"
                AppLanguage.ENGLISH -> "Starting Syllables for All 4 Padas of $starName:"
            }
            canvas.drawText(padasSectionTitle, boxLeft, currentY + 8f, textPaint)
            currentY += 14f

            val padaCardWidth = (contentWidth - 18f) / 4f
            val padaCardHeight = 76f

            result.nakshatraLetters.padas.forEachIndexed { idx, pada ->
                val pLeft = boxLeft + idx * (padaCardWidth + 6f)
                val pRight = pLeft + padaCardWidth
                val isCurrentPada = (pada.padaNumber == result.janmaPada)

                fillPaint.color = if (isCurrentPada) COLOR_LIGHT_GOLD else COLOR_CARD_BG
                canvas.drawRoundRect(RectF(pLeft, currentY, pRight, currentY + padaCardHeight), 6f, 6f, fillPaint)

                borderPaint.color = if (isCurrentPada) COLOR_MAROON else COLOR_BORDER
                borderPaint.strokeWidth = if (isCurrentPada) 1.6f else 0.8f
                canvas.drawRoundRect(RectF(pLeft, currentY, pRight, currentY + padaCardHeight), 6f, 6f, borderPaint)

                textPaint.textAlign = Paint.Align.CENTER
                textPaint.textSize = 8.5f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = if (isCurrentPada) COLOR_MAROON else COLOR_MUTED_TEXT

                val cardPadaTitle = when (lang) {
                    AppLanguage.TAMIL -> "${pada.padaNumber}-ஆம் பாதம்" + if (isCurrentPada) " ★ (ஜென்மம்)" else ""
                    AppLanguage.HINDI -> "चरण ${pada.padaNumber}" + if (isCurrentPada) " ★ (जन्म)" else ""
                    AppLanguage.ENGLISH -> "Pada ${pada.padaNumber}" + if (isCurrentPada) " ★ (Birth)" else ""
                }
                canvas.drawText(cardPadaTitle, pLeft + padaCardWidth / 2f, currentY + 14f, textPaint)

                // Letter
                val cardLetter = when (lang) {
                    AppLanguage.TAMIL -> pada.letterTa
                    AppLanguage.HINDI -> pada.letterHi
                    AppLanguage.ENGLISH -> pada.letterEn
                }
                textPaint.textSize = 20f
                textPaint.color = COLOR_MAROON
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText(cardLetter, pLeft + padaCardWidth / 2f, currentY + 40f, textPaint)

                // Rasi of Pada
                val padaRasiName = when (lang) {
                    AppLanguage.TAMIL -> pada.rasiTa
                    AppLanguage.HINDI -> pada.rasiHi
                    AppLanguage.ENGLISH -> pada.rasiEn
                }
                textPaint.textSize = 8f
                textPaint.color = COLOR_MUTED_TEXT
                textPaint.typeface = Typeface.DEFAULT
                canvas.drawText(padaRasiName, pLeft + padaCardWidth / 2f, currentY + 62f, textPaint)
            }

            currentY += padaCardHeight + 8f

            // 8. Vedic Astrological Naming Rules Card
            val rulesTop = currentY
            val rulesHeight = 94f
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas.drawRoundRect(RectF(boxLeft, rulesTop, boxRight, rulesTop + rulesHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(boxLeft, rulesTop, boxRight, rulesTop + rulesHeight), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 9.5f
            val rulesTitle = when (lang) {
                AppLanguage.TAMIL -> "📜 நாமகரண சாஸ்திர விதிகள் & பொருத்தங்கள்:"
                AppLanguage.HINDI -> "📜 नामकरण शास्त्रीय नियम एवं विशेषताएं:"
                AppLanguage.ENGLISH -> "📜 VEDIC NAMING PRINCIPLES & ATTRIBUTES:"
            }
            canvas.drawText(rulesTitle, boxLeft + 10f, rulesTop + 14f, textPaint)

            val textPaintWrapped = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 7.8f
                color = COLOR_DARK_TEXT
                typeface = Typeface.DEFAULT
            }

            val ganaVal = when (lang) {
                AppLanguage.TAMIL -> result.nakshatraLetters.ganaTa
                AppLanguage.HINDI -> result.nakshatraLetters.ganaHi
                AppLanguage.ENGLISH -> result.nakshatraLetters.ganaEn
            }
            val yoniVal = when (lang) {
                AppLanguage.TAMIL -> result.nakshatraLetters.yoniTa
                AppLanguage.HINDI -> result.nakshatraLetters.yoniHi
                AppLanguage.ENGLISH -> result.nakshatraLetters.yoniEn
            }
            val rajjuVal = when (lang) {
                AppLanguage.TAMIL -> result.nakshatraLetters.rajjuTa
                AppLanguage.HINDI -> result.nakshatraLetters.rajjuHi
                AppLanguage.ENGLISH -> result.nakshatraLetters.rajjuEn
            }

            val rulesText = when (lang) {
                AppLanguage.TAMIL -> """
                    1. ஜென்ம நட்சத்திர பாத ஆரம்ப ஒலி அலைகள் குழந்தையின் வாழ்நாள் முழுவதும் நேர்மறை ஆற்றலையும் தெய்வீக பாதுகாப்பையும் வழங்கும்.
                    2. பஞ்சாங்க பொருத்தங்கள்: கணம்: $ganaVal | யோனி: $yoniVal | ரஜ்ஜு: $rajjuVal
                    3. மங்கலகரமான, நற்பண்புகளையும் இறை அருளையும் குறிக்கும் சுப நாமங்களை சூட்டுவது உத்தமம்.
                """.trimIndent()
                AppLanguage.HINDI -> """
                    1. जन्म नक्षत्र चरण के शुभ ध्वनि अक्षर से नाम रखने से बालक को दैवीय रक्षा, उत्तम स्वास्थ्य एवं यश प्राप्त होता है।
                    2. ज्योतिषीय विशेषताएं: गण: $ganaVal | योनि: $yoniVal | रज्जु: $rajjuVal
                    3. सुंदर, अर्थपूर्ण एवं सकारात्मक ऊर्जा वाले नाम का चयन करना अत्यंत शुभ माना गया है।
                """.trimIndent()
                AppLanguage.ENGLISH -> """
                    1. Initial Syllable: Starting the name with the resonant vibration of birth nakshatra pada aligns cosmic planetary energies.
                    2. Astrological Attributes: Gana: $ganaVal | Yoni: $yoniVal | Rajju: $rajjuVal
                    3. Meaningful Identity: Choose a positive, uplifting name carrying noble virtues and divine blessings.
                """.trimIndent()
            }

            drawMultilineText(
                canvas = canvas,
                text = rulesText,
                x = boxLeft + 10f,
                y = rulesTop + 22f,
                width = contentWidth - 20,
                textPaint = textPaintWrapped
            )

            currentY = rulesTop + rulesHeight + 8f

            // 9. Temple Blessings & Signature Card (Bottom Box)
            val bottomTop = currentY
            val bottomHeight = (PAGE_HEIGHT - 26f) - bottomTop
            fillPaint.color = Color.WHITE
            canvas.drawRoundRect(RectF(boxLeft, bottomTop, boxRight, bottomTop + bottomHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 1.2f
            canvas.drawRoundRect(RectF(boxLeft, bottomTop, boxRight, bottomTop + bottomHeight), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 9.5f
            val blessingsHeader = when (lang) {
                AppLanguage.TAMIL -> "🕉️ ஓம் சரவணபவ • ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருவருள் ஆசிகள் 🕉️"
                AppLanguage.HINDI -> "॥ ॐ नमः शिवाय ॥ श्री शिव सुब्रमण्य स्वामी शुभाशीर्वाद"
                AppLanguage.ENGLISH -> "🕉️ OM SARAVANABHAVA • SRI SIVA SUBRAMANIYA SWAMI TEMPLE BLESSINGS 🕉️"
            }
            canvas.drawText(blessingsHeader, (PAGE_WIDTH / 2).toFloat(), bottomTop + 15f, textPaint)

            val blessingPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 7.8f
                color = COLOR_DARK_TEXT
                typeface = Typeface.DEFAULT
            }

            val blessingText = when (lang) {
                AppLanguage.TAMIL -> "நாடி ஸ்ரீ சிவ சுப்பிரமணிய சுவாமியின் திருவருளாலும் தலைமை குருக்கள் மோகன் அவர்களின் ஆசிகளாலும் குழந்தை சகல சௌபாக்கியங்களும் பெற்று நீடூழி வாழ ஆசீர்வதிக்கிறோம்."
                AppLanguage.HINDI -> "भगवान श्री शिव सुब्रमण्य स्वामी की असीम कृपा से शिशु को दीर्घायु, उत्तम स्वास्थ्य, तेजस्विता एवं सर्व सुख-समृद्धि की प्राप्ति हो।"
                AppLanguage.ENGLISH -> "May Lord Sri Siva Subramaniya Swami shower the newborn child with divine grace, sound health, long life, supreme wisdom, and prosperous fortune."
            }

            drawMultilineText(
                canvas = canvas,
                text = blessingText,
                x = boxLeft + 12f,
                y = bottomTop + 22f,
                width = contentWidth - 24,
                textPaint = blessingPaint
            )

            // Seal & Date
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 8f
            textPaint.color = COLOR_MUTED_TEXT
            textPaint.typeface = Typeface.DEFAULT
            val datePrefix = when (lang) {
                AppLanguage.TAMIL -> "தேதி:"
                AppLanguage.HINDI -> "तिथि:"
                AppLanguage.ENGLISH -> "Date:"
            }
            canvas.drawText("$datePrefix ${java.time.LocalDate.now().format(dateFmt)}", boxLeft + 12f, bottomTop + bottomHeight - 8f, textPaint)

            textPaint.textAlign = Paint.Align.RIGHT
            val priestSign = when (lang) {
                AppLanguage.TAMIL -> "பிரம்மஸ்ரீ மோகன் குருக்கள் • தலைமை குருக்கள், நாடி திருக்கோயில் (+6797607465)"
                AppLanguage.HINDI -> "ब्रह्मश्री मोहन गुरुक्कल • मुख्य पुजारी, नादी मंदिर (+6797607465)"
                AppLanguage.ENGLISH -> "Brahmasri Mohan Gurukkal • Head Priest, Nadi Temple, Fiji (+6797607465)"
            }
            canvas.drawText(priestSign, boxRight - 12f, bottomTop + bottomHeight - 8f, textPaint)

            pdfDocument.finishPage(page)

            // Write File
            val outputDir = File(context.cacheDir, "baby_naming_certificates").apply { mkdirs() }
            val cleanBabyName = result.babyName.replace(" ", "_").ifBlank { "Newborn_Baby" }
            val langCode = lang.name.lowercase()
            val fileName = "Baby_Naming_Letters_${cleanBabyName}_${langCode}_${System.currentTimeMillis()}.pdf"
            val file = File(outputDir, fileName)
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun drawCornerAccent(canvas: Canvas, fillPaint: Paint, x: Float, y: Float) {
        fillPaint.color = COLOR_GOLD
        canvas.drawCircle(x, y, 3f, fillPaint)
    }

    private fun drawLabelValue(canvas: Canvas, label: String, value: String, x: Float, y: Float, labelWidth: Float) {
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_MAROON
            textSize = 8.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.LEFT
        }
        val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_DARK_TEXT
            textSize = 8.5f
            typeface = Typeface.DEFAULT
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText(label, x, y, labelPaint)
        canvas.drawText(value, x + labelWidth, y, valuePaint)
    }

    @Suppress("DEPRECATION")
    private fun drawMultilineText(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        width: Int,
        textPaint: TextPaint
    ) {
        val layout = StaticLayout(
            text,
            textPaint,
            width,
            Layout.Alignment.ALIGN_NORMAL,
            1.15f,
            0.0f,
            false
        )
        canvas.save()
        canvas.translate(x, y)
        layout.draw(canvas)
        canvas.restore()
    }

    fun shareBabyNamingPdf(context: Context, file: File, babyName: String, lang: AppLanguage = AppLanguage.ENGLISH) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val displayName = babyName.ifBlank {
                when (lang) {
                    AppLanguage.TAMIL -> "குழந்தை"
                    AppLanguage.HINDI -> "शिशु"
                    AppLanguage.ENGLISH -> "Newborn Baby"
                }
            }
            val subject = when (lang) {
                AppLanguage.TAMIL -> "வேத நாமகரண சான்றிதழ் - $displayName"
                AppLanguage.HINDI -> "वैदिक नामकरण प्रमाण पत्र - $displayName"
                AppLanguage.ENGLISH -> "Vedic Baby Naming Certificate - $displayName"
            }
            val textBody = when (lang) {
                AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி • வேத நாமகரண & நட்சத்திர சுப ஆரம்ப அட்சர சான்றிதழ் PDF"
                AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी मंदिर, नादी, फिजी • वैदिक नामकरण एवं नक्षत्र शुभ अक्षर प्रमाण पत्र PDF"
                AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Temple, Nadi, Fiji • Vedic Baby Naming & Nakshatra Starting Letters Certificate PDF"
            }
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, textBody)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Baby Naming Certificate PDF"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
