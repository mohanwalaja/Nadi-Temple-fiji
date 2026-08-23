package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
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
        lang: AppLanguage
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

            // 1. Background
            fillPaint.color = Color.WHITE
            canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), fillPaint)

            // 2. Ornate Double Borders (Maroon & Gold)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 2.5f
            canvas.drawRoundRect(RectF(18f, 18f, (PAGE_WIDTH - 18).toFloat(), (PAGE_HEIGHT - 18).toFloat()), 10f, 10f, borderPaint)

            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(23f, 23f, (PAGE_WIDTH - 23).toFloat(), (PAGE_HEIGHT - 23).toFloat()), 8f, 8f, borderPaint)

            // Ornate Corner accents
            drawCornerAccent(canvas, borderPaint, fillPaint, 24f, 24f)
            drawCornerAccent(canvas, borderPaint, fillPaint, (PAGE_WIDTH - 24).toFloat(), 24f)
            drawCornerAccent(canvas, borderPaint, fillPaint, 24f, (PAGE_HEIGHT - 24).toFloat())
            drawCornerAccent(canvas, borderPaint, fillPaint, (PAGE_WIDTH - 24).toFloat(), (PAGE_HEIGHT - 24).toFloat())

            var y = 44f

            // 3. Temple Header Banner
            fillPaint.color = COLOR_MAROON
            canvas.drawRoundRect(RectF(32f, y, (PAGE_WIDTH - 32).toFloat(), y + 68f), 8f, 8f, fillPaint)

            headerPaint.textAlign = Paint.Align.CENTER
            headerPaint.color = Color.WHITE
            headerPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

            headerPaint.textSize = 12.5f
            canvas.drawText("🕉️ ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில் 🕉️", (PAGE_WIDTH / 2).toFloat(), y + 20f, headerPaint)

            headerPaint.textSize = 10.5f
            headerPaint.color = COLOR_GOLD
            canvas.drawText("SRI SIVA SUBRAMANIYA SWAMI KOVIL • NADI, FIJI ISLANDS", (PAGE_WIDTH / 2).toFloat(), y + 36f, headerPaint)

            headerPaint.textSize = 11.5f
            headerPaint.color = Color.WHITE
            val certTitle = when (lang) {
                AppLanguage.TAMIL -> "குழந்தை நாமகரண சுப ஆரம்ப அட்சர சான்றிதழ்"
                AppLanguage.HINDI -> "शिशु नामकरण शुभ नक्षत्र अक्षर प्रमाण पत्र"
                AppLanguage.ENGLISH -> "VEDIC BABY NAMING & NAKSHATRA INITIAL LETTERS CERTIFICATE"
            }
            canvas.drawText(certTitle, (PAGE_WIDTH / 2).toFloat(), y + 54f, headerPaint)

            y += 78f

            // 4. Baby & Birth Information Card
            val boxLeft = 32f
            val boxRight = (PAGE_WIDTH - 32).toFloat()
            val infoTop = y
            val infoHeight = 68f

            fillPaint.color = COLOR_CARD_BG
            canvas.drawRoundRect(RectF(boxLeft, infoTop, boxRight, infoTop + infoHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(boxLeft, infoTop, boxRight, infoTop + infoHeight), 6f, 6f, borderPaint)

            val dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
            val timeFmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

            val genderLabel = if (result.gender.equals("M", ignoreCase = true)) {
                when (lang) { AppLanguage.TAMIL -> "ஆண் குழந்தை (Baby Boy)"; AppLanguage.HINDI -> "बालक (Boy)"; AppLanguage.ENGLISH -> "Baby Boy" }
            } else {
                when (lang) { AppLanguage.TAMIL -> "பெண் குழந்தை (Baby Girl)"; AppLanguage.HINDI -> "बालिका (Girl)"; AppLanguage.ENGLISH -> "Baby Girl" }
            }

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 9.5f

            // Left Col
            val r1Y = infoTop + 18f
            val r2Y = infoTop + 36f
            val r3Y = infoTop + 54f

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "குழந்தை பெயர்:"; AppLanguage.HINDI -> "शिशु नाम:"; AppLanguage.ENGLISH -> "Baby Name:" }, boxLeft + 12f, r1Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.babyName.ifBlank { "செல்வன் / செல்வி (Child)" }, boxLeft + 110f, r1Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "பிறந்த தேதி (DOB):"; AppLanguage.HINDI -> "जन्म तिथि (DOB):"; AppLanguage.ENGLISH -> "Date of Birth:" }, boxLeft + 12f, r2Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.dob.format(dateFmt), boxLeft + 110f, r2Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "பிறந்த நேரம் (TOB):"; AppLanguage.HINDI -> "जन्म समय (TOB):"; AppLanguage.ENGLISH -> "Time of Birth:" }, boxLeft + 12f, r3Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.tob.format(timeFmt), boxLeft + 110f, r3Y, textPaint)

            // Right Col
            val col2X = boxLeft + 265f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "பாலினம் (Gender):"; AppLanguage.HINDI -> "लिंग (Gender):"; AppLanguage.ENGLISH -> "Gender:" }, col2X, r1Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(genderLabel, col2X + 95f, r1Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "பிறந்த இடம் (Place):"; AppLanguage.HINDI -> "जन्म स्थान:"; AppLanguage.ENGLISH -> "Birth Place:" }, col2X, r2Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.birthPlace, col2X + 95f, r2Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "லக்னம் (Lagna):"; AppLanguage.HINDI -> "लग्न (Lagna):"; AppLanguage.ENGLISH -> "Ascendant:" }, col2X, r3Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.lagnaRasi.getName(lang), col2X + 95f, r3Y, textPaint)

            y = infoTop + infoHeight + 12f

            // 5. Astrological Star & Pada Particulars Banner (Gold & Maroon)
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 46f), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_GOLD
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 46f), 6f, 6f, borderPaint)

            val starName = result.nakshatraLetters.getName(lang)
            val rasiName = result.chandraRasi.getName(lang)
            val padaText = when (lang) {
                AppLanguage.TAMIL -> "${result.janmaPada}-ஆம் பாதம் (Pada ${result.janmaPada})"
                AppLanguage.HINDI -> "चरण ${result.janmaPada} (Pada ${result.janmaPada})"
                AppLanguage.ENGLISH -> "Pada ${result.janmaPada}"
            }

            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 12f
            canvas.drawText("⭐ $starName ($padaText)  •  🌙 $rasiName", (PAGE_WIDTH / 2).toFloat(), y + 20f, textPaint)

            textPaint.textSize = 9f
            textPaint.color = COLOR_MUTED_TEXT
            val attrText = when (lang) {
                AppLanguage.TAMIL -> "நட்சத்திர அதிபதி: ${result.nakshatraLetters.getLord(lang)}  |  அதிதேவதை: ${result.nakshatraLetters.getDeity(lang)}  |  கணம்: ${result.nakshatraLetters.getGana(lang)}"
                AppLanguage.HINDI -> "स्वामी: ${result.nakshatraLetters.getLord(lang)}  |  देवता: ${result.nakshatraLetters.getDeity(lang)}  |  गण: ${result.nakshatraLetters.getGana(lang)}"
                AppLanguage.ENGLISH -> "Star Lord: ${result.nakshatraLetters.getLord(lang)}  |  Deity: ${result.nakshatraLetters.getDeity(lang)}  |  Gana: ${result.nakshatraLetters.getGana(lang)}"
            }
            canvas.drawText(attrText, (PAGE_WIDTH / 2).toFloat(), y + 36f, textPaint)

            y += 56f

            // 6. Highlight: Primary Auspicious Starting Letter for Exact Birth Pada
            val medWidth = boxRight - boxLeft
            val medHeight = 82f
            fillPaint.color = Color.WHITE
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + medHeight), 8f, 8f, fillPaint)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 1.8f
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + medHeight), 8f, 8f, borderPaint)

            // Inner header
            fillPaint.color = COLOR_MAROON
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 22f), 8f, 8f, fillPaint)
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = Color.WHITE
            textPaint.textSize = 9.5f
            val primaryHeader = when (lang) {
                AppLanguage.TAMIL -> "✨ ஜென்ம பாதத்திற்குரிய முதன்மை சுப ஆரம்ப அட்சரம் (Primary Starting Letter)"
                AppLanguage.HINDI -> "✨ जन्म पाद के अनुसार मुख्य शुभ नामकरण अक्षर (Primary Initial Letter)"
                AppLanguage.ENGLISH -> "✨ PRIMARY AUSPICIOUS INITIAL LETTER FOR BIRTH PADA"
            }
            canvas.drawText(primaryHeader, (PAGE_WIDTH / 2).toFloat(), y + 15f, textPaint)

            // Large letter display
            val primaryLetterTa = result.primaryPadaInfo.letterTa
            val primaryLetterEn = result.primaryPadaInfo.letterEn
            val primaryLetterHi = result.primaryPadaInfo.letterHi

            textPaint.textSize = 28f
            textPaint.color = COLOR_MAROON
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            val mainLetterDisplay = when (lang) {
                AppLanguage.TAMIL -> "$primaryLetterTa  ($primaryLetterEn)"
                AppLanguage.HINDI -> "$primaryLetterHi  ($primaryLetterEn)"
                AppLanguage.ENGLISH -> "$primaryLetterEn  ($primaryLetterTa)"
            }
            canvas.drawText(mainLetterDisplay, (PAGE_WIDTH / 2).toFloat(), y + 54f, textPaint)

            textPaint.textSize = 9.5f
            textPaint.color = COLOR_MUTED_TEXT
            textPaint.typeface = Typeface.DEFAULT
            val padaNote = when (lang) {
                AppLanguage.TAMIL -> "தமிழ் பஞ்சாங்க விதிப்படி $starName ${result.janmaPada}-ஆம் பாதத்தில் பிறந்த குழந்தைக்கு இப்பெயர் எழுத்து சர்வ மங்கலங்களையும் தரும்."
                AppLanguage.HINDI -> "पारंपरिक पंचांग अनुसार $starName चरण ${result.janmaPada} में जन्मे शिशु के लिए यह अक्षर सर्वकल्याणकारी है।"
                AppLanguage.ENGLISH -> "As per Vedic Panchangam, naming with this syllable vibration brings supreme prosperity and longevity."
            }
            canvas.drawText(padaNote, (PAGE_WIDTH / 2).toFloat(), y + 72f, textPaint)

            y += medHeight + 14f

            // 7. Grid for All 4 Padas of the Nakshatra
            headerPaint.textAlign = Paint.Align.LEFT
            headerPaint.color = COLOR_MAROON
            headerPaint.textSize = 10.5f
            headerPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            val allPadasTitle = when (lang) {
                AppLanguage.TAMIL -> "நட்சத்திரத்தின் 4 பாதங்களுக்குரிய சுப எழுத்துக்கள் (All 4 Padas Letters):"
                AppLanguage.HINDI -> "नक्षत्र के सभी 4 चरणों के शुभ अक्षर:"
                AppLanguage.ENGLISH -> "Starting Syllables for All 4 Padas of $starName:"
            }
            canvas.drawText(allPadasTitle, boxLeft, y, headerPaint)
            y += 8f

            val padaCardWidth = (boxRight - boxLeft - 18f) / 4f
            val padaCardHeight = 62f

            result.nakshatraLetters.padas.forEachIndexed { idx, pada ->
                val pLeft = boxLeft + idx * (padaCardWidth + 6f)
                val pRight = pLeft + padaCardWidth
                val isCurrentPada = (pada.padaNumber == result.janmaPada)

                fillPaint.color = if (isCurrentPada) COLOR_LIGHT_GOLD else COLOR_CARD_BG
                canvas.drawRoundRect(RectF(pLeft, y, pRight, y + padaCardHeight), 6f, 6f, fillPaint)

                borderPaint.color = if (isCurrentPada) COLOR_MAROON else COLOR_BORDER
                borderPaint.strokeWidth = if (isCurrentPada) 1.5f else 0.8f
                canvas.drawRoundRect(RectF(pLeft, y, pRight, y + padaCardHeight), 6f, 6f, borderPaint)

                textPaint.textAlign = Paint.Align.CENTER
                textPaint.textSize = 8.5f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = if (isCurrentPada) COLOR_MAROON else COLOR_MUTED_TEXT
                val padaTitle = when (lang) {
                    AppLanguage.TAMIL -> "பாதம் ${pada.padaNumber}" + if (isCurrentPada) " (ஜென்மம்)" else ""
                    AppLanguage.HINDI -> "चरण ${pada.padaNumber}" + if (isCurrentPada) " (जन्म)" else ""
                    AppLanguage.ENGLISH -> "Pada ${pada.padaNumber}" + if (isCurrentPada) " ★" else ""
                }
                canvas.drawText(padaTitle, pLeft + padaCardWidth / 2f, y + 14f, textPaint)

                // Letter
                textPaint.textSize = 18f
                textPaint.color = COLOR_MAROON
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText(pada.letterTa, pLeft + padaCardWidth / 2f, y + 34f, textPaint)

                textPaint.textSize = 8.5f
                textPaint.color = COLOR_DARK_TEXT
                textPaint.typeface = Typeface.DEFAULT
                canvas.drawText(pada.letterEn, pLeft + padaCardWidth / 2f, y + 46f, textPaint)

                textPaint.textSize = 7.5f
                textPaint.color = COLOR_MUTED_TEXT
                canvas.drawText(when (lang) { AppLanguage.TAMIL -> pada.rasiTa; AppLanguage.HINDI -> pada.rasiHi; AppLanguage.ENGLISH -> pada.rasiEn }, pLeft + padaCardWidth / 2f, y + 56f, textPaint)
            }

            y += padaCardHeight + 14f

            // 8. Traditional Auspicious Baby Name Suggestions Box
            val namesBoxTop = y
            val namesBoxHeight = 110f

            fillPaint.color = COLOR_CARD_BG
            canvas.drawRoundRect(RectF(boxLeft, namesBoxTop, boxRight, namesBoxTop + namesBoxHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(boxLeft, namesBoxTop, boxRight, namesBoxTop + namesBoxHeight), 6f, 6f, borderPaint)

            // Header for names box
            fillPaint.color = COLOR_MAROON
            canvas.drawRoundRect(RectF(boxLeft, namesBoxTop, boxRight, namesBoxTop + 20f), 6f, 6f, fillPaint)
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = Color.WHITE
            textPaint.textSize = 9.5f
            val namesHeader = when (lang) {
                AppLanguage.TAMIL -> "📖 பாரம்பரிய சுப பெயர் பரிந்துரைகள் (Auspicious Name Suggestions):"
                AppLanguage.HINDI -> "📖 पारंपरिक शुभ नाम सुझाव (Traditional Name Suggestions):"
                AppLanguage.ENGLISH -> "📖 TRADITIONAL AUSPICIOUS NAME SUGGESTIONS:"
            }
            canvas.drawText(namesHeader, boxLeft + 12f, namesBoxTop + 14f, textPaint)

            val suggestions = result.suggestedNames.take(6)
            var nY = namesBoxTop + 34f
            val nCol1X = boxLeft + 12f
            val nCol2X = boxLeft + 265f

            textPaint.textSize = 8.5f
            suggestions.forEachIndexed { i, s ->
                val currentX = if (i % 2 == 0) nCol1X else nCol2X
                val lineY = if (i % 2 == 0) nY else nY

                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = COLOR_MAROON
                val bullet = "• ${s.nameTa} (${s.nameEn}): "
                canvas.drawText(bullet, currentX, lineY, textPaint)

                val prefixWidth = textPaint.measureText(bullet)
                textPaint.typeface = Typeface.DEFAULT
                textPaint.color = COLOR_DARK_TEXT
                val meaning = when (lang) { AppLanguage.TAMIL -> s.meaningTa; else -> s.meaningEn }
                val maxMeaning = if (meaning.length > 25) meaning.take(24) + "..." else meaning
                canvas.drawText(maxMeaning, currentX + prefixWidth, lineY, textPaint)

                if (i % 2 == 1) {
                    nY += 22f
                }
            }

            y = namesBoxTop + namesBoxHeight + 14f

            // 9. Vedic Astrological Rules & Significance
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 42f), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_GOLD
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 42f), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 8.5f
            canvas.drawText("📜 ஜோதிட சாஸ்திர முக்கியத்துவம் (Astrological Significance):", boxLeft + 10f, y + 14f, textPaint)

            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            textPaint.textSize = 8f
            val ruleText = when (lang) {
                AppLanguage.TAMIL -> "குழந்தையின் ஜென்ம நட்சத்திர பாத ஒலி அலைகள் வாழ்நாள் முழுவதும் நேர்மறை ஆற்றலையும் புகழையும் ஈர்க்கும்."
                AppLanguage.HINDI -> "जन्म नक्षत्र चरण का शुभ ध्वनि-कंपन शिशु के संपूर्ण जीवन में सकारात्मक ऊर्जा एवं उत्तम स्वास्थ्य प्रदान करता है।"
                AppLanguage.ENGLISH -> "Starting the child's name with the resonant Vedic syllable of the birth star brings divine protection and success."
            }
            canvas.drawText(ruleText, boxLeft + 10f, y + 30f, textPaint)

            y += 50f

            // 10. Temple Blessings & Signature Block
            fillPaint.color = Color.WHITE
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, (PAGE_HEIGHT - 32).toFloat()), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, (PAGE_HEIGHT - 32).toFloat()), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 9.5f
            canvas.drawText("🕉️ ஓம் சரவணபவ • கோயில் குருக்கள் ஆசிர்வாதம் 🕉️", (PAGE_WIDTH / 2).toFloat(), y + 18f, textPaint)

            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            textPaint.textSize = 8.5f
            val blessingMsg = when (lang) {
                AppLanguage.TAMIL -> "எல்லாம் வல்ல நாடி ஸ்ரீ சிவ சுப்பிரமணிய சுவாமியின் அருளால் குழந்தை சகல நலன்களும், ஆயுள், ஆரோக்கியம், கல்வி, செல்வமும் பெற்று வாழ ஆசிகள்."
                AppLanguage.HINDI -> "भगवान शिव सुब्रमण्यम स्वामी के आशीर्वाद से शिशु को दीर्घायु, उत्तम स्वास्थ्य, विद्या एवं समृद्धि प्राप्त हो।"
                AppLanguage.ENGLISH -> "May Lord Sri Siva Subramaniya Swami shower the child with abundant health, longevity, wisdom, and eternal prosperity."
            }
            canvas.drawText(blessingMsg, (PAGE_WIDTH / 2).toFloat(), y + 32f, textPaint)

            // Seal & Date
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 8f
            textPaint.color = COLOR_MUTED_TEXT
            canvas.drawText("தேதி (Date): ${java.time.LocalDate.now().format(dateFmt)}", boxLeft + 16f, (PAGE_HEIGHT - 40).toFloat(), textPaint)

            textPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText("தலைமை அர்ச்சகர் (Chief Priest) / நாடி திருக்கோயில், பிஜி", boxRight - 16f, (PAGE_HEIGHT - 40).toFloat(), textPaint)

            pdfDocument.finishPage(page)

            // Write File
            val outputDir = File(context.cacheDir, "baby_naming_certificates").apply { mkdirs() }
            val fileName = "Baby_Naming_Letters_${result.babyName.replace(" ", "_").ifBlank { "Baby" }}_${System.currentTimeMillis()}.pdf"
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

    private fun drawCornerAccent(canvas: android.graphics.Canvas, borderPaint: Paint, fillPaint: Paint, x: Float, y: Float) {
        fillPaint.color = COLOR_GOLD
        canvas.drawCircle(x, y, 3f, fillPaint)
    }

    fun shareBabyNamingPdf(context: Context, file: File, babyName: String) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "குழந்தை நாமகரண சுப அட்சர சான்றிதழ் - $babyName")
                putExtra(Intent.EXTRA_TEXT, "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில், நாடி - குழந்தை நாமகரண சுப ஆரம்ப அட்சர சான்றிதழ் PDF.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Baby Naming Certificate PDF"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
