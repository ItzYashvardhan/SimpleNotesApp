package com.justlime.simplenotesapp.data.maper

import com.justlime.simplenotesapp.data.local.entity.TaskEntity
import com.justlime.simplenotesapp.domain.enums.Priority
import com.justlime.simplenotesapp.domain.enums.Status
import com.justlime.simplenotesapp.domain.models.Task
import com.justlime.simplenotesapp.utils.currentMilliseconds
import com.justlime.simplenotesapp.utils.toLocalDateTime
import com.justlime.simplenotesapp.utils.toLong

fun Task.toTaskEntity(
    withCreatedDate: Boolean = true, withModifiedDate: Boolean = true, useGivenId: Boolean = false
): TaskEntity {
    val finalCreatedDate =
        if (withCreatedDate) this.createdAt.toLong() else currentMilliseconds
    val finalModifiedDate =
        if (withModifiedDate) this.createdAt.toLong() else currentMilliseconds
    val result = TaskEntity(
        title = this.title,
        description = this.description ?: "",
        status = this.status.name,
        priority = this.priority.name,
        createdAt = finalCreatedDate,
        updatedAt = finalModifiedDate
    )
    return if (useGivenId) result.copy(id = this.id) else result.copy()
}

fun TaskEntity.toTask(): Task {
    return Task(
        id,
        title,
        description,
        Status.valueOf(status),
        Priority.valueOf(priority),
        createdAt.toLocalDateTime(),
        updatedAt.toLocalDateTime()
    )
}

fun List<Task>.toTaskEntityList(
    withCreatedDate: Boolean = true,
    withModifiedDate: Boolean = true
): List<TaskEntity> {
    return this.map {
        it.toTaskEntity(withCreatedDate, withModifiedDate)
    }
}

fun List<TaskEntity>.toTaskList(): List<Task> {
    return this.map {
        it.toTask()
    }
}