package com.example

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class MainActivityRobolectricTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testLoginAndBypass() {
        // Try to click Send OTP
        composeTestRule.onNodeWithText("Send OTP").performClick()
        
        // Try to click Verify & Login
        composeTestRule.onNodeWithText("Verify & Login").performClick()
        
        composeTestRule.waitForIdle()

        // RURAL LINK NETWORK text is in the Connected Farmgate card
        composeTestRule.onNodeWithText("RURAL LINK NETWORK", substring = true).performClick()
        
        composeTestRule.waitForIdle()
    }
}
