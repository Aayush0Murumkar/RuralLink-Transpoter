import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

orig = """                            job = uiState.activeDeliveryJob,
                            onTriggerNewOffer = { viewModel.fetchNewOrderFromSupabase() },
                            onFinishJob = { 
                                viewModel.setJobState("idle") 
                                viewModel.dismissOfferDialog()
                            },
                            onBackoutJob = { viewModel.triggerBackoutModal() }
                        )"""

new_val = """                            job = uiState.activeDeliveryJob,
                            onTriggerNewOffer = { viewModel.fetchNewOrderFromSupabase() },
                            onFinishJob = { 
                                viewModel.setJobState("idle") 
                                viewModel.dismissOfferDialog()
                            },
                            onBackoutJob = { viewModel.triggerBackoutModal() },
                            onShowPaymentQR = { viewModel.openPaymentQRModal() }
                        )"""
content = content.replace(orig, new_val)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
