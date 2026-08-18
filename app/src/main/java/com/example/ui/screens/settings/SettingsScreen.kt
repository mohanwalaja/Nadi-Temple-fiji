package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prefs by viewModel.preferences.collectAsState()
    val lang = prefs.language

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = if (lang == AppLanguage.TAMIL) "அமைப்புகள் (Settings)" else "Settings",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_btn")) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary
                    )
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("settings_container"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Language Preference Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (lang) {
                                    AppLanguage.TAMIL -> "மொழி தேர்வு (Language)"
                                    AppLanguage.HINDI -> "भाषा का चयन (Language)"
                                    AppLanguage.ENGLISH -> "App Language"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val isTamil = lang == AppLanguage.TAMIL
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setLanguage(AppLanguage.TAMIL) }
                                    .testTag("setting_lang_tamil"),
                                color = if (isTamil) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isTamil) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                )
                            ) {
                                Text(
                                    text = "தமிழ்\n(Tamil)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTamil) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                                )
                            }

                            val isHindi = lang == AppLanguage.HINDI
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setLanguage(AppLanguage.HINDI) }
                                    .testTag("setting_lang_hindi"),
                                color = if (isHindi) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isHindi) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                )
                            ) {
                                Text(
                                    text = "हिन्दी\n(Hindi)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isHindi) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                                )
                            }

                            val isEng = lang == AppLanguage.ENGLISH
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setLanguage(AppLanguage.ENGLISH) }
                                    .testTag("setting_lang_english"),
                                color = if (isEng) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isEng) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                )
                            ) {
                                Text(
                                    text = "English\n(ஆங்கிலம்)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isEng) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. UI Color Palette & Theme Chooser
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (lang) {
                                    AppLanguage.TAMIL -> "வர்ண அலங்காரம் (Color Theme)"
                                    AppLanguage.HINDI -> "रंग थीम (Color Theme)"
                                    AppLanguage.ENGLISH -> "App Color Theme"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (lang) {
                                AppLanguage.TAMIL -> "செயலியின் வண்ணத்தை உங்களுக்கு விருப்பமான முறையில் மாற்றிக் கொள்ளலாம்"
                                AppLanguage.HINDI -> "अपनी पसंद के अनुसार ऐप का रंग बदलें"
                                AppLanguage.ENGLISH -> "Choose your preferred devotional color palette"
                            },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Theme Preset Grid
                        KovilThemePreset.entries.forEach { preset ->
                            val isSelected = prefs.themePreset == preset.code
                            val themeName = when (lang) {
                                AppLanguage.TAMIL -> preset.nameTa
                                AppLanguage.HINDI -> preset.nameHi
                                AppLanguage.ENGLISH -> preset.nameEn
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.setThemePreset(preset.code) }
                                    .testTag("theme_preset_${preset.code.lowercase()}"),
                                color = if (isSelected) preset.primaryColor.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) preset.primaryColor else MaterialTheme.colorScheme.outlineVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Color Swatch Circles
                                    Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                                        Surface(
                                            shape = androidx.compose.foundation.shape.CircleShape,
                                            color = preset.primaryColor,
                                            modifier = Modifier.size(24.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White)
                                        ) {}
                                        Surface(
                                            shape = androidx.compose.foundation.shape.CircleShape,
                                            color = preset.secondaryColor,
                                            modifier = Modifier.size(24.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White)
                                        ) {}
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = themeName,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) preset.primaryColor else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = preset.primaryColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Dark Mode Toggle
                        SettingToggleRow(
                            title = if (lang == AppLanguage.TAMIL) "இரவு முறை (Dark Mode)" else if (lang == AppLanguage.HINDI) "डार्क मोड (Dark Mode)" else "Dark Mode",
                            subtitle = if (lang == AppLanguage.TAMIL) "கண்களுக்கு இதமான இருண்ட தோற்றம்" else if (lang == AppLanguage.HINDI) "आँखों के लिए आरामदायक डार्क थीम" else "Eye-friendly dark atmosphere",
                            isChecked = prefs.isDarkMode && !prefs.useSystemTheme,
                            onCheckedChange = { isDark ->
                                viewModel.setDarkMode(enabled = isDark, useSystem = false)
                            },
                            testTag = "toggle_dark_mode"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // System Theme Toggle
                        SettingToggleRow(
                            title = if (lang == AppLanguage.TAMIL) "கைபேசி முறைப்படி (System Default)" else if (lang == AppLanguage.HINDI) "सिस्टम के अनुसार (System Default)" else "Follow System Theme",
                            subtitle = if (lang == AppLanguage.TAMIL) "போனின் அமைப்பைப் பின்பற்றும்" else if (lang == AppLanguage.HINDI) "डिवाइस की थीम का पालन करें" else "Auto match phone settings",
                            isChecked = prefs.useSystemTheme,
                            onCheckedChange = { useSys ->
                                viewModel.setDarkMode(enabled = prefs.isDarkMode, useSystem = useSys)
                            },
                            testTag = "toggle_system_theme"
                        )
                    }
                }
            }

            // Location & Timezone Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (lang) {
                                    AppLanguage.TAMIL -> "இருப்பிடம் மற்றும் நேரம் (Location & Time)"
                                    AppLanguage.HINDI -> "स्थान और समय क्षेत्र (Location & Time)"
                                    AppLanguage.ENGLISH -> "Location & Time Zone"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TempleMaroon
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = when (lang) {
                                AppLanguage.TAMIL -> "தற்போது தேர்வு செய்யப்பட்ட இடம்: ${prefs.selectedLocation}\nநேர மண்டலம்: UTC+12:00 (Fiji Time - நாடி, பிஜி)"
                                AppLanguage.HINDI -> "वर्तमान चयनित स्थान: ${prefs.selectedLocation}\nसमय क्षेत्र: UTC+12:00 (फ़िजी समय - नादी, फ़िजी)"
                                AppLanguage.ENGLISH -> "Current Location: ${prefs.selectedLocation}\nTime Zone: UTC+12:00 (Fiji Time - Nadi, Fiji)"
                            },
                            fontSize = 13.sp,
                            color = TempleMaroonDark,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val locations = listOf(
                            "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)",
                            "சுவா, பிஜி (Suva, Fiji Islands)",
                            "சென்னை (Chennai, India)",
                            "சிங்கப்பூர் (Singapore)",
                            "சிட்னி (Sydney, Australia)"
                        )

                        locations.forEach { loc ->
                            val isSelected = prefs.selectedLocation == loc
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable { viewModel.setLocation(loc) },
                                color = if (isSelected) TempleMaroon.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.setLocation(loc) },
                                        colors = RadioButtonDefaults.colors(selectedColor = TempleMaroon)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = loc,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TempleMaroon else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Notifications Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (lang == AppLanguage.TAMIL) "நினைவூட்டல் அமைப்புகள் (Notifications)" else "Notifications",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TempleMaroon
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        SettingToggleRow(
                            title = if (lang == AppLanguage.TAMIL) "இந்துப் பண்டிகைகள் அறிவிப்பு" else "Hindu Festival Alerts",
                            subtitle = if (lang == AppLanguage.TAMIL) "விசேஷ தினங்களுக்கு முந்தைய நாள் நினைவூட்டல்" else "Reminders before major festivals",
                            isChecked = prefs.festivalNotificationEnabled,
                            onCheckedChange = { viewModel.setFestivalNotification(it) },
                            testTag = "toggle_notif_festival"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        SettingToggleRow(
                            title = if (lang == AppLanguage.TAMIL) "திருக்கோயில் விசேஷங்கள்" else "Temple Events & Poojas",
                            subtitle = if (lang == AppLanguage.TAMIL) "மகா அபிஷேகங்கள், பிரதோஷம், சஷ்டி" else "Special abhishekams and utsavam alerts",
                            isChecked = prefs.templeEventNotificationEnabled,
                            onCheckedChange = { viewModel.setTempleEventNotification(it) },
                            testTag = "toggle_notif_temple"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        SettingToggleRow(
                            title = if (lang == AppLanguage.TAMIL) "தினசரி பஞ்சாங்கம்" else "Daily Panchangam Morning Alert",
                            subtitle = if (lang == AppLanguage.TAMIL) "காலை 6:00 மணிக்கு திதி, நட்சத்திர குறிப்பு" else "Daily morning tithi and nakshatra",
                            isChecked = prefs.panchangamNotificationEnabled,
                            onCheckedChange = { viewModel.setPanchangamNotification(it) },
                            testTag = "toggle_notif_panchangam"
                        )
                    }
                }
            }

            // 3. About Kovil & App Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (lang == AppLanguage.TAMIL) "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில்" else "Sri Siva Subramaniya Swami Kovil",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (lang == AppLanguage.TAMIL)
                                "பக்தி மணம் கமழும் தமிழ் நாட்காட்டி, பஞ்சாங்கம், ஜோதிடம் மற்றும் திருக்கோயில் சேவைகளுக்கான அதிகாரப்பூர்வ செயலி.\n\nபதிப்பு: 1.0.0 (Native Android with Jetpack Compose & Room)"
                            else
                                "Official devotional Tamil Calendar, Ephemeris Panchangam, Jyotisha and Temple companion application.\n\nVersion: 1.0.0 (Native Android with Jetpack Compose & Room)",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = TempleMaroonDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TempleMaroon,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
