package com.example.someapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.someapp.data.local.dao.DeviceDao
import com.example.someapp.data.local.dao.DeviceTypeDao
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceTypeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var deviceDao: DeviceDao
    private lateinit var deviceTypeDao: DeviceTypeDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        deviceDao = database.deviceDao()
        deviceTypeDao = database.deviceTypeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun deviceType_insertAndGet() = runTest {
        val deviceType = DeviceTypeEntity(name = "Phone", icon = "phone_icon")
        deviceTypeDao.insert(deviceType)

        val result = deviceTypeDao.getAll().first()
        assertEquals(1, result.size)
        assertEquals("Phone", result[0].name)
        assertEquals("phone_icon", result[0].icon)
    }

    @Test
    fun device_insertAndGet() = runTest {
        val deviceType = DeviceTypeEntity(name = "Phone", icon = "phone_icon")
        deviceTypeDao.insert(deviceType)

        val device = DeviceEntity(
            name = "iPhone 15",
            icon = "iphone_icon",
            typeId = 1,
            price = 999.99,
            isServing = true,
            purchaseDate = System.currentTimeMillis()
        )
        deviceDao.insert(device)

        val result = deviceDao.getAll().first()
        assertEquals(1, result.size)
        assertEquals("iPhone 15", result[0].name)
        assertEquals(999.99, result[0].price, 0.01)
        assertTrue(result[0].isServing)
    }

    @Test
    fun device_getByTypeId() = runTest {
        val deviceType = DeviceTypeEntity(name = "Phone", icon = "phone_icon")
        deviceTypeDao.insert(deviceType)

        val device1 = DeviceEntity(name = "iPhone 15", icon = "i", typeId = 1, price = 999.0, isServing = true, purchaseDate = 0)
        val device2 = DeviceEntity(name = "Galaxy S24", icon = "g", typeId = 1, price = 899.0, isServing = true, purchaseDate = 0)
        val device3 = DeviceEntity(name = "iPad", icon = "i", typeId = 2, price = 799.0, isServing = true, purchaseDate = 0)
        deviceDao.insertAll(listOf(device1, device2, device3))

        val result = deviceDao.getByTypeId(1).first()
        assertEquals(2, result.size)
        assertTrue(result.all { it.typeId == 1L })
    }

    @Test
    fun device_getByServingStatus() = runTest {
        val device1 = DeviceEntity(name = "Serving Device", icon = "i", typeId = 1, price = 100.0, isServing = true, purchaseDate = 0)
        val device2 = DeviceEntity(name = "Retired Device", icon = "i", typeId = 1, price = 50.0, isServing = false, purchaseDate = 0)
        deviceDao.insertAll(listOf(device1, device2))

        val serving = deviceDao.getByServingStatus(true).first()
        val retired = deviceDao.getByServingStatus(false).first()

        assertEquals(1, serving.size)
        assertEquals("Serving Device", serving[0].name)
        assertEquals(1, retired.size)
        assertEquals("Retired Device", retired[0].name)
    }

    @Test
    fun device_update() = runTest {
        val device = DeviceEntity(name = "Original", icon = "i", typeId = 1, price = 100.0, isServing = true, purchaseDate = 0)
        deviceDao.insert(device)

        val inserted = deviceDao.getAll().first()[0]
        deviceDao.update(inserted.copy(name = "Updated", isServing = false))

        val result = deviceDao.getAll().first()[0]
        assertEquals("Updated", result.name)
        assertFalse(result.isServing)
    }

    @Test
    fun device_delete() = runTest {
        val device = DeviceEntity(name = "To Delete", icon = "i", typeId = 1, price = 100.0, isServing = true, purchaseDate = 0)
        deviceDao.insert(device)

        val inserted = deviceDao.getAll().first()[0]
        deviceDao.delete(inserted)

        assertTrue(deviceDao.getAll().first().isEmpty())
    }
}
