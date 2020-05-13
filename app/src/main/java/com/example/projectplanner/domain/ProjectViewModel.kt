package com.example.projectplanner.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.projectplanner.data.db.ProjectPlannerDatabase
import com.example.projectplanner.data.db.ProjectPlannerRepository
import com.example.projectplanner.data.db.models.Project
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

class ProjectViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectPlannerRepository
    val allProjects: List<Project>

    init {
        val projectPlannerDao = ProjectPlannerDatabase.getDatabase(application, viewModelScope).projectPlannerDao()
        repository = ProjectPlannerRepository(projectPlannerDao)
        allProjects = repository.allProjects
    }
}