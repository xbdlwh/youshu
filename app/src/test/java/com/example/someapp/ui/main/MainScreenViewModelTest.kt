package com.example.someapp.ui.main

import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.data.repository.DeviceRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainScreenViewModelTest {
  @Test
  fun uiState_initiallyLoading() = runTest {
    val viewModel = MainScreenViewModel(FakeDeviceRepository())

    assertEquals(MainScreenUiState.Loading, viewModel.uiState.first())
  }
}

private class FakeDeviceRepository : DeviceRepository {
  private val devices = MutableStateFlow<List<DeviceEntity>>(emptyList())
  private val deviceTypes = MutableStateFlow<List<DeviceTypeEntity>>(emptyList())
  private val devicesWithType = MutableStateFlow<List<DeviceWithType>>(emptyList())

  override fun getAllDevices(): Flow<List<DeviceEntity>> = devices

  override fun getAllDevicesWithType(): Flow<List<DeviceWithType>> = devicesWithType

  override fun getDevicesByTypeId(typeId: Long): Flow<List<DeviceEntity>> = devices

  override fun getDevicesByServingStatus(isServing: Boolean): Flow<List<DeviceEntity>> = devices

  override suspend fun getDeviceById(id: Long): DeviceEntity? = devices.value.firstOrNull { it.id == id }

  override suspend fun insertDevice(device: DeviceEntity): Long {
    val id = devices.value.size + 1L
    devices.value += device.copy(id = id)
    return id
  }

  override suspend fun insertDevices(devices: List<DeviceEntity>) {
    devices.forEach { insertDevice(it) }
  }

  override suspend fun updateDevice(device: DeviceEntity) {
    devices.value = devices.value.map { if (it.id == device.id) device else it }
  }

  override suspend fun deleteDevice(device: DeviceEntity) {
    devices.value = devices.value.filterNot { it.id == device.id }
  }

  override fun getAllDeviceTypes(): Flow<List<DeviceTypeEntity>> = deviceTypes

  override suspend fun getDeviceTypeById(id: Long): DeviceTypeEntity? =
    deviceTypes.value.firstOrNull { it.id == id }

  override suspend fun insertDeviceType(deviceType: DeviceTypeEntity): Long {
    val id = deviceTypes.value.size + 1L
    deviceTypes.value += deviceType.copy(id = id)
    return id
  }

  override suspend fun insertDeviceTypes(deviceTypes: List<DeviceTypeEntity>) {
    deviceTypes.forEach { insertDeviceType(it) }
  }

  override suspend fun updateDeviceType(deviceType: DeviceTypeEntity) {
    deviceTypes.value = deviceTypes.value.map { if (it.id == deviceType.id) deviceType else it }
  }

  override suspend fun deleteDeviceType(deviceType: DeviceTypeEntity) {
    deviceTypes.value = deviceTypes.value.filterNot { it.id == deviceType.id }
  }
}
