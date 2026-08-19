package com.example.data.service

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.data.model.*
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class WeddingMatchPdfData(
    val brideName: String,
    val brideDob: LocalDate,
    val brideTob: LocalTime,
    val bridePlace: String,
    val brideRasi: Rasi,
    val brideNakshatraName: String,
    val bridePada: Int,
    val brideLagna: Rasi?,
    val brideMarsHouse: Int,

    val groomName: String,
    val groomDob: LocalDate,
    val groomTob: LocalTime,
    val groomPlace: String,
    val groomRasi: Rasi,
    val groomNakshatraName: String,
    val groomPada: Int,
    val groomLagna: Rasi?,
    val groomMarsHouse: Int,

    val result: WeddingMatchResult,
    val language: AppLanguage
)

object WeddingMatchPdfExporter {

    private const val PAGE_WIDTH = 595 // A4 standard point width (72 dpi)
    private const val PAGE_HEIGHT = 842 // A4 standard point height (72 dpi)

    fun generatePdf(context: Context, data: WeddingMatchPdfData): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        drawMatchmakingReport(canvas, data)

        document.finishPage(page)

        // Save to cache directory
        val cacheDir = File(context.cacheDir, "pdf")
        if (!cacheDir.exists()) cacheDir.mkdirs()

        val fileName = "Wedding_Match_${System.currentTimeMillis()}.pdf"
        val pdfFile = File(cacheDir, fileName)

        val outputStream = FileOutputStream(pdfFile)
        document.writeTo(outputStream)
        outputStream.flush()
        outputStream.close()
        document.close()

        return pdfFile
    }

    fun sharePdf(context: Context, pdfFile: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            pdfFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Wedding Match Report - Sri Siva Subramaniya Swami Kovil")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Wedding Match PDF"))
    }

    private fun drawMatchmakingReport(canvas: Canvas, data: WeddingMatchPdfData) {
        val lang = data.language

        // Paints
        val maroonPaint = Paint().apply {
            color = Color.rgb(128, 0, 32)
            isAntiAlias = true
        }

        val goldPaint = Paint().apply {
            color = Color.rgb(212, 160, 23)
            isAntiAlias = true
        }

        val textPaint = Paint().apply {
            color = Color.rgb(30, 30, 30)
            textSize = 10f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val boldTextPaint = Paint().apply {
            color = Color.rgb(20, 20, 20)
            textSize = 11f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val headerTitlePaint = Paint().apply {
            color = Color.rgb(128, 0, 32)
            textSize = 15f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }

        val subHeaderPaint = Paint().apply {
            color = Color.rgb(100, 100, 100)
            textSize = 9f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val borderPaint = Paint().apply {
            color = Color.rgb(212, 160, 23)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }

        val lightBgPaint = Paint().apply {
            color = Color.rgb(253, 248, 240)
            style = Paint.Style.FILL
        }

        val tableHeaderPaint = Paint().apply {
            color = Color.rgb(128, 0, 32)
            style = Paint.Style.FILL
        }

        val whiteTextPaint = Paint().apply {
            color = Color.WHITE
            textSize = 10f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        // Draw Outer Border
        val margin = 24f
        canvas.drawRect(margin, margin, PAGE_WIDTH - margin, PAGE_HEIGHT - margin, borderPaint)
        canvas.drawRect(margin + 3, margin + 3, PAGE_WIDTH - margin - 3, PAGE_HEIGHT - margin - 3, borderPaint)

        var y = 50f

        // 1. Temple Header
        val templeName = when (lang) {
            AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில்"
            AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी मंदिर"
            AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Kovil"
        }
        canvas.drawText(templeName, PAGE_WIDTH / 2f, y, headerTitlePaint)
        y += 14f

        val templeSub = when (lang) {
            AppLanguage.TAMIL -> "நாடி, பிஜி தீவுகள் • துல்லிய திருக்கணித ஜோதிட திருமணப் பொருத்த அறிக்கை"
            AppLanguage.HINDI -> "नादी, फिजी • वैदिक ज्योतिष विवाह मिलान प्रमाण पत्र"
            AppLanguage.ENGLISH -> "Nadi, Fiji Islands • Vedic Astrological Matchmaking Report"
        }
        canvas.drawText(templeSub, PAGE_WIDTH / 2f, y, subHeaderPaint)
        y += 18f

        // Decorative line
        canvas.drawLine(margin + 20, y, PAGE_WIDTH - margin - 20, y, goldPaint)
        y += 16f

        // 2. Groom & Bride Information Table (Side-by-side)
        val boxWidth = (PAGE_WIDTH - (margin * 2) - 16) / 2
        val brideBoxLeft = margin + 8
        val groomBoxLeft = brideBoxLeft + boxWidth + 8
        val boxHeight = 110f

        // Draw Bride Box
        val brideBoxRect = RectF(brideBoxLeft, y, brideBoxLeft + boxWidth, y + boxHeight)
        canvas.drawRoundRect(brideBoxRect, 8f, 8f, lightBgPaint)
        canvas.drawRoundRect(brideBoxRect, 8f, 8f, borderPaint)

        // Draw Groom Box
        val groomBoxRect = RectF(groomBoxLeft, y, groomBoxLeft + boxWidth, y + boxHeight)
        canvas.drawRoundRect(groomBoxRect, 8f, 8f, lightBgPaint)
        canvas.drawRoundRect(groomBoxRect, 8f, 8f, borderPaint)

        // Fill Bride Info
        var by = y + 16
        val bx = brideBoxLeft + 10
        val brideTitle = when (lang) {
            AppLanguage.TAMIL -> "மணமகள் விபரம் (Bride)"
            AppLanguage.HINDI -> "कन्या (वधू) विवरण"
            AppLanguage.ENGLISH -> "Bride Details"
        }
        canvas.drawText(brideTitle, bx, by, boldTextPaint.apply { color = Color.rgb(194, 24, 91) })
        by += 14
        canvas.drawText("${getLabel("Name", lang)}: ${data.brideName.ifBlank { "-" }}", bx, by, textPaint)
        by += 13
        canvas.drawText("${getLabel("DOB", lang)}: ${data.brideDob.format(DateTimeFormatter.ISO_LOCAL_DATE)} (${data.brideTob})", bx, by, textPaint)
        by += 13
        canvas.drawText("${getLabel("Place", lang)}: ${data.bridePlace}", bx, by, textPaint)
        by += 13
        canvas.drawText("${getLabel("Rasi", lang)}: ${data.brideRasi.getName(lang)} (${data.brideRasi.getLord(lang)})", bx, by, textPaint)
        by += 13
        canvas.drawText("${getLabel("Star", lang)}: ${data.brideNakshatraName} - ${getLabel("Pada", lang)} ${data.bridePada}", bx, by, textPaint)
        by += 13
        val brideLagnaStr = data.brideLagna?.getName(lang) ?: "-"
        val brideMarsStr = "${data.brideMarsHouse}-ஆம் இடம் (${if (data.brideMarsHouse in listOf(2,4,7,8,12)) "தோஷம்" else "சுபம்"})"
        canvas.drawText("${getLabel("Lagna", lang)}: $brideLagnaStr | செவ்வாய்: $brideMarsStr", bx, by, textPaint)

        // Fill Groom Info
        var gy = y + 16
        val gx = groomBoxLeft + 10
        val groomTitle = when (lang) {
            AppLanguage.TAMIL -> "மணமகன் விபரம் (Groom)"
            AppLanguage.HINDI -> "वर (दूल्हा) विवरण"
            AppLanguage.ENGLISH -> "Groom Details"
        }
        canvas.drawText(groomTitle, gx, gy, boldTextPaint.apply { color = Color.rgb(128, 0, 32) })
        gy += 14
        canvas.drawText("${getLabel("Name", lang)}: ${data.groomName.ifBlank { "-" }}", gx, gy, textPaint)
        gy += 13
        canvas.drawText("${getLabel("DOB", lang)}: ${data.groomDob.format(DateTimeFormatter.ISO_LOCAL_DATE)} (${data.groomTob})", gx, gy, textPaint)
        gy += 13
        canvas.drawText("${getLabel("Place", lang)}: ${data.groomPlace}", gx, gy, textPaint)
        gy += 13
        canvas.drawText("${getLabel("Rasi", lang)}: ${data.groomRasi.getName(lang)} (${data.groomRasi.getLord(lang)})", gx, gy, textPaint)
        gy += 13
        canvas.drawText("${getLabel("Star", lang)}: ${data.groomNakshatraName} - ${getLabel("Pada", lang)} ${data.groomPada}", gx, gy, textPaint)
        gy += 13
        val groomLagnaStr = data.groomLagna?.getName(lang) ?: "-"
        val groomMarsStr = "${data.groomMarsHouse}-ஆம் இடம் (${if (data.groomMarsHouse in listOf(2,4,7,8,12)) "தோஷம்" else "சுபம்"})"
        canvas.drawText("${getLabel("Lagna", lang)}: $groomLagnaStr | செவ்வாய்: $groomMarsStr", gx, gy, textPaint)

        y += boxHeight + 14

        // 3. Verdict Banner
        val result = data.result
        val verdictBgColor = when (result.verdictStatus) {
            PoruthamStatus.UTTHAMAM -> Color.rgb(232, 245, 233)
            PoruthamStatus.MADHYAMAM -> Color.rgb(255, 243, 224)
            PoruthamStatus.PORUNDHADHU -> Color.rgb(255, 235, 238)
        }
        val verdictTextColor = when (result.verdictStatus) {
            PoruthamStatus.UTTHAMAM -> Color.rgb(46, 125, 50)
            PoruthamStatus.MADHYAMAM -> Color.rgb(230, 81, 0)
            PoruthamStatus.PORUNDHADHU -> Color.rgb(198, 40, 40)
        }

        val bannerRect = RectF(margin + 8, y, PAGE_WIDTH - margin - 8, y + 42)
        val bannerPaint = Paint().apply { color = verdictBgColor }
        canvas.drawRoundRect(bannerRect, 6f, 6f, bannerPaint)

        val vPaint = Paint().apply {
            color = verdictTextColor
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val scoreStr = "${getLabel("TotalScore", lang)}: ${result.totalPoruthamsMatched} / 10 (${result.totalScore} / 10.0) - ${result.verdictStatus.getName(lang)}"
        canvas.drawText(scoreStr, margin + 18, y + 18, vPaint)

        val rajjuStr = if (result.rajjuMatch) "✓ ரஜ்ஜு சுபம்" else "✗ ரஜ்ஜு தோஷம்"
        val samyamStr = if (result.sevvayDosham.doshaSamyamStatusTa.contains("உண்டு")) "✓ செவ்வாய் சமநிலை" else "⚠ தோஷ நிவர்த்தி தேவை"
        val subVerdictPaint = Paint().apply {
            color = Color.rgb(60, 60, 60)
            textSize = 9.5f
            isAntiAlias = true
        }
        canvas.drawText("ரஜ்ஜு: $rajjuStr   |   செவ்வாய் தோஷம்: $samyamStr", margin + 18, y + 33, subVerdictPaint)

        y += 52

        // 4. 10 Poruthams Table
        val tableLeft = margin + 8
        val tableRight = PAGE_WIDTH - margin - 8
        val colWidths = floatArrayOf(28f, 110f, 180f, 90f, 45f)
        val colPositions = floatArrayOf(
            tableLeft,
            tableLeft + colWidths[0],
            tableLeft + colWidths[0] + colWidths[1],
            tableLeft + colWidths[0] + colWidths[1] + colWidths[2],
            tableLeft + colWidths[0] + colWidths[1] + colWidths[2] + colWidths[3]
        )

        // Draw Table Header
        val headerHeight = 20f
        canvas.drawRect(tableLeft, y, tableRight, y + headerHeight, tableHeaderPaint)

        canvas.drawText("எண்", colPositions[0] + 4, y + 14, whiteTextPaint)
        canvas.drawText("பொருத்தம்", colPositions[1] + 4, y + 14, whiteTextPaint)
        canvas.drawText("முக்கிய பலன்", colPositions[2] + 4, y + 14, whiteTextPaint)
        canvas.drawText("முடிவு", colPositions[3] + 4, y + 14, whiteTextPaint)
        canvas.drawText("புள்ளி", colPositions[4] + 4, y + 14, whiteTextPaint)
        y += headerHeight

        val rowPaint = Paint().apply {
            color = Color.rgb(250, 250, 250)
            style = Paint.Style.FILL
        }
        val altRowPaint = Paint().apply {
            color = Color.rgb(242, 242, 242)
            style = Paint.Style.FILL
        }
        val linePaint = Paint().apply {
            color = Color.rgb(220, 220, 220)
            strokeWidth = 1f
        }

        result.poruthams.forEachIndexed { index, p ->
            val rowHeight = 22f
            canvas.drawRect(tableLeft, y, tableRight, y + rowHeight, if (index % 2 == 0) rowPaint else altRowPaint)

            val statusColor = when (p.status) {
                PoruthamStatus.UTTHAMAM -> Color.rgb(46, 125, 50)
                PoruthamStatus.MADHYAMAM -> Color.rgb(230, 81, 0)
                PoruthamStatus.PORUNDHADHU -> Color.rgb(198, 40, 40)
            }
            val statusPaint = Paint().apply {
                color = statusColor
                textSize = 9.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            canvas.drawText("${index + 1}", colPositions[0] + 6, y + 15, textPaint)
            canvas.drawText(p.getName(lang), colPositions[1] + 4, y + 15, boldTextPaint.apply { color = Color.BLACK; textSize = 9.5f })

            val expShort = p.getExplanation(lang).take(38) + if (p.getExplanation(lang).length > 38) "..." else ""
            canvas.drawText(expShort, colPositions[2] + 4, y + 15, textPaint.apply { textSize = 8.5f })

            canvas.drawText(p.status.getName(lang), colPositions[3] + 4, y + 15, statusPaint)
            canvas.drawText("${p.pointsEarned}/${p.maxPoints}", colPositions[4] + 6, y + 15, boldTextPaint.apply { textSize = 9.5f })

            canvas.drawLine(tableLeft, y + rowHeight, tableRight, y + rowHeight, linePaint)
            y += rowHeight
        }

        y += 14

        // 5. Sevvay Dosham Analysis Box
        val sevvayBoxRect = RectF(margin + 8, y, PAGE_WIDTH - margin - 8, y + 80)
        canvas.drawRoundRect(sevvayBoxRect, 6f, 6f, lightBgPaint)
        canvas.drawRoundRect(sevvayBoxRect, 6f, 6f, borderPaint)

        var sy = y + 16
        val sx = margin + 18
        canvas.drawText("செவ்வாய் தோஷ விளக்கம் & தோஷ சாம்யம் (Kuja Dosha Analysis)", sx, sy, boldTextPaint.apply { color = Color.rgb(128, 0, 32); textSize = 10.5f })
        sy += 15

        val brideSevTxt = "மணமகள்: ${result.sevvayDosham.brideDoshamSeverity} ${result.sevvayDosham.brideCancellationReasonTa ?: ""}"
        canvas.drawText(brideSevTxt, sx, sy, textPaint.apply { textSize = 9f })
        sy += 14

        val groomSevTxt = "மணமகன்: ${result.sevvayDosham.groomDoshamSeverity} ${result.sevvayDosham.groomCancellationReasonTa ?: ""}"
        canvas.drawText(groomSevTxt, sx, sy, textPaint.apply { textSize = 9f })
        sy += 14

        val samyamTxt = "தோஷ சமநிலை: ${result.sevvayDosham.doshaSamyamStatusTa} • ${result.sevvayDosham.recommendationTa}"
        canvas.drawText(samyamTxt.take(85), sx, sy, boldTextPaint.apply { color = Color.rgb(40, 40, 40); textSize = 9f })

        // 6. Footer
        val footerPaint = Paint().apply {
            color = Color.rgb(128, 0, 32)
            textSize = 9f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("ஓம் சரவணபவ • ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி துணை • சுபம்", PAGE_WIDTH / 2f, (PAGE_HEIGHT - 35).toFloat(), footerPaint)
    }

    private fun getLabel(key: String, lang: AppLanguage): String = when (key) {
        "Name" -> when (lang) {
            AppLanguage.TAMIL -> "பெயர்"
            AppLanguage.HINDI -> "नाम"
            AppLanguage.ENGLISH -> "Name"
        }
        "DOB" -> when (lang) {
            AppLanguage.TAMIL -> "பிறந்த தேதி"
            AppLanguage.HINDI -> "जन्म तिथि"
            AppLanguage.ENGLISH -> "DOB"
        }
        "Place" -> when (lang) {
            AppLanguage.TAMIL -> "பிறந்த இடம்"
            AppLanguage.HINDI -> "जन्म स्थान"
            AppLanguage.ENGLISH -> "Place"
        }
        "Rasi" -> when (lang) {
            AppLanguage.TAMIL -> "ராசி"
            AppLanguage.HINDI -> "राशि"
            AppLanguage.ENGLISH -> "Rasi"
        }
        "Star" -> when (lang) {
            AppLanguage.TAMIL -> "நட்சத்திரம்"
            AppLanguage.HINDI -> "नक्षत्र"
            AppLanguage.ENGLISH -> "Nakshatram"
        }
        "Pada" -> when (lang) {
            AppLanguage.TAMIL -> "பாதம்"
            AppLanguage.HINDI -> "चरण"
            AppLanguage.ENGLISH -> "Pada"
        }
        "Lagna" -> when (lang) {
            AppLanguage.TAMIL -> "லக்னம்"
            AppLanguage.HINDI -> "लग्न"
            AppLanguage.ENGLISH -> "Lagna"
        }
        "TotalScore" -> when (lang) {
            AppLanguage.TAMIL -> "மொத்த பொருத்தம்"
            AppLanguage.HINDI -> "कुल मिलान"
            AppLanguage.ENGLISH -> "Total Match Score"
        }
        else -> key
    }
}
