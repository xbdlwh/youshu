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
  val devices by viewModel.devices.collectAsStateWithLifecycle()
  val formState by viewModel.formState.collectAsStateWithLifecycle()

  AddDeviceScaffold(
    onNavigateBack = onNavigateBack,
    deviceTypes = deviceTypes,
    typeIdsInUse = devices.map { it.typeId }.toSet(),
    formState = formState,
    onFormStateChange = viewModel::updateFormState,
    onTypeSelected = viewModel::selectDeviceType,
    onAddDeviceType = viewModel::addDeviceType,
    onUpdateDeviceType = viewModel::updateDeviceType,
    onDeleteDeviceType = viewModel::deleteDeviceType,
    onSubmit = {
      if (viewModel.addDevice()) {
        onNavigateBack()
      }
    }
  )
}
