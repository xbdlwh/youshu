package com.example.someapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import java.io.File

@Composable
fun DeviceCategoryList(
  devices: List<DeviceWithType>,
  deviceTypes: List<DeviceTypeEntity>,
  onDeviceClick: (DeviceWithType) -> Unit,
  deviceItemContent: @Composable (DeviceWithType) -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

  // First tab is "全部", then device types
  val tabs = listOf<TabItem>(TabItem(null, "全部")) + deviceTypes.map { TabItem(it, it.name) }
  val filteredDevices = if (selectedTabIndex == 0) {
    devices
  } else {
    val selectedTypeId = deviceTypes.getOrNull(selectedTabIndex - 1)?.id ?: 0L
    devices.filter { it.device.typeId == selectedTypeId }
  }

  Column(modifier = modifier) {
    SecondaryScrollableTabRow(
      selectedTabIndex = selectedTabIndex
    ) {
      tabs.forEachIndexed { index, tab ->
        Tab(
          selected = selectedTabIndex == index,
          onClick = { selectedTabIndex = index }
        ) {
          Row(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (tab.deviceType != null && tab.deviceType.icon.isNotBlank() && File(tab.deviceType.icon).exists()) {
              AsyncImage(
                model = File(tab.deviceType.icon),
                contentDescription = null,
                modifier = Modifier
                  .size(20.dp)
                  .clip(CircleShape),
                contentScale = ContentScale.Crop
              )
              Spacer(modifier = Modifier.size(6.dp))
            }
            Text(tab.title)
          }
        }
      }
    }

    Spacer(Modifier.height(12.dp))

    LazyColumn {
      items(filteredDevices, key = { it.device.id }) { deviceWithType ->
        Column(
          modifier = Modifier.clickable { onDeviceClick(deviceWithType) }
        ) {
          deviceItemContent(deviceWithType)
        }
        Spacer(Modifier.height(9.dp))
      }
      item {
        Spacer(Modifier.height(80.dp))
      }
    }
  }
}

private data class TabItem(
  val deviceType: DeviceTypeEntity?,
  val title: String
)