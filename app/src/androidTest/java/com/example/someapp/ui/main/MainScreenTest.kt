package com.example.someapp.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.theme.MyApplicationTheme
import com.example.someapp.ui.main.components.DeviceItem
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** UI tests for the main device list components. */
class MainScreenTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent {
      MyApplicationTheme {
        DeviceItem(deviceWithType = FAKE_DEVICE)
      }
    }
  }

  @Test
  fun deviceItem_displaysDeviceDetails() {
    composeTestRule.onNodeWithText("iPhone 15 Pro").assertIsDisplayed()
    composeTestRule.onNodeWithText("Phone").assertIsDisplayed()
    composeTestRule.onNodeWithText("$999.99").assertIsDisplayed()
    composeTestRule.onNodeWithText("Serving").assertIsDisplayed()
  }
}

private val FAKE_DEVICE = DeviceWithType(
  device = DeviceEntity(
    id = 1,
    name = "iPhone 15 Pro",
    icon = "phone",
    typeId = 1,
    price = 999.99,
    isServing = true,
    purchaseDate = 0
  ),
  deviceType = DeviceTypeEntity(
    id = 1,
    name = "Phone",
    icon = "phone"
  )
)
