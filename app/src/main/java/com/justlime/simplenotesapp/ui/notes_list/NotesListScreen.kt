package com.justlime.simplenotesapp.ui.notes_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.simplenotesapp.domain.models.Note

@Composable
fun NotesListScreen(
    onNavigateToAddEditNote: (id: Int, isAdding: Boolean) -> Unit,
    notes: List<Note>,
    onClick: (note: Note) -> Unit = {},
    onDelete: (id: Int) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppHeader() },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onNavigateToAddEditNote(-1, true)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding
        ) {
            items(notes) { note ->
                NoteCard(note = note, onClick, onDelete)
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
fun NoteCard(note: Note, onClick: (note: Note) -> Unit, onDelete: (id: Int) -> Unit) {


    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                onClick(note)
            }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(note.title, fontSize = 32.sp)
                Spacer(Modifier.height(4.dp))
                Text(note.description, fontSize = 16.sp)
            }
            Spacer(Modifier.weight(1.0f))
            Column() {
                Button(onClick = { onDelete(note.id) }) {
                    Icon(Icons.Default.Close, "Delete Note")
                }
            }
        }
    }
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
            Note(0, "Grocery List", "Milk, Eggs, Bread, Butter"),
            Note(0, "Work Meeting", "Discuss project milestones and deadlines"),
            Note(0, "Gym Session", "Leg day workout at 6 PM")
        )
    )
}
