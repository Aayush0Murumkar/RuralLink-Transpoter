import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Fix MainActivity CurrentScreen invocation
main_current_orig = """                            onTriggerNewOffer = { viewModel.fetchNewOrderFromSupabase() },
                            onFinishJob = { 
                                viewModel.setJobState("idle") 
                                viewModel.dismissOfferDialog()
                            },
                            onBackoutJob = { viewModel.triggerBackoutModal() },
                            onShowPaymentQR = { viewModel.openPaymentQRModal() }
                        )"""
main_current_new = """                            onTriggerNewOffer = { viewModel.fetchNewOrderFromSupabase() },
                            onFinishJob = { 
                                viewModel.openPaymentQRModal()
                            },
                            onBackoutJob = { viewModel.triggerBackoutModal() }
                        )"""
content = content.replace(main_current_orig, main_current_new)

# Fix PaymentQRDialog invocation
qr_orig = """        PaymentQRDialog(
            amount = uiState.activeDeliveryJob.payoutAmount,
            onPaymentConfirmed = { viewModel.confirmPaymentAndStartRide() },
            onClose = { viewModel.closePaymentQRModal() }
        )"""
qr_new = """        PaymentQRDialog(
            amount = uiState.activeDeliveryJob.payoutAmount,
            onPaymentConfirmed = { viewModel.confirmPaymentAndCompleteJob() },
            onClose = { viewModel.closePaymentQRModal() }
        )"""
content = content.replace(qr_orig, qr_new)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
