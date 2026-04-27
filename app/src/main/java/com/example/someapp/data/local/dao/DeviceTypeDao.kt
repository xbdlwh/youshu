package com.example.someapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.someapp.data.local.entity.DeviceTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceTypeDao {
    @Query("SELECT * FROM device_type")
    fun getAll(): Flow<List<DeviceTypeEntity>>

    @Query("SELECT * FROM device_type WHERE id = :id")
    suspend fun getById(id: Long): DeviceTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deviceType: DeviceTypeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(deviceTypes: List<DeviceTypeEntity>)

    @Update
    suspend fun update(deviceType: DeviceTypeEntity)

    @Delete
    suspend fun delete(deviceType: DeviceTypeEntity)

    @Query("DELETE FROM device_type")
    suspend fun deleteAll()
}
