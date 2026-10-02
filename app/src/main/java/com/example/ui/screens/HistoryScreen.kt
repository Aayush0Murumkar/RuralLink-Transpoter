package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TripHistoryItem
import com.example.data.model.WeeklyDayEarning
import com.example.ui.theme.*

@Composable
fun HistoryScreen(
    period: String, // "today", "week"
    filter: String, // "all", "farm", "artisan"
    searchQuery: String,
    items: List<TripHistoryItem>,
    weeklyEarnings: List<WeeklyDayEarning>,
    todayEarnings: Int,
    todayDistanceKm: Double,
    todayJobsCount: Int,
    onPeriodChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onSearchChange: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Blue Stats Card
        item {
            HistoryHeaderCard(
                period = period,
                todayEarnings = todayEarnings,
                todayDistanceKm = todayDistanceKm,
                todayJobsCount = todayJobsCount,
                onPeriodChange = onPeriodChange
            )
        }

        if (period == "today") {
            // Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChipItem(
                        label = "📦 All Products",
                        isSelected = filter == "all",
                        onClick = { onFilterChange("all") }
                    )
                    FilterChipItem(
                        label = "🌾 Farm Produce",
                        isSelected = filter == "farm",
                        onClick = { onFilterChange("farm") }
                    )
                    FilterChipItem(
                        label = "🌱 Agri Inputs",
                        isSelected = filter == "agri",
                        onClick = { onFilterChange("agri") }
                    )
                    FilterChipItem(
                        label = "🎨 Artisan Goods",
                        isSelected = filter == "artisan",
                        onClick = { onFilterChange("artisan") }
                    )
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    placeholder = {
                        Text(
                            text = "🔍 Search village, farmer, or tracking ID...",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF64748B)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = PartnerBlue,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    singleLine = true
                )
            }

            // Deliveries List
            val filteredList = items.filter { item ->
                val matchesFilter = when (filter) {
                    "farm" -> item.category == "FARM_PRODUCE"
                    "agri" -> item.category == "AGRI_INPUTS"
                    "artisan" -> item.category == "ARTISAN"
                    else -> true
                }
                val matchesSearch = searchQuery.isBlank() ||
                        item.title.contains(searchQuery, ignoreCase = true) ||
                        item.supplierOrFarm.contains(searchQuery, ignoreCase = true) ||
                        item.id.contains(searchQuery, ignoreCase = true)
                matchesFilter && matchesSearch
            }

            items(filteredList) { trip ->
                TripHistoryCard(trip = trip)
            }
        } else {
            // Weekly Breakdown View
            item {
                Text(
                    text = "📈 WEEKLY EARNINGS RECORD",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
            }

            items(weeklyEarnings) { day ->
                WeeklyEarningCard(day = day)
            }
        }
    }
}

@Composable
private fun HistoryHeaderCard(
    period: String,
    todayEarnings: Int,
    todayDistanceKm: Double,
    todayJobsCount: Int,
    onPeriodChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(PartnerBlue, Color(0xFF1D4ED8))
                )
            )
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Top Row: Title + Period Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "📜 AUDIT & TRANSACTIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Trip History & Payouts 💰",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Switcher: Today | Week
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (period == "today") PartnerYellow else Color.Transparent)
                            .clickable { onPeriodChange("today") }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "☀️ Today",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (period == "today") Color(0xFF1E293B) else Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (period == "week") PartnerYellow else Color.Transparent)
                            .clickable { onPeriodChange("week") }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "📅 Week",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (period == "week") Color(0xFF1E293B) else Color.White
                        )
                    }
                }
            }

            // Metrics Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "💰 EARNINGS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = "₹$todayEarnings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp)
                        .background(Color.White.copy(alpha = 0.25f))
                )

                Column {
                    Text(
                        text = "🛣️ DISTANCE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = "$todayDistanceKm km",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp)
                        .background(Color.White.copy(alpha = 0.25f))
                )

                Column {
                    Text(
                        text = "📦 JOBS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = "$todayJobsCount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PartnerBlue else Color.White)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF475569)
        )
    }
}

@Composable
private fun TripHistoryCard(trip: TripHistoryItem) {
    val statusText = when (trip.status) {
        "ACCEPTED" -> "🚚 ACCEPTED"
        "DELIVERED" -> "✅ DELIVERED"
        else -> "📦 ${trip.status}"
    }

    val statusBg = when (trip.status) {
        "ACCEPTED" -> PartnerYellowLight
        "DELIVERED" -> Color(0xFFDCFCE7)
        else -> Color(0xFFF1F5F9)
    }

    val statusFg = when (trip.status) {
        "ACCEPTED" -> PartnerYellowText
        "DELIVERED" -> Color(0xFF15803D)
        else -> Color(0xFF475569)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = trip.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "💰 +₹${trip.payout}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF15803D)
                )
            }

            // Subtitle & Status Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🏷️ ${trip.id} • ${trip.supplierOrFarm}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusText,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusFg
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            // Route & Distance Details
            Text(
                text = "📍 ${trip.fromLocation} ➔ 🏁 ${trip.toLocation} • 🛣️ ${trip.distanceKm} km • ⚖️ ${trip.weightKg} kg",
                fontSize = 11.sp,
                color = Color(0xFF475569)
            )
        }
    }
}

@Composable
private fun WeeklyEarningCard(day: WeeklyDayEarning) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "📅 ${day.dayName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (day.isToday) PartnerBlue else Color(0xFF0F172A)
                    )
                    if (day.isToday) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(PartnerYellowLight)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🌟 ACTIVE TODAY",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = PartnerYellowText
                            )
                        }
                    }
                }
                Text(
                    text = "📦 ${day.ordersCount} orders • 🛣️ ${day.distanceKm} km",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "💰 ₹${day.earnings}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF15803D)
                )
                Text(
                    text = "🎁 Bonus +₹${day.bonus}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF166534)
                )
            }
        }
    }
}
