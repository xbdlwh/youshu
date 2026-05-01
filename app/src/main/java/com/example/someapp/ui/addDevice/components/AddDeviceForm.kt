package com.example.someapp.ui.addDevice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.someapp.data.local.entity.DeviceTypeEntity
import com.example.someapp.ui.addDevice.AddDeviceFormState

private val PRICE_INPUT_REGEX = Regex("""\d*\.?\d{0,2}""")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceForm(
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
  val selectedTypeName = deviceTypes.find { it.id == formState.typeId }?.name.orEmpty()

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

    ImagePickerButton(
      currentIconPath = formState.icon,
      onImageSelected = { filePath ->
          onFormStateChange(formState.copy(icon = filePath))
      },
      modifier = Modifier.fillMaxWidth()
    )

    DeviceTypeSelector(
      selectedTypeId = formState.typeId,
      selectedTypeName = selectedTypeName,
      deviceTypes = deviceTypes,
      typeIdsInUse = typeIdsInUse,
      onTypeSelected = onTypeSelected,
      onAddDeviceType = onAddDeviceType,
      onUpdateDeviceType = onUpdateDeviceType,
      onDeleteDeviceType = onDeleteDeviceType,
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

    PurchaseDateSelector(
      purchaseDate = formState.purchaseDate,
      onDateSelected = { selectedDate ->
          onFormStateChange(formState.copy(purchaseDate = selectedDate))
      },
      modifier = Modifier.fillMaxWidth()
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
      Text("OK")
    }
  }
}

@Composable
fun DeviceTypeEditorDialog(
  title: String,
  name: String,
  icon: String,
  isDuplicate: Boolean,
  onNameChange: (String) -> Unit,
  onIconChange: (String) -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Boolean,
) {
  val trimmedName = name.trim()
  val canSubmit = trimmedName.isNotEmpty() && !isDuplicate

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(title) },
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
        TypeIconPicker(
          currentIconPath = icon,
          onIconSelected = onIconChange,
          modifier = Modifier.align(Alignment.CenterHorizontally)
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
