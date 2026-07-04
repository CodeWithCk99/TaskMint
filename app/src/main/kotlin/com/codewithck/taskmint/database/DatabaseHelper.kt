package com.codewithck.taskmint.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DatabaseContract.DATABASE_NAME,
    null,
    DatabaseContract.DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {

        val createTableQuery = """
            CREATE TABLE ${DatabaseContract.TaskTable.TABLE_NAME} (
                ${DatabaseContract.TaskTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseContract.TaskTable.COLUMN_TITLE} TEXT NOT NULL,
                ${DatabaseContract.TaskTable.COLUMN_DESCRIPTION} TEXT,
                ${DatabaseContract.TaskTable.COLUMN_PRIORITY} TEXT,
                ${DatabaseContract.TaskTable.COLUMN_CATEGORY} TEXT,
                ${DatabaseContract.TaskTable.COLUMN_DUE_DATE} INTEGER,
                ${DatabaseContract.TaskTable.COLUMN_COMPLETED} INTEGER DEFAULT 0
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL(
            "DROP TABLE IF EXISTS ${DatabaseContract.TaskTable.TABLE_NAME}"
        )

        onCreate(db)
    }
}