package com.example.someapp

import android.app.Application
import com.example.someapp.data.DataRepository
import com.example.someapp.data.DefaultDataRepository
import com.example.someapp.data.local.AppDatabase
import com.example.someapp.data.repository.DeviceRepository
import com.example.someapp.data.repository.LocalDeviceRepository

class MyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

class AppContainer(application: Application) {

    val database: AppDatabase by lazy { AppDatabase.getInstance(application) }

    val deviceDao by lazy { database.deviceDao() }
    val deviceTypeDao by lazy { database.deviceTypeDao() }

    val dataRepository: DataRepository by lazy { DefaultDataRepository() }

    val deviceRepository: DeviceRepository by lazy {
        LocalDeviceRepository(deviceDao, deviceTypeDao)
    }
}
