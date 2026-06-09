package com.justlime.simplenotesapp.domain.models

data class Note(
    val id: Int,
    val title: String,
    val description: String //optional
)