package com.example.someapp.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.someapp.MyViewModelProvider
import com.example.someapp.ui.main.components.DeviceListScreen

@Composable
fun MainScreen(
  onNavigateToAdd: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: MainScreenViewModel = viewModel(factory = MyViewModelProvider.FACTORY),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  when (state) {
    MainScreenUiState.Loading -> {
      Text("Loading...")
    }
    is MainScreenUiState.Success -> {
      val successState = state as MainScreenUiState.Success
      DeviceListScreen(
        devices = successState.devices,
        deviceTypes = successState.deviceTypes,
        onDeviceAdded = { input -> viewModel.addDevice(input) },
        onNavigateToAdd = onNavigateToAdd,
        modifier = modifier
      )
    }
  }
}
