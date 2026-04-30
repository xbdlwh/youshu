package com.example.someapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.someapp.ui.main.MainScreen
import com.example.someapp.ui.addDevice.AddDeviceScreen
import com.example.someapp.ui.test.TestScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(FileTest)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry <FileTest>{
          TestScreen()
        }
        entry<Main> {
          MainScreen(
            onNavigateToAdd = { backStack.add(AddDevice) },
            modifier = Modifier.safeDrawingPadding().padding(16.dp)
          )
        }
        entry<AddDevice> {
          AddDeviceScreen(
            onNavigateBack = { backStack.removeLastOrNull() }
          )
        }
      },
  )
}
