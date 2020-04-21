package com.example.projectplanner.data.db.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import java.sql.Date

class Task: Model {

    @Column(name = "title")
    public var title: String? = null

    @Column(name = "text")
    public var text: String? = null

    @Column(name = "start_date")
    public var startDate: Date? = null

    @Column(name = "end_date")
    public var endDate: Date? = null

    @Column(name = "project")
    public var project: Project? = null

    @Column(name = "priority")
    public var priority: Int? = null

    @Column(name = "archived")
    public var archived: Boolean? = null

    constructor(title: String, startDate: Date, endDate: Date, project: Project, priority: Int, archived: Boolean) {
        this.title = title
        this.startDate = startDate
        this.endDate = endDate
        this.project = project
        this.priority = priority
        this.archived = archived
    }

    constructor()
}