package com.example.someapp.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import java.io.File

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
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Device Image
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        if (device.icon.isNotBlank() && File(device.icon).exists()) {
          AsyncImage(
            model = File(device.icon),
            contentDescription = device.name,
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape),
            contentScale = ContentScale.Crop
          )
        } else {
          Text(
            text = device.name.first().toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        }
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Center: Device Info
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = device.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
          )
          Spacer(modifier = Modifier.width(8.dp))
          StatusBadge(isServing = deviceWithType.device.isServing)
        }

        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = deviceWithType.deviceType.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "$ownedDays days",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          InfoChip(
            label = "$${String.format("%.2f", device.price)}"
          )
          InfoChip(
            label = "${dailyCost.currencyText()}/day",
            highlighted = true
          )
        }
      }
    }
  }
}

@Composable
private fun StatusBadge(isServing: Boolean) {
  val backgroundColor = if (isServing) {
    MaterialTheme.colorScheme.primaryContainer
  } else {
    MaterialTheme.colorScheme.errorContainer
  }
  val textColor = if (isServing) {
    MaterialTheme.colorScheme.onPrimaryContainer
  } else {
    MaterialTheme.colorScheme.onErrorContainer
  }
  val text = if (isServing) "Active" else "Retired"

  Box(
    modifier = Modifier
      .clip(MaterialTheme.shapes.small)
      .background(backgroundColor)
      .padding(horizontal = 8.dp, vertical = 2.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall,
      color = textColor,
      fontWeight = FontWeight.Medium
    )
  }
}

@Composable
private fun InfoChip(
  label: String,
  highlighted: Boolean = false
) {
  Box(
    modifier = Modifier
      .clip(MaterialTheme.shapes.extraSmall)
      .background(
        if (highlighted) {
          MaterialTheme.colorScheme.primaryContainer
        } else {
          MaterialTheme.colorScheme.surfaceVariant
        }
      )
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      color = if (highlighted) {
        MaterialTheme.colorScheme.onPrimaryContainer
      } else {
        MaterialTheme.colorScheme.onSurfaceVariant
      },
      fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Normal
    )
  }
}