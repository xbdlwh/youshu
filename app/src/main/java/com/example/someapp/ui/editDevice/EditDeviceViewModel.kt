package com.example.someapp.ui.editDevice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.data.repository.DeviceRepository
import com.example.someapp.ui.addDevice.AddDeviceFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditDeviceViewModel(
    val deviceRepository: DeviceRepository,
) : ViewModel() {

  var _uiState: MutableStateFlow<EditDeviceUiState> = MutableStateFlow(EditDeviceUiState.Loading);
  val uiState: StateFlow<EditDeviceUiState> = _uiState.asStateFlow()

  private var editingDevice: DeviceEntity? = null

  val deviceTypes: StateFlow<List<com.example.someapp.data.local.entity.DeviceTypeEntity>> = deviceRepository.getAllDeviceTypes()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  fun loadDevice(deviceWithType: DeviceWithType) {
    editingDevice = deviceWithType.device
    val device = deviceWithType.device
    _uiState.value = EditDeviceUiState.Ready(
      formState = AddDeviceFormState(
        name = device.name,
        icon = device.icon,
        typeId = device.typeId,
        price = device.price.toString(),
        isServing = device.isServing,
        purchaseDate = device.purchaseDate
      )
    )
  }

  fun updateFormState(state: AddDeviceFormState) {
    _uiState.value = EditDeviceUiState.Ready(state)
  }

  fun selectDeviceType(typeId: Long) {
    val currentState = _uiState.value
    if (currentState is EditDeviceUiState.Ready) {
      _uiState.value = EditDeviceUiState.Ready(currentState.formState.copy(typeId = typeId))
    }
  }

  fun updateDevice(): Boolean {
    val currentState = _uiState.value
    if (currentState !is EditDeviceUiState.Ready) return false

    val formState = currentState.formState
    val price = formState.price.toDoubleOrNull() ?: return false
    if (!formState.isValid) return false

    val device = editingDevice ?: return false

    viewModelScope.launch {
      deviceRepository.updateDevice(
        device.copy(
          name = formState.name,
          icon = formState.icon,
          typeId = formState.typeId,
          price = price,
          isServing = formState.isServing,
          purchaseDate = formState.purchaseDate
        )
      )
    }
    return true
  }
}

sealed interface EditDeviceUiState {
  data object Loading : EditDeviceUiState
  data class Ready(val formState: AddDeviceFormState) : EditDeviceUiState
}
