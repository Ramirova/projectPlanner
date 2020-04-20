package com.example.projectplanner.ui.grid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.projectplanner.R
import com.example.projectplanner.data.db.models.Project

class GridActivity : AppCompatActivity(), GridMvpView {

    @InjectPresenter
    lateinit var presenter: GridPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
    }

    override fun onProjectsLoaded(notes: List<Project>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProjectDeleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProjectDeleteDialog(project: Project) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProjectDeleteDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAddProjectDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideAddProjectDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}