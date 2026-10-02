cat << 'INNER_EOF' >> app/src/main/java/com/example/ui/components/DeliveryOfferDialog.kt

@Composable
fun PricingBreakdownView(job: DeliveryJob) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "PRICING & CAPACITY BREAKDOWN",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF64748B),
                letterSpacing = 0.5.sp
            )
            
            // Shared Capacity
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Capacity Share", fontSize = 12.sp, color = Color(0xFF475569))
                Text("${job.capacitySharePercent}% of truck utilized", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            }
            
            // Fuel Cost estimated
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Est. Fuel Cost (Diesel)", fontSize = 12.sp, color = Color(0xFF475569))
                Text("₹${job.fuelCostEstimated}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFFDC2626))
            }
            
            // Backload / Return Trip Opportunity
            if (job.isReturnTrip) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Return-Trip Matching", fontSize = 12.sp, color = Color(0xFF475569))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFEF08A))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("25% Backload Discount Applied", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF854D0E))
                    }
                }
            }
        }
    }
}
INNER_EOF
