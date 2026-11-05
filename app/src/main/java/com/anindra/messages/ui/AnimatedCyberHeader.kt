package com.anindra.messages.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anindra.messages.R

/**
 * High-Tech Cyberpunk Terminal Header for A/SMS.
 * Features an authentic dynamic laser perimeter beam continuously traversing the border,
 * strictly synchronized with active theme colors and contrast-aware in light mode.
 */
@Composable
fun AnimatedCyberHeader(
    onSearchClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit,
    showArchiveButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cyber_header_anim")

    // Laser beam traversing perimeter 360 degrees
    val laserProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "laser_perimeter"
    )

    // Breathing neon pulse
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "neon_pulse"
    )

    val cs = MaterialTheme.colorScheme
    val isDark = cs.surface.luminance() < 0.5f
    val hudPrimary = cs.primary
    val effectiveLaserColor = if (!isDark && hudPrimary.luminance() > 0.65f) Color(0xFF005FB8) else hudPrimary

    Surface(
        color = cs.surfaceContainer.copy(alpha = if (isDark) 0.85f else 0.95f),
        shape = RoundedCornerShape(9.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .drawWithContent {
                drawContent()

                val w = size.width
                val h = size.height
                val cornerRadius = 9.dp.toPx()
                val perimeter = 2 * (w + h)
                val dist = laserProgress * perimeter

                // Calculate laser head position along perimeter (clockwise: top -> right -> bottom -> left)
                val (lx, ly) = when {
                    dist < w -> Pair(dist, 0f)
                    dist < w + h -> Pair(w, dist - w)
                    dist < 2 * w + h -> Pair(w - (dist - (w + h)), h)
                    else -> Pair(0f, h - (dist - (2 * w + h)))
                }

                // 1. Subtle static base border
                drawRoundRect(
                    color = effectiveLaserColor.copy(alpha = if (isDark) 0.25f else 0.40f),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Stroke(width = 1.dp.toPx())
                )

                // 2. High-intensity traveling laser beam with theme radial glow (Smooth beam, circle removed)
                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.90f),
                            effectiveLaserColor.copy(alpha = 0.85f),
                            effectiveLaserColor.copy(alpha = 0.35f),
                            Color.Transparent
                        ),
                        center = Offset(lx, ly),
                        radius = 85.dp.toPx()
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Stroke(width = 2.2.dp.toPx())
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            effectiveLaserColor.copy(alpha = if (isDark) 0.07f else 0.04f),
                            cs.surface.copy(alpha = if (isDark) 0.6f else 0.9f),
                            effectiveLaserColor.copy(alpha = if (isDark) 0.05f else 0.03f)
                        )
                    )
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Clean Minimal Hacker Identity
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Terminal,
                        contentDescription = null,
                        tint = effectiveLaserColor,
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(pulseAlpha)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "A/SMS",
                        color = cs.onSurface,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Right: Action Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (showArchiveButton) {
                        IconButton(
                            onClick = onArchiveClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Archive,
                                contentDescription = stringResource(R.string.conversations_archived),
                                tint = cs.onSurfaceVariant,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = stringResource(R.string.icon_search),
                            tint = effectiveLaserColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = "Settings",
                            tint = cs.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
