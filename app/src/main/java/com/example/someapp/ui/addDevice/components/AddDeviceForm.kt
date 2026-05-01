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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
  var isTypeSheetVisible by rememberSaveable { mutableStateOf(false) }
  var isManagingTypes by rememberSaveable { mutableStateOf(false) }
  var isTypeDialogVisible by rememberSaveable { mutableStateOf(false) }
  var editingTypeId by rememberSaveable { mutableStateOf<Long?>(null) }
  var typeDialogName by rememberSaveable { mutableStateOf("") }
  var typeDialogIcon by rememberSaveable { mutableStateOf("") }
  var isPurchaseDatePickerVisible by rememberSaveable { mutableStateOf(false) }
  val editingType = deviceTypes.firstOrNull { it.id == editingTypeId }

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

    PurchaseDateSelector(
      purchaseDate = formState.purchaseDate,
      onClick = { isPurchaseDatePickerVisible = true },
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

  if (isTypeSheetVisible) {
    DeviceTypeBottomSheet(
      deviceTypes = deviceTypes,
      typeIdsInUse = typeIdsInUse,
      selectedTypeId = formState.typeId,
      isManaging = isManagingTypes,
      onManagingChange = { isManagingTypes = it },
      onDismiss = {
        isTypeSheetVisible = false
        isManagingTypes = false
      },
      onTypeSelected = { typeId ->
        onTypeSelected(typeId)
        isTypeSheetVisible = false
        isManagingTypes = false
      },
      onAddTypeClick = {
        editingTypeId = null
        typeDialogName = ""
        typeDialogIcon = ""
        isTypeDialogVisible = true
      },
      onEditTypeClick = { type ->
        editingTypeId = type.id
        typeDialogName = type.name
        typeDialogIcon = type.icon
        isTypeDialogVisible = true
      },
      onDeleteTypeClick = { type ->
        onDeleteDeviceType(type)
      }
    )
  }

  if (isTypeDialogVisible) {
    DeviceTypeEditorDialog(
      title = if (editingType == null) "Add Type" else "Edit Type",
      name = typeDialogName,
      icon = typeDialogIcon,
      isDuplicate = deviceTypes.any {
        it.id != editingTypeId && it.name.equals(typeDialogName.trim(), ignoreCase = true)
      },
      onNameChange = { typeDialogName = it },
      onIconChange = { typeDialogIcon = it },
      onDismiss = {
        isTypeDialogVisible = false
        editingTypeId = null
        typeDialogName = ""
        typeDialogIcon = ""
      },
      onConfirm = {
        val saved = if (editingType == null) {
          onAddDeviceType(typeDialogName, typeDialogIcon)
        } else {
          onUpdateDeviceType(editingType, typeDialogName, typeDialogIcon)
        }
        if (saved) {
          isTypeDialogVisible = false
          isTypeSheetVisible = false
          isManagingTypes = false
          editingTypeId = null
          typeDialogName = ""
          typeDialogIcon = ""
        }
        saved
      }
    )
  }

  if (isPurchaseDatePickerVisible) {
    PurchaseDatePickerDialog(
      selectedDateMillis = formState.purchaseDate,
      onDateSelected = { selectedDate ->
        onFormStateChange(formState.copy(purchaseDate = selectedDate))
        isPurchaseDatePickerVisible = false
      },
      onDismiss = { isPurchaseDatePickerVisible = false }
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

@Composable
private fun PurchaseDateSelector(
  purchaseDate: Long,
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
          text = "Purchase Date",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = formatPurchaseDate(purchaseDate),
          style = MaterialTheme.typography.bodyLarge
        )
      }
      Icon(Icons.Default.Event, contentDescription = "Select Purchase Date")
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PurchaseDatePickerDialog(
  selectedDateMillis: Long,
  onDateSelected: (Long) -> Unit,
  onDismiss: () -> Unit,
) {
  val datePickerState = androidx.compose.material3.rememberDatePickerState(
    initialSelectedDateMillis = selectedDateMillis
  )

  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = {
          onDateSelected(datePickerState.selectedDateMillis ?: selectedDateMillis)
        }
      ) {
        Text("OK")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  ) {
    DatePicker(state = datePickerState)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceTypeBottomSheet(
  deviceTypes: List<DeviceTypeEntity>,
  typeIdsInUse: Set<Long>,
  selectedTypeId: Long,
  isManaging: Boolean,
  onManagingChange: (Boolean) -> Unit,
  onDismiss: () -> Unit,
  onTypeSelected: (Long) -> Unit,
  onAddTypeClick: () -> Unit,
  onEditTypeClick: (DeviceTypeEntity) -> Unit,
  onDeleteTypeClick: (DeviceTypeEntity) -> Unit,
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
        Text(
          text = if (isManaging) "Manage Types" else "Device Type",
          style = MaterialTheme.typography.titleLarge
        )
        TextButton(onClick = { onManagingChange(!isManaging) }) {
          Text(if (isManaging) "Done" else "Manage")
        }
      }

      LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(deviceTypes, key = { it.id }) { type ->
          val isInUse = type.id in typeIdsInUse
          ListItem(
            headlineContent = { Text(type.name) },
            supportingContent = {
              Text(if (isManaging && isInUse) "${type.icon} · In use" else type.icon)
            },
            trailingContent = {
              if (isManaging) {
                Row {
                  IconButton(onClick = { onEditTypeClick(type) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Type")
                  }
                  IconButton(
                    onClick = { onDeleteTypeClick(type) },
                    enabled = !isInUse
                  ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Type")
                  }
                }
              } else if (type.id == selectedTypeId) {
                Icon(Icons.Default.Check, contentDescription = "Selected")
              }
            },
            modifier = if (isManaging) {
              Modifier
            } else {
              Modifier.clickable { onTypeSelected(type.id) }
            }
          )
        }
      }

      TextButton(
        onClick = onAddTypeClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Text("Add Type")
      }

      Spacer(modifier = Modifier.height(24.dp))
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
  val trimmedIcon = icon.trim()
  val canSubmit = trimmedName.isNotEmpty() && trimmedIcon.isNotEmpty() && !isDuplicate

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

private fun formatPurchaseDate(timestamp: Long): String =
  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
