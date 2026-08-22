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
    primary = Color(0xFF18181B),          // Bold Jet Black
    onPrimary = Color.White,              // Crisp White
    primaryContainer = Color(0xFFF4F4F5), // Light Gray/Silver Container
    onPrimaryContainer = Color(0xFF09090B),
    secondary = Color(0xFF27272A),        // Dark Slate Accent
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF4F4F5),
    onSecondaryContainer = Color(0xFF18181B),
    tertiary = Color(0xFF3F3F46),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF4F4F5),
    onTertiaryContainer = Color(0xFF18181B),
    background = Color(0xFFFFFFFF),       // Pure White Canvas
    onBackground = Color(0xFF09090B),     // Jet Black Text
    surface = Color(0xFFFFFFFF),          // Pure White Surface
    onSurface = Color(0xFF09090B),        // Jet Black Text
    surfaceVariant = Color(0xFFF4F4F5),   // Clean Neutral Light Container
    onSurfaceVariant = Color(0xFF52525B), // Slate Subtitle
    outline = Color(0xFFE4E4E7),          // Clean Border
    outlineVariant = Color(0xFFF4F4F5),
    error = Color(0xFF18181B),
    onError = Color.White
)

// 2. Dark Mode (Black & White)
val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),          // Pure Crisp White
    onPrimary = Color(0xFF000000),        // Pitch Black
    primaryContainer = Color(0xFF27272A), // Dark Slate Container
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFFE4E4E7),        // Light Slate Accent
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF27272A),
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = Color(0xFFD4D4D8),
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFF27272A),
    onTertiaryContainer = Color(0xFFFFFFFF),
    background = Color(0xFF000000),       // Pure Pitch Black Canvas
    onBackground = Color(0xFFFFFFFF),     // Pure White Text
    surface = Color(0xFF121214),          // Deep Charcoal Black Card Surface
    onSurface = Color(0xFFFFFFFF),        // Pure White Text
    surfaceVariant = Color(0xFF1C1C1F),   // Dark Elevated Container
    onSurfaceVariant = Color(0xFFA1A1AA), // Silver Gray Subtitle
    outline = Color(0xFF27272A),          // Subtle Dark Border
    outlineVariant = Color(0xFF1C1C1F),
    error = Color(0xFFFFFFFF),
    onError = Color(0xFF000000)
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

