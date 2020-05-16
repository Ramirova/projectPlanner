package com.example.projectplanner.data.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import java.util.*

class ProjectPlannerRepository(private val projectPlannerDao: ProjectPlannerDao) {

    val allProjects = projectPlannerDao.getProjects()
    val allTasks = projectPlannerDao.getAllTasks()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertProject(project: Project): Long {
        return projectPlannerDao.insertProject(project)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTask(task: Task): Long {
        return projectPlannerDao.insertTask(task)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteTask(task: Task) {
        projectPlannerDao.deleteTask(task)
    }

    fun getProject(projectId: Long): LiveData<Project> {
        return projectPlannerDao.getProjectById(projectId)
    }

    fun getTasksForProject(projectId: Long, includeArchived: Boolean = false): LiveData<List<Task>> {
        return if (includeArchived)
            projectPlannerDao.getAllTasksForProjectIncludingArchived(projectId)
        else
            projectPlannerDao.getAllTasksForProject(projectId)
    }

    fun getTasksBetweenDates(from: Date, to: Date): LiveData<List<Task>> {
        return projectPlannerDao.getTasksBetweenDates(from.time, to.time)
    }

    fun getTaskParentProjectWithoutLiveDataBullshit(task: Task): Project {
        return projectPlannerDao.getProjectByIdWithoutLiveDataBullshit(task.parentProjectId)
    }
    
    fun getTaskByIdWithoutLiveDataBullshit(taskId: Long): Task {
        return projectPlannerDao.getTaskByIdWithoutLiveDataBullshit(taskId)
    }
}