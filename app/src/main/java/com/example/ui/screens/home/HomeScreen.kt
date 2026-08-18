package com.example.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.repository.AppStrings
import com.example.ui.theme.*
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCalendar: () -> Unit,
    onNavigateToPanchangam: () -> Unit,
    onNavigateToJathagam: () -> Unit,
    onNavigateToTemple: () -> Unit,
    onNavigateToRasiPalan: () -> Unit,
    onNavigateToDharmaSastra: () -> Unit,
    onNavigateToFestivalDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val lang = state.language

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen_container"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Hero Gopuram Banner with Kovil Name and Divine Emblem
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_temple_gopuram),
                    contentDescription = "Sri Siva Subramaniya Swami Kovil Gopuram",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Devotional gradient scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    TempleMaroon.copy(alpha = 0.6f),
                                    TempleMaroonDark.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Overlay Kovil text
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        color = TempleGold,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = AppStrings.templeTag(lang),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TempleMaroonDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = AppStrings.appTitle(lang),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp
                        ),
                        color = Color.White
                    )

                    Text(
                        text = AppStrings.deityName(lang),
                        fontSize = 12.sp,
                        color = TempleGoldLight
                    )
                }
            }
        }

        // 2. Temple Opening Status & Daily Arti Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("temple_status_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (state.isTempleOpen) SacredGreen else TempleKumkum)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (state.isTempleOpen) AppStrings.templeStatusOpen(lang) else AppStrings.templeStatusClosed(lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (state.isTempleOpen) SacredGreen else TempleKumkum
                            )
                        }

                        Text(
                            text = AppStrings.templeClosingTime(lang, state.templeClosingTimeToday),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Daily Arti Timings
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // Morning Arti
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Morning Arti",
                                tint = TempleGoldDark,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = AppStrings.morningArti(lang),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = state.morningArtiTime,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TempleMaroon
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        )

                        // Evening Arti
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NightlightRound,
                                contentDescription = "Evening Arti",
                                tint = TempleSaffron,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = AppStrings.eveningArti(lang),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = state.eveningArtiTime,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TempleMaroon
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Today's Date, Panchangam & Auspicious Times Dashboard Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onNavigateToPanchangam() }
                    .testTag("today_panchangam_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Date & Day
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.todayTamilDateStr,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            state.panchangamDetail?.let { p ->
                                Text(
                                    text = "${p.ayanam} • ${p.ritu}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = state.todayEnglishDate,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            color = TempleMaroonContainer,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, TempleMaroonLight)
                        ) {
                            Text(
                                text = state.panchangamDetail?.vasaram?.split(" ")?.take(2)?.joinToString(" ") ?: state.dayOfWeekStr,
                                color = TempleMaroonText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    state.panchangamDetail?.let { p ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${AppStrings.monthPrefix(lang)}: ${p.sanskritMonth}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = p.paksha.split(" ")[0] + " " + p.paksha.split(" ")[1],
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Current Tithi & Nakshatram Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Tithi Box
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.NightlightRound,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = AppStrings.tithi(lang),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = state.tithiStr,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Nakshatram Box
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = AppStrings.nakshatram(lang),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = state.nakshatramStr,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Today's Auspicious & Inauspicious Times (Nalla Neram, Rahukaalam, Yamagandam)
                    state.panchangamDetail?.let { panchangam ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = AppStrings.dayTimingHeader(lang),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Row 1: Nalla Neram (Auspicious)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🟢", fontSize = 10.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${AppStrings.nallaNeramLabel(lang)}:",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Text(
                                        text = "${panchangam.nallaNeramMorning} | ${panchangam.nallaNeramEvening}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SacredGreenText
                                    )
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )

                                // Row 2: Rahukaalam & Yamagandam (Important inauspicious periods)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Rahukaalam Box
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)) {
                                            Text(
                                                text = AppStrings.rahuKalam(lang),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                            Text(
                                                text = panchangam.rahuKalam,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }

                                    // Yamagandam Box
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)) {
                                            Text(
                                                text = AppStrings.yamagandam(lang),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                            Text(
                                                text = panchangam.yamagandam,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Today's Observance Banner
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${AppStrings.todayFestival(lang)}: ${state.todayObservanceStr}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // 3B. 2026 Planetary Transit Alert & Rasi Palan Showcase Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onNavigateToRasiPalan() }
                    .testTag("home_card_2026_rasi_palan"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, TempleMaroon.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.5.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = TempleMaroonContainer,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = "Alert",
                                        tint = TempleMaroon,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = AppStrings.transitAlertTitle(lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TempleMaroon
                            )
                        }

                        Surface(
                            color = TempleGoldContainer,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, TempleGold)
                        ) {
                            Text(
                                text = AppStrings.rasiPalan(lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TempleGoldText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = AppStrings.transitAlertSummary(lang),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = AppStrings.transitCategories(lang),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.readMore(lang),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TempleMaroon
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = TempleMaroon,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Quick Action Grid
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = AppStrings.quickServices(lang),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TempleMaroon,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = AppStrings.calendar(lang),
                        subtitle = when (lang) {
                            AppLanguage.TAMIL -> "தமிழ் காலண்டர்"
                            AppLanguage.HINDI -> "कैलेंडर विवरण"
                            AppLanguage.ENGLISH -> "Tamil Calendar"
                        },
                        icon = Icons.Default.CalendarMonth,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToCalendar,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_btn_calendar"
                    )
                    QuickActionCard(
                        title = AppStrings.panchangam(lang),
                        subtitle = when (lang) {
                            AppLanguage.TAMIL -> "தினசரி பஞ்சாங்கம்"
                            AppLanguage.HINDI -> "दैनिक पंचांग"
                            AppLanguage.ENGLISH -> "Daily Panchangam"
                        },
                        icon = Icons.Default.WbSunny,
                        color = MaterialTheme.colorScheme.secondary,
                        onClick = onNavigateToPanchangam,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_btn_panchangam"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = AppStrings.jathagam(lang),
                        subtitle = when (lang) {
                            AppLanguage.TAMIL -> "ஜாதக கணிப்பு"
                            AppLanguage.HINDI -> "कुंडली एवं दोष"
                            AppLanguage.ENGLISH -> "Horoscope & Birth Chart"
                        },
                        icon = Icons.Default.AutoAwesome,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToJathagam,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_btn_jathagam"
                    )
                    QuickActionCard(
                        title = AppStrings.rasiPalan(lang),
                        subtitle = when (lang) {
                            AppLanguage.TAMIL -> "2026 வருட பலன்கள்"
                            AppLanguage.HINDI -> "2026 वार्षिक फल"
                            AppLanguage.ENGLISH -> "2026 Yearly Predictions"
                        },
                        icon = Icons.Default.Star,
                        color = MaterialTheme.colorScheme.secondary,
                        onClick = onNavigateToRasiPalan,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_btn_rasi_palan"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = AppStrings.dharmaSastra(lang),
                        subtitle = when (lang) {
                            AppLanguage.TAMIL -> "சாஸ்திர விதிகள்"
                            AppLanguage.HINDI -> "शास्त्र नियम एवं मार्गदर्शन"
                            AppLanguage.ENGLISH -> "Scriptural Codes"
                        },
                        icon = Icons.Default.MenuBook,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToDharmaSastra,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_btn_dharma_sastra"
                    )
                    QuickActionCard(
                        title = AppStrings.temple(lang),
                        subtitle = when (lang) {
                            AppLanguage.TAMIL -> "பூஜை விவரங்கள்"
                            AppLanguage.HINDI -> "दर्शन एवं पूजा"
                            AppLanguage.ENGLISH -> "Temple & Darshan"
                        },
                        icon = Icons.Default.AccountBalance,
                        color = MaterialTheme.colorScheme.secondary,
                        onClick = onNavigateToTemple,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_btn_temple"
                    )
                }
            }
        }

        // 5. Upcoming Hindu & Temple Festivals
        item {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.upcomingFestivals(lang),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TempleMaroon
                    )

                    TextButton(onClick = onNavigateToCalendar) {
                        Text(text = AppStrings.viewAll(lang), color = TempleGoldDark)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.upcomingFestivals) { (fest, date) ->
                        val dateFormatted = date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
                        Card(
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { onNavigateToFestivalDetail(fest.id) }
                                .testTag("fest_card_${fest.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = fest.iconEmoji, fontSize = 24.sp)
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(4.dp),
                                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            text = dateFormatted,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = fest.getName(lang),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TempleMaroon,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = fest.getDeity(lang),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = color,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
