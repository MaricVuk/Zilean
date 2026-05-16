package com.example.zilean.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF22C55E),
    secondary = Color(0xFF3B82F6),
    background = Color(0xFF000000),
    surface = Color(0xFF191919)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF22C55E),
    secondary = Color(0xFF3B82F6),
    background = Color(0xFFE1E1E1),
    surface = Color(0xFFFFFFFF)
)

@Composable
fun ZileanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
