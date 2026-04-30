package com.example.someapp.util

import android.content.Context
import android.net.Uri
import java.io.File

fun copyUriToFile(
    context: Context,
    uri: Uri,
    fileName: String = "device_icon_${System.currentTimeMillis()}"
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
