package com.example.someapp.ui.addDevice.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
  onTypeSelected: (Long) -> Unit,
  onAddDeviceType: () -> Boolean,
  onDismissAddTypeDialog: () -> Unit,
  onSubmit: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val selectedTypeName = deviceTypes.find { it.id == formState.typeId }?.name.orEmpty()
  var isTypeSheetVisible by rememberSaveable { mutableStateOf(false) }
  var isAddTypeDialogVisible by rememberSaveable { mutableStateOf(false) }

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

    DeviceTypeSelector(
      selectedTypeName = selectedTypeName,
      onClick = { isTypeSheetVisible = true },
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

  if (isTypeSheetVisible) {
    DeviceTypeBottomSheet(
      deviceTypes = deviceTypes,
      selectedTypeId = formState.typeId,
      onDismiss = { isTypeSheetVisible = false },
      onTypeSelected = { typeId ->
        onTypeSelected(typeId)
        isTypeSheetVisible = false
      },
      onAddTypeClick = {
        isAddTypeDialogVisible = true
      }
    )
  }

  if (isAddTypeDialogVisible) {
    AddDeviceTypeDialog(
      name = formState.newTypeName,
      icon = formState.newTypeIcon,
      isDuplicate = deviceTypes.any {
        it.name.equals(formState.newTypeName.trim(), ignoreCase = true)
      },
      onNameChange = { onFormStateChange(formState.copy(newTypeName = it)) },
      onIconChange = { onFormStateChange(formState.copy(newTypeIcon = it)) },
      onDismiss = {
        onDismissAddTypeDialog()
        isAddTypeDialogVisible = false
      },
      onConfirm = {
        val added = onAddDeviceType()
        if (added) {
          isAddTypeDialogVisible = false
          isTypeSheetVisible = false
        }
        added
      }
    )
  }
}

@Composable
private fun DeviceTypeSelector(
  selectedTypeName: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Device Type",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = selectedTypeName.ifEmpty { "Select Type" },
          style = MaterialTheme.typography.bodyLarge
        )
      }
      Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Select Device Type")
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceTypeBottomSheet(
  deviceTypes: List<DeviceTypeEntity>,
  selectedTypeId: Long,
  onDismiss: () -> Unit,
  onTypeSelected: (Long) -> Unit,
  onAddTypeClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    modifier = modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Device Type", style = MaterialTheme.typography.titleLarge)
        TextButton(onClick = onAddTypeClick) {
          Icon(Icons.Default.Add, contentDescription = null)
          Text("Add Type")
        }
      }

      LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(deviceTypes, key = { it.id }) { type ->
          ListItem(
            headlineContent = { Text(type.name) },
            supportingContent = { Text(type.icon) },
            trailingContent = {
              if (type.id == selectedTypeId) {
                Icon(Icons.Default.Check, contentDescription = "Selected")
              }
            },
            modifier = Modifier.clickable { onTypeSelected(type.id) }
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun AddDeviceTypeDialog(
  name: String,
  icon: String,
  isDuplicate: Boolean,
  onNameChange: (String) -> Unit,
  onIconChange: (String) -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Boolean,
) {
  val trimmedName = name.trim()
  val trimmedIcon = icon.trim()
  val canSubmit = trimmedName.isNotEmpty() && trimmedIcon.isNotEmpty() && !isDuplicate

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add Type") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = onNameChange,
          label = { Text("Type Name") },
          singleLine = true,
          isError = isDuplicate
        )
        if (isDuplicate) {
          Text(
            text = "Type name already exists",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
          )
        }
        OutlinedTextField(
          value = icon,
          onValueChange = onIconChange,
          label = { Text("Icon") },
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm() },
        enabled = canSubmit
      ) {
        Text("Save")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

private val PRICE_INPUT_REGEX = Regex("""\d*\.?\d{0,2}""")
