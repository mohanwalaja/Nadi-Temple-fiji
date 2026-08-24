package com.example.ui.screens.babynames

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
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
import com.example.data.model.AppLanguage
import com.example.data.model.BabyNamingBirthResult
import com.example.data.model.NakshatraBabyLetters
import com.example.ui.theme.*
import com.example.util.BabyNamePdfExporter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyNamesScreen(
    viewModel: BabyNamesViewModel,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val allNakshatras = viewModel.allNakshatraLetters

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("baby_names_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header Auspicious Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = TempleGold.copy(alpha = 0.12f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(TempleGold)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = TempleMaroon.copy(alpha = 0.15f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("👶", fontSize = 22.sp)
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "குழந்தை பெயர் எழுத்துக்கள் (தமிழ் பஞ்சாங்கம்)"
                                AppLanguage.HINDI -> "शिशु नामकरण शुभ अक्षर (वैदिक पंचांग)"
                                AppLanguage.ENGLISH -> "Baby Name Letters (Vedic Panchangam)"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TempleMaroon
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "பிறந்த தேதி, நேரம், இடத்தைக் கொண்டு நட்சத்திரம், ராசி & சுப தொடக்க எழுத்துக்கள்"
                                AppLanguage.HINDI -> "जन्म तिथि, समय एवं स्थान अनुसार नक्षत्र, राशि एवं शुभ नामकरण अक्षर"
                                AppLanguage.ENGLISH -> "Find auspicious initial letters by DOB, Time & Place with Rasi & Nakshatram"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Tab Navigation
        item {
            TabRow(
                selectedTabIndex = uiState.tabIndex,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = TempleMaroon,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = uiState.tabIndex == 0,
                    onClick = { viewModel.setTabIndex(0) },
                    text = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "பிறந்த விபரம் வழி"
                                AppLanguage.HINDI -> "जन्म विवरण से"
                                AppLanguage.ENGLISH -> "By Birth Details"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    },
                    icon = { Icon(Icons.Filled.ChildCare, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = uiState.tabIndex == 1,
                    onClick = { viewModel.setTabIndex(1) },
                    text = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "நட்சத்திர விளக்கம்"
                                AppLanguage.HINDI -> "नक्षत्रवार"
                                AppLanguage.ENGLISH -> "By Nakshatram"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    },
                    icon = { Icon(Icons.Filled.Stars, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = uiState.tabIndex == 2,
                    onClick = { viewModel.setTabIndex(2) },
                    text = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "27 அட்டவணை"
                                AppLanguage.HINDI -> "27 सूची"
                                AppLanguage.ENGLISH -> "All 27 Stars"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    },
                    icon = { Icon(Icons.Filled.ListAlt, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }
        }

        // Tab 0: Find Baby Name Letters with Date of Birth, Time, Place, Nakshatram & Rasi
        if (uiState.tabIndex == 0) {
            item {
                BirthDetailsInputCard(
                    uiState = uiState,
                    currentLanguage = currentLanguage,
                    onNameChange = { viewModel.updateInputBabyName(it) },
                    onGenderChange = { viewModel.updateInputGender(it) },
                    onDobChange = { viewModel.updateInputDob(it) },
                    onTobChange = { viewModel.updateInputTob(it) },
                    onPlaceChange = { viewModel.updateInputPlace(it) },
                    onCalculate = { viewModel.calculateByBirthDetails() }
                )
            }

            uiState.birthResult?.let { result ->
                item {
                    BabyNamingResultCard(
                        result = result,
                        currentLanguage = currentLanguage,
                        onExportPdf = {
                            val file = viewModel.exportPdfCertificate(context, currentLanguage)
                            if (file != null) {
                                Toast.makeText(context, "PDF சான்றிதழ் தயாராகிவிட்டது!", Toast.LENGTH_SHORT).show()
                                BabyNamePdfExporter.shareBabyNamingPdf(context, file, result.babyName)
                            } else {
                                Toast.makeText(context, "PDF உருவாக்குவதில் பிழை ஏற்பட்டது", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        } else if (uiState.tabIndex == 1) {
            // Tab 1: Single Nakshatra Spotlight Browser
            item {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "நட்சத்திரம் அல்லது எழுத்தால் தேட... (எ.கா: அஸ்வினி, சு, Chu)"
                                AppLanguage.HINDI -> "नक्षत्र या अक्षर से खोजें... (उदा: अश्विनी, चु, Chu)"
                                AppLanguage.ENGLISH -> "Search star or letter... (e.g. Ashwini, Chu, La)"
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = TempleMaroon)
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "நட்சத்திரத்தைத் தேர்வு செய்யவும்:"
                            AppLanguage.HINDI -> "नक्षत्र का चयन करें:"
                            AppLanguage.ENGLISH -> "Select Nakshatram:"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(allNakshatras) { star ->
                            val isSelected = star.nakshatraIndex == uiState.selectedNakshatraIndex
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectNakshatra(star.nakshatraIndex) },
                                label = {
                                    Text(
                                        text = "${star.nakshatraIndex}. ${star.getName(currentLanguage)}",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TempleMaroon,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            uiState.currentNakshatraLetters?.let { star ->
                item {
                    NakshatraSpotlightCard(
                        star = star,
                        currentLanguage = currentLanguage
                    )
                }
            }
        } else {
            // Tab 2: All 27 Nakshatras Directory
            item {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "27 நட்சத்திரங்களில் தேட..."
                                AppLanguage.HINDI -> "27 नक्षत्रों में खोजें..."
                                AppLanguage.ENGLISH -> "Search in all 27 stars..."
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = TempleMaroon)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            items(uiState.searchResults) { star ->
                NakshatraSummaryCard(
                    star = star,
                    currentLanguage = currentLanguage
                )
            }
        }

        // Traditional Guidelines Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.MenuBook, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(20.dp))
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "பெயர் சூட்டும் ஜோதிட சாஸ்திர விதிகள்"
                                AppLanguage.HINDI -> "नामकरण संस्कार ज्योतिषीय नियम"
                                AppLanguage.ENGLISH -> "Vedic Naming Guidelines"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TempleMaroon
                        )
                    }

                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "1. குழந்தையின் ஜென்ம நட்சத்திர பாதத்திற்குரிய முதல் சுப அட்சரத்தைக் கொண்டு பெயர் தொடங்குவது ஆயுள், ஆரோக்கியம் மற்றும் நற்புகழை அருளும்.\n2. ஒரு நட்சத்திரத்திற்கு நான்கு பாதங்கள் உள்ளன. பிறந்த நேரத்தைக்கொண்டு துல்லிய பாதத்தை அறிந்து கொள்ளவும்.\n3. குலதெய்வம் அல்லது முன்னோர்களின் பெயர்களை இணைக்கும்போதும் ஆரம்ப எழுத்து சுப பாத எழுத்தாக இருப்பது உத்தமம்."
                            AppLanguage.HINDI -> "1. जन्म नक्षत्र और पाद (चरण) के प्रथम शुभ अक्षर से नामकरण करने से दीर्घायु, उत्तम स्वास्थ्य एवं कीर्ति की प्राप्ति होती है।\n2. प्रत्येक नक्षत्र में 4 चरण होते हैं, जन्म समय से सही चरण ज्ञात करें।"
                            AppLanguage.ENGLISH -> "1. Starting the child's name with the auspicious letter of the birth Nakshatra Pada brings longevity, health, and prosperity.\n2. Each Nakshatram has 4 Padas with unique sound vibrations (Aksharas)."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BirthDetailsInputCard(
    uiState: BabyNamesUiState,
    currentLanguage: AppLanguage,
    onNameChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onDobChange: (LocalDate) -> Unit,
    onTobChange: (LocalTime) -> Unit,
    onPlaceChange: (String) -> Unit,
    onCalculate: () -> Unit
) {
    val context = LocalContext.current
    val dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
    val timeFmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.EditCalendar, contentDescription = null, tint = TempleMaroon)
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.TAMIL -> "குழந்தையின் பிறந்த விபரங்கள் உள்ளிடுக:"
                        AppLanguage.HINDI -> "शिशु जन्म विवरण दर्ज करें:"
                        AppLanguage.ENGLISH -> "Enter Baby Birth Details:"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TempleMaroon
                )
            }

            // Baby Name Field
            OutlinedTextField(
                value = uiState.inputBabyName,
                onValueChange = onNameChange,
                label = {
                    Text(when (currentLanguage) {
                        AppLanguage.TAMIL -> "குழந்தையின் பெயர் (விரும்பினால்)"
                        AppLanguage.HINDI -> "शिशु का नाम (वैकल्पिक)"
                        AppLanguage.ENGLISH -> "Baby Name (Optional)"
                    })
                },
                placeholder = { Text("செல்வன் / செல்வி") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = TempleMaroon) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Gender Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.TAMIL -> "பாலினம்:"
                        AppLanguage.HINDI -> "लिंग:"
                        AppLanguage.ENGLISH -> "Gender:"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                FilterChip(
                    selected = uiState.inputGender == "M",
                    onClick = { onGenderChange("M") },
                    label = { Text("👦 " + when (currentLanguage) { AppLanguage.TAMIL -> "ஆண்"; AppLanguage.HINDI -> "बालक"; AppLanguage.ENGLISH -> "Boy" }) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TempleMaroon, selectedLabelColor = Color.White)
                )

                FilterChip(
                    selected = uiState.inputGender == "F",
                    onClick = { onGenderChange("F") },
                    label = { Text("👧 " + when (currentLanguage) { AppLanguage.TAMIL -> "பெண்"; AppLanguage.HINDI -> "बालिका"; AppLanguage.ENGLISH -> "Girl" }) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TempleMaroon, selectedLabelColor = Color.White)
                )
            }

            // Date & Time Selectors Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // DOB Picker
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val d = uiState.inputDob
                            DatePickerDialog(context, { _, year, month, dayOfMonth ->
                                onDobChange(LocalDate.of(year, month + 1, dayOfMonth))
                            }, d.year, d.monthValue - 1, d.dayOfMonth).show()
                        },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "பிறந்த தேதி (DOB)"
                                AppLanguage.HINDI -> "जन्म तिथि"
                                AppLanguage.ENGLISH -> "Date of Birth"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(16.dp))
                            Text(uiState.inputDob.format(dateFmt), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                // TOB Picker
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val t = uiState.inputTob
                            TimePickerDialog(context, { _, hourOfDay, minute ->
                                onTobChange(LocalTime.of(hourOfDay, minute))
                            }, t.hour, t.minute, false).show()
                        },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "பிறந்த நேரம் (TOB)"
                                AppLanguage.HINDI -> "जन्म समय"
                                AppLanguage.ENGLISH -> "Time of Birth"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(16.dp))
                            Text(uiState.inputTob.format(timeFmt), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Birth Place Field
            OutlinedTextField(
                value = uiState.inputPlace,
                onValueChange = onPlaceChange,
                label = {
                    Text(when (currentLanguage) {
                        AppLanguage.TAMIL -> "பிறந்த இடம் (Place of Birth)"
                        AppLanguage.HINDI -> "जन्म स्थान (Birth Place)"
                        AppLanguage.ENGLISH -> "Place of Birth"
                    })
                },
                leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TempleMaroon) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Calculate Button
            Button(
                onClick = onCalculate,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("calculate_baby_letters_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TempleMaroon)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = TempleGold)
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "சுப பெயர் எழுத்துக்களைக் கணிக்கவும்"
                            AppLanguage.HINDI -> "शुभ नामकरण अक्षर ज्ञात करें"
                            AppLanguage.ENGLISH -> "Calculate Auspicious Baby Letters"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun BabyNamingResultCard(
    result: BabyNamingBirthResult,
    currentLanguage: AppLanguage,
    onExportPdf: () -> Unit
) {
    val star = result.nakshatraLetters
    val currentPada = result.janmaPada

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("baby_naming_result_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Result Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "⭐ ${star.getName(currentLanguage)} (${result.janmaPada}-ஆம் பாதம்)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TempleMaroon
                    )
                    Text(
                        text = "🌙 ${result.chandraRasi.getName(currentLanguage)}  •  லக்னம்: ${result.lagnaRasi.getName(currentLanguage)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TempleGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = star.getLettersSummary(currentLanguage),
                        fontWeight = FontWeight.Bold,
                        color = TempleMaroon,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            HorizontalDivider()

            // PRIMARY LETTER SPOTLIGHT MEDALLION
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TempleGold.copy(alpha = 0.15f)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(TempleGold))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "✨ ஜென்ம பாதத்திற்குரிய முதன்மை சுப ஆரம்ப அட்சரம் ✨"
                            AppLanguage.HINDI -> "✨ जन्म चरण का मुख्य शुभ नामकरण अक्षर ✨"
                            AppLanguage.ENGLISH -> "✨ PRIMARY INITIAL LETTER FOR BIRTH PADA ✨"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TempleMaroon
                    )

                    Surface(
                        shape = CircleShape,
                        color = TempleMaroon,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = result.primaryPadaInfo.letterTa,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = TempleGold
                            )
                        }
                    }

                    Text(
                        text = "${result.primaryPadaInfo.letterTa} (${result.primaryPadaInfo.letterEn})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TempleMaroon
                    )

                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "தமிழ் பஞ்சாங்க விதிப்படி ${star.getName(currentLanguage)} $currentPada-ஆம் பாதத்தில் பிறந்த குழந்தைக்கு இப்பெயர் ஒலி அட்சரம் சர்வ மங்கலங்களையும் தரும்."
                            AppLanguage.HINDI -> "वैदिक पंचांग अनुसार $currentPada चरण में जन्मे शिशु के लिए यह शुभ ध्वनि कंपन सर्वोत्तम है।"
                            AppLanguage.ENGLISH -> "Naming the child starting with this sacred syllable vibration brings divine protection & success."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }

            // 4 Padas Starting Letters Grid
            Text(
                text = when (currentLanguage) {
                    AppLanguage.TAMIL -> "நட்சத்திரத்தின் 4 பாத சுப ஆரம்ப எழுத்துக்கள்:"
                    AppLanguage.HINDI -> "नक्षत्र के सभी 4 चरणों के शुभ अक्षर:"
                    AppLanguage.ENGLISH -> "Starting Letters for all 4 Padas:"
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                star.padas.forEach { pada ->
                    val isCurrent = (pada.padaNumber == result.janmaPada)
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) TempleGold.copy(alpha = 0.22f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isCurrent) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(TempleMaroon)) else null
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isCurrent) TempleMaroon else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                modifier = Modifier.size(20.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${pada.padaNumber}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> pada.letterTa
                                    AppLanguage.HINDI -> pada.letterHi
                                    AppLanguage.ENGLISH -> pada.letterEn
                                },
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TempleMaroon
                            )

                            Text(
                                text = pada.letterEn,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> pada.rasiTa
                                    AppLanguage.HINDI -> pada.rasiHi
                                    AppLanguage.ENGLISH -> pada.rasiEn
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TempleGoldDark
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // EXPORT CERTIFICATE PDF BUTTON
            Button(
                onClick = onExportPdf,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("export_baby_naming_pdf_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TempleMaroon)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.PictureAsPdf, contentDescription = null, tint = TempleGold)
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "நாமகரண சான்றிதழ் PDF பதிவிறக்கம் / பகிர்வு"
                            AppLanguage.HINDI -> "नामकरण प्रमाण पत्र PDF डाउनलोड / साझा करें"
                            AppLanguage.ENGLISH -> "Export & Share Naming Certificate PDF"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun NakshatraSpotlightCard(
    star: NakshatraBabyLetters,
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
            // Title Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${star.nakshatraIndex}. ${star.getName(currentLanguage)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TempleMaroon
                    )
                    if (currentLanguage == AppLanguage.TAMIL) {
                        Text(
                            text = "(${star.nakshatraNameEn})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TempleGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = star.getLettersSummary(currentLanguage),
                        fontWeight = FontWeight.Bold,
                        color = TempleMaroon,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            HorizontalDivider()

            // 4 Padas Visual Display Grid
            Text(
                text = when (currentLanguage) {
                    AppLanguage.TAMIL -> "4 பாதங்களின் சுப ஆரம்ப எழுத்துக்கள் (4 Padas):"
                    AppLanguage.HINDI -> "4 चरणों के शुभ अक्षर:"
                    AppLanguage.ENGLISH -> "Auspicious Starting Letters for 4 Padas:"
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                star.padas.forEach { pada ->
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = TempleMaroon,
                                contentColor = Color.White,
                                modifier = Modifier.size(22.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("${pada.padaNumber}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> pada.letterTa
                                    AppLanguage.HINDI -> pada.letterHi
                                    AppLanguage.ENGLISH -> pada.letterEn
                                },
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TempleMaroon
                            )
                            if (currentLanguage != AppLanguage.ENGLISH) {
                                Text(
                                    text = pada.letterEn,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = TempleGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.TAMIL -> pada.rasiTa
                                        AppLanguage.HINDI -> pada.rasiHi
                                        AppLanguage.ENGLISH -> pada.rasiEn
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TempleMaroon,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // Astrological Attributes Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Lord & Deity
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            when (currentLanguage) {
                                AppLanguage.TAMIL -> "அதிபதி & தேவதை"
                                AppLanguage.HINDI -> "स्वामी एवं देवता"
                                AppLanguage.ENGLISH -> "Lord & Deity"
                            },
                            fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text("${star.getLord(currentLanguage)} • ${star.getDeity(currentLanguage)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Ganam & Yoni
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            when (currentLanguage) {
                                AppLanguage.TAMIL -> "கணம் & யோனி"
                                AppLanguage.HINDI -> "गण एवं योनि"
                                AppLanguage.ENGLISH -> "Gana & Yoni"
                            },
                            fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text("${star.getGana(currentLanguage)} • ${star.getYoni(currentLanguage)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun NakshatraSummaryCard(
    star: NakshatraBabyLetters,
    currentLanguage: AppLanguage
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        color = TempleMaroon.copy(alpha = 0.12f),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("${star.nakshatraIndex}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TempleMaroon)
                        }
                    }
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "${star.nakshatraIndex}. ${star.nakshatraNameTa} (${star.nakshatraNameEn})"
                            AppLanguage.HINDI -> "${star.nakshatraIndex}. ${star.nakshatraNameHi}"
                            AppLanguage.ENGLISH -> "${star.nakshatraIndex}. ${star.nakshatraNameEn}"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = when (currentLanguage) {
                        AppLanguage.TAMIL -> "அதிபதி: ${star.getLord(currentLanguage)}"
                        AppLanguage.HINDI -> "स्वामी: ${star.getLord(currentLanguage)}"
                        AppLanguage.ENGLISH -> "Lord: ${star.getLord(currentLanguage)}"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 4 Letters Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                star.padas.forEach { pada ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                when (currentLanguage) {
                                    AppLanguage.TAMIL -> "பாதம் ${pada.padaNumber}"
                                    AppLanguage.HINDI -> "पाद ${pada.padaNumber}"
                                    AppLanguage.ENGLISH -> "Pada ${pada.padaNumber}"
                                },
                                fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                when (currentLanguage) {
                                    AppLanguage.TAMIL -> pada.letterTa
                                    AppLanguage.HINDI -> pada.letterHi
                                    AppLanguage.ENGLISH -> pada.letterEn
                                },
                                fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TempleMaroon
                            )
                            Text(pada.letterEn, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                when (currentLanguage) {
                                    AppLanguage.TAMIL -> pada.rasiTa
                                    AppLanguage.HINDI -> pada.rasiHi
                                    AppLanguage.ENGLISH -> pada.rasiEn
                                },
                                fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = TempleGoldDark
                            )
                        }
                    }
                }
            }
        }
    }
}
