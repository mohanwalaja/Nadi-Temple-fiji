package com.example.ui.screens.jathagam

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.HoroscopeProfileEntity
import com.example.data.model.AppLanguage
import com.example.data.model.DoshaCheckResult
import com.example.data.model.Graha
import com.example.data.model.HoroscopeResult
import com.example.data.model.PalanTimeframe
import com.example.data.repository.AppStrings
import com.example.ui.components.SouthIndianRasiChart
import com.example.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JathagamScreen(
    viewModel: JathagamViewModel,
    modifier: Modifier = Modifier,
    onNavigateToRasiPalanWithTimeframe: ((PalanTimeframe) -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()
    val lang = state.language
    var selectedTab by remember { mutableStateOf(0) } // 0: Horoscope, 1: Saved Profiles
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.birthDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )
    val timePickerState = rememberTimePickerState(
        initialHour = state.birthTime.hour,
        initialMinute = state.birthTime.minute,
        is24Hour = false
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                            viewModel.onBirthDateChange(selectedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(if (lang == AppLanguage.TAMIL) "சரி (OK)" else "OK", fontWeight = FontWeight.Bold, color = TempleMaroon)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(if (lang == AppLanguage.TAMIL) "ரத்து (Cancel)" else "Cancel", color = Color.Gray)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.tertiary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = if (lang == AppLanguage.TAMIL) "பிறந்த நேரம் தேர்வு" else "Select Birth Time",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                            selectorColor = MaterialTheme.colorScheme.primary,
                            periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                            periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                            clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onBirthTimeChange(LocalTime.of(timePickerState.hour, timePickerState.minute))
                        showTimePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(if (lang == AppLanguage.TAMIL) "சரி (OK)" else "OK", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(if (lang == AppLanguage.TAMIL) "ரத்து" else "Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("jathagam_screen_container")
    ) {
        // Tab Header
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = if (lang == AppLanguage.TAMIL) "ஜாதக கணிப்பு" else "Horoscope Calculator",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.testTag("tab_jathagam_calc")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "${if (lang == AppLanguage.TAMIL) "சேமித்தவை" else "Saved"} (${state.savedProfiles.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.testTag("tab_jathagam_saved")
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Birth Details Input Card (High Contrast M3 Theme Tokens)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "பிறப்பு விவரங்கள் (Birth Details)" else "Birth Details Input",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "தேதி, நேரம் மற்றும் பிறந்த ஊரை உள்ளிடவும்" else "Enter date, time & location to calculate birth chart",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                TextButton(
                                    onClick = { viewModel.resetToNew() },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "+ புதியது" else "+ New",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Name Input
                            OutlinedTextField(
                                value = state.nameInput,
                                onValueChange = { viewModel.onNameChange(it) },
                                label = { Text(text = if (lang == AppLanguage.TAMIL) "ஜாதகர் பெயர் (Devotee Name)" else "Devotee Name") },
                                placeholder = { Text(text = if (lang == AppLanguage.TAMIL) "பெயர் உள்ளிடவும்" else "Enter devotee name") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                trailingIcon = {
                                    if (state.nameInput.isNotBlank()) {
                                        IconButton(onClick = { viewModel.onNameChange("") }) {
                                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_jathagam_name"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Date & Time Selectors with Interactive High-Contrast Clickable Controls
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Date field
                                OutlinedTextField(
                                    value = state.birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(text = if (lang == AppLanguage.TAMIL) "பிறந்த தேதி (DOB)" else "Birth Date (DOB)") },
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { showDatePicker = true }) {
                                            Icon(imageVector = Icons.Default.EditCalendar, contentDescription = "Pick Date", tint = MaterialTheme.colorScheme.primary)
                                        }
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { showDatePicker = true }
                                        .testTag("input_jathagam_dob"),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                // Time field
                                val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
                                OutlinedTextField(
                                    value = state.birthTime.format(timeFormatter),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(text = if (lang == AppLanguage.TAMIL) "பிறந்த நேரம் (Time)" else "Birth Time") },
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { showTimePicker = true }) {
                                            Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Pick Time", tint = MaterialTheme.colorScheme.primary)
                                        }
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { showTimePicker = true }
                                        .testTag("input_jathagam_tob"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Birth Place Input & Quick Suggestions
                            OutlinedTextField(
                                value = state.birthPlace,
                                onValueChange = { viewModel.onBirthPlaceChange(it) },
                                label = { Text(text = if (lang == AppLanguage.TAMIL) "பிறந்த ஊர் / நகரம் (Birth Place)" else "Birth Location / City") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_jathagam_place"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Quick Location Suggestions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val popularPlaces = listOf("நாடி (Nadi)", "சுவா (Suva)", "சென்னை (Chennai)", "மதுரை (Madurai)")
                                popularPlaces.forEach { place ->
                                    val isSelected = state.birthPlace.contains(place.substringBefore(" "))
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                        ),
                                        modifier = Modifier.clickable { viewModel.onBirthPlaceChange(place) }
                                    ) {
                                        Text(
                                            text = place,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.calculateHoroscope() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(vertical = 14.dp),
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .testTag("btn_calculate_jathagam")
                                ) {
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "🔮 ஜாதகம் கணிக்க" else "🔮 Calculate Chart",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                OutlinedButton(
                                    onClick = { viewModel.saveCurrentProfile() },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                    contentPadding = PaddingValues(vertical = 14.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("btn_save_jathagam_profile")
                                ) {
                                    Icon(
                                        imageVector = if (state.isSavedSuccessfully) Icons.Default.CheckCircle else Icons.Default.BookmarkAdd,
                                        contentDescription = null,
                                        tint = if (state.isSavedSuccessfully) SacredGreen else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (state.isSavedSuccessfully) (if (lang == AppLanguage.TAMIL) "சேமிக்கப்பட்டது!" else "Saved!") else AppStrings.saveProfile(lang),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Horoscope Results View (Clean High-Contrast Cards)
                state.horoscopeResult?.let { result ->
                    // Summary Banner Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${result.devoteeName} - ${if (lang == AppLanguage.TAMIL) "ஜாதகக் குறிப்பு" else "Horoscope Summary"}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = if (lang == AppLanguage.TAMIL) "லக்னம்" else "Lagna", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = "${result.lagnaRasi.getName(lang)} (${String.format("%.1f", result.lagnaDegrees)}°)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Column {
                                        Text(text = if (lang == AppLanguage.TAMIL) "சந்திர ராசி" else "Chandra Rasi", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = result.chandraRasi.getName(lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Column {
                                        Text(text = if (lang == AppLanguage.TAMIL) "நட்சத்திரம் / பாதம்" else "Nakshatram / Pada", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = "${result.janmaNakshatram} (${result.janmaPada})", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }

                    // Jathagam-Based Rasi Palan Quick Access Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("card_jathagam_rasi_palan"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = TempleMaroon,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "ஜன்ம ராசி பலன்கள் (${result.chandraRasi.nameTa})"
                                            AppLanguage.HINDI -> "जन्म राशि फल (${result.chandraRasi.nameHi})"
                                            AppLanguage.ENGLISH -> "Janma Rasi Predictions (${result.chandraRasi.nameEn})"
                                        },
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TempleMaroon
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "உங்கள் ஜாதகத்தின்படி கணிக்கப்பட்ட ராசிபலன் விவரங்கள்"
                                        AppLanguage.HINDI -> "आपकी जन्मकुंडली के आधार पर गणना किया गया राशिफल"
                                        AppLanguage.ENGLISH -> "Personalized astrological forecast generated from your horoscope"
                                    },
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = { onNavigateToRasiPalanWithTimeframe?.invoke(PalanTimeframe.YEARLY) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_palan_yearly"),
                                    colors = ButtonDefaults.buttonColors(containerColor = TempleMaroon),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Stars,
                                            contentDescription = null,
                                            tint = TempleGoldLight,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "📖 2026 வருட ராசிபலன் & கிரக எச்சரிக்கைகள்"
                                                AppLanguage.HINDI -> "📖 2026 वार्षिक राशिफल एवं ग्रह गोचर अलर्ट"
                                                AppLanguage.ENGLISH -> "📖 2026 Yearly Rasi Palan & Transit Alerts"
                                            },
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 3. Visual South Indian Rasi Chart
                    item {
                        SouthIndianRasiChart(
                            lagnaRasi = result.lagnaRasi,
                            planetPositions = result.planetPositions,
                            lang = lang
                        )
                    }

                    // 4. Nine Graha Positions Table
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (lang == AppLanguage.TAMIL) "நவக்கிரக நிலைகள் (9 Planetary Positions)" else "9 Planetary Positions (Navagrahas)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TempleMaroon
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                result.planetPositions.forEach { p ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = p.graha.getName(lang),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (p.graha == Graha.SURYA || p.graha == Graha.CHANDRA) TempleMaroon else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1.2f)
                                        )
                                        Text(
                                            text = p.rasi.getName(lang),
                                            fontSize = 12.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "${String.format("%.1f", p.degrees)}° (${p.nakshatram} - ${p.pada})",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.weight(1.5f)
                                        )
                                        if (p.isRetrograde) {
                                            Surface(
                                                color = TempleKumkum.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "வக்ரம்",
                                                    fontSize = 9.sp,
                                                    color = TempleKumkum,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                                }
                            }
                        }
                    }

                    // 5. Sani Transit Analysis (Ezharai, Ashtama, Kandaka Sani)
                    item {
                        val sani = result.saniStatus
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "சனிப் பெயர்ச்சி நிலை (Sani Transit Status)" else "Saturn Transit Analysis",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TempleMaroon
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Surface(
                                    color = if (sani.isEzharaiSani || sani.isAshtamaSani) TempleKumkum.copy(alpha = 0.1f) else SacredGreen.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = if (sani.isEzharaiSani) {
                                                "ஏழரை சனி நடப்பு: ${if (lang == AppLanguage.TAMIL) sani.ezharaiTypeTa else sani.ezharaiTypeEn}"
                                            } else if (sani.isAshtamaSani) {
                                                "அஷ்டம சனி நடப்பு"
                                            } else if (sani.isKandakaSani) {
                                                "கண்டக சனி நடப்பு"
                                            } else {
                                                if (lang == AppLanguage.TAMIL) "ஏழரை / அஷ்டம சனி தாக்கம் இல்லை (அனுகூல காலம்)" else "No Sade Sati / Ashtama Sani active"
                                            },
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (sani.isEzharaiSani || sani.isAshtamaSani) TempleKumkum else SacredGreen
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "பரிகாரம்: ${if (lang == AppLanguage.TAMIL) sani.remedyTa else sani.remedyEn}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 6. Dosha Checks (Kuja Dosham, Kala Sarpa, Pitru)
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (lang == AppLanguage.TAMIL) "தோஷ பரிசீலனை (Dosha Assessment)" else "Dosha Assessment & Remedies",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TempleMaroon
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                result.doshas.forEach { dosha ->
                                    DoshaItemRow(dosha = dosha, lang = lang)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }

                    // 7. Temple Jathaga Summary (Health, Wealth, Education, Career, Marriage, Family, Foreign travel)
                    item {
                        val summ = result.summary
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "திருக்கோயில் ஜாதக சுருக்கம் (முக்கிய பலன்கள்)"
                                            AppLanguage.HINDI -> "मंदिर ज्योतिष भाव सारांश (प्रमुख फलकथन)"
                                            AppLanguage.ENGLISH -> "Temple Jyotisha Aspect Summary"
                                        },
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "ஆரோக்கியம் (Health)"
                                        AppLanguage.HINDI -> "स्वास्थ्य (Health)"
                                        AppLanguage.ENGLISH -> "Health & Vitality"
                                    },
                                    text = summ.getHealth(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "தனம் & நிதி (Wealth)"
                                        AppLanguage.HINDI -> "धन एवं संपत्ति (Wealth)"
                                        AppLanguage.ENGLISH -> "Wealth & Finances"
                                    },
                                    text = summ.getWealth(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "கல்வி (Education)"
                                        AppLanguage.HINDI -> "शिक्षा एवं विद्या (Education)"
                                        AppLanguage.ENGLISH -> "Education & Intellect"
                                    },
                                    text = summ.getEducation(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "தொழில் & வேலை (Career)"
                                        AppLanguage.HINDI -> "व्यवसाय एवं आजीविका (Career)"
                                        AppLanguage.ENGLISH -> "Career & Status"
                                    },
                                    text = summ.getCareer(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "திருமணம் (Marriage)"
                                        AppLanguage.HINDI -> "विवाह एवं संबंध (Marriage)"
                                        AppLanguage.ENGLISH -> "Marriage & Union"
                                    },
                                    text = summ.getMarriage(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "குடும்பம் (Family)"
                                        AppLanguage.HINDI -> "परिवार एवं सुख (Family)"
                                        AppLanguage.ENGLISH -> "Family & Harmony"
                                    },
                                    text = summ.getFamily(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "வெளிநாட்டு வாய்ப்புகள் (Foreign)"
                                        AppLanguage.HINDI -> "विदेश योग एवं यात्रा (Foreign Travels)"
                                        AppLanguage.ENGLISH -> "Foreign Travels"
                                    },
                                    text = summ.getForeignTravel(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "தற்போதைய வழிகாட்டுதல் (Guidance)"
                                        AppLanguage.HINDI -> "समयानुसार मार्गदर्शन एवं मंदिर परिहार"
                                        AppLanguage.ENGLISH -> "Current Planetary Guidance"
                                    },
                                    text = summ.getCurrentPeriodGuidance(lang)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Saved Profiles Tab
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.savedProfiles.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (lang == AppLanguage.TAMIL) "சேமிக்கப்பட்ட ஜாதகங்கள் எதுவும் இல்லை" else "No saved horoscope profiles yet.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                } else {
                    items(state.savedProfiles) { profile ->
                        SavedProfileCard(
                            profile = profile,
                            lang = lang,
                            onLoad = {
                                viewModel.loadProfile(profile)
                                selectedTab = 0
                            },
                            onDelete = { viewModel.deleteProfile(profile) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DoshaItemRow(dosha: DoshaCheckResult, lang: AppLanguage) {
    Surface(
        color = if (dosha.isPresent) TempleKumkum.copy(alpha = 0.08f) else SacredGreen.copy(alpha = 0.08f),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dosha.getName(lang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (dosha.isPresent) TempleKumkum else SacredGreen
                )

                Text(
                    text = dosha.getSeverity(lang),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (dosha.isPresent) TempleKumkum else SacredGreen
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = dosha.getDescription(lang), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)

            if (dosha.isPresent) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "பரிகாரம்: ${dosha.getRemedy(lang)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SummaryAspectRow(title: String, text: String) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 6.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    }
}

@Composable
private fun SavedProfileCard(
    profile: HoroscopeProfileEntity,
    lang: AppLanguage,
    onLoad: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLoad() }
            .testTag("saved_profile_${profile.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = profile.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(
                    text = "${profile.birthYear}-${profile.birthMonth}-${profile.birthDay} | ${profile.birthHour}:${String.format("%02d", profile.birthMinute)} | ${profile.birthPlace}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${profile.nakshatram} (${profile.pada}-ஆம் பாதம்)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold
                )
            }

            Row {
                IconButton(onClick = onLoad) {
                    Icon(imageVector = Icons.Default.Launch, contentDescription = "Load", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
