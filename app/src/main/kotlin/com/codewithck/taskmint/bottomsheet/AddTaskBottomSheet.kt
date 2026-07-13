package com.codewithck.taskmint.bottomsheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import android.widget.TextView
import android.widget.Toast
import com.codewithck.taskmint.R
import com.codewithck.taskmint.model.Task
import com.codewithck.taskmint.repository.TaskRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.materialswitch.MaterialSwitch
import java.text.SimpleDateFormat
import com.codewithck.taskmint.utils.AlarmHelper
import java.util.Calendar
import java.util.Locale

class AddTaskBottomSheet : BottomSheetDialogFragment() {

    interface OnTaskSavedListener {
        fun onTaskSaved()
    }

    private var listener: OnTaskSavedListener? = null

    fun setOnTaskSavedListener(listener: OnTaskSavedListener) {
        this.listener = listener
    }

    private var editTask: Task? = null
    private var isEditMode = false

    fun setEditTask(task: Task) {
        editTask = task
        isEditMode = true
    }

    private lateinit var repository: TaskRepository

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

    private lateinit var switchReminder: MaterialSwitch
    private lateinit var txtReminderDate: TextView
    private lateinit var txtReminderTime: TextView

    private val calendar = Calendar.getInstance()
    private val reminderCalendar = Calendar.getInstance()

    private var selectedDueDate: Long = 0L

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

        repository = TaskRepository(requireContext())

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

        switchReminder = view.findViewById(R.id.switchReminder)
        txtReminderDate = view.findViewById(R.id.txtReminderDate)
        txtReminderTime = view.findViewById(R.id.txtReminderTime)
        
                switchReminder.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                txtReminderDate.visibility = View.VISIBLE
                txtReminderTime.visibility = View.VISIBLE
            } else {
                txtReminderDate.visibility = View.GONE
                txtReminderTime.visibility = View.GONE
            }

        }

        txtReminderDate.setOnClickListener {

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->

                    reminderCalendar.set(year, month, day)

                    val formatter = SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    )

                    txtReminderDate.text =
                        "📅 " + formatter.format(reminderCalendar.time)

                },
                reminderCalendar.get(Calendar.YEAR),
                reminderCalendar.get(Calendar.MONTH),
                reminderCalendar.get(Calendar.DAY_OF_MONTH)

            ).show()

        }

        txtReminderTime.setOnClickListener {

            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->

                    reminderCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    reminderCalendar.set(Calendar.MINUTE, minute)

                    val formatter = SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    )

                    txtReminderTime.text =
                        "🕒 " + formatter.format(reminderCalendar.time)

                },
                reminderCalendar.get(Calendar.HOUR_OF_DAY),
                reminderCalendar.get(Calendar.MINUTE),
                false

            ).show()

        }

        if (isEditMode && editTask != null) {

            etTaskTitle.setText(editTask!!.title)
            etDescription.setText(editTask!!.description)

            selectedDueDate = editTask!!.dueDate

            if (selectedDueDate != 0L) {

                calendar.timeInMillis = selectedDueDate

                val formatter = SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                )

                btnDueDate.text =
                    "📅 " + formatter.format(calendar.time)

            }

            actCategory.setText(
                editTask!!.category,
                false
            )

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

        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        )

        actCategory.setAdapter(categoryAdapter)

        btnDueDate.setOnClickListener {

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->

                    calendar.set(year, month, day)

                    selectedDueDate = calendar.timeInMillis

                    val formatter = SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    )

                    btnDueDate.text =
                        "📅 " + formatter.format(calendar.time)

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

            val description =
                etDescription.text.toString().trim()

            val priority = when (rgPriority.checkedRadioButtonId) {

                R.id.rbLow -> "Low"

                R.id.rbHigh -> "High"

                else -> "Medium"

            }

            val category =
                if (actCategory.text.toString().trim().isEmpty()) {

                    "Personal"

                } else {

                    actCategory.text.toString().trim()

                }

            val reminderTime =
                if (switchReminder.isChecked)
                    reminderCalendar.timeInMillis
                else
                    0L
                    
                                val task = if (isEditMode) {

                Task(
                    id = editTask!!.id,
                    title = title,
                    description = description,
                    priority = priority,
                    category = category,
                    dueDate = selectedDueDate,
                    reminderTime = reminderTime,
                    isCompleted = editTask!!.isCompleted
                )

            } else {

                Task(
                    title = title,
                    description = description,
                    priority = priority,
                    category = category,
                    dueDate = selectedDueDate,
                    reminderTime = reminderTime
                )

            }

            val success = if (isEditMode) {

                repository.updateTask(task) > 0

            } else {

                repository.addTask(task) > 0

            }

            if (success) {
            
            if (switchReminder.isChecked && reminderTime > 0L) {

    try {

        AlarmHelper.scheduleReminder(
            requireContext(),
            reminderTime,
            title,
            description.ifEmpty { "You have a pending task." }
        )

    } catch (e: Exception) {

        e.printStackTrace()

        Toast.makeText(
            requireContext(),
            e.message ?: "Reminder Error",
            Toast.LENGTH_LONG
        ).show()

    }
}


                Toast.makeText(
                    requireContext(),
                    if (isEditMode)
                        "Task updated successfully"
                    else
                        "Task saved successfully",
                    Toast.LENGTH_SHORT
                ).show()

                listener?.onTaskSaved()

                dismiss()

            } else {

                Toast.makeText(
                    requireContext(),
                    "Failed to save task",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }
        
                return view
    }

}