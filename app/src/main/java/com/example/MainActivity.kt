package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.AppBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PartnerYellow
import com.example.ui.viewmodel.PartnerViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PartnerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RuralLinkPartnerApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RuralLinkPartnerApp(
    viewModel: PartnerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    if (!uiState.isAuthenticated) {
        MockLoginScreen(
            uiState = uiState,
            viewModel = viewModel
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            PartnerTopBar(
                onBackClick = {
                    if (uiState.selectedTab != "current") {
                        viewModel.selectTab("current")
                    } else if (uiState.jobState == "active") {
                        viewModel.setJobState("idle")
                    }
                },
                onCopilotClick = { viewModel.openCopilotModal() },
                onNotificationClick = { viewModel.openNotificationOffer() },
                hasNotification = uiState.hasNewNotification,
                isOnline = uiState.isOnline,
                onToggleOnline = { viewModel.toggleOnlineStatus(it) }
            )
        },
        bottomBar = {
            PartnerBottomNav(
                selectedTab = uiState.selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AppBackground)
        ) {
            when (uiState.selectedTab) {
                "history" -> {
                    HistoryScreen(
                        period = uiState.historyPeriod,
                        filter = uiState.historyFilter,
                        searchQuery = uiState.historySearchQuery,
                        items = viewModel.historyItems,
                        weeklyEarnings = viewModel.weeklyEarnings,
                        todayEarnings = uiState.todayEarnings,
                        todayDistanceKm = uiState.todayDistanceKm,
                        todayJobsCount = uiState.todayJobsCount,
                        onPeriodChange = { viewModel.setHistoryPeriod(it) },
                        onFilterChange = { viewModel.setHistoryFilter(it) },
                        onSearchChange = { viewModel.setHistorySearchQuery(it) }
                    )
                }
                "profile" -> {
                    ProfileScreen(
                        profile = uiState.profile,
                        activeVehicle = uiState.activeVehicle,
                        vehiclesCount = uiState.vehicles.size,
                        selectedLanguage = uiState.selectedLanguage,
                        isEditingQuestionnaire = uiState.isEditingProfileQuestionnaire,
                        onOpenQuestionnaire = { viewModel.openProfileQuestionnaire() },
                        onCloseQuestionnaire = { viewModel.closeProfileQuestionnaire() },
                        onSaveProfile = { viewModel.savePartnerProfile(it) },
                        onSignOut = { viewModel.signOutPartner() },
                        onManageFleetClick = { viewModel.openFleetModal(0) },
                        onAddVehicleClick = { viewModel.openFleetModal(1) },
                        onLanguageSelect = { viewModel.setLanguage(it) }
                    )
                }
                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CurrentScreen(
                            jobState = uiState.jobState,
                            job = uiState.activeDeliveryJob,
                            onTriggerNewOffer = { viewModel.fetchNewOrderFromSupabase() },
                            onFinishJob = { 
                                viewModel.setJobState("idle") 
                            },
                            onBackoutJob = {
                                viewModel.openBackoutModal()
                            }
                        )

                        // Floating AI Chatbot Popup for "Scanning" and "New Order Found"
                        androidx.compose.animation.AnimatedVisibility(
                            visible = uiState.isFetchingOrder || uiState.aiPopupMessage.isNotEmpty(),
                            enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.slideInVertically(initialOffsetY = { -50 }),
                            exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.slideOutVertically(targetOffsetY = { -50 }),
                            modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White,
                                shadowElevation = 8.dp,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CuteAiBotAvatar(size = 36.dp)
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = uiState.aiPopupMessage,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                        if (uiState.isFetchingOrder) {
                                            LinearProgressIndicator(
                                                modifier = Modifier
                                                    .fillMaxWidth(0.6f)
                                                    .height(4.dp)
                                                    .clip(RoundedCornerShape(2.dp)),
                                                color = PartnerYellow,
                                                trackColor = Color(0xFFF1F5F9)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (uiState.showBackoutModal) {
        OrderBackoutConfirmationDialog(
            orderId = uiState.activeDeliveryJob.id,
            onDismiss = { viewModel.closeBackoutModal() },
            onConfirmBackout = { reason -> 
                viewModel.confirmBackoutOrder(reason) 
            }
        )
    }// New Delivery Offer Popup Dialog
    if (uiState.showOfferModal) {
        DeliveryOfferDialog(
            job = uiState.activeDeliveryJob,
            countdownSeconds = uiState.offerTimerSeconds,
            onAccept = { viewModel.acceptOffer() },
            onDecline = { viewModel.dismissOfferDialog() }
        )
    }
    
    if (uiState.showPaymentQRModal) {
        PaymentQRDialog(
            amount = uiState.activeDeliveryJob.payoutAmount,
            onPaymentConfirmed = { viewModel.confirmPaymentAndCompleteJob() },
            onClose = { viewModel.closePaymentQRModal() }
        )
    }

    // Manage Fleet & Add Vehicle Modal Dialog
    if (uiState.showFleetModal) {
        ManageFleetDialog(
            vehicles = uiState.vehicles,
            activeVehicleId = uiState.activeVehicle.id,
            selectedTabIndex = uiState.fleetModalTabIndex,
            onTabSelected = { viewModel.setFleetModalTab(it) },
            onSetActive = { viewModel.setActiveVehicle(it) },
            onDismiss = { viewModel.closeFleetModal() },
            vehicleName = uiState.newVehicleName,
            plateNumber = uiState.newVehiclePlate,
            fuelType = uiState.newVehicleFuelType,
            battery = uiState.newVehicleBattery,
            range = uiState.newVehicleRange,
            payload = uiState.newVehiclePayload,
            onUpdateForm = { name, plate, fuel, bat, rng, pld ->
                viewModel.updateNewVehicleForm(name, plate, fuel, bat, rng, pld)
            },
            onRegisterVehicle = { viewModel.registerNewVehicle() }
        )
    }

    // Rural Link AI Partner Copilot Drawer Dialog
    if (uiState.showCopilotModal) {
        RuralLinkAiCopilotDialog(
            messages = uiState.copilotMessages,
            inputText = uiState.copilotInputText,
            isTyping = uiState.isCopilotTyping,
            onInputChange = { viewModel.setCopilotInputText(it) },
            onSendMessage = { viewModel.sendCopilotMessage(it) },
            onDismiss = { viewModel.closeCopilotModal() }
        )
    }
}
