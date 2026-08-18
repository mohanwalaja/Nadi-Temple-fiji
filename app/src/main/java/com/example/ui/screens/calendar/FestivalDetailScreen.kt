package com.example.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.repository.FestivalRepository
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FestivalDetailScreen(
    festivalId: String,
    festivalRepository: FestivalRepository,
    currentLanguage: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val festival = remember(festivalId) { festivalRepository.getFestivalById(festivalId) }
    val currentDate = remember { LocalDate.now() }
    val nextDate = remember(festival) {
        festival?.calculateDateForYear(currentDate.year).let { calculated ->
            if (calculated != null && calculated.isBefore(currentDate)) {
                festival?.calculateDateForYear(currentDate.year + 1)
            } else {
                calculated
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = festival?.getName(currentLanguage) ?: "Festival Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("fest_detail_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TempleMaroon)
            )
        }
    ) { innerPadding ->
        if (festival == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Festival not found", color = MaterialTheme.colorScheme.onSurface)
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .testTag("festival_detail_container"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Badge
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = TempleMaroon),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = festival.iconEmoji, fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = festival.getName(currentLanguage),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${festival.tamilMonth.name(currentLanguage)} • ${festival.getDeity(currentLanguage)}",
                                fontSize = 13.sp,
                                color = TempleGoldLight
                            )

                            if (nextDate != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    color = TempleGold,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    val dateStr = nextDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"))
                                    Text(
                                        text = if (currentLanguage == AppLanguage.TAMIL) "அடுத்த திருநாள்: $dateStr" else "Next Observance: $dateStr",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleMaroonDark,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Significance Section
                item {
                    DetailSectionCard(
                        title = if (currentLanguage == AppLanguage.TAMIL) "புராணச் சிறப்பும் முக்கியத்துவமும்" else "Significance & Mythological Lore",
                        content = festival.getSignificance(currentLanguage),
                        icon = Icons.Default.AutoStories,
                        iconColor = TempleGoldDark
                    )
                }

                // Rituals Section
                item {
                    DetailSectionCard(
                        title = if (currentLanguage == AppLanguage.TAMIL) "வழிபாட்டு முறைகளும் நைவேத்தியமும்" else "Rituals, Offerings & Pooja Guidelines",
                        content = festival.getRituals(currentLanguage),
                        icon = Icons.Default.SelfImprovement,
                        iconColor = TempleSaffron
                    )
                }

                // Traditional Astronomical Rule Section
                item {
                    DetailSectionCard(
                        title = if (currentLanguage == AppLanguage.TAMIL) "பாரம்பரிய பஞ்சாங்க நிர்ணய விதி" else "Traditional Calculation & Ephemeris Rule",
                        content = festival.getTraditionalRule(currentLanguage),
                        icon = Icons.Default.Balance,
                        iconColor = TempleMaroon
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = iconColor.copy(alpha = 0.12f),
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TempleMaroon
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            )
        }
    }
}
