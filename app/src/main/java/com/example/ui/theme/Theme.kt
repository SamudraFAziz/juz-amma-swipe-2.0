package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Option 2: Midnight Slate & Mint Glow (Dark Theme)
private val MidnightDarkColorScheme = darkColorScheme(
    primary = MidnightDarkPrimary,
    onPrimary = MidnightDarkOnPrimary,
    primaryContainer = MidnightDarkPrimaryContainer,
    onPrimaryContainer = MidnightDarkOnPrimaryContainer,
    secondary = MidnightDarkSecondary,
    onSecondary = MidnightDarkOnSecondary,
    secondaryContainer = MidnightDarkSecondaryContainer,
    onSecondaryContainer = MidnightDarkOnSecondaryContainer,
    background = MidnightDarkBackground,
    onBackground = MidnightDarkOnSurface,
    surface = MidnightDarkSurface,
    onSurface = MidnightDarkOnSurface,
    surfaceVariant = MidnightDarkSurfaceVariant,
    onSurfaceVariant = MidnightDarkOnSurfaceVariant,
    outline = MidnightDarkOutline,
    outlineVariant = MidnightDarkOutlineVariant
)

// Option 1: Emerald Oasis & Warm Parchment (Light Theme)
private val ParchmentLightColorScheme = lightColorScheme(
    primary = ParchmentLightPrimary,
    onPrimary = ParchmentLightOnPrimary,
    primaryContainer = ParchmentLightPrimaryContainer,
    onPrimaryContainer = ParchmentLightOnPrimaryContainer,
    secondary = ParchmentLightSecondary,
    onSecondary = ParchmentLightOnSecondary,
    secondaryContainer = ParchmentLightSecondaryContainer,
    onSecondaryContainer = ParchmentLightOnSecondaryContainer,
    background = ParchmentLightBackground,
    onBackground = ParchmentLightOnSurface,
    surface = ParchmentLightSurface,
    onSurface = ParchmentLightOnSurface,
    surfaceVariant = ParchmentLightSurfaceVariant,
    onSurfaceVariant = ParchmentLightOnSurfaceVariant,
    outline = ParchmentLightOutline,
    outlineVariant = ParchmentLightOutlineVariant
)

@Composable
fun JuzAmmaTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MidnightDarkColorScheme else ParchmentLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


