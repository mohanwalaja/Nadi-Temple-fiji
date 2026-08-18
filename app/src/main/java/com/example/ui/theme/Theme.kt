package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ============================================================================
// SRI SIVA SUBRAMANIYA SWAMI KOVIL - COHESIVE TWO-TONE PALETTE
// Tone 1: Royal Devotional Crimson / Maroon (Primary)
// Tone 2: Burnished Temple Gold (Secondary / Accent)
// ============================================================================

// 1. Signature Auspicious Day Theme (Two-Tone: Crimson & Burnished Gold on Warm Sandal Canvas)
val TempleDayColorScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF9F1239),           // Tone 1: Royal Devotional Crimson
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE4E6),  // Soft rose container
    onPrimaryContainer = Color(0xFF4C0519),
    secondary = Color(0xFFB45309),         // Tone 2: Burnished Temple Gold
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7), // Warm sandalwood silk
    onSecondaryContainer = Color(0xFF451A03),
    tertiary = Color(0xFFB45309),          // Tone 2: Unified Gold Accent
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF451A03),
    background = Color(0xFFFAF8F5),        // Warm sacred off-white canvas
    onBackground = Color(0xFF1C1917),      // Deep charcoal text
    surface = Color.White,                 // Crisp pure white card surface
    onSurface = Color(0xFF1C1917),
    surfaceVariant = Color(0xFFF5EFEB),    // Sandalwood silk tint
    onSurfaceVariant = Color(0xFF57534E),  // Clear readable subtitle
    outline = Color(0xFFE7DFD8),           // Elegant card border
    outlineVariant = Color(0xFFF0E9E2),
    error = Color(0xFF9F1239)
)

// 2. Signature Sacred Dark Theme (Two-Tone: Luminous Rose Crimson & Radiant Gold on Deep Obsidian Canvas)
val TempleDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFFDA4AF),           // Tone 1: Soft Radiant Rose Crimson
    onPrimary = Color(0xFF4C0519),
    primaryContainer = Color(0xFF881337),
    onPrimaryContainer = Color(0xFFFFE4E6),
    secondary = Color(0xFFFBBF24),         // Tone 2: Radiant Temple Gold
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = Color(0xFFFEF3C7),
    tertiary = Color(0xFFFBBF24),          // Tone 2: Unified Gold Accent
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = Color(0xFF78350F),
    onTertiaryContainer = Color(0xFFFEF3C7),
    background = Color(0xFF120E10),        // Deep obsidian canvas
    onBackground = Color(0xFFF5F5F4),      // Crisp ivory text
    surface = Color(0xFF1C1619),           // Refined dark slate-maroon card
    onSurface = Color(0xFFF5F5F4),
    surfaceVariant = Color(0xFF2B2226),    // Warm dark surface
    onSurfaceVariant = Color(0xFFD6D3D1),  // Soft gray subtitle
    outline = Color(0xFF44373C),
    outlineVariant = Color(0xFF2B2226),
    error = Color(0xFFFDA4AF),
    onError = Color(0xFF881337)
)

@Composable
fun SriSivaKovilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) TempleDarkColorScheme else TempleDayColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Retain alias for project compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    SriSivaKovilTheme(darkTheme = darkTheme, content = content)
}
