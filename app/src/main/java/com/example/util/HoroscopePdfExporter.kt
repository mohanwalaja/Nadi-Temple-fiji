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
import com.example.data.repository.BabyNamesRepository
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.util.Locale

object HoroscopePdfExporter {

    private const val PAGE_WIDTH = 595 // Standard A4 width at 72 DPI
    private const val PAGE_HEIGHT = 842 // Standard A4 height at 72 DPI

    // Theme Colors
    private val COLOR_MAROON = Color.rgb(185, 28, 28)
    private val COLOR_GOLD = Color.rgb(217, 119, 6)
    private val COLOR_LIGHT_GOLD = Color.rgb(255, 251, 235)
    private val COLOR_BORDER = Color.rgb(226, 232, 240)
    private val COLOR_DARK_TEXT = Color.rgb(15, 23, 42)
    private val COLOR_GRAY_TEXT = Color.rgb(100, 116, 139)
    private val COLOR_KUMKUM = Color.rgb(220, 38, 38)
    private val COLOR_GREEN = Color.rgb(22, 163, 74)
    private val COLOR_BG_LIGHT = Color.rgb(248, 249, 250)

    fun exportHoroscopeToPdf(context: Context, result: HoroscopeResult, lang: AppLanguage = AppLanguage.TAMIL): File? {
        val pdfDocument = PdfDocument()

        try {
            // Paint objects
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = COLOR_DARK_TEXT
                textSize = 10.5f
            }
            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = COLOR_MAROON
                textSize = 16f
                typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            }
            val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = COLOR_MAROON
                textSize = 12f
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

            val isTa = lang == AppLanguage.TAMIL
            val isHi = lang == AppLanguage.HINDI

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

            var y = 36f

            // Auspicious Invocation
            val invocationPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = COLOR_MAROON
                textSize = 9.5f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            }
            val invocationText = when (lang) {
                AppLanguage.TAMIL -> "|| ஸ்ரீ கணேசாய நம: ||   || சுபமஸ்து ||   || குருப்யோ நம: ||"
                AppLanguage.HINDI -> "॥ श्री गणेशाय नमः ॥   ॥ शुभमस्तु ॥   ॥ श्री गुरुभ्यो नमः ॥"
                AppLanguage.ENGLISH -> "|| Sri Ganeshaya Namah ||   || Subhamastu ||   || Gurubhyo Namah ||"
            }
            canvas1.drawText(invocationText, (PAGE_WIDTH / 2).toFloat(), y, invocationPaint)
            y += 14f

            // Temple Header
            titlePaint.textAlign = Paint.Align.CENTER
            titlePaint.color = COLOR_MAROON
            titlePaint.textSize = 14.5f
            val templeTitle = when (lang) {
                AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில்"
                AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी मंदिर"
                AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Kovil"
            }
            canvas1.drawText(templeTitle, (PAGE_WIDTH / 2).toFloat(), y, titlePaint)
            y += 12f

            textPaint.textSize = 8.5f
            textPaint.color = COLOR_GRAY_TEXT
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            val templeLocation = when (lang) {
                AppLanguage.TAMIL -> "நாடி, பிஜி தீவுகள் • துல்லிய திருக்கணித பஞ்சாங்க ஜாதகக் கணிப்பு அறிக்கை"
                AppLanguage.HINDI -> "नादी, फिजी द्वीप • वैदिक जन्मकुंडली फलकथन रिपोर्ट"
                AppLanguage.ENGLISH -> "Nadi, Fiji Islands • Vedic Horoscope Assessment Report"
            }
            canvas1.drawText(templeLocation, (PAGE_WIDTH / 2).toFloat(), y, textPaint)
            y += 10f

            // Divider
            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 1.2f
            canvas1.drawLine(35f, y, (PAGE_WIDTH - 35).toFloat(), y, borderPaint)
            y += 11f

            // Devotee Information Box
            val boxLeft = 32f
            val boxRight = (PAGE_WIDTH - 32).toFloat()
            val boxTop = y
            val boxHeight = 88f
            fillPaint.color = Color.WHITE
            canvas1.drawRoundRect(RectF(boxLeft, boxTop, boxRight, boxTop + boxHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            canvas1.drawRoundRect(RectF(boxLeft, boxTop, boxRight, boxTop + boxHeight), 6f, 6f, borderPaint)

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 10f
            val timeFmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            val dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)

            val row1Y = boxTop + 15f
            val row2Y = boxTop + 31f
            val row3Y = boxTop + 47f
            val row4Y = boxTop + 63f
            val row5Y = boxTop + 79f

            // Labels for Personal Info
            val lblName = when (lang) { AppLanguage.TAMIL -> "பெயர் (Name):"; AppLanguage.HINDI -> "नाम (Name):"; AppLanguage.ENGLISH -> "Name:" }
            val lblDob = when (lang) { AppLanguage.TAMIL -> "பிறந்த தேதி (DOB):"; AppLanguage.HINDI -> "जन्म तिथि (DOB):"; AppLanguage.ENGLISH -> "Date of Birth:" }
            val lblTob = when (lang) { AppLanguage.TAMIL -> "பிறந்த நேரம் (TOB):"; AppLanguage.HINDI -> "जन्म समय (TOB):"; AppLanguage.ENGLISH -> "Time of Birth:" }
            val lblPlace = when (lang) { AppLanguage.TAMIL -> "பிறந்த இடம் (Place):"; AppLanguage.HINDI -> "जन्म स्थान (Place):"; AppLanguage.ENGLISH -> "Birth Place:" }

            val lblLagna = when (lang) { AppLanguage.TAMIL -> "லக்னம் (Lagna):"; AppLanguage.HINDI -> "लग्न (Lagna):"; AppLanguage.ENGLISH -> "Ascendant (Lagna):" }
            val lblRasi = when (lang) { AppLanguage.TAMIL -> "ராசி (Moon Rasi):"; AppLanguage.HINDI -> "राशि (Rasi):"; AppLanguage.ENGLISH -> "Janma Rasi (Moon):" }
            val lblStar = when (lang) { AppLanguage.TAMIL -> "நட்சத்திரம் (Star):"; AppLanguage.HINDI -> "नक्षत्र (Nakshatra):"; AppLanguage.ENGLISH -> "Nakshatra (Star):" }
            val lblPada = when (lang) { AppLanguage.TAMIL -> "பாதம் (Pada):"; AppLanguage.HINDI -> "चरण / पाद (Pada):"; AppLanguage.ENGLISH -> "Pada / Quarter:" }

            // Column 1
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(lblName, boxLeft + 10f, row1Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.devoteeName, boxLeft + 105f, row1Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(lblDob, boxLeft + 10f, row2Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.dob.format(dateFmt), boxLeft + 105f, row2Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(lblTob, boxLeft + 10f, row3Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.tob.format(timeFmt), boxLeft + 105f, row3Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(lblPlace, boxLeft + 10f, row4Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.birthPlace, boxLeft + 105f, row4Y, textPaint)

            // Column 2
            val col2X = boxLeft + 265f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(lblLagna, col2X, row1Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText("${result.lagnaRasi.getName(lang)} (${String.format(Locale.ENGLISH, "%.1f", result.lagnaDegrees)}°)", col2X + 95f, row1Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(lblRasi, col2X, row2Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.chandraRasi.getName(lang), col2X + 95f, row2Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(lblStar, col2X, row3Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(result.getJanmaNakshatram(lang), col2X + 95f, row3Y, textPaint)

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(lblPada, col2X, row4Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            val padaSuffix = when (lang) {
                AppLanguage.TAMIL -> "${result.janmaPada}-ஆம் பாதம்"
                AppLanguage.HINDI -> "चरण ${result.janmaPada}"
                AppLanguage.ENGLISH -> "Pada ${result.janmaPada}"
            }
            canvas1.drawText(padaSuffix, col2X + 95f, row4Y, textPaint)

            // Row 5: Baby Naming Starting Letters (As per Tamil Panchangam)
            val babyLetters = BabyNamesRepository().getByNakshatraName(result.janmaNakshatram)
            val padaLetterInfo = babyLetters.padas.firstOrNull { it.padaNumber == result.janmaPada }
            val lblBabyLetters = when (lang) {
                AppLanguage.TAMIL -> "பெயர் தொடக்க எழுத்து:"
                AppLanguage.HINDI -> "नामकरण शुभ अक्षर:"
                AppLanguage.ENGLISH -> "Baby Name Letters:"
            }
            val babyLetterVal = if (padaLetterInfo != null) {
                when (lang) {
                    AppLanguage.TAMIL -> "பாதம் ${result.janmaPada}: ${padaLetterInfo.letterTa} (${padaLetterInfo.letterEn}) • 4 பாதங்கள்: ${babyLetters.allLettersSummaryTa}"
                    AppLanguage.HINDI -> "चरण ${result.janmaPada}: ${padaLetterInfo.letterHi} (${padaLetterInfo.letterEn}) • 4 चरण: ${babyLetters.allLettersSummaryHi}"
                    AppLanguage.ENGLISH -> "Pada ${result.janmaPada}: ${padaLetterInfo.letterEn} • All 4 Padas: ${babyLetters.allLettersSummaryEn}"
                }
            } else {
                babyLetters.getLettersSummary(lang)
            }

            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textSize = 9.5f
            canvas1.drawText(lblBabyLetters, boxLeft + 10f, row5Y, textPaint)
            textPaint.typeface = Typeface.DEFAULT
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText(babyLetterVal, boxLeft + 125f, row5Y, textPaint)
            textPaint.textSize = 10f

            y = boxTop + boxHeight + 12f

            // South Indian Rasi Chart
            headerPaint.textAlign = Paint.Align.LEFT
            headerPaint.color = COLOR_MAROON
            headerPaint.textSize = 10f
            val chartHeader = when (lang) {
                AppLanguage.TAMIL -> "ஜாதக ராசிக் கட்டம் (Rasi Chart)"
                AppLanguage.HINDI -> "जन्म कुंडली चक्र (Rasi Chart)"
                AppLanguage.ENGLISH -> "South Indian Rasi Chart"
            }
            canvas1.drawText(chartHeader, 32f, y, headerPaint)
            y += 7f

            val chartSize = 155f
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
            textPaint.textSize = 9.5f
            val centerLabel = when (lang) {
                AppLanguage.TAMIL -> "ராசி"
                AppLanguage.HINDI -> "राशि"
                AppLanguage.ENGLISH -> "RASI"
            }
            canvas1.drawText(centerLabel, chartLeft + 2 * cellSize, chartTop + 1.8f * cellSize, textPaint)
            textPaint.textSize = 7.5f
            textPaint.color = COLOR_GOLD
            canvas1.drawText("CHART", chartLeft + 2 * cellSize, chartTop + 2.3f * cellSize, textPaint)

            // Draw Rasi Houses & Occupant Planets
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
                textPaint.textSize = 7f
                textPaint.color = COLOR_GRAY_TEXT
                textPaint.typeface = Typeface.DEFAULT
                val rasiTag = when (lang) {
                    AppLanguage.TAMIL -> rasi.nameTa.take(2)
                    AppLanguage.HINDI -> rasi.nameHi.substringBefore(" (").take(2)
                    AppLanguage.ENGLISH -> rasi.nameEn.substringBefore(" (").take(3)
                }
                canvas1.drawText(rasiTag, cX + 2f, cY + 7.5f, textPaint)

                // Check Lagna
                var pY = cY + 16.5f
                if (result.lagnaRasi == rasi) {
                    textPaint.textAlign = Paint.Align.LEFT
                    textPaint.textSize = 7.5f
                    textPaint.color = COLOR_KUMKUM
                    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    val lagText = when (lang) {
                        AppLanguage.TAMIL -> "லக் (L)"
                        AppLanguage.HINDI -> "लग्न (L)"
                        AppLanguage.ENGLISH -> "Asc (L)"
                    }
                    canvas1.drawText(lagText, cX + 2f, pY, textPaint)
                    pY += 8.5f
                }

                // Planets in this rasi
                val planetsInRasi = result.planetPositions.filter { it.rasi == rasi }
                planetsInRasi.forEach { p ->
                    textPaint.textAlign = Paint.Align.LEFT
                    textPaint.textSize = 7.5f
                    textPaint.color = if (p.graha == Graha.SURYA || p.graha == Graha.CHANDRA) COLOR_MAROON else COLOR_DARK_TEXT
                    textPaint.typeface = if (p.isRetrograde) Typeface.create(Typeface.DEFAULT, Typeface.BOLD) else Typeface.DEFAULT
                    val shortGraha = when (lang) {
                        AppLanguage.TAMIL -> p.graha.shortTa
                        AppLanguage.HINDI -> p.graha.shortHi
                        AppLanguage.ENGLISH -> p.graha.shortEn
                    }
                    val retSuffix = if (p.isRetrograde) "(R)" else ""
                    canvas1.drawText("$shortGraha$retSuffix", cX + 2f, pY, textPaint)
                    pY += 8f
                }
            }

            // Right side: Navagraha Positions Table (Width = 367f, perfectly spaced columns)
            val tableLeft = 196f
            val tableRight = boxRight
            val tableTop = chartTop

            headerPaint.textSize = 10f
            headerPaint.color = COLOR_MAROON
            val tableTitle = when (lang) {
                AppLanguage.TAMIL -> "நவக்கிரக நிலைகள் (Navagraha Positions)"
                AppLanguage.HINDI -> "नवग्रह स्थितियाँ (Navagraha Positions)"
                AppLanguage.ENGLISH -> "Planetary Positions (Navagrahas)"
            }
            canvas1.drawText(tableTitle, tableLeft, tableTop - 2f, headerPaint)

            var tY = tableTop + 9f
            // Table Header row
            fillPaint.color = COLOR_LIGHT_GOLD
            canvas1.drawRect(tableLeft, tY - 8f, tableRight, tY + 4f, fillPaint)
            borderPaint.color = COLOR_BORDER
            canvas1.drawRect(tableLeft, tY - 8f, tableRight, tY + 4f, borderPaint)

            val colPlanet = when (lang) { AppLanguage.TAMIL -> "கிரகம்"; AppLanguage.HINDI -> "ग्रह"; AppLanguage.ENGLISH -> "Planet" }
            val colRasi = when (lang) { AppLanguage.TAMIL -> "ராசி"; AppLanguage.HINDI -> "राशि"; AppLanguage.ENGLISH -> "Rasi" }
            val colDeg = when (lang) { AppLanguage.TAMIL -> "பாகை"; AppLanguage.HINDI -> "अंश"; AppLanguage.ENGLISH -> "Deg" }
            val colNak = when (lang) { AppLanguage.TAMIL -> "நட்சத்திரம் (பாதம்)"; AppLanguage.HINDI -> "नक्षत्र (चरण)"; AppLanguage.ENGLISH -> "Nakshatra (Pada)" }
            val colStatus = when (lang) { AppLanguage.TAMIL -> "நிலை"; AppLanguage.HINDI -> "स्थिति"; AppLanguage.ENGLISH -> "Status" }

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 8f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            canvas1.drawText(colPlanet, tableLeft + 4f, tY, textPaint)
            canvas1.drawText(colRasi, tableLeft + 60f, tY, textPaint)
            canvas1.drawText(colDeg, tableLeft + 138f, tY, textPaint)
            canvas1.drawText(colNak, tableLeft + 176f, tY, textPaint)
            canvas1.drawText(colStatus, tableLeft + 296f, tY, textPaint)

            tY += 12f
            result.planetPositions.forEach { p ->
                textPaint.typeface = Typeface.DEFAULT
                textPaint.color = COLOR_DARK_TEXT
                textPaint.textSize = 8f

                val cleanPlanetName = when (lang) {
                    AppLanguage.TAMIL -> p.graha.nameTa
                    AppLanguage.HINDI -> p.graha.nameHi.substringBefore(" (")
                    AppLanguage.ENGLISH -> p.graha.nameEn.substringBefore(" (")
                }
                val cleanRasiName = when (lang) {
                    AppLanguage.TAMIL -> p.rasi.nameTa
                    AppLanguage.HINDI -> p.rasi.nameHi.substringBefore(" (")
                    AppLanguage.ENGLISH -> p.rasi.nameEn.substringBefore(" (")
                }
                val cleanNakName = p.getNakshatram(lang)

                canvas1.drawText(cleanPlanetName, tableLeft + 4f, tY, textPaint)
                canvas1.drawText(cleanRasiName, tableLeft + 60f, tY, textPaint)
                canvas1.drawText(String.format(Locale.ENGLISH, "%.1f°", p.degrees), tableLeft + 138f, tY, textPaint)
                canvas1.drawText("$cleanNakName (${p.pada})", tableLeft + 176f, tY, textPaint)

                val status = if (p.isRetrograde) {
                    when (lang) { AppLanguage.TAMIL -> "வக்ரம் (R)"; AppLanguage.HINDI -> "वक्री (R)"; AppLanguage.ENGLISH -> "Retrograde (R)" }
                } else if (p.isCombust) {
                    when (lang) { AppLanguage.TAMIL -> "அஸ்தங்கம்"; AppLanguage.HINDI -> "अस्त"; AppLanguage.ENGLISH -> "Combust" }
                } else {
                    when (lang) { AppLanguage.TAMIL -> "நேர்கதி"; AppLanguage.HINDI -> "मार्गी"; AppLanguage.ENGLISH -> "Direct" }
                }
                textPaint.color = if (p.isRetrograde) COLOR_KUMKUM else if (p.isCombust) COLOR_GOLD else COLOR_DARK_TEXT
                canvas1.drawText(status, tableLeft + 296f, tY, textPaint)

                borderPaint.color = Color.rgb(238, 238, 238)
                canvas1.drawLine(tableLeft, tY + 2f, tableRight, tY + 2f, borderPaint)
                tY += 11f
            }

            y = chartTop + chartSize + 12f

            // Vimshottari Dasha Periods Table Section
            headerPaint.textSize = 10.5f
            headerPaint.color = COLOR_MAROON
            val dashaHeader = when (lang) {
                AppLanguage.TAMIL -> "விம்சோத்தரி மகாதிசை வரிசை அட்டவணை (Vimshottari Dasha Timeline)"
                AppLanguage.HINDI -> "विंशोत्तरी महादशा क्रम सारणी (Vimshottari Dasha Timeline)"
                AppLanguage.ENGLISH -> "Vimshottari Mahadasha Sequence Timeline"
            }
            canvas1.drawText(dashaHeader, 32f, y, headerPaint)
            y += 6f

            val dashaTableTop = y
            val dashaTableHeight = 68f
            fillPaint.color = Color.WHITE
            canvas1.drawRoundRect(RectF(32f, dashaTableTop, boxRight, dashaTableTop + dashaTableHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            borderPaint.strokeWidth = 0.8f
            canvas1.drawRoundRect(RectF(32f, dashaTableTop, boxRight, dashaTableTop + dashaTableHeight), 6f, 6f, borderPaint)

            // Dasha Table Header Row
            fillPaint.color = Color.rgb(254, 243, 199) // Soft warm gold/amber header
            canvas1.drawRoundRect(RectF(32f, dashaTableTop, boxRight, dashaTableTop + 14f), 5f, 5f, fillPaint)
            canvas1.drawRect(32f, dashaTableTop + 8f, boxRight, dashaTableTop + 14f, fillPaint)

            textPaint.textSize = 8f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            textPaint.textAlign = Paint.Align.LEFT
            val colLord = when (lang) { AppLanguage.TAMIL -> "மகாதிசை அதிபதி"; AppLanguage.HINDI -> "महादशा स्वामी"; AppLanguage.ENGLISH -> "Mahadasha Lord" }
            val colStart = when (lang) { AppLanguage.TAMIL -> "தொடக்க நாள்"; AppLanguage.HINDI -> "प्रारंभ तिथि"; AppLanguage.ENGLISH -> "Start Date" }
            val colEnd = when (lang) { AppLanguage.TAMIL -> "முடிவு நாள்"; AppLanguage.HINDI -> "समाप्ति तिथि"; AppLanguage.ENGLISH -> "End Date" }
            val colDur = when (lang) { AppLanguage.TAMIL -> "கால அளவு"; AppLanguage.HINDI -> "अवधि"; AppLanguage.ENGLISH -> "Duration" }

            canvas1.drawText(colLord, 42f, dashaTableTop + 10f, textPaint)
            canvas1.drawText(colStart, 165f, dashaTableTop + 10f, textPaint)
            canvas1.drawText(colEnd, 255f, dashaTableTop + 10f, textPaint)
            canvas1.drawText(colDur, 345f, dashaTableTop + 10f, textPaint)

            borderPaint.color = Color.rgb(230, 230, 230)
            canvas1.drawLine(32f, dashaTableTop + 14f, boxRight, dashaTableTop + 14f, borderPaint)

            var dY = dashaTableTop + 24f
            result.dashaPeriods.take(4).forEachIndexed { idx, dasha ->
                textPaint.textAlign = Paint.Align.LEFT
                textPaint.textSize = 8f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = COLOR_MAROON
                val lordName = dasha.mahadashaLord.getName(lang)
                canvas1.drawText(lordName, 42f, dY, textPaint)

                textPaint.typeface = Typeface.DEFAULT
                textPaint.color = COLOR_DARK_TEXT
                val startStr = dasha.startDate.format(dateFmt)
                val endStr = dasha.endDate.format(dateFmt)

                val durationText = if (idx == 0) {
                    when (lang) {
                        AppLanguage.TAMIL -> "பிறப்பில் தசா இருப்பு காலம்"
                        AppLanguage.HINDI -> "जन्म समय महादशा शेष अवधि"
                        AppLanguage.ENGLISH -> "Mahadasha Balance at Birth"
                    }
                } else {
                    val yrs = java.time.temporal.ChronoUnit.YEARS.between(dasha.startDate, dasha.endDate).coerceAtLeast(1)
                    when (lang) {
                        AppLanguage.TAMIL -> "$yrs ஆண்டுகள் ($yrs Years)"
                        AppLanguage.HINDI -> "$yrs वर्ष ($yrs Years)"
                        AppLanguage.ENGLISH -> "$yrs Years"
                    }
                }

                canvas1.drawText(startStr, 165f, dY, textPaint)
                canvas1.drawText(endStr, 255f, dY, textPaint)
                canvas1.drawText(durationText, 345f, dY, textPaint)

                if (idx < 3) {
                    borderPaint.color = Color.rgb(240, 240, 240)
                    canvas1.drawLine(38f, dY + 3.5f, boxRight - 6f, dY + 3.5f, borderPaint)
                }
                dY += 12.5f
            }

            y += dashaTableHeight + 10f

            // Sani Transit & Saturn Status Card
            headerPaint.textSize = 10.5f
            headerPaint.color = COLOR_MAROON
            val saniHeader = when (lang) {
                AppLanguage.TAMIL -> "சனிப் பெயர்ச்சி நிலை (Saturn Transit Status)"
                AppLanguage.HINDI -> "शनि गोचर स्थिति (Saturn Transit Status)"
                AppLanguage.ENGLISH -> "Saturn Transit (Sani Gochara) Status"
            }
            canvas1.drawText(saniHeader, 32f, y, headerPaint)
            y += 6f

            val sani = result.saniStatus
            val saniBgColor = if (sani.isEzharaiSani || sani.isAshtamaSani) Color.rgb(255, 240, 240) else Color.rgb(240, 250, 240)
            fillPaint.color = saniBgColor
            canvas1.drawRoundRect(RectF(32f, y, boxRight, y + 46f), 6f, 6f, fillPaint)
            borderPaint.color = if (sani.isEzharaiSani || sani.isAshtamaSani) COLOR_KUMKUM else COLOR_GREEN
            canvas1.drawRoundRect(RectF(32f, y, boxRight, y + 46f), 6f, 6f, borderPaint)

            val saniStatusText = if (sani.isEzharaiSani) {
                when (lang) {
                    AppLanguage.TAMIL -> "ஏழரை சனி நடப்பு: ${sani.getEzharaiType(lang)} (சனி பகவான் சாதகமற்ற கோச்சாரத்தில் உள்ளார்)"
                    AppLanguage.HINDI -> "साढ़ेसाती सक्रिय: ${sani.getEzharaiType(lang)} (शनि देव का प्रभावशील गोचर)"
                    AppLanguage.ENGLISH -> "Sade Sati Active: ${sani.getEzharaiType(lang)} (Challenging Saturn Transit)"
                }
            } else if (sani.isAshtamaSani) {
                when (lang) {
                    AppLanguage.TAMIL -> "அஷ்டம சனி நடப்பு (எச்சரிக்கையும் கவனமும் தேவைப்படும் காலம்)"
                    AppLanguage.HINDI -> "अष्टम शनि सक्रिय (सावधानी और धैर्य की आवश्यकता)"
                    AppLanguage.ENGLISH -> "Ashtama Sani Active (Caution and patience recommended)"
                }
            } else if (sani.isKandakaSani) {
                when (lang) {
                    AppLanguage.TAMIL -> "கண்டக சனி நடப்பு"
                    AppLanguage.HINDI -> "कंटक शनि सक्रिय"
                    AppLanguage.ENGLISH -> "Kandaka Sani Active"
                }
            } else {
                when (lang) {
                    AppLanguage.TAMIL -> "ஏழரை / அஷ்டம சனி தாக்கம் இல்லை (அனுகூலமான கோச்சார காலம்)"
                    AppLanguage.HINDI -> "साढ़ेसाती / अष्टम शनि का कोई प्रभाव नहीं (अनुकूल गोचर)"
                    AppLanguage.ENGLISH -> "No adverse Sade Sati / Ashtama Sani impact (Favorable period)"
                }
            }

            val lblRemedy = when (lang) { AppLanguage.TAMIL -> "பரிகாரம்:"; AppLanguage.HINDI -> "उपाय:"; AppLanguage.ENGLISH -> "Remedy:" }

            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 9.5f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textPaint.color = if (sani.isEzharaiSani || sani.isAshtamaSani) COLOR_KUMKUM else COLOR_GREEN
            canvas1.drawText(saniStatusText, 42f, y + 15f, textPaint)

            textPaint.typeface = Typeface.DEFAULT
            textPaint.textSize = 9f
            textPaint.color = COLOR_DARK_TEXT
            canvas1.drawText("$lblRemedy ${sani.getRemedy(lang)}", 42f, y + 30f, textPaint)

            // Page 1 Footer
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.textSize = 8.5f
            textPaint.color = COLOR_GRAY_TEXT
            val footerText1 = when (lang) {
                AppLanguage.TAMIL -> "பக்கம் 1 / 2 • ஸ்ரீ சிவ சுப்ரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி"
                AppLanguage.HINDI -> "पृष्ठ 1 / 2 • श्री शिव सुब्रमण्य स्वामी मंदिर, नादी, फिजी"
                AppLanguage.ENGLISH -> "Page 1 / 2 • Sri Siva Subramaniya Swami Temple, Nadi, Fiji"
            }
            canvas1.drawText(footerText1, (PAGE_WIDTH / 2).toFloat(), (PAGE_HEIGHT - 26).toFloat(), textPaint)

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

            var y2 = 40f

            headerPaint.textAlign = Paint.Align.LEFT
            headerPaint.textSize = 10.5f
            headerPaint.color = COLOR_MAROON
            val doshaHeaderTitle = when (lang) {
                AppLanguage.TAMIL -> "தோஷ பரிசீலனை மற்றும் பரிகாரங்கள் (Dosha Assessment & Remedies)"
                AppLanguage.HINDI -> "दोष परीक्षण एवं पारंपरिक उपाय (Dosha Assessment & Remedies)"
                AppLanguage.ENGLISH -> "Dosha Assessment & Vedic Remedies"
            }
            canvas2.drawText(doshaHeaderTitle, 32f, y2, headerPaint)
            y2 += 10f

            result.doshas.forEach { dosha ->
                val cardH = if (dosha.isPresent) 48f else 36f
                val doshaBg = if (dosha.isPresent) Color.rgb(255, 245, 245) else Color.rgb(245, 255, 245)
                fillPaint.color = doshaBg
                canvas2.drawRoundRect(RectF(32f, y2, boxRight, y2 + cardH), 5f, 5f, fillPaint)
                borderPaint.color = if (dosha.isPresent) COLOR_KUMKUM else COLOR_GREEN
                borderPaint.strokeWidth = 0.8f
                canvas2.drawRoundRect(RectF(32f, y2, boxRight, y2 + cardH), 5f, 5f, borderPaint)

                textPaint.textAlign = Paint.Align.LEFT
                textPaint.textSize = 9f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = if (dosha.isPresent) COLOR_KUMKUM else COLOR_GREEN
                canvas2.drawText("${dosha.getName(lang)} - ${dosha.getSeverity(lang)}", 40f, y2 + 13f, textPaint)

                textPaint.typeface = Typeface.DEFAULT
                textPaint.textSize = 8f
                textPaint.color = COLOR_DARK_TEXT
                canvas2.drawText(dosha.getDescription(lang), 40f, y2 + 25f, textPaint)

                if (dosha.isPresent) {
                    textPaint.color = COLOR_MAROON
                    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    canvas2.drawText("$lblRemedy ${dosha.getRemedy(lang)}", 40f, y2 + 37f, textPaint)
                }

                y2 += cardH + 5f
            }

            y2 += 4f

            // Aspect Summaries
            headerPaint.textSize = 10.5f
            headerPaint.color = COLOR_MAROON
            val lifeAspectTitle = when (lang) {
                AppLanguage.TAMIL -> "திருக்கோயில் ஜாதக சுருக்கம் & முக்கிய பலன்கள் (Life Aspects Summary)"
                AppLanguage.HINDI -> "जीवन फलकथन एवं महत्वपूर्ण सारांश (Life Aspects Summary)"
                AppLanguage.ENGLISH -> "Life Aspects Analysis & Astrological Summary"
            }
            canvas2.drawText(lifeAspectTitle, 32f, y2, headerPaint)
            y2 += 8f

            val summ = result.summary
            val aspects = listOf(
                Pair(when (lang) { AppLanguage.TAMIL -> "ஆரோக்கியம் (Health)"; AppLanguage.HINDI -> "स्वास्थ्य (Health)"; AppLanguage.ENGLISH -> "Health & Vitality" }, summ.getHealth(lang)),
                Pair(when (lang) { AppLanguage.TAMIL -> "தனம் & நிதி (Wealth)"; AppLanguage.HINDI -> "धन एवं समृद्धि (Wealth)"; AppLanguage.ENGLISH -> "Wealth & Finances" }, summ.getWealth(lang)),
                Pair(when (lang) { AppLanguage.TAMIL -> "கல்வி & அறிவு (Education)"; AppLanguage.HINDI -> "शिक्षा एवं ज्ञान (Education)"; AppLanguage.ENGLISH -> "Education & Intellect" }, summ.getEducation(lang)),
                Pair(when (lang) { AppLanguage.TAMIL -> "தொழில் & வேலை (Career)"; AppLanguage.HINDI -> "व्यवसाय एवं करियर (Career)"; AppLanguage.ENGLISH -> "Career & Profession" }, summ.getCareer(lang)),
                Pair(when (lang) { AppLanguage.TAMIL -> "திருமணம் & உறவு (Marriage)"; AppLanguage.HINDI -> "विवाह एवं संबंध (Marriage)"; AppLanguage.ENGLISH -> "Marriage & Relationships" }, summ.getMarriage(lang)),
                Pair(when (lang) { AppLanguage.TAMIL -> "குடும்பம் & பூமி (Family)"; AppLanguage.HINDI -> "परिवार एवं संपत्ति (Family)"; AppLanguage.ENGLISH -> "Family & Property" }, summ.getFamily(lang)),
                Pair(when (lang) { AppLanguage.TAMIL -> "வெளிநாட்டு வாய்ப்புகள் (Travel)"; AppLanguage.HINDI -> "विदेश यात्रा (Travel)"; AppLanguage.ENGLISH -> "Foreign Travel & Prospects" }, summ.getForeignTravel(lang)),
                Pair(when (lang) { AppLanguage.TAMIL -> "தற்போதைய வழிகாட்டல் (Guidance)"; AppLanguage.HINDI -> "वर्तमान मार्गदर्शन (Guidance)"; AppLanguage.ENGLISH -> "Current Period Guidance" }, summ.getCurrentPeriodGuidance(lang))
            )

            // Pre-calculate wrapped lines for exact card height
            textPaint.textSize = 7.8f
            val maxTextWidth = boxRight - 55f
            val aspectLinesList = mutableListOf<Pair<String, List<String>>>()
            var totalContentHeight = 12f

            aspects.forEach { (title, desc) ->
                val words = desc.split(" ")
                val lines = mutableListOf<String>()
                var currentLine = ""
                for (word in words) {
                    val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                    if (textPaint.measureText(testLine) < maxTextWidth) {
                        currentLine = testLine
                    } else {
                        if (currentLine.isNotEmpty()) lines.add(currentLine)
                        currentLine = word
                    }
                }
                if (currentLine.isNotEmpty()) lines.add(currentLine)
                aspectLinesList.add(Pair(title, lines))
                totalContentHeight += 10f + (lines.size * 9.5f) + 4f
            }

            val aspectBoxTop = y2
            val aspectBoxHeight = totalContentHeight.coerceIn(210f, 255f)
            fillPaint.color = Color.WHITE
            canvas2.drawRoundRect(RectF(32f, aspectBoxTop, boxRight, aspectBoxTop + aspectBoxHeight), 6f, 6f, fillPaint)
            borderPaint.color = COLOR_BORDER
            canvas2.drawRoundRect(RectF(32f, aspectBoxTop, boxRight, aspectBoxTop + aspectBoxHeight), 6f, 6f, borderPaint)

            var aY = aspectBoxTop + 10f
            aspectLinesList.forEachIndexed { index, (title, lines) ->
                textPaint.textAlign = Paint.Align.LEFT
                textPaint.textSize = 8.2f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.color = COLOR_MAROON
                canvas2.drawText("• $title", 40f, aY, textPaint)
                aY += 9.5f

                textPaint.typeface = Typeface.DEFAULT
                textPaint.textSize = 7.6f
                textPaint.color = COLOR_DARK_TEXT
                for (line in lines) {
                    canvas2.drawText(line, 48f, aY, textPaint)
                    aY += 9f
                }
                if (index < aspectLinesList.size - 1) {
                    borderPaint.color = Color.rgb(242, 242, 242)
                    canvas2.drawLine(40f, aY + 1f, boxRight - 10f, aY + 1f, borderPaint)
                    aY += 4f
                }
            }

            // Priest Endorsement & Blessings Card
            val priestCardTop = aspectBoxTop + aspectBoxHeight + 8f
            val priestCardHeight = 40f
            val priestCardRect = RectF(32f, priestCardTop, boxRight, priestCardTop + priestCardHeight)
            val priestCardBg = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.rgb(250, 245, 235); style = Paint.Style.FILL }
            canvas2.drawRoundRect(priestCardRect, 6f, 6f, priestCardBg)
            borderPaint.color = COLOR_GOLD
            borderPaint.strokeWidth = 1f
            canvas2.drawRoundRect(priestCardRect, 6f, 6f, borderPaint)

            var py = priestCardTop + 13f
            val endorseTitle = when (lang) {
                AppLanguage.TAMIL -> "ஜாதகக் கணிப்பு அறிக்கை & ஆசிகள் வழங்கியவர்:"
                AppLanguage.HINDI -> "जन्मकुंडली फलकथन एवं शुभाशीर्वाद प्रदाता:"
                AppLanguage.ENGLISH -> "Horoscope Assessment & Divine Blessings Issued By:"
            }
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = 7.5f
            textPaint.color = COLOR_GRAY_TEXT
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas2.drawText(endorseTitle, 42f, py, textPaint)
            py += 13f

            val priestSigText = when (lang) {
                AppLanguage.TAMIL -> "மோகன் குருக்கள் • தலைமை குருக்கள், ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில், நாடி • Mobile: +6797607465"
                AppLanguage.HINDI -> "मोहन गुरुक्कल • मुख्य पुजारी, श्री शिव सुब्रमण्य स्वामी मंदिर, नादी • Mobile: +6797607465"
                AppLanguage.ENGLISH -> "Mohan Gurukkal • Head Priest, Sri Siva Subramaniya Swami Kovil, Nadi, Fiji • Mobile: +6797607465"
            }
            textPaint.textSize = 8.2f
            textPaint.color = COLOR_MAROON
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas2.drawText(priestSigText, 42f, py, textPaint)

            // Temple Blessings & Disclaimer
            val discY = (PAGE_HEIGHT - 48).toFloat()
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.textSize = 8.5f
            textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textPaint.color = COLOR_MAROON
            val blessingText = when (lang) {
                AppLanguage.TAMIL -> "வெற்றிவேல் முருகனுக்கு அரோகரா! ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருவருள் துணை! • லோகா சமஸ்தா சுகினோ பவந்து"
                AppLanguage.HINDI -> "॥ ॐ नमः शिवाय ॥ श्री शिव सुब्रमण्य स्वामी प्रसन्न ॥ लोकाः समस्ताः सुखिनो भवन्तु ॥ शुभम् ॥"
                AppLanguage.ENGLISH -> "May Lord Murugan & Sri Siva Subramaniya Swami Bestow Divine Blessings & Prosperity!"
            }
            canvas2.drawText(blessingText, (PAGE_WIDTH / 2).toFloat(), discY, textPaint)

            textPaint.typeface = Typeface.DEFAULT
            textPaint.textSize = 7.2f
            textPaint.color = COLOR_GRAY_TEXT
            canvas2.drawText(summ.getDisclaimer(lang), (PAGE_WIDTH / 2).toFloat(), discY + 10f, textPaint)

            // Page 2 Footer
            val footerText2 = when (lang) {
                AppLanguage.TAMIL -> "பக்கம் 2 / 2 • ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி"
                AppLanguage.HINDI -> "पृष्ठ 2 / 2 • श्री शिव सुब्रमण्य स्वामी मंदिर, नादी, फिजी"
                AppLanguage.ENGLISH -> "Page 2 / 2 • Sri Siva Subramaniya Swami Kovil, Nadi, Fiji"
            }
            canvas2.drawText(footerText2, (PAGE_WIDTH / 2).toFloat(), (PAGE_HEIGHT - 22).toFloat(), textPaint)

            pdfDocument.finishPage(page2)

            // Write PDF to cache directory
            val fileName = "Jathagam_${result.devoteeName.replace(" ", "_")}_${lang.code}_${System.currentTimeMillis()}.pdf"
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

    fun shareOrOpenPdf(context: Context, pdfFile: File, title: String = "Jathagam Horoscope PDF") {
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
                putExtra(Intent.EXTRA_TEXT, title)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, title)
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "PDF Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}

