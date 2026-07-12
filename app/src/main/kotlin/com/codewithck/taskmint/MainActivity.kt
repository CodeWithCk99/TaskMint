package com.codewithck.taskmint

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codewithck.taskmint.adapter.TaskAdapter
import com.codewithck.taskmint.bottomsheet.AddTaskBottomSheet
import com.codewithck.taskmint.bottomsheet.FilterBottomSheet
import com.codewithck.taskmint.bottomsheet.ThemeBottomSheet
import com.codewithck.taskmint.model.Task
import com.codewithck.taskmint.repository.TaskRepository
import com.codewithck.taskmint.utils.ThemeManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(),
    AddTaskBottomSheet.OnTaskSavedListener {

    private lateinit var rvTasks: RecyclerView
    private lateinit var emptyLayout: View

    private lateinit var fabAdd: FloatingActionButton

    private lateinit var etSearch: TextInputEditText

    private lateinit var btnFilter: ImageButton
    private lateinit var btnSettings: ImageButton

    private lateinit var tvTotalTasks: TextView
    private lateinit var tvPendingTasks: TextView
    private lateinit var tvCompletedTasks: TextView

    private lateinit var adapter: TaskAdapter
    private lateinit var repository: TaskRepository

    private var taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.applyTheme(this)

        super.onCreate(savedInstanceState)

        try {

            setContentView(R.layout.activity_main)

        } catch (e: Exception) {

            e.printStackTrace()

            Toast.makeText(
                this,
                e.message ?: "Layout Error",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        rvTasks = findViewById(R.id.rvTasks)
        emptyLayout = findViewById(R.id.emptyLayout)

        fabAdd = findViewById(R.id.fabAdd)

        etSearch = findViewById(R.id.etSearch)

        btnFilter = findViewById(R.id.btnFilter)
        btnSettings = findViewById(R.id.btnSettings)

        tvTotalTasks = findViewById(R.id.tvTotalTasks)
        tvPendingTasks = findViewById(R.id.tvPendingTasks)
        tvCompletedTasks = findViewById(R.id.tvCompletedTasks)

        repository = TaskRepository(this)
        
                adapter = TaskAdapter(

            taskList,

            onTaskChecked = { task ->

                val result = repository.updateTask(task)

                if (result > 0) {

                    loadTasks()

                } else {

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

                AlertDialog.Builder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Delete") { _, _ ->

                        if (repository.deleteTask(task.id) > 0) {

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

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                filterTasks(s.toString())
            }

            override fun afterTextChanged(
                s: Editable?
            ) {
            }
        })

        fabAdd.setOnClickListener {

            val bottomSheet = AddTaskBottomSheet()

            bottomSheet.setOnTaskSavedListener(this)

            bottomSheet.show(
                supportFragmentManager,
                "AddTaskBottomSheet"
            )
        }

        btnFilter.setOnClickListener {

            val bottomSheet = FilterBottomSheet()

            bottomSheet.setOnFilterAppliedListener(

                object : FilterBottomSheet.OnFilterAppliedListener {

                    override fun onFilterApplied(
                        priority: String,
                        status: String
                    ) {

                        applyFilter(
                            priority,
                            status
                        )
                    }
                }
            )

            bottomSheet.show(
                supportFragmentManager,
                "FilterBottomSheet"
            )
        }

        btnSettings.setOnClickListener {

            ThemeBottomSheet().show(
                supportFragmentManager,
                "ThemeBottomSheet"
            )
        }
    }
    
        private fun loadTasks() {

        taskList = repository.getAllTasks()

        adapter.updateList(taskList)

        updateDashboard()

        if (taskList.isEmpty()) {

            emptyLayout.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE

        } else {

            emptyLayout.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }

    private fun updateDashboard() {

        val total = taskList.size
        val completed = taskList.count { it.isCompleted }
        val pending = total - completed

        tvTotalTasks.text = total.toString()
        tvPendingTasks.text = pending.toString()
        tvCompletedTasks.text = completed.toString()
    }

    private fun filterTasks(query: String) {

        if (query.isBlank()) {

            adapter.updateList(taskList)
            updateDashboard()

            if (taskList.isEmpty()) {
                emptyLayout.visibility = View.VISIBLE
                rvTasks.visibility = View.GONE
            } else {
                emptyLayout.visibility = View.GONE
                rvTasks.visibility = View.VISIBLE
            }

            return
        }

        val filteredList = taskList.filter {

            it.title.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)

        }.toMutableList()

        adapter.updateList(filteredList)

        if (filteredList.isEmpty()) {
            emptyLayout.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            emptyLayout.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }

    private fun applyFilter(
        priority: String,
        status: String
    ) {

        val filteredList = taskList.filter { task ->

            val priorityMatch =
                priority == "All" ||
                task.priority.equals(priority, ignoreCase = true)

            val statusMatch = when (status) {

                "Pending" -> !task.isCompleted

                "Completed" -> task.isCompleted

                else -> true
            }

            priorityMatch && statusMatch

        }.toMutableList()

        adapter.updateList(filteredList)

        if (filteredList.isEmpty()) {

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