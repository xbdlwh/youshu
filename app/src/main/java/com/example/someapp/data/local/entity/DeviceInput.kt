package com.example.someapp.data.local.entity

data class DeviceInput(
    val name: String,
    val icon: String,
    val typeId: Long,
    val price: Double,
    val isServing: Boolean,
    val purchaseDate: Long? = null
)
