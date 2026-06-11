package com.justlime.simplenotesapp.domain.models

import com.justlime.simplenotesapp.domain.enums.Priority
import com.justlime.simplenotesapp.domain.enums.Status
import com.justlime.simplenotesapp.utils.currentMilliseconds
import com.justlime.simplenotesapp.utils.toLocalDateTime
import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Int = -1,
    val title: String,
    val description: String,
    val status: Status,
    val priority: Priority,
    val createdAt: LocalDateTime = currentMilliseconds.toLocalDateTime(),
    val updatedAt: LocalDateTime = currentMilliseconds.toLocalDateTime()
)