package com.justlime.simplenotesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.justlime.simplenotesapp.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("Select * from Notes")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("Select * from Notes where id = :id")
    fun getNoteById(id: Int): Flow<NoteEntity?>

    @Insert
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM Notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int)

    @Update
    suspend fun updateNote(note: NoteEntity)

}