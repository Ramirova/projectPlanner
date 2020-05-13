package com.example.projectplanner.ui.grid

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.projectplanner.R
import com.example.projectplanner.data.db.models.Project
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.util.*

class CreateProjectActivity : AppCompatActivity() {

    var startDate = Date()
    var endDate = Date()
    var chosenColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_create)
    }

    fun onChooseStartDateButtonClick(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val startDateText = findViewById<TextView>(R.id.create_project_start_date_text)
                    .setText(dayOfMonth.toString() + "." + monthOfYear + "." + year)
                startDate = Date(year, monthOfYear, dayOfMonth)
            }, year, month, day
        )

        dpd.show()
    }

    fun onChooseEndDateButtonClick(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val endValue = "$dayOfMonth.$monthOfYear.$year"
                findViewById<TextView>(R.id.create_project_end_date_text).text = endValue
                endDate = Date(year, monthOfYear, dayOfMonth)
            }, year, month, day
        )

        dpd.show()
    }

    fun onChooseColorPickerButtonClick(view: View) {
        var mDefaultColor = ContextCompat.getColor(this, R.color.colorPrimary)
        var layout = findViewById<ConstraintLayout>(R.id.create_project_layout)

        val ambilWarnaListener = object : OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog?) {}
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                // here the work with color
                chosenColor = color

                var newColor = "#" + Integer.toHexString(color)
                findViewById<TextView>(R.id.create_project_color_text).text = newColor
            }
        }
        var colorPicker = AmbilWarnaDialog(this, mDefaultColor, ambilWarnaListener)

        colorPicker.show()
    }

    fun onCancelButtonClick(view: View) {
        this.onBackPressed()
    }

    fun onCreateButtonClick(view: View) {
        // input data processing
        // input values
        val projectTitle =
            findViewById<EditText>(R.id.create_project_title_input).text.toString()
        val projectDescription =
            findViewById<EditText>(R.id.create_project_description_input).text.toString()
        val projectStartDate = startDate
        val projectEndDate = endDate
        val projectColor = chosenColor

        val newProject = Project(
            -1,
            projectTitle,
            projectDescription,
            projectStartDate, projectEndDate,
            Color.valueOf(projectColor)
        )

    }
}