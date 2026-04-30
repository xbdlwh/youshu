package com.example.someapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.someapp.data.local.entity.DeviceWithType

@Composable
fun DeviceCategoryList(
  devices: List<DeviceWithType>,
  deviceTypes: List<com.example.someapp.data.local.entity.DeviceTypeEntity>,
  onDeviceClick: (DeviceWithType) -> Unit,
  deviceItemContent: @Composable (DeviceWithType) -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

  val categories = listOf("全部") + deviceTypes.map { it.name }
  val filteredDevices = if (selectedTabIndex == 0) {
    devices
  } else {
    val selectedTypeId = deviceTypes.getOrNull(selectedTabIndex - 1)?.id ?: 0L
    devices.filter { it.device.typeId == selectedTypeId }
  }

  Column(modifier = modifier) {
    PrimaryScrollableTabRow(
      selectedTabIndex = selectedTabIndex
    ) {
      categories.forEachIndexed { index, category ->
        Tab(
          selected = selectedTabIndex == index,
          onClick = { selectedTabIndex = index },
          text = { Text(category) }
        )
      }
    }

    Spacer(Modifier.height(12.dp))

    LazyColumn {
      items(filteredDevices, key = { it.device.id }) { deviceWithType ->
        deviceItemContent(deviceWithType)
        Spacer(Modifier.height(9.dp))
      }
    }
  }
}
