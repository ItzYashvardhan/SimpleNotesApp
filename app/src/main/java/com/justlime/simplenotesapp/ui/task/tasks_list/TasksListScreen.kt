package com.justlime.simplenotesapp.ui.task.tasks_list

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.simplenotesapp.domain.enums.Priority
import com.justlime.simplenotesapp.domain.enums.Status
import com.justlime.simplenotesapp.domain.models.Task
import com.justlime.simplenotesapp.ui.note.notes_list.ShowDescription
import com.justlime.simplenotesapp.ui.task.state.UiTaskState
import com.justlime.simplenotesapp.ui.theme.LightBlue
import com.justlime.simplenotesapp.utils.mockTask
import com.justlime.simplenotesapp.utils.mockTaskList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    onSnackBarLaunch: (Task) -> Unit,
    taskState: UiTaskState,
    onCheckBoxClick: (Status) -> Unit,
    onClick: (task: Task) -> Unit = {},
    onUndo: (task: Task) -> Unit = {},
    onDelete: (id: Int) -> Unit = {},
) {

    when (taskState) {
        UiTaskState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is UiTaskState.Success -> {
            TaskListContent(
                modifier,
                taskState.tasks,
                onCheckBoxClick,
                onClick,
                onSnackBarLaunch,
                onDelete
            )
        }

        is UiTaskState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Error Loading Task!")
            }
            return
        }
    }
}

@Composable
private fun TaskListContent(
    modifier: Modifier,
    tasks: List<Task>,
    onCheckBoxClick: (Status) -> Unit,
    onClick: (Task) -> Unit,
    onSnackBarLaunch: (Task) -> Unit,
    onDelete: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(10.dp,4.dp),
    ) {
        items(tasks) { note ->
            TaskCard(note, onCheckBoxClick, onClick) { task ->
                onSnackBarLaunch(task)
                onDelete(task.id)
            }
        }
    }
    if (tasks.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No tasks available")
        }
    }
}


@Composable
fun TaskCard(
    task: Task,
    onCheckBoxClick: (Status) -> Unit,
    onClick: (task: Task) -> Unit,
    onDelete: (task: Task) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                onClick(task)
            }
    ) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        var isCheck by rememberSaveable { mutableStateOf(task.status == Status.COMPLETED) }
        Row(modifier = Modifier.fillMaxSize()) {


            Box {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.Close, "Delete Note", Modifier.clickable {
                        onDelete(task)
                    })
                }
                Column() {
                    Row(Modifier.fillMaxSize().padding(8.dp)) {
                        Column(modifier = Modifier.padding(6.dp, 16.dp)) {
                            val trimTitle = task.title.trim()
                            var finalTitle = trimTitle
                            val maxTitleSize = 16

                            if (trimTitle.length > maxTitleSize) {
                                finalTitle =
                                    trimTitle.dropLast(trimTitle.length - maxTitleSize) + "..."
                            }
                            Text(finalTitle, fontSize = 28.sp)
                            Spacer(Modifier.height(4.dp))
                            Log.d(
                                "myApp",
                                "Length: ${task.description.length} of ${task.description}"
                            )
                            ShowDescription(isExpanded, task.description) {
                                isExpanded = it
                            }
                            Row(
                                Modifier
                                    .padding(8.dp, 4.dp)
                                    .fillMaxSize()
                            ) {
                                PriorityButton(task.priority.name)
                                Spacer(Modifier.weight(1f))
                                StatusButton(task.status.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusButton(text: String) {
    Text("Status: ", fontSize = 8.sp)
    Spacer(Modifier.width(6.dp))
    var color = Color.White
    Card(
        colors = CardDefaults.cardColors(containerColor = pickColor(Status.valueOf(text)) {
            color = it
        })
    ) {
        Text(text, fontSize = 8.sp, modifier = Modifier.padding(8.dp, 0.dp), color = color, textAlign = TextAlign.Center)
    }
}

@Composable
fun PriorityButton(text: String) {
    Text("Priority: ", fontSize = 10.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.width(6.dp))
    var color = Color.White
    Card(
        colors = CardDefaults.cardColors(containerColor = pickColor(Priority.valueOf(text)) {
            color = it
        }),
    ) {
        Text(text, fontSize = 8.sp, modifier = Modifier.padding(8.dp, 0.dp), color = color, textAlign = TextAlign.Center)
    }
}

fun pickColor(priority: Priority, textColor: (Color) -> Unit): Color {
    return when (priority) {
        Priority.HIGH -> {
            textColor(Color.White)
            Color.Red
        }

        Priority.MEDIUM -> {
            textColor(Color.Black)
            LightBlue
        }

        Priority.LOW -> {
            textColor(Color.Black)
            Color.Yellow
        }
    }
}

fun pickColor(status: Status, textColor: (Color) -> Unit): Color {
    return when (status) {
        Status.COMPLETED -> {
            textColor(Color.White)
            Color.Green
        }

        Status.ONGOING -> {
            textColor(Color.Black)
            Color.Cyan
        }

        Status.PENDING -> {
            textColor(Color.Black)
            Color.White
        }
    }
}

@Preview("TaskCardPreview", heightDp = 120, backgroundColor = 0xFFFFFFFF)
@Composable
fun TaskCardPreview() {
    TaskCard(mockTask, {}, {}, {})
}

@Preview("TaskListScreen", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TaskCardList() {
    TaskListScreen(Modifier, {}, taskState = UiTaskState.Success(mockTaskList), {}, {})
}