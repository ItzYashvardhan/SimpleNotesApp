package com.justlime.simplenotesapp.ui.task.upsert_task

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.simplenotesapp.domain.enums.Priority
import com.justlime.simplenotesapp.domain.enums.Status
import com.justlime.simplenotesapp.domain.models.Task
import com.justlime.simplenotesapp.ui.task.state.uiTaskState
import com.justlime.simplenotesapp.utils.mockTask
import com.justlime.simplenotesapp.utils.mockTaskList

@Composable
fun UpsertTaskScreen(
    modifier: Modifier = Modifier,
    isAdding: Boolean,
    task: Task,
    taskState: uiTaskState,
    onAdd: (task: Task) -> Unit,
    onUpdate: (task: Task) -> Unit,
    onBack: () -> Unit
) {
    when (taskState) {
        uiTaskState.Loading -> {
            CircularProgressIndicator()
        }

        is uiTaskState.Error -> {

        }

        is uiTaskState.Success -> {
            TaskUpsertContent(taskState, task, modifier, isAdding, onAdd, onUpdate, onBack)
        }
    }

}

@Composable
private fun TaskUpsertContent(
    state: uiTaskState,
    task: Task,
    modifier: Modifier,
    isAdding: Boolean,
    onAdd: (Task) -> Unit,
    onUpdate: (Task) -> Unit,
    onBack: () -> Unit
) {


    var title by rememberSaveable(task.id) { mutableStateOf(task.title) }
    var description by rememberSaveable(task.id) { mutableStateOf(task.description) }

    var status by rememberSaveable {
        mutableStateOf(task.status)
    }
    var priority by rememberSaveable(task.id) { mutableStateOf(task.priority) }
    var priorityExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(task.status) {
        status = task.status
    }

    LaunchedEffect(task.priority) {
        priority = task.priority
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp, 8.dp)
    ) {
        Row {
            Text("Title")
            Spacer(Modifier.weight(1f))
            CustomTextField(title, true) {
                title = it
            }
        }

        OutlinedTextField(
            description,
            label = { Text("Description") },
            onValueChange = { description = it },
            minLines = 5,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Spacer(Modifier.height(8.dp))
        Row {
            Text("Select Priority")
            Spacer(Modifier.weight(1.0f))
            DropDownMenu(
                task.priority.name,
                { priorityExpanded = true },
                priorityExpanded,
                Priority.entries.map { it.name },
                { priorityExpanded = false }) {
                priority = Priority.valueOf(it)
                priorityExpanded = false
            }
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Text("Select Status")
            Spacer(Modifier.weight(1f))
            DropDownMenu(
                task.status.name,
                { statusExpanded = true },
                statusExpanded,
                Status.entries.map { it.name },
                { statusExpanded = false }) {
                status = Status.valueOf(it)
                statusExpanded = false
            }
        }
        Spacer(Modifier.fillMaxHeight(0.7f))
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button({
                val finalTask = Task(task.id, title, description, status, priority)

                if (isAdding) {
                    onAdd(finalTask)
                } else {
                    onUpdate(finalTask)
                    Log.d("myApp", "updated for $finalTask")
                }
                onBack()
            }, modifier = Modifier.padding(10.dp, 4.dp)) {
                if (isAdding) {
                    Icon(Icons.Outlined.AddTask, "Add Task")
                    Spacer(Modifier.width(6.dp))
                    Text("Add Task")
                } else {
                    Text("Update Task")
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        value,
        onValueChange,
        modifier = Modifier
            .width(250.dp)
            .height(if (singleLine) 34.dp else 90.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(text = "Enter text", color = Color.LightGray)
                }
                innerTextField()
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)

    )
}

@Composable
fun DropDownMenu(
    currentText: String,
    onShow: () -> Unit,
    expanded: Boolean,
    options: List<String>,
    onDisMiss: () -> Unit,
    onClick: (String) -> Unit,
) {
    var selectedText by rememberSaveable { mutableStateOf(currentText) }
    Card(
        modifier = Modifier.clickable { onShow() },
        shape = RectangleShape
    ) {
        Text(selectedText, fontSize = 12.sp, modifier = Modifier.padding(6.dp, 4.dp))
    }
    DropdownMenu(expanded, onDismissRequest = onDisMiss) {
        options.forEach {
            DropdownMenuItem(
                text = { Text(it) },
                onClick = { onClick(it); selectedText = it }
            )
        }
    }
}


@Composable
@Preview("Task Upsert Preview screen", showBackground = true)
fun PreviewUpsertTaskScreen() {
    UpsertTaskScreen(
        Modifier.padding(32.dp),
        true,
        mockTask,
        taskState = uiTaskState.Success(mockTaskList),
        {},
        {}) {}
}
