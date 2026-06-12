package com.justlime.simplenotesapp.ui.task.state

import com.justlime.simplenotesapp.domain.models.Task

sealed interface UiTaskState {
    object Loading : UiTaskState
    data class Success(val tasks: List<Task>): UiTaskState
    data class Error(val message: String?) : UiTaskState
}

