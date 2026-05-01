package com.example.someapp.util

import android.content.Context
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipTool(private val context: Context) {
    private val files = mutableListOf<Pair<String, String>>() // Pair(fileUrl, internalPath)

    fun put(fileUrl: String, internalPath: String): ZipTool {
        files.add(fileUrl to internalPath)
        return this
    }

    suspend fun export(): String? = withContext(Dispatchers.IO) {
        if (files.isEmpty()) return@withContext null

        val zipFileName = "export_${System.currentTimeMillis()}.zip"
        val zipFile = File(context.cacheDir, zipFileName)

        ZipOutputStream(zipFile.outputStream()).use { zos ->
            files.forEach { (fileUrl, internalPath) ->
                val file = File(fileUrl)
                if (file.exists()) {
                    zos.putNextEntry(ZipEntry(internalPath))
                    FileInputStream(file).use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }
        }

        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            zipFile
        ).toString()
    }
}
