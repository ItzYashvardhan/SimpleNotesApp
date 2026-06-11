package com.justlime.simplenotesapp.ui.task.tasks_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justlime.simplenotesapp.domain.models.Task
import com.justlime.simplenotesapp.domain.repository.TaskRepository
import com.justlime.simplenotesapp.ui.task.state.uiTaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<uiTaskState> = combine(
        _isLoading, repository.getTasks(), _error,
    ) { isLoading, tasks, error ->
        when {
            isLoading -> uiTaskState.Loading
            tasks.isEmpty() -> uiTaskState.Success(emptyList())
            error != null -> uiTaskState.Error(error)
            else -> uiTaskState.Success(tasks)
        }
    }.onStart {
        _isLoading.value = false
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        uiTaskState.Loading
    )

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {

        }
    }

    fun onDeleteTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    fun onUndoTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task, true)
        }
    }


}