package com.justlime.simplenotesapp.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant


val currentTimeZone get() = TimeZone.currentSystemDefault()
val currentMilliseconds get() = Clock.System.now().toEpochMilliseconds()

fun LocalDateTime.toLong(): Long {
    return this.toInstant(currentTimeZone).toEpochMilliseconds()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(currentTimeZone)
}