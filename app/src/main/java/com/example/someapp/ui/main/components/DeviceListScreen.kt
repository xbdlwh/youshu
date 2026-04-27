package com.example.someapp.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.theme.MyApplicationTheme

@Composable
internal fun DeviceListScreen(
  devices: List<DeviceWithType>,
  modifier: Modifier = Modifier,
) {
  Scaffold(modifier,
    floatingActionButton = {
      BottomSheetTrigger(
        trigger = { onClick ->
          androidx.compose.material3.FloatingActionButton(onClick = onClick) {
            Text("+")
          }
        },
        sheetContent = {
          Text("Add your device content here")
        }
      )
    }) {paddingValues ->
    Column(modifier = Modifier
      .padding(paddingValues)
      .padding(horizontal = 8.dp),) {
      devices.forEach { deviceWithType ->
        DeviceItem(deviceWithType = deviceWithType)
        Spacer(Modifier.height(9.dp))
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeviceListScreenPreview() {
  MyApplicationTheme {
    DeviceListScreen(
      devices = listOf(
        DeviceWithType(
          device = DeviceEntity(
            id = 1,
            name = "iPhone 15",
            icon = "iphone",
            typeId = 1,
            price = 999.99,
            isServing = true,
            purchaseDate = 0
          ),
          deviceType = DeviceTypeEntity(
            id = 1,
            name = "Phone",
            icon = "phone_icon"
          )
        ),
        DeviceWithType(
          device = DeviceEntity(
            id = 2,
            name = "Galaxy S24",
            icon = "galaxy",
            typeId = 1,
            price = 899.99,
            isServing = true,
            purchaseDate = 0
          ),
          deviceType = DeviceTypeEntity(
            id = 1,
            name = "Phone",
            icon = "phone_icon"
          )
        )
      )
    )
  }
}
