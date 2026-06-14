package com.justlime.simplenotesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.justlime.simplenotesapp.ui.graphs.GraphRoute
import com.justlime.simplenotesapp.ui.note.notes_list.NotesListScreen
import com.justlime.simplenotesapp.ui.note.notes_list.NotesViewModel
import com.justlime.simplenotesapp.ui.note.upsert_note.UpsertNoteScreen
import com.justlime.simplenotesapp.ui.payment_form.PaymentFormRoute
import com.justlime.simplenotesapp.ui.route.UpsertNoteRoute
import com.justlime.simplenotesapp.ui.route.UpsertTaskRoute
import com.justlime.simplenotesapp.ui.task.state.UiTaskState
import com.justlime.simplenotesapp.ui.task.tasks_list.TaskListScreen
import com.justlime.simplenotesapp.ui.task.tasks_list.TaskViewModel
import com.justlime.simplenotesapp.ui.task.upsert_task.UpsertTaskScreen
import com.justlime.simplenotesapp.ui.task.upsert_task.UpsertTaskViewModel
import com.justlime.simplenotesapp.ui.theme.SimpleNotesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNotesAppTheme {
                var selectedApp by rememberSaveable { mutableStateOf(AppType.GRAPH) }
                return@SimpleNotesAppTheme when (selectedApp) {
                    AppType.FORM -> Scaffold { PaymentFormRoute(it) }
                    AppType.NOTES -> NavigationTab()
                    AppType.MAIN -> SimpleNav { selectedApp = it }
                    AppType.GRAPH -> GraphRoute()
                }
            }
        }
    }
}

enum class AppType {
    FORM, NOTES, MAIN, GRAPH
}

@Composable
fun SimpleNav(onClick: (AppType) -> Unit) {
    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                { onClick(AppType.FORM) }) {
                Text("Form App")
            }
            Button(
                { onClick(AppType.NOTES) }) {
                Text("Notes App")
            }
            Button(
                {onClick(AppType.GRAPH)}
            ){
                Text("Graph App")
            }
        }
    }
}

enum class Destination(
    val route: String, val label: String, val icon: ImageVector, val contentDescription: String
) {
    NOTES("notes_list", "Notes", Icons.Outlined.NoteAlt, "Visit page of notes"), TASKS(
        "tasks_list", "Task", Icons.Outlined.CheckBox, "Visit page of Tasks"
    )
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = startDestination.route
    ) {

        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.NOTES -> {
                        val viewModel: NotesViewModel = hiltViewModel()
                        val notes by viewModel.notes.collectAsState(initial = emptyList())
                        NotesListScreen(modifier = modifier, { note ->
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
                                        viewModel.onUndoNote(note)
                                    }
                                }
                            }
                        }, notes = notes, onClick = {
                            val route = UpsertNoteRoute(it.id, isAdding = false)
                            navController.navigate(route)
                        }, onDelete = { id -> viewModel.onDeleteNote(id) })
                    }

                    Destination.TASKS -> {
                        val viewModel: TaskViewModel = hiltViewModel()
                        val taskUiState by viewModel.state.collectAsState()
                        Log.d("myApp", taskUiState.toString())
                        if (taskUiState is UiTaskState.Success) {
                            Log.d(
                                "myApp",
                                "The Size is ${(taskUiState as UiTaskState.Success).tasks.size}"
                            )
                        }

                        TaskListScreen(modifier, onCheckBoxClick = {}, onSnackBarLaunch = { task ->
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    "You have deleted a task",
                                    "Undo",
                                    true,
                                    SnackbarDuration.Long
                                )
                                when (result) {
                                    SnackbarResult.Dismissed -> {}
                                    SnackbarResult.ActionPerformed -> {
                                        viewModel.onUndoTask(task)
                                    }
                                }
                            }
                        }, taskState = taskUiState, onClick = {
                            val route = UpsertTaskRoute(it.id, isAdding = false)
                            navController.navigate(route)
                        }, onDelete = { id -> viewModel.onDeleteTask(id) })
                    }
                }
            }
        }
        composable<UpsertNoteRoute> { backStackHandler ->
            UpsertNoteScreen(
                modifier = modifier,
                onUpdateNote = {
                   // viewmodel.onUpdateNote(it)
                               },
                onAddNote = {
                  //  viewmodel.onAddNote(it)
                            },
                onBack = { navController.popBackStack() })
        }
        composable<UpsertTaskRoute> { backStackHandler ->
            val viewmodel = hiltViewModel<UpsertTaskViewModel>()
            viewmodel.setTaskById()
            val uiState by viewmodel.state.collectAsStateWithLifecycle()
            val isAdding = viewmodel.isAdding
            val task = viewmodel.task.collectAsStateWithLifecycle()
            UpsertTaskScreen(
                modifier,
                isAdding = isAdding,
                task = task.value ?: viewmodel.initialTask,
                onUpdate = { viewmodel.onUpdateTask(it) },
                onAdd = { viewmodel.onAddTask(it) },
                onBack = { navController.popBackStack() })
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTab(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<MainViewModel>()
    val navController = rememberNavController()
    val startDestination = Destination.NOTES
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val destination = Destination.entries[selectedDestination]
    val date by viewModel.date.collectAsState(null)

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            val route = UpsertNoteRoute(-1, true)
            when (destination) {
                Destination.NOTES -> {
                    navController.navigate(route)
                }

                Destination.TASKS -> {
                    val route = UpsertTaskRoute(-1, true)
                    navController.navigate(route)
                }
            }
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }, snackbarHost = {
        SnackbarHost(snackBarHostState)
    }, topBar = {
        TopAppBar(title = {
            when (destination) {
                Destination.NOTES -> {
                    Column {
                        Text("Write Notes", color = Color.Blue, fontSize = 48.sp)
                        if (date != null) {
                            DateDisplay(date!!)
                        }
                    }
                }

                Destination.TASKS -> {
                    Column {
                        Text("Task Flow", color = Color.Blue, fontSize = 48.sp)
                        if (date != null) {
                            DateDisplay(date!!)
                        }
                    }
                }
            }
        })
    }, bottomBar = {
        NavigationBar(
            windowInsets = NavigationBarDefaults.windowInsets
        ) {
            Destination.entries.forEachIndexed { index, destination ->
                NavigationBarItem(selected = selectedDestination == index, onClick = {
                    navController.navigate(route = destination.route)
                    selectedDestination = index
                }, label = {
                    Text(
                        text = destination.label, maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                }, icon = { Icon(destination.icon, destination.contentDescription) })
            }
        }
    }

    ) { contentPadding ->
        AppNavHost(
            navController,
            startDestination,
            scope,
            snackBarHostState,
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
        )
    }


}


@Composable
fun DateDisplay(dateTime: LocalDateTime) {
    val customFormat = remember {
        LocalDateTime.Format {
            // Jun 11, 2026 16:15
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            this@Format.day(padding = Padding.ZERO)
            char(',')
            char(' ')
            year()
        }
    }
    Text(text = remember(dateTime) { "Today - " + dateTime.format(customFormat) }, fontSize = 16.sp)
}
