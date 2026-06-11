package com.justlime.simplenotesapp.data.repository

import com.justlime.simplenotesapp.data.local.dao.TaskDao
import com.justlime.simplenotesapp.data.maper.toTask
import com.justlime.simplenotesapp.data.maper.toTaskEntity
import com.justlime.simplenotesapp.data.maper.toTaskList
import com.justlime.simplenotesapp.domain.models.Task
import com.justlime.simplenotesapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val dao: TaskDao) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return dao.getTasks().map {
            it.toTaskList()
        }
    }

    override fun getTaskById(id: Int): Flow<Task?> = dao.getTaskById(id).map { it?.toTask() }


    override suspend fun addTask(task: Task, useGivenId: Boolean) = dao.addTask(
        task.toTaskEntity(
            useGivenId = useGivenId,
            withCreatedDate = false,
            withModifiedDate = false
        )
    )

    override suspend fun updateTask(task: Task) {
        return dao.updateTask(task.toTaskEntity(withModifiedDate = false, useGivenId = true))
    }

    override suspend fun deleteTask(id: Int): Boolean {
        return dao.deleteTask(id) > 0
    }


}