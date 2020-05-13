package com.example.projectplanner.data.db

import androidx.annotation.WorkerThread
import com.example.projectplanner.data.db.models.Project

class ProjectPlannerRepository(private val projectPlannerDao: ProjectPlannerDao) {

    val allProjects: List<Project> = projectPlannerDao.getProjects()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(project: Project) {
        projectPlannerDao.createProject(project)
    }
}