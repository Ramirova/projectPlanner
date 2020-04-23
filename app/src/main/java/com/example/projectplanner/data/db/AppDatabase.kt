package com.example.projectplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.projectplanner.data.db.models.Converters
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task

@Database(entities = [Project::class, Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectPlannerDao(): ProjectPlannerDao
}