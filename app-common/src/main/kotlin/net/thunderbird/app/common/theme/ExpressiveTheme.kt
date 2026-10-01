package net.thunderbird.app.common.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import app.k9mail.core.ui.legacy.theme2.common.R
import net.thunderbird.components.ui.bolt.theme.BoltTheme
import net.thunderbird.components.ui.bolt.theme.ThemeColorScheme
import net.thunderbird.components.ui.bolt.theme.ThemeColorSchemeVariants
import net.thunderbird.components.ui.bolt.theme.ThemeConfig
import net.thunderbird.components.ui.bolt.theme.ThemeImageVariants
import net.thunderbird.components.ui.bolt.theme.ThemeTypography

private val expressiveWeights = listOf(
    FontWeight.Light,
    FontWeight.Normal,
    FontWeight.Medium,
    FontWeight.SemiBold,
    FontWeight.Bold,
)

private val GoogleSansFlex = FontFamily(
    expressiveWeights.map { weight ->
        Font(
            resId = R.font.google_sans_flex,
            weight = weight,
            variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
        )
    },
)

/**
 * Re-themes the surrounding Bolt theme with dynamic color and the Material 3 Expressive typeface.
 *
 * Must be called inside an app's Bolt theme. Bolt's own values (semantic colors, images, sizes, spacings) are kept;
 * the Material color roles are replaced with the Android 12+ dynamic color scheme and text uses Google Sans Flex.
 */
@Composable
fun ExpressiveTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val dynamicColorScheme = dynamicColorScheme(darkTheme)
    val boltColors = BoltTheme.colors
    val colors = remember(boltColors, dynamicColorScheme) {
        dynamicColorScheme?.let { boltColors.withMaterialRoles(it) } ?: boltColors
    }
    val boltTypography = BoltTheme.typography
    val typography = remember(boltTypography) { boltTypography.withFontFamily(GoogleSansFlex) }
    val images = BoltTheme.images

    val themeConfig = ThemeConfig(
        colors = ThemeColorSchemeVariants(dark = colors, light = colors),
        elevations = BoltTheme.elevations,
        images = ThemeImageVariants(dark = images, light = images),
        shapes = BoltTheme.shapes,
        sizes = BoltTheme.sizes,
        spacings = BoltTheme.spacings,
        typography = typography,
    )

    BoltTheme(themeConfig = themeConfig, darkTheme = darkTheme) {
        val boltMaterialColors = MaterialTheme.colorScheme
        MaterialTheme(
            colorScheme = dynamicColorScheme?.let { boltMaterialColors.withFixedRoles(it) } ?: boltMaterialColors,
            shapes = MaterialTheme.shapes,
            typography = MaterialTheme.typography,
            content = content,
        )
    }
}

@Composable
private fun dynamicColorScheme(darkTheme: Boolean): ColorScheme? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return null

    val context = LocalContext.current
    return if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
}

private fun ThemeColorScheme.withMaterialRoles(scheme: ColorScheme) = copy(
    primary = scheme.primary,
    onPrimary = scheme.onPrimary,
    primaryContainer = scheme.primaryContainer,
    onPrimaryContainer = scheme.onPrimaryContainer,
    secondary = scheme.secondary,
    onSecondary = scheme.onSecondary,
    secondaryContainer = scheme.secondaryContainer,
    onSecondaryContainer = scheme.onSecondaryContainer,
    tertiary = scheme.tertiary,
    onTertiary = scheme.onTertiary,
    tertiaryContainer = scheme.tertiaryContainer,
    onTertiaryContainer = scheme.onTertiaryContainer,
    error = scheme.error,
    onError = scheme.onError,
    errorContainer = scheme.errorContainer,
    onErrorContainer = scheme.onErrorContainer,
    surfaceDim = scheme.surfaceDim,
    surface = scheme.surface,
    surfaceBright = scheme.surfaceBright,
    onSurface = scheme.onSurface,
    onSurfaceVariant = scheme.onSurfaceVariant,
    surfaceContainerLowest = scheme.surfaceContainerLowest,
    surfaceContainerLow = scheme.surfaceContainerLow,
    surfaceContainer = scheme.surfaceContainer,
    surfaceContainerHigh = scheme.surfaceContainerHigh,
    surfaceContainerHighest = scheme.surfaceContainerHighest,
    inverseSurface = scheme.inverseSurface,
    inverseOnSurface = scheme.inverseOnSurface,
    inversePrimary = scheme.inversePrimary,
    outline = scheme.outline,
    outlineVariant = scheme.outlineVariant,
    scrim = scheme.scrim,
)

private fun ColorScheme.withFixedRoles(scheme: ColorScheme) = copy(
    primaryFixed = scheme.primaryFixed,
    primaryFixedDim = scheme.primaryFixedDim,
    onPrimaryFixed = scheme.onPrimaryFixed,
    onPrimaryFixedVariant = scheme.onPrimaryFixedVariant,
    secondaryFixed = scheme.secondaryFixed,
    secondaryFixedDim = scheme.secondaryFixedDim,
    onSecondaryFixed = scheme.onSecondaryFixed,
    onSecondaryFixedVariant = scheme.onSecondaryFixedVariant,
    tertiaryFixed = scheme.tertiaryFixed,
    tertiaryFixedDim = scheme.tertiaryFixedDim,
    onTertiaryFixed = scheme.onTertiaryFixed,
    onTertiaryFixedVariant = scheme.onTertiaryFixedVariant,
)

private fun ThemeTypography.withFontFamily(fontFamily: FontFamily): ThemeTypography {
    fun TextStyle.withFont() = copy(fontFamily = fontFamily)

    return copy(
        displayLarge = displayLarge.withFont(),
        displayMedium = displayMedium.withFont(),
        displaySmall = displaySmall.withFont(),
        headlineLarge = headlineLarge.withFont(),
        headlineMedium = headlineMedium.withFont(),
        headlineSmall = headlineSmall.withFont(),
        titleLarge = titleLarge.withFont(),
        titleMedium = titleMedium.withFont(),
        titleSmall = titleSmall.withFont(),
        bodyLarge = bodyLarge.withFont(),
        bodyMedium = bodyMedium.withFont(),
        bodySmall = bodySmall.withFont(),
        labelLarge = labelLarge.withFont(),
        labelMedium = labelMedium.withFont(),
        labelSmall = labelSmall.withFont(),
    )
}
