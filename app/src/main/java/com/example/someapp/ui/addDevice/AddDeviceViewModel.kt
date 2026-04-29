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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class AddDeviceFormState(
    val name: String = "",
    val icon: String = "",
    val typeId: Long = 0L,
    val price: String = "",
    val isServing: Boolean = true,
    val purchaseDate: Long = System.currentTimeMillis()
) {
    val isValid: Boolean
        get() = name.isNotBlank() && typeId > 0 && price.toDoubleOrNull() != null

    fun toDeviceInput(): DeviceInput? {
        val priceValue = price.toDoubleOrNull() ?: return null
        return DeviceInput(
            name = name,
            icon = icon,
            typeId = typeId,
            price = priceValue,
            isServing = isServing,
            purchaseDate = purchaseDate
        )
    }
}


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

  val devices: StateFlow<List<DeviceEntity>> = deviceRepository.getAllDevices()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Companion.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  init {
    seedDefaultDeviceTypes()
  }

  fun updateFormState(state: AddDeviceFormState) {
    _formState.value = state
  }

  fun selectDeviceType(typeId: Long) {
    _formState.value = _formState.value.copy(typeId = typeId)
  }

  fun addDeviceType(name: String, icon: String): Boolean {
    val name = name.trim()
    val icon = icon.trim()
    if (name.isEmpty() || icon.isEmpty()) return false
    if (deviceTypes.value.any { it.name.equals(name, ignoreCase = true) }) return false

    viewModelScope.launch {
      val id = deviceRepository.insertDeviceType(
        DeviceTypeEntity(
          name = name,
          icon = icon
        )
      )
      _formState.value = _formState.value.copy(
        typeId = id
      )
    }
    return true
  }

  fun updateDeviceType(
    type: DeviceTypeEntity,
    name: String,
    icon: String
  ): Boolean {
    val name = name.trim()
    val icon = icon.trim()
    if (name.isEmpty() || icon.isEmpty()) return false
    if (deviceTypes.value.any { it.id != type.id && it.name.equals(name, ignoreCase = true) }) return false

    viewModelScope.launch {
      deviceRepository.updateDeviceType(
        type.copy(
          name = name,
          icon = icon
        )
      )
    }
    return true
  }

  fun deleteDeviceType(type: DeviceTypeEntity): Boolean {
    if (devices.value.any { it.typeId == type.id }) return false

    viewModelScope.launch {
      deviceRepository.deleteDeviceType(type)
      if (_formState.value.typeId == type.id) {
        _formState.value = _formState.value.copy(typeId = 0L)
      }
    }
    return true
  }

  fun addDevice(): Boolean {
    val input = _formState.value.toDeviceInput() ?: return false
    viewModelScope.launch {
      deviceRepository.insertDevice(
        DeviceEntity(
          name = input.name,
          icon = input.icon.ifBlank { "devices" },
          typeId = input.typeId,
          price = input.price,
          isServing = input.isServing,
          purchaseDate = input.purchaseDate ?: System.currentTimeMillis()
        )
      )
    }
    _formState.value = AddDeviceFormState()
    return true
  }

  private fun seedDefaultDeviceTypes() {
    viewModelScope.launch {
      if (deviceRepository.getAllDeviceTypes().first().isEmpty()) {
        deviceRepository.insertDeviceTypes(defaultDeviceTypes)
      }
    }
  }

  private companion object {
    val defaultDeviceTypes = listOf(
      DeviceTypeEntity(name = "Phone", icon = "phone"),
      DeviceTypeEntity(name = "Tablet", icon = "tablet"),
      DeviceTypeEntity(name = "Laptop", icon = "laptop"),
      DeviceTypeEntity(name = "Wearable", icon = "watch"),
      DeviceTypeEntity(name = "Other", icon = "devices")
    )
  }
}
