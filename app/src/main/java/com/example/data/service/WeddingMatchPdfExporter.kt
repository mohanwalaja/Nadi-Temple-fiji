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
            putExtra(Intent.EXTRA_SUBJECT, "Wedding Match Certificate - Head Priest Mohan Gurukkal (+6797607465)")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Wedding Match PDF"))
    }

    private fun drawMatchmakingReport(canvas: Canvas, data: WeddingMatchPdfData) {
        val lang = data.language

        // Traditional South Indian Color Palette
        val deepMaroon = Color.rgb(122, 21, 38)
        val templeGold = Color.rgb(200, 155, 60)
        val borderGold = Color.rgb(180, 135, 45)
        val warmBg = Color.rgb(255, 253, 248)
        val cardBg = Color.rgb(252, 249, 242)
        val textDark = Color.rgb(25, 25, 25)
        val textMuted = Color.rgb(80, 80, 80)

        // Paints
        val invocationPaint = Paint().apply {
            color = deepMaroon
            textSize = 9.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }

        val templeTitlePaint = Paint().apply {
            color = deepMaroon
            textSize = 14f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }

        val priestPaint = Paint().apply {
            color = deepMaroon
            textSize = 10.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val subHeaderPaint = Paint().apply {
            color = textMuted
            textSize = 8.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val goldLinePaint = Paint().apply {
            color = templeGold
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
            isAntiAlias = true
        }

        val outerBorderPaint = Paint().apply {
            color = deepMaroon
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            isAntiAlias = true
        }

        val innerBorderPaint = Paint().apply {
            color = borderGold
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        val cardBgPaint = Paint().apply {
            color = cardBg
            style = Paint.Style.FILL
        }

        val cardBorderPaint = Paint().apply {
            color = Color.rgb(220, 200, 160)
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        val textPaint = Paint().apply {
            color = textDark
            textSize = 9f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val boldTextPaint = Paint().apply {
            color = textDark
            textSize = 9.5f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val tableHeaderPaint = Paint().apply {
            color = deepMaroon
            style = Paint.Style.FILL
        }

        val whiteTextPaint = Paint().apply {
            color = Color.WHITE
            textSize = 9f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        // 1. South Indian Ornamental Double Border with Corner Accents
        val m = 18f
        canvas.drawRect(m, m, PAGE_WIDTH - m, PAGE_HEIGHT - m, outerBorderPaint)
        canvas.drawRect(m + 3.5f, m + 3.5f, PAGE_WIDTH - m - 3.5f, PAGE_HEIGHT - m - 3.5f, innerBorderPaint)

        // Draw Corner Diamond Ornaments (South Indian Kolam motif)
        drawCornerDiamond(canvas, m + 3.5f, m + 3.5f, templeGold)
        drawCornerDiamond(canvas, PAGE_WIDTH - m - 3.5f, m + 3.5f, templeGold)
        drawCornerDiamond(canvas, m + 3.5f, PAGE_HEIGHT - m - 3.5f, templeGold)
        drawCornerDiamond(canvas, PAGE_WIDTH - m - 3.5f, PAGE_HEIGHT - m - 3.5f, templeGold)

        var y = 36f

        // 2. Auspicious Invocation
        val invocationText = when (lang) {
            AppLanguage.TAMIL -> "|| ஸ்ரீ கணேசாய நம: ||   || சுபமஸ்து ||   || குருப்யோ நம: ||"
            AppLanguage.HINDI -> "॥ श्री गणेशाय नमः ॥   ॥ शुभमस्तु ॥   ॥ श्री गुरुभ्यो नमः ॥"
            AppLanguage.ENGLISH -> "|| Sri Ganeshaya Namah ||   || Subhamastu ||   || Gurubhyo Namah ||"
        }
        canvas.drawText(invocationText, PAGE_WIDTH / 2f, y, invocationPaint)
        y += 15f

        // 3. Temple Name & Mohan Gurukkal Info
        val templeName = when (lang) {
            AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில்"
            AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी मंदिर"
            AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Kovil"
        }
        canvas.drawText(templeName, PAGE_WIDTH / 2f, y, templeTitlePaint)
        y += 14f

        val priestText = when (lang) {
            AppLanguage.TAMIL -> "தலைமை குருக்கள்: மோகன் குருக்கள் (Head Priest: Mohan Gurukkal) • Mobile: +6797607465"
            AppLanguage.HINDI -> "मुख्य पुजारी (Head Priest): मोहन गुरुक्कल • Mobile: +6797607465"
            AppLanguage.ENGLISH -> "Head Priest: Mohan Gurukkal • Mobile: +6797607465"
        }
        canvas.drawText(priestText, PAGE_WIDTH / 2f, y, priestPaint)
        y += 12f

        val subtitle = when (lang) {
            AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி தீவுகள் • திருமணப் பொருத்த அறிக்கை"
            AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी मंदिर, नादी, फिजी द्वीप • विवाह मिलान प्रमाण पत्र"
            AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Kovil, Nadi, Fiji • Vedic Matchmaking Certificate"
        }
        canvas.drawText(subtitle, PAGE_WIDTH / 2f, y, subHeaderPaint)
        y += 12f

        // Ornamental Separator Line
        canvas.drawLine(m + 25, y, PAGE_WIDTH - m - 25, y, goldLinePaint)
        y += 10f

        // 4. Side-by-Side Bride & Groom Kundali Cards
        val availableWidth = PAGE_WIDTH - (m * 2) - 16
        val cardWidth = (availableWidth - 10) / 2
        val brideLeft = m + 8
        val groomLeft = brideLeft + cardWidth + 10
        val cardHeight = 104f

        // Bride Card Rect
        val brideRect = RectF(brideLeft, y, brideLeft + cardWidth, y + cardHeight)
        canvas.drawRoundRect(brideRect, 6f, 6f, cardBgPaint)
        canvas.drawRoundRect(brideRect, 6f, 6f, cardBorderPaint)

        // Bride Card Header
        val brideHeaderRect = RectF(brideLeft, y, brideLeft + cardWidth, y + 18f)
        val brideHeaderPaint = Paint().apply { color = Color.rgb(194, 24, 91) }
        canvas.drawRoundRect(brideHeaderRect, 6f, 6f, brideHeaderPaint)
        val brideHeaderTitle = when (lang) {
            AppLanguage.TAMIL -> "மணமகள் விபரம் (Bride Profile)"
            AppLanguage.HINDI -> "वधू (कन्या) विवरण"
            AppLanguage.ENGLISH -> "Bride Profile"
        }
        canvas.drawText(brideHeaderTitle, brideLeft + 8, y + 13, whiteTextPaint)

        // Bride Card Content
        var by = y + 31f
        val bx = brideLeft + 8
        canvas.drawText("${getLabel("Name", lang)}: ${data.brideName.ifBlank { "-" }}", bx, by, boldTextPaint)
        by += 12f
        canvas.drawText("${getLabel("DOB", lang)}: ${data.brideDob.format(DateTimeFormatter.ISO_LOCAL_DATE)} | ${data.brideTob}", bx, by, textPaint)
        by += 12f
        canvas.drawText("${getLabel("Place", lang)}: ${data.bridePlace.ifBlank { "-" }}", bx, by, textPaint)
        by += 12f
        canvas.drawText("${getLabel("Rasi", lang)}: ${data.brideRasi.getName(lang)} (${data.brideRasi.getLord(lang)})", bx, by, textPaint)
        by += 12f
        canvas.drawText("${getLabel("Star", lang)}: ${data.brideNakshatraName} - ${getLabel("Pada", lang)} ${data.bridePada}", bx, by, textPaint)
        by += 12f
        val brideLagnaStr = data.brideLagna?.getName(lang) ?: "-"
        val brideMarsDoshaStr = if (data.brideMarsHouse in listOf(2, 4, 7, 8, 12)) getLabel("DoshamPresent", lang) else getLabel("NoDosham", lang)
        val brideMarsHouseLabel = getLabel("House", lang)
        val marsLabel = getLabel("Mars", lang)
        canvas.drawText("${getLabel("Lagna", lang)}: $brideLagnaStr | $marsLabel: ${data.brideMarsHouse}$brideMarsHouseLabel ($brideMarsDoshaStr)", bx, by, textPaint)

        // Groom Card Rect
        val groomRect = RectF(groomLeft, y, groomLeft + cardWidth, y + cardHeight)
        canvas.drawRoundRect(groomRect, 6f, 6f, cardBgPaint)
        canvas.drawRoundRect(groomRect, 6f, 6f, cardBorderPaint)

        // Groom Card Header
        val groomHeaderRect = RectF(groomLeft, y, groomLeft + cardWidth, y + 18f)
        val groomHeaderPaint = Paint().apply { color = deepMaroon }
        canvas.drawRoundRect(groomHeaderRect, 6f, 6f, groomHeaderPaint)
        val groomHeaderTitle = when (lang) {
            AppLanguage.TAMIL -> "மணமகன் விபரம் (Groom Profile)"
            AppLanguage.HINDI -> "वर (दूल्हा) विवरण"
            AppLanguage.ENGLISH -> "Groom Profile"
        }
        canvas.drawText(groomHeaderTitle, groomLeft + 8, y + 13, whiteTextPaint)

        // Groom Card Content
        var gy = y + 31f
        val gx = groomLeft + 8
        canvas.drawText("${getLabel("Name", lang)}: ${data.groomName.ifBlank { "-" }}", gx, gy, boldTextPaint)
        gy += 12f
        canvas.drawText("${getLabel("DOB", lang)}: ${data.groomDob.format(DateTimeFormatter.ISO_LOCAL_DATE)} | ${data.groomTob}", gx, gy, textPaint)
        gy += 12f
        canvas.drawText("${getLabel("Place", lang)}: ${data.groomPlace.ifBlank { "-" }}", gx, gy, textPaint)
        gy += 12f
        canvas.drawText("${getLabel("Rasi", lang)}: ${data.groomRasi.getName(lang)} (${data.groomRasi.getLord(lang)})", gx, gy, textPaint)
        gy += 12f
        canvas.drawText("${getLabel("Star", lang)}: ${data.groomNakshatraName} - ${getLabel("Pada", lang)} ${data.groomPada}", gx, gy, textPaint)
        gy += 12f
        val groomLagnaStr = data.groomLagna?.getName(lang) ?: "-"
        val groomMarsDoshaStr = if (data.groomMarsHouse in listOf(2, 4, 7, 8, 12)) getLabel("DoshamPresent", lang) else getLabel("NoDosham", lang)
        val groomMarsHouseLabel = getLabel("House", lang)
        canvas.drawText("${getLabel("Lagna", lang)}: $groomLagnaStr | $marsLabel: ${data.groomMarsHouse}$groomMarsHouseLabel ($groomMarsDoshaStr)", gx, gy, textPaint)

        y += cardHeight + 8f

        // 5. Verdict & Score Banner
        val result = data.result
        val verdictBgColor = when (result.verdictStatus) {
            PoruthamStatus.UTTHAMAM -> Color.rgb(235, 248, 237)
            PoruthamStatus.MADHYAMAM -> Color.rgb(255, 248, 230)
            PoruthamStatus.PORUNDHADHU -> Color.rgb(255, 238, 238)
        }
        val verdictBorderColor = when (result.verdictStatus) {
            PoruthamStatus.UTTHAMAM -> Color.rgb(76, 175, 80)
            PoruthamStatus.MADHYAMAM -> Color.rgb(255, 167, 38)
            PoruthamStatus.PORUNDHADHU -> Color.rgb(239, 83, 80)
        }
        val verdictTextColor = when (result.verdictStatus) {
            PoruthamStatus.UTTHAMAM -> Color.rgb(30, 110, 40)
            PoruthamStatus.MADHYAMAM -> Color.rgb(200, 90, 0)
            PoruthamStatus.PORUNDHADHU -> Color.rgb(180, 30, 30)
        }

        val bannerRect = RectF(brideLeft, y, groomLeft + cardWidth, y + 36f)
        val bannerBgPaint = Paint().apply { color = verdictBgColor; style = Paint.Style.FILL }
        val bannerBorderPaint = Paint().apply { color = verdictBorderColor; style = Paint.Style.STROKE; strokeWidth = 1f; isAntiAlias = true }
        canvas.drawRoundRect(bannerRect, 6f, 6f, bannerBgPaint)
        canvas.drawRoundRect(bannerRect, 6f, 6f, bannerBorderPaint)

        val vPaint = Paint().apply {
            color = verdictTextColor
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val scoreStr = "${getLabel("TotalScore", lang)}: ${result.totalPoruthamsMatched} / 10 (${result.totalScore} / 10.0) — ${result.verdictStatus.getName(lang)}"
        canvas.drawText(scoreStr, brideLeft + 10, y + 15, vPaint)

        val rajjuLocalized = if (result.rajjuMatch) getLabel("RajjuAuspicious", lang) else getLabel("RajjuDosham", lang)
        val samyamLocalized = if (result.sevvayDosham.doshaSamyamStatusTa.contains("உண்டு")) getLabel("MarsBalanced", lang) else getLabel("MarsRemedyNeeded", lang)

        val subVerdictPaint = Paint().apply {
            color = textDark
            textSize = 8.5f
            isAntiAlias = true
        }
        canvas.drawText("${getLabel("Rajju", lang)}: $rajjuLocalized   |   ${getLabel("KujaDoshaShort", lang)}: $samyamLocalized", brideLeft + 10, y + 29, subVerdictPaint)

        y += 42f

        // 6. 10 Poruthams Table
        val tableLeft = brideLeft
        val tableRight = groomLeft + cardWidth
        val tableWidth = tableRight - tableLeft

        // Column widths strictly calibrated:
        // Col 0: No (24f)
        // Col 1: Porutham Name (115f)
        // Col 2: Explanation / Astrological Result (245f)
        // Col 3: Verdict (90f)
        // Col 4: Points (45f)
        val col0 = tableLeft
        val col1 = col0 + 24f
        val col2 = col1 + 115f
        val col3 = col2 + 245f
        val col4 = col3 + 90f

        val headerHeight = 18f
        canvas.drawRect(tableLeft, y, tableRight, y + headerHeight, tableHeaderPaint)

        canvas.drawText(getLabel("ColNo", lang), col0 + 4, y + 13, whiteTextPaint)
        canvas.drawText(getLabel("ColPorutham", lang), col1 + 4, y + 13, whiteTextPaint)
        canvas.drawText(getLabel("ColSignificance", lang), col2 + 4, y + 13, whiteTextPaint)
        canvas.drawText(getLabel("ColVerdict", lang), col3 + 4, y + 13, whiteTextPaint)
        canvas.drawText(getLabel("ColPoints", lang), col4 + 4, y + 13, whiteTextPaint)
        y += headerHeight

        val rowPaintEven = Paint().apply { color = Color.rgb(255, 255, 255); style = Paint.Style.FILL }
        val rowPaintOdd = Paint().apply { color = Color.rgb(249, 246, 240); style = Paint.Style.FILL }
        val linePaint = Paint().apply { color = Color.rgb(225, 215, 195); strokeWidth = 0.75f }

        val rowHeight = 21f
        result.poruthams.forEachIndexed { index, p ->
            val rowY = y
            canvas.drawRect(tableLeft, rowY, tableRight, rowY + rowHeight, if (index % 2 == 0) rowPaintEven else rowPaintOdd)

            val statusColor = when (p.status) {
                PoruthamStatus.UTTHAMAM -> Color.rgb(40, 130, 45)
                PoruthamStatus.MADHYAMAM -> Color.rgb(210, 100, 0)
                PoruthamStatus.PORUNDHADHU -> Color.rgb(195, 30, 30)
            }
            val statusPaint = Paint().apply {
                color = statusColor
                textSize = 8.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            // No
            canvas.drawText("${index + 1}", col0 + 6, rowY + 14, textPaint.apply { textSize = 8.5f })

            // Porutham Name (cleanly truncated if too long)
            val pName = p.getName(lang)
            val pNameClean = if (pName.length > 22) pName.take(21) + "…" else pName
            canvas.drawText(pNameClean, col1 + 4, rowY + 14, boldTextPaint.apply { textSize = 8.5f })

            // Explanation (cleanly fitted in 245f width)
            val rawExp = p.getExplanation(lang)
            val maxChars = when (lang) {
                AppLanguage.ENGLISH -> 48
                AppLanguage.HINDI -> 44
                AppLanguage.TAMIL -> 44
            }
            val expClean = if (rawExp.length > maxChars) rawExp.take(maxChars - 1) + "…" else rawExp
            canvas.drawText(expClean, col2 + 4, rowY + 14, textPaint.apply { textSize = 7.8f })

            // Status Verdict
            canvas.drawText(p.status.getName(lang), col3 + 4, rowY + 14, statusPaint)

            // Points
            val ptsStr = if (p.pointsEarned == p.pointsEarned.toInt().toDouble()) "${p.pointsEarned.toInt()}/${p.maxPoints.toInt()}" else "${p.pointsEarned}/${p.maxPoints.toInt()}"
            canvas.drawText(ptsStr, col4 + 8, rowY + 14, boldTextPaint.apply { textSize = 8.5f })

            canvas.drawLine(tableLeft, rowY + rowHeight, tableRight, rowY + rowHeight, linePaint)
            y += rowHeight
        }

        // Draw Table Outer Border
        canvas.drawRect(tableLeft, y - (10 * rowHeight) - headerHeight, tableRight, y, bannerBorderPaint)
        y += 8f

        // 7. Sevvay Dosham Analysis Box
        val sevvayBoxHeight = 62f
        val sevvayRect = RectF(tableLeft, y, tableRight, y + sevvayBoxHeight)
        canvas.drawRoundRect(sevvayRect, 6f, 6f, cardBgPaint)
        canvas.drawRoundRect(sevvayRect, 6f, 6f, cardBorderPaint)

        var sy = y + 14f
        val sx = tableLeft + 10
        canvas.drawText(getLabel("KujaDoshaTitle", lang), sx, sy, boldTextPaint.apply { color = deepMaroon; textSize = 9.5f })
        sy += 13f

        val brideSevTxt = "${getLabel("Bride", lang)}: ${result.sevvayDosham.getBrideDoshamSeverity(lang)} ${result.sevvayDosham.getBrideCancellationReason(lang) ?: ""}"
        val groomSevTxt = "${getLabel("Groom", lang)}: ${result.sevvayDosham.getGroomDoshamSeverity(lang)} ${result.sevvayDosham.getGroomCancellationReason(lang) ?: ""}"
        canvas.drawText("${brideSevTxt.take(45)}  |  ${groomSevTxt.take(45)}", sx, sy, textPaint.apply { textSize = 8.2f })
        sy += 13f

        val samyamStatusTxt = when (lang) {
            AppLanguage.TAMIL -> result.sevvayDosham.doshaSamyamStatusTa
            AppLanguage.HINDI -> result.sevvayDosham.doshaSamyamStatusHi
            AppLanguage.ENGLISH -> result.sevvayDosham.doshaSamyamStatusEn
        }
        val samyamRecTxt = when (lang) {
            AppLanguage.TAMIL -> result.sevvayDosham.recommendationTa
            AppLanguage.HINDI -> result.sevvayDosham.recommendationHi
            AppLanguage.ENGLISH -> result.sevvayDosham.recommendationEn
        }
        val fullSamyamTxt = "${getLabel("DoshaBalance", lang)}: $samyamStatusTxt • $samyamRecTxt"
        canvas.drawText(fullSamyamTxt.take(90), sx, sy, boldTextPaint.apply { color = textDark; textSize = 8.2f })

        y += sevvayBoxHeight + 8f

        // 8. Priest Seal & Auspicious Blessing Box (South Indian Style Signature Box)
        val priestCardHeight = 44f
        val priestCardRect = RectF(tableLeft, y, tableRight, y + priestCardHeight)
        val priestCardBg = Paint().apply { color = Color.rgb(250, 245, 235); style = Paint.Style.FILL }
        canvas.drawRoundRect(priestCardRect, 6f, 6f, priestCardBg)
        canvas.drawRoundRect(priestCardRect, 6f, 6f, cardBorderPaint)

        var py = y + 14f
        val px = tableLeft + 10
        val endorseTitle = when (lang) {
            AppLanguage.TAMIL -> "திருமணப் பொருத்த அறிக்கை & ஆசிகள் வழங்கியவர்:"
            AppLanguage.HINDI -> "विवाह मिलान प्रमाण पत्र एवं शुभाशीर्वाद प्रदाता:"
            AppLanguage.ENGLISH -> "Matchmaking Certificate & Blessings Issued By:"
        }
        canvas.drawText(endorseTitle, px, py, textPaint.apply { textSize = 8f; color = textMuted })
        py += 13f

        val priestSigText = "மோகன் குருக்கள் (Mohan Gurukkal) • Head Priest, Sri Siva Subramaniya Swami Kovil, Nadi, Fiji • Ph: +6797607465"
        canvas.drawText(priestSigText, px, py, boldTextPaint.apply { textSize = 8.8f; color = deepMaroon })
        py += 12f

        // 9. Auspicious Footer
        val footerPaint = Paint().apply {
            color = deepMaroon
            textSize = 8.5f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }
        val footerText = when (lang) {
            AppLanguage.TAMIL -> "ஓம் சரவணபவ • ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி துணை • லோகா சமஸ்தா சுகினோ பவந்து • சுபம்"
            AppLanguage.HINDI -> "॥ ॐ नमः शिवाय ॥ श्री शिव सुब्रमण्य स्वामी प्रसन्न ॥ लोकाः समस्ताः सुखिनो भवन्तु ॥ शुभम् ॥"
            AppLanguage.ENGLISH -> "Om Saravanabhava • Sri Siva Subramaniya Swami Thunai • Subhamastu"
        }
        canvas.drawText(footerText, PAGE_WIDTH / 2f, PAGE_HEIGHT - 25f, footerPaint)
    }

    private fun drawCornerDiamond(canvas: Canvas, cx: Float, cy: Float, colorInt: Int) {
        val paint = Paint().apply {
            color = colorInt
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val path = Path().apply {
            moveTo(cx, cy - 3.5f)
            lineTo(cx + 3.5f, cy)
            lineTo(cx, cy + 3.5f)
            lineTo(cx - 3.5f, cy)
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
            AppLanguage.TAMIL -> "பிறந்த தேதி/நேரம்"
            AppLanguage.HINDI -> "जन्म तिथि/समय"
            AppLanguage.ENGLISH -> "DOB / Time"
        }
        "Place" -> when (lang) {
            AppLanguage.TAMIL -> "பிறந்த இடம்"
            AppLanguage.HINDI -> "जन्म स्थान"
            AppLanguage.ENGLISH -> "Birth Place"
        }
        "Rasi" -> when (lang) {
            AppLanguage.TAMIL -> "ராசி (அதிபதி)"
            AppLanguage.HINDI -> "राशि (स्वामी)"
            AppLanguage.ENGLISH -> "Rasi (Lord)"
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
            AppLanguage.TAMIL -> "✓ ரஜ்ஜு சுபம் (பொருத்தம் உண்டு)"
            AppLanguage.HINDI -> "✓ रज्जु शुभ (अनुकूल)"
            AppLanguage.ENGLISH -> "✓ Rajju Auspicious (Matched)"
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
            AppLanguage.TAMIL -> "✓ செவ்வாய் சமநிலை உண்டு"
            AppLanguage.HINDI -> "✓ मंगल दोष साम्य"
            AppLanguage.ENGLISH -> "✓ Mars Balanced"
        }
        "MarsRemedyNeeded" -> when (lang) {
            AppLanguage.TAMIL -> "⚠ தோஷ நிவர்த்தி தேவை"
            AppLanguage.HINDI -> "⚠ दोष निवारण आवश्यक"
            AppLanguage.ENGLISH -> "⚠ Remedy / Review Advised"
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
            AppLanguage.HINDI -> "पोरुथम (गुण)"
            AppLanguage.ENGLISH -> "Porutham Name"
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
            AppLanguage.TAMIL -> "புள்ளி"
            AppLanguage.HINDI -> "अंक"
            AppLanguage.ENGLISH -> "Score"
        }
        else -> key
    }
}
