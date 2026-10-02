import re

with open('app/src/main/java/com/example/ui/screens/MockLoginScreen.kt', 'r') as f:
    content = f.read()

# Update back handler logic
back_handler_orig = """    if (uiState.showOtpStep) {
        BackHandler {
            // Revert back to phone number entry
            viewModel.submitLoginPhone(revert = true)
        }
    }"""
back_handler_new = """    if (uiState.showTwoFactorStep) {
        BackHandler {
            // Can't revert back to OTP since it's verified, but we can reset the whole flow
            viewModel.submitLoginPhone(revert = true)
        }
    } else if (uiState.showOtpStep) {
        BackHandler {
            // Revert back to phone number entry
            viewModel.submitLoginPhone(revert = true)
        }
    }"""
content = content.replace(back_handler_orig, back_handler_new)

# Update if/else block
if_else_orig = """        if (!uiState.showOtpStep) {
            OutlinedTextField(
                value = uiState.loginPhone,
                onValueChange = { viewModel.setLoginPhone(it) },
                label = { Text("Phone Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PartnerBlue,
                    focusedLabelColor = PartnerBlue
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.submitLoginPhone() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PartnerYellow, contentColor = PartnerBlue)
            ) {
                Text("Send OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        } else {
            Text(
                text = "OTP sent to ${uiState.loginPhone}",
                fontSize = 14.sp,
                color = PartnerBlue,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.loginOtp,
                onValueChange = { viewModel.setLoginOtp(it) },
                label = { Text("Enter OTP (e.g. 1234)") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PartnerBlue,
                    focusedLabelColor = PartnerBlue
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.verifyLoginOtp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PartnerYellow, contentColor = PartnerBlue)
            ) {
                Text("Verify & Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }"""

if_else_new = """        if (uiState.showTwoFactorStep) {
            Text(
                text = "Two-Factor Authentication",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PartnerBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter the 6-digit code from your Authenticator app.",
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = uiState.loginTwoFactorCode,
                onValueChange = { viewModel.setLoginTwoFactorCode(it) },
                label = { Text("6-digit Code (e.g. 123456)") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PartnerBlue,
                    focusedLabelColor = PartnerBlue
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.verifyTwoFactorCode() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PartnerYellow, contentColor = PartnerBlue)
            ) {
                Text("Verify & Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        } else if (uiState.showOtpStep) {
            Text(
                text = "OTP sent to ${uiState.loginPhone}",
                fontSize = 14.sp,
                color = PartnerBlue,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.loginOtp,
                onValueChange = { viewModel.setLoginOtp(it) },
                label = { Text("Enter OTP (e.g. 1234)") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PartnerBlue,
                    focusedLabelColor = PartnerBlue
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.verifyLoginOtp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PartnerYellow, contentColor = PartnerBlue)
            ) {
                Text("Verify OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        } else {
            OutlinedTextField(
                value = uiState.loginPhone,
                onValueChange = { viewModel.setLoginPhone(it) },
                label = { Text("Phone Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PartnerBlue,
                    focusedLabelColor = PartnerBlue
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.submitLoginPhone() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PartnerYellow, contentColor = PartnerBlue)
            ) {
                Text("Send OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }"""
content = content.replace(if_else_orig, if_else_new)

with open('app/src/main/java/com/example/ui/screens/MockLoginScreen.kt', 'w') as f:
    f.write(content)
