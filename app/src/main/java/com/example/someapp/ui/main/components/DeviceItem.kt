package com.example.someapp.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.theme.MyApplicationTheme

@Composable
fun DeviceItem(
  deviceWithType: DeviceWithType,
  modifier: Modifier = Modifier,
  currentTimeMillis: Long = System.currentTimeMillis(),
) {
  val device = deviceWithType.device
  val dailyCost = device.dailyCost(currentTimeMillis)
  val ownedDays = device.ownedDays(currentTimeMillis)

  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = deviceWithType.device.name.first().toString(),
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(
          text = device.name,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.SemiBold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = deviceWithType.deviceType.name,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(
          text = "${dailyCost.currencyText()}/day",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = device.price.currencyText(),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = "$ownedDays days",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StatusBadge(isServing = deviceWithType.device.isServing)
      }
    }
  }
}

@Composable
private fun StatusBadge(isServing: Boolean) {
  val backgroundColor = if (isServing) {
    MaterialTheme.colorScheme.tertiaryContainer
  } else {
    MaterialTheme.colorScheme.errorContainer
  }
  val textColor = if (isServing) {
    MaterialTheme.colorScheme.onTertiaryContainer
  } else {
    MaterialTheme.colorScheme.onErrorContainer
  }
  val text = if (isServing) "Serving" else "Retired"

  Box(
    modifier = Modifier
      .clip(MaterialTheme.shapes.small)
      .background(backgroundColor)
      .padding(horizontal = 8.dp, vertical = 2.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall,
      color = textColor
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun DeviceItemPreview() {
  MyApplicationTheme {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      DeviceItem(
        deviceWithType = DeviceWithType(
          device = DeviceEntity(
            id = 1,
            name = "iPhone 15 Pro",
            icon = "iphone",
            typeId = 1,
            price = 999.99,
            isServing = true,
            purchaseDate = 0
          ),
          deviceType = DeviceTypeEntity(id = 1, name = "Phone", icon = "phone_icon")
        )
      )
      DeviceItem(
        deviceWithType = DeviceWithType(
          device = DeviceEntity(
            id = 2,
            name = "Galaxy S24 Ultra",
            icon = "galaxy",
            typeId = 1,
            price = 1199.99,
            isServing = false,
            purchaseDate = 0
          ),
          deviceType = DeviceTypeEntity(id = 1, name = "Phone", icon = "phone_icon")
        )
      )
    }
  }
}
