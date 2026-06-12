package com.justlime.simplenotesapp.ui.task.upsert_task

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.justlime.simplenotesapp.domain.enums.Priority
import com.justlime.simplenotesapp.domain.enums.Status
import com.justlime.simplenotesapp.domain.models.Task
import com.justlime.simplenotesapp.domain.repository.TaskRepository
import com.justlime.simplenotesapp.ui.route.UpsertTaskRoute
import com.justlime.simplenotesapp.ui.task.state.UiTaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TaskRepository
) : ViewModel() {


    private val _state = MutableStateFlow<UiTaskState>(UiTaskState.Loading)
    val state = _state.asStateFlow()

    val route = savedStateHandle.toRoute<UpsertTaskRoute>()
    val taskId = route.taskId
    val isAdding = route.isAdding

    val initialTask = if (isAdding) Task(0, "", "", Status.PENDING, Priority.LOW)
    else {
        Task(0, "Loading...", "Loading...", Status.PENDING, Priority.LOW)
    }
    private var _selectedTask = MutableStateFlow<Task?>(initialTask)
    private var selectedTask = _selectedTask.asStateFlow()
    private val currentTask = mutableListOf<Task>()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            repository.getTasks().stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            ).collect {
                Log.d("myApp", "Collecting...")
                currentTask.addAll(it)
                if (currentTask.isNotEmpty()) {
                    _state.update { UiTaskState.Success(currentTask) }
                }
            }
        }
    }

    val task: StateFlow<Task?> = _selectedTask.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun setTaskById(id: Int = taskId) {
        viewModelScope.launch {
            repository.getTaskById(id).collect {
                _selectedTask.value = it
            }
        }
    }

    fun onAddTask(task: Task) {
        viewModelScope.launch {
            currentTask.add(task)
            repository.addTask(task)
            _state.update { UiTaskState.Success(currentTask) }
        }
    }

    fun onUpdateTask(task: Task) {
        viewModelScope.launch {
            val taskIndex = currentTask.indexOfFirst { it.id == task.id }
            if (taskIndex == -1) {
                _state.update { UiTaskState.Error("The Task is not found or has been already deleted") }
                return@launch
            }
            currentTask[taskIndex] = task
            repository.updateTask(task)
            _state.update { UiTaskState.Success(currentTask) }

        }
    }

}