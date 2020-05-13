package com.example.projectplanner.ui.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectplanner.R
import com.example.projectplanner.domain.ProjectViewModel
import javax.inject.Inject

class ProjectActivity : AppCompatActivity() {

    @Inject
    lateinit var projectViewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
    }
}
