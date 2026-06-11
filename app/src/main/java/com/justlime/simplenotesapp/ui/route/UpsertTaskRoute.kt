package com.justlime.simplenotesapp.ui.route

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpsertTaskRoute(
    @SerialName("task_id")
    val taskId: Int,
    @SerialName("is_adding")
    val isAdding: Boolean,
)
