package com.example.someapp.ui.deviceDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

  fun loadDevice(id: Long) {
    viewModelScope.launch {
      _uiState.value = DeviceDetailUiState.Success(deviceWithType =  deviceRepository.getAllDevicesWithType().first().find { it.device.id == id } !!)
    }
  }

}

sealed interface DeviceDetailUiState {
  data object Loading : DeviceDetailUiState
  data object NotFound : DeviceDetailUiState
  data class Success(val deviceWithType: DeviceWithType) : DeviceDetailUiState
}
