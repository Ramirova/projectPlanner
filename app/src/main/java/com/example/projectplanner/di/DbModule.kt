package com.example.projectplanner.di

import android.app.Application
import androidx.room.Room
import com.example.projectplanner.data.db.AppDatabase
import com.example.projectplanner.data.db.ProjectPlannerDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) : AppDatabase {
        return Room.databaseBuilder(
            application, AppDatabase::class.java, "ProjectPlanner.db")
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    internal fun provideDao(appDatabase: AppDatabase): ProjectPlannerDao {
        return appDatabase.projectPlannerDao()
    }

}