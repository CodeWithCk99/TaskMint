package com.codewithck.taskmint.repository

import android.content.Context
import com.codewithck.taskmint.database.DatabaseManager
import com.codewithck.taskmint.model.Task

class TaskRepository(context: Context) {

    private val databaseManager = DatabaseManager(context)

    fun addTask(task: Task): Long {
        return databaseManager.insertTask(task)
    }

    fun getAllTasks(): MutableList<Task> {
        return databaseManager.getAllTasks()
    }

    fun updateTask(task: Task): Int {
        return databaseManager.updateTask(task)
    }

    fun deleteTask(id: Long): Int {
        return databaseManager.deleteTask(id)
    }
}