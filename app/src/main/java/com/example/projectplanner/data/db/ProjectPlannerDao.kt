package com.example.projectplanner.data.db

import androidx.room.*
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task

@Dao
interface ProjectPlannerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createProject(project: Project): Long

    @Update
    fun updateProject(project: Project)

    @Query("SELECT * FROM project")
    fun getProjects(): List<Project>

    @Query("SELECT * FROM project WHERE projectId = :projectId")
    fun getProjectById(projectId: Long): Project

    @Query("DELETE FROM project")
    fun deleteAllProjects()

    @Delete
    fun deleteProject(project: Project)

    // TASKS

    @Insert
    fun createTask(task: Task): Long

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM task WHERE parentProjectId = :projectId AND task_archived = 0")
    fun fetchAllTasksForProject(projectId: Long): List<Task>

    @Query("SELECT * FROM task WHERE parentProjectId = :projectId")
    fun fetchAllTasksForProjectIncludingUnarchived(projectId: Long): List<Task>

    @Query("SELECT * FROM task WHERE taskId = :taskId")
    fun getTaskById(taskId: Long): Task


    @Query("UPDATE task SET task_archived = 1 WHERE taskId = :taskId")
    fun archiveTask(taskId: Long)

    @Query("UPDATE task SET task_archived = 0 WHERE taskId = :taskId")
    fun unarchiveTask(taskId: Long)

    @Delete
    fun deleteTask(task: Task)

    @Transaction
    @Query("DELETE FROM task WHERE parentProjectId = :projectId")
    fun deleteAllTaskForProject(projectId: Long)
}