package com.example.projectplanner.di

import com.example.projectplanner.ui.grid.GridPresenter
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(gridPresenter : GridPresenter)
}