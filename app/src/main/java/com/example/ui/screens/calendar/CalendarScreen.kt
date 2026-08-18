package com.example.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.TamilSamvatsaraEngine
import com.example.data.repository.AppStrings
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onNavigateToPanchangamDate: (LocalDate) -> Unit,
    onNavigateToFestivalDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val lang = state.language
    var selectedTab by remember { mutableStateOf(0) } // 0: Month Calendar, 1: All Festivals

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("calendar_screen_container")
    ) {
        // Tab row: Monthly Calendar vs Hindu Festivals
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = TempleMaroon
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = if (lang == AppLanguage.TAMIL) "மாத நாட்காட்டி" else "Monthly Calendar",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                    )
                },
                modifier = Modifier.testTag("tab_monthly_calendar")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = if (lang == AppLanguage.TAMIL) "இந்துப் பண்டிகைகள் (${state.allFestivals.size})" else "Hindu Festivals (${state.allFestivals.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                    )
                },
                modifier = Modifier.testTag("tab_all_festivals")
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Month Switcher Header
                item {
                    val currentYm = state.currentYearMonth
                    val monthNameEn = currentYm.month.name.lowercase().replaceFirstChar { it.uppercase() }
                    val (_, tDate) = TamilSamvatsaraEngine.getTamilDate(currentYm.atDay(15))
                    val (tMonth, _) = TamilSamvatsaraEngine.getTamilDate(currentYm.atDay(15))
                    val tYear = TamilSamvatsaraEngine.getSamvatsaraForDate(currentYm.atDay(15))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.previousMonth() },
                                modifier = Modifier.testTag("cal_prev_month_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Previous Month",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$monthNameEn ${currentYm.year}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${tMonth.name(lang)} • ${tYear.displayName(lang)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TempleGoldText
                                )
                            }

                            IconButton(
                                onClick = { viewModel.nextMonth() },
                                modifier = Modifier.testTag("cal_next_month_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next Month",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Days Grid
                item {
                    val weekdays = if (lang == AppLanguage.TAMIL) {
                        listOf("ஞாயிறு", "திங்கள்", "செவ்வாய்", "புதன்", "வியாழன்", "வெள்ளி", "சனி")
                    } else {
                        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            // Weekday Header Row
                            Row(modifier = Modifier.fillMaxWidth()) {
                                weekdays.forEach { dayName ->
                                    Text(
                                        text = dayName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (dayName == "ஞாயிறு" || dayName == "Sun") TempleKumkum else MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Grid of 42 cells (6 rows x 7 cols)
                            val chunkedDays = state.daysInMonthGrid.chunked(7)
                            chunkedDays.forEach { week ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                ) {
                                    week.forEach { item ->
                                        CalendarDayCell(
                                            item = item,
                                            isSelected = item.date == state.selectedDate,
                                            onClick = { viewModel.selectDate(item.date) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Selected Day Details Card
                item {
                    state.selectedDayPanchangam?.let { panchangam ->
                        val selectedDateFormatted = state.selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .testTag("cal_selected_day_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${panchangam.tamilMonth.name(lang)} ${panchangam.tamilDate}",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = selectedDateFormatted,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Button(
                                        onClick = { onNavigateToPanchangamDate(state.selectedDate) },
                                        colors = ButtonDefaults.buttonColors(containerColor = TempleMaroon),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.testTag("btn_view_full_panchangam")
                                    ) {
                                        Text(
                                            text = if (lang == AppLanguage.TAMIL) "பஞ்சாங்கம்" else "Panchangam",
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = TempleGold.copy(alpha = 0.3f))

                                // Tithi, Nakshatram, Yogam, Karanam
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = AppStrings.tithi(lang), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = "${panchangam.tithi} (${panchangam.tithiEndTime})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TempleMaroonDark)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = AppStrings.nakshatram(lang), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = "${panchangam.nakshatram} (${panchangam.nakshatramEndTime})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TempleMaroonDark)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = AppStrings.yogam(lang), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = panchangam.yogam, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = AppStrings.karanam(lang), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = panchangam.karanam, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }

                                if (panchangam.specialObservances.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        color = TempleGold.copy(alpha = 0.25f),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Stars,
                                                contentDescription = null,
                                                tint = TempleSaffron,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = panchangam.specialObservances.joinToString(" • ") { it.getDisplayName(lang) },
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TempleMaroonDark
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // All 21 Hindu Festivals Tab
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // Search Field
                OutlinedTextField(
                    value = state.filterQuery,
                    onValueChange = { viewModel.setFilterQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("festivals_search_field"),
                    placeholder = {
                        Text(text = if (lang == AppLanguage.TAMIL) "பண்டிகைகளைத் தேடுக..." else "Search Hindu Festivals...")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                val filteredFestivals = state.allFestivals.filter { fest ->
                    fest.nameTa.contains(state.filterQuery, ignoreCase = true) ||
                            fest.nameEn.contains(state.filterQuery, ignoreCase = true) ||
                            fest.deityTa.contains(state.filterQuery, ignoreCase = true) ||
                            fest.deityEn.contains(state.filterQuery, ignoreCase = true)
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredFestivals) { fest ->
                        FestivalListItem(
                            festival = fest,
                            lang = lang,
                            onClick = { onNavigateToFestivalDetail(fest.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    item: CalendarDayItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        isSelected -> TempleMaroon
        item.isToday -> TempleGold.copy(alpha = 0.25f)
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> Color.White
        !item.isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
        item.isToday -> TempleMaroon
        else -> MaterialTheme.colorScheme.onSurface
    }

    val tamilTextColor = when {
        isSelected -> TempleGoldLight
        !item.isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
        item.isToday -> TempleMaroonDark
        else -> TempleMaroonText
    }

    Box(
        modifier = modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable(enabled = item.isCurrentMonth) { onClick() }
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Gregorian Day Number
            Text(
                text = "${item.gregorianDay}",
                fontSize = 13.sp,
                fontWeight = if (isSelected || item.isToday) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )

            // Tamil Day Number
            Text(
                text = "${item.tamilDate}",
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = tamilTextColor
            )

            // Observance dot indicator
            if (item.hasObservance) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) TempleGoldLight else TempleKumkum)
                )
            }
        }
    }
}

@Composable
private fun FestivalListItem(
    festival: com.example.data.model.HinduFestival,
    lang: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("fest_item_${festival.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = festival.iconEmoji,
                fontSize = 28.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = festival.getName(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TempleMaroon
                )
                Text(
                    text = "${festival.tamilMonth.name(lang)} • ${festival.getDeity(lang)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = festival.getSignificance(lang),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
