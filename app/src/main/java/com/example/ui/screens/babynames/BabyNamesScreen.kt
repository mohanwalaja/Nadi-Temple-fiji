package com.example.ui.screens.babynames

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.NakshatraBabyLetters
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyNamesScreen(
    viewModel: BabyNamesViewModel,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
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
                            Text("🪔", fontSize = 22.sp)
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "27 நட்சத்திர சுப பெயர் தொடக்க எழுத்துக்கள்"
                                AppLanguage.HINDI -> "27 नक्षत्र शुभ नामकरण अक्षर"
                                AppLanguage.ENGLISH -> "27 Nakshatra Auspicious Initial Letters"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TempleMaroon
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "பிறந்த நட்சத்திரம் மற்றும் பாதத்திற்குரிய சுப ஆரம்ப அட்சரங்கள் (108 பாதங்கள்)"
                                AppLanguage.HINDI -> "जन्म नक्षत्र एवं पाद (चरण) अनुसार पारंपरिक शुभ नामकरण अक्षर"
                                AppLanguage.ENGLISH -> "Traditional Vedic starting syllables for all 27 stars & 108 Padas"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Search Bar for Instant Lookup
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

        // View Mode Tab (Focused Spotlight vs All 27 Directory)
        item {
            TabRow(
                selectedTabIndex = uiState.viewMode,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = TempleMaroon,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = uiState.viewMode == 0,
                    onClick = { viewModel.setViewMode(0) },
                    text = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "நட்சத்திர வாரியாக (Spotlight)"
                                AppLanguage.HINDI -> "नक्षत्रवार (Spotlight)"
                                AppLanguage.ENGLISH -> "By Nakshatram"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = uiState.viewMode == 1,
                    onClick = { viewModel.setViewMode(1) },
                    text = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "அனைத்து 27 நட்சத்திர பட்டியல்"
                                AppLanguage.HINDI -> "सभी 27 नक्षत्र सूची"
                                AppLanguage.ENGLISH -> "All 27 Star List"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        if (uiState.viewMode == 0) {
            // Nakshatram Quick Horizontal Carousel
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

            // Spotlight Card of the Selected Nakshatram
            uiState.currentNakshatraLetters?.let { star ->
                item {
                    NakshatraSpotlightCard(
                        star = star,
                        currentLanguage = currentLanguage
                    )
                }
            }
        } else {
            // All 27 Nakshatrams Comprehensive List
            items(uiState.searchResults) { star ->
                NakshatraSummaryCard(
                    star = star,
                    currentLanguage = currentLanguage
                )
            }
        }

        // Traditional Rules & Guidelines Card
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
