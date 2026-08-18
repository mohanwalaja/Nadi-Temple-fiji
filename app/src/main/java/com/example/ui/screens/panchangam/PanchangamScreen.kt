package com.example.ui.screens.panchangam

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.repository.AppStrings
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanchangamScreen(
    viewModel: PanchangamViewModel,
    modifier: Modifier = Modifier
) {
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
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = state.selectedLocation,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
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
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Quick 1-tap presets
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val quickPresets = listOf(
                                "🇫🇯 நாடி (Nadi +12h)" to "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)",
                                "🇮🇳 சென்னை (IST +5:30)" to "சென்னை (Chennai, India)",
                                "🌍 பிற இடங்கள்..." to ""
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
                                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
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

            // 3. Complete Drik Panchangam 10 Core Limbs Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                                text = if (lang == AppLanguage.TAMIL) "த்ரிக் பஞ்சாங்கம் (10 முக்கிய அங்கங்கள்)" else "Drik Panchangam (10 Limbs)",
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

                        Spacer(modifier = Modifier.height(14.dp))

                        // 1. Tamil Year (Samvatsara)
                        DrikAngamItem(
                            number = "1",
                            titleTa = "தமிழ் வருடம் (Samvatsara)",
                            titleEn = "Tamil Year (Samvatsara)",
                            value = panchangam.samvatsaraName,
                            subValue = "60 வருட சுழற்சி (60 Year Vedic Cycle)",
                            icon = Icons.Default.AutoAwesome,
                            color = TempleMaroon,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 2. Ayanam (Dakshinayanam / Uttarayanam)
                        DrikAngamItem(
                            number = "2",
                            titleTa = "அயனம் (Ayanam)",
                            titleEn = "Ayanam (Solar Movement)",
                            value = panchangam.ayanam,
                            subValue = if (panchangam.ayanam.contains("தக்ஷிணாயணம்")) "சூரியனின் தெற்கு நோக்கிய நகர்வு" else "சூரியனின் வடக்கு நோக்கிய நகர்வு",
                            icon = Icons.Default.Explore,
                            color = TempleGoldDark,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 3. Rithu (Season)
                        DrikAngamItem(
                            number = "3",
                            titleTa = "ருது (Rithu - பருவகாலம்)",
                            titleEn = "Rithu (Vedic Season)",
                            value = panchangam.ritu,
                            subValue = "வேத கால 6 ருதுக்கள் (6 Vedic Seasons)",
                            icon = Icons.Default.Eco,
                            color = SacredGreen,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 4. Tamil Masam in Sanskrit (Mesha / Simha Masa)
                        DrikAngamItem(
                            number = "4",
                            titleTa = "மாதம் & சமஸ்கிருத மாசம் (Solar Month)",
                            titleEn = "Solar Month (Sanskrit & Tamil Masa)",
                            value = panchangam.sanskritMonth,
                            subValue = "தேதி: ${panchangam.tamilDate} (Sidereal Solar Day)",
                            icon = Icons.Default.WbSunny,
                            color = TempleSaffron,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 5. Paksham (Shukla / Krishna)
                        DrikAngamItem(
                            number = "5",
                            titleTa = "பக்ஷம் (Paksham)",
                            titleEn = "Paksham (Lunar Fortnight)",
                            value = panchangam.paksha,
                            subValue = if (panchangam.paksha.contains("சுக்ல")) "வளர்பிறை காலம் (Waxing Moon)" else "தேய்பிறை காலம் (Waning Moon)",
                            icon = Icons.Default.NightlightRound,
                            color = TempleMaroonLight,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 6. Tithi
                        DrikAngamItem(
                            number = "6",
                            titleTa = "திதி (Tithi)",
                            titleEn = "Tithi (Lunar Day)",
                            value = panchangam.tithi,
                            subValue = "${panchangam.tithiEndTime} வரை (Next: ${panchangam.nextTithi ?: "-"})",
                            icon = Icons.Default.BrightnessMedium,
                            color = TempleMaroon,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 7. Vasaram (Vedic Weekday)
                        DrikAngamItem(
                            number = "7",
                            titleTa = "வாசரம் (Vasaram / கிழமை)",
                            titleEn = "Vasaram (Vedic Weekday / Vara)",
                            value = panchangam.vasaram,
                            subValue = panchangam.dayOfWeek,
                            icon = Icons.Default.CalendarToday,
                            color = TempleGoldDark,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 8. Nakshatram
                        DrikAngamItem(
                            number = "8",
                            titleTa = "நட்சத்திரம் (Nakshatram)",
                            titleEn = "Nakshatram (Asterism)",
                            value = "${panchangam.nakshatram} (பாதம் ${panchangam.pada})",
                            subValue = "${panchangam.nakshatramEndTime} வரை (Next: ${panchangam.nextNakshatram ?: "-"})",
                            icon = Icons.Default.Star,
                            color = TempleSaffron,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 9. Yogam (Nithya Yoga & Dina Yoga)
                        DrikAngamItem(
                            number = "9",
                            titleTa = "யோகம் (Yogam)",
                            titleEn = "Yogam (Nithya & Dina Yoga)",
                            value = "${panchangam.yogam}\n${panchangam.dinaYogam}",
                            subValue = "${panchangam.yogamEndTime} வரை",
                            icon = Icons.Default.SelfImprovement,
                            color = SacredGreen,
                            lang = lang
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 10. Karanam
                        DrikAngamItem(
                            number = "10",
                            titleTa = "கரணம் (Karanam)",
                            titleEn = "Karanam (Half-Tithi)",
                            value = panchangam.karanam,
                            subValue = "${panchangam.karanamEndTime} வரை (Next: ${panchangam.nextKaranam ?: "-"})",
                            icon = Icons.Default.HourglassBottom,
                            color = TempleMaroonLight,
                            lang = lang
                        )
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
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = {
                Text(
                    text = if (lang == AppLanguage.TAMIL) "பஞ்சாங்க இடத்தை தேர்ந்தெடுக்கவும்" else "Select Location",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    state.availableLocations.forEach { loc ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setLocation(loc)
                                    showLocationDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = loc == state.selectedLocation,
                                onClick = {
                                    viewModel.setLocation(loc)
                                    showLocationDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = loc, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text(text = if (lang == AppLanguage.TAMIL) "முடிந்தது" else "Done")
                }
            }
        )
    }
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
