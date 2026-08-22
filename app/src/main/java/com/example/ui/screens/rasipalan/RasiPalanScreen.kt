package com.example.ui.screens.rasipalan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlertSeverity
import com.example.data.model.AppLanguage
import com.example.data.model.PalanTimeframe
import com.example.data.model.Rasi
import com.example.data.model.RasiPalanResult
import com.example.data.model.RasiTransitPalan2026
import com.example.data.repository.RasiTransit2026Repository
import com.example.ui.theme.*

@Composable
fun RasiPalanScreen(
    viewModel: RasiPalanViewModel,
    onBack: () -> Unit,
    onNavigateToJathagam: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val lang = state.language
    val palan = state.palanResult
    val jathagam = state.jathagam
    val selectedRasi = state.selectedRasi
    val selectedTimeframe = state.selectedTimeframe
    val isDark = isSystemInDarkTheme()

    val screenTitle = when (selectedTimeframe) {
        PalanTimeframe.DAILY -> when (lang) {
            AppLanguage.TAMIL -> "இன்றைய ராசிபலன் (Daily)"
            AppLanguage.HINDI -> "आज का राशिफल (Daily)"
            AppLanguage.ENGLISH -> "Daily Horoscope"
        }
        PalanTimeframe.WEEKLY -> when (lang) {
            AppLanguage.TAMIL -> "வார ராசிபலன் (Weekly)"
            AppLanguage.HINDI -> "साप्ताहिक राशिफल (Weekly)"
            AppLanguage.ENGLISH -> "Weekly Horoscope"
        }
        PalanTimeframe.MONTHLY -> when (lang) {
            AppLanguage.TAMIL -> "மாத ராசிபலன் (Monthly)"
            AppLanguage.HINDI -> "मासिक राशिफल (Monthly)"
            AppLanguage.ENGLISH -> "Monthly Horoscope"
        }
        PalanTimeframe.YEARLY -> when (lang) {
            AppLanguage.TAMIL -> "2026–2027 வருட ராசிபலன்"
            AppLanguage.HINDI -> "2026–2027 वार्षिक राशिफल"
            AppLanguage.ENGLISH -> "2026–2027 Yearly Horoscope"
        }
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("rasi_palan_container"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 0. Timeframe Selector (Daily, Weekly, Monthly, Yearly)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (lang == AppLanguage.TAMIL) "காலப்பகுதி (Period):" else if (lang == AppLanguage.HINDI) "काल अवधि (Timeframe):" else "Timeframe / Period:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("timeframe_tab_row"),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val timeframes = listOf(
                        PalanTimeframe.DAILY to when (lang) { AppLanguage.TAMIL -> "இன்று"; AppLanguage.HINDI -> "दैनिक"; AppLanguage.ENGLISH -> "Daily" },
                        PalanTimeframe.WEEKLY to when (lang) { AppLanguage.TAMIL -> "வாரம்"; AppLanguage.HINDI -> "साप्ताहिक"; AppLanguage.ENGLISH -> "Weekly" },
                        PalanTimeframe.MONTHLY to when (lang) { AppLanguage.TAMIL -> "மாதம்"; AppLanguage.HINDI -> "मासिक"; AppLanguage.ENGLISH -> "Monthly" },
                        PalanTimeframe.YEARLY to when (lang) { AppLanguage.TAMIL -> "2026 வருடம்"; AppLanguage.HINDI -> "2026 वर्ष"; AppLanguage.ENGLISH -> "2026 Year" }
                    )

                    timeframes.forEach { (tf, label) ->
                        val isSelected = selectedTimeframe == tf
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) primaryColor else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isSelected) primaryColor else MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.selectTimeframe(tf) }
                                .testTag("timeframe_tab_${tf.name.lowercase()}")
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }

            // 1. Rasi Selector Horizontal Strip
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (lang == AppLanguage.TAMIL) "ராசி தேர்வு (Select Rasi):" else "Select Rasi / Sign:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TempleMaroon
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("rasi_selector_row")
                    ) {
                        // My Jathagam Option (if exists)
                        if (jathagam != null) {
                            item {
                                val isMyJathagamSelected = selectedRasi == null || selectedRasi == jathagam.chandraRasi
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (isMyJathagamSelected) TempleMaroon else MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(1.dp, if (isMyJathagamSelected) TempleMaroon else TempleGold),
                                    modifier = Modifier.clickable { viewModel.selectRasi(jathagam.chandraRasi) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = if (isMyJathagamSelected) TempleGoldLight else TempleMaroon,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (lang == AppLanguage.TAMIL) "என் ஜாதகம் (${jathagam.chandraRasi.nameTa})" else if (lang == AppLanguage.HINDI) "मेरी जन्म कुंडली (${jathagam.chandraRasi.nameHi})" else "My Janma Rasi (${jathagam.chandraRasi.nameEn})",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isMyJathagamSelected) Color.White else TempleMaroon
                                        )
                                    }
                                }
                            }
                        }

                        // 12 Rasis
                        items(Rasi.values()) { rasiItem ->
                            val isSelected = (selectedRasi == rasiItem) || (selectedRasi == null && jathagam != null && jathagam.chandraRasi == rasiItem)
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) TempleMaroon else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, if (isSelected) TempleMaroon else MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier.clickable { viewModel.selectRasi(rasiItem) }
                            ) {
                                Text(
                                    text = "${rasiItem.getName(lang)}",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Main Selected Rasi & Period Header Banner
            palan?.let { palanResult ->
                val rasiTransit2026 = RasiTransit2026Repository.getPalanForRasi(palanResult.rasi)

                // 2A. 2026 Planetary Transit Alert Banner (High Priority Transits)
                item {
                    val alertSeverity = rasiTransit2026.severity
                    val alertCardBg = MaterialTheme.colorScheme.surfaceVariant
                    val alertBorderColor = MaterialTheme.colorScheme.outline
                    val alertAccentColor = MaterialTheme.colorScheme.primary
                    val alertHeaderTag = when (alertSeverity) {
                        AlertSeverity.HIGH_ALERT -> if (lang == AppLanguage.TAMIL) "முக்கிய கிரக எச்சரிக்கை" else if (lang == AppLanguage.HINDI) "अति महत्वपूर्ण ग्रह अलर्ट" else "Critical Planetary Alert"
                        AlertSeverity.PERIOD_ALERT -> if (lang == AppLanguage.TAMIL) "காலப்பகுதி சார்ந்த எச்சரிக்கை" else if (lang == AppLanguage.HINDI) "समयानुसार ग्रह अलर्ट" else "Period-wise Transit Watch"
                        AlertSeverity.STANDARD_WATCH -> if (lang == AppLanguage.TAMIL) "கோச்சார வழிகாட்டல்" else if (lang == AppLanguage.HINDI) "गोचर मार्गदर्शन" else "Transit Watch"
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_2026_planetary_alert"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = alertCardBg),
                        border = BorderStroke(1.5.dp, alertBorderColor.copy(alpha = 0.6f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = "Alert",
                                        tint = alertAccentColor,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = alertHeaderTag,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = alertAccentColor
                                    )
                                }

                                Surface(
                                    color = alertAccentColor.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "2026",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = alertAccentColor,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "${palanResult.rasi.getName(lang)} (${palanResult.rasiSymbol}) — ${rasiTransit2026.getAlertTag(lang)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Key Planets details
                            rasiTransit2026.keyPlanets.forEach { planetAlert ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(0.5.dp, alertBorderColor.copy(alpha = 0.3f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "${planetAlert.getPlanet(lang)}: ${planetAlert.getHeadline(lang)}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = alertAccentColor
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = planetAlert.getDetails(lang),
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Primary Advice
                            Surface(
                                color = alertAccentColor.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(0.5.dp, alertBorderColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Advice",
                                        tint = alertAccentColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${if (lang == AppLanguage.TAMIL) "⚠️ முக்கிய அறிவுரை: " else if (lang == AppLanguage.HINDI) "⚠️ मुख्य सलाह: " else "⚠️ Key Advice: "}${rasiTransit2026.getPrimaryAdvice(lang)}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = alertAccentColor,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // 2B. Selected Rasi & Vedic Profile Header Banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_jathagam_header"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.5.dp, TempleMaroon.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.5.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (selectedRasi == null && jathagam != null) {
                                            if (lang == AppLanguage.TAMIL) "ஜாதகர்: ${jathagam.devoteeName}" else "Horoscope: ${jathagam.devoteeName}"
                                        } else {
                                            if (lang == AppLanguage.TAMIL) "வேத ஜோதிட ராசி சுயவிவரம் (Vedic Astrology Profile)" else if (lang == AppLanguage.HINDI) "वैदिक ज्योतिष राशिफल विवरण" else "Vedic Astrology Sign Profile"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${palanResult.rasi.getName(lang)} ${palanResult.rasiSymbol}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TempleMaroon
                                    )
                                }

                                Surface(
                                    color = TempleGoldContainer,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(0.5.dp, TempleGold)
                                ) {
                                    Text(
                                        text = palanResult.periodYearLabel,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleGoldText,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Grid of Vedic Attributes
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "அதிபதி கிரகம்" else if (lang == AppLanguage.HINDI) "स्वामी ग्रह" else "Ruling Lord",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = palanResult.getRasiLord(lang),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleMaroon
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "தத்துவம் (Element)" else if (lang == AppLanguage.HINDI) "तत्व (Element)" else "Element",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = palanResult.getElement(lang),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleMaroon
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "இயல்பு (Quality)" else if (lang == AppLanguage.HINDI) "स्वभाव (Quality)" else "Quality",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = palanResult.getQuality(lang),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleMaroon
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Nakshatras in this Rasi
                            if (palanResult.getNakshatras(lang).isNotBlank()) {
                                Surface(
                                    color = TempleGold.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(0.5.dp, TempleGold.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Stars,
                                            contentDescription = null,
                                            tint = TempleGoldDark,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${if (lang == AppLanguage.TAMIL) "நட்சத்திர பாதங்கள்: " else if (lang == AppLanguage.HINDI) "नक्षत्र चरण: " else "Nakshatra Padas: "}${palanResult.getNakshatras(lang)}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Lucky Gemstone, Metal, Direction & Days
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1.3f)) {
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "அதிர்ஷ்ட ரத்தினம்" else if (lang == AppLanguage.HINDI) "शुभ रत्न" else "Lucky Gemstone",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = palanResult.getLuckyGemstone(lang),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleMaroon
                                    )
                                }

                                Column(modifier = Modifier.weight(0.9f)) {
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "அதிர்ஷ்ட திசை" else if (lang == AppLanguage.HINDI) "शुभ दिशा" else "Lucky Direction",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = palanResult.getLuckyDirection(lang),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleMaroon
                                    )
                                }

                                Column(modifier = Modifier.weight(1.1f)) {
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "அதிர்ஷ்டக் கிழமைகள்" else if (lang == AppLanguage.HINDI) "शुभ दिन" else "Lucky Days",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = palanResult.getLuckyDays(lang),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleMaroon
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action button to customize/recalculate horoscope
                            OutlinedButton(
                                onClick = onNavigateToJathagam,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_recalculate_jathagam"),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = TempleMaroon
                                ),
                                border = BorderStroke(1.dp, TempleMaroon.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (lang == AppLanguage.TAMIL) "பிறந்த விவரங்களை மாற்ற / புதிய நபர் ஜாதகம்" else "Change Birth Details / New Person Horoscope",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TempleMaroon
                                )
                            }
                        }
                    }
                }

                // 4. Planetary Transits (Gochara) Status Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, TempleGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "கிரக கோச்சார நிலைகள் (Planetary Transits)"
                                        AppLanguage.HINDI -> "ग्रह गोचर स्थिति"
                                        AppLanguage.ENGLISH -> "Planetary Transits (Gochara)"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TempleMaroon
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = palanResult.getPlanetaryTransitsSummary(lang),
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (palanResult.dashaInfluenceTa.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = TempleGoldLight.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, TempleGold.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = palanResult.getDashaInfluence(lang),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TempleMaroonDark,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. Quick Highlights: Lucky Numbers & Colors
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "அதிர்ஷ்ட எண்கள்"
                                        AppLanguage.HINDI -> "शुभ अंक"
                                        AppLanguage.ENGLISH -> "Lucky Numbers"
                                    },
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = palanResult.luckyNumber,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TempleMaroon
                                )
                            }

                            Column(modifier = Modifier.weight(1.2f)) {
                                Text(
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "அதிர்ஷ்ட வண்ணங்கள்"
                                        AppLanguage.HINDI -> "शुभ रंग"
                                        AppLanguage.ENGLISH -> "Lucky Colors"
                                    },
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = palanResult.getLuckyColor(lang),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TempleMaroon
                                )
                            }
                        }
                    }
                }

                // 6. General Palan
                item {
                    val generalText = "${rasiTransit2026.getAlertTag(lang)}\n${palanResult.getGeneral(lang)}"
                    
                    PalanAspectCard(
                        title = when (lang) {
                            AppLanguage.TAMIL -> "பொது பலன் (General Overview)"
                            AppLanguage.HINDI -> "सामान्य फल"
                            AppLanguage.ENGLISH -> "General Overview"
                        },
                        description = generalText,
                        icon = Icons.Default.Info,
                        iconTint = TempleMaroon
                    )
                }

                // 7. Money & Finance
                item {
                    val financeText = "${rasiTransit2026.getFinance(lang)} (${palanResult.getMoney(lang)})"

                    PalanAspectCard(
                        title = when (lang) {
                            AppLanguage.TAMIL -> "தனம் & வரவு-செலவு (Finance)"
                            AppLanguage.HINDI -> "धन एवं आर्थिक स्थिति"
                            AppLanguage.ENGLISH -> "Finance & Wealth"
                        },
                        description = financeText,
                        icon = Icons.Default.AccountBalance,
                        iconTint = TempleGoldDark
                    )
                }

                // 8. Career & Profession
                item {
                    val careerText = "${rasiTransit2026.getCareer(lang)} (${palanResult.getCareer(lang)})"

                    PalanAspectCard(
                        title = when (lang) {
                            AppLanguage.TAMIL -> "தொழில் & உத்தியோகம் (Career)"
                            AppLanguage.HINDI -> "व्यवसाय एवं आजीविका"
                            AppLanguage.ENGLISH -> "Career & Profession"
                        },
                        description = careerText,
                        icon = Icons.Default.Work,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                // 9. Education
                item {
                    val eduText = "${rasiTransit2026.getEducation(lang)} (${palanResult.getEducation(lang)})"

                    PalanAspectCard(
                        title = when (lang) {
                            AppLanguage.TAMIL -> "கல்வி & வித்தை (Education)"
                            AppLanguage.HINDI -> "शिक्षा एवं विद्या"
                            AppLanguage.ENGLISH -> "Education & Studies"
                        },
                        description = eduText,
                        icon = Icons.Default.School,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                // 10. Family & Harmony
                item {
                    val familyText = "${rasiTransit2026.getFamily(lang)} (${palanResult.getFamily(lang)})"

                    PalanAspectCard(
                        title = when (lang) {
                            AppLanguage.TAMIL -> "குடும்பம் & அமைதி (Family)"
                            AppLanguage.HINDI -> "परिवार एवं सुख-शांति"
                            AppLanguage.ENGLISH -> "Family & Peace"
                        },
                        description = familyText,
                        icon = Icons.Default.Home,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                // 11. Marriage & Relationships
                item {
                    val marriageText = "${rasiTransit2026.getMarriage(lang)} (${palanResult.getMarriage(lang)})"

                    PalanAspectCard(
                        title = when (lang) {
                            AppLanguage.TAMIL -> "திருமணம் & உறவுகள் (Marriage)"
                            AppLanguage.HINDI -> "विवाह एवं संबंध"
                            AppLanguage.ENGLISH -> "Marriage & Relationships"
                        },
                        description = marriageText,
                        icon = Icons.Default.Favorite,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                // 12. Health & Wellness
                item {
                    val healthText = "${rasiTransit2026.getHealth(lang)} (${palanResult.getHealth(lang)})"

                    PalanAspectCard(
                        title = when (lang) {
                            AppLanguage.TAMIL -> "ஆரோக்கியம் & உடல்திறன் (Health)"
                            AppLanguage.HINDI -> "स्वास्थ्य एवं ऊर्जा"
                            AppLanguage.ENGLISH -> "Health & Wellness"
                        },
                        description = healthText,
                        icon = Icons.Default.Healing,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                // 13. Travel & Journeys
                item {
                    PalanAspectCard(
                        title = when (lang) {
                            AppLanguage.TAMIL -> "பயணம் & வெளிநாடு (Travel)"
                            AppLanguage.HINDI -> "यात्रा एवं विदेश योग"
                            AppLanguage.ENGLISH -> "Travel & Journeys"
                        },
                        description = palanResult.getTravel(lang),
                        icon = Icons.Default.Flight,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                // 14. Temple Pariharam & Remedies
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_palan_pariharam"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "திருக்கோயில் பரிகாரம் & வழிபாடு"
                                        AppLanguage.HINDI -> "मंदिर परिहार एवं पूजा उपाय"
                                        AppLanguage.ENGLISH -> "Temple Pariharam & Remedies"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = palanResult.getPariharam(lang),
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி தீவுகள்",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }

                // 15. Jyotisha Guidance Disclaimer
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = when (lang) {
                                    AppLanguage.TAMIL -> "வழிகாட்டுதல் குறிப்பு: இவை பாரம்பரிய ஜோதிட விதிமுறைகள் மற்றும் கோச்சார நிலைகளின் அடிப்படையிலான பலன்களாகும். இறை வழிபாடும் நற்செயல்களும் சுப யோகங்களை மேலும் பெருக்கும்."
                                    AppLanguage.HINDI -> "मार्गदर्शन: यह फल वैदिक ज्योतिष के पारम्परिक नियमों एवं गोचर स्थिति पर आधारित हैं। प्रभु भक्ति एवं सत्कर्म शुभ फलों में वृद्धि करते हैं।"
                                    AppLanguage.ENGLISH -> "Guidance Note: These interpretations are based on traditional Jyotisha principles and active planetary transits. Devotion and virtue amplify positive energy."
                                },
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
private fun PalanAspectCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = iconTint.copy(alpha = 0.14f),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TempleMaroon
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
