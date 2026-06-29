package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WarmColorScheme = lightColorScheme(
    primary = CoralPrimary,
    secondary = WarmBrownSecondary,
    tertiary = AmberTertiary,
    background = LightCreamBg,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DarkBrownText,
    onSurface = DarkBrownText,
    primaryContainer = Color(0xFFFFEBEE),
    onPrimaryContainer = Color(0xFFC62828),
    secondaryContainer = Color(0xFFEFEBE9),
    onSecondaryContainer = Color(0xFF4E342E)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WarmColorScheme,
        typography = Typography,
        content = content
    )
}
