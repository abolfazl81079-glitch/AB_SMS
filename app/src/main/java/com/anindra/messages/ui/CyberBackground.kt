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
