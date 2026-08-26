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
            putExtra(Intent.EXTRA_SUBJECT, "Wedding Match Certificate - Head Priest Mohan Gurukkal (+6797607465)")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Share Wedding Match PDF")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private fun drawMatchmakingReport(canvas: Canvas, data: WeddingMatchPdfData) {
        val lang = data.language

        // Colors
        val deepMaroon = Color.rgb(122, 21, 38)
        val templeGold = Color.rgb(200, 155, 60)
        val borderGold = Color.rgb(180, 135, 45)
        val warmBg = Color.rgb(255, 253, 248)
        val cardBg = Color.rgb(252, 249, 242)
        val textDark = Color.rgb(25, 25, 25)
        val textMuted = Color.rgb(85, 85, 85)

        // Base background fill
        val bgPaint = Paint().apply { color = warmBg; style = Paint.Style.FILL }
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), bgPaint)

        // Paints
        val invocationPaint = Paint().apply {
            color = deepMaroon
            textSize = 9f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }

        val templeTitlePaint = Paint().apply {
            color = deepMaroon
            textSize = 13f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }

        val priestPaint = Paint().apply {
            color = deepMaroon
            textSize = 9.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val subHeaderPaint = Paint().apply {
            color = textMuted
            textSize = 8f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val goldLinePaint = Paint().apply {
            color = templeGold
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
            isAntiAlias = true
        }

        val outerBorderPaint = Paint().apply {
            color = deepMaroon
            style = Paint.Style.STROKE
            strokeWidth = 2.2f
            isAntiAlias = true
        }

        val innerBorderPaint = Paint().apply {
            color = borderGold
            style = Paint.Style.STROKE
            strokeWidth = 0.8f
            isAntiAlias = true
        }

        val cardBgPaint = Paint().apply {
            color = cardBg
            style = Paint.Style.FILL
        }

        val cardBorderPaint = Paint().apply {
            color = Color.rgb(220, 200, 160)
            style = Paint.Style.STROKE
            strokeWidth = 0.8f
            isAntiAlias = true
        }

        val textPaint = Paint().apply {
            color = textDark
            textSize = 8f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val labelPaint = Paint().apply {
            color = textMuted
            textSize = 7.8f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val boldTextPaint = Paint().apply {
            color = textDark
            textSize = 8.2f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val tableHeaderPaint = Paint().apply {
            color = deepMaroon
            style = Paint.Style.FILL
        }

        val whiteTextPaint = Paint().apply {
            color = Color.WHITE
            textSize = 8.2f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        // 1. South Indian Ornamental Double Border
        val m = 18f
        canvas.drawRect(m, m, PAGE_WIDTH - m, PAGE_HEIGHT - m, outerBorderPaint)
        canvas.drawRect(m + 3.5f, m + 3.5f, PAGE_WIDTH - m - 3.5f, PAGE_HEIGHT - m - 3.5f, innerBorderPaint)

        // Corner Diamond Accents
        drawCornerDiamond(canvas, m + 3.5f, m + 3.5f, templeGold)
        drawCornerDiamond(canvas, PAGE_WIDTH - m - 3.5f, m + 3.5f, templeGold)
        drawCornerDiamond(canvas, m + 3.5f, PAGE_HEIGHT - m - 3.5f, templeGold)
        drawCornerDiamond(canvas, PAGE_WIDTH - m - 3.5f, PAGE_HEIGHT - m - 3.5f, templeGold)

        var y = 33f

        // 2. Invocation
        val invocationText = when (lang) {
            AppLanguage.TAMIL -> "|| ஸ்ரீ கணேசாய நம: ||   || சுபமஸ்து ||   || குருப்யோ நம: ||"
            AppLanguage.HINDI -> "॥ श्री गणेशाय नमः ॥   ॥ शुभमस्तु ॥   ॥ श्री गुरुभ्यो नमः ॥"
            AppLanguage.ENGLISH -> "|| Sri Ganeshaya Namah ||   || Subhamastu ||   || Gurubhyo Namah ||"
        }
        canvas.drawText(invocationText, PAGE_WIDTH / 2f, y, invocationPaint)
        y += 14f

        // 3. Temple Name
        val templeName = when (lang) {
            AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில்"
            AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी मंदिर"
            AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Kovil"
        }
        canvas.drawText(templeName, PAGE_WIDTH / 2f, y, templeTitlePaint)
        y += 12f

        val subtitle = when (lang) {
            AppLanguage.TAMIL -> "நாடி, பிஜி தீவுகள் • துல்லிய வேத திருமணப் பொருத்த அறிக்கை"
            AppLanguage.HINDI -> "नादी, फिजी द्वीप • वैदिक विवाह मिलान प्रमाण पत्र"
            AppLanguage.ENGLISH -> "Nadi, Fiji Islands • Vedic Marriage Compatibility Certificate"
        }
        canvas.drawText(subtitle, PAGE_WIDTH / 2f, y, subHeaderPaint)
        y += 10f

        // Ornamental Separator Line
        canvas.drawLine(m + 20f, y, PAGE_WIDTH - m - 20f, y, goldLinePaint)
        y += 8f

        // 4. Side-by-Side Bride & Groom Profiles
        val contentLeft = m + 8f
        val contentRight = PAGE_WIDTH - m - 8f
        val totalAvailableWidth = contentRight - contentLeft
        val cardGap = 8f
        val cardWidth = (totalAvailableWidth - cardGap) / 2f
        val brideLeft = contentLeft
        val groomLeft = brideLeft + cardWidth + cardGap
        val cardHeight = 104f

        // --- Bride Card ---
        val brideRect = RectF(brideLeft, y, brideLeft + cardWidth, y + cardHeight)
        canvas.drawRoundRect(brideRect, 5f, 5f, cardBgPaint)
        canvas.drawRoundRect(brideRect, 5f, 5f, cardBorderPaint)

        val brideHeaderRect = RectF(brideLeft, y, brideLeft + cardWidth, y + 16f)
        val brideHeaderPaint = Paint().apply { color = Color.rgb(180, 30, 80) }
        canvas.drawRoundRect(brideHeaderRect, 5f, 5f, brideHeaderPaint)
        canvas.drawRect(brideLeft, y + 8f, brideLeft + cardWidth, y + 16f, brideHeaderPaint)
        val brideHeaderTitle = when (lang) {
            AppLanguage.TAMIL -> "மணமகள் விபரம்"
            AppLanguage.HINDI -> "वधू (कन्या) विवरण"
            AppLanguage.ENGLISH -> "Bride Profile"
        }
        canvas.drawText(brideHeaderTitle, brideLeft + 8f, y + 11.5f, whiteTextPaint)

        var by = y + 27f
        val bx = brideLeft + 8f
        val maxValWidth = cardWidth - 16f

        drawField(canvas, bx, by, getLabel("Name", lang), data.brideName.ifBlank { "-" }, labelPaint, boldTextPaint, maxValWidth)
        by += 12.5f

        val dobStr = "${data.brideDob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} (${String.format("%02d:%02d", data.brideTob.hour, data.brideTob.minute)})"
        drawField(canvas, bx, by, getLabel("DOB", lang), dobStr, labelPaint, textPaint, maxValWidth)
        by += 12.5f

        drawField(canvas, bx, by, getLabel("Place", lang), data.bridePlace.ifBlank { "-" }.take(22), labelPaint, textPaint, maxValWidth)
        by += 12.5f

        val brideRasiStr = "${data.brideRasi.getName(lang)} (${data.brideRasi.getLord(lang)})"
        drawField(canvas, bx, by, getLabel("Rasi", lang), brideRasiStr, labelPaint, textPaint, maxValWidth)
        by += 12.5f

        val brideStarStr = when (lang) {
            AppLanguage.TAMIL -> "${data.brideNakshatraName} (${data.bridePada}-ஆம் பாதம்)"
            AppLanguage.HINDI -> "${data.brideNakshatraName} (चरण ${data.bridePada})"
            AppLanguage.ENGLISH -> "${data.brideNakshatraName} (Pada ${data.bridePada})"
        }
        drawField(canvas, bx, by, getLabel("Star", lang), brideStarStr, labelPaint, textPaint, maxValWidth)
        by += 12.5f

        val brideLagnaStr = data.brideLagna?.getName(lang) ?: "-"
        val brideMarsDoshaStr = if (data.brideMarsHouse in listOf(2, 4, 7, 8, 12)) getLabel("DoshamPresent", lang) else getLabel("NoDosham", lang)
        val brideLagnaMarsStr = when (lang) {
            AppLanguage.TAMIL -> "$brideLagnaStr • செவ்: ${data.brideMarsHouse} ($brideMarsDoshaStr)"
            AppLanguage.HINDI -> "$brideLagnaStr • मंगल: ${data.brideMarsHouse} ($brideMarsDoshaStr)"
            AppLanguage.ENGLISH -> "$brideLagnaStr • Mars: House ${data.brideMarsHouse} ($brideMarsDoshaStr)"
        }
        drawField(canvas, bx, by, getLabel("Lagna", lang), brideLagnaMarsStr, labelPaint, textPaint, maxValWidth)

        // --- Groom Card ---
        val groomRect = RectF(groomLeft, y, groomLeft + cardWidth, y + cardHeight)
        canvas.drawRoundRect(groomRect, 5f, 5f, cardBgPaint)
        canvas.drawRoundRect(groomRect, 5f, 5f, cardBorderPaint)

        val groomHeaderRect = RectF(groomLeft, y, groomLeft + cardWidth, y + 16f)
        val groomHeaderPaint = Paint().apply { color = deepMaroon }
        canvas.drawRoundRect(groomHeaderRect, 5f, 5f, groomHeaderPaint)
        canvas.drawRect(groomLeft, y + 8f, groomLeft + cardWidth, y + 16f, groomHeaderPaint)
        val groomHeaderTitle = when (lang) {
            AppLanguage.TAMIL -> "மணமகன் விபரம்"
            AppLanguage.HINDI -> "वर (दूल्हा) विवरण"
            AppLanguage.ENGLISH -> "Groom Profile"
        }
        canvas.drawText(groomHeaderTitle, groomLeft + 8f, y + 11.5f, whiteTextPaint)

        var gy = y + 27f
        val gx = groomLeft + 8f

        drawField(canvas, gx, gy, getLabel("Name", lang), data.groomName.ifBlank { "-" }, labelPaint, boldTextPaint, maxValWidth)
        gy += 12.5f

        val groomDobStr = "${data.groomDob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} (${String.format("%02d:%02d", data.groomTob.hour, data.groomTob.minute)})"
        drawField(canvas, gx, gy, getLabel("DOB", lang), groomDobStr, labelPaint, textPaint, maxValWidth)
        gy += 12.5f

        drawField(canvas, gx, gy, getLabel("Place", lang), data.groomPlace.ifBlank { "-" }.take(22), labelPaint, textPaint, maxValWidth)
        gy += 12.5f

        val groomRasiStr = "${data.groomRasi.getName(lang)} (${data.groomRasi.getLord(lang)})"
        drawField(canvas, gx, gy, getLabel("Rasi", lang), groomRasiStr, labelPaint, textPaint, maxValWidth)
        gy += 12.5f

        val groomStarStr = when (lang) {
            AppLanguage.TAMIL -> "${data.groomNakshatraName} (${data.groomPada}-ஆம் பாதம்)"
            AppLanguage.HINDI -> "${data.groomNakshatraName} (चरण ${data.groomPada})"
            AppLanguage.ENGLISH -> "${data.groomNakshatraName} (Pada ${data.groomPada})"
        }
        drawField(canvas, gx, gy, getLabel("Star", lang), groomStarStr, labelPaint, textPaint, maxValWidth)
        gy += 12.5f

        val groomLagnaStr = data.groomLagna?.getName(lang) ?: "-"
        val groomMarsDoshaStr = if (data.groomMarsHouse in listOf(2, 4, 7, 8, 12)) getLabel("DoshamPresent", lang) else getLabel("NoDosham", lang)
        val groomLagnaMarsStr = when (lang) {
            AppLanguage.TAMIL -> "$groomLagnaStr • செவ்: ${data.groomMarsHouse} ($groomMarsDoshaStr)"
            AppLanguage.HINDI -> "$groomLagnaStr • मंगल: ${data.groomMarsHouse} ($groomMarsDoshaStr)"
            AppLanguage.ENGLISH -> "$groomLagnaStr • Mars: House ${data.groomMarsHouse} ($groomMarsDoshaStr)"
        }
        drawField(canvas, gx, gy, getLabel("Lagna", lang), groomLagnaMarsStr, labelPaint, textPaint, maxValWidth)

        y += cardHeight + 7f

        // 5. Verdict & Score Banner
        val result = data.result
        val verdictBgColor = when (result.verdictStatus) {
            PoruthamStatus.UTTHAMAM -> Color.rgb(238, 250, 240)
            PoruthamStatus.MADHYAMAM -> Color.rgb(255, 248, 230)
            PoruthamStatus.PORUNDHADHU -> Color.rgb(255, 240, 240)
        }
        val verdictBorderColor = when (result.verdictStatus) {
            PoruthamStatus.UTTHAMAM -> Color.rgb(76, 175, 80)
            PoruthamStatus.MADHYAMAM -> Color.rgb(255, 167, 38)
            PoruthamStatus.PORUNDHADHU -> Color.rgb(239, 83, 80)
        }
        val verdictTextColor = when (result.verdictStatus) {
            PoruthamStatus.UTTHAMAM -> Color.rgb(30, 115, 45)
            PoruthamStatus.MADHYAMAM -> Color.rgb(190, 85, 0)
            PoruthamStatus.PORUNDHADHU -> Color.rgb(180, 25, 25)
        }

        val bannerHeight = 34f
        val bannerRect = RectF(contentLeft, y, contentRight, y + bannerHeight)
        val bannerBgPaint = Paint().apply { color = verdictBgColor; style = Paint.Style.FILL }
        val bannerBorderPaint = Paint().apply { color = verdictBorderColor; style = Paint.Style.STROKE; strokeWidth = 1f; isAntiAlias = true }
        canvas.drawRoundRect(bannerRect, 5f, 5f, bannerBgPaint)
        canvas.drawRoundRect(bannerRect, 5f, 5f, bannerBorderPaint)

        val vPaint = Paint().apply {
            color = verdictTextColor
            textSize = 9.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val scoreStr = "${getLabel("TotalScore", lang)}: ${result.totalPoruthamsMatched} / 10 (${result.totalScore} / 10.0) — ${result.verdictStatus.getName(lang)}"
        canvas.drawText(scoreStr, contentLeft + 8f, y + 13.5f, vPaint)

        val rajjuLocalized = if (result.rajjuMatch) getLabel("RajjuAuspicious", lang) else getLabel("RajjuDosham", lang)
        val samyamLocalized = if (result.sevvayDosham.doshaSamyamStatusTa.contains("உண்டு")) getLabel("MarsBalanced", lang) else getLabel("MarsRemedyNeeded", lang)

        val subVerdictPaint = Paint().apply {
            color = textDark
            textSize = 7.8f
            isAntiAlias = true
        }
        val subLine = "${getLabel("Rajju", lang)}: $rajjuLocalized   •   ${getLabel("KujaDoshaShort", lang)}: $samyamLocalized"
        canvas.drawText(subLine, contentLeft + 8f, y + 27f, subVerdictPaint)

        y += bannerHeight + 7f

        // 6. 10 Poruthams Table
        val tableLeft = contentLeft
        val tableRight = contentRight
        val tableWidth = tableRight - tableLeft

        // Column widths strictly calibrated:
        // Col 0: No (22f)
        // Col 1: Porutham Name (112f)
        // Col 2: Explanation (260f)
        // Col 3: Verdict (95f)
        // Col 4: Score (54f)
        val w0 = 22f
        val w1 = 112f
        val w2 = 260f
        val w3 = 95f
        val w4 = tableWidth - (w0 + w1 + w2 + w3) // 54f

        val col0 = tableLeft
        val col1 = col0 + w0
        val col2 = col1 + w1
        val col3 = col2 + w2
        val col4 = col3 + w3

        val headerHeight = 16f
        canvas.drawRect(tableLeft, y, tableRight, y + headerHeight, tableHeaderPaint)

        canvas.drawText(getLabel("ColNo", lang), col0 + 4f, y + 11.5f, whiteTextPaint)
        canvas.drawText(getLabel("ColPorutham", lang), col1 + 4f, y + 11.5f, whiteTextPaint)
        canvas.drawText(getLabel("ColSignificance", lang), col2 + 4f, y + 11.5f, whiteTextPaint)
        canvas.drawText(getLabel("ColVerdict", lang), col3 + 4f, y + 11.5f, whiteTextPaint)
        canvas.drawText(getLabel("ColPoints", lang), col4 + 4f, y + 11.5f, whiteTextPaint)
        y += headerHeight

        val rowPaintEven = Paint().apply { color = Color.rgb(255, 255, 255); style = Paint.Style.FILL }
        val rowPaintOdd = Paint().apply { color = Color.rgb(250, 247, 240); style = Paint.Style.FILL }
        val gridLinePaint = Paint().apply { color = Color.rgb(228, 220, 205); strokeWidth = 0.6f }

        val rowHeight = 24f
        result.poruthams.forEachIndexed { index, p ->
            val rowY = y
            canvas.drawRect(tableLeft, rowY, tableRight, rowY + rowHeight, if (index % 2 == 0) rowPaintEven else rowPaintOdd)

            val statusColor = when (p.status) {
                PoruthamStatus.UTTHAMAM -> Color.rgb(35, 125, 40)
                PoruthamStatus.MADHYAMAM -> Color.rgb(195, 90, 0)
                PoruthamStatus.PORUNDHADHU -> Color.rgb(190, 25, 25)
            }
            val statusPaint = Paint().apply {
                color = statusColor
                textSize = 7.8f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            // Col 0: No
            canvas.drawText("${index + 1}", col0 + 5f, rowY + 15f, textPaint.apply { textSize = 7.8f })

            // Col 1: Porutham Name (Clean & Formatted)
            val pName = getStandardPoruthamName(index, lang, p.getName(lang))
            canvas.drawText(pName, col1 + 4f, rowY + 15f, boldTextPaint.apply { textSize = 7.8f })

            // Col 2: Explanation (Clean 2-line wrapping so meaning is fully visible without cutoffs)
            val rawExp = p.getExplanation(lang)
            val expPaint = Paint().apply {
                color = textDark
                textSize = 6.9f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            val maxCol2Width = w2 - 8f
            val expLines = wrapTextToLines(rawExp, expPaint, maxCol2Width, 2)
            if (expLines.size <= 1) {
                canvas.drawText(expLines.getOrElse(0) { "" }, col2 + 4f, rowY + 15f, expPaint)
            } else {
                canvas.drawText(expLines[0], col2 + 4f, rowY + 10f, expPaint)
                canvas.drawText(expLines[1], col2 + 4f, rowY + 19.5f, expPaint)
            }

            // Col 3: Status Verdict
            canvas.drawText(p.status.getName(lang), col3 + 4f, rowY + 15f, statusPaint)

            // Col 4: Points
            val ptsStr = if (p.pointsEarned == p.pointsEarned.toInt().toDouble()) "${p.pointsEarned.toInt()} / ${p.maxPoints.toInt()}" else "${p.pointsEarned} / ${p.maxPoints.toInt()}"
            canvas.drawText(ptsStr, col4 + 6f, rowY + 15f, boldTextPaint.apply { textSize = 7.8f; color = textDark })

            // Horizontal row line
            canvas.drawLine(tableLeft, rowY + rowHeight, tableRight, rowY + rowHeight, gridLinePaint)

            // Vertical column dividers
            canvas.drawLine(col1, rowY, col1, rowY + rowHeight, gridLinePaint)
            canvas.drawLine(col2, rowY, col2, rowY + rowHeight, gridLinePaint)
            canvas.drawLine(col3, rowY, col3, rowY + rowHeight, gridLinePaint)
            canvas.drawLine(col4, rowY, col4, rowY + rowHeight, gridLinePaint)

            y += rowHeight
        }

        // Table Outer Border
        val tableTotalHeight = (10 * rowHeight) + headerHeight
        canvas.drawRect(tableLeft, y - tableTotalHeight, tableRight, y, bannerBorderPaint)
        y += 7f

        // 7. Sevvay Dosham Analysis Box
        val sevvayBoxHeight = 72f
        val sevvayRect = RectF(tableLeft, y, tableRight, y + sevvayBoxHeight)
        canvas.drawRoundRect(sevvayRect, 5f, 5f, cardBgPaint)
        canvas.drawRoundRect(sevvayRect, 5f, 5f, cardBorderPaint)

        var sy = y + 13f
        val sx = tableLeft + 8f

        // Header
        canvas.drawText(getLabel("KujaDoshaTitle", lang), sx, sy, boldTextPaint.apply { color = deepMaroon; textSize = 8.8f })
        sy += 13f

        // Two columns for Bride & Groom Mars status
        val colMid = tableLeft + (tableWidth / 2f)
        val brideSevTxt = "${getLabel("Bride", lang)}: ${result.sevvayDosham.getBrideDoshamSeverity(lang)} ${result.sevvayDosham.getBrideCancellationReason(lang)?.let { "($it)" } ?: ""}"
        val groomSevTxt = "${getLabel("Groom", lang)}: ${result.sevvayDosham.getGroomDoshamSeverity(lang)} ${result.sevvayDosham.getGroomCancellationReason(lang)?.let { "($it)" } ?: ""}"

        canvas.drawText(brideSevTxt.take(48), sx, sy, textPaint.apply { textSize = 7.8f; color = textDark })
        canvas.drawText(groomSevTxt.take(48), colMid + 4f, sy, textPaint.apply { textSize = 7.8f; color = textDark })
        sy += 13f

        // Samyam Balance
        val samyamStatusTxt = when (lang) {
            AppLanguage.TAMIL -> result.sevvayDosham.doshaSamyamStatusTa
            AppLanguage.HINDI -> result.sevvayDosham.doshaSamyamStatusHi
            AppLanguage.ENGLISH -> result.sevvayDosham.doshaSamyamStatusEn
        }
        val samyamLine = "${getLabel("DoshaBalance", lang)}: $samyamStatusTxt"
        canvas.drawText(samyamLine.take(85), sx, sy, boldTextPaint.apply { color = textDark; textSize = 7.8f })
        sy += 12.5f

        // Recommendation
        val samyamRecTxt = when (lang) {
            AppLanguage.TAMIL -> result.sevvayDosham.recommendationTa
            AppLanguage.HINDI -> result.sevvayDosham.recommendationHi
            AppLanguage.ENGLISH -> result.sevvayDosham.recommendationEn
        }
        val recPrefix = when (lang) {
            AppLanguage.TAMIL -> "வழிகாட்டல் / பலன்: "
            AppLanguage.HINDI -> "मार्गदर्शन / फल: "
            AppLanguage.ENGLISH -> "Guidance / Remedy: "
        }
        val recLine = "$recPrefix$samyamRecTxt"
        canvas.drawText(recLine.take(90), sx, sy, labelPaint.apply { textSize = 7.5f })

        y += sevvayBoxHeight + 7f

        // 8. Head Priest Endorsement & Blessings Box
        val priestCardHeight = 58f
        val priestCardRect = RectF(tableLeft, y, tableRight, y + priestCardHeight)
        val priestCardBg = Paint().apply { color = Color.rgb(250, 245, 235); style = Paint.Style.FILL }
        canvas.drawRoundRect(priestCardRect, 5f, 5f, priestCardBg)
        canvas.drawRoundRect(priestCardRect, 5f, 5f, cardBorderPaint)

        var py = y + 13f
        val px = tableLeft + 8f
        val endorseTitle = when (lang) {
            AppLanguage.TAMIL -> "திருமணப் பொருத்த அறிக்கை & சுப ஆசிகள் வழங்கியவர்:"
            AppLanguage.HINDI -> "विवाह मिलान प्रमाण पत्र एवं शुभाशीर्वाद प्रदाता:"
            AppLanguage.ENGLISH -> "Matchmaking Certificate & Blessings Issued By:"
        }
        canvas.drawText(endorseTitle, px, py, labelPaint.apply { textSize = 7.5f; color = textMuted })
        py += 13f

        val priestName = when (lang) {
            AppLanguage.TAMIL -> "பிரம்மஸ்ரீ மோகன் குருக்கள்"
            AppLanguage.HINDI -> "ब्रह्मश्री मोहन गुरुक्कल"
            AppLanguage.ENGLISH -> "Brahmasri Mohan Gurukkal (Head Priest)"
        }
        canvas.drawText(priestName, px, py, boldTextPaint.apply { textSize = 9.2f; color = deepMaroon })
        py += 12.5f

        val priestTemple = when (lang) {
            AppLanguage.TAMIL -> "தலைமை குருக்கள் • ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி தீவுகள் • Ph: +6797607465"
            AppLanguage.HINDI -> "मुख्य पुजारी • श्री शिव सुब्रमण्य स्वामी मंदिर, नादी, फिजी द्वीप • Ph: +6797607465"
            AppLanguage.ENGLISH -> "Head Priest • Sri Siva Subramaniya Swami Kovil, Nadi, Fiji Islands • Ph: +6797607465"
        }
        canvas.drawText(priestTemple, px, py, textPaint.apply { textSize = 7.8f; color = textDark })

        // Right-side Auspicious Seal Badge
        val sealRight = tableRight - 10f
        val sealWidth = 95f
        val sealTop = y + 8f
        val sealBottom = y + priestCardHeight - 8f
        val sealRect = RectF(sealRight - sealWidth, sealTop, sealRight, sealBottom)
        val sealBorderPaint = Paint().apply {
            color = templeGold
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }
        canvas.drawRoundRect(sealRect, 4f, 4f, sealBorderPaint)

        val sealPaint = Paint().apply {
            color = deepMaroon
            textSize = 7.2f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }
        val sealMidX = sealRight - (sealWidth / 2f)
        when (lang) {
            AppLanguage.TAMIL -> {
                canvas.drawText("|| சுபமஸ்து ||", sealMidX, sealTop + 13f, sealPaint)
                canvas.drawText("ஸ்ரீ சுப்பிரமணிய", sealMidX, sealTop + 24f, sealPaint)
                canvas.drawText("சுவாமி துணை", sealMidX, sealTop + 34f, sealPaint)
            }
            AppLanguage.HINDI -> {
                canvas.drawText("॥ शुभमस्तु ॥", sealMidX, sealTop + 13f, sealPaint)
                canvas.drawText("श्री सुब्रमण्य", sealMidX, sealTop + 24f, sealPaint)
                canvas.drawText("स्वामी कृपा", sealMidX, sealTop + 34f, sealPaint)
            }
            AppLanguage.ENGLISH -> {
                canvas.drawText("|| Subhamastu ||", sealMidX, sealTop + 13f, sealPaint)
                canvas.drawText("Sri Subramaniya", sealMidX, sealTop + 24f, sealPaint)
                canvas.drawText("Swami Blessings", sealMidX, sealTop + 34f, sealPaint)
            }
        }

        y += priestCardHeight + 7f

        // 9. Vedic Marriage Blessings & Crucial Notes Box
        val guidanceHeight = PAGE_HEIGHT - m - 24f - y
        if (guidanceHeight > 25f) {
            val guideRect = RectF(tableLeft, y, tableRight, y + guidanceHeight)
            canvas.drawRoundRect(guideRect, 5f, 5f, cardBgPaint)
            canvas.drawRoundRect(guideRect, 5f, 5f, cardBorderPaint)

            var gy2 = y + 12f
            val gx2 = tableLeft + 8f

            val guideTitle = when (lang) {
                AppLanguage.TAMIL -> "முக்கிய பொருத்த விளக்கம்:"
                AppLanguage.HINDI -> "मुख्य मिलान सूत्र:"
                AppLanguage.ENGLISH -> "Key Astrological Marriage Harmony Factors:"
            }
            canvas.drawText(guideTitle, gx2, gy2, boldTextPaint.apply { color = deepMaroon; textSize = 8f })
            gy2 += 11f

            val rajjuNote = when (lang) {
                AppLanguage.TAMIL -> "• ரஜ்ஜுப் பொருத்தம்: தம்பதியரின் மாங்கல்ய பலம் மற்றும் தீர்க்காயுளுக்கு அதிமுக்கிய பொருத்தமாகும்."
                AppLanguage.HINDI -> "• रज्जु मिलान: दांपत्य जीवन में दीर्घायु एवं अखंड सौभाग्य हेतु अत्यंत महत्वपूर्ण मिलान है।"
                AppLanguage.ENGLISH -> "• Rajju Match: Most crucial for marital longevity and unbroken union."
            }
            canvas.drawText(rajjuNote.take(90), gx2, gy2, labelPaint.apply { textSize = 7.2f })
            gy2 += 10.5f

            val ganaDinaNote = when (lang) {
                AppLanguage.TAMIL -> "• தினப் பொருத்தம் & கணப் பொருத்தம்: தேக சௌக்கியம், ஆயுள் விருத்தி மற்றும் தம்பதியர் மன ஒற்றுமைக்கு உகந்தது."
                AppLanguage.HINDI -> "• दिन एवं गण मिलान: उत्तम स्वास्थ्य, आरोग्य, दीर्घायु एवं आपसी सामंजस्य के लिए आवश्यक है।"
                AppLanguage.ENGLISH -> "• Dina & Gana Match: Essential for physical health, longevity, and mental harmony."
            }
            canvas.drawText(ganaDinaNote.take(90), gx2, gy2, labelPaint.apply { textSize = 7.2f })
        }

        // 10. Auspicious Footer
        val footerPaint = Paint().apply {
            color = deepMaroon
            textSize = 8f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }
        val footerText = when (lang) {
            AppLanguage.TAMIL -> "ஓம் சரவணபவ • ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி துணை • லோகா சமஸ்தா சுகினோ பவந்து • சுபம்"
            AppLanguage.HINDI -> "॥ ॐ नमः शिवाय ॥ श्री शिव सुब्रमण्य स्वामी प्रसन्न ॥ लोकाः समस्ताः सुखिनो भवन्तु ॥ शुभम् ॥"
            AppLanguage.ENGLISH -> "Om Saravanabhava • Sri Siva Subramaniya Swami Blessings • Subhamastu"
        }
        canvas.drawText(footerText, PAGE_WIDTH / 2f, PAGE_HEIGHT - 22f, footerPaint)
    }

    private fun drawField(
        canvas: Canvas,
        x: Float,
        y: Float,
        label: String,
        value: String,
        labelPaint: Paint,
        valPaint: Paint,
        maxWidth: Float
    ) {
        val labelStr = "$label: "
        canvas.drawText(labelStr, x, y, labelPaint)
        val labelWidth = labelPaint.measureText(labelStr)
        val remainingWidth = maxWidth - labelWidth

        var cleanVal = value
        if (valPaint.measureText(cleanVal) > remainingWidth) {
            while (cleanVal.isNotEmpty() && valPaint.measureText("$cleanVal…") > remainingWidth) {
                cleanVal = cleanVal.dropLast(1)
            }
            cleanVal = "$cleanVal…"
        }
        canvas.drawText(cleanVal, x + labelWidth, y, valPaint)
    }

    private fun getStandardPoruthamName(index: Int, lang: AppLanguage, originalName: String): String {
        return when (index) {
            0 -> when (lang) {
                AppLanguage.TAMIL -> "1. தினப் பொருத்தம்"
                AppLanguage.HINDI -> "1. दिन मिलान (Dina)"
                AppLanguage.ENGLISH -> "1. Dina Porutham"
            }
            1 -> when (lang) {
                AppLanguage.TAMIL -> "2. கணப் பொருத்தம்"
                AppLanguage.HINDI -> "2. गण मिलान (Gana)"
                AppLanguage.ENGLISH -> "2. Gana Porutham"
            }
            2 -> when (lang) {
                AppLanguage.TAMIL -> "3. மகேந்திரப் பொருத்தம்"
                AppLanguage.HINDI -> "3. महेन्द्र मिलान (Mahendra)"
                AppLanguage.ENGLISH -> "3. Mahendra Porutham"
            }
            3 -> when (lang) {
                AppLanguage.TAMIL -> "4. ஸ்திரீ தீர்க்கம்"
                AppLanguage.HINDI -> "4. स्त्री दीर्घ (Stree)"
                AppLanguage.ENGLISH -> "4. Stree Deergha"
            }
            4 -> when (lang) {
                AppLanguage.TAMIL -> "5. யோனிப் பொருத்தம்"
                AppLanguage.HINDI -> "5. योनि मिलान (Yoni)"
                AppLanguage.ENGLISH -> "5. Yoni Porutham"
            }
            5 -> when (lang) {
                AppLanguage.TAMIL -> "6. ராசிப் பொருத்தம்"
                AppLanguage.HINDI -> "6. राशि मिलान (Rasi)"
                AppLanguage.ENGLISH -> "6. Rasi Porutham"
            }
            6 -> when (lang) {
                AppLanguage.TAMIL -> "7. ராசி அதிபதி"
                AppLanguage.HINDI -> "7. राश्याधिपति (Lord)"
                AppLanguage.ENGLISH -> "7. Rasi Lord Match"
            }
            7 -> when (lang) {
                AppLanguage.TAMIL -> "8. வசியப் பொருத்தம்"
                AppLanguage.HINDI -> "8. वश्य मिलान (Vasiya)"
                AppLanguage.ENGLISH -> "8. Vasiya Porutham"
            }
            8 -> when (lang) {
                AppLanguage.TAMIL -> "9. ரஜ்ஜுப் பொருத்தம்"
                AppLanguage.HINDI -> "9. रज्जु मिलान (Rajju)"
                AppLanguage.ENGLISH -> "9. Rajju Porutham"
            }
            9 -> when (lang) {
                AppLanguage.TAMIL -> "10. வேதைப் பொருத்தம்"
                AppLanguage.HINDI -> "10. वेधा मिलान (Vedha)"
                AppLanguage.ENGLISH -> "10. Vedha Porutham"
            }
            else -> originalName
        }
    }

    private fun drawCornerDiamond(canvas: Canvas, cx: Float, cy: Float, colorInt: Int) {
        val paint = Paint().apply {
            color = colorInt
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val path = Path().apply {
            moveTo(cx, cy - 3f)
            lineTo(cx + 3f, cy)
            lineTo(cx, cy + 3f)
            lineTo(cx - 3f, cy)
            close()
        }
        canvas.drawPath(path, paint)
    }

    private fun getLabel(key: String, lang: AppLanguage): String = when (key) {
        "Name" -> when (lang) {
            AppLanguage.TAMIL -> "பெயர்"
            AppLanguage.HINDI -> "नाम"
            AppLanguage.ENGLISH -> "Name"
        }
        "DOB" -> when (lang) {
            AppLanguage.TAMIL -> "தேதி / நேரம்"
            AppLanguage.HINDI -> "तिथि / समय"
            AppLanguage.ENGLISH -> "DOB / Time"
        }
        "Place" -> when (lang) {
            AppLanguage.TAMIL -> "பிறந்த இடம்"
            AppLanguage.HINDI -> "जन्म स्थान"
            AppLanguage.ENGLISH -> "Birth Place"
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
        "Mars" -> when (lang) {
            AppLanguage.TAMIL -> "செவ்வாய்"
            AppLanguage.HINDI -> "मंगल"
            AppLanguage.ENGLISH -> "Mars"
        }
        "House" -> when (lang) {
            AppLanguage.TAMIL -> "-ஆம் இடம்"
            AppLanguage.HINDI -> " भाव"
            AppLanguage.ENGLISH -> "th House"
        }
        "DoshamPresent" -> when (lang) {
            AppLanguage.TAMIL -> "தோஷம்"
            AppLanguage.HINDI -> "दोष"
            AppLanguage.ENGLISH -> "Dosham"
        }
        "NoDosham" -> when (lang) {
            AppLanguage.TAMIL -> "சுபம்"
            AppLanguage.HINDI -> "शुभ"
            AppLanguage.ENGLISH -> "Auspicious"
        }
        "TotalScore" -> when (lang) {
            AppLanguage.TAMIL -> "மொத்த பொருத்தம்"
            AppLanguage.HINDI -> "कुल गुण मिलान"
            AppLanguage.ENGLISH -> "Total Match Score"
        }
        "Rajju" -> when (lang) {
            AppLanguage.TAMIL -> "ரஜ்ஜு"
            AppLanguage.HINDI -> "रज्जु"
            AppLanguage.ENGLISH -> "Rajju"
        }
        "RajjuAuspicious" -> when (lang) {
            AppLanguage.TAMIL -> "✓ ரஜ்ஜு சுபம் (பொருந்தும்)"
            AppLanguage.HINDI -> "✓ रज्जु शुभ (अनुकूल)"
            AppLanguage.ENGLISH -> "✓ Rajju Matched"
        }
        "RajjuDosham" -> when (lang) {
            AppLanguage.TAMIL -> "✗ ஏக ரஜ்ஜு (தோஷம்)"
            AppLanguage.HINDI -> "✗ एक रज्जु (दोष)"
            AppLanguage.ENGLISH -> "✗ Same Rajju (Afflicted)"
        }
        "KujaDoshaShort" -> when (lang) {
            AppLanguage.TAMIL -> "செவ்வாய் தோஷம்"
            AppLanguage.HINDI -> "मंगल दोष"
            AppLanguage.ENGLISH -> "Kuja Dosha"
        }
        "MarsBalanced" -> when (lang) {
            AppLanguage.TAMIL -> "✓ சமநிலை உண்டு"
            AppLanguage.HINDI -> "✓ दोष साम्य"
            AppLanguage.ENGLISH -> "✓ Balanced"
        }
        "MarsRemedyNeeded" -> when (lang) {
            AppLanguage.TAMIL -> "⚠ நிவர்த்தி தேவை"
            AppLanguage.HINDI -> "⚠ दोष निवारण"
            AppLanguage.ENGLISH -> "⚠ Review Advised"
        }
        "KujaDoshaTitle" -> when (lang) {
            AppLanguage.TAMIL -> "செவ்வாய் தோஷ விளக்கம் & தோஷ சாம்யம் (Kuja Dosha Analysis)"
            AppLanguage.HINDI -> "मंगल दोष विश्लेषण एवं दोष साम्य (Kuja Dosha Analysis)"
            AppLanguage.ENGLISH -> "Kuja (Mars) Dosha & Balance Analysis"
        }
        "Bride" -> when (lang) {
            AppLanguage.TAMIL -> "மணமகள்"
            AppLanguage.HINDI -> "वधू"
            AppLanguage.ENGLISH -> "Bride"
        }
        "Groom" -> when (lang) {
            AppLanguage.TAMIL -> "மணமகன்"
            AppLanguage.HINDI -> "वर"
            AppLanguage.ENGLISH -> "Groom"
        }
        "DoshaBalance" -> when (lang) {
            AppLanguage.TAMIL -> "தோஷ சமநிலை"
            AppLanguage.HINDI -> "दोष साम्य"
            AppLanguage.ENGLISH -> "Dosha Balance"
        }
        "ColNo" -> when (lang) {
            AppLanguage.TAMIL -> "எண்"
            AppLanguage.HINDI -> "क्र."
            AppLanguage.ENGLISH -> "No."
        }
        "ColPorutham" -> when (lang) {
            AppLanguage.TAMIL -> "பொருத்தம்"
            AppLanguage.HINDI -> "पोरुथम"
            AppLanguage.ENGLISH -> "Porutham"
        }
        "ColSignificance" -> when (lang) {
            AppLanguage.TAMIL -> "பொருத்த பலன் விளக்கம்"
            AppLanguage.HINDI -> "महत्व एवं फल विवरण"
            AppLanguage.ENGLISH -> "Significance & Blessing"
        }
        "ColVerdict" -> when (lang) {
            AppLanguage.TAMIL -> "முடிவு"
            AppLanguage.HINDI -> "परिणाम"
            AppLanguage.ENGLISH -> "Verdict"
        }
        "ColPoints" -> when (lang) {
            AppLanguage.TAMIL -> "மதிப்பெண்"
            AppLanguage.HINDI -> "अंक"
            AppLanguage.ENGLISH -> "Score"
        }
        else -> key
    }
}

private fun wrapTextToLines(text: String, paint: Paint, maxWidth: Float, maxLines: Int = 2): List<String> {
    if (paint.measureText(text) <= maxWidth) return listOf(text)
    val words = text.split(" ")
    val lines = mutableListOf<String>()
    var currentLine = ""

    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        if (paint.measureText(testLine) <= maxWidth) {
            currentLine = testLine
        } else {
            if (currentLine.isNotEmpty()) {
                lines.add(currentLine)
            }
            if (lines.size >= maxLines - 1) {
                // Building the last allowed line
                currentLine = word
            } else if (lines.size >= maxLines) {
                currentLine = ""
                break
            } else {
                currentLine = word
            }
        }
    }
    if (currentLine.isNotEmpty() && lines.size < maxLines) {
        var finalLine = currentLine
        if (paint.measureText(finalLine) > maxWidth) {
            while (finalLine.isNotEmpty() && paint.measureText("$finalLine…") > maxWidth) {
                finalLine = finalLine.dropLast(1)
            }
            finalLine = "$finalLine…"
        }
        lines.add(finalLine)
    }
    return lines
}

