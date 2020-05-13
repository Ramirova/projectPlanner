package com.example.projectplanner.data.db.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "task")
data class Task (

    @PrimaryKey(autoGenerate = true)
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

) : Parcelable