package com.example.ui.screens.panchangam

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.repository.AppStrings
import com.example.data.service.GpsLocationHelper
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanchangamScreen(
    viewModel: PanchangamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val gpsHelper = remember { GpsLocationHelper(context) }
    var isLocating by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isLocating = true
            coroutineScope.launch {
                val data = gpsHelper.getCurrentLocation()
                isLocating = false
                if (data != null) {
                    val gpsString = "GPS: ${data.latitude}, ${data.longitude}, ${data.utcOffsetHours}"
                    viewModel.setLocation(gpsString)
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
            isLocating = true
            coroutineScope.launch {
                val data = gpsHelper.getCurrentLocation()
                isLocating = false
                if (data != null) {
                    viewModel.setCoordinates(data.latitude, data.longitude, data.utcOffsetHours, data.locationName)
                    Toast.makeText(context, "Location set: ${data.locationName}", Toast.LENGTH_SHORT).show()
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
    val panchangam = state.panchangamDetail
    var showLocationDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("panchangam_screen_container"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Date Navigator Card & Location selector
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Top location bar
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.clickable { showLocationDialog = true }
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Location",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    val displayName = state.parsedLocation?.let {
                                        if (lang == AppLanguage.TAMIL) it.nameTa else if (lang == AppLanguage.HINDI) it.nameHi else it.nameEn
                                    } ?: state.selectedLocation
                                    Text(
                                        text = displayName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Location",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                state.parsedLocation?.let { coords ->
                                    val latStr = String.format(Locale.US, "%.2f°%s", kotlin.math.abs(coords.lat), if (coords.lat >= 0) "N" else "S")
                                    val lonStr = String.format(Locale.US, "%.2f°%s", kotlin.math.abs(coords.lon), if (coords.lon >= 0) "E" else "W")
                                    val tzStr = String.format(Locale.US, "UTC%+05.1f", coords.timeZoneOffsetHours)
                                    Text(
                                        text = "$latStr, $lonStr • $tzStr",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Quick 1-tap presets with Use My Location button
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // GPS Button
                            val isGpsSelected = state.selectedLocation.startsWith("GPS")
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isGpsSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isGpsSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier
                                    .weight(1.3f)
                                    .clickable { onUseMyLocationClick() }
                                    .testTag("use_my_location_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    if (isLocating) {
                                        CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 1.5.dp, color = MaterialTheme.colorScheme.primary)
                                    } else {
                                        Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Use My Location", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(13.dp))
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (lang == AppLanguage.TAMIL) "📍 எனது GPS" else if (lang == AppLanguage.HINDI) "📍 मेरा GPS" else "📍 My GPS",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            val quickPresets = listOf(
                                "🇫🇯 நாடி" to "நாடி, பிஜி தீவுகள்",
                                "🇮🇳 சென்னை" to "சென்னை, தமிழ்நாடு",
                                "🌍 உலகம்" to ""
                            )
                            quickPresets.forEach { (label, loc) ->
                                val isSelected = loc.isNotEmpty() && state.selectedLocation.contains(loc.substringBefore(","))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    border = androidx.compose.foundation.BorderStroke(
                                        0.5.dp,
                                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            if (loc.isEmpty()) {
                                                showLocationDialog = true
                                            } else {
                                                viewModel.setLocation(loc)
                                            }
                                        }
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Date Switcher Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.previousDay() },
                            modifier = Modifier.testTag("panchangam_prev_day_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous Day",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val engFormatted = state.selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH))
                            val tamilDateStr = if (panchangam != null) {
                                "${panchangam.tamilMonth.name(lang)} ${panchangam.tamilDate} | ${panchangam.tamilYear.tamilName} வருடம்"
                            } else ""

                            Text(
                                text = tamilDateStr,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = engFormatted,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { viewModel.nextDay() },
                            modifier = Modifier.testTag("panchangam_next_day_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Day",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Today shortcut button if not selected today
                    if (state.selectedDate != LocalDate.now()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(
                            onClick = { viewModel.setDate(LocalDate.now()) },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "இன்றைய தேதிக்குச் செல்லவும்" else "Go to Today",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        if (panchangam != null) {
            // 2. Special Observance Banner (if any)
            if (panchangam.specialObservances.isNotEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (lang == AppLanguage.TAMIL) "இன்றைய விசேஷ விரத நாள்" else "Today's Special Observance",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = panchangam.specialObservances.joinToString(" • ") { it.getDisplayName(lang) },
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // 3. Complete Drik Panchangam 10 Core Limbs Card (Clean compact text format)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_panchangam_10_angams"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "பஞ்சாங்கம் (10 அங்கங்கள்)" else if (lang == AppLanguage.HINDI) "पंचांग (10 अंग)" else "Drik Panchangam (10 Limbs)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "Drik Ganitha",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val angamsList = listOf(
                            Triple("1", if (lang == AppLanguage.TAMIL) "சம்வத்சரம் (Samvatsaram)" else if (lang == AppLanguage.HINDI) "संवत्सर (Samvatsaram)" else "Samvatsaram (Tamil Year)", panchangam.samvatsaraName),
                            Triple("2", if (lang == AppLanguage.TAMIL) "அயனம் (Ayanam)" else if (lang == AppLanguage.HINDI) "अयन (Ayanam)" else "Ayanam (Solar Movement)", panchangam.ayanam),
                            Triple("3", if (lang == AppLanguage.TAMIL) "ருது (Rithu)" else if (lang == AppLanguage.HINDI) "ऋतु (Rithu)" else "Rithu (Season)", panchangam.ritu),
                            Triple("4", if (lang == AppLanguage.TAMIL) "மாசம் (Masam)" else if (lang == AppLanguage.HINDI) "मास (Masam)" else "Masam (Solar Month)", panchangam.sanskritMonth),
                            Triple("5", if (lang == AppLanguage.TAMIL) "பக்ஷம் (Paksham)" else if (lang == AppLanguage.HINDI) "पक्ष (Paksham)" else "Paksham (Fortnight)", panchangam.paksha),
                            Triple("6", if (lang == AppLanguage.TAMIL) "திதி (Tithi)" else if (lang == AppLanguage.HINDI) "तिथि (Tithi)" else "Tithi (Lunar Day)", "${panchangam.tithi} (${panchangam.tithiEndTime} வரை)"),
                            Triple("7", if (lang == AppLanguage.TAMIL) "வாசரம் (Vasaram)" else if (lang == AppLanguage.HINDI) "वासर (Vasaram)" else "Vasaram (Weekday)", "${panchangam.vasaram} (${panchangam.dayOfWeek})"),
                            Triple("8", if (lang == AppLanguage.TAMIL) "நட்சத்திரம் (Nakshatram)" else if (lang == AppLanguage.HINDI) "नक्षत्र (Nakshatram)" else "Nakshatram (Asterism)", "${panchangam.nakshatram} பாதம் ${panchangam.pada} (${panchangam.nakshatramEndTime} வரை)"),
                            Triple("9", if (lang == AppLanguage.TAMIL) "யோகம் (Yogam)" else if (lang == AppLanguage.HINDI) "योग (Yogam)" else "Yogam (Nithya / Dina Yoga)", "${panchangam.yogam} / ${panchangam.dinaYogam} (${panchangam.yogamEndTime} வரை)"),
                            Triple("10", if (lang == AppLanguage.TAMIL) "கரணம் (Karanam)" else if (lang == AppLanguage.HINDI) "करण (Karanam)" else "Karanam (Half-Tithi)", "${panchangam.karanam} (${panchangam.karanamEndTime} வரை)")
                        )

                        angamsList.forEachIndexed { index, (num, label, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "$num. $label",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1.1f)
                                )
                                Text(
                                    text = value,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1.4f)
                                )
                            }
                            if (index < angamsList.size - 1) {
                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Chandrashtamam Warning Card (if applicable)
            if (panchangam.chandrashtamam.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = TempleKumkum.copy(alpha = 0.08f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TempleKumkum.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = "Chandrashtamam",
                                tint = TempleKumkum,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (lang == AppLanguage.TAMIL) "சந்திராஷ்டமம்" else "Chandrashtamam",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TempleKumkum
                                )
                                Text(
                                    text = panchangam.chandrashtamam,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // 5. Auspicious Timings Card (சுப முகூர்த்த / நல்ல நேரம்)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (lang == AppLanguage.TAMIL) "சுப நேரங்கள் (Auspicious Timings)" else "Auspicious Timings",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SacredGreen
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Nalla Neram
                        TimingInfoBlock(
                            title = if (lang == AppLanguage.TAMIL) "நல்ல நேரம்" else "Nalla Neram",
                            line1 = "காலை: ${panchangam.nallaNeramMorning}",
                            line2 = "மாலை: ${panchangam.nallaNeramEvening}",
                            accentColor = SacredGreen
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Gowri Nalla Neram
                        TimingInfoBlock(
                            title = if (lang == AppLanguage.TAMIL) "கௌரி நல்ல நேரம்" else "Gowri Nalla Neram",
                            line1 = "காலை: ${panchangam.gowriNallaNeramMorning}",
                            line2 = "மாலை: ${panchangam.gowriNallaNeramEvening}",
                            accentColor = TempleMaroon
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Abhijit
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SacredGreen.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "அபிஜித் முகூர்த்தம்" else "Abhijit Muhurtham",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = panchangam.abhijitMuhurtham,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SacredGreen
                            )
                        }
                    }
                }
            }

            // 6. Inauspicious Periods & Disha Soola (அசுப நேரங்கள் & திசா சூலம்)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (lang == AppLanguage.TAMIL) "அசுப நேரங்கள் & திசா சூலம்" else "Inauspicious Timings & Soola",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TempleKumkum
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Rahu, Yama, Kuligai
                        Surface(
                            color = TempleKumkum.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = AppStrings.rahuKalam(lang), fontSize = 12.sp)
                                    Text(text = panchangam.rahuKalam, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TempleKumkum)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = AppStrings.yamagandam(lang), fontSize = 12.sp)
                                    Text(text = panchangam.yamagandam, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = AppStrings.kuligai(lang), fontSize = 12.sp)
                                    Text(text = panchangam.kuligai, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "துர்முஹூர்த்தம்", fontSize = 12.sp)
                                    Text(text = panchangam.durMuhurtham, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "வர்ஜ்யம்", fontSize = 12.sp)
                                    Text(text = panchangam.varjyam, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Disha Soola & Pariharam
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "சூலம்: ${panchangam.dishaSoola}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "பரிகாரம்: ${panchangam.soolaPariharam}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TempleMaroon
                                )
                            }
                        }
                    }
                }
            }

            // 7. Solar & Lunar Timings Card (சூரிய / சந்திர நிலைகள்)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (lang == AppLanguage.TAMIL) "சூரிய - சந்திர காலங்கள் & ராசி நிலைகள்" else "Solar, Lunar & Signs",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TempleMaroon
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TimeBox(title = AppStrings.sunrise(lang), time = panchangam.sunrise, icon = Icons.Default.WbSunny, color = TempleGoldDark, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            TimeBox(title = AppStrings.sunset(lang), time = panchangam.sunset, icon = Icons.Default.WbTwilight, color = TempleSaffron, modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TimeBox(title = AppStrings.moonrise(lang), time = panchangam.moonrise, icon = Icons.Default.NightlightRound, color = TempleMaroonLight, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            TimeBox(title = AppStrings.moonset(lang), time = panchangam.moonset, icon = Icons.Default.Bedtime, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "சூரிய ராசி: ${panchangam.suryaRasi}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "சந்திர ராசி: ${panchangam.chandraRasi}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "அயனம்: ${panchangam.ayanam}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "ருது: ${panchangam.ritu}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // Location Selection Dialog
    if (showLocationDialog) {
        LocationSelectionDialog(
            currentLocation = state.selectedLocation,
            availableLocations = state.availableLocations,
            lang = lang,
            isLocating = isLocating,
            onUseMyLocationClick = {
                showLocationDialog = false
                onUseMyLocationClick()
            },
            onSelectPreset = { loc ->
                viewModel.setLocation(loc.nameTa)
                showLocationDialog = false
            },
            onSaveCustomCoordinates = { lat, lon, offset, name ->
                viewModel.setCoordinates(lat, lon, offset, name)
                showLocationDialog = false
            },
            onDismiss = { showLocationDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationSelectionDialog(
    currentLocation: String,
    availableLocations: List<com.example.data.service.LocationCoordinates>,
    lang: AppLanguage,
    isLocating: Boolean,
    onUseMyLocationClick: () -> Unit,
    onSelectPreset: (com.example.data.service.LocationCoordinates) -> Unit,
    onSaveCustomCoordinates: (Double, Double, Double, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedRegion by remember { mutableStateOf("All") }

    // Custom coordinates state
    var customLat by remember { mutableStateOf("") }
    var customLon by remember { mutableStateOf("") }
    var customTz by remember { mutableStateOf("") }
    var customName by remember { mutableStateOf("") }
    var customError by remember { mutableStateOf<String?>(null) }

    val regions = remember(availableLocations) {
        listOf("All") + availableLocations.map { it.region }.distinct()
    }

    val filteredLocations = remember(availableLocations, searchQuery, selectedRegion) {
        availableLocations.filter { loc ->
            val matchesRegion = selectedRegion == "All" || loc.region.equals(selectedRegion, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    loc.nameTa.contains(searchQuery, ignoreCase = true) ||
                    loc.nameEn.contains(searchQuery, ignoreCase = true) ||
                    loc.nameHi.contains(searchQuery, ignoreCase = true) ||
                    loc.region.contains(searchQuery, ignoreCase = true)
            matchesRegion && matchesSearch
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (lang == AppLanguage.TAMIL) "இருப்பிடம் தேர்வு (Location)" else if (lang == AppLanguage.HINDI) "स्थान का चयन (Location)" else "Select Location",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp)) {
                // GPS One-tap card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onUseMyLocationClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isLocating) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.primary)
                        } else {
                            Icon(imageVector = Icons.Default.MyLocation, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "📍 எனது இருப்பிடம் (GPS)" else if (lang == AppLanguage.HINDI) "📍 मेरा स्थान (GPS)" else "📍 Use My GPS Location",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "துல்லியமான அட்சரேகை & தீர்க்கரேகை கணக்கீடு" else "Calculates exact Lat, Lon & Local Solar Ephemeris",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tabs: World Cities vs Custom Coordinates
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "🌍 உலக நகரங்கள்" else "🌍 World Cities",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "🧭 தனிப்பயன் (Lat/Lon)" else "🧭 Custom Coordinates",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (selectedTab == 0) {
                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = if (lang == AppLanguage.TAMIL) "நகரம் / நாடு தேடவும்..." else "Search city or country...", fontSize = 12.sp) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(16.dp)) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Regions scrollable chips
                    androidx.compose.foundation.lazy.LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(regions.size) { idx ->
                            val r = regions[idx]
                            val isSelected = selectedRegion == r
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { selectedRegion = r }
                            ) {
                                Text(
                                    text = r,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cities list
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredLocations.size) { idx ->
                            val loc = filteredLocations[idx]
                            val isSelected = currentLocation.contains(loc.nameTa.substringBefore(",")) ||
                                    currentLocation.contains(loc.nameEn.substringBefore(","))
                            val title = if (lang == AppLanguage.TAMIL) loc.nameTa else if (lang == AppLanguage.HINDI) loc.nameHi else loc.nameEn
                            val latStr = String.format(Locale.US, "%.2f°%s", kotlin.math.abs(loc.lat), if (loc.lat >= 0) "N" else "S")
                            val lonStr = String.format(Locale.US, "%.2f°%s", kotlin.math.abs(loc.lon), if (loc.lon >= 0) "E" else "W")
                            val tzStr = String.format(Locale.US, "UTC%+05.1f", loc.timeZoneOffsetHours)

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                border = androidx.compose.foundation.BorderStroke(
                                    0.5.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectPreset(loc) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { onSelectPreset(loc) },
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = title,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "$latStr, $lonStr • $tzStr (${loc.region})",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Custom coordinates form
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "எந்தவொரு ஊர்/கோயிலின் அட்சரேகை & தீர்க்கரேகையை உள்ளிடவும்:" else "Enter exact coordinates for any town, village or temple:",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = customName,
                                onValueChange = { customName = it },
                                label = { Text(if (lang == AppLanguage.TAMIL) "இடத்தின் பெயர் (Place Name)" else "Location / Temple Name", fontSize = 11.sp) },
                                placeholder = { Text("e.g. Sri Shiva Temple, Houston", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = customLat,
                                    onValueChange = { customLat = it },
                                    label = { Text("Latitude (°N/S)", fontSize = 11.sp) },
                                    placeholder = { Text("e.g. 13.0827", fontSize = 11.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = customLon,
                                    onValueChange = {
                                        customLon = it
                                        val lonVal = it.toDoubleOrNull()
                                        if (lonVal != null && customTz.isBlank()) {
                                            val estTz = kotlin.math.round((lonVal / 15.0) * 2.0) / 2.0
                                            customTz = String.format(Locale.US, "%.1f", estTz)
                                        }
                                    },
                                    label = { Text("Longitude (°E/W)", fontSize = 11.sp) },
                                    placeholder = { Text("e.g. 80.2707", fontSize = 11.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = customTz,
                                onValueChange = { customTz = it },
                                label = { Text("UTC Time Zone Offset (Hours)", fontSize = 11.sp) },
                                placeholder = { Text("e.g. +5.5 for India, +12.0 for Fiji, -5.0 for NY", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (customError != null) {
                            item {
                                Text(
                                    text = customError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        item {
                            Button(
                                onClick = {
                                    val lat = customLat.trim().toDoubleOrNull()
                                    val lon = customLon.trim().toDoubleOrNull()
                                    val tz = customTz.trim().toDoubleOrNull() ?: if (lon != null) (kotlin.math.round((lon / 15.0) * 2.0) / 2.0) else null
                                    val name = if (customName.trim().isNotBlank()) customName.trim() else "Custom Location"

                                    if (lat == null || lat < -90.0 || lat > 90.0) {
                                        customError = "Please enter a valid Latitude (-90.0 to +90.0)"
                                    } else if (lon == null || lon < -180.0 || lon > 180.0) {
                                        customError = "Please enter a valid Longitude (-180.0 to +180.0)"
                                    } else if (tz == null || tz < -12.0 || tz > 14.0) {
                                        customError = "Please enter a valid Timezone Offset (-12.0 to +14.0)"
                                    } else {
                                        customError = null
                                        onSaveCustomCoordinates(lat, lon, tz, name)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (lang == AppLanguage.TAMIL) "பயன்படுத்து & கணக்கிடு" else "Apply Coordinates")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = if (lang == AppLanguage.TAMIL) "மூடு" else "Close")
            }
        }
    )
}

@Composable
private fun DrikAngamItem(
    number: String,
    titleTa: String,
    titleEn: String,
    value: String,
    subValue: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    lang: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = color.copy(alpha = 0.12f),
            modifier = Modifier.size(34.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (lang == AppLanguage.TAMIL) titleTa else titleEn,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subValue.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subValue,
                    fontSize = 11.sp,
                    color = color,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun TimingInfoBlock(
    title: String,
    line1: String,
    line2: String,
    accentColor: Color
) {
    Surface(
        color = accentColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = line1, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = line2, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun TimeBox(
    title: String,
    time: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.08f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = time,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}
