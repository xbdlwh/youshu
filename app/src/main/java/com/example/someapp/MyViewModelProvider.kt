package com.example.someapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.someapp.data.repository.DeviceRepository
import com.example.someapp.ui.addDevice.AddDeviceViewModel
import com.example.someapp.ui.deviceDetail.DeviceDetailViewModel
import com.example.someapp.ui.editDevice.EditDeviceViewModel
import com.example.someapp.ui.main.MainScreenViewModel

class MyViewModelProvider {
    companion object{
        val FACTORY = viewModelFactory {
            initializer {
                MainScreenViewModel(container().deviceRepository)
            }
            initializer {
                AddDeviceViewModel(container().deviceRepository)
            }
            initializer {
                DeviceDetailViewModel(container().deviceRepository)
            }
            initializer {
                EditDeviceViewModel(container().deviceRepository)
            }
        }
    }
}

fun CreationExtras.container() : AppContainer =
    (this[APPLICATION_KEY] as MyApplication).container
