import re

with open('app/src/main/java/com/example/ui/components/DeliveryOfferDialog.kt', 'r') as f:
    content = f.read()

new_view = """fun PricingBreakdownView(job: DeliveryJob) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "PRICING & CAPACITY BREAKDOWN",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF64748B),
                letterSpacing = 0.5.sp
            )
            
            // Shared Capacity
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Capacity Share", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                    Text("${job.capacitySharePercent}% of truck utilized", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }
                Text("Based on ${job.payloadKg}kg out of 800kg total vehicle capacity", fontSize = 10.sp, color = Color(0xFF64748B))
            }
            
            // Fuel Cost estimated
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Est. Fuel Cost", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                    Text("₹${job.fuelCostEstimated}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFFDC2626))
                }
                Text("@ ₹98.38/L diesel, 10 km/L over ${job.distanceKm}km", fontSize = 10.sp, color = Color(0xFF64748B))
            }

            // Operational & Profit
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Margin & Operating Cost", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                    Text("Included", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF166534))
                }
                Text("Includes 20% profit margin, driver, & maintenance base", fontSize = 10.sp, color = Color(0xFF64748B))
            }
            
            // Backload / Return Trip Opportunity
            if (job.isReturnTrip) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Return-Trip Match", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                        Text("Discount applied to farmer for utilizing return capacity", fontSize = 10.sp, color = Color(0xFF64748B))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFEF08A))
                            .padding(start = 8.dp, end = 6.dp, top = 2.dp, bottom = 2.dp)
                    ) {
                        Text("-25% Farmer Fare", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF854D0E))
                    }
                }
            }
        }
    }
}"""

# regex to replace the function entirely
content = re.sub(r'fun PricingBreakdownView.*?^}', new_view, content, flags=re.MULTILINE|re.DOTALL)

with open('app/src/main/java/com/example/ui/components/DeliveryOfferDialog.kt', 'w') as f:
    f.write(content)

