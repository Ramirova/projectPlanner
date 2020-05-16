package com.example.projectplanner.ui.project

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projectplanner.ProjectPlannerApplication
import com.example.projectplanner.R
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.domain.ProjectViewModel
import kotlinx.android.synthetic.main.project_create.*
import java.util.*
import javax.inject.Inject

class CreateProjectActivity : AppCompatActivity() {

    @Inject
    lateinit var projectViewModel: ProjectViewModel

    private var startDate = Date()
    private var endDate = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_create)
        (application as ProjectPlannerApplication).appComponent.inject(this)
        create_project_create_btn.setOnClickListener { onCreateButtonClick() }

        // put correct buttons on project view
        val project = intent.getParcelableExtra<Project>("project")
        if (project != null) {
            findViewById<Button>(R.id.create_project_create_btn).visibility = View.INVISIBLE
            findViewById<Button>(R.id.create_project_delete_btn).visibility = View.VISIBLE
        } else {
            findViewById<Button>(R.id.create_project_create_btn).visibility = View.VISIBLE
            findViewById<Button>(R.id.create_project_delete_btn).visibility = View.INVISIBLE
        }

        create_project_start_date_btn.setOnClickListener { onChooseStartDateButtonClick() }
        create_project_end_date_btn.setOnClickListener { onChooseEndDateButtonClick() }
        create_project_cancel_btn.setOnClickListener { onCancelButtonClick() }
    }

    private fun onChooseStartDateButtonClick() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, monthOfYear, dayOfMonth ->
                val startDateText = String.format(this.getString(R.string.date_dotted), dayOfMonth.toString(), monthOfYear, selectedYear)
                findViewById<TextView>(R.id.create_project_start_date_text).text = startDateText

                val cal = Calendar.getInstance()
                cal[Calendar.YEAR] = selectedYear
                cal[Calendar.MONTH] = monthOfYear
                cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                startDate = cal.time
            }, year, month, day
        )

        dpd.show()
    }

    private fun onChooseEndDateButtonClick() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, monthOfYear, dayOfMonth ->
                val endValue = "$dayOfMonth.$monthOfYear.$selectedYear"
                findViewById<TextView>(R.id.create_project_end_date_text).text = endValue

                val cal = Calendar.getInstance()
                cal[Calendar.YEAR] = selectedYear
                cal[Calendar.MONTH] = monthOfYear
                cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                endDate = cal.time
            }, year, month, day
        )



        dpd.show()
    }

    private fun onCancelButtonClick() {
        this.onBackPressed()
    }

    private fun onCreateButtonClick() {
        val newProject = Project(
            UUID.randomUUID().leastSignificantBits,
            findViewById<EditText>(R.id.create_project_title_input).text.toString(),
            findViewById<EditText>(R.id.create_project_description_input).text.toString(),
            startDate,
            endDate,
            Color.valueOf(0)
        )

        projectViewModel.insertProject(newProject)
        this.onBackPressed()
    }
}