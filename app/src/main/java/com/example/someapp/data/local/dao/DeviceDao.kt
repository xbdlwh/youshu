package com.example.someapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.someapp.data.local.entity.DeviceEntity
import com.example.someapp.data.local.entity.DeviceWithType
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device")
    fun getAll(): Flow<List<DeviceEntity>>

    @Transaction
    @Query("SELECT * FROM device")
    fun getAllWithType(): Flow<List<DeviceWithType>>

    @Query("SELECT * FROM device WHERE id = :id")
    suspend fun getById(id: Long): DeviceEntity?

    @Query("SELECT * FROM device WHERE typeId = :typeId")
    fun getByTypeId(typeId: Long): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM device WHERE isServing = :isServing")
    fun getByServingStatus(isServing: Boolean): Flow<List<DeviceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: DeviceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devices: List<DeviceEntity>)

    @Update
    suspend fun update(device: DeviceEntity)

    @Delete
    suspend fun delete(device: DeviceEntity)

    @Query("DELETE FROM device")
    suspend fun deleteAll()
}
