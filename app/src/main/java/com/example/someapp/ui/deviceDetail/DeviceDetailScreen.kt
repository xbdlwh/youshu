package com.example.someapp.ui.deviceDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.someapp.MyViewModelProvider
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceWithType
import java.io.File
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
  modifier: Modifier = Modifier,
  deviceId: Long,
  onNavigateBack: () -> Unit,
  onNavigateToEdit: (DeviceWithType) -> Unit,
  viewModel: DeviceDetailViewModel = viewModel(factory = MyViewModelProvider.FACTORY),
) {

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(deviceId) {
    viewModel.loadDevice(deviceId)
  }
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text("设备详情") },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          val state = uiState
          if (state is DeviceDetailUiState.Success) {
            IconButton(onClick = { onNavigateToEdit(state.deviceWithType) }) {
              Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
          }
        }
      )
    }
  ) { paddingValues ->
    when (val state = uiState) {
      DeviceDetailUiState.Loading -> {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
          contentAlignment = Alignment.Center
        ) {
          Text("加载中...")
        }
      }
      DeviceDetailUiState.NotFound -> {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
          contentAlignment = Alignment.Center
        ) {
          Text("设备不存在")
        }
      }
      is DeviceDetailUiState.Success -> {
        DeviceDetailContent(
          deviceWithType = state.deviceWithType,
          modifier = Modifier.padding(paddingValues)
        )
      }
    }
  }
}

@Composable
private fun DeviceDetailContent(
  deviceWithType: DeviceWithType,
  modifier: Modifier = Modifier,
) {
  val device = deviceWithType.device
  val deviceType = deviceWithType.deviceType

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // 设备图片
    Box(
      modifier = Modifier
        .size(120.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer),
      contentAlignment = Alignment.Center
    ) {
      if (device.icon.isNotBlank() && File(device.icon).exists()) {
        AsyncImage(
          model = File(device.icon),
          contentDescription = device.name,
          modifier = Modifier
            .size(120.dp)
            .clip(CircleShape),
          contentScale = ContentScale.Crop
        )
      } else {
        Text(
          text = device.name.first().toString(),
          style = MaterialTheme.typography.displayMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }
    }

    Spacer(Modifier.height(24.dp))

    // 设备名称
    Text(
      text = device.name,
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(8.dp))

    // 设备类型
    Text(
      text = deviceType.name,
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(Modifier.height(24.dp))

    // 详情卡片
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      )
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        DetailRow(label = "价格", value = device.price.currencyText())
        DetailRow(label = "日均成本", value = device.dailyCost().currencyText())
        DetailRow(label = "持有天数", value = "${device.ownedDays()} 天")
        DetailRow(label = "状态", value = if (device.isServing) "使用中" else "已停用")
      }
    }
  }
}

@Composable
private fun DetailRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.SemiBold
    )
  }
}

private fun Double.currencyText() = "$${String.format("%.2f", this)}"

private fun DeviceEntity.dailyCost(): Double = price / ownedDays()

private fun DeviceEntity.ownedDays(): Long {
  val currentTimeMillis = System.currentTimeMillis()
  val endTime = soldDate ?: currentTimeMillis
  val durationMillis = max(endTime - purchaseDate, MILLIS_PER_DAY)
  return max(1L, durationMillis / MILLIS_PER_DAY)
}

private const val MILLIS_PER_DAY = 24L * 60L * 60L * 1000L
