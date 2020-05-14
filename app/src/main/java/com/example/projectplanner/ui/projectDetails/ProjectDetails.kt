package com.example.projectplanner.ui.projectDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectplanner.ProjectPlannerApplication
import com.example.projectplanner.R
import com.example.projectplanner.domain.ProjectViewModel
import javax.inject.Inject

const val PROJECT_ID = "project_id"

class ProjectDetails : AppCompatActivity() {

    @Inject
    lateinit var projectViewModel: ProjectViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TasksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details)

        (application as ProjectPlannerApplication).appComponent.inject(this)

        recyclerView = findViewById(R.id.recycler_view)

        adapter = TasksListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val id = intent.getLongExtra(PROJECT_ID, 0)

        subscribeTitleInfo(id)
        subscribeTasksRecyclerView(id)
    }

    private fun subscribeTitleInfo(projectId: Long) {
        val projectName: TextView = findViewById(R.id.project_view_title)
        val projectDescription: TextView = findViewById(R.id.project_description_text)

        projectViewModel.getProject(projectId).observe(this, Observer { project ->
            if (project != null) {
                projectName.text = project.projectTitle
                projectDescription.text = project.projectDescription
            }
        })
    }

    private fun subscribeTasksRecyclerView(projectId: Long) {
        projectViewModel.getTasksForProject(projectId).observe(this, Observer { tasks ->
            if (tasks != null) {
                adapter.setTasks(tasks)
            }
        })
    }
}
