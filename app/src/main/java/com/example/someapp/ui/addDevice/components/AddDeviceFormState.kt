package com.example.someapp.ui.addDevice

import com.example.someapp.data.local.entity.DeviceInput

data class AddDeviceFormState(
    val name: String = "",
    val icon: String = "",
    val typeId: Long = 0L,
    val price: String = "",
    val isServing: Boolean = true,
    val isDropdownExpanded: Boolean = false
) {
    val isValid: Boolean
        get() = name.isNotBlank() && typeId > 0 && price.isNotBlank()

    fun toDeviceInput(): DeviceInput? {
        val priceValue = price.toDoubleOrNull() ?: return null
        return DeviceInput(
            name = name,
            icon = icon,
            typeId = typeId,
            price = priceValue,
            isServing = isServing,
            purchaseDate = System.currentTimeMillis()
        )
    }
}
