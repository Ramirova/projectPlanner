package com.example.projectplanner.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.projectplanner.data.db.models.Converters
import com.example.projectplanner.data.db.models.Project
import com.example.projectplanner.data.db.models.Task
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Project::class, Task::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ProjectPlannerDatabase : RoomDatabase() {
    abstract fun projectPlannerDao(): ProjectPlannerDao

    companion object {
        @Volatile
        private var INSTANCE: ProjectPlannerDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ProjectPlannerDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {

                    val instance = Room.databaseBuilder(
                        context.applicationContext, ProjectPlannerDatabase::class.java, "ProjectPlanner.db")
                        .allowMainThreadQueries().build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
        }
    }
}