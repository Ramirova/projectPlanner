package com.example.projectplanner.ui.project

import android.app.DatePickerDialog
import android.content.Intent
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
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

class TaskActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var projectViewModel: ProjectViewModel

    var currentProjectName: String = "" //TODO переделать в кортеж с именами
    var currentProjectId: Long = 0

    var startDate: Date? = null
    var endDate: Date? = null

    var currentTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit)
        (application as ProjectPlannerApplication).appComponent.inject(this)

        currentTask = projectViewModel.getTask(
            intent.getLongExtra("EXTRA_TASK_ID", 0)
        ).value

        populateProjects()

        select_project.onItemSelectedListener = this

        start_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this@TaskActivity,
                DatePickerDialog.OnDateSetListener { view, sY, sM, sD ->
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
                DatePickerDialog.OnDateSetListener { view, sY, sM, sD ->
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

        if (currentTask != null) {
            delete_button.setOnClickListener {
                projectViewModel.deleteTask(currentTask!!)
                Toast.makeText(it.context, "Task Deleted", Toast.LENGTH_LONG).show()
                onBackPressed()
            }
            task_screen_title.text = "Edit Task"
            task_name.setText(currentTask!!.taskTitle)
            select_project.setSelection((select_project.adapter as ArrayAdapter<String>).getPosition(
                runBlocking {
                    projectViewModel.getProject(currentTask!!.parentProjectId).value!!.projectTitle
                }
            ))

            currentTask!!.taskStartDate.let {
                start_date.setText("${it.day}/${it.month}/${it.year}")
            }

            currentTask!!.taskEndDate.let {
                end_date.setText("${it.day}/${it.month}/${it.year}")
            }

        } else {
            delete_button.text = getString(R.string.Cancel)
            delete_button.setOnClickListener {
                onBackPressed()
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
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        currentProjectName = select_project.selectedItem.toString()

        projectViewModel.allProjects.value?.findLast { project -> project.projectTitle == currentProjectName }?.projectId?.let {
            currentProjectId = it
        }
    }
}
