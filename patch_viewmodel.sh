sed -i 's/val loginOtp: String = "",/val loginOtp: String = "",\n    val loginTwoFactorCode: String = "",/g' app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt
sed -i 's/val showOtpStep: Boolean = false,/val showOtpStep: Boolean = false,\n    val showTwoFactorStep: Boolean = false,/g' app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt
