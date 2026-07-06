package com.codewithck.taskmint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.graphics.Paint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.codewithck.taskmint.R
import com.codewithck.taskmint.model.Task

class TaskAdapter(
    private var taskList: MutableList<Task>,
    private val onTaskChecked: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkCompleted: CheckBox =
            itemView.findViewById(R.id.checkCompleted)

        val txtTitle: TextView =
            itemView.findViewById(R.id.txtTitle)

        val txtDescription: TextView =
            itemView.findViewById(R.id.txtDescription)

        val txtPriority: TextView =
            itemView.findViewById(R.id.txtPriority)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {

        val task = taskList[position]

        holder.txtTitle.text = task.title
        holder.txtDescription.text = task.description
        holder.txtPriority.text = task.priority
        holder.checkCompleted.setOnCheckedChangeListener(null)

        holder.checkCompleted.isChecked = task.isCompleted

        holder.checkCompleted.setOnCheckedChangeListener { _, isChecked ->

        task.isCompleted = isChecked

    onTaskChecked(task)
}
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
        
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun updateList(newList: MutableList<Task>) {
        taskList = newList
        notifyDataSetChanged()
    }
}