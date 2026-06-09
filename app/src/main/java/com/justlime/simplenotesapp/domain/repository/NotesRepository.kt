package com.justlime.simplenotesapp.domain.repository

import com.justlime.simplenotesapp.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun getNotes(): Flow<List<Note>>

    fun getNoteById(id: Int): Flow<Note?>

    suspend fun addNote(note: Note)

    suspend fun deleteNote(noteId: Int)

    suspend fun updateNote(note: Note)
}