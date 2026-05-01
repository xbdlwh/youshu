package com.example.someapp.ui.addDevice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.someapp.data.local.entity.DeviceTypeEntity
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceTypeSelector(
    selectedTypeId: Long,
    selectedTypeName: String,
    deviceTypes: List<DeviceTypeEntity>,
    typeIdsInUse: Set<Long>,
    onTypeSelected: (Long) -> Unit,
    onAddDeviceType: (String, String) -> Boolean,
    onUpdateDeviceType: (DeviceTypeEntity, String, String) -> Boolean,
    onDeleteDeviceType: (DeviceTypeEntity) -> Boolean,
    modifier: Modifier = Modifier,
) {
    var isTypeSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isManagingTypes by rememberSaveable { mutableStateOf(false) }
    var isTypeDialogVisible by rememberSaveable { mutableStateOf(false) }
    var editingTypeId by rememberSaveable { mutableStateOf<Long?>(null) }
    var typeDialogName by rememberSaveable { mutableStateOf("") }
    var typeDialogIcon by rememberSaveable { mutableStateOf("") }
    val editingType = deviceTypes.firstOrNull { it.id == editingTypeId }
    val sheetState = rememberModalBottomSheetState()

    OutlinedButton(
        onClick = { isTypeSheetVisible = true },
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

    if (isTypeSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                isTypeSheetVisible = false
                isManagingTypes = false
            },
            sheetState = sheetState,
            modifier = Modifier
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
                        text = if (isManagingTypes) "Manage Types" else "Device Type",
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextButton(onClick = { isManagingTypes = !isManagingTypes }) {
                        Text(if (isManagingTypes) "Done" else "Manage")
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(deviceTypes, key = { it.id }) { type ->
                        val isInUse = type.id in typeIdsInUse
                        ListItem(
                            headlineContent = { Text(type.name) },
                            supportingContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (type.icon.isNotBlank() && File(type.icon).exists()) {
                                        AsyncImage(
                                            model = File(type.icon),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(Modifier.size(8.dp))
                                    }
                                    Text(
                                        text = if (isManagingTypes && isInUse) "In use" else "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            trailingContent = {
                                if (isManagingTypes) {
                                    Row {
                                        IconButton(onClick = {
                                            editingTypeId = type.id
                                            typeDialogName = type.name
                                            typeDialogIcon = type.icon
                                            isTypeDialogVisible = true
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit Type")
                                        }
                                        IconButton(
                                            onClick = { onDeleteDeviceType(type) },
                                            enabled = !isInUse
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete Type")
                                        }
                                    }
                                } else if (type.id == selectedTypeId) {
                                    Icon(Icons.Default.Check, contentDescription = "Selected")
                                }
                            },
                            modifier = if (isManagingTypes) {
                                Modifier
                            } else {
                                Modifier.clickable {
                                    onTypeSelected(type.id)
                                }
                            }
                        )
                    }
                }

                TextButton(
                    onClick = {
                        editingTypeId = null
                        typeDialogName = ""
                        typeDialogIcon = ""
                        isTypeDialogVisible = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Add Type")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
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
                    editingTypeId = null
                    typeDialogName = ""
                    typeDialogIcon = ""
                }
                saved
            }
        )
    }
}
