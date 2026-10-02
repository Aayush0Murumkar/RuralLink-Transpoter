package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PartnerBlueLight,
    onPrimary = Color.White,
    primaryContainer = PartnerBlueDark,
    onPrimaryContainer = PartnerBlueSubtle,
    secondary = PartnerYellow,
    onSecondary = Color.Black,
    secondaryContainer = PartnerYellowDark,
    onSecondaryContainer = PartnerYellowLight,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = Color(0xFFE2E8F0),
    onSurface = Color(0xFFE2E8F0)
)

private val LightColorScheme = lightColorScheme(
    primary = PartnerBlue,
    onPrimary = Color.White,
    primaryContainer = PartnerBlueSurface,
    onPrimaryContainer = PartnerBlueDark,
    secondary = PartnerYellow,
    onSecondary = Color.Black,
    secondaryContainer = PartnerYellowLight,
    onSecondaryContainer = PartnerYellowText,
    tertiary = StatusGreen,
    onTertiary = Color.White,
    tertiaryContainer = StatusGreenContainer,
    background = AppBackground,
    surface = CardSurface,
    surfaceVariant = PartnerBlueSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

