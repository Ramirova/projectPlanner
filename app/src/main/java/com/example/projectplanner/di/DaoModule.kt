package com.example.projectplanner.di

import com.example.projectplanner.data.db.ProjectPlannerDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {

    @Provides
    @Singleton
    fun provideDao() : ProjectPlannerDao =
        ProjectPlannerDao()

}