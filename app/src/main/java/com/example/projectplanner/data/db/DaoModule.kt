package com.example.projectplanner.data.db

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {

    @Provides
    @Singleton
    fun provideDao() : ProjectPlannerDao = ProjectPlannerDao()

}