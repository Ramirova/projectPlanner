package com.example.projectplanner.data.db.models

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "project")
data class Project (

    @PrimaryKey val projectId: Long,

    @ColumnInfo(name = "project_title")
    val projectTitle: String,

    @ColumnInfo(name = "project_desc")
    val projectDescription: String?,

    @ColumnInfo(name = "project_start_date")
    val projectStartDate: Date,

    @ColumnInfo(name = "project_end_date")
    val projectEndDate: Date,

    @ColumnInfo(name = "project_color")
    val projectColor: Color

)