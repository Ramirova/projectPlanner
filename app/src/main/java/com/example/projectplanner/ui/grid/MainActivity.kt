package com.example.projectplanner.ui.grid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.projectplanner.ProjectPlannerApplication
import com.example.projectplanner.R
import com.example.projectplanner.domain.ProjectViewModel
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import com.example.projectplanner.ui.ProjectTableView
import com.example.projectplanner.ui.project.TaskActivity
import kotlinx.android.synthetic.main.app_toolbar.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var projectViewModel: ProjectViewModel
    private val projectToColumns = ArrayList<Long>()
    private val projectNames = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as ProjectPlannerApplication).appComponent.inject(this)

        initializeToolbar()

        subscribeProjects()
        subscribeTasks()

        timetable.setOnStickerSelectEventListener(object :
            ProjectTableView.OnStickerSelectedListener {
            override fun OnStickerSelected(idx: Int, schedules: java.util.ArrayList<Schedule>?) {
                val taskIntent = Intent(this@MainActivity, TaskActivity::class.java)
                // FIXME: this handler provides a sticker.
                // TODO: Need to get make a task out of it, but how to get id?
                // taskIntent.putExtra("EXTRA_TASK", schedules!![idx])
                startActivity(taskIntent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // TODO: how observes behave on activity change?
    }

    private fun subscribeProjects() {
        projectViewModel.allProjects.observe(this, Observer { projects ->
            projectToColumns.clear()
            projectNames.clear()

            projects.forEach { project ->
                projectToColumns.add(project.projectId)
                projectNames.add(project.projectTitle)
            }

            timetable.removeAll()
            timetable.updateHeaderTitle(projectNames)
            projectViewModel.selectMonth(projectViewModel.selectedMonth.value!!)
        })
    }

    private fun subscribeTasks() {
        projectViewModel.selectedTasks.observe(this, Observer { tasks ->
            timetable.removeAll()
            if (!tasks.isNullOrEmpty()) {
                tasks.forEach { task ->
                    val schedules = ArrayList<Schedule>()
                    val schedule = Schedule()
                    schedule.classTitle = task.taskTitle
                    schedule.classPlace = task.taskDescription
                    schedule.startTime = Time(task.taskStartDate.date, 0)
                    schedule.endTime = Time(task.taskEndDate.date + 1, 0)
                    schedule.day = this@MainActivity.projectToColumns.indexOf(task.parentProjectId)
                    schedules.add(schedule)
                    timetable.add(schedules)
                }
            }
        })
    }

    // toolbar initializing
    fun initializeToolbar() {
        setSupportActionBar(toolbar as Toolbar?)
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // yes, this does look like an assignment completed by scholar. Too bad!
                // god I hope this is correct
                val nDays = when (monthSpinner.selectedItem.toString()) {
                    "January", "March", "May", "July", "August", "October", "December" -> 31
                    "April", "June", "September", "November" -> 30
                    "February" -> 28
                    else -> 30
                }
                timetable.removeAll()
                // this function removes everything except for stickers,
                // so get get pushed to the side.
                timetable.updateNumDays(nDays)
                projectViewModel.selectMonth(position)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;
        if (id == R.id.create_project_btn) {
            onCreateProjectButtonClick()
        } else if (id == R.id.archived_projects_btn) {
            // move to archived projects view
        }
        return super.onOptionsItemSelected(item)
    }

    fun onCreateProjectButtonClick() {
        val createProjectIntent = Intent(this, CreateProjectActivity::class.java)
        startActivity(createProjectIntent)
    }

    fun onPlusButtonCLick(view: View) {
        if (projectNames.isEmpty()) {
            Toast.makeText(this, "Create a project first!", Toast.LENGTH_SHORT).show()
        } else {
            val taskIntent = Intent(this, TaskActivity::class.java)
            startActivity(taskIntent)
        }
    }
}


