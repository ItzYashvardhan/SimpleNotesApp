package com.justlime.simplenotesapp.domain.repository

import com.justlime.simplenotesapp.domain.models.Task
import kotlinx.coroutines.flow.Flow


interface TaskRepository {
    fun getTasks(): Flow<List<Task>>

    fun getTaskById(id: Int): Flow<Task?>

    suspend fun addTask(task: Task, useGivenId: Boolean = false)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(id: Int): Boolean
}