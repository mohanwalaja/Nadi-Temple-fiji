package com.example.ui.screens.jathagam

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.data.local.entities.HoroscopeProfileEntity
import com.example.data.model.AppLanguage
import com.example.data.model.DoshaCheckResult
import com.example.data.model.Graha
import com.example.data.model.HoroscopeResult
import com.example.data.model.PalanTimeframe
import com.example.data.repository.AppStrings
import com.example.data.service.GpsLocationHelper
import com.example.ui.components.SouthIndianRasiChart
import com.example.ui.theme.*
import com.example.util.HoroscopePdfExporter
import kotlinx.coroutines.launch
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val gpsHelper = remember { GpsLocationHelper(context) }
    var isGpsLocating by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isGpsLocating = true
            coroutineScope.launch {
                val data = gpsHelper.getCurrentLocation()
                isGpsLocating = false
                if (data != null) {
                    viewModel.onBirthPlaceChange(data.locationName)
                    Toast.makeText(context, "Location: ${data.locationName}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Could not fetch GPS location", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Location permission required for GPS", Toast.LENGTH_SHORT).show()
        }
    }

    val onUseMyLocationClick: () -> Unit = {
        if (gpsHelper.hasLocationPermission()) {
            isGpsLocating = true
            coroutineScope.launch {
                val data = gpsHelper.getCurrentLocation()
                isGpsLocating = false
                if (data != null) {
                    viewModel.onBirthPlaceChange(data.locationName)
                    Toast.makeText(context, "Location: ${data.locationName}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Could not fetch GPS location", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

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
                    Text(
                        text = when (lang) {
                            AppLanguage.TAMIL -> "சரி"
                            AppLanguage.HINDI -> "ठीक है"
                            AppLanguage.ENGLISH -> "OK"
                        },
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(
                        text = when (lang) {
                            AppLanguage.TAMIL -> "ரத்து"
                            AppLanguage.HINDI -> "रद्द करें"
                            AppLanguage.ENGLISH -> "Cancel"
                        },
                        color = Color.Gray
                    )
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
                    text = when (lang) {
                        AppLanguage.TAMIL -> "பிறந்த நேரம் தேர்வு"
                        AppLanguage.HINDI -> "जन्म समय चुनें"
                        AppLanguage.ENGLISH -> "Select Birth Time"
                    },
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
                    Text(
                        text = when (lang) {
                            AppLanguage.TAMIL -> "சரி"
                            AppLanguage.HINDI -> "ठीक है"
                            AppLanguage.ENGLISH -> "OK"
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(
                        text = when (lang) {
                            AppLanguage.TAMIL -> "ரத்து"
                            AppLanguage.HINDI -> "रद्द करें"
                            AppLanguage.ENGLISH -> "Cancel"
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                        text = when (lang) {
                            AppLanguage.TAMIL -> "ஜாதக கணிப்பு"
                            AppLanguage.HINDI -> "कुंडली कैलकुलेटर"
                            AppLanguage.ENGLISH -> "Horoscope Calculator"
                        },
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
                    val savedTitle = when (lang) {
                        AppLanguage.TAMIL -> "சேமித்தவை"
                        AppLanguage.HINDI -> "सहेजे गए"
                        AppLanguage.ENGLISH -> "Saved"
                    }
                    Text(
                        text = "$savedTitle (${state.savedProfiles.size})",
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
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "பிறப்பு விவரங்கள்"
                                            AppLanguage.HINDI -> "जन्म विवरण"
                                            AppLanguage.ENGLISH -> "Birth Details"
                                        },
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "தேதி, நேரம் மற்றும் பிறந்த ஊரை உள்ளிடவும்"
                                            AppLanguage.HINDI -> "जन्म तिथि, समय एवं स्थान दर्ज करें"
                                            AppLanguage.ENGLISH -> "Enter date, time & location"
                                        },
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
                                         text = when (lang) {
                                             AppLanguage.TAMIL -> "+ புதியது"
                                             AppLanguage.HINDI -> "+ नया"
                                             AppLanguage.ENGLISH -> "+ New"
                                         },
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
                                label = {
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "ஜாதகர் பெயர்"
                                            AppLanguage.HINDI -> "जातक का नाम"
                                            AppLanguage.ENGLISH -> "Devotee Name"
                                        }
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "பெயர் உள்ளிடவும்"
                                            AppLanguage.HINDI -> "नाम दर्ज करें"
                                            AppLanguage.ENGLISH -> "Enter devotee name"
                                        }
                                    )
                                },
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
                                    label = {
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "பிறந்த தேதி"
                                                AppLanguage.HINDI -> "जन्म तिथि"
                                                AppLanguage.ENGLISH -> "Date of Birth"
                                            }
                                        )
                                    },
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
                                    label = {
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "பிறந்த நேரம்"
                                                AppLanguage.HINDI -> "जन्म समय"
                                                AppLanguage.ENGLISH -> "Birth Time"
                                            }
                                        )
                                    },
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
                                label = {
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "பிறந்த ஊர் / நகரம்"
                                            AppLanguage.HINDI -> "जन्म स्थान / शहर"
                                            AppLanguage.ENGLISH -> "Birth Place / City"
                                        }
                                    )
                                },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { onUseMyLocationClick() },
                                        modifier = Modifier.testTag("jathagam_gps_btn")
                                    ) {
                                        if (isGpsLocating) {
                                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.primary)
                                        } else {
                                            Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Use My Location", tint = MaterialTheme.colorScheme.primary)
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
                                    .testTag("input_jathagam_place"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Quick Location Suggestions with Use My Location (GPS) Chip
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // GPS My Location Chip
                                val isGpsActive = state.birthPlace.startsWith("GPS")
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isGpsActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isGpsActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.clickable { onUseMyLocationClick() }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.MyLocation, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "📍 எனது இடம் (GPS)"
                                                AppLanguage.HINDI -> "📍 मेरा स्थान (GPS)"
                                                AppLanguage.ENGLISH -> "📍 My Location (GPS)"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                val popularPlaces = when (lang) {
                                    AppLanguage.TAMIL -> listOf("சென்னை", "மதுரை", "நாடி, பிஜி", "சுவா, பிஜி")
                                    AppLanguage.HINDI -> listOf("चेन्नई", "मदुरै", "नादी, फिजी", "सुवा, फिजी")
                                    AppLanguage.ENGLISH -> listOf("Chennai", "Madurai", "Nadi, Fiji", "Suva, Fiji")
                                }
                                popularPlaces.forEach { placeName ->
                                    val isSelected = state.birthPlace.contains(placeName.substringBefore(","))
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                        ),
                                        modifier = Modifier.clickable { viewModel.onBirthPlaceChange(placeName) }
                                    ) {
                                        Text(
                                            text = placeName,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Timezone Info Badge
                            val tzDesc = if (state.birthPlace.contains("Chennai", ignoreCase = true) ||
                                state.birthPlace.contains("சென்னை", ignoreCase = true) ||
                                state.birthPlace.contains("चेन्नई", ignoreCase = true) ||
                                state.birthPlace.contains("Madurai", ignoreCase = true) ||
                                state.birthPlace.contains("மதுரை", ignoreCase = true) ||
                                state.birthPlace.contains("मदुरै", ignoreCase = true) ||
                                state.birthPlace.contains("India", ignoreCase = true) ||
                                state.birthPlace.contains("भारत", ignoreCase = true)) {
                                when (lang) {
                                    AppLanguage.TAMIL -> "🇮🇳 இந்திய திட்ட நேரம் (IST / UTC+05:30)"
                                    AppLanguage.HINDI -> "🇮🇳 भारतीय मानक समय (IST / UTC+05:30)"
                                    AppLanguage.ENGLISH -> "🇮🇳 Indian Standard Time (IST / UTC+05:30)"
                                }
                            } else {
                                when (lang) {
                                    AppLanguage.TAMIL -> "🇫🇯 பிஜி திட்ட நேரம் (FJT / UTC+12:00)"
                                    AppLanguage.HINDI -> "🇫🇯 फिजी मानक समय (FJT / UTC+12:00)"
                                    AppLanguage.ENGLISH -> "🇫🇯 Fiji Standard Time (FJT / UTC+12:00)"
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "கால மண்டலம்: $tzDesc"
                                            AppLanguage.HINDI -> "समय क्षेत्र: $tzDesc"
                                            AppLanguage.ENGLISH -> "Timezone: $tzDesc"
                                        },
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

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
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "🔮 ஜாதகம் கணிக்க"
                                            AppLanguage.HINDI -> "🔮 कुंडली गणना करें"
                                            AppLanguage.ENGLISH -> "🔮 Calculate Horoscope"
                                        },
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
                                        text = if (state.isSavedSuccessfully) {
                                            when (lang) {
                                                AppLanguage.TAMIL -> "சேமிக்கப்பட்டது!"
                                                AppLanguage.HINDI -> "सहेजा गया!"
                                                AppLanguage.ENGLISH -> "Saved!"
                                            }
                                        } else {
                                            AppStrings.saveProfile(lang)
                                        },
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
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "${result.devoteeName} - ஜாதகக் குறிப்பு"
                                        AppLanguage.HINDI -> "${result.devoteeName} - कुंडली सारांश"
                                        AppLanguage.ENGLISH -> "${result.devoteeName} - Horoscope Summary"
                                    },
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
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "லக்னம்"
                                                AppLanguage.HINDI -> "लग्न"
                                                AppLanguage.ENGLISH -> "Lagna"
                                            },
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(text = "${result.lagnaRasi.getName(lang)} (${String.format("%.1f", result.lagnaDegrees)}°)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Column {
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "சந்திர ராசி"
                                                AppLanguage.HINDI -> "चंद्र राशि"
                                                AppLanguage.ENGLISH -> "Chandra Rasi"
                                            },
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(text = result.chandraRasi.getName(lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Column {
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "நட்சத்திரம் / பாதம்"
                                                AppLanguage.HINDI -> "नक्षत्र / चरण"
                                                AppLanguage.ENGLISH -> "Star / Pada"
                                            },
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "${result.getJanmaNakshatram(lang)} (${if (lang == AppLanguage.TAMIL) "${result.janmaPada}-ம் பாதம்" else if (lang == AppLanguage.HINDI) "चरण ${result.janmaPada}" else "Pada ${result.janmaPada}"})",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 📄 Export PDF Action Card (Supporting all 3 languages: Tamil, English, Hindi)
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("card_jathagam_export_pdf"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "ஜாதக PDF அறிக்கை"
                                                AppLanguage.HINDI -> "कुंडली PDF रिपोर्ट"
                                                AppLanguage.ENGLISH -> "Vedic Horoscope PDF Report"
                                            },
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "ராசிக் கட்டம், நவகிரக நிலைகள், தசா புக்தி மற்றும் பலன்கள் அடங்கிய PDF அறிக்கை"
                                                AppLanguage.HINDI -> "कुंडली चक्र, नवग्रह स्थितियाँ, दशा और फलकथन युक्त PDF रिपोर्ट"
                                                AppLanguage.ENGLISH -> "Complete report with Rasi chart, planetary positions, Dasha & predictions"
                                            },
                                            fontSize = 11.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Quick Language PDF Export Chips
                                Text(
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "மொழியினைத் தேர்ந்தெடுத்து பதிவிறக்கவும்:"
                                        AppLanguage.HINDI -> "भाषा चुनकर PDF डाउनलोड करें:"
                                        AppLanguage.ENGLISH -> "Export PDF in preferred language:"
                                    },
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val languages = listOf(
                                        Triple(when (lang) { AppLanguage.TAMIL -> "தமிழ்"; AppLanguage.HINDI -> "तमिल"; AppLanguage.ENGLISH -> "Tamil" }, AppLanguage.TAMIL, "ta"),
                                        Triple(when (lang) { AppLanguage.TAMIL -> "ஆங்கிலம்"; AppLanguage.HINDI -> "अंग्रेजी"; AppLanguage.ENGLISH -> "English" }, AppLanguage.ENGLISH, "en"),
                                        Triple(when (lang) { AppLanguage.TAMIL -> "இந்தி"; AppLanguage.HINDI -> "हिन्दी"; AppLanguage.ENGLISH -> "Hindi" }, AppLanguage.HINDI, "hi")
                                    )

                                    languages.forEach { (label, exportLang, tag) ->
                                        Button(
                                            onClick = {
                                                val pdfFile = HoroscopePdfExporter.exportHoroscopeToPdf(context, result, exportLang)
                                                if (pdfFile != null) {
                                                    Toast.makeText(context, if (exportLang == AppLanguage.TAMIL) "ஜாதக PDF உருவாக்கப்பட்டது!" else if (exportLang == AppLanguage.HINDI) "कुंडली PDF तैयार है!" else "Horoscope PDF created!", Toast.LENGTH_SHORT).show()
                                                    val shareTitle = "${result.devoteeName} - ${if (exportLang == AppLanguage.TAMIL) "ஜாதக அறிக்கை" else if (exportLang == AppLanguage.HINDI) "कुंडली रिपोर्ट" else "Horoscope Report"}"
                                                    HoroscopePdfExporter.shareOrOpenPdf(context, pdfFile, shareTitle)
                                                } else {
                                                    Toast.makeText(context, "PDF Error", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (lang == exportLang) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                                contentColor = if (lang == exportLang) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("btn_export_pdf_$tag")
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Download,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = label,
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // HTML A4 Web/Printable Export
                                OutlinedButton(
                                    onClick = {
                                        val htmlFile = HoroscopePdfExporter.exportHoroscopeToHtml(context, result, lang)
                                        if (htmlFile != null) {
                                            Toast.makeText(context, if (lang == AppLanguage.TAMIL) "A4 HTML அறிக்கை உருவாக்கப்பட்டது!" else if (lang == AppLanguage.HINDI) "A4 HTML रिपोर्ट तैयार है!" else "A4 HTML Report Generated!", Toast.LENGTH_SHORT).show()
                                            try {
                                                val uri = androidx.core.content.FileProvider.getUriForFile(
                                                    context,
                                                    "${context.packageName}.fileprovider",
                                                    htmlFile
                                                )
                                                val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                                    type = "text/html"
                                                    putExtra(android.content.Intent.EXTRA_STREAM, uri)
                                                    putExtra(android.content.Intent.EXTRA_SUBJECT, "${result.devoteeName} - Horoscope Report (A4 HTML)")
                                                    putExtra(android.content.Intent.EXTRA_TEXT, HoroscopePdfExporter.generateHoroscopeA4Html(result, lang))
                                                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                }
                                                val chooser = android.content.Intent.createChooser(intent, "Share A4 HTML Report")
                                                chooser.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                                context.startActivity(chooser)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_export_html_a4"),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                                ) {
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "🌐 ஒற்றைப் பக்க A4 HTML அறிக்கை"
                                            AppLanguage.HINDI -> "🌐 एक-पृष्ठीय A4 HTML रिपोर्ट"
                                            AppLanguage.ENGLISH -> "🌐 Single-Page A4 HTML Report (Web/Print)"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
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
                                        tint = MaterialTheme.colorScheme.primary,
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
                                        color = MaterialTheme.colorScheme.primary
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
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
                                            tint = MaterialTheme.colorScheme.onPrimary,
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
                                            color = MaterialTheme.colorScheme.onPrimary
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
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "நவக்கிரக நிலைகள்"
                                        AppLanguage.HINDI -> "नवग्रह स्थितियाँ"
                                        AppLanguage.ENGLISH -> "9 Planetary Positions (Navagrahas)"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
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
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1.2f)
                                        )
                                        Text(
                                            text = p.rasi.getName(lang),
                                            fontSize = 12.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "${String.format("%.1f", p.degrees)}° (${p.getNakshatram(lang)} - ${p.pada})",
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
                                                    text = when (lang) {
                                                        AppLanguage.TAMIL -> "வக்ரம்"
                                                        AppLanguage.HINDI -> "वक्री"
                                                        AppLanguage.ENGLISH -> "Retrograde"
                                                    },
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
                                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.TAMIL -> "சனிப் பெயர்ச்சி நிலை"
                                            AppLanguage.HINDI -> "शनि गोचर स्थिति"
                                            AppLanguage.ENGLISH -> "Saturn Transit Analysis"
                                        },
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
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
                                                when (lang) {
                                                    AppLanguage.TAMIL -> "ஏழரை சனி நடப்பு: ${sani.getEzharaiType(lang)}"
                                                    AppLanguage.HINDI -> "साढ़े साती चल रही है: ${sani.getEzharaiType(lang)}"
                                                    AppLanguage.ENGLISH -> "Sade Sati Active: ${sani.getEzharaiType(lang)}"
                                                }
                                            } else if (sani.isAshtamaSani) {
                                                when (lang) {
                                                    AppLanguage.TAMIL -> "அஷ்டம சனி நடப்பு"
                                                    AppLanguage.HINDI -> "अष्टम शनि चल रहा है"
                                                    AppLanguage.ENGLISH -> "Ashtama Sani Active"
                                                }
                                            } else if (sani.isKandakaSani) {
                                                when (lang) {
                                                    AppLanguage.TAMIL -> "கண்டக சனி நடப்பு"
                                                    AppLanguage.HINDI -> "कंटक शनि चल रहा है"
                                                    AppLanguage.ENGLISH -> "Kandaka Sani Active"
                                                }
                                            } else {
                                                when (lang) {
                                                    AppLanguage.TAMIL -> "ஏழரை / அஷ்டம சனி தாக்கம் இல்லை (அனுகூல காலம்)"
                                                    AppLanguage.HINDI -> "साढ़े साती / अष्टम शनि का प्रभाव नहीं है (अनुकूल समय)"
                                                    AppLanguage.ENGLISH -> "No Sade Sati / Ashtama Sani active (Favorable Period)"
                                                }
                                            },
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (sani.isEzharaiSani || sani.isAshtamaSani) TempleKumkum else SacredGreen
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = when (lang) {
                                                AppLanguage.TAMIL -> "பரிகாரம்: ${sani.getRemedy(lang)}"
                                                AppLanguage.HINDI -> "उपाय: ${sani.getRemedy(lang)}"
                                                AppLanguage.ENGLISH -> "Remedy: ${sani.getRemedy(lang)}"
                                            },
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
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "தோஷ பரிசீலனை"
                                        AppLanguage.HINDI -> "दोष विचार एवं परिहार"
                                        AppLanguage.ENGLISH -> "Dosha Assessment & Remedies"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
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
                                        AppLanguage.TAMIL -> "ஆரோக்கியம்"
                                        AppLanguage.HINDI -> "स्वास्थ्य"
                                        AppLanguage.ENGLISH -> "Health & Vitality"
                                    },
                                    text = summ.getHealth(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "தனம் & நிதி"
                                        AppLanguage.HINDI -> "धन एवं संपत्ति"
                                        AppLanguage.ENGLISH -> "Wealth & Finances"
                                    },
                                    text = summ.getWealth(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "கல்வி"
                                        AppLanguage.HINDI -> "शिक्षा एवं विद्या"
                                        AppLanguage.ENGLISH -> "Education & Intellect"
                                    },
                                    text = summ.getEducation(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "தொழில் & வேலை"
                                        AppLanguage.HINDI -> "व्यवसाय एवं आजीविका"
                                        AppLanguage.ENGLISH -> "Career & Status"
                                    },
                                    text = summ.getCareer(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "திருமணம்"
                                        AppLanguage.HINDI -> "विवाह एवं संबंध"
                                        AppLanguage.ENGLISH -> "Marriage & Union"
                                    },
                                    text = summ.getMarriage(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "குடும்பம்"
                                        AppLanguage.HINDI -> "परिवार एवं सुख"
                                        AppLanguage.ENGLISH -> "Family & Harmony"
                                    },
                                    text = summ.getFamily(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "வெளிநாட்டு வாய்ப்புகள்"
                                        AppLanguage.HINDI -> "विदेश योग एवं यात्रा"
                                        AppLanguage.ENGLISH -> "Foreign Travels"
                                    },
                                    text = summ.getForeignTravel(lang)
                                )
                                SummaryAspectRow(
                                    title = when (lang) {
                                        AppLanguage.TAMIL -> "தற்போதைய வழிகாட்டுதல்"
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
                                    text = when (lang) {
                                        AppLanguage.TAMIL -> "சேமிக்கப்பட்ட ஜாதகங்கள் எதுவும் இல்லை"
                                        AppLanguage.HINDI -> "कोई सहेजी गई कुंडली नहीं है"
                                        AppLanguage.ENGLISH -> "No saved horoscope profiles yet."
                                    },
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
                    text = when (lang) {
                        AppLanguage.TAMIL -> "பரிகாரம்: ${dosha.getRemedy(lang)}"
                        AppLanguage.HINDI -> "उपाय: ${dosha.getRemedy(lang)}"
                        AppLanguage.ENGLISH -> "Remedy: ${dosha.getRemedy(lang)}"
                    },
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
                    text = when (lang) {
                        AppLanguage.TAMIL -> "${profile.nakshatram} (${profile.pada}-ஆம் பாதம்)"
                        AppLanguage.HINDI -> "${profile.nakshatram} (चरण ${profile.pada})"
                        AppLanguage.ENGLISH -> "${profile.nakshatram} (Pada ${profile.pada})"
                    },
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
