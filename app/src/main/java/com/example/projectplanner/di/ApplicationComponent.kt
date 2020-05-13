package com.example.projectplanner.di

import com.example.projectplanner.ui.grid.CreateProjectActivity
import com.example.projectplanner.ui.grid.MainActivity
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(gridPresenter : MainActivity)
    fun inject(createProject : CreateProjectActivity)
}