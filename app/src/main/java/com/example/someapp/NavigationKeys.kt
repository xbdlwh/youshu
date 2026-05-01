package com.example.someapp

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import com.example.someapp.data.local.entity.DeviceWithType

@Serializable data object Main : NavKey

@Serializable data object AddDevice : NavKey

@Serializable data object FileTest: NavKey

@Serializable data class DeviceDetail(public val deviceId: Long) : NavKey

@Serializable data class EditDevice(public val deviceWithType: DeviceWithType) : NavKey
