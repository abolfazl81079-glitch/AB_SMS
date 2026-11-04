package com.anindra.messages.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

/**
 * AB/SMS Cyberpunk + Hacker Interface + Liquid Glass Theme System.
 * Defines the 9 specialized built-in themes specified in Section 9 of the Master Development Prompt.
 */
object CyberTheme {

    // ──────────────── 0. Stark Cyber HUD (Hacker Cockpit Theme) ────────────────
    val StarkHud = darkColorScheme(
        primary = Color(0xFFFFCC00),             // Stark Tech Gold / Cyber Amber
        onPrimary = Color(0xFF060A10),
        primaryContainer = Color(0xFF261D05),    // Dark Amber Glass
        onPrimaryContainer = Color(0xFFFFF4D0),
        inversePrimary = Color(0xFFFFB300),
        secondary = Color(0xFF00E5FF),           // Tech HUD Cyan
        onSecondary = Color(0xFF060A10),
        secondaryContainer = Color(0xFF0A1420),  // Dark Cyan Glass
        onSecondaryContainer = Color(0xFFD0F4FF),
        tertiary = Color(0xFFFF2A4A),            // Digital Crimson Alert
        onTertiary = Color(0xFF060A10),
        background = Color(0xFF04070B),          // Deep Cockpit Black
        onBackground = Color(0xFFEAF4FC),        // High-tech White
        surface = Color(0xFF080D14),             // Dark Cockpit Slate
        onSurface = Color(0xFFEAF4FC),
        surfaceVariant = Color(0xFF0F1722),      // HUD Cell Card Background
        onSurfaceVariant = Color(0xFF8B9DAE),    // Telemetry secondary text
        outline = Color(0xFFFFCC00),             // Cyber Gold Borders
        outlineVariant = Color(0xFF00E5FF),      // Tech Cyan Linework
        error = Color(0xFFFF2A4A),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFF04070B),
        surfaceContainerLow = Color(0xFF060A10),
        surfaceContainer = Color(0xFF0A1018),    // Cell Card Background
        surfaceContainerHigh = Color(0xFF101824),
        surfaceContainerHighest = Color(0xFF182232)
    )

    // ──────────────── 1. AB Cyber Blue (Primary Theme) ────────────────
    val AbCyberBlue = darkColorScheme(
        primary = Color(0xFF00E5FF),             // Neon cyan
        onPrimary = Color(0xFF030609),
        primaryContainer = Color(0xFF005299),    // Electric blue container
        onPrimaryContainer = Color(0xFFEAF6FF),
        inversePrimary = Color(0xFF008CFF),
        secondary = Color(0xFF008CFF),           // Electric blue
        onSecondary = Color(0xFF030609),
        secondaryContainer = Color(0xFF0B1622),  // Secondary background
        onSecondaryContainer = Color(0xFFEAF6FF),
        tertiary = Color(0xFF00E5FF),
        onTertiary = Color(0xFF030609),
        background = Color(0xFF030609),          // Deep black
        onBackground = Color(0xFFEAF6FF),        // White
        surface = Color(0xFF070D14),             // Dark background
        onSurface = Color(0xFFEAF6FF),
        surfaceVariant = Color(0xFF0B1622),
        onSurfaceVariant = Color(0xFF7892A8),    // Secondary text
        outline = Color(0xFF00E5FF),
        outlineVariant = Color(0xFF005299),
        surfaceContainerLowest = Color(0xFF030609),
        surfaceContainerLow = Color(0xFF050A0F),
        surfaceContainer = Color(0xFF070D14),
        surfaceContainerHigh = Color(0xFF0B1622),
        surfaceContainerHighest = Color(0xFF101F30)
    )

    // ──────────────── 2. Blackout (OLED pure black) ────────────────
    val Blackout = darkColorScheme(
        primary = Color(0xFFFFFFFF),
        onPrimary = Color(0xFF000000),
        primaryContainer = Color(0xFF222222),
        onPrimaryContainer = Color(0xFFFFFFFF),
        secondary = Color(0xFFAAAAAA),
        onSecondary = Color(0xFF000000),
        secondaryContainer = Color(0xFF141414),
        onSecondaryContainer = Color(0xFFE0E0E0),
        background = Color(0xFF000000),
        onBackground = Color(0xFFFFFFFF),
        surface = Color(0xFF000000),
        onSurface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFF111111),
        onSurfaceVariant = Color(0xFF888888),
        outline = Color(0xFF444444),
        outlineVariant = Color(0xFF222222),
        surfaceContainer = Color(0xFF080808),
        surfaceContainerHigh = Color(0xFF111111),
        surfaceContainerHighest = Color(0xFF1A1A1A)
    )

    // ──────────────── 3. Neon Matrix (Black & Green) ────────────────
    val NeonMatrix = darkColorScheme(
        primary = Color(0xFF00FF66),             // Matrix neon green
        onPrimary = Color(0xFF020B05),
        primaryContainer = Color(0xFF005522),
        onPrimaryContainer = Color(0xFFD4FFE0),
        secondary = Color(0xFF00CC55),
        onSecondary = Color(0xFF020B05),
        secondaryContainer = Color(0xFF081F0E),
        onSecondaryContainer = Color(0xFFD4FFE0),
        background = Color(0xFF020B05),
        onBackground = Color(0xFFD4FFE0),
        surface = Color(0xFF041208),
        onSurface = Color(0xFFD4FFE0),
        surfaceVariant = Color(0xFF081F0E),
        onSurfaceVariant = Color(0xFF6B9975),
        outline = Color(0xFF00FF66),
        outlineVariant = Color(0xFF007733),
        surfaceContainer = Color(0xFF041208),
        surfaceContainerHigh = Color(0xFF081F0E),
        surfaceContainerHighest = Color(0xFF0D2E16)
    )

    // ──────────────── 4. Arctic Glass (Dark blue & Cyan) ────────────────
    val ArcticGlass = darkColorScheme(
        primary = Color(0xFF7FE8FF),
        onPrimary = Color(0xFF050D1A),
        primaryContainer = Color(0xFF004D66),
        onPrimaryContainer = Color(0xFFE5F9FF),
        secondary = Color(0xFF00E5FF),
        onSecondary = Color(0xFF050D1A),
        secondaryContainer = Color(0xFF0D253A),
        onSecondaryContainer = Color(0xFFE5F9FF),
        background = Color(0xFF050D1A),
        onBackground = Color(0xFFE5F9FF),
        surface = Color(0xFF081526),
        onSurface = Color(0xFFE5F9FF),
        surfaceVariant = Color(0xFF0D253A),
        onSurfaceVariant = Color(0xFF7AA5C2),
        outline = Color(0xFF7FE8FF),
        outlineVariant = Color(0xFF00E5FF),
        surfaceContainer = Color(0xFF081526),
        surfaceContainerHigh = Color(0xFF0D253A),
        surfaceContainerHighest = Color(0xFF133550)
    )

    // ──────────────── 5. Ghost Protocol (Black & Silver) ────────────────
    val GhostProtocol = darkColorScheme(
        primary = Color(0xFFE0E6ED),
        onPrimary = Color(0xFF0B0C0E),
        primaryContainer = Color(0xFF333842),
        onPrimaryContainer = Color(0xFFFFFFFF),
        secondary = Color(0xFF9AA4B2),
        onSecondary = Color(0xFF0B0C0E),
        secondaryContainer = Color(0xFF1A1D24),
        onSecondaryContainer = Color(0xFFE0E6ED),
        background = Color(0xFF0B0C0E),
        onBackground = Color(0xFFE0E6ED),
        surface = Color(0xFF101318),
        onSurface = Color(0xFFE0E6ED),
        surfaceVariant = Color(0xFF1A1D24),
        onSurfaceVariant = Color(0xFF7A8490),
        outline = Color(0xFF9AA4B2),
        outlineVariant = Color(0xFF4A5260),
        surfaceContainer = Color(0xFF101318),
        surfaceContainerHigh = Color(0xFF1A1D24),
        surfaceContainerHighest = Color(0xFF242832)
    )

    // ──────────────── 6. Deep Ocean (Dark Navy & Electric Blue) ────────────────
    val DeepOcean = darkColorScheme(
        primary = Color(0xFF00B4D8),
        onPrimary = Color(0xFF030A14),
        primaryContainer = Color(0xFF003D66),
        onPrimaryContainer = Color(0xFFE0F4FF),
        secondary = Color(0xFF008CFF),
        onSecondary = Color(0xFF030A14),
        secondaryContainer = Color(0xFF081C38),
        onSecondaryContainer = Color(0xFFE0F4FF),
        background = Color(0xFF030A14),
        onBackground = Color(0xFFE0F4FF),
        surface = Color(0xFF061324),
        onSurface = Color(0xFFE0F4FF),
        surfaceVariant = Color(0xFF081C38),
        onSurfaceVariant = Color(0xFF6B8CAE),
        outline = Color(0xFF00B4D8),
        outlineVariant = Color(0xFF008CFF),
        surfaceContainer = Color(0xFF061324),
        surfaceContainerHigh = Color(0xFF081C38),
        surfaceContainerHighest = Color(0xFF0E2A52)
    )

    // ──────────────── 7. Crimson Hacker (Black & Red) ────────────────
    val CrimsonHacker = darkColorScheme(
        primary = Color(0xFFFF2A4A),
        onPrimary = Color(0xFF0E0404),
        primaryContainer = Color(0xFF660A18),
        onPrimaryContainer = Color(0xFFFFE5E8),
        secondary = Color(0xFFFF4D6D),
        onSecondary = Color(0xFF0E0404),
        secondaryContainer = Color(0xFF240A10),
        onSecondaryContainer = Color(0xFFFFE5E8),
        background = Color(0xFF0E0404),
        onBackground = Color(0xFFFFE5E8),
        surface = Color(0xFF170608),
        onSurface = Color(0xFFFFE5E8),
        surfaceVariant = Color(0xFF240A10),
        onSurfaceVariant = Color(0xFFA86B75),
        outline = Color(0xFFFF2A4A),
        outlineVariant = Color(0xFF990F24),
        surfaceContainer = Color(0xFF170608),
        surfaceContainerHigh = Color(0xFF240A10),
        surfaceContainerHighest = Color(0xFF381018)
    )

    // ──────────────── 8. Quantum (Multicolor Cyber Accents) ────────────────
    val Quantum = darkColorScheme(
        primary = Color(0xFFC084FC),             // Cyber Purple
        onPrimary = Color(0xFF090412),
        primaryContainer = Color(0xFF4C1D95),
        onPrimaryContainer = Color(0xFFF3E8FF),
        secondary = Color(0xFF00E5FF),           // Cyan
        onSecondary = Color(0xFF090412),
        secondaryContainer = Color(0xFF1E0E38),
        onSecondaryContainer = Color(0xFFF3E8FF),
        background = Color(0xFF090412),
        onBackground = Color(0xFFF3E8FF),
        surface = Color(0xFF0F071D),
        onSurface = Color(0xFFF3E8FF),
        surfaceVariant = Color(0xFF1E0E38),
        onSurfaceVariant = Color(0xFF9B82B8),
        outline = Color(0xFFC084FC),
        outlineVariant = Color(0xFF7E22CE),
        surfaceContainer = Color(0xFF0F071D),
        surfaceContainerHigh = Color(0xFF1E0E38),
        surfaceContainerHighest = Color(0xFF2E1754)
    )

    // ──────────────── 9. Minimal Glass (Restrained Transparent) ────────────────
    val MinimalGlass = darkColorScheme(
        primary = Color(0xFF00E5FF),
        onPrimary = Color(0xFF050507),
        primaryContainer = Color(0xFF1A2230),
        onPrimaryContainer = Color(0xFFF0F4F8),
        secondary = Color(0xFF8899A6),
        onSecondary = Color(0xFF050507),
        secondaryContainer = Color(0xFF12141A),
        onSecondaryContainer = Color(0xFFF0F4F8),
        background = Color(0xFF050507),
        onBackground = Color(0xFFF0F4F8),
        surface = Color(0xFF0B0D12),
        onSurface = Color(0xFFF0F4F8),
        surfaceVariant = Color(0xFF12141A),
        onSurfaceVariant = Color(0xFF708090),
        outline = Color(0xFF303848),
        outlineVariant = Color(0xFF202632),
        surfaceContainer = Color(0xFF0B0D12),
        surfaceContainerHigh = Color(0xFF12141A),
        surfaceContainerHighest = Color(0xFF1C202A)
    )

    /**
     * Resolves the requested theme mode into a ColorScheme.
     */
    fun schemeForMode(mode: String): ColorScheme = when (mode) {
        "stark", "stark_hud", "hacker_hud", "hud" -> StarkHud
        "cyber", "cyber_blue", "ab_cyber_blue" -> AbCyberBlue
        "blackout" -> Blackout
        "matrix", "neon_matrix" -> NeonMatrix
        "arctic", "arctic_glass" -> ArcticGlass
        "ghost", "ghost_protocol" -> GhostProtocol
        "ocean", "deep_ocean" -> DeepOcean
        "crimson", "crimson_hacker" -> CrimsonHacker
        "quantum" -> Quantum
        "minimal", "minimal_glass" -> MinimalGlass
        else -> StarkHud // Default is Stark Cyber HUD from screenshot
    }
}
