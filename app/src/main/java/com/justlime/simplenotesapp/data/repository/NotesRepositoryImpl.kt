package com.justlime.simplenotesapp.data.repository

import com.justlime.simplenotesapp.data.local.dao.NotesDao
import com.justlime.simplenotesapp.data.maper.toNoteEntity
import com.justlime.simplenotesapp.data.maper.toNoteEntityWithId
import com.justlime.simplenotesapp.data.maper.toNotes
import com.justlime.simplenotesapp.data.maper.toNotesList
import com.justlime.simplenotesapp.domain.models.Note
import com.justlime.simplenotesapp.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(private val dao: NotesDao): NotesRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes().map{entity ->
            entity.toNotesList()
        }
    }

    override fun getNoteById(id: Int): Flow<Note?> {
        return dao.getNoteById(id).map{
            it?.toNotes()
        }
    }

    override suspend fun addNote(note: Note) {
        dao.addNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(noteId: Int) {
        dao.deleteNote(noteId)
    }


    override suspend fun updateNote(note: Note) {
        dao.updateNote(note.toNoteEntityWithId())
    }
}