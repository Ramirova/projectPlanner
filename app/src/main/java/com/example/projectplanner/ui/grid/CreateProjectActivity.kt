package com.example.projectplanner.ui.grid

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projectplanner.R

class CreateProjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_create)
    }

    fun onCancelButtonClick(view: View) {
        this.onBackPressed()
    }

    fun onCreateButtonClick(view: View) {
        // input data processing
        // input values
        val projectTitle = findViewById<EditText>(R.id.create_project_title_input).text.toString()
        val projectDescription = findViewById<EditText>(R.id.create_project_description_input).text.toString()
        val projectStartDate = findViewById<EditText>(R.id.create_project_start_date_input).text.toString()
        val projectEndDate = findViewById<EditText>(R.id.create_project_end_date_input).text.toString()
        val projectColor = findViewById<EditText>(R.id.create_project_color_input).text.toString()
    }

    fun onChooseColorButtonClick(view: View) {

    }
}