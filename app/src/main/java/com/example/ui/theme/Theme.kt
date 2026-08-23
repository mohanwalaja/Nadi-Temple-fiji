package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ============================================================================
// TWO THEMES ONLY:
// 1. White and Black Day Mode
// 2. Black and White Dark Mode
// ============================================================================

// 1. Day Mode (White & Black)
val DayColorScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF111827),          // Dark Slate Primary
    onPrimary = Color.White,              // Crisp White
    primaryContainer = Color(0xFFF3F4F6), // Light Gray/Silver Container
    onPrimaryContainer = Color(0xFF111827),
    secondary = Color(0xFF374151),        // Dark Slate Accent
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3F4F6),
    onSecondaryContainer = Color(0xFF111827),
    tertiary = Color(0xFF4B5563),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF3F4F6),
    onTertiaryContainer = Color(0xFF111827),
    background = Color(0xFFFFFFFF),       // Pure White Canvas
    onBackground = Color(0xFF111827),     // Dark Slate Text
    surface = Color(0xFFFFFFFF),          // Pure White Surface
    onSurface = Color(0xFF111827),        // Dark Slate Text
    surfaceVariant = Color(0xFFF9FAFB),   // Clean Neutral Light Container
    onSurfaceVariant = Color(0xFF374151), // Slate Subtitle
    outline = Color(0xFFE5E7EB),          // Clean Border
    outlineVariant = Color(0xFFF3F4F6),
    error = Color(0xFFDC2626),
    onError = Color.White
)

// 2. Dark Mode (Black & White)
val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),          // Pure Crisp White
    onPrimary = Color(0xFF0F172A),        // Pitch Black / Dark Slate
    primaryContainer = Color(0xFF1E293B), // Slate Elevated Container
    onPrimaryContainer = Color(0xFFF9FAFB),
    secondary = Color(0xFFE5E7EB),        // Light Slate Accent
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = Color(0xFF1E293B),
    onSecondaryContainer = Color(0xFFF9FAFB),
    tertiary = Color(0xFFD1D5DB),
    onTertiary = Color(0xFF0F172A),
    tertiaryContainer = Color(0xFF1E293B),
    onTertiaryContainer = Color(0xFFF9FAFB),
    background = Color(0xFF0F172A),       // Deep Slate / Midnight Canvas
    onBackground = Color(0xFFF9FAFB),     // Pure White Text
    surface = Color(0xFF1E293B),          // Elevated Slate Card Surface
    onSurface = Color(0xFFF9FAFB),        // Pure White Text
    surfaceVariant = Color(0xFF334155),   // Elevated Container
    onSurfaceVariant = Color(0xFFE5E7EB), // Light Silver Subtitle
    outline = Color(0xFF374151),          // Clean Dark Border
    outlineVariant = Color(0xFF1E293B),
    error = Color(0xFFF87171),
    onError = Color(0xFF0F172A)
)

// Aliases for compatibility
val TempleDayColorScheme = DayColorScheme
val TempleDarkColorScheme = DarkColorScheme

@Composable
fun SriSivaKovilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else DayColorScheme

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

