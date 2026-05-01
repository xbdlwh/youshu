package com.example.someapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.someapp.data.repository.DeviceRepository
import com.example.someapp.ui.main.MainScreen
import com.example.someapp.ui.main.MainScreenViewModel
import com.example.someapp.ui.addDevice.AddDeviceScreen
import com.example.someapp.ui.test.TestScreen
import com.example.someapp.ui.deviceDetail.DeviceDetailScreen
import com.example.someapp.ui.editDevice.EditDeviceScreen
import kotlinx.coroutines.runBlocking

@Composable
fun MainNavigation(
  deviceRepository: DeviceRepository,
) {
  val backStack = rememberNavBackStack(Main)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry <FileTest>{
          TestScreen()
        }
        entry<Main> {
//          val mainScreenViewModel = MainScreenViewModel(deviceRepository)
          MainScreen(
            onNavigateToAdd = { backStack.add(AddDevice) },
            onNavigateToDetail = { deviceId -> backStack.add(DeviceDetail(deviceId)) },
//            onExportJson = {
//              runBlocking {
//                mainScreenViewModel.exportDataAsJson()
//              }
//            },
//            modifier = Modifier.safeDrawingPadding().padding(16.dp)
          )
        }
        entry<AddDevice> {
          AddDeviceScreen(
            onNavigateBack = { backStack.removeLastOrNull() }
          )
        }
        entry<DeviceDetail> { key ->
          DeviceDetailScreen(
            deviceId = key.deviceId,
            onNavigateBack = { backStack.removeLastOrNull() },
            onNavigateToEdit = { deviceWithType -> backStack.add(EditDevice(deviceWithType)) }
          )
        }
        entry<EditDevice> { key ->
          EditDeviceScreen(
            deviceWithType = key.deviceWithType,
            onNavigateBack = { backStack.removeLastOrNull() }
          )
        }
      },
  )
}
