package com.codewithck.taskmint.bottomsheet

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.codewithck.taskmint.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.widget.Toast
import com.codewithck.taskmint.model.Task
import com.codewithck.taskmint.repository.TaskRepository

class AddTaskBottomSheet : BottomSheetDialogFragment() {

    interface OnTaskSavedListener {
        fun onTaskSaved()
    }

    private var listener: OnTaskSavedListener? = null

    fun setOnTaskSavedListener(listener: OnTaskSavedListener) {
        this.listener = listener
    }
    
    fun setEditTask(task: Task) {
    editTask = task
    isEditMode = true
}

    private lateinit var etTaskTitle: EditText
    private lateinit var etDescription: EditText

    private lateinit var rgPriority: RadioGroup
    private lateinit var rbLow: RadioButton
    private lateinit var rbMedium: RadioButton
    private lateinit var rbHigh: RadioButton

    private lateinit var actCategory: AutoCompleteTextView

    private lateinit var btnDueDate: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSaveTask: Button

    private val calendar = Calendar.getInstance()
    private var selectedDueDate: Long = 0L
    private lateinit var repository: TaskRepository
    
    private var editTask: Task? = null
private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.bottom_sheet_add_task,
            container,
            false
        )

        etTaskTitle = view.findViewById(R.id.etTaskTitle)
        etDescription = view.findViewById(R.id.etDescription)

        rgPriority = view.findViewById(R.id.rgPriority)
        rbLow = view.findViewById(R.id.rbLow)
        rbMedium = view.findViewById(R.id.rbMedium)
        rbHigh = view.findViewById(R.id.rbHigh)

        actCategory = view.findViewById(R.id.actCategory)

        btnDueDate = view.findViewById(R.id.btnDueDate)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnSaveTask = view.findViewById(R.id.btnSaveTask)
        repository = TaskRepository(requireContext())

        if (isEditMode && editTask != null) {

    etTaskTitle.setText(editTask!!.title)
    etDescription.setText(editTask!!.description)

    selectedDueDate = editTask!!.dueDate

    if (selectedDueDate != 0L) {

        calendar.timeInMillis = selectedDueDate

        val formattedDate = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).format(calendar.time)

        btnDueDate.text = "📅 $formattedDate"
    }

    actCategory.setText(editTask!!.category, false)

    when (editTask!!.priority) {

        "Low" -> rbLow.isChecked = true

        "High" -> rbHigh.isChecked = true

        else -> rbMedium.isChecked = true
    }

    btnSaveTask.text = "Update Task"
}
        
        val categories = arrayOf(
            "Personal",
            "Work",
            "Study",
            "Shopping",
            "Health",
            "Finance"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        )

        actCategory.setAdapter(adapter)

        btnDueDate.setOnClickListener {

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->

                    calendar.set(year, month, day)

                    selectedDueDate = calendar.timeInMillis

                    val formattedDate = SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)

                    btnDueDate.text = "📅 $formattedDate"

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

            ).show()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
        
        btnSaveTask.setOnClickListener {

    val title = etTaskTitle.text.toString().trim()

    if (title.isEmpty()) {
        etTaskTitle.error = "Title is required"
        etTaskTitle.requestFocus()
        return@setOnClickListener
    }

    val description = etDescription.text.toString().trim()

    val priority = when (rgPriority.checkedRadioButtonId) {
        R.id.rbLow -> "Low"
        R.id.rbHigh -> "High"
        else -> "Medium"
    }

    val category = if (actCategory.text.toString().trim().isEmpty()) {
        "Personal"
    } else {
        actCategory.text.toString().trim()
    }

    val task = if (isEditMode) {

    Task(
        id = editTask!!.id,
        title = title,
        description = description,
        priority = priority,
        category = category,
        dueDate = selectedDueDate,
        isCompleted = editTask!!.isCompleted
    )

} else {

    Task(
        title = title,
        description = description,
        priority = priority,
        category = category,
        dueDate = selectedDueDate
    )
}

val success = if (isEditMode) {
    repository.updateTask(task) > 0
} else {
    repository.addTask(task) > 0
}
    if (success) {
        Toast.makeText(
            requireContext(),
            "Task saved successfully",
            Toast.LENGTH_SHORT
        ).show()

        listener?.onTaskSaved()
        dismiss()
    } else {
        Toast.makeText(
    requireContext(),
    if (isEditMode) "Task updated successfully"
    else "Task saved successfully",
    Toast.LENGTH_SHORT
).show()
    }
}

        return view
    }
}