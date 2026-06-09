package com.justlime.simplenotesapp.data.maper

import com.justlime.simplenotesapp.data.local.entity.NoteEntity
import com.justlime.simplenotesapp.domain.models.Note

fun NoteEntity.toNotes(): Note{
    return Note(this.id,this.title,this.description)
}
fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(title = this.title, description = this.description)
}
fun Note.toNoteEntityWithId(): NoteEntity{
    return NoteEntity(this.id,this.title,this.description)
}

fun List<NoteEntity>.toNotesList(): List<Note> {
    return this.map { it.toNotes() }
}

fun List<Note>.toNoteEntityList(): List<NoteEntity> {
    return this.map { it.toNoteEntity() }
}
