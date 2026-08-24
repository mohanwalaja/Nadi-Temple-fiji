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
        @Suppress("UNUSED_PARAMETER") lang: AppLanguage = AppLanguage.ENGLISH
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

            // 3. Temple Header Banner (3 Languages: Tamil, English, Hindi)
            val headerHeight = 72f
            fillPaint.color = COLOR_MAROON
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + headerHeight), 8f, 8f, fillPaint)

            headerPaint.textAlign = Paint.Align.CENTER
            headerPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

            headerPaint.color = Color.WHITE
            headerPaint.textSize = 12f
            canvas.drawText("🕉️ ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில் • நாடி, பிஜி 🕉️", (PAGE_WIDTH / 2).toFloat(), currentY + 18f, headerPaint)

            headerPaint.color = COLOR_GOLD
            headerPaint.textSize = 9.5f
            canvas.drawText("SRI SIVA SUBRAMANIYA SWAMI TEMPLE • NADI, FIJI ISLANDS", (PAGE_WIDTH / 2).toFloat(), currentY + 34f, headerPaint)

            headerPaint.color = Color.WHITE
            headerPaint.textSize = 8.5f
            canvas.drawText("श्री शिव सुब्रमण्यम स्वामी मंदिर • नाडी, फिजी द्वीप", (PAGE_WIDTH / 2).toFloat(), currentY + 48f, headerPaint)

            headerPaint.color = COLOR_LIGHT_GOLD
            headerPaint.textSize = 10f
            canvas.drawText("VEDIC BABY NAMING & NAKSHATRA LETTERS CERTIFICATE • நாமகரண சுப அட்சர சான்றிதழ்", (PAGE_WIDTH / 2).toFloat(), currentY + 63f, headerPaint)

            currentY += headerHeight + 8f

            // 4. Baby & Birth Particulars Card (Trilingual)
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

            val babyDisplayName = result.babyName.ifBlank { "Newborn Baby / பிறந்த குழந்தை" }
            val genderLabel = if (result.gender == "M") "Boy / ஆண் / बालक" else "Girl / பெண் / बालिका"

            // Row 1 - Col 1 & 2
            drawLabelValue(canvas, "Name / பெயர் / नाम:", babyDisplayName, col1X, r1Y, 115f)
            drawLabelValue(canvas, "Gender / பாலினம் / लिंग:", genderLabel, col2X, r1Y, 115f)

            // Row 2 - Col 1 & 2
            drawLabelValue(canvas, "DOB / பிறந்த தேதி:", result.dob.format(dateFmt), col1X, r2Y, 115f)
            val birthPlaceTrimmed = result.birthPlace.take(30)
            drawLabelValue(canvas, "Place / இடம் / स्थान:", birthPlaceTrimmed, col2X, r2Y, 115f)

            // Row 3 - Col 1 & 2
            drawLabelValue(canvas, "TOB / பிறந்த நேரம்:", result.tob.format(timeFmt), col1X, r3Y, 115f)
            val lagnaCombined = "${result.lagnaRasi.nameEn} (${result.lagnaRasi.nameTa} / ${result.lagnaRasi.nameHi})"
            drawLabelValue(canvas, "Lagna / லக்னம் / लग्न:", lagnaCombined, col2X, r3Y, 115f)

            currentY = infoTop + infoHeight + 8f

            // 5. Astrological Star & Moon Sign Banner (All 3 Languages)
            val starBannerHeight = 44f
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + starBannerHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_GOLD
            canvas.drawRoundRect(RectF(boxLeft, currentY, boxRight, currentY + starBannerHeight), 6f, 6f, borderPaint)

            val starTa = result.nakshatraLetters.nakshatraNameTa
            val starEn = result.nakshatraLetters.nakshatraNameEn
            val starHi = result.nakshatraLetters.nakshatraNameHi
            val rasiTa = result.chandraRasi.nameTa
            val rasiEn = result.chandraRasi.nameEn
            val rasiHi = result.chandraRasi.nameHi

            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 11.5f
            canvas.drawText("⭐ $starEn ($starTa / $starHi) • Pada ${result.janmaPada} (${result.janmaPada}-ஆம் பாதம் / चरण ${result.janmaPada})", (PAGE_WIDTH / 2).toFloat(), currentY + 16f, textPaint)

            textPaint.textSize = 9f
            textPaint.color = COLOR_DARK_TEXT
            val attrText = "🌙 Rasi: $rasiEn ($rasiTa / $rasiHi)  •  Lord: ${result.nakshatraLetters.lordEn} (${result.nakshatraLetters.lordTa})  •  Deity: ${result.nakshatraLetters.deityEn} (${result.nakshatraLetters.deityTa})"
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
            canvas.drawText("✨ PRIMARY AUSPICIOUS STARTING LETTER • முதன்மை ஆரம்ப சுப அட்சரம் (Pada ${result.janmaPada}) ✨", (PAGE_WIDTH / 2).toFloat(), currentY + 14f, textPaint)

            val pTa = result.primaryPadaInfo.letterTa
            val pEn = result.primaryPadaInfo.letterEn
            val pHi = result.primaryPadaInfo.letterHi

            // Large letters in 3 scripts
            textPaint.textSize = 28f
            textPaint.color = COLOR_MAROON
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("$pTa   •   $pEn   •   $pHi", (PAGE_WIDTH / 2).toFloat(), currentY + 50f, textPaint)

            // Explanatory note in 3 languages wrapped properly
            textPaint.textSize = 8f
            textPaint.color = COLOR_MUTED_TEXT
            textPaint.typeface = Typeface.DEFAULT
            val padaNote = "Tamil Panchangam & Vedic Jyotish resonance for $starEn Pada ${result.janmaPada}: Naming with syllable '$pEn' ($pTa / $pHi) grants longevity, wisdom and prosperity."
            canvas.drawText(padaNote, (PAGE_WIDTH / 2).toFloat(), currentY + 70f, textPaint)

            currentY += medHeight + 8f

            // 7. Grid for All 4 Padas of the Star (Trilingual Columns)
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Starting Syllables for All 4 Padas of $starEn • 4 பாதங்களுக்கும் உரிய எழுத்துக்கள்:", boxLeft, currentY + 8f, textPaint)
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
                val padaTitle = "Pada ${pada.padaNumber}" + if (isCurrentPada) " ★ (Birth)" else ""
                canvas.drawText(padaTitle, pLeft + padaCardWidth / 2f, currentY + 14f, textPaint)

                // Letters (Tamil, English, Hindi)
                textPaint.textSize = 18f
                textPaint.color = COLOR_MAROON
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText(pada.letterTa, pLeft + padaCardWidth / 2f, currentY + 36f, textPaint)

                textPaint.textSize = 9.5f
                textPaint.color = COLOR_DARK_TEXT
                textPaint.typeface = Typeface.DEFAULT
                canvas.drawText("${pada.letterEn} • ${pada.letterHi}", pLeft + padaCardWidth / 2f, currentY + 50f, textPaint)

                textPaint.textSize = 7.5f
                textPaint.color = COLOR_MUTED_TEXT
                canvas.drawText("${pada.rasiEn} (${pada.rasiTa})", pLeft + padaCardWidth / 2f, currentY + 65f, textPaint)
            }

            currentY += padaCardHeight + 8f

            // 8. Vedic Astrological Naming Rules Card (Multi-line Wrapped with StaticLayout)
            val rulesTop = currentY
            val rulesHeight = 100f
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas.drawRoundRect(RectF(boxLeft, rulesTop, boxRight, rulesTop + rulesHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 1f
            canvas.drawRoundRect(RectF(boxLeft, rulesTop, boxRight, rulesTop + rulesHeight), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 9.5f
            canvas.drawText("📜 VEDIC NAMING PRINCIPLES & GUIDELINES • நாமகரண சாஸ்திர விதிகள்:", boxLeft + 10f, rulesTop + 14f, textPaint)

            val textPaintWrapped = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 7.8f
                color = COLOR_DARK_TEXT
                typeface = Typeface.DEFAULT
            }

            val rulesCombined = """
                1. English: Starting the name with the resonant syllable of birth nakshatra pada harmonizes cosmic planetary energies and enhances health, fame and intellect.
                2. தமிழ்: ஜென்ம நட்சத்திர பாத ஆரம்ப ஒலி அலைகள் குழந்தையின் வாழ்நாள் முழுவதும் நேர்மறை ஆற்றலையும் தெய்வீக பாதுகாப்பையும் வழங்கும்.
                3. हिन्दी: जन्म नक्षत्र के शुभ चरण अक्षर से नामकरण करने से बालक को दीर्घायु, विद्या, बल और सर्वतोन्मुखी समृद्धि प्राप्त होती है।
                4. Astrological Vibrations: Gana: ${result.nakshatraLetters.ganaEn} (${result.nakshatraLetters.ganaTa}) | Yoni: ${result.nakshatraLetters.yoniEn} (${result.nakshatraLetters.yoniTa}) | Rajju: ${result.nakshatraLetters.rajjuEn} (${result.nakshatraLetters.rajjuTa})
            """.trimIndent()

            drawMultilineText(
                canvas = canvas,
                text = rulesCombined,
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
            textPaint.textSize = 10f
            canvas.drawText("🕉️ ஓம் சரவணபவ • SRI SIVA SUBRAMANIYA SWAMI TEMPLE BLESSINGS 🕉️", (PAGE_WIDTH / 2).toFloat(), bottomTop + 15f, textPaint)

            val blessingPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 7.8f
                color = COLOR_DARK_TEXT
                typeface = Typeface.DEFAULT
            }

            val blessingCombined = """
                May Lord Sri Siva Subramaniya Swami shower the child with divine grace, sound health, long life, supreme wisdom, and prosperous fortune.
                நாடி ஸ்ரீ சிவ சுப்பிரமணிய சுவாமியின் திருவருளால் குழந்தை சகல சௌபாக்கியங்களும் பெற்று நீடூழி வாழ ஆசீர்வதிக்கிறோம்.
                भगवान शिव सुब्रमण्यम स्वामी की कृपा से शिशु को चिरंजीवी, यशस्वी और सर्व कल्याण की प्राप्ति हो।
            """.trimIndent()

            drawMultilineText(
                canvas = canvas,
                text = blessingCombined,
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
            canvas.drawText("Date: ${java.time.LocalDate.now().format(dateFmt)}", boxLeft + 12f, bottomTop + bottomHeight - 8f, textPaint)

            textPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText("Chief Priest / தலைமை குருக்கள் • Nadi Temple, Fiji", boxRight - 12f, bottomTop + bottomHeight - 8f, textPaint)

            pdfDocument.finishPage(page)

            // Write File
            val outputDir = File(context.cacheDir, "baby_naming_certificates").apply { mkdirs() }
            val cleanBabyName = result.babyName.replace(" ", "_").ifBlank { "Newborn_Baby" }
            val fileName = "Baby_Naming_Letters_${cleanBabyName}_${System.currentTimeMillis()}.pdf"
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

    fun shareBabyNamingPdf(context: Context, file: File, babyName: String) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val displayName = babyName.ifBlank { "Newborn Baby" }
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Vedic Baby Naming Certificate - $displayName")
                putExtra(Intent.EXTRA_TEXT, "Sri Siva Subramaniya Swami Temple, Nadi, Fiji - Vedic Baby Naming & Nakshatra Starting Letters Certificate PDF (Trilingual: English, Tamil, Hindi).")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Baby Naming Certificate PDF"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
