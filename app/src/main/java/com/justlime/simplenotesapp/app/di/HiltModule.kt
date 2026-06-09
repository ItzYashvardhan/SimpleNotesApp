package com.justlime.simplenotesapp.app.di

import android.content.Context
import androidx.room.Room
import com.justlime.simplenotesapp.data.local.dao.NotesDao
import com.justlime.simplenotesapp.data.local.database.NoteDatabase
import com.justlime.simplenotesapp.data.repository.NotesRepositoryImpl
import com.justlime.simplenotesapp.domain.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context, NoteDatabase::class.java,
            "notes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: NoteDatabase): NotesDao = db.notesDao

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NotesDao): NotesRepository = NotesRepositoryImpl(dao)
}