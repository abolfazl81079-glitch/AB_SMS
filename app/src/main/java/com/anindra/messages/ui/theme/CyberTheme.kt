package com.anindra.messages.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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


    // ──────────────── 10. Cyber Orange (Neon Amber / Orange Hacker) ────────────────
    val CyberOrange = darkColorScheme(
        primary = Color(0xFFFF6D00),             // Electric Neon Orange
        onPrimary = Color(0xFF0F0500),
        primaryContainer = Color(0xFF4E1D00),    // Dark Orange Glass
        onPrimaryContainer = Color(0xFFFFE0CC),
        inversePrimary = Color(0xFFFF9E40),
        secondary = Color(0xFFFF9100),           // Vivid Amber Accent
        onSecondary = Color(0xFF0F0500),
        secondaryContainer = Color(0xFF220E02),  // Secondary Deep Brown/Orange Glass
        onSecondaryContainer = Color(0xFFFFE0CC),
        tertiary = Color(0xFFFFAB40),            // Soft Amber
        onTertiary = Color(0xFF0F0500),
        background = Color(0xFF0A0502),          // Deep Obsidian Black
        onBackground = Color(0xFFFFF2E6),        // Warm Light Text
        surface = Color(0xFF100703),             // Dark Cockpit Slate
        onSurface = Color(0xFFFFF2E6),
        surfaceVariant = Color(0xFF1E0E06),      // Cell Card Background
        onSurfaceVariant = Color(0xFFA68574),    // Secondary Text
        outline = Color(0xFFFF6D00),             // Cyber Orange Borders
        outlineVariant = Color(0xFF8C3800),
        surfaceContainerLowest = Color(0xFF0A0502),
        surfaceContainerLow = Color(0xFF0E0703),
        surfaceContainer = Color(0xFF140A04),    // Conversation Cell Background
        surfaceContainerHigh = Color(0xFF1E0E06),
        surfaceContainerHighest = Color(0xFF2C160A)
    )

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 1. Stark Clean Tech (Light Tech Paper + Cobalt Blue) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipStarkClean = lightColorScheme(
        primary = Color(0xFF0052FF),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFDBEAFE),
        onPrimaryContainer = Color(0xFF1E3A8A),
        secondary = Color(0xFF0284C7),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE0F2FE),
        onSecondaryContainer = Color(0xFF0369A1),
        tertiary = Color(0xFF059669),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFF8FAFC),
        onBackground = Color(0xFF0F172A),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF0F172A),
        surfaceVariant = Color(0xFFF1F5F9),
        onSurfaceVariant = Color(0xFF334155),
        outline = Color(0xFF0052FF),
        outlineVariant = Color(0xFF93C5FD),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF8FAFC),
        surfaceContainer = Color(0xFFF1F5F9),
        surfaceContainerHigh = Color(0xFFE2E8F0),
        surfaceContainerHighest = Color(0xFFCBD5E1)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 2. Neo Matrix Light (Digital Paper + Emerald Green) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipMatrixLight = lightColorScheme(
        primary = Color(0xFF059669),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD1FAE5),
        onPrimaryContainer = Color(0xFF064E3B),
        secondary = Color(0xFF10B981),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFA7F3D0),
        onSecondaryContainer = Color(0xFF065F46),
        tertiary = Color(0xFF047857),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFF0FDF4),
        onBackground = Color(0xFF052E16),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF052E16),
        surfaceVariant = Color(0xFFDCFCE7),
        onSurfaceVariant = Color(0xFF14532D),
        outline = Color(0xFF059669),
        outlineVariant = Color(0xFF6EE7B7),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF0FDF4),
        surfaceContainer = Color(0xFFE8F7ED),
        surfaceContainerHigh = Color(0xFFDCFCE7),
        surfaceContainerHighest = Color(0xFFBBF7D0)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 3. Solar Amber Sunset (Warm Solar Pearl + Incandescent Amber) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipSunsetAmber = lightColorScheme(
        primary = Color(0xFFD97706),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFEF3C7),
        onPrimaryContainer = Color(0xFF78350F),
        secondary = Color(0xFFEA580C),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFEDD5),
        onSecondaryContainer = Color(0xFF7C2D12),
        tertiary = Color(0xFFB45309),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFFFFBEB),
        onBackground = Color(0xFF291505),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF291505),
        surfaceVariant = Color(0xFFFEF3C7),
        onSurfaceVariant = Color(0xFF5A2A04),
        outline = Color(0xFFD97706),
        outlineVariant = Color(0xFFFCD34D),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFFFBEB),
        surfaceContainer = Color(0xFFFEF3C7),
        surfaceContainerHigh = Color(0xFFFDE68A),
        surfaceContainerHighest = Color(0xFFFBBF24)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 4. Quantum Vaporwave (Pastel Lilac + Electric Violet) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipQuantumLilac = lightColorScheme(
        primary = Color(0xFF7C3AED),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFEDE9FE),
        onPrimaryContainer = Color(0xFF4C1D95),
        secondary = Color(0xFFC026D3),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFAE8FF),
        onSecondaryContainer = Color(0xFF701A75),
        tertiary = Color(0xFF6D28D9),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFFAF5FF),
        onBackground = Color(0xFF1E1035),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF1E1035),
        surfaceVariant = Color(0xFFF3E8FF),
        onSurfaceVariant = Color(0xFF3B166B),
        outline = Color(0xFF7C3AED),
        outlineVariant = Color(0xFFC4B5FD),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFAF5FF),
        surfaceContainer = Color(0xFFF3E8FF),
        surfaceContainerHigh = Color(0xFFE9D5FF),
        surfaceContainerHighest = Color(0xFFD8B4FE)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 5. Arctic Glacial Ice (Glacial Frost White + Azure Cyan) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipArcticIce = lightColorScheme(
        primary = Color(0xFF0284C7),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFE0F2FE),
        onPrimaryContainer = Color(0xFF075985),
        secondary = Color(0xFF0EA5E9),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFBAE6FD),
        onSecondaryContainer = Color(0xFF0369A1),
        tertiary = Color(0xFF0369A1),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFF0F9FF),
        onBackground = Color(0xFF082F49),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF082F49),
        surfaceVariant = Color(0xFFE0F2FE),
        onSurfaceVariant = Color(0xFF0C4A6E),
        outline = Color(0xFF0284C7),
        outlineVariant = Color(0xFF7DD3FC),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF0F9FF),
        surfaceContainer = Color(0xFFE0F2FE),
        surfaceContainerHigh = Color(0xFFBAE6FD),
        surfaceContainerHighest = Color(0xFF7DD3FC)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 6. Phantom Hacker (Pitch Void OLED Black + Toxic Mint) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipPhantomHacker = darkColorScheme(
        primary = Color(0xFF00FFB2),
        onPrimary = Color(0xFF001A10),
        primaryContainer = Color(0xFF003824),
        onPrimaryContainer = Color(0xFFD0FFF0),
        secondary = Color(0xFF10B981),
        onSecondary = Color(0xFF001A10),
        secondaryContainer = Color(0xFF04281B),
        onSecondaryContainer = Color(0xFFA7F3D0),
        tertiary = Color(0xFF00E5FF),
        onTertiary = Color(0xFF001A10),
        background = Color(0xFF000000),
        onBackground = Color(0xFFECFDF5),
        surface = Color(0xFF030A06),
        onSurface = Color(0xFFECFDF5),
        surfaceVariant = Color(0xFF06140D),
        onSurfaceVariant = Color(0xFF86EFAC),
        outline = Color(0xFF00FFB2),
        outlineVariant = Color(0xFF059669),
        error = Color(0xFFFF2A4A),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFF000000),
        surfaceContainerLow = Color(0xFF020604),
        surfaceContainer = Color(0xFF050E09),
        surfaceContainerHigh = Color(0xFF09160F),
        surfaceContainerHighest = Color(0xFF0E2218)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 7. Blood Overdrive (Obsidian Black + Neon Blood Red) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipBloodOverdrive = darkColorScheme(
        primary = Color(0xFFFF0055),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF3B0012),
        onPrimaryContainer = Color(0xFFFFD9E2),
        secondary = Color(0xFFFF3366),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFF2E000D),
        onSecondaryContainer = Color(0xFFFFB3C6),
        tertiary = Color(0xFFFF5277),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFF050002),
        onBackground = Color(0xFFFFF0F5),
        surface = Color(0xFF0C0105),
        onSurface = Color(0xFFFFF0F5),
        surfaceVariant = Color(0xFF17020A),
        onSurfaceVariant = Color(0xFFFFA3BA),
        outline = Color(0xFFFF0055),
        outlineVariant = Color(0xFFA80036),
        error = Color(0xFFFF2A4A),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFF050002),
        surfaceContainerLow = Color(0xFF0A0104),
        surfaceContainer = Color(0xFF120208),
        surfaceContainerHigh = Color(0xFF1A040C),
        surfaceContainerHighest = Color(0xFF260613)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 8. Synthwave 2077 (Neo Tokyo Laser Pink & Cyan Duality) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipSynthwave2077 = darkColorScheme(
        primary = Color(0xFFFF007F),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF3E001E),
        onPrimaryContainer = Color(0xFFFFD9E8),
        secondary = Color(0xFF00F0FF),
        onSecondary = Color(0xFF002024),
        secondaryContainer = Color(0xFF00363D),
        onSecondaryContainer = Color(0xFF97F0FF),
        tertiary = Color(0xFFBD00FF),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFF08040C),
        onBackground = Color(0xFFFDF4FF),
        surface = Color(0xFF100719),
        onSurface = Color(0xFFFDF4FF),
        surfaceVariant = Color(0xFF1A0C2A),
        onSurfaceVariant = Color(0xFFE879F9),
        outline = Color(0xFFFF007F),
        outlineVariant = Color(0xFF00F0FF),
        error = Color(0xFFFF2A4A),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFF08040C),
        surfaceContainerLow = Color(0xFF0E0615),
        surfaceContainer = Color(0xFF160A22),
        surfaceContainerHigh = Color(0xFF1F0E30),
        surfaceContainerHighest = Color(0xFF2B1442)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 9. Titanium Stealth Monolith (Tactical Graphite + Cold Titanium) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipTitaniumMonolith = darkColorScheme(
        primary = Color(0xFFE2E8F0),
        onPrimary = Color(0xFF0F172A),
        primaryContainer = Color(0xFF334155),
        onPrimaryContainer = Color(0xFFF8FAFC),
        secondary = Color(0xFF94A3B8),
        onSecondary = Color(0xFF0F172A),
        secondaryContainer = Color(0xFF1E293B),
        onSecondaryContainer = Color(0xFFE2E8F0),
        tertiary = Color(0xFFCBD5E1),
        onTertiary = Color(0xFF0F172A),
        background = Color(0xFF0B0D10),
        onBackground = Color(0xFFF8FAFC),
        surface = Color(0xFF111418),
        onSurface = Color(0xFFF8FAFC),
        surfaceVariant = Color(0xFF1A1F26),
        onSurfaceVariant = Color(0xFF94A3B8),
        outline = Color(0xFF94A3B8),
        outlineVariant = Color(0xFF475569),
        error = Color(0xFFFF4D4D),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFF0B0D10),
        surfaceContainerLow = Color(0xFF0E1115),
        surfaceContainer = Color(0xFF14181E),
        surfaceContainerHigh = Color(0xFF1B2028),
        surfaceContainerHighest = Color(0xFF252C37)
    )

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ VIP 10. Matrix Terminal Zero (CRT Phosphor Green + Scanlines) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    val VipMatrixTerminalZero = darkColorScheme(
        primary = Color(0xFF00FF41),
        onPrimary = Color(0xFF001B06),
        primaryContainer = Color(0xFF00380D),
        onPrimaryContainer = Color(0xFFB8FFC8),
        secondary = Color(0xFF39FF14),
        onSecondary = Color(0xFF001B06),
        secondaryContainer = Color(0xFF002709),
        onSecondaryContainer = Color(0xFF86EFAC),
        tertiary = Color(0xFF00CC33),
        onTertiary = Color(0xFF001B06),
        background = Color(0xFF000500),
        onBackground = Color(0xFFE0FFE5),
        surface = Color(0xFF000A02),
        onSurface = Color(0xFFE0FFE5),
        surfaceVariant = Color(0xFF001404),
        onSurfaceVariant = Color(0xFF4ADE80),
        outline = Color(0xFF00FF41),
        outlineVariant = Color(0xFF15803D),
        error = Color(0xFFFF2A4A),
        onError = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFF000500),
        surfaceContainerLow = Color(0xFF000701),
        surfaceContainer = Color(0xFF000E03),
        surfaceContainerHigh = Color(0xFF001605),
        surfaceContainerHighest = Color(0xFF002008)
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
                "orange", "cyber_orange", "amber_orange" -> CyberOrange
        "vip_stark_clean" -> VipStarkClean
        "vip_matrix_light" -> VipMatrixLight
        "vip_sunset_amber" -> VipSunsetAmber
        "vip_quantum_lilac" -> VipQuantumLilac
        "vip_arctic_ice" -> VipArcticIce
        "vip_phantom_hacker" -> VipPhantomHacker
        "vip_blood_overdrive" -> VipBloodOverdrive
        "vip_synthwave_2077" -> VipSynthwave2077
        "vip_titanium_monolith" -> VipTitaniumMonolith
        "vip_matrix_terminal_zero" -> VipMatrixTerminalZero
        else -> StarkHud // Default is Stark Cyber HUD from screenshot
    }
}
