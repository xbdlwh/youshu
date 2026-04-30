package com.example.someapp

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey

@Serializable data object AddDevice : NavKey

@Serializable data object FileTest: NavKey
