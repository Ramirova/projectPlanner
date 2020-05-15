package com.example.projectplanner.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task

@Dao
interface ProjectPlannerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProject(project: Project): Long

    @Query("SELECT * FROM project")
    fun getProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM project WHERE projectId = :projectId")
    fun getProjectById(projectId: Long): LiveData<Project>

    @Query("DELETE FROM project")
    fun deleteAllProjects()

    @Delete
    fun deleteProject(project: Project)

    // TASKS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task): Long

    @Query("SELECT * FROM task")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE taskId = :taskId")
    fun getTaskById(taskId: Long): LiveData<Task>

    @Query("SELECT * FROM task WHERE parentProjectId = :projectId AND task_archived = 0")
    fun getAllTasksForProject(projectId: Long): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE parentProjectId = :projectId")
    fun getAllTasksForProjectIncludingArchived(projectId: Long): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE task_start_date BETWEEN :from AND :to")
    fun getTasksBetweenDates(from: Long, to: Long): LiveData<List<Task>>

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