package com.example.someapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DeviceWithType(
    @Embedded
    val device: DeviceEntity,
    @Relation(
        parentColumn = "typeId",
        entityColumn = "id"
    )
    val deviceType: DeviceTypeEntity
)
