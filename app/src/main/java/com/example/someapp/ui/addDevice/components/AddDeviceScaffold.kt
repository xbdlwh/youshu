package com.example.someapp.ui.addDevice.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        title = {
          Text(
            text = "Add Device",
            style = MaterialTheme.typography.titleLarge
          )
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface,
          titleContentColor = MaterialTheme.colorScheme.onSurface
        )
      )
    },
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = modifier
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      AddDeviceForm(
        deviceTypes = deviceTypes,
        typeIdsInUse = typeIdsInUse,
        formState = formState,
        onFormStateChange = onFormStateChange,
        onTypeSelected = onTypeSelected,
        onAddDeviceType = onAddDeviceType,
        onUpdateDeviceType = onUpdateDeviceType,
        onDeleteDeviceType = onDeleteDeviceType,
        onSubmit = onSubmit,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}