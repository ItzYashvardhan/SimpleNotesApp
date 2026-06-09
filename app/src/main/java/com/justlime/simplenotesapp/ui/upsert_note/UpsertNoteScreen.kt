package com.justlime.simplenotesapp.ui.upsert_note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justlime.simplenotesapp.domain.models.Note


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertNoteScreen(
    isAdding: Boolean,
    note: Note? = null,
    onUpdateNote: (Note) -> Unit,
    onAddNote: (Note) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var showDialog by rememberSaveable() { mutableStateOf(false) }

    var title by rememberSaveable(note?.id) { mutableStateOf(note?.title ?: "") }
    var description by rememberSaveable(note?.id) { mutableStateOf(note?.description ?: "") }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Note Edit") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding + PaddingValues(4.dp, 6.dp))
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Bottom
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                minLines = 1,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                minLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .imePadding()
            )
            Button(
                onClick = {

                    val upsertNote = note?.copy(title = title, description = description) ?: Note(
                        0,
                        title,
                        description
                    )
                    if (isAdding) onAddNote(upsertNote) else onUpdateNote(upsertNote)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isAdding) "Add Note" else "Update Note")
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                showDialog = false
            },
            title = { Text("Invalid Note") },
            text = { Text("The note is found to be null which shouldn't happen") },
        )
    }
}


@Composable
@Preview("Notes Upsert Screen")
fun NotesUpsertScreen() {
    UpsertNoteScreen(
        isAdding = true,
        note = Note(1, "New", "My New Note"),
        onUpdateNote = {},
        onAddNote = {},
        onBack = {}
    )
}