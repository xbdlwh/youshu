package com.example.someapp

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.someapp.ui.main.MainScreenViewModel

class MyViewModelProvider {
    companion object{
        val FACTORY = viewModelFactory {
            initializer {
                MainScreenViewModel(container().deviceRepository)
            }
        }
    }
}

fun CreationExtras.container() : AppContainer =
    (this[APPLICATION_KEY] as MyApplication).container
