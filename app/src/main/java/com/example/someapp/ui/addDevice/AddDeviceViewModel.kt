package com.example.someapp.ui.addDevice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceInput
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddDeviceViewModel(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {

  private val _formState = MutableStateFlow(AddDeviceFormState())
  val formState: StateFlow<AddDeviceFormState> = _formState.asStateFlow()

  val deviceTypes: StateFlow<List<DeviceTypeEntity>> = deviceRepository.getAllDeviceTypes()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Companion.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  fun updateFormState(state: AddDeviceFormState) {
    _formState.value = state
  }

  fun addDevice() {
    val input = _formState.value.toDeviceInput() ?: return
    viewModelScope.launch {
      deviceRepository.insertDevice(
          DeviceEntity(
              name = input.name,
              icon = input.icon,
              typeId = input.typeId,
              price = input.price,
              isServing = input.isServing,
              purchaseDate = input.purchaseDate ?: System.currentTimeMillis()
          )
      )
    }
    _formState.value = AddDeviceFormState()
  }
}
