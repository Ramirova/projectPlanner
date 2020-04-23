package com.example.projectplanner.di

import com.example.projectplanner.ui.grid.GridPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DbModule::class])
interface AppComponent {

    fun inject(gridPresenter : GridPresenter)

}