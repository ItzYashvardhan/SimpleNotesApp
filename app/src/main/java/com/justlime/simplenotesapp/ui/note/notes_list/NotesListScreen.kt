package com.justlime.simplenotesapp.ui.note.notes_list

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.simplenotesapp.domain.models.Note
import com.justlime.simplenotesapp.ui.theme.LightBlue

@Composable
fun NotesListScreen(
    modifier: Modifier = Modifier,
    onSnackBarLaunch: (Note) -> Unit,
    notes: List<Note>,
    onClick: (note: Note) -> Unit = {},
    onDelete: (id: Int) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        Log.d("myApp", "Size of notes is ${notes.size}")
        items(notes) { note ->
            NoteCard(note = note, onClick) { note ->
                onSnackBarLaunch(note)
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
    val isLongTextLimit = 50
    val isLongText = trimDesc.length > isLongTextLimit
    val descriptionSize = 12.sp
    val finalDescription =
        if (isExpanded) {
            description
        } else {
            if (isLongText) {
                val lettersToRemoved = trimDesc.length-isLongTextLimit+3
                trimDesc.dropLast(lettersToRemoved) + "..."
            } else trimDesc
        }
    Column(verticalArrangement = Arrangement.Bottom) {
        Row(modifier = Modifier.fillMaxSize()) {
            Text(finalDescription, fontSize = descriptionSize)
        }
        if (isLongText) {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End) {
                Text(
                    if (isExpanded) "Hide" else "Show",
                    modifier = Modifier.clickable { onExpand(!isExpanded) },
                    color = LightBlue,
                    fontSize = descriptionSize
                )
            }
        }
    }
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
        Modifier,
        { },
        notes = listOf(
            Note(0, "Grocery List Shopping", "Milk, Eggs, Bread, Butter"),
            Note(0, "Work Meeting", "Discuss project milestones and deadlines"),
            Note(0, "Gym Session", "Leg day workout at 6 PM")
        )
    )
}
