package com.anindra.messages.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.Settings
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anindra.messages.R

/**
 * Animated Cyberpunk Terminal Header for AB/SMS.
 * Displays neon scan effects, pulsing terminal status indicator, and futuristic branding.
 */
@Composable
fun AnimatedCyberHeader(
    onSearchClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit,
    showArchiveButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cyber_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "neon_pulse"
    )

    val scanOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scan_offset"
    )

    val cs = MaterialTheme.colorScheme
    val hudPrimary = cs.primary
    val hudSecondary = cs.secondary
    val deepBlack = cs.background

    Surface(
        color = cs.surface.copy(alpha = 0.92f),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, hudPrimary.copy(alpha = 0.5f * pulseAlpha)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            hudPrimary.copy(alpha = 0.08f),
                            hudSecondary.copy(alpha = 0.04f),
                            hudPrimary.copy(alpha = 0.08f)
                        )
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Logo & Identity
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Futuristic HUD Cassette / Terminal Box
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .border(1.dp, hudPrimary.copy(alpha = pulseAlpha), RoundedCornerShape(2.dp))
                            .background(deepBlack),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AB",
                            color = hudPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.alpha(pulseAlpha)
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AB/SMS",
                                color = cs.onSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(Modifier.width(6.dp))
                            // Status indicator pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(hudPrimary.copy(alpha = 0.2f))
                                    .border(0.5.dp, hudPrimary, RoundedCornerShape(2.dp))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "HUD // ONLINE",
                                    color = hudPrimary,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.alpha(pulseAlpha)
                                )
                            }
                        }
                        Text(
                            text = "STARK TERMINAL // ENCRYPTED",
                            color = cs.onSurfaceVariant,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Right: Action Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            tint = hudPrimary,
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
                            tint = hudSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
