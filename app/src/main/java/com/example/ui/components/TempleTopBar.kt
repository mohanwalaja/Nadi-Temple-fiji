package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempleTopBar(
    currentLanguage: AppLanguage,
    onToggleLanguage: () -> Unit,
    onNavigateToSettings: () -> Unit,
    title: String? = null
) {
    Column {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = title ?: if (currentLanguage == AppLanguage.TAMIL) "பஞ்சாங்கம் • ஜாதகம்" else if (currentLanguage == AppLanguage.HINDI) "पंचांग • कुंडली • राशिफल" else "Panchangam • Horoscope",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (currentLanguage == AppLanguage.TAMIL) "துல்லிய திருக்கணித பஞ்சாங்கம் • திருமணப் பொருத்தம்" else if (currentLanguage == AppLanguage.HINDI) "सटीक वैदिक पंचांग • विवाह मिलान" else "Precision Vedic Panchangam & Astrology",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            actions = {
                // Language Toggle pill button
                Surface(
                    onClick = onToggleLanguage,
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("top_bar_language_toggle_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "தமிழ்"
                                AppLanguage.HINDI -> "हिन्दी"
                                AppLanguage.ENGLISH -> "ENG"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Settings quick action
                IconButton(
                    onClick = onNavigateToSettings,
                    modifier = Modifier.testTag("top_bar_settings_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
    }
}
