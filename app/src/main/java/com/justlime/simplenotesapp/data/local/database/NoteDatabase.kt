package com.justlime.simplenotesapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justlime.simplenotesapp.data.local.dao.NotesDao
import com.justlime.simplenotesapp.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract val notesDao: NotesDao
}