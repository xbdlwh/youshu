package com.example.someapp.ui.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.theme.MyApplicationTheme

@Composable
fun AssetSummaryCard(
  devices: List<DeviceWithType>,
  modifier: Modifier = Modifier,
  currentTimeMillis: Long = System.currentTimeMillis(),
) {
  val totalAssets = devices
    .filter { it.device.isServing }
    .sumOf { it.device.price }
  val dailyCost = devices.sumOf { deviceWithType ->
    deviceWithType.device.dailyCost(currentTimeMillis)
  }

  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      SummaryMetric(
        label = "Total Assets",
        value = totalAssets.currencyText(),
        modifier = Modifier.weight(1f)
      )
      SummaryMetric(
        label = "Daily Cost",
        value = "${dailyCost.currencyText()}/day",
        modifier = Modifier.weight(1f)
      )
    }
  }
}

@Composable
private fun SummaryMetric(
  label: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onPrimaryContainer
    )
    Text(
      text = value,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onPrimaryContainer
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun AssetSummaryCardPreview() {
  MyApplicationTheme {
    AssetSummaryCard(
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
          deviceType = DeviceTypeEntity(id = 1, name = "Phone", icon = "phone")
        ),
        DeviceWithType(
          device = DeviceEntity(
            id = 2,
            name = "Laptop",
            icon = "laptop",
            typeId = 2,
            price = 1499.99,
            isServing = true,
            purchaseDate = 0
          ),
          deviceType = DeviceTypeEntity(id = 2, name = "Laptop", icon = "laptop")
        )
      )
    )
  }
}
