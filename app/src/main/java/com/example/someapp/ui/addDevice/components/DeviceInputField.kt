package com.example.someapp.ui.addDevice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeviceInputField(modifier: Modifier = Modifier,value: String,onValueChange:(String) -> Unit,placeholder: @Composable ()->Unit = {},leadingIcon:@Composable () -> Unit = {}) {
        OutlinedTextField(
            modifier= Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    placeholder()
                }
            },
            shape = MaterialTheme.shapes.extraLarge,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
            leadingIcon = leadingIcon,
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent, focusedContainerColor = Color.Transparent,unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent, disabledContainerColor = Color.Transparent)
        )
}


@Preview( showBackground = true)
@Composable
private fun DeviceInputFieldPreview() {
    var value by remember { mutableStateOf("") }
    Column(Modifier.padding(20.dp)) {
        DeviceInputField(value = value, onValueChange = { value = it }, placeholder = {
            Text("Hello")
        }, leadingIcon = {
            Row(Modifier.padding(start = 12.dp)) {
                Icon(Icons.Default.Devices, contentDescription = "", Modifier.scale(0.7f))
                Spacer(Modifier.width(10.dp))
                Text("价格")

            }
        })
    }
}