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
    val purchaseDate: Long = System.currentTimeMillis(),
    val newTypeName: String = "",
    val newTypeIcon: String = ""
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

  init {
    seedDefaultDeviceTypes()
  }

  fun updateFormState(state: AddDeviceFormState) {
    _formState.value = state
  }

  fun selectDeviceType(typeId: Long) {
    _formState.value = _formState.value.copy(typeId = typeId)
  }

  fun dismissAddTypeDialog() {
    _formState.value = _formState.value.copy(
      newTypeName = "",
      newTypeIcon = ""
    )
  }

  fun addDeviceType(): Boolean {
    val state = _formState.value
    val name = state.newTypeName.trim()
    val icon = state.newTypeIcon.trim()
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
        typeId = id,
        newTypeName = "",
        newTypeIcon = ""
      )
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
