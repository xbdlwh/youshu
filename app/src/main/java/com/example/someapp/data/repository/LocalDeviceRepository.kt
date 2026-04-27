package com.example.someapp.data.repository

import com.example.someapp.data.local.dao.DeviceDao
import com.example.someapp.data.local.dao.DeviceTypeDao
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import kotlinx.coroutines.flow.Flow

class LocalDeviceRepository(
    private val deviceDao: DeviceDao,
    private val deviceTypeDao: DeviceTypeDao
) : DeviceRepository {

    override fun getAllDevices(): Flow<List<DeviceEntity>> = deviceDao.getAll()

    override fun getDevicesByTypeId(typeId: Long): Flow<List<DeviceEntity>> =
        deviceDao.getByTypeId(typeId)

    override fun getDevicesByServingStatus(isServing: Boolean): Flow<List<DeviceEntity>> =
        deviceDao.getByServingStatus(isServing)

    override suspend fun getDeviceById(id: Long): DeviceEntity? =
        deviceDao.getById(id)

    override suspend fun insertDevice(device: DeviceEntity): Long =
        deviceDao.insert(device)

    override suspend fun insertDevices(devices: List<DeviceEntity>) =
        deviceDao.insertAll(devices)

    override suspend fun updateDevice(device: DeviceEntity) =
        deviceDao.update(device)

    override suspend fun deleteDevice(device: DeviceEntity) =
        deviceDao.delete(device)

    override fun getAllDeviceTypes(): Flow<List<DeviceTypeEntity>> =
        deviceTypeDao.getAll()

    override suspend fun getDeviceTypeById(id: Long): DeviceTypeEntity? =
        deviceTypeDao.getById(id)

    override suspend fun insertDeviceType(deviceType: DeviceTypeEntity): Long =
        deviceTypeDao.insert(deviceType)

    override suspend fun insertDeviceTypes(deviceTypes: List<DeviceTypeEntity>) =
        deviceTypeDao.insertAll(deviceTypes)

    override suspend fun updateDeviceType(deviceType: DeviceTypeEntity) =
        deviceTypeDao.update(deviceType)

    override suspend fun deleteDeviceType(deviceType: DeviceTypeEntity) =
        deviceTypeDao.delete(deviceType)
}
