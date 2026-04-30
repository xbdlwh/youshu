package com.example.someapp.ui.test

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import java.io.File

val fileName = "hello"

@Composable
fun TestScreen(modifier: Modifier = Modifier) {
    val dirPath = LocalContext.current.filesDir
    val context = LocalContext.current
    var imageUri: Uri? by remember { mutableStateOf(null) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", File(context.filesDir,fileName).absolutePath)
            Log.d("PhotoPicker", "Selected URI: $uri")
            imageUri = uri
            copyUriToFile(context,uri,fileName)

        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    Scaffold() {paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Text(dirPath.absolutePath)
            if (imageUri != null) {
                AsyncImage(imageUri, contentDescription = "")
            }
            AsyncImage("/data/user/0/com.example.someapp/files/hello","some File!")
            Button(onClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Text("Select Image")
            }
        }
    }
}

fun copyUriToFile(
    context: Context,
    uri: Uri,
    fileName: String = "image_${System.currentTimeMillis()}"
): File? {
    val resolver = context.contentResolver

    val file = File(context.filesDir, fileName)

    return try {
        resolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}