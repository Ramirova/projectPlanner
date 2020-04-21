package com.example.projectplanner.data.db

import android.graphics.Color
import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import java.sql.Date

class ProjectPlannerDao {

    fun createProject(): Project {
        var note = Project("Новый проект", Date(0), Date(0), ArrayList<Task>(), Color())
        note.save()
        return note
    }

    fun saveProject(project: Project) = project.save()

    fun loadAllProjects() = Select().from(Project::class.java).execute<Project>() //better fetch

    fun getProjectById(projectId: Long) = Select().from(Project::class.java).where("id = ?", projectId).executeSingle<Project>()

    fun deleteAllProjects() {
        Delete().from(Project::class.java).execute<Project>();
    }

    fun deleteProject(project: Project) {
        project.delete()
    }

    fun updateProject() {}

    // TASKS

    fun createTask(projectId: Long): Task {

        return Task()
    }

    fun updateTask(task: Task) {

    }

    fun fetchAllTasksForProject(projectID: String, addArchived: Boolean): ArrayList<Task> {
        return ArrayList(0)
    }

    fun getTaskById(taskID: Long) = Select().from(Project::class.java).where("id = ?", taskID).executeSingle<Task>()

    fun deleteAllTaskForProject() {
        Delete().from(Project::class.java).execute<Project>();
    }

    fun deleteTask(taskId: Long) {

    }

    fun archiveTask(taskId: Long) {}

    fun unarchiveTask(taskId: Long) {}
}