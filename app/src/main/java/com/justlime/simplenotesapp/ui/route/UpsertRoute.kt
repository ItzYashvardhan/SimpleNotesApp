package com.justlime.simplenotesapp.ui.route

import kotlinx.serialization.Serializable

@Serializable
data class UpsertRoute(
    val id: Int,
    val isAdding: Boolean,
)