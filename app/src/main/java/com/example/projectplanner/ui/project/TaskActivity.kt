package com.example.projectplanner.ui.project

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.ColumnInfo
import com.example.projectplanner.R
import com.example.projectplanner.data.db.models.Task
import com.example.projectplanner.domain.ProjectViewModel
import com.example.projectplanner.ui.grid.PROJECT_ID
import kotlinx.android.synthetic.main.activity_task.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class TaskActivity : AppCompatActivity() {

    @Inject
    lateinit var projectViewModel: ProjectViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val projectID = intent.getLongExtra(PROJECT_ID, 0)

        var startDate: Date? = null
        var endDate: Date? = null

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
                    val task = Task(UUID.randomUUID().toString().toLong(), projectID, task_name.text.toString(), null,
                        start, end, 0, false)

                    projectViewModel.insertTask(task)
                } }
        }
    }
}
