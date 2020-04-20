package com.example.projectplanner.ui.grid

import android.app.Application
import android.widget.GridView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.projectplanner.ProjectPlannerApplication
import com.example.projectplanner.data.db.ProjectPlannerDao
import javax.inject.Inject

@InjectViewState
class GridPresenter: MvpPresenter<GridMvpView>() {

    @Inject
    lateinit var projectPlannerDao: ProjectPlannerDao

    init {
        ProjectPlannerApplication.graph.inject(this)
    }

}