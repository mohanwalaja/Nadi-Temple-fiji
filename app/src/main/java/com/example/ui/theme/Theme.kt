package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ============================================================================
// THEME COLOR SCHEMES GENERATOR
// ============================================================================

enum class KovilThemePreset(
    val code: String,
    val nameTa: String,
    val nameEn: String,
    val nameHi: String,
    val primaryColor: Color,
    val secondaryColor: Color
) {
    RUBY_MAROON(
        code = "RUBY_MAROON",
        nameTa = "ராஜ குங்குமம் & பொன் (Ruby Crimson)",
        nameEn = "Royal Ruby Crimson & Gold",
        nameHi = "राज कुमकुम एवं स्वर्ण",
        primaryColor = Color(0xFFB91C1C),
        secondaryColor = Color(0xFFD97706)
    ),
    PEACOCK_TEAL(
        code = "PEACOCK_TEAL",
        nameTa = "மயில் நீலம் & பொன் (Peacock Teal)",
        nameEn = "Peacock Teal & Gold (Murugan)",
        nameHi = "मयूर नीला एवं स्वर्ण",
        primaryColor = Color(0xFF0E7490),
        secondaryColor = Color(0xFFD97706)
    ),
    SAFFRON_GOLD(
        code = "SAFFRON_GOLD",
        nameTa = "சூரிய காவி & சந்தனம் (Surya Saffron)",
        nameEn = "Radiant Saffron & Sandalwood",
        nameHi = "सूर्य केसरिया एवं चंदन",
        primaryColor = Color(0xFFEA580C),
        secondaryColor = Color(0xFFB45309)
    ),
    SHIVA_BLUE(
        code = "SHIVA_BLUE",
        nameTa = "கங்காதர நீலம் & வெள்ளி (Shiva Blue)",
        nameEn = "Sacred Shiva Blue & Platinum",
        nameHi = "गंगाधर नीला एवं रजत",
        primaryColor = Color(0xFF1D4ED8),
        secondaryColor = Color(0xFF0284C7)
    ),
    BILVA_GREEN(
        code = "BILVA_GREEN",
        nameTa = "வில்வ பச்சை & பொன்னிறம் (Bilva Green)",
        nameEn = "Auspicious Bilva Emerald & Gold",
        nameHi = "बिल्व हरा एवं स्वर्ण",
        primaryColor = Color(0xFF15803D),
        secondaryColor = Color(0xFFD97706)
    ),
    DIVINE_PURPLE(
        code = "DIVINE_PURPLE",
        nameTa = "ராஜ ஊதா & பவளம் (Divine Violet)",
        nameEn = "Royal Divine Purple & Rose",
        nameHi = "राज बैंगनी एवं गुलाब",
        primaryColor = Color(0xFF7E22CE),
        secondaryColor = Color(0xFFBE185D)
    );

    companion object {
        fun fromCode(code: String): KovilThemePreset {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: RUBY_MAROON
        }
    }
}

fun getLightColorSchemeForPreset(preset: KovilThemePreset): ColorScheme {
    return when (preset) {
        KovilThemePreset.RUBY_MAROON -> lightColorScheme(
            primary = Color(0xFFB91C1C),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFEF2F2),
            onPrimaryContainer = Color(0xFF881337),
            secondary = Color(0xFFD97706),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFFBEB),
            onSecondaryContainer = Color(0xFF78350F),
            tertiary = Color(0xFFEA580C),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFFF7ED),
            onTertiaryContainer = Color(0xFF7C2D12),
            background = Color(0xFFF8F9FA),
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFF1F5F9),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFE2E8F0),
            outlineVariant = Color(0xFFF1F5F9),
            error = Color(0xFFDC2626)
        )
        KovilThemePreset.PEACOCK_TEAL -> lightColorScheme(
            primary = Color(0xFF0E7490),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFECFEFF),
            onPrimaryContainer = Color(0xFF155E75),
            secondary = Color(0xFFD97706),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFFBEB),
            onSecondaryContainer = Color(0xFF78350F),
            tertiary = Color(0xFF0284C7),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFF0F9FF),
            onTertiaryContainer = Color(0xFF0369A1),
            background = Color(0xFFF8FAFC),
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFF1F5F9),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFE2E8F0),
            outlineVariant = Color(0xFFF1F5F9),
            error = Color(0xFFDC2626)
        )
        KovilThemePreset.SAFFRON_GOLD -> lightColorScheme(
            primary = Color(0xFFEA580C),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFFF7ED),
            onPrimaryContainer = Color(0xFF9A3412),
            secondary = Color(0xFFD97706),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFFBEB),
            onSecondaryContainer = Color(0xFF78350F),
            tertiary = Color(0xFFB91C1C),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFEF2F2),
            onTertiaryContainer = Color(0xFF881337),
            background = Color(0xFFFFFDF8),
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFFBF4EB),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFEFE6DC),
            outlineVariant = Color(0xFFFBF4EB),
            error = Color(0xFFDC2626)
        )
        KovilThemePreset.SHIVA_BLUE -> lightColorScheme(
            primary = Color(0xFF1D4ED8),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFEFF6FF),
            onPrimaryContainer = Color(0xFF1E3A8A),
            secondary = Color(0xFF0284C7),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFF0F9FF),
            onSecondaryContainer = Color(0xFF075985),
            tertiary = Color(0xFFD97706),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFFFBEB),
            onTertiaryContainer = Color(0xFF78350F),
            background = Color(0xFFF8FAFC),
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFF1F5F9),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFE2E8F0),
            outlineVariant = Color(0xFFF1F5F9),
            error = Color(0xFFDC2626)
        )
        KovilThemePreset.BILVA_GREEN -> lightColorScheme(
            primary = Color(0xFF15803D),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFF0FDF4),
            onPrimaryContainer = Color(0xFF14532D),
            secondary = Color(0xFFD97706),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFFBEB),
            onSecondaryContainer = Color(0xFF78350F),
            tertiary = Color(0xFF047857),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFECFDF5),
            onTertiaryContainer = Color(0xFF064E3B),
            background = Color(0xFFF9FBF9),
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFF0F5F0),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFDDE8DD),
            outlineVariant = Color(0xFFF0F5F0),
            error = Color(0xFFDC2626)
        )
        KovilThemePreset.DIVINE_PURPLE -> lightColorScheme(
            primary = Color(0xFF7E22CE),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFAF5FF),
            onPrimaryContainer = Color(0xFF581C87),
            secondary = Color(0xFFD97706),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFFBEB),
            onSecondaryContainer = Color(0xFF78350F),
            tertiary = Color(0xFFBE185D),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFDF2F8),
            onTertiaryContainer = Color(0xFF831843),
            background = Color(0xFFFAF8FC),
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFF3E8FF).copy(alpha = 0.35f),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFE9D5FF),
            outlineVariant = Color(0xFFF5F3FF),
            error = Color(0xFFDC2626)
        )
    }
}

fun getDarkColorSchemeForPreset(preset: KovilThemePreset): ColorScheme {
    return darkColorScheme(
        primary = preset.primaryColor,
        onPrimary = Color.White,
        primaryContainer = Color(0xFF1E293B),
        onPrimaryContainer = Color(0xFFF8FAFC),
        secondary = Color(0xFFFDE68A),
        onSecondary = Color(0xFF451A03),
        secondaryContainer = Color(0xFF334155),
        onSecondaryContainer = Color(0xFFFEF3C7),
        tertiary = Color(0xFFFED7AA),
        onTertiary = Color(0xFF431407),
        background = Color(0xFF0F172A),
        onBackground = Color(0xFFF8FAFC),
        surface = Color(0xFF1E293B),
        onSurface = Color(0xFFF8FAFC),
        surfaceVariant = Color(0xFF334155),
        onSurfaceVariant = Color(0xFFCBD5E1),
        outline = Color(0xFF475569),
        outlineVariant = Color(0xFF334155),
        error = Color(0xFFFCA5A5),
        onError = Color(0xFF7F1D1D)
    )
}

@Composable
fun SriSivaKovilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themePresetCode: String = "RUBY_MAROON",
    content: @Composable () -> Unit,
) {
    val preset = KovilThemePreset.fromCode(themePresetCode)
    val colorScheme = if (darkTheme) getDarkColorSchemeForPreset(preset) else getLightColorSchemeForPreset(preset)

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
