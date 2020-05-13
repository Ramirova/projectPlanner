package com.example.projectplanner.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "task")
data class Task (

    @PrimaryKey
    val taskId: Long,

    // Room doesn't support direct object references here
    @ColumnInfo(name = "parentProjectId")
    val parentProjectId: Long,

    @ColumnInfo(name = "task_title")
    val taskTitle: String,

    @ColumnInfo(name = "task_desc")
    val taskDescription: String?,

    @ColumnInfo(name = "task_start_date")
    val taskStartDate: Date,

    @ColumnInfo(name = "task_end_date")
    val taskEndDate: Date,

    @ColumnInfo(name = "task_priority")
    val taskPriority: Int?,

    @ColumnInfo(name = "task_archived")
    val taskArchived: Boolean

)