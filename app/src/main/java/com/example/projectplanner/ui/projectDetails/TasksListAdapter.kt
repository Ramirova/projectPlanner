package com.example.projectplanner.ui.projectDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectplanner.R
import com.example.projectplanner.data.db.models.Task
import java.text.SimpleDateFormat
import java.util.*

class TasksListAdapter internal constructor(val context: Context) : RecyclerView.Adapter<TasksListAdapter.TaskViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var animals = emptyList<Task>()

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name_text_view)
        val taskDate: TextView = itemView.findViewById(R.id.task_date_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = inflater.inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = animals[position]

        val startDate = toString(current.taskStartDate)
        val endDate = toString(current.taskEndDate)

        holder.taskName.text = current.taskTitle
        holder.taskDate.text = String.format(context.getString(R.string.task_date_string), startDate, endDate)
    }

    private fun toString(date: Date): String {
        val format = SimpleDateFormat("dd/MM/yy", Locale.US)
        return format.format(date)
    }

    internal fun setTasks(animals: List<Task>) {
        this.animals = animals
        notifyDataSetChanged()
    }

    override fun getItemCount() = animals.size
}


