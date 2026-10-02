package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PartnerBlue
import com.example.ui.theme.PartnerYellow
import com.example.ui.viewmodel.PartnerViewModel
import com.example.ui.viewmodel.PartnerUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockLoginScreen(
    uiState: PartnerUiState,
    viewModel: PartnerViewModel
) {
    if (uiState.showTwoFactorStep) {
        BackHandler {
            // Can't revert back to OTP since it's verified, but we can reset the whole flow
            viewModel.submitLoginPhone(revert = true)
        }
    } else if (uiState.showOtpStep) {
        BackHandler {
            // Revert back to phone number entry
            viewModel.submitLoginPhone(revert = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Rural Link Partner",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = PartnerBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Driver & Fleet Onboarding",
            fontSize = 16.sp,
            color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.height(48.dp))

        if (uiState.showTwoFactorStep) {
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
        }
    }
}
