package com.codewithck.taskmint.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codewithck.taskmint.R
import com.codewithck.taskmint.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(
    private var taskList: MutableList<Task>,
    private val onTaskChecked: (Task) -> Unit,
    private val onTaskClick: (Task) -> Unit,
    private val onTaskLongClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val checkCompleted: CheckBox =
            itemView.findViewById(R.id.checkCompleted)

        val txtTitle: TextView =
            itemView.findViewById(R.id.txtTitle)

        val txtDescription: TextView =
            itemView.findViewById(R.id.txtDescription)

        val txtPriority: TextView =
            itemView.findViewById(R.id.txtPriority)

        val txtCategory: TextView =
            itemView.findViewById(R.id.txtCategory)

        val txtDueDate: TextView =
            itemView.findViewById(R.id.txtDueDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.task_item,
                parent,
                false
            )

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {

        val task = taskList[position]

        holder.txtTitle.text = task.title
        holder.txtDescription.text = task.description
        holder.txtCategory.text = "📂 ${task.category}"

        if (task.dueDate != 0L) {

            val sdf = SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
            )

            holder.txtDueDate.text =
                "📅 ${sdf.format(Date(task.dueDate))}"

        } else {

            holder.txtDueDate.text = "📅 No Date"

        }

        when (task.priority) {

            "High" -> {
                holder.txtPriority.text = "High"
                holder.txtPriority.setBackgroundColor(Color.parseColor("#EF4444"))
            }

            "Medium" -> {
                holder.txtPriority.text = "Medium"
                holder.txtPriority.setBackgroundColor(Color.parseColor("#F59E0B"))
            }

            else -> {
                holder.txtPriority.text = "Low"
                holder.txtPriority.setBackgroundColor(Color.parseColor("#22C55E"))
            }
        }

        holder.checkCompleted.setOnCheckedChangeListener(null)

        holder.checkCompleted.isChecked = task.isCompleted
        
               if (task.isCompleted) {

            holder.txtTitle.paintFlags =
                holder.txtTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.txtDescription.paintFlags =
                holder.txtDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.itemView.alpha = 0.55f

        } else {

            holder.txtTitle.paintFlags =
                holder.txtTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            holder.txtDescription.paintFlags =
                holder.txtDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            holder.itemView.alpha = 1f
        }

        holder.checkCompleted.setOnCheckedChangeListener { _, isChecked ->

            task.isCompleted = isChecked

            onTaskChecked(task)

            val adapterPosition = holder.bindingAdapterPosition

            if (adapterPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(adapterPosition)
            }
        }

        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }

        holder.itemView.setOnLongClickListener {
            onTaskLongClick(task)
            true
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun updateList(newList: MutableList<Task>) {
        taskList = newList
        notifyDataSetChanged()
    }
}