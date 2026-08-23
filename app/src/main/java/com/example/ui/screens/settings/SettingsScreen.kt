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
import com.example.data.repository.AppStrings
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
                            text = when (lang) {
                                AppLanguage.TAMIL -> "அமைப்புகள்"
                                AppLanguage.HINDI -> "सेटिंग्स"
                                AppLanguage.ENGLISH -> "Settings"
                            },
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
                                    AppLanguage.HINDI -> "भाषा का चयन"
                                    AppLanguage.ENGLISH -> "Language Selection"
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
                                    text = "English\n(Default)",
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

            // 2. Display & Appearance (Day Mode / Dark Mode)
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
                            Icon(imageVector = Icons.Default.Brightness4, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (lang) {
                                    AppLanguage.TAMIL -> "தோற்ற அமைப்பு (Display & Theme)"
                                    AppLanguage.HINDI -> "थीम और दृश्य"
                                    AppLanguage.ENGLISH -> "Display & Appearance"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dark Mode Toggle
                        SettingToggleRow(
                            title = when (lang) {
                                AppLanguage.TAMIL -> "இரவு முறை (Dark Mode)"
                                AppLanguage.HINDI -> "डार्क मोड (Dark Theme)"
                                AppLanguage.ENGLISH -> "Dark Mode"
                            },
                            subtitle = when (lang) {
                                AppLanguage.TAMIL -> "கண்களுக்கு இதமான இருண்ட தோற்றம்"
                                AppLanguage.HINDI -> "आँखों के लिए आरामदायक डार्क थीम"
                                AppLanguage.ENGLISH -> "Comfortable dark theme for low light"
                            },
                            isChecked = prefs.isDarkMode && !prefs.useSystemTheme,
                            onCheckedChange = { isDark ->
                                viewModel.setDarkMode(enabled = isDark, useSystem = false)
                            },
                            testTag = "toggle_dark_mode"
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(10.dp))

                        // System Theme Toggle
                        SettingToggleRow(
                            title = when (lang) {
                                AppLanguage.TAMIL -> "கைபேசி முறைப்படி (System Default)"
                                AppLanguage.HINDI -> "सिस्टम के अनुसार (System Default)"
                                AppLanguage.ENGLISH -> "Follow System Theme"
                            },
                            subtitle = when (lang) {
                                AppLanguage.TAMIL -> "போனின் அமைப்பைப் பின்பற்றும்"
                                AppLanguage.HINDI -> "डिवाइस की थीम का पालन करें"
                                AppLanguage.ENGLISH -> "Automatically match device system theme"
                            },
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
                                    AppLanguage.HINDI -> "स्थान और समय क्षेत्र"
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

                        val locations = when (lang) {
                            AppLanguage.TAMIL -> listOf(
                                "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)",
                                "சுவா, பிஜி (Suva, Fiji Islands)",
                                "சென்னை (Chennai, India)",
                                "சிங்கப்பூர் (Singapore)",
                                "சிட்னி (Sydney, Australia)"
                            )
                            AppLanguage.HINDI -> listOf(
                                "नादी, फ़िजी द्वीप समूह (Nadi, Fiji Islands)",
                                "सुवा, फ़िजी (Suva, Fiji Islands)",
                                "चेन्नई, भारत (Chennai, India)",
                                "सिंगापुर (Singapore)",
                                "सिडनी, ऑस्ट्रेलिया (Sydney, Australia)"
                            )
                            AppLanguage.ENGLISH -> listOf(
                                "Nadi, Fiji Islands",
                                "Suva, Fiji Islands",
                                "Chennai, India",
                                "Singapore",
                                "Sydney, Australia"
                            )
                        }

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
                                text = when (lang) {
                                    AppLanguage.TAMIL -> "நினைவூட்டல் அமைப்புகள் (Notifications)"
                                    AppLanguage.HINDI -> "अधिसूचना सेटिंग्स (Notifications)"
                                    AppLanguage.ENGLISH -> "Notifications"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TempleMaroon
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        SettingToggleRow(
                            title = when (lang) {
                                AppLanguage.TAMIL -> "இந்துப் பண்டிகைகள் அறிவிப்பு"
                                AppLanguage.HINDI -> "हिन्दू पर्व एवं त्योहार सूचना"
                                AppLanguage.ENGLISH -> "Hindu Festival Alerts"
                            },
                            subtitle = when (lang) {
                                AppLanguage.TAMIL -> "விசேஷ தினங்களுக்கு முந்தைய நாள் நினைவூட்டல்"
                                AppLanguage.HINDI -> "प्रमुख त्योहारों से पहले स्मरण सूचना"
                                AppLanguage.ENGLISH -> "Reminders before major festivals"
                            },
                            isChecked = prefs.festivalNotificationEnabled,
                            onCheckedChange = { viewModel.setFestivalNotification(it) },
                            testTag = "toggle_notif_festival"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        SettingToggleRow(
                            title = when (lang) {
                                AppLanguage.TAMIL -> "திருக்கோயில் விசேஷங்கள்"
                                AppLanguage.HINDI -> "मंदिर विशेष पूजा एवं उत्सव"
                                AppLanguage.ENGLISH -> "Temple Events & Poojas"
                            },
                            subtitle = when (lang) {
                                AppLanguage.TAMIL -> "மகா அபிஷேகங்கள், பிரதோஷம், சஷ்டி"
                                AppLanguage.HINDI -> "महाअभिषेक, प्रदोष, षष्ठी उत्सव सूचना"
                                AppLanguage.ENGLISH -> "Special abhishekams and utsavam alerts"
                            },
                            isChecked = prefs.templeEventNotificationEnabled,
                            onCheckedChange = { viewModel.setTempleEventNotification(it) },
                            testTag = "toggle_notif_temple"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        SettingToggleRow(
                            title = when (lang) {
                                AppLanguage.TAMIL -> "தினசரி பஞ்சாங்கம்"
                                AppLanguage.HINDI -> "दैनिक पंचांग प्रातः सूचना"
                                AppLanguage.ENGLISH -> "Daily Panchangam Morning Alert"
                            },
                            subtitle = when (lang) {
                                AppLanguage.TAMIL -> "காலை 6:00 மணிக்கு திதி, நட்சத்திர குறிப்பு"
                                AppLanguage.HINDI -> "प्रातः 6:00 बजे दैनिक तिथि एवं नक्षत्र सूचना"
                                AppLanguage.ENGLISH -> "Daily morning tithi and nakshatra"
                            },
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
                            text = AppStrings.appTitle(lang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (lang) {
                                AppLanguage.TAMIL -> "பக்தி மணம் கமழும் தமிழ் நாட்காட்டி, பஞ்சாங்கம், ஜோதிடம் மற்றும் திருக்கோயில் சேவைகளுக்கான அதிகாரப்பூர்வ செயலி.\n\nபதிப்பு: 1.0.0 (Native Android with Jetpack Compose & Room)"
                                AppLanguage.HINDI -> "प्रामाणिक वैदिक पंचांग, दैनिक राशिफल, विवाह मिलान, कुंडली एवं मंदिर सेवा हेतु आधिकारिक ऐप।\n\nसंस्करण: 1.0.0 (Native Android with Jetpack Compose & Room)"
                                AppLanguage.ENGLISH -> "Official devotional Hindu Calendar, Ephemeris Panchangam, Horoscope, Wedding Match and Temple companion application.\n\nVersion: 1.0.0 (Native Android with Jetpack Compose & Room)"
                            },
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
