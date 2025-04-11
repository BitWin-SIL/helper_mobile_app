package com.bitwin.helperapp.core.theme

import android.app.Activity
import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = White,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = Black,

    secondary = Secondary,
    onSecondary = White,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = Black,

    tertiary = Accent,
    onTertiary = Black,
    tertiaryContainer = AccentLight,
    onTertiaryContainer = Black,

    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,

    error = Color(0xFFB00020),
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = White,
    primaryContainer = Primary,
    onPrimaryContainer = White,

    secondary = DarkSecondary,
    onSecondary = White,
    secondaryContainer = Secondary,
    onSecondaryContainer = White,

    tertiary = DarkAccent,
    onTertiary = Black,
    tertiaryContainer = Accent,
    onTertiaryContainer = Black,

    background = Black,
    onBackground = White,
    surface = DarkGray,
    onSurface = White,

    error = Color(0xFFCF6679),
    onError = Black
)

@Composable
fun HelperAppTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            dynamicLightColorScheme(context)
        }
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}