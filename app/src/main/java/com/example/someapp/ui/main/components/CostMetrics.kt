package com.example.someapp.ui.main.components

import com.example.someapp.data.local.entity.DeviceEntity
import java.util.Locale
import kotlin.math.max

internal fun DeviceEntity.dailyCost(currentTimeMillis: Long = System.currentTimeMillis()): Double =
  price / ownedDays(currentTimeMillis)

internal fun DeviceEntity.ownedDays(currentTimeMillis: Long): Long {
  val endTime = soldDate ?: currentTimeMillis
  val durationMillis = max(endTime - purchaseDate, MILLIS_PER_DAY)
  return max(1L, durationMillis / MILLIS_PER_DAY)
}

internal fun Double.currencyText(): String =
  "$${String.format(Locale.US, "%.2f", this)}"

private const val MILLIS_PER_DAY = 24L * 60L * 60L * 1000L
