package com.example.someapp.ui.editDevice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.someapp.MyViewModelProvider
import com.example.someapp.data.local.entity.DeviceWithType
import com.example.someapp.ui.editDevice.components.EditDeviceScaffold

@Composable
fun EditDeviceScreen(
  deviceWithType: DeviceWithType,
  onNavigateBack: () -> Unit,
  viewModel: EditDeviceViewModel = viewModel(factory = MyViewModelProvider.FACTORY),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val deviceTypes by viewModel.deviceTypes.collectAsStateWithLifecycle()

  LaunchedEffect(deviceWithType) {
    viewModel.loadDevice(deviceWithType)
  }

  when (val state = uiState) {
    EditDeviceUiState.Loading -> {
      // Loading handled in scaffold
    }
    is EditDeviceUiState.Ready -> {
      EditDeviceScaffold(
        onNavigateBack = onNavigateBack,
        deviceTypes = deviceTypes,
        formState = state.formState,
        onFormStateChange = viewModel::updateFormState,
        onTypeSelected = viewModel::selectDeviceType,
        onSubmit = {
          if (viewModel.updateDevice()) {
            onNavigateBack()
          }
        }
      )
    }
  }
}
