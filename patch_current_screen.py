import re

with open('app/src/main/java/com/example/ui/screens/CurrentScreen.kt', 'r') as f:
    content = f.read()

# CurrentScreen Signature
sig_orig = """fun CurrentScreen(
    jobState: String, // "idle" or "active"
    job: DeliveryJob,
    onTriggerNewOffer: () -> Unit,
    onFinishJob: () -> Unit,
    onBackoutJob: () -> Unit = {}
)"""
sig_new = """fun CurrentScreen(
    jobState: String, // "idle" or "active"
    job: DeliveryJob,
    onTriggerNewOffer: () -> Unit,
    onFinishJob: () -> Unit,
    onBackoutJob: () -> Unit = {},
    onShowPaymentQR: () -> Unit = {}
)"""
content = content.replace(sig_orig, sig_new)

# CurrentScreen body
body_orig = """            ActiveDeliveryTrackingView(
                job = job,
                onFinishJob = onFinishJob,
                onBackoutJob = onBackoutJob
            )"""
body_new = """            ActiveDeliveryTrackingView(
                job = job,
                onFinishJob = onFinishJob,
                onBackoutJob = onBackoutJob,
                onShowPaymentQR = onShowPaymentQR
            )"""
content = content.replace(body_orig, body_new)

# ActiveDeliveryTrackingView signature
active_sig_orig = """private fun ActiveDeliveryTrackingView(
    job: DeliveryJob,
    onFinishJob: () -> Unit,
    onBackoutJob: () -> Unit = {}
)"""
active_sig_new = """private fun ActiveDeliveryTrackingView(
    job: DeliveryJob,
    onFinishJob: () -> Unit,
    onBackoutJob: () -> Unit = {},
    onShowPaymentQR: () -> Unit = {}
)"""
content = content.replace(active_sig_orig, active_sig_new)

# Buttons in ActiveDeliveryTrackingView
buttons_orig = """                    Button(
                        onClick = onFinishJob,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDCFCE7),
                            contentColor = Color(0xFF15803D)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mark Run Delivered & Settle ₹${job.payoutAmount}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }"""

buttons_new = """                    if (job.status == "ACCEPTED") {
                        Button(
                            onClick = onShowPaymentQR,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFEF08A),
                                contentColor = Color(0xFF854D0E)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle, // Or a QR icon if available
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Show Payment QR to Farmer",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = onFinishJob,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFDCFCE7),
                                contentColor = Color(0xFF15803D)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mark Run Delivered & Settle ₹${job.payoutAmount}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }"""
content = content.replace(buttons_orig, buttons_new)

with open('app/src/main/java/com/example/ui/screens/CurrentScreen.kt', 'w') as f:
    f.write(content)
