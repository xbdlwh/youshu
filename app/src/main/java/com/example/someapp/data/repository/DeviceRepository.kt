package com.example.someapp.data.repository

import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

interface DeviceRepository {
    fun getAllDevices(): Flow<List<DeviceEntity>>
    fun getAllDevicesWithType(): Flow<List<DeviceWithType>>
    fun getDevicesByTypeId(typeId: Long): Flow<List<DeviceEntity>>
    fun getDevicesByServingStatus(isServing: Boolean): Flow<List<DeviceEntity>>
    suspend fun getDeviceById(id: Long): DeviceEntity?
    suspend fun insertDevice(device: DeviceEntity): Long
    suspend fun insertDevices(devices: List<DeviceEntity>)
    suspend fun updateDevice(device: DeviceEntity)
    suspend fun deleteDevice(device: DeviceEntity)
    suspend fun deleteDeviceById(id: Long)

    fun getAllDeviceTypes(): Flow<List<DeviceTypeEntity>>
    suspend fun getDeviceTypeById(id: Long): DeviceTypeEntity?
    suspend fun insertDeviceType(deviceType: DeviceTypeEntity): Long
    suspend fun insertDeviceTypes(deviceTypes: List<DeviceTypeEntity>)
    suspend fun updateDeviceType(deviceType: DeviceTypeEntity)
    suspend fun deleteDeviceType(deviceType: DeviceTypeEntity)

    suspend fun getAllData(): AllDevicesAndTypes
}

@Serializable
data class AllDevicesAndTypes(
    val devices: List<DeviceEntity>,
    val types: List<DeviceTypeEntity>
)
