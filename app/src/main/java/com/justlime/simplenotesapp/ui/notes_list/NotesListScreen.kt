package com.justlime.simplenotesapp.ui.notes_list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.simplenotesapp.domain.models.Note
import com.justlime.simplenotesapp.ui.theme.LightBlue
import kotlinx.coroutines.launch

@Composable
fun NotesListScreen(
    onNavigateToAddEditNote: (id: Int, isAdding: Boolean) -> Unit,
    notes: List<Note>,
    onClick: (note: Note) -> Unit = {},
    onUndo: (note: Note) -> Unit = {},
    onDelete: (id: Int) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppHeader() },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onNavigateToAddEditNote(-1, true)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding
        ) {
            items(notes) { note ->
                NoteCard(note = note, onClick) { note ->
                    scope.launch {
                        val result = snackBarHostState.showSnackbar(
                            "You have deleted a note",
                            "Undo",
                            true,
                            SnackbarDuration.Long
                        )
                        when (result) {
                            SnackbarResult.Dismissed -> {}
                            SnackbarResult.ActionPerformed -> {
                                onUndo(note)
                            }
                        }
                    }
                    onDelete(note.id)
                }
            }
        }
        if (notes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No notes available")
            }
        }
    }
}

@Composable
fun NoteCard(note: Note, onClick: (note: Note) -> Unit, onDelete: (note: Note) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                onClick(note)
            }
    ) {
        var isExpanded by rememberSaveable { mutableStateOf<Boolean>(false) }

        Row(modifier = Modifier.fillMaxSize()) {


            Box {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.Close, "Delete Note", Modifier.clickable {
                        onDelete(note)
                    })
                }
                Row(Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(6.dp, 16.dp)) {
                        val trimTitle = note.title.trim()
                        var finalTitle = trimTitle
                        val maxTitleSize = 16

                        if (trimTitle.length > maxTitleSize) {
                            finalTitle = trimTitle.dropLast(trimTitle.length - maxTitleSize) + "..."
                        }
                        Text(finalTitle, fontSize = 28.sp)
                        Spacer(Modifier.height(4.dp))
                        Log.d("myApp", "Length: ${note.description.length} of ${note.description}")
                        ShowDescription(isExpanded, note.description) {
                            isExpanded = it
                        }

                    }

                }
            }
        }


    }
}

@Composable
fun ShowDescription(isExpanded: Boolean, description: String, onExpand: (Boolean) -> Unit) {
    val trimDesc = description.replace("\n", " ").trim()
    val isLongText = trimDesc.length > 30
    val finalDescription =
        if (isExpanded) {
            description
        } else {
            if (isLongText) {
                trimDesc.dropLast(trimDesc.length - 30) + "..."
            } else trimDesc
        }
    Column(verticalArrangement = Arrangement.Bottom) {
        Row(modifier = Modifier.fillMaxSize()) {
            Text(finalDescription)
        }
        if (isLongText) {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End) {
                Text(
                    if (isExpanded) "Hide" else "Show",
                    modifier = Modifier.clickable { onExpand(!isExpanded) },
                    color = LightBlue
                )
            }
        }
    }
}

fun addMoreToText(description: String) {
    val totalNewLines = description.contains("/n")
    description.length
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader() {
    TopAppBar(
        title = { Text("Notes List") }
    )
}

@Preview("Note Card Preview")
@Composable
fun NoteCardPreview() {
    NoteCard(
        Note(
            id = 0,
            "Complete Car Washing on Sunday",
            "The car washing is done on Sunday due to long travelling for Monday"
        ), {}
    ) {}
}

@Preview(showBackground = true)
@Composable
fun NotesListScreenPreview() {
    NotesListScreen(
        onNavigateToAddEditNote = { _, _ -> Unit },
        notes = listOf(
            Note(0, "Grocery List Shopping", "Milk, Eggs, Bread, Butter"),
            Note(0, "Work Meeting", "Discuss project milestones and deadlines"),
            Note(0, "Gym Session", "Leg day workout at 6 PM")
        )
    )
}
