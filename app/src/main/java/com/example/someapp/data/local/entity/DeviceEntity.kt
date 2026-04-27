package com.example.someapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class DeviceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String,
    val typeId: Long,
    val price: Double,
    val isServing: Boolean,
    val purchaseDate: Long,
    val soldDate: Long? = null
)
