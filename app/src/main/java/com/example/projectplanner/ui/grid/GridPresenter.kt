package com.example.projectplanner.ui.grid

import android.app.Application
import android.widget.GridView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.projectplanner.ProjectPlannerApplication
import com.example.projectplanner.data.db.ProjectPlannerDao
import com.example.projectplanner.domain.ProjectViewModel
import javax.inject.Inject

@InjectViewState
class GridPresenter: MvpPresenter<GridMvpView>() {

    @Inject lateinit var projectViewModel: ProjectViewModel

    init {
        projectViewModel.getApplication<ProjectPlannerApplication>().appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAllProjects()
    }
    
    fun loadAllProjects() {
        val projectList = projectViewModel.allProjects
        viewState.onProjectsLoaded(projectList)
    }
}