package com.example.projectplanner.ui.project

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

    var currentProjectName: String = "" //TODO переделать в кортеж с именами
    var currentProjectId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        (application as ProjectPlannerApplication).appComponent.inject(this)

        var startDate: Date? = null
        var endDate: Date? = null

        populateProjects()

        start_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this@TaskActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    start_date.setText("$dayOfMonth/$monthOfYear/$year")
                    endDate = Date(year, month, day)
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
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    end_date.setText("$dayOfMonth/$monthOfYear/$year")
                    startDate = Date(year, month, day)
                }, year, month, day
            ).show()
        }

        save_button.setOnClickListener {
            startDate?.let { start ->
                endDate?.let { end ->
                    val task = Task(UUID.randomUUID().mostSignificantBits, currentProjectId, task_name.text.toString(), null,
                        start, end, 0, false)

                    projectViewModel.insertTask(task)
                } }
        }
    }

    private fun populateProjects() {
        projectViewModel.allProjects.observe(this, Observer { projects ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, projects.map { project ->  project.projectTitle })
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
