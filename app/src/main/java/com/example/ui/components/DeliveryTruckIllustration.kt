package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Animated Last-Mile Logistics EV Delivery Truck Illustration.
 * Features smooth vertical bobbing suspension, rotating multi-spoke wheels,
 * soft headlight glow, pulsing shadow, and moving ground speed particles.
 */
@Composable
fun DeliveryTruckIllustration(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "TruckAnimation")

    // Vertical gentle bobbing motion (simulates road suspension)
    val bobOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TruckBob"
    )

    // Slight chassis tilt/pitch
    val tiltAngle by infiniteTransition.animateFloat(
        initialValue = -1.0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TruckTilt"
    )

    // Continuous wheel rotation
    val wheelRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WheelSpin"
    )

    // Road speed motion dashes
    val roadMotionProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RoadSpeed"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // 1. Road speed motion lines under the truck (moving right-to-left)
        val roadY = height * 0.88f
        val dashWidth = width * 0.18f
        val gap = width * 0.12f
        val totalSpan = dashWidth + gap

        for (i in -1..4) {
            val startX = (i * totalSpan - roadMotionProgress * totalSpan)
            if (startX + dashWidth > 0 && startX < width) {
                drawLine(
                    color = Color.White.copy(alpha = 0.35f),
                    start = Offset(startX.coerceAtLeast(0f), roadY),
                    end = Offset((startX + dashWidth).coerceAtMost(width), roadY),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // Secondary subtle speed streaks behind the truck
        val streakOffset1 = ((1f - roadMotionProgress) * width * 0.4f)
        drawLine(
            color = Color.White.copy(alpha = 0.25f),
            start = Offset(width * 0.05f - streakOffset1 * 0.4f, height * 0.38f),
            end = Offset(width * 0.12f - streakOffset1 * 0.4f, height * 0.38f),
            strokeWidth = 2.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.White.copy(alpha = 0.20f),
            start = Offset(width * 0.02f - streakOffset1 * 0.6f, height * 0.52f),
            end = Offset(width * 0.09f - streakOffset1 * 0.6f, height * 0.52f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // 2. Dynamic Ground Shadow (pulsing slightly with bob)
        val shadowScale = 1f - (bobOffset / 40f)
        val shadowWidth = width * 0.78f * shadowScale
        val shadowHeight = 14.dp.toPx() * shadowScale
        val shadowLeft = (width - shadowWidth) / 2f

        drawOval(
            color = Color(0xFF0F172A).copy(alpha = 0.35f),
            topLeft = Offset(shadowLeft, height * 0.82f - bobOffset * 0.2f),
            size = Size(shadowWidth, shadowHeight)
        )

        // 3. Animated Truck Body with Bobbing & Tilt
        val bodyYOffset = bobOffset
        rotate(
            degrees = tiltAngle,
            pivot = Offset(width * 0.5f, height * 0.6f)
        ) {
            // Front Headlight Light Beam Cone
            val beamPath = Path().apply {
                moveTo(width * 0.86f, height * 0.57f + bodyYOffset)
                lineTo(width * 0.99f, height * 0.50f + bodyYOffset)
                lineTo(width * 0.99f, height * 0.76f + bodyYOffset)
                lineTo(width * 0.86f, height * 0.65f + bodyYOffset)
                close()
            }
            drawPath(
                path = beamPath,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFEF08A).copy(alpha = 0.45f),
                        Color(0xFFFEF08A).copy(alpha = 0.0f)
                    ),
                    startX = width * 0.86f,
                    endX = width * 0.99f
                )
            )

            // Cargo Container Box (White with soft gloss gradient)
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFF1F5F9))
                ),
                topLeft = Offset(width * 0.12f, height * 0.22f + bodyYOffset),
                size = Size(width * 0.52f, height * 0.52f),
                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
            )

            // Cargo Container Texture Lines (Corrugated Panels)
            val panelX1 = width * 0.25f
            val panelX2 = width * 0.38f
            val panelX3 = width * 0.51f
            val panelTop = height * 0.26f + bodyYOffset
            val panelBottom = height * 0.68f + bodyYOffset

            listOf(panelX1, panelX2, panelX3).forEach { px ->
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(px, panelTop),
                    end = Offset(px, panelBottom),
                    strokeWidth = 2.dp.toPx()
                )
            }

            // Cargo Brand Blue Accent Stripe
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF2563EB), Color(0xFF3B82F6))
                ),
                topLeft = Offset(width * 0.14f, height * 0.34f + bodyYOffset),
                size = Size(width * 0.48f, 8.dp.toPx()),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
            )

            // EV Green Lightning Bolt Icon badge on cargo
            val boltPath = Path().apply {
                val bx = width * 0.36f
                val by = height * 0.44f + bodyYOffset
                moveTo(bx + 4.dp.toPx(), by)
                lineTo(bx - 3.dp.toPx(), by + 9.dp.toPx())
                lineTo(bx + 1.dp.toPx(), by + 9.dp.toPx())
                lineTo(bx - 2.dp.toPx(), by + 18.dp.toPx())
                lineTo(bx + 6.dp.toPx(), by + 7.dp.toPx())
                lineTo(bx + 2.dp.toPx(), by + 7.dp.toPx())
                close()
            }
            drawPath(path = boltPath, color = Color(0xFF10B981))

            // Cabin (Vibrant Vermillion Red EV Cabin)
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFEF4444), Color(0xFFDC2626))
                ),
                topLeft = Offset(width * 0.61f, height * 0.32f + bodyYOffset),
                size = Size(width * 0.26f, height * 0.42f),
                cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
            )

            // Front Bumper / Radiator Grill
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(width * 0.84f, height * 0.63f + bodyYOffset),
                size = Size(width * 0.04f, height * 0.11f),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // Cabin Windshield Window (Sky Blue with specular shine)
            drawRoundRect(
                color = Color(0xFF93C5FD),
                topLeft = Offset(width * 0.68f, height * 0.36f + bodyYOffset),
                size = Size(width * 0.17f, height * 0.18f),
                cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx())
            )

            // Windshield Specular Reflection Line
            drawLine(
                color = Color.White.copy(alpha = 0.7f),
                start = Offset(width * 0.72f, height * 0.38f + bodyYOffset),
                end = Offset(width * 0.77f, height * 0.50f + bodyYOffset),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Front Headlight (Bright Amber / Yellow)
            drawRoundRect(
                color = Color(0xFFFDE047),
                topLeft = Offset(width * 0.85f, height * 0.58f + bodyYOffset),
                size = Size(7.dp.toPx(), 11.dp.toPx()),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
            )
        }

        // 4. Wheels with Animated Rotation
        val wheelRadius = 18.dp.toPx()
        val rimRadius = 9.5.dp.toPx()
        val rearWheelCenter = Offset(width * 0.28f, height * 0.75f + bodyYOffset)
        val frontWheelCenter = Offset(width * 0.73f, height * 0.75f + bodyYOffset)

        drawAnimatedWheel(center = rearWheelCenter, radius = wheelRadius, rimRadius = rimRadius, rotationAngle = wheelRotation)
        drawAnimatedWheel(center = frontWheelCenter, radius = wheelRadius, rimRadius = rimRadius, rotationAngle = wheelRotation)
    }
}

/**
 * Draws an interactive spinning wheel with tire tread, metallic rim, hub cap, and 4 rotating spokes.
 */
private fun DrawScope.drawAnimatedWheel(
    center: Offset,
    radius: Float,
    rimRadius: Float,
    rotationAngle: Float
) {
    // Outer Dark Tire
    drawCircle(
        color = Color(0xFF0F172A),
        radius = radius,
        center = center
    )

    // Inner Metallic Rim
    drawCircle(
        color = Color(0xFFE2E8F0),
        radius = rimRadius,
        center = center
    )

    // Rotating Spokes
    rotate(degrees = rotationAngle, pivot = center) {
        val spokeLen = rimRadius * 0.85f
        for (i in 0 until 4) {
            val angleRad = Math.toRadians((i * 90.0))
            val spokeEnd = Offset(
                center.x + (spokeLen * cos(angleRad)).toFloat(),
                center.y + (spokeLen * sin(angleRad)).toFloat()
            )
            drawLine(
                color = Color(0xFF64748B),
                start = center,
                end = spokeEnd,
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }

    // Center Hub Cap
    drawCircle(
        color = Color(0xFF2563EB),
        radius = rimRadius * 0.35f,
        center = center
    )
}

