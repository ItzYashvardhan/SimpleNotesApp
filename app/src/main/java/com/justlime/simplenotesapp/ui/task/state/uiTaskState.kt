package com.justlime.simplenotesapp.ui.task.state

import com.justlime.simplenotesapp.domain.models.Task

sealed interface uiTaskState {
    object Loading : uiTaskState
    data class Success(val tasks: List<Task>): uiTaskState
    data class Error(val message: String?) : uiTaskState
}

