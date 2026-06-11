package com.justlime.simplenotesapp.ui.route

import kotlinx.serialization.Serializable

@Serializable
data class UpsertNoteRoute(
    val id: Int,
    val isAdding: Boolean,
)