package com.codewithck.taskmint.database

import android.content.ContentValues
import android.content.Context
import com.codewithck.taskmint.model.Task

class DatabaseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertTask(task: Task): Long {

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseContract.TaskTable.COLUMN_TITLE, task.title)
            put(DatabaseContract.TaskTable.COLUMN_DESCRIPTION, task.description)
            put(DatabaseContract.TaskTable.COLUMN_PRIORITY, task.priority)
            put(DatabaseContract.TaskTable.COLUMN_CATEGORY, task.category)
            put(DatabaseContract.TaskTable.COLUMN_DUE_DATE, task.dueDate)
            put(
                DatabaseContract.TaskTable.COLUMN_COMPLETED,
                if (task.isCompleted) 1 else 0
            )
        }

        val result = db.insert(
            DatabaseContract.TaskTable.TABLE_NAME,
            null,
            values
        )

        db.close()

        return result
    }
}