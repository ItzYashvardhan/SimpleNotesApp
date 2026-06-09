package com.justlime.simplenotesapp.ui.upsert_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.justlime.simplenotesapp.domain.models.Note
import com.justlime.simplenotesapp.domain.repository.NotesRepository
import com.justlime.simplenotesapp.ui.route.UpsertRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: NotesRepository
) : ViewModel() {


    val route = savedStateHandle.toRoute<UpsertRoute>()
    val id = route.id
    val isAdding = route.isAdding

    val initialNote = Note(0, "Loading...", "Loading...")
    private var _note = MutableStateFlow<Note?>(initialNote)
    val note: StateFlow<Note?> = _note.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun setNoteById(id: Int) {
        viewModelScope.launch {
            repository.getNoteById(id).collect { _note.value = it }
        }
    }


//    fun getNote(id: Int): StateFlow<Note?> {
//        return repository.getNoteById(id)
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.Lazily,
//                initialValue =
//            )
//    }

    fun createDefaultNote(): Note {
        return Note(0, "", "")
    }

    fun onUpdateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun onAddNote(note: Note) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

}