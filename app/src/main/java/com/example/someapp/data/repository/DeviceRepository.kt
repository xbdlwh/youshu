package com.example.someapp.data.repository

import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getAllDevices(): Flow<List<DeviceEntity>>
    fun getDevicesByTypeId(typeId: Long): Flow<List<DeviceEntity>>
    fun getDevicesByServingStatus(isServing: Boolean): Flow<List<DeviceEntity>>
    suspend fun getDeviceById(id: Long): DeviceEntity?
    suspend fun insertDevice(device: DeviceEntity): Long
    suspend fun insertDevices(devices: List<DeviceEntity>)
    suspend fun updateDevice(device: DeviceEntity)
    suspend fun deleteDevice(device: DeviceEntity)

    fun getAllDeviceTypes(): Flow<List<DeviceTypeEntity>>
    suspend fun getDeviceTypeById(id: Long): DeviceTypeEntity?
    suspend fun insertDeviceType(deviceType: DeviceTypeEntity): Long
    suspend fun insertDeviceTypes(deviceTypes: List<DeviceTypeEntity>)
    suspend fun updateDeviceType(deviceType: DeviceTypeEntity)
    suspend fun deleteDeviceType(deviceType: DeviceTypeEntity)
}
