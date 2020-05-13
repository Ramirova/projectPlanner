package com.example.projectplanner.ui.grid

import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.projectplanner.ProjectPlannerApplication
import com.example.projectplanner.R
import com.example.projectplanner.domain.ProjectViewModel
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.app_toolbar.*
import com.example.projectplanner.ui.ProjectTableView
import com.example.projectplanner.ui.project.TaskActivity
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var projectViewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as ProjectPlannerApplication).appComponent.inject(this)

        // init toolbar
        initializeToolbar()

        // Update list of projects
        val projectNames = arrayOf("", "MAP", "MobDev")
        timetable.updateHeaderTitle(projectNames)

        // Update number of days in month
        val numDays = 30
        timetable.updateNumDays(numDays)

        // Create task
        val schedules = ArrayList<Schedule>()
        val schedule = Schedule()
        schedule.classTitle = "Data Structure" // sets subject (header of the task)
        schedule.classPlace = "IT-601" // sets place (can be used as short description)

        // How to use Time as date?
        // Pretend like hour is a day
        // E. g. below you can see task starting at 25 day of month and ending at 28
        schedule.startTime = Time(25, 0) // sets the beginning of task date (day)
        schedule.endTime = Time(28, 0) // sets the end of task date (day)
        schedule.day = 1 // index of project in a list of projects

        schedules.add(schedule)
        timetable.add(schedules)

        // Here is how it possible to get all stickers and their indices
        // timetable.allSchedulesInStickers

        // Remove task
        timetable.remove(0)

        schedules.add(schedule)
        timetable.add(schedules)


        val taskIntent = Intent(this, TaskActivity::class.java)

        timetable.setOnStickerSelectEventListener(object : ProjectTableView.OnStickerSelectedListener {
            override fun OnStickerSelected(idx: Int, schedules: java.util.ArrayList<Schedule>?){
                startActivity(taskIntent)
            }
        })
    }

    // toolbar initializing
    fun initializeToolbar() {
        setSupportActionBar(toolbar as Toolbar?)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;
        if (id == R.id.create_project_btn) {
            onCreateProjectButtonClick()
        }
        else if (id == R.id.archived_projects_btn) {
            // move to archived projects view
        }
        return super.onOptionsItemSelected(item)
    }

    fun onCreateProjectButtonClick() {
        val createProjectIntent = Intent(this, CreateProjectActivity::class.java)
        startActivity(createProjectIntent)
    }

    fun onPlusButtonCLick(view: View) {
        val taskIntent = Intent(this, TaskActivity::class.java)
        startActivity(taskIntent)
    }
}


