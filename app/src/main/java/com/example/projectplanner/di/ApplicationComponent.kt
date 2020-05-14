package com.example.projectplanner.di

import com.example.projectplanner.ui.grid.CreateProjectActivity
import com.example.projectplanner.ui.grid.MainActivity
import com.example.projectplanner.ui.project.TaskActivity
import com.example.projectplanner.ui.projectDetails.ProjectDetails
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(gridPresenter : MainActivity)
    fun inject(createProject : CreateProjectActivity)
    fun inject(taskActivity: TaskActivity)
    fun inject(taskActivity: ProjectDetails)
}