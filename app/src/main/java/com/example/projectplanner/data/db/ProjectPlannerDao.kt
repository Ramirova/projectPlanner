package com.example.projectplanner.data.db

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import java.sql.Date

class ProjectPlannerDao {

    fun createProject(): Project {
        var note = Project("Новый проект", Date(0), Date(0), ArrayList<Task>())
        note.save()
        return note
    }

    fun saveProject(project: Project) = project.save()

    fun loadAllProjects() = Select().from(Project::class.java).execute<Project>()

    fun getProjectById(projectId: Long) = Select().from(Project::class.java).where("id = ?", projectId).executeSingle<Project>()

    fun deleteAllProjects() {
        Delete().from(Project::class.java).execute<Project>();
    }

    fun deleteProject(project: Project) {
        project.delete()
    }

    //Same for task...
}