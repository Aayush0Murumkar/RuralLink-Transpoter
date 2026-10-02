import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Add import
if 'import com.example.ui.components.PaymentQRDialog' not in content:
    content = content.replace('import com.example.ui.components.OrderBackoutConfirmationDialog',
                             'import com.example.ui.components.OrderBackoutConfirmationDialog\nimport com.example.ui.components.PaymentQRDialog')

# Add modal instantiation
modal_code = """
    // New Delivery Offer Popup Dialog
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
            onPaymentConfirmed = { viewModel.confirmPaymentAndStartRide() },
            onClose = { viewModel.closePaymentQRModal() }
        )
    }
"""

content = re.sub(r'\s*// New Delivery Offer Popup Dialog[\s\S]*?onDecline = \{ viewModel\.dismissOfferDialog\(\) \}\n        \)', modal_code.strip() + '\n        }', content)

# But simpler replacement:
content = re.sub(r'    // New Delivery Offer Popup Dialog\n    if \(uiState\.showOfferModal\) \{\n        DeliveryOfferDialog\(\n            job = uiState\.activeDeliveryJob,\n            countdownSeconds = uiState\.offerTimerSeconds,\n            onAccept = \{ viewModel\.acceptOffer\(\) \},\n            onDecline = \{ viewModel\.dismissOfferDialog\(\) \}\n        \)\n    \}', modal_code.strip(), content)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
