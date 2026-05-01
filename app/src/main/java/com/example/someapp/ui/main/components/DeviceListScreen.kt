package com.example.someapp.ui.main.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceInput
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.theme.MyApplicationTheme
import com.example.someapp.ui.components.DeviceCategoryList
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeviceListScreen(
  devices: List<DeviceWithType>,
  modifier: Modifier = Modifier,
  deviceTypes: List<DeviceTypeEntity> = emptyList(),
  onDeviceAdded: (DeviceInput) -> Unit = {},
  onNavigateToAdd: () -> Unit = {},
  onDeviceClick: (Long) -> Unit = {},
  onExportJson: () -> String = { "{}" },
) {
  val context = LocalContext.current
  Scaffold(modifier,
    topBar = {
      TopAppBar(title = { Text("我的财产💰") }, actions = {
        IconButton(onClick = {
          kotlinx.coroutines.MainScope().launch {
            val json = onExportJson()
            if (json != "{}") {
              val file = File(context.cacheDir, "devices_export.json")
              file.writeText(json)
              val uri = androidx.core.content.FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
              )
              val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
              }
              context.startActivity(Intent.createChooser(intent, "导出设备数据"))
            }
          }
        }) {
          Icon(Icons.Default.Share, contentDescription = "")
        }
      })
    },
    floatingActionButton = {

      Button (
        onClick = onNavigateToAdd,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//          containerColor = MaterialTheme.colorScheme.primary
      ) {
        Icon(Icons.Default.Add, contentDescription = "Add Device")
      }
//      Row(Modifier.fillMaxWidth().background(Color.Red)) {
//      }
    }) { paddingValues ->
    Column(modifier = Modifier
      .padding(paddingValues)
      .padding(horizontal = 8.dp)) {
      AssetSummaryCard(devices = devices)
      Spacer(Modifier.height(12.dp))
      DeviceCategoryList(
        devices = devices,
        deviceTypes = deviceTypes,
        onDeviceClick = { deviceWithType -> onDeviceClick(deviceWithType.device.id) },
        deviceItemContent = { deviceWithType ->
          DeviceItem(deviceWithType = deviceWithType)
        }
      )
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
