package com.codewithck.taskmint.model

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val priority: String = "Medium",
    val category: String = "Personal",
    val dueDate: Long = 0L,
    val isCompleted: Boolean = false
)