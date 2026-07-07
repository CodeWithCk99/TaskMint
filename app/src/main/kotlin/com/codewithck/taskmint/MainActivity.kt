package com.codewithck.taskmint

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codewithck.taskmint.adapter.TaskAdapter
import com.codewithck.taskmint.bottomsheet.AddTaskBottomSheet
import com.codewithck.taskmint.model.Task
import com.codewithck.taskmint.repository.TaskRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(),
    AddTaskBottomSheet.OnTaskSavedListener {

    private lateinit var rvTasks: RecyclerView
    private lateinit var emptyLayout: View
    private lateinit var fabAdd: FloatingActionButton

    private lateinit var adapter: TaskAdapter
    private lateinit var repository: TaskRepository

    private var taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTasks = findViewById(R.id.rvTasks)
        emptyLayout = findViewById(R.id.emptyLayout)
        fabAdd = findViewById(R.id.fabAdd)

        repository = TaskRepository(this)

        adapter = TaskAdapter(

    taskList,

    onTaskChecked = { task ->

        val result = repository.updateTask(task)

        if (result <= 0) {
            Toast.makeText(
                this,
                "Failed to update task",
                Toast.LENGTH_SHORT
            ).show()
        }
    },

    onTaskClick = { task ->

        val bottomSheet = AddTaskBottomSheet()

        bottomSheet.setOnTaskSavedListener(this)

        bottomSheet.setEditTask(task)

        bottomSheet.show(
            supportFragmentManager,
            "EditTaskBottomSheet"
        )
    },

    onTaskLongClick = { task ->

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { _, _ ->

    val result = repository.deleteTask(task.id)

    if (result > 0) {

        Toast.makeText(
            this,
            "Task deleted successfully",
            Toast.LENGTH_SHORT
        ).show()

        loadTasks()

    } else {

        Toast.makeText(
            this,
            "Failed to delete task",
            Toast.LENGTH_SHORT
        ).show()
    }
}
            .setNegativeButton("Cancel", null)
            .show()
    }
)

        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter

        loadTasks()

        fabAdd.setOnClickListener {

            val bottomSheet = AddTaskBottomSheet()

            bottomSheet.setOnTaskSavedListener(this)

            bottomSheet.show(
                supportFragmentManager,
                "AddTaskBottomSheet"
            )
        }
    }

    private fun loadTasks() {

        taskList = repository.getAllTasks()

        adapter.updateList(taskList)

        if (taskList.isEmpty()) {
            emptyLayout.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            emptyLayout.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }

    override fun onTaskSaved() {
        loadTasks()
    }
}