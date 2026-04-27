package com.example.someapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.someapp.data.local.entity.DeviceInput
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.data.repository.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(private val deviceRepository: DeviceRepository) : ViewModel() {

  private val _deviceTypes = deviceRepository.getAllDeviceTypes()

  val uiState: StateFlow<MainScreenUiState> = combine(
    deviceRepository.getAllDevicesWithType(),
    _deviceTypes
  ) { devices, deviceTypes ->
    MainScreenUiState.Success(
      devices = devices,
      deviceTypes = deviceTypes
    )
  }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = MainScreenUiState.Loading
    )

  fun addDevice(input: DeviceInput) {
    viewModelScope.launch {
      deviceRepository.insertDevice(
        com.example.someapp.data.local.entity.DeviceEntity(
          name = input.name,
          icon = input.icon,
          typeId = input.typeId,
          price = input.price,
          isServing = input.isServing,
          purchaseDate = input.purchaseDate ?: System.currentTimeMillis()
        )
      )
    }
  }
}

sealed interface MainScreenUiState {
  object Loading : MainScreenUiState

  data class Success(
    val devices: List<DeviceWithType>,
    val deviceTypes: List<DeviceTypeEntity>
  ) : MainScreenUiState
}
