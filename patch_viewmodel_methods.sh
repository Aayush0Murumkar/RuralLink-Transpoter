sed -i 's/fun setLoginOtp(otp: String) {/fun setLoginTwoFactorCode(code: String) {\n        _uiState.update { it.copy(loginTwoFactorCode = code) }\n    }\n\n    fun setLoginOtp(otp: String) {/g' app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt

cat << 'INNER_EOF' > app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt.patch
--- app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt
+++ app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt
@@ -160,11 +160,18 @@
     fun verifyLoginOtp() {
         _uiState.update { 
             it.copy(
-                isAuthenticated = true,
                 showOtpStep = false,
+                showTwoFactorStep = true
+            )
+        }
+    }
+
+    fun verifyTwoFactorCode() {
+        _uiState.update {
+            it.copy(
+                isAuthenticated = true,
+                showTwoFactorStep = false,
                 loginPhone = "",
-                loginOtp = ""
+                loginOtp = "",
+                loginTwoFactorCode = ""
             )
         }
     }
INNER_EOF
patch -p0 < app/src/main/java/com/example/ui/viewmodel/PartnerViewModel.kt.patch
