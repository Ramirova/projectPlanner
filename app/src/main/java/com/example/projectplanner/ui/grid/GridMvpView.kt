package com.example.projectplanner.ui.grid

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.projectplanner.data.db.models.Project

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface GridMvpView : MvpView {

    fun onProjectsLoaded(notes: List<Project>)

    fun updateView()

    fun onProjectDeleted()

    fun showProjectDeleteDialog(project: Project)

    fun hideProjectDeleteDialog()

    fun showAddProjectDialog()

    fun hideAddProjectDialog()
}