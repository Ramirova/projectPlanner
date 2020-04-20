package com.example.projectplanner.data.db.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import java.sql.Date

class Project: Model {

    @Column(name = "title")
    public var title: String? = null

    @Column(name = "text")
    public var text: String? = null

    @Column(name = "start_date")
    public var startDate: Date? = null

    @Column(name = "end_date")
    public var endDate: Date? = null

    @Column(name = "tasks")
    public var tasks: ArrayList<Task>? = null

    constructor(title: String, startDate: Date, endDate: Date, tasks: ArrayList<Task>?) {
        this.title = title
        this.startDate = startDate
        this.endDate = endDate
        this.tasks = tasks
    }

    constructor()
}