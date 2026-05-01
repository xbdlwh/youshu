package com.example.someapp.ui.deviceDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.data.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeviceDetailViewModel(
  val deviceRepository: DeviceRepository,
) : ViewModel() {

  var _uiState: MutableStateFlow<DeviceDetailUiState> = MutableStateFlow(DeviceDetailUiState.Loading);
  val uiState: StateFlow<DeviceDetailUiState> = _uiState.asStateFlow()

  private var currentDeviceId: Long = 0L

  fun loadDevice(id: Long) {
    currentDeviceId = id
    viewModelScope.launch {
      _uiState.value = DeviceDetailUiState.Loading
      val deviceWithType = deviceRepository.getAllDevicesWithType().first().find { it.device.id == id }
      _uiState.value = if (deviceWithType != null) {
        DeviceDetailUiState.Success(deviceWithType)
      } else {
        DeviceDetailUiState.NotFound
      }
    }
  }

  fun deleteDevice(onDeleted: () -> Unit) {
    viewModelScope.launch {
      deviceRepository.deleteDeviceById(currentDeviceId)
      onDeleted()
    }
  }
}

sealed interface DeviceDetailUiState {
  data object Loading : DeviceDetailUiState
  data object NotFound : DeviceDetailUiState
  data class Success(val deviceWithType: DeviceWithType) : DeviceDetailUiState
}