package com.justlime.simplenotesapp.utils

import com.justlime.simplenotesapp.domain.enums.Priority
import com.justlime.simplenotesapp.domain.enums.Status
import com.justlime.simplenotesapp.domain.models.Task

val mockTask = Task(
    2,
    "Complete University Assignment",
    "The University has given a assignment which should be complete before Exam in mid July",
    Status.PENDING,
    Priority.LOW
)

val mockTaskList = listOf(
    Task(
        0,
        "Go To Library",
        "Submit the library book and/or renew as well borrow new books",
        Status.PENDING,
        Priority.MEDIUM
    ),
    Task(
        1, "Complete Todo App", "The App submission should be done within June", Status.ONGOING,
        Priority.HIGH
    ),
    Task(
        2,
        "Complete University Assignment",
        "The University has given a assignment which should be complete before Exam in mid July",
        Status.COMPLETED,
        Priority.LOW
    )
)