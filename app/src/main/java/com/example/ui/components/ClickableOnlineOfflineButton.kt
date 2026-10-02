package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClickableOnlineOfflineButton(
    isOnline: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isOnline) Color(0xFFECFDF5) else Color(0xFFF1F5F9),
        label = "bg_color"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isOnline) Color(0xFF10B981) else Color(0xFFCBD5E1),
        label = "border_color"
    )
    val textColor by animateColorAsState(
        targetValue = if (isOnline) Color(0xFF047857) else Color(0xFF64748B),
        label = "text_color"
    )
    val dotColor by animateColorAsState(
        targetValue = if (isOnline) Color(0xFF10B981) else Color(0xFF94A3B8),
        label = "dot_color"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = BorderStroke(1.5.dp, borderColor),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onToggle(!isOnline) }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Animated Status Dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )

            // Lightning Bolt Icon
            Icon(
                imageVector = if (isOnline) Icons.Default.Bolt else Icons.Default.PowerSettingsNew,
                contentDescription = if (isOnline) "Online" else "Offline",
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )

            // Status Label
            Text(
                text = if (isOnline) "Online" else "Offline",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = textColor
            )
        }
    }
}
