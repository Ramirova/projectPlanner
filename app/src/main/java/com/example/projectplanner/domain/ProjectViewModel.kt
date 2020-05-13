package com.example.projectplanner.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.projectplanner.data.db.ProjectPlannerDatabase
import com.example.projectplanner.data.db.ProjectPlannerRepository
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

class ProjectViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectPlannerRepository
    val allProjects: LiveData<List<Project>>
    val allTasks: LiveData<List<Task>>

    init {
        val projectPlannerDao = ProjectPlannerDatabase.getDatabase(application, viewModelScope).projectPlannerDao()
        repository = ProjectPlannerRepository(projectPlannerDao)
        allProjects = repository.allProjects
        allTasks = repository.allTasks
    }

    suspend fun getTasksForProject(project: Project): LiveData<List<Task>> {
        return repository.getTasksForProject(project)
    }

    suspend fun getTasksForProject(projectId: Long): LiveData<List<Task>> {
        return repository.getTasksForProject(projectId)
    }

}