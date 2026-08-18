package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark Temple Color Scheme - Soothing, high-contrast dark atmosphere
private val DarkColorScheme = darkColorScheme(
    primary = TempleGoldLight,               // 0xFFFDE68A
    onPrimary = Color(0xFF451A03),
    primaryContainer = Color(0xFF4C1D24),
    onPrimaryContainer = Color(0xFFFEE2E2),
    inversePrimary = TempleMaroonLight,

    secondary = TempleGoldLight,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFEF3C7),

    tertiary = TempleSaffronLight,
    onTertiary = Color(0xFF431407),
    tertiaryContainer = Color(0xFF431407),
    onTertiaryContainer = Color(0xFFFFEDD5),

    background = TempleDarkBackground,
    onBackground = TempleDarkOnSurface,

    surface = TempleDarkSurface,
    onSurface = TempleDarkOnSurface,
    surfaceVariant = TempleDarkSurfaceVariant,
    onSurfaceVariant = TempleDarkOnSurfaceVariant,
    surfaceTint = TempleGoldLight,

    inverseSurface = TempleLightBackground,
    inverseOnSurface = TempleLightOnSurface,

    outline = TempleDarkOutline,
    outlineVariant = TempleDarkOutlineVariant,

    error = Color(0xFFFCA5A5),
    onError = Color(0xFF7F1D1D),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2),

    scrim = Color.Black
)

// Light Temple Color Scheme - Clean, modern, bright, light and airy palette
private val LightColorScheme = lightColorScheme(
    primary = TempleMaroon,                  // 0xFF9E2A2B - Soft Warm Ruby Crimson
    onPrimary = Color.White,
    primaryContainer = TempleMaroonContainer,// 0xFFFEF2F2 - Soft delicate rose pastel
    onPrimaryContainer = TempleMaroonOnContainer, // 0xFF881337
    inversePrimary = TempleMaroonLight,

    secondary = TempleGoldDark,              // 0xFF92400E - Rich golden bronze
    onSecondary = Color.White,
    secondaryContainer = TempleGoldContainer,// 0xFFFFFBEB - Sandalwood silk pastel
    onSecondaryContainer = TempleGoldOnContainer, // 0xFF78350F

    tertiary = TempleSaffron,                // 0xFFEA580C - Crisp Warm Saffron
    onTertiary = Color.White,
    tertiaryContainer = TempleSaffronContainer, // 0xFFFFF7ED - Soft peach pastel
    onTertiaryContainer = TempleSaffronOnContainer, // 0xFF7C2D12

    background = TempleLightBackground,       // 0xFFF8F9FA - Pure airy light background
    onBackground = TempleLightOnSurface,      // 0xFF1E293B - Crisp slate dark text
    surface = TempleLightSurface,             // 0xFFFFFFFF - Pure white card surface
    onSurface = TempleLightOnSurface,         // 0xFF1E293B
    surfaceVariant = TempleLightSurfaceVariant, // 0xFFF3F4F6 - Soft subtle light grey
    onSurfaceVariant = TempleLightOnSurfaceVariant, // 0xFF475569 - Secondary text
    surfaceTint = TempleMaroon,

    inverseSurface = TempleDarkSurface,
    inverseOnSurface = TempleDarkOnSurface,

    outline = TempleLightOutline,             // 0xFFE2E8F0 - Soft border
    outlineVariant = TempleLightOutlineVariant, // 0xFFF1F5F9

    error = WarningRed,                      // 0xFFDC2626
    onError = Color.White,
    errorContainer = WarningRedContainer,    // 0xFFFEF2F2
    onErrorContainer = WarningRedOnContainer, // 0xFF991B1B

    scrim = Color.Black
)

@Composable
fun SriSivaKovilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

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
