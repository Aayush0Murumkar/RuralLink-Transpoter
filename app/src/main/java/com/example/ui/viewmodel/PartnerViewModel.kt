package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CopilotMessage
import com.example.data.model.DeliveryJob
import com.example.data.model.PartnerProfile
import com.example.data.model.PartnerVehicle
import com.example.data.model.TransportRequest
import com.example.data.model.TripHistoryItem
import com.example.data.model.WeeklyDayEarning
import com.example.data.service.SupabaseService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PartnerUiState(
    val selectedTab: String = "current", // "history", "current", "profile"
    val jobState: String = "idle", // "idle", "offer", "active"
    val showOfferModal: Boolean = false,
    val showPaymentQRModal: Boolean = false,
    val offerTimerSeconds: Int = 43,
    val activeDeliveryJob: DeliveryJob = DeliveryJob(),
    val isSupabaseConnected: Boolean = false,
    val isFetchingOrder: Boolean = false,
    val hasNewNotification: Boolean = false,
    val aiPopupMessage: String = "",
    
    // History tab state
    val historyPeriod: String = "today", // "today", "week"
    val historyFilter: String = "farm", // "all", "farm", "artisan"
    val historySearchQuery: String = "",
    val todayEarnings: Int = 1480,
    val todayDistanceKm: Double = 69.5,
    val todayJobsCount: Int = 7,
    
    // Fleet management
    val showFleetModal: Boolean = false,
    val fleetModalTabIndex: Int = 0, // 0: My Fleet, 1: Add Vehicle
    val activeVehicle: PartnerVehicle = PartnerVehicle(
        id = "V1",
        name = "Hero Electric Cargo Trike (800kg Capacity)",
        categoryTag = "EV ⚡",
        payloadKg = 800,
        rangeKm = 85,
        plateNumber = "MH-15-EV-9201",
        powerType = "EV ⚡",
        batteryPack = "12.4 kWh Lithium-Ion",
        chargePercent = 78,
        isActive = true
    ),
    val vehicles: List<PartnerVehicle> = listOf(
        PartnerVehicle(
            id = "V1",
            name = "Hero Electric Cargo Trike (800kg Capacity)",
            payloadKg = 800,
            rangeKm = 85,
            plateNumber = "MH-15-EV-9201",
            powerType = "EV ⚡",
            batteryPack = "12.4 kWh Lithium-Ion",
            chargePercent = 78,
            isActive = true
        ),
        PartnerVehicle(
            id = "V2",
            name = "Mahindra Treo Zor 3W EV Cargo",
            payloadKg = 550,
            rangeKm = 120,
            plateNumber = "MH-15-EV-4088",
            powerType = "EV ⚡",
            batteryPack = "8.8 kWh Lithium-Ion",
            chargePercent = 92,
            isActive = false
        ),
        PartnerVehicle(
            id = "V3",
            name = "TVS iQube Electric Scooter (Express Produce)",
            payloadKg = 180,
            rangeKm = 100,
            plateNumber = "MH-15-EV-1120",
            powerType = "EV ⚡",
            batteryPack = "3.4 kWh Lithium-Ion",
            chargePercent = 65,
            isActive = false
        )
    ),
    
    // Add Vehicle Form state
    val newVehicleName: String = "Euler HiLoad EV (1000kg Heavy Cargo)",
    val newVehiclePlate: String = "MH-15-EV-",
    val newVehicleFuelType: String = "EV ⚡",
    val newVehicleBattery: String = "12.4 kWh Lithium-",
    val newVehicleRange: String = "110",
    val newVehiclePayload: String = "850",
    
    // AI Copilot state
    val showCopilotModal: Boolean = false,
    val isCopilotTyping: Boolean = false,
    val copilotInputText: String = "",
    val copilotMessages: List<CopilotMessage> = listOf(
        CopilotMessage(
            id = "m1",
            isUser = false,
            text = "Hello Ramesh Patil! I am your Rural Link Partner AI Assistant. How can I assist your logistics run today? Ask me about road conditions, crop handling, or customer translation.",
            time = "12:17 AM"
        )
    ),
    
    // Auth State
    val isAuthenticated: Boolean = false,
    val loginPhone: String = "",
    val loginOtp: String = "",
    val loginTwoFactorCode: String = "",
    val showOtpStep: Boolean = false,
    val showTwoFactorStep: Boolean = false,
    
    // Profile & Settings
    val profile: PartnerProfile = PartnerProfile(),
    val isEditingProfileQuestionnaire: Boolean = false,
    val selectedLanguage: String = "English",
    val isOnline: Boolean = true,
    val showSosAlert: Boolean = false,
    val showBackoutModal: Boolean = false
)

class PartnerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PartnerUiState())
    val uiState = _uiState.asStateFlow()

    private val supabaseService = SupabaseService()
    private val _transportRequests = MutableStateFlow<List<TransportRequest>>(emptyList())
    val transportRequests = _transportRequests.asStateFlow()

    private var countdownJob: Job? = null
    private var realtimeJob: Job? = null

    init {
        // observeSupabaseRequests()
    }

    fun setLoginPhone(phone: String) {
        _uiState.update { it.copy(loginPhone = phone) }
    }

    fun setLoginTwoFactorCode(code: String) {
        _uiState.update { it.copy(loginTwoFactorCode = code) }
    }

    fun setLoginOtp(otp: String) {
        _uiState.update { it.copy(loginOtp = otp) }
    }

    fun submitLoginPhone(revert: Boolean = false) {
        if (revert) {
            _uiState.update { it.copy(showOtpStep = false, loginOtp = "") }
        } else {
            _uiState.update { it.copy(showOtpStep = true) }
        }
    }

    fun verifyLoginOtp() {
        _uiState.update { 
            it.copy(
                showOtpStep = false,
                showTwoFactorStep = true
            )
        }
    }

    fun verifyTwoFactorCode() {
        _uiState.update {
            it.copy(
                isAuthenticated = true,
                showTwoFactorStep = false,
                loginPhone = "",
                loginOtp = "",
                loginTwoFactorCode = ""
            )
        }
    }

    private fun observeSupabaseRequests() {
        realtimeJob?.cancel()
        realtimeJob = viewModelScope.launch {
            supabaseService.getTransportRequestsRealtime().collect { requests ->
                _transportRequests.value = requests

                val pendingRequest = requests.firstOrNull {
                    it.status.equals("PENDING", ignoreCase = true) ||
                    it.status.equals("NEW", ignoreCase = true) ||
                    it.status.equals("OFFERED", ignoreCase = true)
                }
                val activeRequest = requests.firstOrNull {
                    it.status.equals("ACCEPTED", ignoreCase = true) ||
                    it.status.equals("IN_TRANSIT", ignoreCase = true)
                }

                _uiState.update { current ->
                    var newJob = current.activeDeliveryJob
                    var newJobState = current.jobState
                    var newShowOffer = current.showOfferModal

                    if (pendingRequest != null) {
                        val mappedJob = pendingRequest.toDeliveryJob()
                        val isNewJob = current.activeDeliveryJob.id != mappedJob.id
                        newJob = mappedJob

                        if (current.jobState == "idle" || isNewJob) {
                            newShowOffer = true
                            startOfferTimer()
                        }
                    } else if (activeRequest != null) {
                        newJob = activeRequest.toDeliveryJob()
                        newJobState = "active"
                    }

                    current.copy(
                        activeDeliveryJob = newJob,
                        jobState = newJobState,
                        showOfferModal = newShowOffer,
                        isSupabaseConnected = supabaseService.isConfigured()
                    )
                }
            }
        }
    }

    private val staticHistoryItems = listOf(
        TripHistoryItem(
            id = "RL-FARMGATE-3709",
            title = "🥭 250kg Fresh Alphonso Mangoes (5 Crates)",
            supplierOrFarm = "🏡 Rameshwar Organic Orchards",
            fromLocation = "Pimple Gaon",
            toLocation = "Satpur Industrial Area",
            distanceKm = 14.5,
            weightKg = 250,
            payout = 440,
            status = "ACCEPTED",
            category = "FARM_PRODUCE"
        ),
        TripHistoryItem(
            id = "RL-NASHIK-9800",
            title = "🍎 150kg Fresh Pomegranates",
            supplierOrFarm = "🏡 Sanjay Farmers Group",
            fromLocation = "Pimple Gaon",
            toLocation = "Satpur",
            distanceKm = 21.0,
            weightKg = 150,
            payout = 410,
            status = "DELIVERED",
            category = "FARM_PRODUCE"
        ),
        TripHistoryItem(
            id = "RL-NASHIK-1120",
            title = "🧅 400kg Onion Bags (Grade A)",
            supplierOrFarm = "🏛️ Lasalgaon Agri Depot",
            fromLocation = "Lasalgaon Mandi",
            toLocation = "Satpur Cold Store",
            distanceKm = 34.0,
            weightKg = 400,
            payout = 630,
            status = "DELIVERED",
            category = "FARM_PRODUCE"
        ),
        TripHistoryItem(
            id = "RL-AGRI-5510",
            title = "🌱 200kg Hybrid Millet & Safflower Seeds",
            supplierOrFarm = "🏬 Niphad Krishak Kendra",
            fromLocation = "Niphad Hub",
            toLocation = "Vinchur Cooperative",
            distanceKm = 12.0,
            weightKg = 200,
            payout = 320,
            status = "DELIVERED",
            category = "AGRI_INPUTS"
        ),
        TripHistoryItem(
            id = "RL-ART-4091",
            title = "🎨 18 Hand-woven Bamboo Crates & Pottery",
            supplierOrFarm = "🏺 Trimbak Artisan Cluster",
            fromLocation = "Trimbakeshwar",
            toLocation = "Nashik City Bazaar",
            distanceKm = 28.5,
            weightKg = 65,
            payout = 390,
            status = "DELIVERED",
            category = "ARTISAN"
        )
    )

    val historyItems: List<TripHistoryItem>
        get() {
            val liveItems = _transportRequests.value
                .filter { it.status == "DELIVERED" || it.status == "ACCEPTED" || it.status == "IN_TRANSIT" }
                .map { req ->
                    TripHistoryItem(
                        id = req.id,
                        title = req.title.ifBlank { "Produce Transport" },
                        supplierOrFarm = req.supplier.ifBlank { "Local Farmer" },
                        fromLocation = req.pickupName.ifBlank { req.pickupAddress },
                        toLocation = req.dropoffName.ifBlank { req.dropoffAddress },
                        distanceKm = req.distanceKm,
                        weightKg = req.payloadKg,
                        payout = req.payoutAmount,
                        status = req.status,
                        category = when (req.category.uppercase()) {
                            "AGRI_INPUTS", "AGRI INPUTS" -> "AGRI_INPUTS"
                            "ARTISAN" -> "ARTISAN"
                            else -> "FARM_PRODUCE"
                        }
                    )
                }
            return (liveItems + staticHistoryItems).distinctBy { it.id }
        }

    val weeklyEarnings = listOf(
        WeeklyDayEarning("Mon", false, 7, 52.0, 1660, 280),
        WeeklyDayEarning("Tue", false, 8, 61.4, 1900, 340),
        WeeklyDayEarning("Wed", false, 5, 39.2, 1170, 180),
        WeeklyDayEarning("Thu", false, 9, 68.0, 2240, 410),
        WeeklyDayEarning("Fri", false, 6, 44.8, 1430, 220),
        WeeklyDayEarning("Sat", false, 10, 74.5, 2600, 490),
        WeeklyDayEarning("Today (Sun)", true, 6, 48.5, 1400, 260)
    )

    fun selectTab(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun fetchNewOrderFromSupabase() {
        if (_uiState.value.isFetchingOrder) return
        
        viewModelScope.launch {
            // Show scanning animation
            _uiState.update { 
                it.copy(
                    isFetchingOrder = true, 
                    aiPopupMessage = "Scanning Rural Link network for active farmgate requests..."
                )
            }
            
            // Simulate network delay
            delay(2500)
            
            // Show new order found
            _uiState.update { 
                it.copy(
                    isFetchingOrder = false,
                    aiPopupMessage = "",
                    hasNewNotification = true
                )
            }
            
            // Automatically open the offer modal
            openNotificationOffer()
        }
    }

    fun openNotificationOffer() {
        if (!_uiState.value.hasNewNotification) return
        
        _uiState.update { it.copy(hasNewNotification = false) }
        
        viewModelScope.launch {
            // Create a sample live transport request
            val isReturnTrip = Math.random() > 0.5
            val pricing = com.example.data.service.PricingEngine.calculatePricing(distanceKm = 14.5, shipmentWeightKg = 250.0, isBackloadTrip = isReturnTrip)
            val sampleReq = TransportRequest(
                id = "RL-FARMGATE-${(1000..9999).random()}",
                title = "🥭 250kg Fresh Organic Mangoes (5 Crates)",
                supplier = "Rameshwar Organic Orchards",
                category = "FARM_PRODUCE",
                payloadKg = 250,
                payoutAmount = pricing.transporterPayout,
                bonusAmount = if (isReturnTrip) 50 else 0,
                distanceKm = 14.5,
                durationMinutes = 24,
                eta = "Today, 4:30 PM",
                pickupName = "Pimple Gaon Farmgate Hub",
                pickupAddress = "Survey 42, Pimple Gaon",
                pickupLandmark = "Near Sahyadri Primary Dairy Co-op",
                pickupDistance = "1.8 km away",
                pickupPhone = "+91 98221 00492",
                dropoffName = "Satpur Industrial APMC Depot",
                dropoffAddress = "Plot 88, MIDC Satpur Cold Storage",
                dropoffLandmark = "Opp. Mahindra Logistics Gate 2",
                dropoffPhone = "+91 98901 88234",
                isPrepaid = true,
                status = "PENDING"
            )

            _uiState.update {
                it.copy(
                    activeDeliveryJob = sampleReq.toDeliveryJob(),
                    showOfferModal = true,
                    offerTimerSeconds = 43
                )
            }
            startOfferTimer()
        }
    }


    fun confirmPaymentAndCompleteJob() {
        _uiState.update {
            it.copy(
                showPaymentQRModal = false,
                jobState = "idle"
            )
        }
    }
    
    fun openPaymentQRModal() {
        _uiState.update { it.copy(showPaymentQRModal = true) }
    }
    
    fun closePaymentQRModal() {
        _uiState.update { it.copy(showPaymentQRModal = false) }
    }

    fun dismissOfferDialog() {
        countdownJob?.cancel()
        _uiState.update { it.copy(showOfferModal = false) }
    }

    fun acceptOffer() {
        countdownJob?.cancel()
        val currentJob = _uiState.value.activeDeliveryJob
        _uiState.update {
            it.copy(
                showOfferModal = false,
                jobState = "active",
                selectedTab = "current"
            )
        }
        /*
        if (currentJob.id.isNotBlank()) {
            viewModelScope.launch {
                supabaseService.updateRequestStatus(currentJob.id, "ACCEPTED")
            }
        }
        */
    }

    fun toggleOnlineStatus(isOnline: Boolean) {
        _uiState.update { it.copy(isOnline = isOnline) }
    }

    fun openBackoutModal() {
        _uiState.update { it.copy(showBackoutModal = true) }
    }

    fun closeBackoutModal() {
        _uiState.update { it.copy(showBackoutModal = false) }
    }

    fun confirmBackoutOrder(reason: String) {
        _uiState.update { 
            it.copy(
                showBackoutModal = false,
                jobState = "idle" // Release back to open dispatch pool
            ) 
        }
    }

    fun setJobState(state: String) {
        val currentJob = _uiState.value.activeDeliveryJob
        _uiState.update { it.copy(jobState = state) }
        /*
        if (state == "idle" && currentJob.id.isNotBlank()) {
            viewModelScope.launch {
                supabaseService.updateRequestStatus(currentJob.id, "DELIVERED")
            }
        }
        */
    }

    private fun startOfferTimer() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (sec in 43 downTo 0) {
                _uiState.update { it.copy(offerTimerSeconds = sec) }
                delay(1000)
            }
            _uiState.update { it.copy(showOfferModal = false) }
        }
    }

    fun setHistoryPeriod(period: String) {
        _uiState.update { it.copy(historyPeriod = period) }
    }

    fun setHistoryFilter(filter: String) {
        _uiState.update { it.copy(historyFilter = filter) }
    }

    fun setHistorySearchQuery(query: String) {
        _uiState.update { it.copy(historySearchQuery = query) }
    }

    fun openFleetModal(tabIndex: Int = 0) {
        _uiState.update { it.copy(showFleetModal = true, fleetModalTabIndex = tabIndex) }
    }

    fun closeFleetModal() {
        _uiState.update { it.copy(showFleetModal = false) }
    }

    fun setFleetModalTab(index: Int) {
        _uiState.update { it.copy(fleetModalTabIndex = index) }
    }

    fun setActiveVehicle(vehicleId: String) {
        _uiState.update { current ->
            val updated = current.vehicles.map { v ->
                v.copy(isActive = v.id == vehicleId)
            }
            val active = updated.find { it.id == vehicleId } ?: current.activeVehicle
            current.copy(vehicles = updated, activeVehicle = active)
        }
    }

    fun updateNewVehicleForm(
        name: String? = null,
        plate: String? = null,
        fuelType: String? = null,
        battery: String? = null,
        range: String? = null,
        payload: String? = null
    ) {
        _uiState.update { current ->
            current.copy(
                newVehicleName = name ?: current.newVehicleName,
                newVehiclePlate = plate ?: current.newVehiclePlate,
                newVehicleFuelType = fuelType ?: current.newVehicleFuelType,
                newVehicleBattery = battery ?: current.newVehicleBattery,
                newVehicleRange = range ?: current.newVehicleRange,
                newVehiclePayload = payload ?: current.newVehiclePayload
            )
        }
    }

    fun registerNewVehicle() {
        val state = _uiState.value
        val newVehicle = PartnerVehicle(
            id = "V${state.vehicles.size + 1}",
            name = state.newVehicleName.ifBlank { "Euler HiLoad EV (1000kg Heavy Cargo)" },
            payloadKg = state.newVehiclePayload.toIntOrNull() ?: 850,
            rangeKm = state.newVehicleRange.toIntOrNull() ?: 110,
            plateNumber = state.newVehiclePlate.ifBlank { "MH-15-EV-9922" },
            powerType = state.newVehicleFuelType,
            batteryPack = state.newVehicleBattery,
            chargePercent = 88,
            isActive = true
        )
        val updatedList = state.vehicles.map { it.copy(isActive = false) } + newVehicle
        _uiState.update {
            it.copy(
                vehicles = updatedList,
                activeVehicle = newVehicle,
                showFleetModal = false,
                fleetModalTabIndex = 0
            )
        }
    }

    fun openCopilotModal() {
        _uiState.update { it.copy(showCopilotModal = true) }
    }

    fun closeCopilotModal() {
        _uiState.update { it.copy(showCopilotModal = false) }
    }

    fun setCopilotInputText(text: String) {
        _uiState.update { it.copy(copilotInputText = text) }
    }

    fun sendCopilotMessage(userPrompt: String? = null) {
        val query = (userPrompt ?: _uiState.value.copilotInputText).trim()
        if (query.isBlank()) return

        val userMsg = CopilotMessage(
            id = "usr_${System.currentTimeMillis()}",
            isUser = true,
            text = query,
            time = "Just now"
        )
        val currentMsgs = _uiState.value.copilotMessages + userMsg
        _uiState.update { it.copy(copilotMessages = currentMsgs, copilotInputText = "", isCopilotTyping = true) }

        viewModelScope.launch {
            // Simulate network delay for AI thinking
            delay(1500)
            
            val lowerQuery = query.lowercase()
            val replyText = when {
                lowerQuery.contains("ev") || lowerQuery.contains("charge") || lowerQuery.contains("battery") -> {
                    "**Solar Battery Swap Station, Vinchur Sector**\n\n• **Distance:** 3.2 km ahead (approx. 8 mins)\n• **Status:** 2 swap slots currently open\n• **Cost:** ₹12/kWh"
                }
                lowerQuery.contains("gas") || lowerQuery.contains("cng") || lowerQuery.contains("diesel") || lowerQuery.contains("fuel") -> {
                    "**IndianOil Pump, Niphad Highway**\n\n• **Distance:** 5.5 km away (approx. 12 mins)\n• **Availability:** High-speed diesel and CNG available."
                }
                lowerQuery.contains("rest") || lowerQuery.contains("dhaba") || lowerQuery.contains("food") || lowerQuery.contains("eat") || lowerQuery.contains("sleep") || lowerQuery.contains("parking") -> {
                    "**Shri Swami Samarth Dhaba**\n\n• **Distance:** 4.0 km away\n• **Amenities:** Secure parking for heavy vehicles and clean washrooms."
                }
                else -> {
                    "I am your Rural Link In-Cab Assistant. I'm locked to Driver Support Mode. Ask me about EV charging, fuel stations, or rest stops to get immediate route assistance!"
                }
            }

            val botMsg = CopilotMessage(
                id = "bot_${System.currentTimeMillis()}",
                isUser = false,
                text = replyText,
                time = "Just now"
            )
            _uiState.update { it.copy(copilotMessages = it.copilotMessages + botMsg, isCopilotTyping = false) }
        }
    }

    fun setLanguage(lang: String) {
        _uiState.update { it.copy(selectedLanguage = lang) }
    }

    fun openProfileQuestionnaire() {
        _uiState.update { it.copy(isEditingProfileQuestionnaire = true) }
    }

    fun closeProfileQuestionnaire() {
        _uiState.update { it.copy(isEditingProfileQuestionnaire = false) }
    }

    fun savePartnerProfile(newProfile: PartnerProfile) {
        val updatedVehicle = _uiState.value.activeVehicle.copy(
            name = newProfile.vehicleType,
            plateNumber = newProfile.numberPlate
        )
        val updatedVehicles = _uiState.value.vehicles.map { v ->
            if (v.id == updatedVehicle.id) updatedVehicle else v
        }

        _uiState.update {
            it.copy(
                profile = newProfile.copy(isSignedUp = true, agreedToTerms = true),
                activeVehicle = updatedVehicle,
                vehicles = updatedVehicles,
                isEditingProfileQuestionnaire = false
            )
        }
    }

    fun signOutPartner() {
        _uiState.update {
            it.copy(
                profile = it.profile.copy(isSignedUp = false, agreedToTerms = false),
                isEditingProfileQuestionnaire = true
            )
        }
    }
}

