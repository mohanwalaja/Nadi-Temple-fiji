package com.example.ui.screens.matchmaking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.service.MatchMakingCalculator
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchMakingScreen(
    viewModel: MatchMakingViewModel,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val result = uiState.result

    var showPdfLanguageDialog by remember { mutableStateOf(false) }
    var showManualOverride by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("match_making_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header / Temple Auspicious Banner with Quick PDF Export
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TempleGold.copy(alpha = 0.12f)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(TempleGold)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = TempleMaroon.copy(alpha = 0.15f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("💍", fontSize = 22.sp)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருமணப் பொருத்தம்"
                                    AppLanguage.HINDI -> "श्री शिव सुब्रमण्य स्वामी वैदिक विवाह मिलान"
                                    AppLanguage.ENGLISH -> "Sri Siva Subramaniya Swami Vedic Matchmaking"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TempleMaroon
                            )
                            Text(
                                text = "10 பொருத்தங்கள் • செவ்வாய் தோஷம் • தோஷ சாம்யம்",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Quick PDF Export Button
                        FilledTonalButton(
                            onClick = { showPdfLanguageDialog = true },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = TempleMaroon,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_export_pdf_top")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PictureAsPdf,
                                contentDescription = "PDF Export",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PDF", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Birth Details Input Section: Bride & Groom
            item {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.TAMIL -> "மணமக்கள் பிறந்த விபரம் (Birth Details):"
                        AppLanguage.HINDI -> "वर-वधू जन्म विवरण:"
                        AppLanguage.ENGLISH -> "Bride & Groom Birth Details:"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Side by Side / Stacked Input Cards for Bride & Groom
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Bride Input Card
                    PersonBirthInputCard(
                        title = when (currentLanguage) {
                            AppLanguage.TAMIL -> "மணமகள் (Bride)"
                            AppLanguage.HINDI -> "कन्या (वधू)"
                            AppLanguage.ENGLISH -> "Bride"
                        },
                        icon = "👰",
                        name = uiState.brideName,
                        dob = uiState.brideDob,
                        tob = uiState.brideTob,
                        place = uiState.bridePlace,
                        onNameChange = { viewModel.updateBrideName(it) },
                        onDobChange = { viewModel.updateBrideDob(it) },
                        onTobChange = { viewModel.updateBrideTob(it) },
                        onPlaceChange = { viewModel.updateBridePlace(it) },
                        calculatedRasi = uiState.brideRasi,
                        calculatedNakshatraIdx = uiState.brideNakshatraIndex,
                        calculatedPada = uiState.bridePada,
                        calculatedLagna = uiState.brideLagna,
                        calculatedMarsHouse = uiState.brideMarsHouse,
                        accentColor = Color(0xFFC2185B),
                        currentLanguage = currentLanguage
                    )

                    // Groom Input Card
                    PersonBirthInputCard(
                        title = when (currentLanguage) {
                            AppLanguage.TAMIL -> "மணமகன் (Groom)"
                            AppLanguage.HINDI -> "वर (दूल्हा)"
                            AppLanguage.ENGLISH -> "Groom"
                        },
                        icon = "🤵",
                        name = uiState.groomName,
                        dob = uiState.groomDob,
                        tob = uiState.groomTob,
                        place = uiState.groomPlace,
                        onNameChange = { viewModel.updateGroomName(it) },
                        onDobChange = { viewModel.updateGroomDob(it) },
                        onTobChange = { viewModel.updateGroomTob(it) },
                        onPlaceChange = { viewModel.updateGroomPlace(it) },
                        calculatedRasi = uiState.groomRasi,
                        calculatedNakshatraIdx = uiState.groomNakshatraIndex,
                        calculatedPada = uiState.groomPada,
                        calculatedLagna = uiState.groomLagna,
                        calculatedMarsHouse = uiState.groomMarsHouse,
                        accentColor = TempleMaroon,
                        currentLanguage = currentLanguage
                    )
                }
            }

            // Prominent Action Buttons: Calculate Chart & Export PDF
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Calculate Button
                    Button(
                        onClick = { viewModel.fetchAndCalculateCharts() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TempleMaroon),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_calculate_charts")
                    ) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "ஜாதகம் கணக்கிடு"
                                AppLanguage.HINDI -> "कुंडली व मिलान निकालें"
                                AppLanguage.ENGLISH -> "Calculate Compatibility"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // PDF Export Button
                    FilledTonalButton(
                        onClick = { showPdfLanguageDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = TempleGold.copy(alpha = 0.2f),
                            contentColor = TempleMaroon
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_export_pdf_main")
                    ) {
                        Icon(Icons.Filled.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "PDF அறிக்கை"
                                AppLanguage.HINDI -> "PDF रिपोर्ट"
                                AppLanguage.ENGLISH -> "Export PDF Report"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Toggle for Manual Adjustment
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showManualOverride = !showManualOverride }
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "கைமுறை திருத்தம் (Manual Rasi/Star Override)"
                            AppLanguage.HINDI -> "हस्तचालित सुधार (Manual Override)"
                            AppLanguage.ENGLISH -> "Manual Rasi/Star Override"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TempleMaroon
                    )
                    Icon(
                        imageVector = if (showManualOverride) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = TempleMaroon
                    )
                }
            }

            // Manual Selector (Only if user expands)
            if (showManualOverride) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PersonManualSelectorCard(
                            title = "மணமகள்",
                            selectedRasi = uiState.brideRasi,
                            selectedNakshatraIdx = uiState.brideNakshatraIndex,
                            selectedPada = uiState.bridePada,
                            selectedMarsHouse = uiState.brideMarsHouse,
                            onRasiSelected = { viewModel.updateBrideRasi(it) },
                            onNakshatraSelected = { viewModel.updateBrideNakshatra(it) },
                            onPadaSelected = { viewModel.updateBridePada(it) },
                            onMarsHouseSelected = { viewModel.updateBrideMarsHouse(it) },
                            currentLanguage = currentLanguage,
                            accentColor = Color(0xFFC2185B),
                            modifier = Modifier.weight(1f)
                        )

                        PersonManualSelectorCard(
                            title = "மணமகன்",
                            selectedRasi = uiState.groomRasi,
                            selectedNakshatraIdx = uiState.groomNakshatraIndex,
                            selectedPada = uiState.groomPada,
                            selectedMarsHouse = uiState.groomMarsHouse,
                            onRasiSelected = { viewModel.updateGroomRasi(it) },
                            onNakshatraSelected = { viewModel.updateGroomNakshatra(it) },
                            onPadaSelected = { viewModel.updateGroomPada(it) },
                            onMarsHouseSelected = { viewModel.updateGroomMarsHouse(it) },
                            currentLanguage = currentLanguage,
                            accentColor = TempleMaroon,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Verdict / Score Banner
            item {
                VerdictCard(
                    result = result,
                    currentLanguage = currentLanguage
                )
            }

            // Sub Tab Selector (10 Poruthams vs Sevvay Dosham)
            item {
                TabRow(
                    selectedTabIndex = uiState.selectedDetailTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = TempleMaroon,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = uiState.selectedDetailTab == 0,
                        onClick = { viewModel.setSelectedDetailTab(0) },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.TAMIL -> "10 பொருத்தங்கள்"
                                        AppLanguage.HINDI -> "10 पोरुथम"
                                        AppLanguage.ENGLISH -> "10 Poruthams"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        },
                        modifier = Modifier.testTag("tab_10_poruthams")
                    )
                    Tab(
                        selected = uiState.selectedDetailTab == 1,
                        onClick = { viewModel.setSelectedDetailTab(1) },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Filled.Shield, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.TAMIL -> "செவ்வாய் தோஷம்"
                                        AppLanguage.HINDI -> "मंगल दोष"
                                        AppLanguage.ENGLISH -> "Sevvay Dosham"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        },
                        modifier = Modifier.testTag("tab_sevvay_dosham")
                    )
                }
            }

            if (uiState.selectedDetailTab == 0) {
                // 10 Poruthams List
                items(result.poruthams) { porutham ->
                    PoruthamDetailCard(
                        porutham = porutham,
                        currentLanguage = currentLanguage
                    )
                }
            } else {
                // Sevvay Dosham Analysis Section
                item {
                    SevvayDoshamDetailCard(
                        analysis = result.sevvayDosham,
                        currentLanguage = currentLanguage
                    )
                }
            }
        }

    // PDF Export Language Selection Dialog
    if (showPdfLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showPdfLanguageDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.PictureAsPdf, contentDescription = null, tint = TempleMaroon)
                    Text("PDF மொழி தேர்வு / Export Language", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "திருமணப் பொருத்த அறிக்கையை எந்த மொழியில் PDF ஆக உருவாக்க வேண்டும்?",
                        style = MaterialTheme.typography.bodySmall
                    )

                    // Tamil Option
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showPdfLanguageDialog = false
                                viewModel.exportAndSharePdf(context, AppLanguage.TAMIL)
                            },
                        colors = CardDefaults.cardColors(containerColor = TempleGold.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🇮🇳", fontSize = 20.sp)
                            Column {
                                Text("தமிழ் (Tamil)", fontWeight = FontWeight.Bold, color = TempleMaroon)
                                Text("முழுமையான தமிழ்ப் பஞ்சாங்க பொருத்த அறிக்கை", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    // Hindi Option
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showPdfLanguageDialog = false
                                viewModel.exportAndSharePdf(context, AppLanguage.HINDI)
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🇮🇳", fontSize = 20.sp)
                            Column {
                                Text("हिन्दी (Hindi)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("वैदिक कुंडली मिलान एवं मंगल दोष रिपोर्ट", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    // English Option
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showPdfLanguageDialog = false
                                viewModel.exportAndSharePdf(context, AppLanguage.ENGLISH)
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🌐", fontSize = 20.sp)
                            Column {
                                Text("English", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("Vedic Matchmaking & Kuja Dosha Report", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPdfLanguageDialog = false }) {
                    Text("ரத்து / Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonBirthInputCard(
    title: String,
    icon: String,
    name: String,
    dob: LocalDate,
    tob: LocalTime,
    place: String,
    onNameChange: (String) -> Unit,
    onDobChange: (LocalDate) -> Unit,
    onTobChange: (LocalTime) -> Unit,
    onPlaceChange: (String) -> Unit,
    calculatedRasi: Rasi,
    calculatedNakshatraIdx: Int,
    calculatedPada: Int,
    calculatedLagna: Rasi?,
    calculatedMarsHouse: Int,
    accentColor: Color,
    currentLanguage: AppLanguage
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(icon, fontSize = 20.sp)
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }

                // Calculated Star & Rasi Pill
                val starName = MatchMakingCalculator.NAKSHATRAM_NAMES_TA.getOrElse(calculatedNakshatraIdx) { "" }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${calculatedRasi.getName(currentLanguage)} • $starName ($calculatedPada)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = accentColor.copy(alpha = 0.2f))

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "பெயர் (Name)"
                            AppLanguage.HINDI -> "नाम"
                            AppLanguage.ENGLISH -> "Name"
                        }
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // DOB and TOB Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // DOB Button
                OutlinedCard(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                onDobChange(LocalDate.of(year, month + 1, dayOfMonth))
                            },
                            dob.year,
                            dob.monthValue - 1,
                            dob.dayOfMonth
                        ).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp), tint = accentColor)
                        Column {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "பிறந்த தேதி"
                                    AppLanguage.HINDI -> "जन्म तिथि"
                                    AppLanguage.ENGLISH -> "Birth Date"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                            Text(
                                text = dob.format(DateTimeFormatter.ISO_LOCAL_DATE),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // TOB Button
                OutlinedCard(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                onTobChange(LocalTime.of(hourOfDay, minute))
                            },
                            tob.hour,
                            tob.minute,
                            false
                        ).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Filled.AccessTime, contentDescription = null, modifier = Modifier.size(16.dp), tint = accentColor)
                        Column {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "பிறந்த நேரம்"
                                    AppLanguage.HINDI -> "जन्म समय"
                                    AppLanguage.ENGLISH -> "Birth Time"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                            Text(
                                text = String.format("%02d:%02d", tob.hour, tob.minute),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Birth Place
            var placeExpanded by remember { mutableStateOf(false) }
            val commonPlaces = listOf("Chennai", "Madurai", "Trichy", "Coimbatore", "Salem", "Nadi, Fiji", "Suva, Fiji", "Sydney", "Singapore", "Kuala Lumpur", "London", "New York")

            ExposedDropdownMenuBox(
                expanded = placeExpanded,
                onExpandedChange = { placeExpanded = !placeExpanded }
            ) {
                OutlinedTextField(
                    value = place,
                    onValueChange = onPlaceChange,
                    label = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "பிறந்த இடம் (City/Place)"
                                AppLanguage.HINDI -> "जन्म स्थान"
                                AppLanguage.ENGLISH -> "Birth Place"
                            }
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = placeExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                ExposedDropdownMenu(
                    expanded = placeExpanded,
                    onDismissRequest = { placeExpanded = false }
                ) {
                    commonPlaces.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p) },
                            onClick = {
                                onPlaceChange(p)
                                placeExpanded = false
                            }
                        )
                    }
                }
            }

            // Calculated Summary Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("லக்னம் (Lagna)", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(calculatedLagna?.getName(currentLanguage) ?: "-", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (calculatedMarsHouse in listOf(2, 4, 7, 8, 12)) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("செவ்வாய் நிலை", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "$calculatedMarsHouse-ஆம் இடம் ${if (calculatedMarsHouse in listOf(2, 4, 7, 8, 12)) "⚠️" else "✓"}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (calculatedMarsHouse in listOf(2, 4, 7, 8, 12)) Color(0xFFC62828) else Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonManualSelectorCard(
    title: String,
    selectedRasi: Rasi,
    selectedNakshatraIdx: Int,
    selectedPada: Int,
    selectedMarsHouse: Int,
    onRasiSelected: (Rasi) -> Unit,
    onNakshatraSelected: (Int) -> Unit,
    onPadaSelected: (Int) -> Unit,
    onMarsHouseSelected: (Int) -> Unit,
    currentLanguage: AppLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    var rasiExpanded by remember { mutableStateOf(false) }
    var starExpanded by remember { mutableStateOf(false) }
    var marsExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            // Rasi
            ExposedDropdownMenuBox(
                expanded = rasiExpanded,
                onExpandedChange = { rasiExpanded = !rasiExpanded }
            ) {
                OutlinedTextField(
                    value = selectedRasi.getName(currentLanguage),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("ராசி", fontSize = 10.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = rasiExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(
                    expanded = rasiExpanded,
                    onDismissRequest = { rasiExpanded = false }
                ) {
                    Rasi.entries.forEach { rasi ->
                        DropdownMenuItem(
                            text = { Text("${rasi.symbol} ${rasi.getName(currentLanguage)}", fontSize = 12.sp) },
                            onClick = {
                                onRasiSelected(rasi)
                                rasiExpanded = false
                            }
                        )
                    }
                }
            }

            // Star
            ExposedDropdownMenuBox(
                expanded = starExpanded,
                onExpandedChange = { starExpanded = !starExpanded }
            ) {
                val starName = MatchMakingCalculator.NAKSHATRAM_NAMES_TA.getOrElse(selectedNakshatraIdx) { "" }
                OutlinedTextField(
                    value = starName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("நட்சத்திரம்", fontSize = 10.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = starExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(
                    expanded = starExpanded,
                    onDismissRequest = { starExpanded = false }
                ) {
                    MatchMakingCalculator.NAKSHATRAM_NAMES_TA.indices.forEach { idx ->
                        DropdownMenuItem(
                            text = { Text("${idx + 1}. ${MatchMakingCalculator.NAKSHATRAM_NAMES_TA[idx]}", fontSize = 12.sp) },
                            onClick = {
                                onNakshatraSelected(idx)
                                starExpanded = false
                            }
                        )
                    }
                }
            }

            // Pada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                (1..4).forEach { pada ->
                    val isSelected = selectedPada == pada
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) accentColor else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onPadaSelected(pada) }
                    ) {
                        Text(
                            text = "$pada",
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            // Mars House
            ExposedDropdownMenuBox(
                expanded = marsExpanded,
                onExpandedChange = { marsExpanded = !marsExpanded }
            ) {
                OutlinedTextField(
                    value = "$selectedMarsHouse-ஆம் இடம்",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("செவ்வாய்", fontSize = 10.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = marsExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(
                    expanded = marsExpanded,
                    onDismissRequest = { marsExpanded = false }
                ) {
                    (1..12).forEach { house ->
                        val isDosha = house in listOf(2, 4, 7, 8, 12)
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "$house-ஆம் இடம் ${if (isDosha) "⚠️ தோஷம்" else "(சுபம்)"}",
                                    fontSize = 12.sp,
                                    color = if (isDosha) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onMarsHouseSelected(house)
                                marsExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VerdictCard(
    result: WeddingMatchResult,
    currentLanguage: AppLanguage
) {
    val (bgColor, textColor, _) = when (result.verdictStatus) {
        PoruthamStatus.UTTHAMAM -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "உத்தமப் பொருத்தம்")
        PoruthamStatus.MADHYAMAM -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), "மத்திமப் பொருத்தம்")
        PoruthamStatus.PORUNDHADHU -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "பொருந்தாது")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(textColor.copy(alpha = 0.4f))
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "மொத்த பொருத்தம்"
                            AppLanguage.HINDI -> "कुल मिलान गुण"
                            AppLanguage.ENGLISH -> "Total Match Score"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = textColor
                    )
                    Text(
                        text = "${result.totalPoruthamsMatched} / 10 பொருத்தம் (${result.totalScore} / 10.0)",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = textColor,
                    contentColor = Color.White
                ) {
                    Text(
                        text = result.verdictStatus.getName(currentLanguage),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            HorizontalDivider(color = textColor.copy(alpha = 0.2f))

            // Rajju & Sevvay highlights
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Rajju tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (result.rajjuMatch) Color(0xFFC8E6C9) else Color(0xFFFFCDD2),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(if (result.rajjuMatch) "✅" else "❌", fontSize = 14.sp)
                        Text(
                            text = if (result.rajjuMatch) "ரஜ்ஜு சுபம்" else "ரஜ்ஜு தோஷம்",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (result.rajjuMatch) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                        )
                    }
                }

                // Sevvay tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (result.sevvayDosham.doshaSamyamStatusTa.contains("உண்டு")) Color(0xFFC8E6C9) else Color(0xFFFFF9C4),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🛡️", fontSize = 14.sp)
                        Text(
                            text = if (result.sevvayDosham.doshaSamyamStatusTa.contains("உண்டு")) "செவ்வாய் சமநிலை" else "தோஷ நிவர்த்தி தேவை",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF37474F)
                        )
                    }
                }
            }

            Text(
                text = when (currentLanguage) {
                    AppLanguage.TAMIL -> result.overallVerdictTa
                    AppLanguage.HINDI -> result.overallVerdictHi
                    AppLanguage.ENGLISH -> result.overallVerdictEn
                },
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

@Composable
fun PoruthamDetailCard(
    porutham: SinglePoruthamResult,
    currentLanguage: AppLanguage
) {
    val (statusColor, badgeBg, iconText) = when (porutham.status) {
        PoruthamStatus.UTTHAMAM -> Triple(Color(0xFF2E7D32), Color(0xFFE8F5E9), "✓")
        PoruthamStatus.MADHYAMAM -> Triple(Color(0xFFE65100), Color(0xFFFFF3E0), "≈")
        PoruthamStatus.PORUNDHADHU -> Triple(Color(0xFFC62828), Color(0xFFFFEBEE), "✗")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = badgeBg,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(iconText, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                    Text(
                        text = porutham.getName(currentLanguage),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (porutham.isCrucial) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = TempleGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "மகா பொருத்தம்",
                                style = MaterialTheme.typography.labelSmall,
                                color = TempleMaroon,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = porutham.status.getName(currentLanguage),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = porutham.getExplanation(currentLanguage),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SevvayDoshamDetailCard(
    analysis: SevvayDoshamAnalysis,
    currentLanguage: AppLanguage
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🛡️", fontSize = 18.sp)
                    }
                }
                Column {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "செவ்வாய் தோஷ விளக்கம் (Kuja Dosha)"
                            AppLanguage.HINDI -> "मंगल दोष विश्लेषण"
                            AppLanguage.ENGLISH -> "Sevvay Dosham Analysis"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "லக்னம் மற்றும் ராசியிலிருந்து 2, 4, 7, 8, 12 இடங்களின் ஆய்வு"
                            AppLanguage.HINDI -> "2, 4, 7, 8, 12 भावों का प्रभाव"
                            AppLanguage.ENGLISH -> "Analysis of 2nd, 4th, 7th, 8th, 12th houses"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider()

            // Bride & Groom Dosham comparison
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bride
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (analysis.isBrideHasDosham) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("👰 மணமகள்", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = analysis.brideDoshamSeverity,
                            fontWeight = FontWeight.Bold,
                            color = if (analysis.isBrideHasDosham) Color(0xFFC62828) else Color(0xFF2E7D32),
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (analysis.brideCancellationReasonTa != null) {
                            Text(
                                text = analysis.brideCancellationReasonTa,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF388E3C)
                            )
                        }
                    }
                }

                // Groom
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (analysis.isGroomHasDosham) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("🤵 மணமகன்", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = analysis.groomDoshamSeverity,
                            fontWeight = FontWeight.Bold,
                            color = if (analysis.isGroomHasDosham) Color(0xFFC62828) else Color(0xFF2E7D32),
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (analysis.groomCancellationReasonTa != null) {
                            Text(
                                text = analysis.groomCancellationReasonTa,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF388E3C)
                            )
                        }
                    }
                }
            }

            // Dosha Samyam Verdict
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = TempleGold.copy(alpha = 0.12f),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(TempleGold)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "தோஷ சாம்யம் (Dosha Samyam Balance):",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        color = TempleMaroon
                    )
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> analysis.doshaSamyamStatusTa
                            AppLanguage.HINDI -> analysis.doshaSamyamStatusHi
                            AppLanguage.ENGLISH -> analysis.doshaSamyamStatusEn
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> analysis.recommendationTa
                            AppLanguage.HINDI -> analysis.recommendationHi
                            AppLanguage.ENGLISH -> analysis.recommendationEn
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
