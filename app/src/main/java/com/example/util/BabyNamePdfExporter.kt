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

            var y = 42f

            // 3. Temple Header Banner
            fillPaint.color = COLOR_MAROON
            canvas.drawRoundRect(RectF(32f, y, (PAGE_WIDTH - 32).toFloat(), y + 72f), 8f, 8f, fillPaint)

            headerPaint.textAlign = Paint.Align.CENTER
            headerPaint.color = Color.WHITE
            headerPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

            headerPaint.textSize = 13.5f
            canvas.drawText("🕉️ ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில் 🕉️", (PAGE_WIDTH / 2).toFloat(), y + 22f, headerPaint)

            headerPaint.textSize = 10.5f
            headerPaint.color = COLOR_GOLD
            canvas.drawText("SRI SIVA SUBRAMANIYA SWAMI KOVIL • NADI, FIJI ISLANDS", (PAGE_WIDTH / 2).toFloat(), y + 39f, headerPaint)

            headerPaint.textSize = 12f
            headerPaint.color = Color.WHITE
            val certTitle = when (lang) {
                AppLanguage.TAMIL -> "குழந்தை நாமகரண சுப ஆரம்ப அட்சர சான்றிதழ்"
                AppLanguage.HINDI -> "शिशु नामकरण शुभ नक्षत्र अक्षर प्रमाण पत्र"
                AppLanguage.ENGLISH -> "VEDIC BABY NAMING & NAKSHATRA INITIAL LETTERS CERTIFICATE"
            }
            canvas.drawText(certTitle, (PAGE_WIDTH / 2).toFloat(), y + 58f, headerPaint)

            y += 82f

            // 4. Baby & Birth Information Card
            val boxLeft = 32f
            val boxRight = (PAGE_WIDTH - 32).toFloat()
            val infoTop = y
            val infoHeight = 74f

            fillPaint.color = COLOR_CARD_BG
            canvas.drawRoundRect(RectF(boxLeft, infoTop, boxRight, infoTop + infoHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(boxLeft, infoTop, boxRight, infoTop + infoHeight), 6f, 6f, borderPaint)

            val dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
            val timeFmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

            val col1X = boxLeft + 16f
            val col2X = boxLeft + 270f
            val r1Y = infoTop + 20f
            val r2Y = infoTop + 40f
            val r3Y = infoTop + 60f

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 9.5f

            // Row 1
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "குழந்தை பெயர்:"; AppLanguage.HINDI -> "शिशु का नाम:"; AppLanguage.ENGLISH -> "Baby Name:" }, col1X, r1Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.babyName.ifBlank { "Baby" }, col1X + 105f, r1Y, textPaint)

            // Row 2
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "பிறந்த தேதி (DOB):"; AppLanguage.HINDI -> "जन्म तिथि:"; AppLanguage.ENGLISH -> "Date of Birth:" }, col1X, r2Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.dob.format(dateFmt), col1X + 105f, r2Y, textPaint)

            // Row 3
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "பிறந்த நேரம் (TOB):"; AppLanguage.HINDI -> "जन्म समय:"; AppLanguage.ENGLISH -> "Time of Birth:" }, col1X, r3Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.tob.format(timeFmt), col1X + 105f, r3Y, textPaint)

            // Col 2
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            val genderLabel = if (result.gender == "M") when (lang) { AppLanguage.TAMIL -> "ஆண் (Boy)"; AppLanguage.HINDI -> "बालक (Male)"; AppLanguage.ENGLISH -> "Boy" } else when (lang) { AppLanguage.TAMIL -> "பெண் (Girl)"; AppLanguage.HINDI -> "बालिका (Female)"; AppLanguage.ENGLISH -> "Girl" }
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "பாலினம் (Gender):"; AppLanguage.HINDI -> "लिंग:"; AppLanguage.ENGLISH -> "Gender:" }, col2X, r1Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(genderLabel, col2X + 100f, r1Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "பிறந்த இடம் (Place):"; AppLanguage.HINDI -> "जन्म स्थान:"; AppLanguage.ENGLISH -> "Birth Place:" }, col2X, r2Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.birthPlace, col2X + 100f, r2Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas.drawText(when (lang) { AppLanguage.TAMIL -> "லக்னம் (Lagna):"; AppLanguage.HINDI -> "लग्न (Lagna):"; AppLanguage.ENGLISH -> "Ascendant:" }, col2X, r3Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas.drawText(result.lagnaRasi.getName(lang), col2X + 100f, r3Y, textPaint)

            y = infoTop + infoHeight + 14f

            // 5. Astrological Star & Pada Particulars Banner (Gold & Maroon)
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 54f), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_GOLD
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 54f), 6f, 6f, borderPaint)

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
            textPaint.textSize = 13f
            canvas.drawText("⭐ $starName ($padaText)  •  🌙 $rasiName", (PAGE_WIDTH / 2).toFloat(), y + 22f, textPaint)

            textPaint.textSize = 9.5f
            textPaint.color = COLOR_MUTED_TEXT
            val attrText = when (lang) {
                AppLanguage.TAMIL -> "நட்சத்திர அதிபதி: ${result.nakshatraLetters.getLord(lang)}  |  அதிதேவதை: ${result.nakshatraLetters.getDeity(lang)}  |  கணம்: ${result.nakshatraLetters.getGana(lang)}"
                AppLanguage.HINDI -> "स्वामी: ${result.nakshatraLetters.getLord(lang)}  |  देवता: ${result.nakshatraLetters.getDeity(lang)}  |  गण: ${result.nakshatraLetters.getGana(lang)}"
                AppLanguage.ENGLISH -> "Star Lord: ${result.nakshatraLetters.getLord(lang)}  |  Deity: ${result.nakshatraLetters.getDeity(lang)}  |  Gana: ${result.nakshatraLetters.getGana(lang)}"
            }
            canvas.drawText(attrText, (PAGE_WIDTH / 2).toFloat(), y + 42f, textPaint)

            y += 66f

            // 6. Highlight: Primary Auspicious Starting Letter for Exact Birth Pada
            val medHeight = 96f
            fillPaint.color = Color.WHITE
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + medHeight), 8f, 8f, fillPaint)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 1.8f
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + medHeight), 8f, 8f, borderPaint)

            // Inner header
            fillPaint.color = COLOR_MAROON
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 24f), 8f, 8f, fillPaint)
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = Color.WHITE
            textPaint.textSize = 10.5f
            val primaryHeader = when (lang) {
                AppLanguage.TAMIL -> "✨ ஜென்ம பாதத்திற்குரிய முதன்மை சுப ஆரம்ப அட்சரம் (Primary Starting Letter)"
                AppLanguage.HINDI -> "✨ जन्म पाद के अनुसार मुख्य शुभ नामकरण अक्षर (Primary Initial Letter)"
                AppLanguage.ENGLISH -> "✨ PRIMARY AUSPICIOUS INITIAL LETTER FOR BIRTH PADA"
            }
            canvas.drawText(primaryHeader, (PAGE_WIDTH / 2).toFloat(), y + 16f, textPaint)

            // Large letter display
            val primaryLetterTa = result.primaryPadaInfo.letterTa
            val primaryLetterEn = result.primaryPadaInfo.letterEn
            val primaryLetterHi = result.primaryPadaInfo.letterHi

            textPaint.textSize = 32f
            textPaint.color = COLOR_MAROON
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            val mainLetterDisplay = when (lang) {
                AppLanguage.TAMIL -> "$primaryLetterTa  ($primaryLetterEn)"
                AppLanguage.HINDI -> "$primaryLetterHi  ($primaryLetterEn)"
                AppLanguage.ENGLISH -> "$primaryLetterEn  ($primaryLetterTa)"
            }
            canvas.drawText(mainLetterDisplay, (PAGE_WIDTH / 2).toFloat(), y + 60f, textPaint)

            textPaint.textSize = 9.5f
            textPaint.color = COLOR_MUTED_TEXT
            textPaint.typeface = Typeface.DEFAULT
            val padaNote = when (lang) {
                AppLanguage.TAMIL -> "தமிழ் பஞ்சாங்க விதிப்படி $starName ${result.janmaPada}-ஆம் பாதத்தில் பிறந்த குழந்தைக்கு இப்பெயர் எழுத்து சர்வ மங்கலங்களையும் தரும்."
                AppLanguage.HINDI -> "पारंपरिक पंचांग अनुसार $starName चरण ${result.janmaPada} में जन्मे शिशु के लिए यह अक्षर सर्वकल्याणकारी है।"
                AppLanguage.ENGLISH -> "As per Vedic Panchangam, naming with this syllable vibration brings supreme prosperity and longevity."
            }
            canvas.drawText(padaNote, (PAGE_WIDTH / 2).toFloat(), y + 84f, textPaint)

            y += medHeight + 18f

            // 7. Grid for All 4 Padas of the Nakshatra
            headerPaint.textAlign = Paint.Align.LEFT
            headerPaint.color = COLOR_MAROON
            headerPaint.textSize = 11f
            headerPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            val allPadasTitle = when (lang) {
                AppLanguage.TAMIL -> "நட்சத்திரத்தின் 4 பாதங்களுக்குரிய சுப எழுத்துக்கள் (All 4 Padas Letters):"
                AppLanguage.HINDI -> "नक्षत्र के सभी 4 चरणों के शुभ अक्षर:"
                AppLanguage.ENGLISH -> "Starting Syllables for All 4 Padas of $starName:"
            }
            canvas.drawText(allPadasTitle, boxLeft, y, headerPaint)
            y += 12f

            val padaCardWidth = (boxRight - boxLeft - 18f) / 4f
            val padaCardHeight = 84f

            result.nakshatraLetters.padas.forEachIndexed { idx, pada ->
                val pLeft = boxLeft + idx * (padaCardWidth + 6f)
                val pRight = pLeft + padaCardWidth
                val isCurrentPada = (pada.padaNumber == result.janmaPada)

                fillPaint.color = if (isCurrentPada) COLOR_LIGHT_GOLD else COLOR_CARD_BG
                canvas.drawRoundRect(RectF(pLeft, y, pRight, y + padaCardHeight), 6f, 6f, fillPaint)

                borderPaint.color = if (isCurrentPada) COLOR_MAROON else COLOR_BORDER
                borderPaint.strokeWidth = if (isCurrentPada) 1.8f else 0.8f
                canvas.drawRoundRect(RectF(pLeft, y, pRight, y + padaCardHeight), 6f, 6f, borderPaint)

                textPaint.textAlign = Paint.Align.CENTER
                textPaint.textSize = 9.5f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = if (isCurrentPada) COLOR_MAROON else COLOR_MUTED_TEXT
                val padaTitle = when (lang) {
                    AppLanguage.TAMIL -> "பாதம் ${pada.padaNumber}" + if (isCurrentPada) " (ஜென்மம்)" else ""
                    AppLanguage.HINDI -> "चरण ${pada.padaNumber}" + if (isCurrentPada) " (जन्म)" else ""
                    AppLanguage.ENGLISH -> "Pada ${pada.padaNumber}" + if (isCurrentPada) " ★" else ""
                }
                canvas.drawText(padaTitle, pLeft + padaCardWidth / 2f, y + 16f, textPaint)

                // Letter
                textPaint.textSize = 22f
                textPaint.color = COLOR_MAROON
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText(pada.letterTa, pLeft + padaCardWidth / 2f, y + 44f, textPaint)

                textPaint.textSize = 10f
                textPaint.color = COLOR_DARK_TEXT
                textPaint.typeface = Typeface.DEFAULT
                canvas.drawText(pada.letterEn, pLeft + padaCardWidth / 2f, y + 60f, textPaint)

                textPaint.textSize = 8.5f
                textPaint.color = COLOR_MUTED_TEXT
                canvas.drawText(when (lang) { AppLanguage.TAMIL -> pada.rasiTa; AppLanguage.HINDI -> pada.rasiHi; AppLanguage.ENGLISH -> pada.rasiEn }, pLeft + padaCardWidth / 2f, y + 74f, textPaint)
            }

            y += padaCardHeight + 20f

            // 8. Vedic Astrological Rules & Significance Box
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 68f), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, y + 68f), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 10f
            canvas.drawText("📜 வேத ஜோதிட நாமகரண விதிமுறைகள் (Vedic Naming Principles):", boxLeft + 12f, y + 18f, textPaint)

            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            textPaint.textSize = 8.5f
            val ruleText1 = when (lang) {
                AppLanguage.TAMIL -> "1. குழந்தையின் ஜென்ம நட்சத்திர பாத ஒலி அலைகள் வாழ்நாள் முழுவதும் நேர்மறை ஆற்றலையும் புகழையும் ஈர்க்கும்."
                AppLanguage.HINDI -> "1. जन्म नक्षत्र चरण का शुभ ध्वनि-कंपन शिशु के संपूर्ण जीवन में सकारात्मक ऊर्जा एवं उत्तम स्वास्थ्य प्रदान करता है।"
                AppLanguage.ENGLISH -> "1. Starting the child's name with the resonant Vedic syllable of the birth star brings divine protection and success."
            }
            val ruleText2 = when (lang) {
                AppLanguage.TAMIL -> "2. யோனி: ${result.nakshatraLetters.getYoni(lang)}  |  ரஜ்ஜு: ${result.nakshatraLetters.getRajju(lang)}  |  தேவதை அருள் பெற்ற பெயர்களைத் தேர்ந்தெடுக்கவும்."
                AppLanguage.HINDI -> "2. योनि: ${result.nakshatraLetters.getYoni(lang)}  |  रज्जु: ${result.nakshatraLetters.getRajju(lang)}  |  शुभ अर्थयुक्त नाम का चयन करें।"
                AppLanguage.ENGLISH -> "2. Yoni: ${result.nakshatraLetters.getYoni(lang)}  |  Rajju: ${result.nakshatraLetters.getRajju(lang)}  |  Choose meaningful names with positive resonance."
            }
            canvas.drawText(ruleText1, boxLeft + 12f, y + 36f, textPaint)
            canvas.drawText(ruleText2, boxLeft + 12f, y + 54f, textPaint)

            y += 82f

            // 9. Temple Blessings & Signature Block
            fillPaint.color = Color.WHITE
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, (PAGE_HEIGHT - 32).toFloat()), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 1.2f
            canvas.drawRoundRect(RectF(boxLeft, y, boxRight, (PAGE_HEIGHT - 32).toFloat()), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 10.5f
            canvas.drawText("🕉️ ஓம் சரவணபவ • கோயில் குருக்கள் ஆசிர்வாதம் 🕉️", (PAGE_WIDTH / 2).toFloat(), y + 20f, textPaint)

            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            textPaint.textSize = 9f
            val blessingMsg = when (lang) {
                AppLanguage.TAMIL -> "எல்லாம் வல்ல நாடி ஸ்ரீ சிவ சுப்பிரமணிய சுவாமியின் அருளால் குழந்தை சகல நலன்களும், ஆயுள், ஆரோக்கியம், கல்வி, செல்வமும் பெற்று வாழ ஆசிகள்."
                AppLanguage.HINDI -> "भगवान शिव सुब्रमण्यम स्वामी के आशीर्वाद से शिशु को दीर्घायु, उत्तम स्वास्थ्य, विद्या एवं समृद्धि प्राप्त हो।"
                AppLanguage.ENGLISH -> "May Lord Sri Siva Subramaniya Swami shower the child with abundant health, longevity, wisdom, and eternal prosperity."
            }
            canvas.drawText(blessingMsg, (PAGE_WIDTH / 2).toFloat(), y + 38f, textPaint)

            // Seal & Date
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 8.5f
            textPaint.color = COLOR_MUTED_TEXT
            canvas.drawText("தேதி (Date): ${java.time.LocalDate.now().format(dateFmt)}", boxLeft + 16f, (PAGE_HEIGHT - 42).toFloat(), textPaint)

            textPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText("தலைமை அர்ச்சகர் (Chief Priest) / நாடி திருக்கோயில், பிஜி", boxRight - 16f, (PAGE_HEIGHT - 42).toFloat(), textPaint)

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
