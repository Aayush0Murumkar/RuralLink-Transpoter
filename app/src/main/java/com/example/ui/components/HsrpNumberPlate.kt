package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HsrpNumberPlate(
    plateNumber: String,
    modifier: Modifier = Modifier,
    rtoLocation: String = "MH RTO NASHIK • HSRP GOVT REGISTRATION",
    serialCode: String = "AA204891002"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFCC00),
                        Color(0xFFFFB800),
                        Color(0xFFE5A600)
                    )
                )
            )
            .border(
                width = 3.5.dp,
                color = Color(0xFF2E2713),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        // Screw holes in four corners
        ScrewHole(Modifier.align(Alignment.TopStart))
        ScrewHole(Modifier.align(Alignment.TopEnd))
        ScrewHole(Modifier.align(Alignment.BottomStart))
        ScrewHole(Modifier.align(Alignment.BottomEnd))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left IND emblem & holographic serial
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .width(42.dp)
            ) {
                // Chakra / IND blue circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E8F0))
                        .border(1.dp, Color(0xFF1E3A8A), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A8A).copy(alpha = 0.85f))
                    )
                }
                Text(
                    text = "IND",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E3A8A),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = serialCode,
                    fontSize = 5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }

            // Center Registration Number
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Main Embossed Plate Number
                Text(
                    text = formatPlateNumber(plateNumber),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp,
                    color = Color(0xFF111827),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                
                // Micro subtext
                Text(
                    text = rtoLocation,
                    fontSize = 6.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF332A15),
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            // Right Green EV badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF047857))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "EV",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(10.dp)
                        )
                    }
                    Text(
                        text = "15-EV-9042",
                        fontSize = 5.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD1FAE5)
                    )
                }
            }
        }
    }
}

@Composable
private fun ScrewHole(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF94A3B8))
            .border(1.dp, Color(0xFF475569), CircleShape)
    )
}

private fun formatPlateNumber(raw: String): String {
    val clean = raw.replace(" ", "").replace("-", "")
    if (clean.length >= 10) {
        return "${clean.take(2)} - ${clean.substring(2, 4)} - ${clean.substring(4, 6)} - ${clean.substring(6)}"
    }
    return raw
}
