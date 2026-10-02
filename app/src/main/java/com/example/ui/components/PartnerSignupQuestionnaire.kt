package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PartnerProfile
import com.example.ui.theme.*

/**
 * Minimalist, aesthetic step-by-step Flashcard Questionnaire for Driver Partner Sign Up & Auth.
 * Includes: Full Name, Email & Phone, Aadhaar Card UIDAI, Vehicle Type, HSRP Number Plate,
 * Address, Location/Cluster, and a working One-Time Sign Up Certification Checkbox.
 */
@Composable
fun PartnerSignupQuestionnaire(
    initialProfile: PartnerProfile,
    onComplete: (PartnerProfile) -> Unit,
    onCancel: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 8

    // Form inputs state
    var name by remember { mutableStateOf(initialProfile.name) }
    var email by remember { mutableStateOf(initialProfile.email) }
    var phone by remember { mutableStateOf(initialProfile.phone) }
    var aadhaarNumber by remember { mutableStateOf(initialProfile.aadhaarNumber) }
    var vehicleType by remember { mutableStateOf(initialProfile.vehicleType) }
    var numberPlate by remember { mutableStateOf(initialProfile.numberPlate) }
    var address by remember { mutableStateOf(initialProfile.address) }
    var locationCluster by remember { mutableStateOf(initialProfile.locationCluster) }
    var agreedToTerms by remember { mutableStateOf(initialProfile.agreedToTerms) }

    // Validation for each step
    val isCurrentStepValid = when (currentStep) {
        0 -> name.trim().length >= 3
        1 -> email.contains("@") && phone.trim().length >= 8
        2 -> aadhaarNumber.replace(" ", "").length >= 12
        3 -> vehicleType.isNotBlank()
        4 -> numberPlate.trim().length >= 6
        5 -> address.trim().length >= 5
        6 -> locationCluster.isNotBlank()
        7 -> agreedToTerms
        else -> true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header & Progress Strip
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(PartnerBlue.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AssignmentInd,
                                contentDescription = null,
                                tint = PartnerBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "PARTNER SIGN UP & AUTH",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PartnerBlue,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "Verified Rural Link Accreditation",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    if (onCancel != null) {
                        TextButton(
                            onClick = onCancel,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Close",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Step Counter & Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Card ${currentStep + 1} of $totalSteps",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${((currentStep + 1) * 100) / totalSteps}% Completed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PartnerBlue
                    )
                }

                LinearProgressIndicator(
                    progress = { (currentStep + 1f) / totalSteps },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PartnerBlue,
                    trackColor = Color(0xFFE2E8F0)
                )

                // Scrollable step pill chips for quick review / navigation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val stepLabels = listOf(
                        "Name", "Contact", "Aadhaar", "Vehicle",
                        "Plate", "Address", "Cluster", "Certify"
                    )
                    stepLabels.forEachIndexed { index, label ->
                        val isSelected = currentStep == index
                        val isCompleted = index < currentStep
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isSelected -> PartnerBlue
                                isCompleted -> Color(0xFFDCFCE7)
                                else -> Color(0xFFF1F5F9)
                            },
                            modifier = Modifier.clickable {
                                currentStep = index
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = StatusGreenText,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                                Text(
                                    text = "${index + 1}. $label",
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = when {
                                        isSelected -> Color.White
                                        isCompleted -> StatusGreenText
                                        else -> Color(0xFF64748B)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Main Animated Flashcard Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { it }
                        ) + fadeIn(animationSpec = tween(300))).togetherWith(
                            slideOutHorizontally(
                                animationSpec = tween(300),
                                targetOffsetX = { -it }
                            ) + fadeOut(animationSpec = tween(300))
                        )
                    } else {
                        (slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { -it }
                        ) + fadeIn(animationSpec = tween(300))).togetherWith(
                            slideOutHorizontally(
                                animationSpec = tween(300),
                                targetOffsetX = { it }
                            ) + fadeOut(animationSpec = tween(300))
                        )
                    }
                },
                label = "FlashcardTransition"
            ) { step ->
                FlashcardContainer(
                    stepNumber = step + 1,
                    totalSteps = totalSteps
                ) {
                    when (step) {
                        0 -> NameQuestionCard(
                            name = name,
                            onNameChange = { name = it }
                        )
                        1 -> ContactQuestionCard(
                            email = email,
                            onEmailChange = { email = it },
                            phone = phone,
                            onPhoneChange = { phone = it }
                        )
                        2 -> AadhaarQuestionCard(
                            aadhaarNumber = aadhaarNumber,
                            onAadhaarChange = { aadhaarNumber = it }
                        )
                        3 -> VehicleTypeQuestionCard(
                            selectedVehicle = vehicleType,
                            onSelectVehicle = { vehicleType = it }
                        )
                        4 -> NumberPlateQuestionCard(
                            numberPlate = numberPlate,
                            onPlateChange = { numberPlate = it.uppercase() }
                        )
                        5 -> AddressQuestionCard(
                            address = address,
                            onAddressChange = { address = it }
                        )
                        6 -> LocationClusterQuestionCard(
                            selectedCluster = locationCluster,
                            onSelectCluster = { locationCluster = it }
                        )
                        7 -> OneTimeSignupCertifyCard(
                            name = name,
                            email = email,
                            phone = phone,
                            aadhaarNumber = aadhaarNumber,
                            vehicleType = vehicleType,
                            numberPlate = numberPlate,
                            address = address,
                            locationCluster = locationCluster,
                            agreedToTerms = agreedToTerms,
                            onAgreedChange = { agreedToTerms = it }
                        )
                    }
                }
            }
        }

        // Bottom Navigation Buttons (Back & Continue / Sign Up)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentStep > 0) {
                OutlinedButton(
                    onClick = { currentStep -= 1 },
                    modifier = Modifier
                        .weight(0.9f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextPrimary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder().copy(
                        brush = Brush.horizontalGradient(listOf(CardBorder, InputBorder))
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Previous",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Button(
                onClick = {
                    if (currentStep < totalSteps - 1) {
                        currentStep += 1
                    } else {
                        // Submit Completed Profile
                        val completedProfile = PartnerProfile(
                            id = initialProfile.id,
                            name = name.trim(),
                            email = email.trim(),
                            phone = phone.trim(),
                            aadhaarNumber = aadhaarNumber.trim(),
                            vehicleType = vehicleType,
                            numberPlate = numberPlate.trim().uppercase(),
                            address = address.trim(),
                            locationCluster = locationCluster,
                            agreedToTerms = agreedToTerms,
                            isSignedUp = true,
                            rating = initialProfile.rating,
                            totalRuns = initialProfile.totalRuns,
                            memberSince = initialProfile.memberSince
                        )
                        onComplete(completedProfile)
                    }
                },
                enabled = isCurrentStepValid,
                modifier = Modifier
                    .weight(if (currentStep > 0) 1.3f else 1f)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PartnerBlue,
                    contentColor = Color.White,
                    disabledContainerColor = PartnerBlue.copy(alpha = 0.4f),
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                )
            ) {
                Text(
                    text = if (currentStep == totalSteps - 1) "Sign Up & Launch Profile" else "Next Question",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = if (currentStep == totalSteps - 1) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Reusable aesthetic card frame with step watermark and scrollable body.
 */
@Composable
private fun FlashcardContainer(
    stepNumber: Int,
    totalSteps: Int,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .shadow(8.dp, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Subtle top corner watermark
            Text(
                text = String.format("%02d", stepNumber),
                fontSize = 58.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFF1F5F9),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// Individual Flashcard Steps
// -----------------------------------------------------------------------------------------

@Composable
private fun CardHeader(
    icon: ImageVector,
    categoryTag: String,
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PartnerBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PartnerBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(PartnerYellowLight)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = categoryTag,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = PartnerYellowText,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Text(
            text = title,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            lineHeight = 24.sp
        )

        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = TextSecondary,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun NameQuestionCard(
    name: String,
    onNameChange: (String) -> Unit
) {
    CardHeader(
        icon = Icons.Default.Person,
        categoryTag = "IDENTITY & PROFILE",
        title = "What is your full legal name?",
        subtitle = "Enter your full name as registered on your Government Driving License or Aadhaar ID."
    )

    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Full Legal Name") },
        placeholder = { Text("e.g. Rameshwar Patil") },
        leadingIcon = {
            Icon(Icons.Default.PersonOutline, contentDescription = null, tint = PartnerBlue)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PartnerBlue,
            unfocusedBorderColor = CardBorder
        ),
        modifier = Modifier.fillMaxWidth()
    )

    // Quick suggestions
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "QUICK SUGGESTIONS",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 0.5.sp
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Ramesh Patil", "Anandrao Shinde", "Dattatraya Gaikwad", "Santosh Kulkarni").forEach { item ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(CardBorder, CardBorder))),
                    modifier = Modifier.clickable { onNameChange(item) }
                ) {
                    Text(
                        text = item,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactQuestionCard(
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit
) {
    CardHeader(
        icon = Icons.Default.Email,
        categoryTag = "COMMUNICATIONS & DISPATCH",
        title = "What is your email & mobile number?",
        subtitle = "We'll send digital dispatch sheets, instant UPI delivery settlements, and OTP notifications."
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email Address") },
            placeholder = { Text("partner@rurallink.in") },
            leadingIcon = {
                Icon(Icons.Default.AlternateEmail, contentDescription = null, tint = PartnerBlue)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PartnerBlue,
                unfocusedBorderColor = CardBorder
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Mobile Phone Number") },
            placeholder = { Text("+91 98901 88234") },
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(start = 12.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🇮🇳 +91", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PartnerBlue,
                unfocusedBorderColor = CardBorder
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AadhaarQuestionCard(
    aadhaarNumber: String,
    onAadhaarChange: (String) -> Unit
) {
    CardHeader(
        icon = Icons.Default.Fingerprint,
        categoryTag = "UIDAI GOVERNMENT KYC",
        title = "Enter your 12-Digit Aadhaar Card Number",
        subtitle = "Used for official driver accreditation, logistics insurance claims, and farmgate co-op trust."
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        OutlinedTextField(
            value = formatAadhaarInput(aadhaarNumber),
            onValueChange = { raw ->
                val digitsOnly = raw.filter { it.isDigit() }.take(12)
                onAadhaarChange(digitsOnly)
            },
            label = { Text("Aadhaar Number (12 Digits)") },
            placeholder = { Text("4829 1048 9201") },
            leadingIcon = {
                Icon(Icons.Default.Security, contentDescription = null, tint = PartnerBlue)
            },
            trailingIcon = {
                if (aadhaarNumber.filter { it.isDigit() }.length == 12) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Valid", tint = StatusGreen)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PartnerBlue,
                unfocusedBorderColor = CardBorder
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Security Guarantee Badge
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEFF6FF),
            border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(PartnerBlueSubtle, PartnerBlueSubtle)))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = PartnerBlue,
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = "UIDAI 256-Bit Encrypted Verification",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = PartnerBlue
                    )
                    Text(
                        text = "Your Aadhaar data is encrypted and validated in compliance with the Government Digital Personal Data Protection Act.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

private fun formatAadhaarInput(raw: String): String {
    val digits = raw.filter { it.isDigit() }.take(12)
    return digits.chunked(4).joinToString(" ")
}

@Composable
private fun VehicleTypeQuestionCard(
    selectedVehicle: String,
    onSelectVehicle: (String) -> Unit
) {
    CardHeader(
        icon = Icons.Default.ElectricRickshaw,
        categoryTag = "FLEET & LOGISTICS ASSET",
        title = "What type of vehicle do you operate?",
        subtitle = "Select your primary carrier for farmgate pickups, Mandi transit, and agricultural deliveries."
    )

    val vehicleOptions = listOf(
        VehicleOption("Hero Electric Cargo Trike (800kg)", "EV 3-Wheeler Trike", "800 kg capacity • 85 km range", "⚡ 12.4 kWh Lithium"),
        VehicleOption("Mahindra Treo Zor EV Cargo (550kg)", "EV Cargo 3W Auto", "550 kg capacity • 120 km range", "⚡ 8.8 kWh Lithium"),
        VehicleOption("Euler HiLoad EV (1000kg Heavy Cargo)", "Heavy Electric Mini Truck", "1000 kg capacity • 110 km range", "⚡ 14.6 kWh Lithium"),
        VehicleOption("Mahindra Bolero Maxi Truck (1500kg)", "Heavy Diesel/CNG Pickup", "1500 kg capacity • Rural Agro", "🛻 Heavy Duty"),
        VehicleOption("TVS iQube Express 2W (180kg)", "Electric Scooter Cargo", "180 kg capacity • Urgent Seeds", "⚡ Fast Courier")
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        vehicleOptions.forEach { opt ->
            val isSelected = selectedVehicle.contains(opt.title.substringBefore(" ")) || selectedVehicle == opt.title
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectVehicle(opt.title) },
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) Color(0xFFEFF6FF) else Color.White,
                border = ButtonDefaults.outlinedButtonBorder().copy(
                    brush = Brush.horizontalGradient(
                        if (isSelected) listOf(PartnerBlue, PartnerBlue) else listOf(CardBorder, CardBorder)
                    )
                ),
                shadowElevation = if (isSelected) 3.dp else 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = opt.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) PartnerBlue else TextPrimary
                            )
                        }
                        Text(
                            text = opt.spec,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) PartnerBlue else Color(0xFFF1F5F9))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = opt.badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF334155)
                        )
                    }
                }
            }
        }
    }
}

private data class VehicleOption(
    val title: String,
    val category: String,
    val spec: String,
    val badge: String
)

@Composable
private fun NumberPlateQuestionCard(
    numberPlate: String,
    onPlateChange: (String) -> Unit
) {
    CardHeader(
        icon = Icons.Default.Badge,
        categoryTag = "VEHICLE REGISTRATION",
        title = "Enter your vehicle's Number Plate",
        subtitle = "High Security Registration Plate (HSRP) format matching your State RTO certificate."
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Live HSRP Plate Rendering
        Text(
            text = "LIVE HSRP PLATE PREVIEW",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 0.8.sp
        )

        HsrpNumberPlate(
            plateNumber = if (numberPlate.isBlank()) "MH 15 EV 9201" else numberPlate,
            rtoLocation = "MAHARASHTRA RTO • HSRP SECURE"
        )

        OutlinedTextField(
            value = numberPlate,
            onValueChange = onPlateChange,
            label = { Text("Registration Number Plate") },
            placeholder = { Text("MH 15 EV 9201") },
            leadingIcon = {
                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = PartnerBlue)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PartnerBlue,
                unfocusedBorderColor = CardBorder
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Fast plates
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("MH 15 EV 9201", "MH 15 EV 4088", "MH 15 EF 3310", "MH 15 EV 1120").forEach { plate ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(CardBorder, CardBorder))),
                    modifier = Modifier.clickable { onPlateChange(plate) }
                ) {
                    Text(
                        text = plate,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddressQuestionCard(
    address: String,
    onAddressChange: (String) -> Unit
) {
    CardHeader(
        icon = Icons.Default.Home,
        categoryTag = "BASE RESIDENCE",
        title = "What is your permanent address?",
        subtitle = "Your base hub address used to calculate dispatch proximity and home-return logistics runs."
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Full Address / Village / Pincode") },
            placeholder = { Text("House No. 42, Pimple Gaon Farmgate Road, Nashik - 422209") },
            leadingIcon = {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = PartnerBlue)
            },
            minLines = 3,
            maxLines = 4,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PartnerBlue,
                unfocusedBorderColor = CardBorder
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Quick addresses
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "SUGGESTED OPERATING HUBS",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
            listOf(
                "House No. 42, Pimple Gaon Farmgate Road, Nashik 422209",
                "Plot 18, Niphad Agro Center, Vinchur Road, Nashik 422303",
                "Farm 7, Dindori Grape Producers Hub, Nashik 422202"
            ).forEach { addr ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(CardBorder, CardBorder))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddressChange(addr) }
                ) {
                    Text(
                        text = addr,
                        fontSize = 11.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationClusterQuestionCard(
    selectedCluster: String,
    onSelectCluster: (String) -> Unit
) {
    CardHeader(
        icon = Icons.Default.Place,
        categoryTag = "OPERATING CLUSTER",
        title = "Which agricultural hub cluster do you operate in?",
        subtitle = "Select your primary daily dispatch corridor and Mandi coverage network."
    )

    val clusters = listOf(
        "Nashik - Niphad Agricultural Belt",
        "Pimplegaon Onion & Tomato Mandi Terminal",
        "Satpur & Ambad Industrial Cold Storage MIDC",
        "Dindori Organic Farmgate Zone",
        "Sinnar Rural Produce Corridor",
        "Lasalgaon Asia's Largest Onion Market"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        clusters.forEach { cluster ->
            val isSelected = selectedCluster == cluster
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectCluster(cluster) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) Color(0xFFEFF6FF) else Color.White,
                border = ButtonDefaults.outlinedButtonBorder().copy(
                    brush = Brush.horizontalGradient(
                        if (isSelected) listOf(PartnerBlue, PartnerBlue) else listOf(CardBorder, CardBorder)
                    )
                ),
                shadowElevation = if (isSelected) 2.dp else 0.5.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = null,
                            tint = if (isSelected) PartnerBlue else Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = cluster,
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) PartnerBlue else TextPrimary
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PartnerBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OneTimeSignupCertifyCard(
    name: String,
    email: String,
    phone: String,
    aadhaarNumber: String,
    vehicleType: String,
    numberPlate: String,
    address: String,
    locationCluster: String,
    agreedToTerms: Boolean,
    onAgreedChange: (Boolean) -> Unit
) {
    CardHeader(
        icon = Icons.Default.Verified,
        categoryTag = "ONE-TIME SIGN UP & CERTIFICATION",
        title = "Review your details & complete sign up",
        subtitle = "Please verify your details and accept the partner code of conduct to activate your profile."
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Summary Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF8FAFC),
            border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(CardBorder, CardBorder)))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryRow("Partner Name", name)
                SummaryRow("Email & Mobile", "$email • $phone")
                SummaryRow("Aadhaar UIDAI", "•••• •••• ${aadhaarNumber.filter { it.isDigit() }.takeLast(4)} (Encrypted)")
                SummaryRow("Vehicle Type", vehicleType)
                SummaryRow("Number Plate", numberPlate)
                SummaryRow("Base Hub", locationCluster)
            }
        }

        // Working One-Time Sign Up Certification Checkbox
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAgreedChange(!agreedToTerms) },
            shape = RoundedCornerShape(14.dp),
            color = if (agreedToTerms) Color(0xFFEFF6FF) else Color(0xFFFFFBEB),
            border = ButtonDefaults.outlinedButtonBorder().copy(
                brush = Brush.horizontalGradient(
                    if (agreedToTerms) listOf(PartnerBlue, PartnerBlue) else listOf(PartnerYellowDark, PartnerYellowDark)
                )
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { onAgreedChange(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PartnerBlue,
                        uncheckedColor = PartnerYellowDark
                    )
                )
                Column {
                    Text(
                        text = "I Certify & Agree to Partner Terms (Mandatory)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (agreedToTerms) PartnerBlue else Color(0xFF92400E)
                    )
                    Text(
                        text = "I certify that the Aadhaar number, vehicle registration plate, and address provided above are genuine and accurate. I agree to the Rural Link Logistics Code of Conduct, Safe Handling protocols, and Dispatch Agreement.",
                        fontSize = 10.5.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
        Text(
            text = value,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}
