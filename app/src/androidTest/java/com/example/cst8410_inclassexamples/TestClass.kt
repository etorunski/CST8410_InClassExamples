package com.example.cst8410_inclassexamples

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestClass {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGreeting() {
        composeTestRule.setContent {
           Greeting("Android")
        }

        // Verify initial state: Text with "Hello" exists
        composeTestRule.onNodeWithText("Hello").assertExists()

        // Click the button
        val button = composeTestRule.onNodeWithText("CLick me")
        button.performClick()

        // Wait for UI to idle after click
        composeTestRule.waitForIdle()

        // Assert that "Hello" text no longer exists
        composeTestRule.onNodeWithText("Hello").assertDoesNotExist()

        // Assert that the text has changed to "You clicked on me"
        composeTestRule.onNodeWithText("You clicked on me").assertExists()
            .assertTextContains("You clicked on me", ignoreCase = true)
    }
}
