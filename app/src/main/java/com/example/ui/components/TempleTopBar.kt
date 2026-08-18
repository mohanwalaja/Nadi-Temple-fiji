package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
    onNavigateToRasiPalan: () -> Unit,
    onNavigateToDharmaSastra: () -> Unit,
    onNavigateToSettings: () -> Unit,
    title: String? = null
) {
    Column {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = title ?: if (currentLanguage == AppLanguage.TAMIL) "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி" else if (currentLanguage == AppLanguage.HINDI) "श्री शिव सुब्रमण्य स्वामी मंदिर" else "Sri Siva Subramaniya Swami",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (currentLanguage == AppLanguage.TAMIL) "திருக்கோயில் • பஞ்சாங்கம் • ஜாதகம்" else if (currentLanguage == AppLanguage.HINDI) "मंदिर • पंचांग • कुंडली" else "Temple • Panchangam • Horoscope",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            actions = {
                // Rasi Palan quick action
                IconButton(
                    onClick = onNavigateToRasiPalan,
                    modifier = Modifier.testTag("top_bar_rasi_palan_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rasi Palan",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                // Dharma Sastra quick action
                IconButton(
                    onClick = onNavigateToDharmaSastra,
                    modifier = Modifier.testTag("top_bar_dharma_sastra_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Dharma Sastra",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

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
