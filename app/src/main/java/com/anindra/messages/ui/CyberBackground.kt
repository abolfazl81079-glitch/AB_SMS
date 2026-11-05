package com.anindra.messages.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

/**
 * Authentic Sci-Fi / Cyberpunk Procedural Background Renderer.
 * Supports:
 * - Tech Schematic Blueprint Grid (+ crosshairs)
 * - Circuit Board PCB Traces & Vias
 * - Cyberpunk Skyscraper Cityscape Silhouette
 * - Tactical Carbon Fiber Mesh
 * - Cockpit HUD Blueprint Telemetry
 * - Pure AMOLED Blackout
 */
@Composable
fun CyberBackground(
    backgroundType: String = "default",
    themeMode: String = "stark",
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val isDark = cs.surface.luminance() < 0.5f
    val accentColor = cs.primary

    // Resolve the active background type
    val resolvedType = when {
        backgroundType != "default" -> backgroundType
        themeMode == "vip_military_hud_gold" -> "military_hud"
        themeMode == "vip_tactical_teal_overwatch" -> "military_hud"
        themeMode == "vip_cyber_waveform_stealth" -> "waveform_scope"
        themeMode == "vip_biometric_pulse_orange" -> "waveform_scope"
        themeMode == "vip_holographic_crimson_tape" -> "waveform_scope"
        themeMode == "vip_cyber_mech_deck" -> "circuit"
        themeMode == "vip_tactical_hud_amber" -> "grid"
        themeMode == "vip_classified_crimson" -> "cityscape"
        themeMode == "vip_classified_cyan" -> "cityscape"
        themeMode == "vip_stark_cockpit_hud" -> "hud_blueprint"
        themeMode == "blackout" -> "blackout"
        else -> "none"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .drawBehind {
                if (isDark) {
                    when (resolvedType) {
                        "grid" -> drawTechGrid(accentColor)
                        "circuit" -> drawCircuitTraces(accentColor)
                        "cityscape" -> drawCityscapeSilhouette(accentColor)
                        "carbon" -> drawCarbonFiber()
                        "hud_blueprint" -> drawHudBlueprint(accentColor)
                        "military_hud" -> drawMilitaryHud(accentColor, cs.secondary)
                        "waveform_scope" -> drawWaveformScope(accentColor, cs.secondary)
                    }
                }
            }
    ) {
        content()
    }
}

/**
 * 1. Technical Schematic Blueprint Grid with coordinate tick marks and crosshairs.
 */
private fun DrawScope.drawTechGrid(accent: Color) {
    val step = 40.dp.toPx()
    val w = size.width
    val h = size.height

    val gridColor = accent.copy(alpha = 0.055f)
    val crossColor = accent.copy(alpha = 0.16f)

    // Vertical grid lines
    var x = step / 2f
    while (x < w) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 0.8f
        )
        x += step
    }

    // Horizontal grid lines
    var y = step / 2f
    while (y < h) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 0.8f
        )
        y += step
    }

    // Crosshairs (+) at key intersections
    var cx = step * 1.5f
    while (cx < w) {
        var cy = step * 2.5f
        while (cy < h) {
            val arm = 4.dp.toPx()
            drawLine(crossColor, Offset(cx - arm, cy), Offset(cx + arm, cy), 1.2f)
            drawLine(crossColor, Offset(cx, cy - arm), Offset(cx, cy + arm), 1.2f)
            cy += step * 4f
        }
        cx += step * 3f
    }
}

/**
 * 2. Electronic PCB Circuit Board Traces with 45-degree angle bends and circular solder vias.
 */
private fun DrawScope.drawCircuitTraces(accent: Color) {
    val traceColor = accent.copy(alpha = 0.085f)
    val viaColor = accent.copy(alpha = 0.14f)
    val w = size.width
    val h = size.height

    // Bus line 1 (left to center-down)
    val path1 = Path().apply {
        moveTo(w * 0.08f, 0f)
        lineTo(w * 0.08f, h * 0.22f)
        lineTo(w * 0.22f, h * 0.36f)
        lineTo(w * 0.22f, h * 0.58f)
        lineTo(w * 0.12f, h * 0.68f)
        lineTo(w * 0.12f, h)
    }
    drawPath(path1, traceColor, style = Stroke(width = 1.2f))

    // Bus line 2 (right side to bottom)
    val path2 = Path().apply {
        moveTo(w * 0.92f, 0f)
        lineTo(w * 0.92f, h * 0.18f)
        lineTo(w * 0.78f, h * 0.32f)
        lineTo(w * 0.78f, h * 0.65f)
        lineTo(w * 0.88f, h * 0.75f)
        lineTo(w * 0.88f, h)
    }
    drawPath(path2, traceColor, style = Stroke(width = 1.2f))

    // Diagonal branch lines
    val path3 = Path().apply {
        moveTo(w * 0.22f, h * 0.44f)
        lineTo(w * 0.45f, h * 0.44f)
        lineTo(w * 0.55f, h * 0.54f)
        lineTo(w * 0.78f, h * 0.54f)
    }
    drawPath(path3, traceColor, style = Stroke(width = 1.0f))

    // Circular solder vias at junctions
    val viaRadius = 2.5.dp.toPx()
    val viaInnerRadius = 1.0.dp.toPx()
    val viaPoints = listOf(
        Offset(w * 0.08f, h * 0.22f),
        Offset(w * 0.22f, h * 0.36f),
        Offset(w * 0.22f, h * 0.44f),
        Offset(w * 0.55f, h * 0.54f),
        Offset(w * 0.78f, h * 0.54f),
        Offset(w * 0.78f, h * 0.32f),
        Offset(w * 0.92f, h * 0.18f)
    )
    for (pt in viaPoints) {
        drawCircle(viaColor, radius = viaRadius, center = pt, style = Stroke(width = 1.2f))
        drawCircle(viaColor, radius = viaInnerRadius, center = pt)
    }
}

/**
 * 3. Cyberpunk Skyscraper Cityscape Silhouette with glowing window matrix and radar reticle.
 * (Directly recreates the background of media_1793885055667_a500a260.jpg)
 */
private fun DrawScope.drawCityscapeSilhouette(accent: Color) {
    val w = size.width
    val h = size.height

    // 1. Radar targeting circle in upper center
    val radarCenter = Offset(w * 0.50f, h * 0.32f)
    val r1 = 60.dp.toPx()
    val r2 = 90.dp.toPx()
    val radarColor = accent.copy(alpha = 0.05f)
    val radarBright = accent.copy(alpha = 0.10f)

    drawCircle(radarColor, radius = r1, center = radarCenter, style = Stroke(width = 1.0f))
    drawCircle(radarBright, radius = r2, center = radarCenter, style = Stroke(width = 1.0f))
    drawLine(radarColor, Offset(radarCenter.x - r2 - 10f, radarCenter.y), Offset(radarCenter.x + r2 + 10f, radarCenter.y), 0.8f)
    drawLine(radarColor, Offset(radarCenter.x, radarCenter.y - r2 - 10f), Offset(radarCenter.x, radarCenter.y + r2 + 10f), 0.8f)

    // 2. Skyscraper silhouettes in bottom half
    val bldgBase = h
    val buildingColor = Color.Black.copy(alpha = 0.25f)
    val bldgOutline = accent.copy(alpha = 0.08f)
    val windowColor = accent.copy(alpha = 0.12f)

    // Buildings specifications: (leftRatio, widthRatio, heightRatio)
    val buildings = listOf(
        Triple(0.00f, 0.18f, 0.35f),
        Triple(0.16f, 0.14f, 0.48f),
        Triple(0.28f, 0.16f, 0.28f),
        Triple(0.42f, 0.18f, 0.52f), // Tower
        Triple(0.58f, 0.15f, 0.38f),
        Triple(0.71f, 0.14f, 0.44f),
        Triple(0.83f, 0.17f, 0.32f)
    )

    for ((xRatio, wRatio, hRatio) in buildings) {
        val bx = w * xRatio
        val bw = w * wRatio
        val bh = h * hRatio
        val by = bldgBase - bh

        // Building mass
        drawRect(buildingColor, Offset(bx, by), Size(bw, bh))
        // Top edge outline
        drawLine(bldgOutline, Offset(bx, by), Offset(bx + bw, by), 1.2f)
        drawLine(bldgOutline, Offset(bx, by), Offset(bx, bldgBase), 0.8f)
        drawLine(bldgOutline, Offset(bx + bw, by), Offset(bx + bw, bldgBase), 0.8f)

        // Matrix windows inside building
        var winY = by + 12.dp.toPx()
        while (winY < bldgBase - 20.dp.toPx()) {
            var winX = bx + 6.dp.toPx()
            while (winX < bx + bw - 6.dp.toPx()) {
                drawRect(windowColor, Offset(winX, winY), Size(2.dp.toPx(), 3.dp.toPx()))
                winX += 7.dp.toPx()
            }
            winY += 9.dp.toPx()
        }
    }
}

/**
 * 4. Tactical Carbon Fiber Diagonal Mesh.
 */
private fun DrawScope.drawCarbonFiber() {
    val step = 8.dp.toPx()
    val w = size.width
    val h = size.height
    val c1 = Color.White.copy(alpha = 0.018f)

    var offset = -h
    while (offset < w + h) {
        drawLine(c1, Offset(offset, 0f), Offset(offset + h, h), strokeWidth = 1.5f)
        offset += step
    }
}

/**
 * 5. HUD Cockpit Blueprint with perimeter digital rulers and telemetry arcs.
 */
private fun DrawScope.drawHudBlueprint(accent: Color) {
    val w = size.width
    val h = size.height
    val lineCol = accent.copy(alpha = 0.07f)
    val tickCol = accent.copy(alpha = 0.14f)

    // Left digital ruler ticks
    var y = 20.dp.toPx()
    while (y < h - 20.dp.toPx()) {
        val tickLen = if ((y.toInt() / 20) % 5 == 0) 10.dp.toPx() else 4.dp.toPx()
        drawLine(tickCol, Offset(2.dp.toPx(), y), Offset(2.dp.toPx() + tickLen, y), 1.0f)
        drawLine(tickCol, Offset(w - 2.dp.toPx(), y), Offset(w - 2.dp.toPx() - tickLen, y), 1.0f)
        y += 8.dp.toPx()
    }

    // Top and bottom telemetry bracket arcs
    drawCircle(
        color = lineCol,
        radius = w * 0.45f,
        center = Offset(w * 0.5f, 0f),
        style = Stroke(width = 1.0f)
    )
}

/**
 * 6. Tactical Military HUD Background (Recreated from Images 4 & 5).
 * Features:
 * - Tactical perimeter polygon border with tick marks
 * - Circular targeting radar reticle with crosshairs
 * - Glowing conduit cable connecting interface modules
 * - World map telemetry silhouette
 * - System information indicators
 */
private fun DrawScope.drawMilitaryHud(accent: Color, secondary: Color) {
    val w = size.width
    val h = size.height
    val frameCol = accent.copy(alpha = 0.08f)
    val radarCol = secondary.copy(alpha = 0.12f)
    val conduitCol = accent.copy(alpha = 0.15f)

    // 1. Tactical perimeter angular boundary lines
    val margin = 8.dp.toPx()
    val chamfer = 24.dp.toPx()
    val framePath = Path().apply {
        moveTo(margin + chamfer, margin)
        lineTo(w - margin - chamfer, margin)
        lineTo(w - margin, margin + chamfer)
        lineTo(w - margin, h - margin - chamfer)
        lineTo(w - margin - chamfer, h - margin)
        lineTo(margin + chamfer, h - margin)
        lineTo(margin, h - margin - chamfer)
        lineTo(margin, margin + chamfer)
        close()
    }
    drawPath(framePath, frameCol, style = Stroke(width = 1.2f))

    // 2. Circular Targeting Radar Reticle in upper-right
    val radarCenter = Offset(w * 0.78f, h * 0.22f)
    val rRadius = 42.dp.toPx()
    drawCircle(radarCol, radius = rRadius, center = radarCenter, style = Stroke(width = 1.0f))
    drawCircle(radarCol, radius = rRadius * 0.60f, center = radarCenter, style = Stroke(width = 0.8f))
    drawCircle(accent.copy(alpha = 0.35f), radius = 3.dp.toPx(), center = radarCenter) // Target lock dot
    // Crosshairs
    drawLine(radarCol, Offset(radarCenter.x - rRadius - 6.dp.toPx(), radarCenter.y), Offset(radarCenter.x + rRadius + 6.dp.toPx(), radarCenter.y), 0.8f)
    drawLine(radarCol, Offset(radarCenter.x, radarCenter.y - rRadius - 6.dp.toPx()), Offset(radarCenter.x, radarCenter.y + rRadius + 6.dp.toPx()), 0.8f)

    // 3. Illuminated Conduit Cable curling across components (from Images 4 & 5)
    val conduitPath = Path().apply {
        moveTo(w * 0.15f, h * 0.35f)
        cubicTo(w * 0.08f, h * 0.42f, w * 0.22f, h * 0.48f, w * 0.12f, h * 0.55f)
        lineTo(w * 0.12f, h * 0.62f)
        lineTo(w * 0.28f, h * 0.62f)
    }
    drawPath(conduitPath, conduitCol, style = Stroke(width = 1.5f))
    drawCircle(accent.copy(alpha = 0.45f), radius = 3.5.dp.toPx(), center = Offset(w * 0.28f, h * 0.62f))

    // 4. Stylized Tactical World Map landmass silhouette in lower portion
    val mapBaseY = h * 0.80f
    val mapCol = accent.copy(alpha = 0.06f)
    // Americas
    drawRect(mapCol, Offset(w * 0.18f, mapBaseY - 30.dp.toPx()), Size(w * 0.14f, 22.dp.toPx()))
    drawRect(mapCol, Offset(w * 0.22f, mapBaseY - 5.dp.toPx()), Size(w * 0.10f, 28.dp.toPx()))
    // Eurasia / Africa
    drawRect(mapCol, Offset(w * 0.42f, mapBaseY - 36.dp.toPx()), Size(w * 0.35f, 26.dp.toPx()))
    drawRect(mapCol, Offset(w * 0.48f, mapBaseY - 8.dp.toPx()), Size(w * 0.16f, 32.dp.toPx()))
    // Target marker over region
    drawCircle(secondary.copy(alpha = 0.30f), radius = 7.dp.toPx(), center = Offset(w * 0.55f, mapBaseY - 14.dp.toPx()), style = Stroke(1.0f))

    // 5. Digital telemetry boxes [01] [54] [28] at bottom left
    var boxX = margin + 12.dp.toPx()
    val boxY = h - margin - 22.dp.toPx()
    val boxW = 18.dp.toPx()
    val boxH = 12.dp.toPx()
    val boxCol = accent.copy(alpha = 0.10f)
    for (i in 0..2) {
        drawRect(boxCol, Offset(boxX, boxY), Size(boxW, boxH))
        drawRect(accent.copy(alpha = 0.20f), Offset(boxX, boxY), Size(boxW, boxH), style = Stroke(0.8f))
        boxX += boxW + 4.dp.toPx()
    }
}

/**
 * 7. Cyber Waveform & Oscilloscope Background (Recreated from Image 2).
 * Features:
 * - Illuminated sine wave oscilloscope waveform
 * - ECG heartbeat pulse telemetry
 * - LED matrix dot grid
 * - Warning hazard indicator blocks
 */
private fun DrawScope.drawWaveformScope(accent: Color, secondary: Color) {
    val w = size.width
    val h = size.height
    val scopeCol = secondary.copy(alpha = 0.14f)
    val ledCol = Color.White.copy(alpha = 0.08f)
    val wireCol = accent.copy(alpha = 0.12f)

    // 1. Oscilloscope Sine Wave Trace in middle section
    val waveY = h * 0.32f
    val wavePath = Path().apply {
        moveTo(w * 0.05f, waveY)
        val segments = 8
        val segW = (w * 0.90f) / segments
        var curX = w * 0.05f
        for (i in 0 until segments) {
            val amp = if (i % 2 == 0) -16.dp.toPx() else 16.dp.toPx()
            cubicTo(
                curX + segW * 0.33f, waveY + amp,
                curX + segW * 0.66f, waveY + amp,
                curX + segW, waveY
            )
            curX += segW
        }
    }
    drawPath(wavePath, scopeCol, style = Stroke(width = 1.4f))

    // 2. ECG Heartbeat Pulse Trace below sine wave (from Image 2 left phone)
    val ecgY = h * 0.68f
    val ecgPath = Path().apply {
        moveTo(w * 0.08f, ecgY)
        lineTo(w * 0.35f, ecgY)
        lineTo(w * 0.38f, ecgY - 6.dp.toPx())
        lineTo(w * 0.41f, ecgY + 8.dp.toPx())
        lineTo(w * 0.44f, ecgY - 26.dp.toPx()) // QRS peak
        lineTo(w * 0.47f, ecgY + 12.dp.toPx())
        lineTo(w * 0.50f, ecgY - 4.dp.toPx())
        lineTo(w * 0.53f, ecgY)
        lineTo(w * 0.92f, ecgY)
    }
    drawPath(ecgPath, accent.copy(alpha = 0.20f), style = Stroke(width = 1.5f))

    // 3. LED Array Matrix (from Image 2 right phone)
    val matrixStartX = w * 0.20f
    val matrixStartY = h * 0.50f
    val dotStep = 7.dp.toPx()
    val dotR = 1.2.dp.toPx()
    for (r in 0..2) {
        for (c in 0..15) {
            drawCircle(ledCol, radius = dotR, center = Offset(matrixStartX + c * dotStep, matrixStartY + r * dotStep))
        }
    }

    // 4. Connecting wire conduit with illuminated termination
    val wirePath = Path().apply {
        moveTo(w * 0.12f, waveY)
        lineTo(w * 0.12f, h * 0.48f)
        lineTo(matrixStartX - 6.dp.toPx(), h * 0.48f)
    }
    drawPath(wirePath, wireCol, style = Stroke(width = 1.2f))
    drawCircle(secondary.copy(alpha = 0.40f), radius = 3.dp.toPx(), center = Offset(matrixStartX - 6.dp.toPx(), h * 0.48f))
}
