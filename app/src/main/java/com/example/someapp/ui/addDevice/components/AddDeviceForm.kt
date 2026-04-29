package com.example.someapp.ui.addDevice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.ui.addDevice.AddDeviceFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceForm(
  deviceTypes: List<DeviceTypeEntity>,
  formState: AddDeviceFormState,
  onFormStateChange: (AddDeviceFormState) -> Unit,
  onSubmit: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    OutlinedTextField(
      value = formState.name,
      onValueChange = { onFormStateChange(formState.copy(name = it)) },
      label = { Text("Device Name") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true
    )

    OutlinedTextField(
      value = formState.icon,
      onValueChange = { onFormStateChange(formState.copy(icon = it)) },
      label = { Text("Icon") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true
    )

    DeviceTypeDropdown(
      deviceTypes = deviceTypes,
      selectedTypeId = formState.typeId,
      expanded = formState.isDropdownExpanded,
      onExpandedChange = { onFormStateChange(formState.copy(isDropdownExpanded = it)) },
      onTypeSelected = { typeId ->
        onFormStateChange(formState.copy(typeId = typeId, isDropdownExpanded = false))
      },
      modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
      value = formState.price,
      onValueChange = { value ->
        if (value.isEmpty() || value.matches(PRICE_INPUT_REGEX)) {
          onFormStateChange(formState.copy(price = value))
        }
      },
      label = { Text("Price") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      prefix = { Text("$") },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text("Serving", style = MaterialTheme.typography.bodyLarge)
      Switch(
        checked = formState.isServing,
        onCheckedChange = { onFormStateChange(formState.copy(isServing = it)) }
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
      onClick = onSubmit,
      modifier = Modifier.fillMaxWidth(),
      enabled = formState.isValid
    ) {
      Text("Add Device")
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceTypeDropdown(
  deviceTypes: List<DeviceTypeEntity>,
  selectedTypeId: Long,
  expanded: Boolean,
  onExpandedChange: (Boolean) -> Unit,
  onTypeSelected: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {
  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = onExpandedChange,
    modifier = modifier
  ) {
    OutlinedTextField(
      value = deviceTypes.find { it.id == selectedTypeId }?.name ?: "Select Type",
      onValueChange = {},
      readOnly = true,
      label = { Text("Device Type") },
      trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
      modifier = Modifier
        .fillMaxWidth()
        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
    )
    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { onExpandedChange(false) }
    ) {
      deviceTypes.forEach { type ->
        DropdownMenuItem(
          text = { Text(type.name) },
          onClick = { onTypeSelected(type.id) }
        )
      }
    }
  }
}

private val PRICE_INPUT_REGEX = Regex("""\d*\.?\d{0,2}""")
