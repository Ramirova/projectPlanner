package com.example.projectplanner.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectplanner.data.db.ProjectPlannerDatabase
import com.example.projectplanner.data.db.ProjectPlannerRepository
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

class ProjectViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectPlannerRepository
    val allProjects: LiveData<List<Project>>
    val allTasks: LiveData<List<Task>>

    var selectedMonth: Int = 0
    var selectedTasks: LiveData<List<Task>> = MutableLiveData<List<Task>>(null)

    init {
        val projectPlannerDao =
            ProjectPlannerDatabase.getDatabase(application, viewModelScope).projectPlannerDao()
        repository = ProjectPlannerRepository(projectPlannerDao)
        allProjects = repository.allProjects
        allTasks = repository.allTasks
        runBlocking {
            selectedTasks = repository.getTasksBetweenDates(
                Date(120, 0, 1),
                Date(120, 1, 1)
            )
        }
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

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            repository.deleteProject(project)
        }
    }

    suspend fun selectMonth(m: Int) {
        selectedMonth = m
        // Yes, this only works for 2020. Too bad!
        selectedTasks = repository.getTasksBetweenDates(
            Date(120, m, 1),
            Date(120, m + 1, 1)
        )
    }

    suspend fun getProject(projectId: Long): LiveData<Project> {
        return repository.getProject(projectId)
    }

    suspend fun getTasksForProject(project: Project): LiveData<List<Task>> {
        return repository.getTasksForProject(project)
    }

    suspend fun getTasksForProject(projectId: Long): LiveData<List<Task>> {
        return repository.getTasksForProject(projectId)
    }

}