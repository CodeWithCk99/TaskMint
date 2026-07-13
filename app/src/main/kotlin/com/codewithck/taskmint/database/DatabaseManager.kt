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
            put(DatabaseContract.TaskTable.COLUMN_REMINDER_TIME, task.reminderTime)
            put(DatabaseContract.TaskTable.COLUMN_COMPLETED,
                if (task.isCompleted) 1 else 0)
        }

        val result = db.insert(
            DatabaseContract.TaskTable.TABLE_NAME,
            null,
            values
        )

        db.close()

        return result
    }

    fun getAllTasks(): MutableList<Task> {

        val taskList = mutableListOf<Task>()

        val db = dbHelper.readableDatabase

        val cursor = db.query(
    DatabaseContract.TaskTable.TABLE_NAME,
    null,
    null,
    null,
    null,
    null,
    "${DatabaseContract.TaskTable.COLUMN_COMPLETED} ASC, ${DatabaseContract.TaskTable.COLUMN_ID} DESC" 
        )

        if (cursor.moveToFirst()) {

            do {

                val task = Task(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.TaskTable.COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TaskTable.COLUMN_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TaskTable.COLUMN_DESCRIPTION))
                        ?: "",
                    priority = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TaskTable.COLUMN_PRIORITY))
                        ?: "Medium",
                    category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TaskTable.COLUMN_CATEGORY))
                        ?: "Personal",
                    dueDate = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.TaskTable.COLUMN_DUE_DATE)),
                    reminderTime = cursor.getLong(
    cursor.getColumnIndexOrThrow(
        DatabaseContract.TaskTable.COLUMN_REMINDER_TIME
    )
),
                    isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TaskTable.COLUMN_COMPLETED)) == 1
                )

                taskList.add(task)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return taskList
    }

    fun updateTask(task: Task): Int {

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseContract.TaskTable.COLUMN_TITLE, task.title)
            put(DatabaseContract.TaskTable.COLUMN_DESCRIPTION, task.description)
            put(DatabaseContract.TaskTable.COLUMN_PRIORITY, task.priority)
            put(DatabaseContract.TaskTable.COLUMN_CATEGORY, task.category)
            put(DatabaseContract.TaskTable.COLUMN_DUE_DATE, task.dueDate)
            put(DatabaseContract.TaskTable.COLUMN_REMINDER_TIME, task.reminderTime)
            put(DatabaseContract.TaskTable.COLUMN_COMPLETED,
                if (task.isCompleted) 1 else 0)
        }

        val result = db.update(
            DatabaseContract.TaskTable.TABLE_NAME,
            values,
            "${DatabaseContract.TaskTable.COLUMN_ID}=?",
            arrayOf(task.id.toString())
        )

        db.close()

        return result
    }

    fun deleteTask(id: Long): Int {

        val db = dbHelper.writableDatabase

        val result = db.delete(
            DatabaseContract.TaskTable.TABLE_NAME,
            "${DatabaseContract.TaskTable.COLUMN_ID}=?",
            arrayOf(id.toString())
        )

        db.close()

        return result
    }
}