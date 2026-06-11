package com.justlime.simplenotesapp.ui.note.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justlime.simplenotesapp.domain.models.Note
import com.justlime.simplenotesapp.domain.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {

    private val _note = MutableStateFlow<List<Note>>(emptyList())
    val notes = _note.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init{
        getNotes()
    }

    fun getNotes(){
        viewModelScope.launch {
            repository.getNotes().collect { _note.value = it }
        }
    }

    fun onUndoNote(note: Note){
        viewModelScope.launch {
            repository.addNote(note,true)
        }
    }
    fun onDeleteNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }
}