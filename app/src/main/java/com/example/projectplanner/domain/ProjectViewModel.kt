package com.example.projectplanner.domain

import android.app.Application
import androidx.lifecycle.*
import com.example.projectplanner.data.db.ProjectPlannerDatabase
import com.example.projectplanner.data.db.ProjectPlannerRepository
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ProjectViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectPlannerRepository
    val allProjects: LiveData<List<Project>>
    val allTasks: LiveData<List<Task>>

    var selectedMonth: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var selectedTasks: LiveData<List<Task>>

    init {
        val projectPlannerDao =
            ProjectPlannerDatabase.getDatabase(application, viewModelScope).projectPlannerDao()
        repository = ProjectPlannerRepository(projectPlannerDao)
        allProjects = repository.allProjects
        allTasks = repository.allTasks

        selectedTasks = Transformations.switchMap(selectedMonth) { m ->
            repository.getTasksBetweenDates(
                // Yes, this only works for 2020. Too bad!
                Date(120, m, 1),
                Date(120, m+1, 1)
            )
        }
    }

    fun insertProject(project: Project) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertProject(project)
    }

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTask(task)
    }

    fun getTaskParentProjectWithoutLiveDataBullshit(task: Task): Project {
        return repository.getTaskParentProjectWithoutLiveDataBullshit(task)
    }

    fun getTaskByIdWithoutLiveDataBullshit(taskId: Long): Task {
        return repository.getTaskByIdWithoutLiveDataBullshit(taskId)
    }

    fun selectMonth(m: Int) {
        selectedMonth.postValue(m)
    }

    fun getProject(projectId: Long): LiveData<Project> {
        return repository.getProject(projectId)
    }

    fun getTasksForProject(projectId: Long): LiveData<List<Task>> {
        return repository.getTasksForProject(projectId)
    }
}