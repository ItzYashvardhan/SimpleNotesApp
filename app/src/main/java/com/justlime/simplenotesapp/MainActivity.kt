package com.justlime.simplenotesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.justlime.simplenotesapp.ui.notes_list.NotesListScreen
import com.justlime.simplenotesapp.ui.notes_list.NotesViewModel
import com.justlime.simplenotesapp.ui.route.UpsertRoute
import com.justlime.simplenotesapp.ui.theme.SimpleNotesAppTheme
import com.justlime.simplenotesapp.ui.upsert_note.UpsertNoteScreen
import com.justlime.simplenotesapp.ui.upsert_note.UpsertNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNotesAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "notes_list"
                ) {
                    composable("notes_list") {
                        val viewModel: NotesViewModel = hiltViewModel()
                        val notes by viewModel.notes.collectAsState(initial = emptyList())
                        NotesListScreen(
                            onNavigateToAddEditNote = { id, isAdding ->
                                val route = UpsertRoute(id, isAdding)
                                navController.navigate(route)
                            },
                            notes = notes,
                            onClick = {
                                val route = UpsertRoute(it.id, isAdding = false)
                                navController.navigate(route)
                            },
                            onDelete = { viewModel.onDeleteNote(it) }
                        )
                    }
                    composable<UpsertRoute> { backStackHandler ->

                        val viewmodel = hiltViewModel<UpsertNoteViewModel>()
                        val noteId = viewmodel.id
                        viewmodel.setNoteById(noteId)

                        val note by viewmodel.note.collectAsStateWithLifecycle(viewmodel.initialNote)
                        val isAdding = viewmodel.isAdding
                        Log.d("myApp", note.toString())
                        UpsertNoteScreen(
                            isAdding = isAdding,
                            note = note,
                            onUpdateNote = { viewmodel.onUpdateNote(it) },
                            onAddNote = { viewmodel.onAddNote(it) },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

