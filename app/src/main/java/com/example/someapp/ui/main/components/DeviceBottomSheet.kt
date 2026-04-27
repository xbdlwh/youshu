package com.example.someapp.ui.main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.someapp.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetTrigger(
  modifier: Modifier = Modifier,
  trigger: @Composable (onClick: () -> Unit) -> Unit,
  sheetContent: @Composable () -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState()

  Box(modifier = modifier, contentAlignment = Alignment.Center) {
    trigger { expanded = true }

    if (expanded) {
      ModalBottomSheet(
        onDismissRequest = { expanded = false },
        sheetState = sheetState,
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          sheetContent()
          Spacer(modifier = Modifier.height(32.dp))
        }
      }
    }
  }
}

@Preview
@Composable
private fun BottomSheetTriggerPreview() {
  MyApplicationTheme {
    BottomSheetTrigger(
      trigger = { onClick ->
        androidx.compose.material3.Button(onClick = onClick) {
          androidx.compose.material3.Text("Add Device")
        }
      },
      sheetContent = {
        androidx.compose.material3.Text("Add your device content here")
      }
    )
  }
}
