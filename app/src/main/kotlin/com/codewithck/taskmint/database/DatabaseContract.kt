package com.codewithck.taskmint.database

object DatabaseContract {

    const val DATABASE_NAME = "taskmint.db"
    const val DATABASE_VERSION = 1

    object TaskTable {
        const val TABLE_NAME = "tasks"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PRIORITY = "priority"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_DUE_DATE = "due_date"
        const val COLUMN_COMPLETED = "completed"
    }
}