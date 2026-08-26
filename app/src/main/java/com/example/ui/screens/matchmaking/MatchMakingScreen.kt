package com.example.ui.screens.matchmaking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import com.example.ui.components.LocationSearchDialog
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
        // Header / Auspicious Banner with Quick PDF Export
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outline)
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
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
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
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "10 பொருத்தங்கள் • செவ்வாய் தோஷம் • தோஷ சாம்யம்"
                                    AppLanguage.HINDI -> "10 पोरुथम • मंगल दोष • दोष साम्य"
                                    AppLanguage.ENGLISH -> "10 Poruthams • Sevvay Dosham • Dosha Samyam"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Quick PDF Export Button
                        FilledTonalButton(
                            onClick = { showPdfLanguageDialog = true },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
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
                        AppLanguage.TAMIL -> "மணமக்கள் பிறந்த விபரம்:"
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
                            AppLanguage.TAMIL -> "மணமகள்"
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
                            AppLanguage.TAMIL -> "மணமகன்"
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
                        accentColor = MaterialTheme.colorScheme.primary,
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
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.primary
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
                            AppLanguage.TAMIL -> "கைமுறை திருத்தம்"
                            AppLanguage.HINDI -> "हस्तचालित सुधार"
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
                            title = when (currentLanguage) {
                                AppLanguage.TAMIL -> "மணமகள்"
                                AppLanguage.HINDI -> "वधू"
                                AppLanguage.ENGLISH -> "Bride"
                            },
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
                            title = when (currentLanguage) {
                                AppLanguage.TAMIL -> "மணமகன்"
                                AppLanguage.HINDI -> "वर"
                                AppLanguage.ENGLISH -> "Groom"
                            },
                            selectedRasi = uiState.groomRasi,
                            selectedNakshatraIdx = uiState.groomNakshatraIndex,
                            selectedPada = uiState.groomPada,
                            selectedMarsHouse = uiState.groomMarsHouse,
                            onRasiSelected = { viewModel.updateGroomRasi(it) },
                            onNakshatraSelected = { viewModel.updateGroomNakshatra(it) },
                            onPadaSelected = { viewModel.updateGroomPada(it) },
                            onMarsHouseSelected = { viewModel.updateGroomMarsHouse(it) },
                            currentLanguage = currentLanguage,
                            accentColor = MaterialTheme.colorScheme.primary,
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
                    contentColor = MaterialTheme.colorScheme.primary,
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
                    Icon(Icons.Filled.PictureAsPdf, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "PDF மொழி தேர்வு"
                            AppLanguage.HINDI -> "PDF भाषा चयन"
                            AppLanguage.ENGLISH -> "PDF Export Language"
                        },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "திருமணப் பொருத்த அறிக்கையை எந்த மொழியில் PDF ஆக உருவாக்க வேண்டும்?"
                            AppLanguage.HINDI -> "विवाह मिलान रिपोर्ट किस भाषा में PDF बनानी है?"
                            AppLanguage.ENGLISH -> "Select the language for the Matchmaking PDF Report:"
                        },
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
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🇮🇳", fontSize = 20.sp)
                            Column {
                                Text("தமிழ் (Tamil)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
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
                    Text(
                        when (currentLanguage) {
                            AppLanguage.TAMIL -> "ரத்து"
                            AppLanguage.HINDI -> "रद्द करें"
                            AppLanguage.ENGLISH -> "Cancel"
                        }
                    )
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
                val nakshatramNames = when (currentLanguage) {
                    AppLanguage.TAMIL -> MatchMakingCalculator.NAKSHATRAM_NAMES_TA
                    AppLanguage.HINDI -> MatchMakingCalculator.NAKSHATRAM_NAMES_HI
                    AppLanguage.ENGLISH -> MatchMakingCalculator.NAKSHATRAM_NAMES_EN
                }
                val starName = nakshatramNames.getOrElse(calculatedNakshatraIdx) { "" }
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
                            AppLanguage.TAMIL -> "பெயர்"
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

            // Birth Place with Google Maps / Search Picker
            var showLocationSearchDialog by remember { mutableStateOf(false) }

            if (showLocationSearchDialog) {
                LocationSearchDialog(
                    currentLanguage = currentLanguage,
                    initialPlace = place,
                    onDismiss = { showLocationSearchDialog = false },
                    onSelectLocation = { newPlace ->
                        onPlaceChange(newPlace)
                        showLocationSearchDialog = false
                    }
                )
            }

            OutlinedTextField(
                value = place,
                onValueChange = onPlaceChange,
                label = {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "பிறந்த இடம்"
                            AppLanguage.HINDI -> "जन्म स्थान"
                            AppLanguage.ENGLISH -> "Birth Place"
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = accentColor
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { showLocationSearchDialog = true }
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search Location",
                            tint = accentColor
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            // Quick Map Picker Button
            OutlinedButton(
                onClick = { showLocationSearchDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Filled.Map, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "🗺️ கூகுள் மேப்ஸ் / ஊர் தேடல்"
                            AppLanguage.HINDI -> "🗺️ गूगल मैप्स / स्थान खोजें"
                            AppLanguage.ENGLISH -> "🗺️ Google Maps / Search Location"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
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
                        Text(
                            when (currentLanguage) {
                                AppLanguage.TAMIL -> "லக்னம்"
                                AppLanguage.HINDI -> "लग्न"
                                AppLanguage.ENGLISH -> "Lagna"
                            },
                            fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(calculatedLagna?.getName(currentLanguage) ?: "-", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            when (currentLanguage) {
                                AppLanguage.TAMIL -> "செவ்வாய் நிலை"
                                AppLanguage.HINDI -> "मंगल स्थिति"
                                AppLanguage.ENGLISH -> "Mars Status"
                            },
                            fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "$calculatedMarsHouse-ஆம் இடம்"
                                AppLanguage.HINDI -> "भाव $calculatedMarsHouse"
                                AppLanguage.ENGLISH -> "House $calculatedMarsHouse"
                            } + " " + (if (calculatedMarsHouse in listOf(2, 4, 7, 8, 12)) "⚠️" else "✓"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
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
                    label = {
                        Text(
                            when (currentLanguage) {
                                AppLanguage.TAMIL -> "ராசி"
                                AppLanguage.HINDI -> "राशि"
                                AppLanguage.ENGLISH -> "Rasi"
                            },
                            fontSize = 10.sp
                        )
                    },
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
                val nakshatramNames = when (currentLanguage) {
                    AppLanguage.TAMIL -> MatchMakingCalculator.NAKSHATRAM_NAMES_TA
                    AppLanguage.HINDI -> MatchMakingCalculator.NAKSHATRAM_NAMES_HI
                    AppLanguage.ENGLISH -> MatchMakingCalculator.NAKSHATRAM_NAMES_EN
                }
                val starName = nakshatramNames.getOrElse(selectedNakshatraIdx) { "" }
                OutlinedTextField(
                    value = starName,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            when (currentLanguage) {
                                AppLanguage.TAMIL -> "நட்சத்திரம்"
                                AppLanguage.HINDI -> "नक्षत्र"
                                AppLanguage.ENGLISH -> "Nakshatram"
                            },
                            fontSize = 10.sp
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = starExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(
                    expanded = starExpanded,
                    onDismissRequest = { starExpanded = false }
                ) {
                    nakshatramNames.indices.forEach { idx ->
                        DropdownMenuItem(
                            text = { Text("${idx + 1}. ${nakshatramNames[idx]}", fontSize = 12.sp) },
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
                    value = when (currentLanguage) {
                        AppLanguage.TAMIL -> "$selectedMarsHouse-ஆம் இடம்"
                        AppLanguage.HINDI -> "भाव $selectedMarsHouse"
                        AppLanguage.ENGLISH -> "House $selectedMarsHouse"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            when (currentLanguage) {
                                AppLanguage.TAMIL -> "செவ்வாய்"
                                AppLanguage.HINDI -> "मंगल भाव"
                                AppLanguage.ENGLISH -> "Mars House"
                            },
                            fontSize = 10.sp
                        )
                    },
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
                                    text = when (currentLanguage) {
                                        AppLanguage.TAMIL -> "$house-ஆம் இடம் ${if (isDosha) "⚠️ தோஷம்" else "(சுபம்)"}"
                                        AppLanguage.HINDI -> "भाव $house ${if (isDosha) "⚠️ दोष" else "(शुभ)"}"
                                        AppLanguage.ENGLISH -> "House $house ${if (isDosha) "⚠️ Dosha" else "(Auspicious)"}"
                                    },
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
    val containerBg = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "${result.totalPoruthamsMatched} / 10 பொருத்தம் (${result.totalScore} / 10.0)"
                            AppLanguage.HINDI -> "${result.totalPoruthamsMatched} / 10 पोरुथम (${result.totalScore} / 10.0)"
                            AppLanguage.ENGLISH -> "${result.totalPoruthamsMatched} / 10 Matched (${result.totalScore} / 10.0)"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(
                        text = result.verdictStatus.getName(currentLanguage),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            // Rajju & Sevvay highlights
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Rajju tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(if (result.rajjuMatch) "✓" else "✗", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> if (result.rajjuMatch) "ரஜ்ஜு சுபம்" else "ரஜ்ஜு தோஷம்"
                                AppLanguage.HINDI -> if (result.rajjuMatch) "रज्जु शुभ" else "रज्जु दोष"
                                AppLanguage.ENGLISH -> if (result.rajjuMatch) "Rajju Match" else "Rajju Dosha"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Sevvay tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🛡️", fontSize = 14.sp)
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> if (result.sevvayDosham.doshaSamyamStatusTa.contains("உண்டு")) "செவ்வாய் சமநிலை" else "தோஷ நிவர்த்தி தேவை"
                                AppLanguage.HINDI -> if (result.sevvayDosham.doshaSamyamStatusTa.contains("உண்டு")) "मंगल साम्य" else "दोष निवारण आवश्यक"
                                AppLanguage.ENGLISH -> if (result.sevvayDosham.doshaSamyamStatusTa.contains("உண்டு")) "Mars Balanced" else "Remedy Needed"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
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
    val iconText = when (porutham.status) {
        PoruthamStatus.UTTHAMAM -> "✓"
        PoruthamStatus.MADHYAMAM -> "≈"
        PoruthamStatus.PORUNDHADHU -> "✗"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(iconText, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "மகா பொருத்தம்"
                                    AppLanguage.HINDI -> "महा पोरुथम"
                                    AppLanguage.ENGLISH -> "Crucial"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = porutham.status.getName(currentLanguage),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Meaning of the porutham wraps to second line and finishes all words fully
            Text(
                text = porutham.getExplanation(currentLanguage),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 19.sp,
                softWrap = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🛡️", fontSize = 18.sp)
                    }
                }
                Column {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "செவ்வாய் தோஷ விளக்கம்"
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
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "👰 " + when (currentLanguage) {
                                AppLanguage.TAMIL -> "மணமகள்"
                                AppLanguage.HINDI -> "वधू"
                                AppLanguage.ENGLISH -> "Bride"
                            },
                            fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = analysis.getBrideDoshamSeverity(currentLanguage),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall
                        )
                        analysis.getBrideCancellationReason(currentLanguage)?.let { reason ->
                            Text(
                                text = reason,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Groom
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "🤵 " + when (currentLanguage) {
                                AppLanguage.TAMIL -> "மணமகன்"
                                AppLanguage.HINDI -> "वर"
                                AppLanguage.ENGLISH -> "Groom"
                            },
                            fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = analysis.getGroomDoshamSeverity(currentLanguage),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall
                        )
                        analysis.getGroomCancellationReason(currentLanguage)?.let { reason ->
                            Text(
                                text = reason,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Dosha Samyam Verdict
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "தோஷ சாம்யம் சமநிலை:"
                            AppLanguage.HINDI -> "दोष साम्य संतुलन:"
                            AppLanguage.ENGLISH -> "Dosha Samyam Balance:"
                        },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
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
