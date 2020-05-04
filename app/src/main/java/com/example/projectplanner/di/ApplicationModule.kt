package com.example.projectplanner.di

import android.content.Context
import com.example.projectplanner.ProjectPlannerApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun provideApplication() = ProjectPlannerApplication.APPLICATION

    @Provides
    fun provideContext(): Context = ProjectPlannerApplication.APPLICATION
}