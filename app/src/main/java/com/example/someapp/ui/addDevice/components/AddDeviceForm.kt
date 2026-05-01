package com.example.someapp.ui.addDevice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
  val selectedType = deviceTypes.find { it.id == formState.typeId }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Card with Image
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
      )
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        ImagePickerButton(
          currentIconPath = formState.icon,
          onImageSelected = { filePath ->
            onFormStateChange(formState.copy(icon = filePath))
          },
          modifier = Modifier
            .width(120.dp)
            .height(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Tap to select device image",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
      }
    }

    // Basic Info Section
    SectionHeader(title = "Basic Information")

    OutlinedTextField(
      value = formState.name,
      onValueChange = { onFormStateChange(formState.copy(name = it)) },
      label = { Text("Device Name") },
      placeholder = { Text("Enter device name") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      supportingText = { Text("e.g., iPhone 15 Pro") }
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

    // Price Section
    SectionHeader(title = "Price")

    OutlinedTextField(
      value = formState.price,
      onValueChange = { value ->
        if (value.isEmpty() || value.matches(PRICE_INPUT_REGEX)) {
          onFormStateChange(formState.copy(price = value))
        }
      },
      label = { Text("Price") },
      placeholder = { Text("0.00") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      prefix = { Text("$", style = MaterialTheme.typography.bodyLarge) },
      supportingText = { Text("Enter the device price") }
    )

    // Date Section
    SectionHeader(title = "Purchase Info")

    PurchaseDateSelector(
      purchaseDate = formState.purchaseDate,
      onDateSelected = { selectedDate ->
        onFormStateChange(formState.copy(purchaseDate = selectedDate))
      },
      modifier = Modifier.fillMaxWidth()
    )

    // Status Section
    SectionHeader(title = "Status")

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = if (formState.isServing) {
          MaterialTheme.colorScheme.tertiaryContainer
        } else {
          MaterialTheme.colorScheme.errorContainer
        }
      )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = if (formState.isServing) "Currently In Use" else "Retired",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = if (formState.isServing) "Device is active" else "Device is no longer in use",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Switch(
          checked = formState.isServing,
          onCheckedChange = { onFormStateChange(formState.copy(isServing = it)) }
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Submit Button
    Button(
      onClick = onSubmit,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      enabled = formState.isValid
    ) {
      Icon(Icons.Default.CheckCircle, contentDescription = null)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Add Device", style = MaterialTheme.typography.titleMedium)
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
private fun SectionHeader(title: String) {
  Text(
    text = title,
    style = MaterialTheme.typography.titleSmall,
    color = MaterialTheme.colorScheme.primary,
    fontWeight = FontWeight.SemiBold
  )
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
      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        TypeIconPicker(
          currentIconPath = icon,
          onIconSelected = onIconChange,
          modifier = Modifier.width(100.dp).height(100.dp)
        )
        OutlinedTextField(
          value = name,
          onValueChange = onNameChange,
          label = { Text("Type Name") },
          placeholder = { Text("Enter type name") },
          singleLine = true,
          isError = isDuplicate,
          modifier = Modifier.fillMaxWidth()
        )
        if (isDuplicate) {
          Text(
            text = "Type name already exists",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
          )
        }
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