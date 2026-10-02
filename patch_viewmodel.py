import re

with open('app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt', 'r') as f:
    content = f.read()

# Add showPaymentQRModal to PartnerUiState
state_orig = """    val showOfferModal: Boolean = false,
    val offerTimerSeconds: Int = 43,
    val activeDeliveryJob: DeliveryJob = DeliveryJob(),"""
state_new = """    val showOfferModal: Boolean = false,
    val showPaymentQRModal: Boolean = false,
    val offerTimerSeconds: Int = 43,
    val activeDeliveryJob: DeliveryJob = DeliveryJob(),"""
content = content.replace(state_orig, state_new)

# Update acceptOffer()
accept_orig = """    fun acceptOffer() {
        countdownJob?.cancel()
        val currentJob = _uiState.value.activeDeliveryJob
        _uiState.update {
            it.copy(
                showOfferModal = false,
                jobState = "active",
                selectedTab = "current"
            )
        }"""
accept_new = """    fun acceptOffer() {
        countdownJob?.cancel()
        val currentJob = _uiState.value.activeDeliveryJob
        _uiState.update {
            it.copy(
                showOfferModal = false,
                showPaymentQRModal = true,
                jobState = "active",
                selectedTab = "current"
            )
        }"""
content = content.replace(accept_orig, accept_new)

# Add confirmPaymentAndStartRide() and openPaymentQRModal()
payment_methods = """
    fun confirmPaymentAndStartRide() {
        val currentJob = _uiState.value.activeDeliveryJob
        _uiState.update {
            it.copy(
                showPaymentQRModal = false,
                activeDeliveryJob = currentJob.copy(status = "IN_TRANSIT")
            )
        }
    }
    
    fun openPaymentQRModal() {
        _uiState.update { it.copy(showPaymentQRModal = true) }
    }
    
    fun closePaymentQRModal() {
        _uiState.update { it.copy(showPaymentQRModal = false) }
    }
"""

content = re.sub(r'    fun dismissOfferDialog\(\) \{', payment_methods + '\n    fun dismissOfferDialog() {', content, count=1)

with open('app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt', 'w') as f:
    f.write(content)
