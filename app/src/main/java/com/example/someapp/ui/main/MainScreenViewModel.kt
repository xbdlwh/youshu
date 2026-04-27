package com.example.someapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.data.repository.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainScreenViewModel(deviceRepository: DeviceRepository) : ViewModel() {

  val uiState: StateFlow<MainScreenUiState> = deviceRepository.getAllDevicesWithType()
    .map { devices ->
      MainScreenUiState.Success(devices)
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = MainScreenUiState.Loading
    )
}

sealed interface MainScreenUiState {
  object Loading : MainScreenUiState

  data class Success(val devices: List<DeviceWithType>) : MainScreenUiState
}
