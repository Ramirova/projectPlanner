package com.example.projectplanner.ui.project

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.projectplanner.ProjectPlannerApplication
import com.example.projectplanner.R
import com.example.projectplanner.data.db.models.Task
import com.example.projectplanner.domain.ProjectViewModel
import kotlinx.android.synthetic.main.activity_task.*
import java.util.*
import javax.inject.Inject

class TaskActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var projectViewModel: ProjectViewModel

    private var currentProjectId: Long = 0
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var currentTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        (application as ProjectPlannerApplication).appComponent.inject(this)

        populateProjects()

        select_project.onItemSelectedListener = this

        task_screen_title.text = getString(R.string.create_new_task)

        start_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this@TaskActivity,
                DatePickerDialog.OnDateSetListener { _, sY, sM, sD ->
                    start_date.setText("$sD/${sM + 1}/$sY")
                    startDate = Date(sY - 1900, sM, sD)
                }, year, month, day
            ).show()
        }

        end_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this@TaskActivity,
                DatePickerDialog.OnDateSetListener { _, sY, sM, sD ->
                    end_date.setText("$sD/${sM + 1}/$sY")
                    endDate = Date(sY - 1900, sM, sD)
                }, year, month, day
            ).show()
        }

        save_button.setOnClickListener {
            startDate?.let { start ->
                endDate?.let { end ->
                    val task = Task(
                        0, currentProjectId, task_name.text.toString(), "",
                        start, end, 0, false
                    )

                    projectViewModel.insertTask(task)
                    onBackPressed()
                }
            }
        }

        cancel_button.setOnClickListener {
            onBackPressed()
        }

        // finally, check if we've been passed a task id to maybe trigger a subscription

        if (intent.hasExtra("EXTRA_TASK_ID")) {
            currentTask = projectViewModel.getTaskByIdWithoutLiveDataBullshit(
                intent.getLongExtra("EXTRA_TASK_ID", 0)
            )
        }
    }

    private fun changeActivityToEditTask() {

        currentTask?.let {task ->

            task_screen_title.text = getString(R.string.edit_task)

            // update selections to Task information

            task_name.setText(currentTask!!.taskTitle)

            select_project.setSelection(
                (select_project.adapter as ArrayAdapter<String>).getPosition(
                    projectViewModel.getTaskParentProjectWithoutLiveDataBullshit(task).projectTitle
                )
            )

            task.taskStartDate.let {
                startDate = it
                start_date.setText("${it.date}/${it.month + 1}/${it.year + 1900}")
            }

            task.taskEndDate.let {
                endDate = it
                end_date.setText("${it.date}/${it.month + 1}/${it.year + 1900}")
            }

            // update onClickListeners to modify selected Task

            delete_button.setOnClickListener {
                projectViewModel.deleteTask(task)
                Toast.makeText(it.context, "Task Deleted", Toast.LENGTH_LONG).show()
                onBackPressed()
            }
            delete_button.visibility = View.VISIBLE
            delete_button.isEnabled = true

            save_button.setOnClickListener {
                startDate?.let { start ->
                    endDate?.let { end ->
                        val newTask = Task(
                            task.taskId,
                            currentProjectId,
                            task_name.text.toString(),
                            "",
                            start, end,
                            0, false
                        )
                        projectViewModel.insertTask(newTask)
                        onBackPressed()
                    }
                }
            }
        }
    }

    private fun populateProjects() {
        projectViewModel.allProjects.observe(this, Observer { projects ->
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                projects.map { project -> project.projectTitle })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            select_project.adapter = adapter

            // XXX: this is horrible, but things above would only happen AFTER onCreate,
            // so we have to wait until we have a proper spinner to try and select an item from it
            changeActivityToEditTask()
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val currentProjectName = select_project.selectedItem.toString()

        projectViewModel.allProjects.value?.findLast { project ->
            project.projectTitle == currentProjectName }?.projectId?.let { currentProjectId = it }
    }
}
