package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PartnerBlue
import com.example.ui.theme.PartnerYellow
import com.example.ui.theme.StatusGreen

@Composable
fun GpsRouteMapView(
    distanceText: String = "11.2 km",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEAF2FD))
            .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(16.dp))
    ) {
        // Map Grid, Roads & Route Polyline Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Grid terrain background lines
            val gridColor = Color(0xFFD7E6FA)
            val step = 32.dp.toPx()
            var x = 0f
            while (x < width) {
                drawLine(gridColor, Offset(x, 0f), Offset(x, height), strokeWidth = 1.dp.toPx())
                x += step
            }
            var y = 0f
            while (y < height) {
                drawLine(gridColor, Offset(0f, y), Offset(width, y), strokeWidth = 1.dp.toPx())
                y += step
            }

            // 2. Secondary gray roads
            val roadColor = Color(0xFFCFDEF2)
            drawLine(roadColor, Offset(0f, height * 0.35f), Offset(width, height * 0.45f), strokeWidth = 5.dp.toPx())
            drawLine(roadColor, Offset(width * 0.6f, 0f), Offset(width * 0.75f, height), strokeWidth = 4.dp.toPx())
            drawLine(roadColor, Offset(0f, height * 0.85f), Offset(width * 0.8f, height * 0.7f), strokeWidth = 4.dp.toPx())

            // 3. Primary Route Polyline (Bold Royal Blue)
            val path = Path().apply {
                moveTo(width * 0.15f, height * 0.72f) // Point A
                cubicTo(
                    width * 0.28f, height * 0.65f,
                    width * 0.35f, height * 0.38f,
                    width * 0.52f, height * 0.42f
                )
                cubicTo(
                    width * 0.68f, height * 0.46f,
                    width * 0.75f, height * 0.22f,
                    width * 0.86f, height * 0.26f // Point B
                )
            }

            // Route casing shadow
            drawPath(
                path = path,
                color = PartnerBlue.copy(alpha = 0.2f),
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )

            // Route solid line
            drawPath(
                path = path,
                color = PartnerBlue,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
            )

            // Waypoint A Outer Pulse & Core
            drawCircle(
                color = Color.White,
                radius = 9.dp.toPx(),
                center = Offset(width * 0.15f, height * 0.72f)
            )
            drawCircle(
                color = PartnerBlue,
                radius = 6.dp.toPx(),
                center = Offset(width * 0.15f, height * 0.72f)
            )

            // Waypoint B Outer Pulse & Core (Destination)
            drawCircle(
                color = Color.White,
                radius = 9.dp.toPx(),
                center = Offset(width * 0.86f, height * 0.26f)
            )
            drawCircle(
                color = StatusGreen,
                radius = 6.dp.toPx(),
                center = Offset(width * 0.86f, height * 0.26f)
            )
        }

        // Floating Truck Marker on Route
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-10).dp, y = (-12).dp)
                .size(34.dp)
                .shadow(elevation = 6.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(PartnerYellow)
                .border(2.dp, Color(0xFF1E293B), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalShipping,
                contentDescription = "EV Cargo Truck",
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(18.dp)
            )
        }

        // Top Floating Live Route Pill
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .shadow(4.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.95f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(PartnerBlue)
            )
            Text(
                text = "Live GPS Route • $distanceText",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
        }

        // Top Right Compass Icon
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(36.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFFE2E8F0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = "Navigation Compass",
                tint = PartnerBlue,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
