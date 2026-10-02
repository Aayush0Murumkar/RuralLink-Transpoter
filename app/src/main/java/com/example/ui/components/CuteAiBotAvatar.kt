package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CardBorder
import com.example.ui.theme.PartnerYellow
import com.example.ui.theme.TextPrimary

/**
 * Minimalist AI Chat & Assistant icon inspired by chat-ai.png.
 * Features a clean rounded speech bubble with a 4-pointed sparkle star on top-right,
 * rendered with clean modern monochromatic/accent colors.
 */
@Composable
fun CuteAiBotAvatar(
    size: Dp = 32.dp,
    modifier: Modifier = Modifier
) {
    val cornerRadius = size * 0.32f
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(Color(0xFFF8FAFC))
            .border(1.dp, CardBorder, shape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(size * 0.76f)
        ) {
            val w = this.size.width
            val h = this.size.height
            val strokeW = w * 0.10f
            val color = Color(0xFF0F172A)

            // 1. Speech Bubble Path
            val centerX = w * 0.44f
            val centerY = h * 0.48f
            val r = w * 0.38f

            val bubblePath = Path().apply {
                arcTo(
                    rect = Rect(centerX - r, centerY - r, centerX + r, centerY + r),
                    startAngleDegrees = 32f,
                    sweepAngleDegrees = 260f,
                    forceMoveTo = true
                )
                lineTo(w * 0.10f, h * 0.94f)
                cubicTo(
                    w * 0.20f, h * 0.90f,
                    w * 0.32f, h * 0.88f,
                    w * 0.40f, h * 0.85f
                )
            }

            drawPath(
                path = bubblePath,
                color = color,
                style = Stroke(
                    width = strokeW,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            val rightArcPath = Path().apply {
                arcTo(
                    rect = Rect(centerX - r, centerY - r, centerX + r, centerY + r),
                    startAngleDegrees = 15f,
                    sweepAngleDegrees = 22f,
                    forceMoveTo = true
                )
            }
            drawPath(
                path = rightArcPath,
                color = color,
                style = Stroke(
                    width = strokeW,
                    cap = StrokeCap.Round
                )
            )

            // 2. Four-pointed AI Sparkle Star (top right)
            val starCenterX = w * 0.74f
            val starCenterY = h * 0.26f
            val starOuterR = w * 0.22f
            val starInnerR = w * 0.08f

            val sparklePath = Path().apply {
                moveTo(starCenterX, starCenterY - starOuterR)
                cubicTo(
                    starCenterX + starInnerR, starCenterY - starInnerR,
                    starCenterX + starInnerR, starCenterY - starInnerR,
                    starCenterX + starOuterR, starCenterY
                )
                cubicTo(
                    starCenterX + starInnerR, starCenterY + starInnerR,
                    starCenterX + starInnerR, starCenterY + starInnerR,
                    starCenterX, starCenterY + starOuterR
                )
                cubicTo(
                    starCenterX - starInnerR, starCenterY + starInnerR,
                    starCenterX - starInnerR, starCenterY + starInnerR,
                    starCenterX - starOuterR, starCenterY
                )
                cubicTo(
                    starCenterX - starInnerR, starCenterY - starInnerR,
                    starCenterX - starInnerR, starCenterY - starInnerR,
                    starCenterX, starCenterY - starOuterR
                )
                close()
            }

            drawPath(
                path = sparklePath,
                color = PartnerYellow,
                style = Fill
            )
            drawPath(
                path = sparklePath,
                color = color,
                style = Stroke(
                    width = strokeW * 0.95f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // 3. Two Interior Dots
            val dotRadius = w * 0.065f
            drawCircle(
                color = color,
                radius = dotRadius,
                center = Offset(w * 0.32f, h * 0.48f)
            )
            drawCircle(
                color = color,
                radius = dotRadius,
                center = Offset(w * 0.54f, h * 0.48f)
            )
        }
    }
}
