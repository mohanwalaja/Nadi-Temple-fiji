package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.*
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.util.Locale

object HoroscopePdfExporter {

    private const val PAGE_WIDTH = 595 // Standard A4 width at 72 DPI
    private const val PAGE_HEIGHT = 842 // Standard A4 height at 72 DPI

    // Theme Colors
    private val COLOR_MAROON = Color.rgb(139, 26, 26)
    private val COLOR_GOLD = Color.rgb(180, 130, 20)
    private val COLOR_LIGHT_GOLD = Color.rgb(254, 249, 231)
    private val COLOR_BORDER = Color.rgb(210, 180, 140)
    private val COLOR_DARK_TEXT = Color.rgb(33, 33, 33)
    private val COLOR_GRAY_TEXT = Color.rgb(100, 100, 100)
    private val COLOR_KUMKUM = Color.rgb(198, 40, 40)
    private val COLOR_GREEN = Color.rgb(46, 125, 50)
    private val COLOR_BG_LIGHT = Color.rgb(250, 247, 242)

    fun exportHoroscopeToPdf(context: Context, result: HoroscopeResult, lang: AppLanguage = AppLanguage.TAMIL): File? {
        val pdfDocument = PdfDocument()

        try {
            // Paint objects
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = COLOR_DARK_TEXT
                textSize = 10f
            }
            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = COLOR_MAROON
                textSize = 15f
                typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            }
            val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = COLOR_MAROON
                textSize = 11f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = COLOR_BORDER
                style = Paint.Style.STROKE
                strokeWidth = 1f
            }
            val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL
            }

            // ==========================================
            // PAGE 1: Personal Details, Rasi Chart, Navagrahas, Dasa
            // ==========================================
            val pageInfo1 = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
            val page1 = pdfDocument.startPage(pageInfo1)
            val canvas1 = page1.canvas

            // Background & Border
            fillPaint.color = COLOR_BG_LIGHT
            canvas1.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), fillPaint)
            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 2f
            canvas1.drawRect(18f, 18f, (PAGE_WIDTH - 18).toFloat(), (PAGE_HEIGHT - 18).toFloat(), borderPaint)
            borderPaint.strokeWidth = 0.8f
            canvas1.drawRect(22f, 22f, (PAGE_WIDTH - 22).toFloat(), (PAGE_HEIGHT - 22).toFloat(), borderPaint)

            var y = 45f

            // Temple Header
            titlePaint.textAlign = Paint.Align.CENTER
            titlePaint.color = COLOR_MAROON
            titlePaint.textSize = 14f
            canvas1.drawText("ஸ்ரீ சிவ சுப்ரமணிய சுவாமி திருக்கோயில்", (PAGE_WIDTH / 2).toFloat(), y, titlePaint)
            y += 15f
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.textSize = 9.5f
            textPaint.color = COLOR_GOLD
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas1.drawText("Sri Siva Subramaniya Swami Temple, Nadi, Fiji Islands", (PAGE_WIDTH / 2).toFloat(), y, textPaint)
            y += 14f
            textPaint.textSize = 11f
            textPaint.color = COLOR_DARK_TEXT
            textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            val docTitle = if (lang == AppLanguage.TAMIL) "திருக்கோயில் ஜாதகக் கணிப்பு அறிக்கை (Horoscope Report)" else "Vedic Horoscope & Astrological Assessment Report"
            canvas1.drawText(docTitle, (PAGE_WIDTH / 2).toFloat(), y, textPaint)
            y += 8f

            // Divider
            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 1f
            canvas1.drawLine(35f, y, (PAGE_WIDTH - 35).toFloat(), y, borderPaint)
            y += 16f

            // Devotee Information Box
            val boxLeft = 32f
            val boxRight = (PAGE_WIDTH - 32).toFloat()
            val boxTop = y
            val boxHeight = 72f
            fillPaint.color = Color.WHITE
            canvas1.drawRoundRect(RectF(boxLeft, boxTop, boxRight, boxTop + boxHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            canvas1.drawRoundRect(RectF(boxLeft, boxTop, boxRight, boxTop + boxHeight), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 9.5f
            val timeFmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            val dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)

            val row1Y = boxTop + 16f
            val row2Y = boxTop + 33f
            val row3Y = boxTop + 50f
            val row4Y = boxTop + 65f

            // Col 1
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("பெயர் (Name):", boxLeft + 10f, row1Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.devoteeName, boxLeft + 85f, row1Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("பிறந்த தேதி (DOB):", boxLeft + 10f, row2Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.dob.format(dateFmt), boxLeft + 105f, row2Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("பிறந்த நேரம் (TOB):", boxLeft + 10f, row3Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.tob.format(timeFmt), boxLeft + 105f, row3Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("பிறந்த இடம் (Place):", boxLeft + 10f, row4Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.birthPlace, boxLeft + 110f, row4Y, textPaint)

            // Col 2
            val col2X = boxLeft + 265f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("லக்னம் (Lagna):", col2X, row1Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText("${result.lagnaRasi.getName(lang)} (${String.format(Locale.ENGLISH, "%.1f", result.lagnaDegrees)}°)", col2X + 80f, row1Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("ராசி (Janma Rasi):", col2X, row2Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.chandraRasi.getName(lang), col2X + 95f, row2Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("நட்சத்திரம் (Star):", col2X, row3Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.janmaNakshatram, col2X + 90f, row3Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("பாதம் (Pada):", col2X, row4Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText("${result.janmaPada}-ஆம் பாதம்", col2X + 70f, row4Y, textPaint)

            y = boxTop + boxHeight + 14f

            // South Indian Rasi Chart (ஜாதக ராசிக் கட்டம்)
            headerPaint.textAlign = Paint.Align.LEFT
            headerPaint.color = COLOR_MAROON
            headerPaint.textSize = 11f
            canvas1.drawText("ஜாதக ராசிக் கட்டம் (South Indian Rasi Chart)", 32f, y, headerPaint)
            y += 8f

            val chartSize = 170f
            val chartLeft = 32f
            val chartTop = y
            val cellSize = chartSize / 4f

            fillPaint.color = Color.WHITE
            canvas1.drawRect(chartLeft, chartTop, chartLeft + chartSize, chartTop + chartSize, fillPaint)
            borderPaint.color = COLOR_MAROON
            borderPaint.strokeWidth = 1.5f
            canvas1.drawRect(chartLeft, chartTop, chartLeft + chartSize, chartTop + chartSize, borderPaint)

            // Inner grid lines
            borderPaint.strokeWidth = 0.8f
            for (i in 1..3) {
                canvas1.drawLine(chartLeft + i * cellSize, chartTop, chartLeft + i * cellSize, chartTop + chartSize, borderPaint)
                canvas1.drawLine(chartLeft, chartTop + i * cellSize, chartLeft + chartSize, chartTop + i * cellSize, borderPaint)
            }
            // Clear center 2x2
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas1.drawRect(chartLeft + cellSize, chartTop + cellSize, chartLeft + 3 * cellSize, chartTop + 3 * cellSize, fillPaint)
            borderPaint.strokeWidth = 1f
            canvas1.drawRect(chartLeft + cellSize, chartTop + cellSize, chartLeft + 3 * cellSize, chartTop + 3 * cellSize, borderPaint)

            textPaint.textAlign = Paint.Align.CENTER
            textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 10f
            canvas1.drawText("ராசி", chartLeft + 2 * cellSize, chartTop + 1.8f * cellSize, textPaint)
            textPaint.textSize = 8f
            textPaint.color = COLOR_GOLD
            canvas1.drawText("RASI", chartLeft + 2 * cellSize, chartTop + 2.3f * cellSize, textPaint)

            // Draw Rasi Houses & Occupant Planets
            // South Indian Fixed Chart mapping (Row, Col):
            // (0,0)=Meenam(12), (0,1)=Mesham(1), (0,2)=Rishabam(2), (0,3)=Mithunam(3)
            // (1,0)=Kumbam(11),                                     (1,3)=Kadagam(4)
            // (2,0)=Magaram(10),                                    (2,3)=Simham(5)
            // (3,0)=Dhanusu(9), (3,1)=Viruchigam(8), (3,2)=Thulam(7), (3,3)=Kanni(6)
            val houseCoords = mapOf(
                Rasi.MEENAM to Pair(0, 0),
                Rasi.MESHAM to Pair(0, 1),
                Rasi.RISHABAM to Pair(0, 2),
                Rasi.MITHUNAM to Pair(0, 3),
                Rasi.KADAGAM to Pair(1, 3),
                Rasi.SIMHAM to Pair(2, 3),
                Rasi.KANNI to Pair(3, 3),
                Rasi.THULAM to Pair(3, 2),
                Rasi.VIRUCHIGAM to Pair(3, 1),
                Rasi.DHANUSU to Pair(3, 0),
                Rasi.MAGARAM to Pair(2, 0),
                Rasi.KUMBAM to Pair(1, 0)
            )

            houseCoords.forEach { (rasi, coord) ->
                val cX = chartLeft + coord.second * cellSize
                val cY = chartTop + coord.first * cellSize

                // Rasi Short Tag
                textPaint.textAlign = Paint.Align.LEFT
                textPaint.textSize = 6.5f
                textPaint.color = COLOR_GRAY_TEXT
                textPaint.typeface = Typeface.DEFAULT
                canvas1.drawText(rasi.nameTa.take(2), cX + 2f, cY + 8f, textPaint)

                // Check Lagna
                var pY = cY + 18f
                if (result.lagnaRasi == rasi) {
                    textPaint.textAlign = Paint.Align.LEFT
                    textPaint.textSize = 7.5f
                    textPaint.color = COLOR_KUMKUM
                    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    canvas1.drawText("லக் (L)", cX + 2f, pY, textPaint)
                    pY += 9f
                }

                // Planets in this rasi
                val planetsInRasi = result.planetPositions.filter { it.rasi == rasi }
                planetsInRasi.forEach { p ->
                    textPaint.textAlign = Paint.Align.LEFT
                    textPaint.textSize = 7f
                    textPaint.color = if (p.graha == Graha.SURYA || p.graha == Graha.CHANDRA) COLOR_MAROON else COLOR_DARK_TEXT
                    textPaint.typeface = if (p.isRetrograde) Typeface.create(Typeface.DEFAULT, Typeface.BOLD) else Typeface.DEFAULT
                    val pText = "${p.graha.shortTa}${if (p.isRetrograde) "(வ)" else ""}"
                    canvas1.drawText(pText, cX + 2f, pY, textPaint)
                    pY += 8.5f
                }
            }

            // Right side: Navagraha Positions Table
            val tableLeft = chartLeft + chartSize + 14f
            val tableRight = boxRight
            val tableTop = chartTop

            headerPaint.textSize = 10f
            headerPaint.color = COLOR_MAROON
            canvas1.drawText("நவக்கிரக நிலைகள் (Planetary Positions)", tableLeft, tableTop - 2f, headerPaint)

            var tY = tableTop + 10f
            // Table Header row
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas1.drawRect(tableLeft, tY - 8f, tableRight, tY + 4f, fillPaint)
            borderPaint.color = COLOR_BORDER
            canvas1.drawRect(tableLeft, tY - 8f, tableRight, tY + 4f, borderPaint)

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 7.5f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText("கிரகம்", tableLeft + 4f, tY, textPaint)
            canvas1.drawText("ராசி", tableLeft + 55f, tY, textPaint)
            canvas1.drawText("பாகை", tableLeft + 115f, tY, textPaint)
            canvas1.drawText("நட்சத்திரம் / பாதம்", tableLeft + 160f, tY, textPaint)
            canvas1.drawText("நிலை", tableLeft + 260f, tY, textPaint)

            tY += 12f
            result.planetPositions.forEach { p ->
                textPaint.typeface = Typeface.DEFAULT
                textPaint.color = COLOR_DARK_TEXT
                textPaint.textSize = 7.5f

                canvas1.drawText(p.graha.getName(lang), tableLeft + 4f, tY, textPaint)
                canvas1.drawText(p.rasi.getName(lang), tableLeft + 55f, tY, textPaint)
                canvas1.drawText(String.format(Locale.ENGLISH, "%.1f°", p.degrees), tableLeft + 115f, tY, textPaint)
                val nakClean = p.nakshatram.substringBefore(" ")
                canvas1.drawText("$nakClean (${p.pada})", tableLeft + 160f, tY, textPaint)
                val status = if (p.isRetrograde) "வக்ரம் (R)" else if (p.isCombust) "அஸ்தங்கம்" else "சுபம்"
                textPaint.color = if (p.isRetrograde) COLOR_KUMKUM else COLOR_GRAY_TEXT
                canvas1.drawText(status, tableLeft + 260f, tY, textPaint)

                borderPaint.color = Color.rgb(235, 235, 235)
                canvas1.drawLine(tableLeft, tY + 2f, tableRight, tY + 2f, borderPaint)
                tY += 11.5f
            }

            y = chartTop + chartSize + 16f

            // Vimshottari Dasha Periods Box
            headerPaint.textSize = 10.5f
            headerPaint.color = COLOR_MAROON
            canvas1.drawText("விம்சmodule மகாதிசை இருப்பு (Vimshottari Dasha Timeline)", 32f, y, headerPaint)
            y += 8f

            fillPaint.color = Color.WHITE
            canvas1.drawRoundRect(RectF(32f, y, boxRight, y + 65f), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            canvas1.drawRoundRect(RectF(32f, y, boxRight, y + 65f), 6f, 6f, borderPaint)

            var dY = y + 14f
            result.dashaPeriods.take(4).forEach { dasha ->
                textPaint.textAlign = Paint.Align.LEFT
                textPaint.textSize = 8f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = COLOR_MAROON
                canvas1.drawText("• ${dasha.mahadashaLord.nameTa} மகாதிசை:", 42f, dY, textPaint)

                textPaint.typeface = Typeface.DEFAULT
                textPaint.color = COLOR_DARK_TEXT
                val startStr = dasha.startDate.format(dateFmt)
                val endStr = dasha.endDate.format(dateFmt)
                canvas1.drawText("$startStr முதல் $endStr வரை (${dasha.descriptionTa})", 160f, dY, textPaint)
                dY += 13f
            }

            y += 75f

            // Sani Transit & Saturn Status Card
            headerPaint.textSize = 10.5f
            headerPaint.color = COLOR_MAROON
            canvas1.drawText("சனிப் பெயர்ச்சி நிலை (Saturn Transit Status)", 32f, y, headerPaint)
            y += 8f

            val sani = result.saniStatus
            val saniBgColor = if (sani.isEzharaiSani || sani.isAshtamaSani) Color.rgb(255, 240, 240) else Color.rgb(240, 250, 240)
            fillPaint.color = saniBgColor
            canvas1.drawRoundRect(RectF(32f, y, boxRight, y + 42f), 6f, 6f, fillPaint)
            borderPaint.color = if (sani.isEzharaiSani || sani.isAshtamaSani) COLOR_KUMKUM else COLOR_GREEN
            canvas1.drawRoundRect(RectF(32f, y, boxRight, y + 42f), 6f, 6f, borderPaint)

            val saniStatusText = if (sani.isEzharaiSani) {
                "ஏழரை சனி நடப்பு: ${sani.ezharaiTypeTa} (சனி பகவான் சாதகமற்ற கோச்சாரத்தில் உள்ளார்)"
            } else if (sani.isAshtamaSani) {
                "அஷ்டம சனி நடப்பு (எச்சரிக்கையும் கவனமும் தேவைப்படும் காலம்)"
            } else if (sani.isKandakaSani) {
                "கண்டக சனி நடப்பு"
            } else {
                "ஏழரை / அஷ்டம சனி தாக்கம் இல்லை (அனுகூலமான கோச்சார காலம்)"
            }

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 8.5f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = if (sani.isEzharaiSani || sani.isAshtamaSani) COLOR_KUMKUM else COLOR_GREEN
            canvas1.drawText(saniStatusText, 42f, y + 14f, textPaint)

            textPaint.typeface = Typeface.DEFAULT
            textPaint.textSize = 8f
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText("பரிகாரம்: ${sani.remedyTa}", 42f, y + 28f, textPaint)

            // Page 1 Footer
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.textSize = 7.5f
            textPaint.color = COLOR_GRAY_TEXT
            canvas1.drawText("பக்கம் 1 / 2 • ஸ்ரீ சிவ சுப்ரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி", (PAGE_WIDTH / 2).toFloat(), (PAGE_HEIGHT - 26).toFloat(), textPaint)

            pdfDocument.finishPage(page1)

            // ==========================================
            // PAGE 2: Doshas, Aspect Predictions & Temple Summary
            // ==========================================
            val pageInfo2 = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 2).create()
            val page2 = pdfDocument.startPage(pageInfo2)
            val canvas2 = page2.canvas

            // Background & Border
            fillPaint.color = COLOR_BG_LIGHT
            canvas2.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), fillPaint)
            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 2f
            canvas2.drawRect(18f, 18f, (PAGE_WIDTH - 18).toFloat(), (PAGE_HEIGHT - 18).toFloat(), borderPaint)
            borderPaint.strokeWidth = 0.8f
            canvas2.drawRect(22f, 22f, (PAGE_WIDTH - 22).toFloat(), (PAGE_HEIGHT - 22).toFloat(), borderPaint)

            var y2 = 42f

            headerPaint.textAlign = Paint.Align.LEFT
            headerPaint.textSize = 12f
            headerPaint.color = COLOR_MAROON
            canvas2.drawText("தோஷ பரிசீலனை மற்றும் பரிகாரங்கள் (Dosha Assessment & Remedies)", 32f, y2, headerPaint)
            y2 += 12f

            result.doshas.forEach { dosha ->
                val doshaBg = if (dosha.isPresent) Color.rgb(255, 245, 245) else Color.rgb(245, 255, 245)
                fillPaint.color = doshaBg
                canvas2.drawRoundRect(RectF(32f, y2, boxRight, y2 + 38f), 5f, 5f, fillPaint)
                borderPaint.color = if (dosha.isPresent) COLOR_KUMKUM else COLOR_GREEN
                borderPaint.strokeWidth = 0.8f
                canvas2.drawRoundRect(RectF(32f, y2, boxRight, y2 + 38f), 5f, 5f, borderPaint)

                textPaint.textAlign = Paint.Align.LEFT
                textPaint.textSize = 8.5f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = if (dosha.isPresent) COLOR_KUMKUM else COLOR_GREEN
                canvas2.drawText("${dosha.nameTa} - ${dosha.severityTa}", 40f, y2 + 12f, textPaint)

                textPaint.typeface = Typeface.DEFAULT
                textPaint.textSize = 7.5f
                textPaint.color = COLOR_DARK_TEXT
                canvas2.drawText(dosha.descriptionTa, 40f, y2 + 23f, textPaint)

                if (dosha.isPresent) {
                    textPaint.color = COLOR_MAROON
                    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    canvas2.drawText("பரிகாரம்: ${dosha.traditionalRemedyTa}", 40f, y2 + 33f, textPaint)
                }

                y2 += 44f
            }

            y2 += 6f

            // Aspect Summaries
            headerPaint.textSize = 12f
            headerPaint.color = COLOR_MAROON
            canvas2.drawText("திருக்கோயில் ஜாதக சுருக்கம் & முக்கிய பலன்கள் (Life Aspects Summary)", 32f, y2, headerPaint)
            y2 += 10f

            val summ = result.summary
            val aspects = listOf(
                Pair("ஆரோக்கியம் (Health & Vitality)", summ.healthTa),
                Pair("தனம் & நிதி (Wealth & Finances)", summ.wealthTa),
                Pair("கல்வி & அறிவு (Education & Intellect)", summ.educationTa),
                Pair("தொழில் & வேலை (Career & Profession)", summ.careerTa),
                Pair("திருமணம் & உறவு (Marriage & Union)", summ.marriageTa),
                Pair("குடும்பம் & பூமி (Family & Property)", summ.familyTa),
                Pair("வெளிநாட்டு வாய்ப்புகள் (Foreign Travels)", summ.foreignTravelTa),
                Pair("தற்போதைய வழிகாட்டல் (Current Guidance)", summ.currentPeriodGuidanceTa)
            )

            fillPaint.color = Color.WHITE
            canvas2.drawRoundRect(RectF(32f, y2, boxRight, y2 + 340f), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            canvas2.drawRoundRect(RectF(32f, y2, boxRight, y2 + 340f), 6f, 6f, borderPaint)

            var aY = y2 + 14f
            aspects.forEach { (title, desc) ->
                textPaint.textAlign = Paint.Align.LEFT
                textPaint.textSize = 8.5f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = COLOR_MAROON
                canvas2.drawText("• $title", 40f, aY, textPaint)
                aY += 10f

                textPaint.typeface = Typeface.DEFAULT
                textPaint.textSize = 7.5f
                textPaint.color = COLOR_DARK_TEXT

                // Simple wrap
                val words = desc.split(" ")
                var line = ""
                for (word in words) {
                    if (textPaint.measureText("$line $word") < (boxRight - 60f)) {
                        line = if (line.isEmpty()) word else "$line $word"
                    } else {
                        canvas2.drawText(line, 48f, aY, textPaint)
                        aY += 9f
                        line = word
                    }
                }
                if (line.isNotEmpty()) {
                    canvas2.drawText(line, 48f, aY, textPaint)
                    aY += 12f
                }
                borderPaint.color = Color.rgb(240, 240, 240)
                canvas2.drawLine(40f, aY - 3f, boxRight - 10f, aY - 3f, borderPaint)
            }

            // Temple Blessings & Disclaimer
            val discY = (PAGE_HEIGHT - 65).toFloat()
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.textSize = 8f
            textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas2.drawText("வெற்றிவேல் முருகனுக்கு அரோகரா! ஸ்ரீ சிவ சுப்ரமணிய சுவாமி திருவருள் துணை!", (PAGE_WIDTH / 2).toFloat(), discY, textPaint)

            textPaint.typeface = Typeface.DEFAULT
            textPaint.textSize = 6.5f
            textPaint.color = COLOR_GRAY_TEXT
            canvas2.drawText("குறிப்பு: இது பாரம்பரிய ஜோதிட கணக்கீட்டு அறிக்கை மட்டுமே. அறிவார்ந்த ஜோதிடரின் ஆலோசனையுடன் அறியவும்.", (PAGE_WIDTH / 2).toFloat(), discY + 11f, textPaint)

            // Page 2 Footer
            canvas2.drawText("பக்கம் 2 / 2 • ஸ்ரீ சிவ சுப்ரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி", (PAGE_WIDTH / 2).toFloat(), (PAGE_HEIGHT - 26).toFloat(), textPaint)

            pdfDocument.finishPage(page2)

            // Write PDF to cache directory
            val fileName = "Jathagam_${result.devoteeName.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
            val outputDir = File(context.cacheDir, "pdf_reports")
            if (!outputDir.exists()) outputDir.mkdirs()
            val outputFile = File(outputDir, fileName)
            val fos = FileOutputStream(outputFile)
            pdfDocument.writeTo(fos)
            fos.flush()
            fos.close()

            return outputFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            pdfDocument.close()
        }
    }

    fun shareOrOpenPdf(context: Context, pdfFile: File, title: String = "ஜாதக அறிக்கை (Horoscope PDF)") {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, "ஸ்ரீ சிவ சுப்ரமணிய சுவாமி திருக்கோயில் - $title")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "ஜாதக PDF பகிரவும் / திறக்கவும் (Share or View PDF)")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "PDF பகிர்வதில் பிழை ஏற்பட்டது: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
