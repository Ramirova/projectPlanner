package com.example.projectplanner.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.projectplanner.data.db.ProjectPlannerDatabase
import com.example.projectplanner.data.db.ProjectPlannerRepository
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    fun insertProject(project: Project) {
        viewModelScope.launch {
            repository.insertProject(project)
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    suspend fun getTasksForProject(project: Project): LiveData<List<Task>> {

        //  TODO: this probably shouldn't be suspended!

        return repository.getTasksForProject(project)

    }

    suspend fun getTasksForProject(projectId: Long): LiveData<List<Task>> {
        return repository.getTasksForProject(projectId)
    }

}