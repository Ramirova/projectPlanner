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
    
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteProject(Project: Project) {
        projectPlannerDao.deleteProject(Project)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getProject(projectId: Long): LiveData<Project> {
        return projectPlannerDao.getProjectById(projectId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getTask(taskId: Long): LiveData<Task> {
        return projectPlannerDao.getTaskById(taskId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getTasksForProject(project: Project, includeArchived: Boolean = false): LiveData<List<Task>> {
        return if (includeArchived)
            projectPlannerDao.getAllTasksForProjectIncludingArchived(project.projectId)
        else
            projectPlannerDao.getAllTasksForProject(project.projectId)

    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getTasksForProject(projectId: Long, includeArchived: Boolean = false): LiveData<List<Task>> {
        return if (includeArchived)
            projectPlannerDao.getAllTasksForProjectIncludingArchived(projectId)
        else
            projectPlannerDao.getAllTasksForProject(projectId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getTasksBetweenDates(from: Date, to: Date): LiveData<List<Task>> {
        return projectPlannerDao.getTasksBetweenDates(from.time, to.time)
    }
}