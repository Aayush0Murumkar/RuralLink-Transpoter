import re

with open('app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt', 'r') as f:
    content = f.read()

verify_login_otp_replacement = """    fun verifyLoginOtp() {
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
    }"""

content = re.sub(r'    fun verifyLoginOtp\(\) \{.*?\}\n    \}', verify_login_otp_replacement, content, flags=re.DOTALL)

with open('app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt', 'w') as f:
    f.write(content)
