package com.example.projectplanner

import com.example.projectplanner.data.db.DaoModule
import com.example.projectplanner.ui.grid.GridPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DaoModule::class))
interface AppComponent {

    fun inject(gridPresenter : GridPresenter)

}