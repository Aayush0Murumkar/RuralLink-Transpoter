import re

with open('app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt', 'r') as f:
    content = f.read()

# Fix acceptOffer
accept_orig = """    fun acceptOffer() {
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
accept_new = """    fun acceptOffer() {
        countdownJob?.cancel()
        val currentJob = _uiState.value.activeDeliveryJob
        _uiState.update {
            it.copy(
                showOfferModal = false,
                jobState = "active",
                selectedTab = "current"
            )
        }"""
content = content.replace(accept_orig, accept_new)

# Fix confirmPaymentAndStartRide
confirm_orig = """    fun confirmPaymentAndStartRide() {
        val currentJob = _uiState.value.activeDeliveryJob
        _uiState.update {
            it.copy(
                showPaymentQRModal = false,
                activeDeliveryJob = currentJob.copy(status = "IN_TRANSIT")
            )
        }
    }"""
confirm_new = """    fun confirmPaymentAndCompleteJob() {
        _uiState.update {
            it.copy(
                showPaymentQRModal = false,
                jobState = "idle"
            )
        }
    }"""
content = content.replace(confirm_orig, confirm_new)

with open('app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt', 'w') as f:
    f.write(content)
