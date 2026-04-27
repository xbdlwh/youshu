package com.example.someapp.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.someapp.MyViewModelProvider
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.theme.MyApplicationTheme

@Composable
fun MainScreen(
  onItemClick: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: MainScreenViewModel = viewModel(factory = MyViewModelProvider.FACTORY),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  when (state) {
    MainScreenUiState.Loading -> {
      Text("Loading...")
    }
    is MainScreenUiState.Success -> {
      DeviceListScreen(
        devices = (state as MainScreenUiState.Success).devices,
        modifier = modifier
      )
    }
  }
}

@Composable
internal fun DeviceListScreen(
  devices: List<DeviceWithType>,
  modifier: Modifier = Modifier,
) {
  Column(modifier) {
    devices.forEach { deviceWithType ->
      DeviceItem(deviceWithType = deviceWithType)
    }
  }
}

@Composable
fun DeviceItem(deviceWithType: DeviceWithType, modifier: Modifier = Modifier) {
  Column(modifier = modifier) {
    Text(text = deviceWithType.device.name, fontSize = 18.sp)
    Text(text = "Type: ${deviceWithType.deviceType.name}", fontSize = 14.sp)
    Text(text = "Price: ${deviceWithType.device.price}", fontSize = 14.sp)
    Text(text = "Status: ${if (deviceWithType.device.isServing) "Serving" else "Retired"}", fontSize = 12.sp)
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeviceListScreenPreview() {
  MyApplicationTheme {
    DeviceListScreen(
      devices = listOf(
        DeviceWithType(
          device = com.example.someapp.data.local.entity.DeviceEntity(
            id = 1,
            name = "iPhone 15",
            icon = "iphone",
            typeId = 1,
            price = 999.99,
            isServing = true,
            purchaseDate = 0
          ),
          deviceType = com.example.someapp.data.local.entity.DeviceTypeEntity(
            id = 1,
            name = "Phone",
            icon = "phone_icon"
          )
        ),
        DeviceWithType(
          device = com.example.someapp.data.local.entity.DeviceEntity(
            id = 1,
            name = "iPhone 15",
            icon = "iphone",
            typeId = 1,
            price = 999.99,
            isServing = true,
            purchaseDate = 0
          ),
          deviceType = com.example.someapp.data.local.entity.DeviceTypeEntity(
            id = 1,
            name = "Phone",
            icon = "phone_icon"
          )
        )
      )
    )
  }
}
