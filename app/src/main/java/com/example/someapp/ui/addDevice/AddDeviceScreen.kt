package com.example.someapp.ui.addDevice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.someapp.MyViewModelProvider
import com.example.someapp.ui.addDevice.components.AddDeviceScaffold

@Composable
fun AddDeviceScreen(
  onNavigateBack: () -> Unit,
  viewModel: AddDeviceViewModel = viewModel(factory = MyViewModelProvider.FACTORY),
) {
  val deviceTypes by viewModel.deviceTypes.collectAsStateWithLifecycle()
  val formState by viewModel.formState.collectAsStateWithLifecycle()

  AddDeviceScaffold(
    onNavigateBack = onNavigateBack,
    deviceTypes = deviceTypes,
    formState = formState,
    onFormStateChange = viewModel::updateFormState,
    onSubmit = viewModel::addDevice
  )
}
