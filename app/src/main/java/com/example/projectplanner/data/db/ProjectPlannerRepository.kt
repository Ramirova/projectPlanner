package com.example.projectplanner.data.db

import androidx.lifecycle.LiveData
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import java.util.*

class ProjectPlannerRepository(private val projectPlannerDao: ProjectPlannerDao) {

    val allProjects = projectPlannerDao.getProjects()
    val allTasks = projectPlannerDao.getAllTasks()

    fun insertProject(project: Project): Long {
        return projectPlannerDao.insertProject(project)
    }

    fun insertTask(task: Task): Long {
        return projectPlannerDao.insertTask(task)
    }

    fun deleteTask(task: Task) {
        projectPlannerDao.deleteTask(task)
    }

    fun deleteProject(Project: Project) {
        projectPlannerDao.deleteProject(Project)
    }

    fun getProject(projectId: Long): LiveData<Project> {
        return projectPlannerDao.getProjectById(projectId)
    }

    fun getTask(taskId: Long): LiveData<Task> {
        return projectPlannerDao.getTaskById(taskId)
    }

    fun getTasksForProject(project: Project, includeArchived: Boolean = false): LiveData<List<Task>> {
        return if (includeArchived)
            projectPlannerDao.getAllTasksForProjectIncludingArchived(project.projectId)
        else
            projectPlannerDao.getAllTasksForProject(project.projectId)
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
}