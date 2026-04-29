package com.example.someapp.ui.addDevice.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.ui.addDevice.AddDeviceFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScaffold(
  onNavigateBack: () -> Unit,
  deviceTypes: List<DeviceTypeEntity>,
  typeIdsInUse: Set<Long>,
  formState: AddDeviceFormState,
  onFormStateChange: (AddDeviceFormState) -> Unit,
  onTypeSelected: (Long) -> Unit,
  onAddDeviceType: (String, String) -> Boolean,
  onUpdateDeviceType: (DeviceTypeEntity, String, String) -> Boolean,
  onDeleteDeviceType: (DeviceTypeEntity) -> Boolean,
  onSubmit: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Add Device") },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        }
      )
    },
    modifier = modifier
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      AddDeviceForm(
        deviceTypes = deviceTypes,
        typeIdsInUse = typeIdsInUse,
        formState = formState,
        onFormStateChange = onFormStateChange,
        onTypeSelected = onTypeSelected,
        onAddDeviceType = onAddDeviceType,
        onUpdateDeviceType = onUpdateDeviceType,
        onDeleteDeviceType = onDeleteDeviceType,
        onSubmit = onSubmit
      )
    }
  }
}
